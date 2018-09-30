package com.android.baselibrary.baseutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/9/27
 *     desc  : 崩溃相关工具类
 * </pre>
 */
public class MyCrashUtils implements UncaughtExceptionHandler {

    // App根目录
    public static final String APP_PATH_ROOT = FileUtil.getRootPath().getAbsolutePath()
                                               + File.separator
                                               + "AndroidLibrary"
                                               + File.separator;
    // 默认存放异常日志文件的路径
    public final static String DEFAULT_CRASH_FILE_PATH = APP_PATH_ROOT + "crash" + File.separator;

    private final String TAG = "MyCrashUtils";

    private Context context;

    private volatile static MyCrashUtils mInstance;

    private UncaughtExceptionHandler mHandler;

    private boolean mInitialized;

    private String versionName;

    private int versionCode;

    private final int CRASHLOG_KEEPDAY = 5;  // 错误日志的保留最长天数
    /**
     * yyyy-MM-dd HH:mm:ss字符串
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd字符串
     */
    public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss格式
     */
    public static final ThreadLocal<SimpleDateFormat>
        defaultDateTimeFormat = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
        }
    };

    /**
     * yyyy-MM-dd格式
     */
    public static final ThreadLocal<SimpleDateFormat>
        defaultDateFormat = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DEFAULT_FORMAT_DATE);
        }
    };

    private MyCrashUtils() {
    }

    /**
     * 获取单例
     * <p>在Application中初始化{@code MyCrashUtils.getInstance().init(this);}</p>
     *
     * @return 单例
     */
    public static MyCrashUtils getInstance() {
        if (mInstance == null) {
            synchronized (MyCrashUtils.class) {
                if (mInstance == null) {
                    mInstance = new MyCrashUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init(Context context) {
        this.context = context;
        if (mInitialized) return true;
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

        // 定时清理过期的日志文件
        autoClear(CRASHLOG_KEEPDAY);

        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        if (throwable != null) {
            // 保存日志到本地
            try {
                saveCrashInfoFile(throwable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //===
        // if (URLUtil.isDebug) {
        // 调用系统来处理异常
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
        // }
        // 直接重新启动应用
//        restartApp(context,MainActivity.class);
    }

    /**
     * 重新启动app
     *
     * @param context
     */
    public static void restartApp(Context context,Class<? extends Activity> clazz) {
        System.exit(0);
        Intent intent = new Intent(context,clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead(String result) {
        String crashHead = "\n************* Crash Log Head ****************" +
                           "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                           "\nDevice Model       : " + Build.MODEL +// 设备型号
                           "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                           "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                           "\nApp VersionName    : " + versionName +
                           "\nApp VersionCode    : " + versionCode +
                           "\n************* Crash Log Head ****************\n\n";
        crashHead += result;
        LoggerUtil.e(TAG, "崩溃相关" + crashHead);
        return crashHead;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     * @throws Exception
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            // 带上设备信息
            sb.append(getCrashHead(result));
            sb.append(result);

            String fileName = writeFile(sb.toString());
            LoggerUtil.e(TAG, "fileName = " + fileName);
            return fileName;
        } catch (Exception e) {
            LoggerUtil.e(TAG, "an error occured while writing file..." + e);
            sb.append("an error occured while writing file...\r\n");
            writeFile(sb.toString());
        }
        return null;
    }

    /**
     * @param sb
     * @return
     * @throws Exception
     */
    private String writeFile(String sb) throws Exception {
        // 用于格式化日期,作为日志文件名的一部分
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(new Date());
        String fileName = "crash-" + time + ".log";

        if (FileUtil.hasSdcard()) {
            File dir = new File(DEFAULT_CRASH_FILE_PATH);
            if (!dir.exists())
                dir.mkdirs();

            FileOutputStream
                fos = new FileOutputStream(DEFAULT_CRASH_FILE_PATH + fileName, true);
            fos.write(sb.getBytes());
            fos.flush();
            fos.close();
        }
        return fileName;
    }

    /**
     * 文件删除
     *
     * @param autoClearDay 文件保存天数
     */
    public void autoClear(final int autoClearDay) {
        FileUtil.delete(DEFAULT_CRASH_FILE_PATH, new FilenameFilter() {
            @Override
            public boolean accept(File file, String filename) {
                LoggerUtil.i(TAG, "autoClear--filename = " + filename);
                String s = FileUtil.getFileNameWithoutExtension(filename);
                LoggerUtil.i(TAG, "autoClear--s = " + s);
                int day = autoClearDay < 0 ? autoClearDay : -1 * autoClearDay;
                LoggerUtil.i(TAG, "autoClear--day = " + day);
                String date = "crash-" + getOtherDay(day);
                LoggerUtil.i(TAG, "autoClear--date = " + date);
                return date.compareTo(s) >= 0;
            }
        });
    }

    /**
     * 获得几天之前或者几天之后的日期
     *
     * @param diff 差值：正的往后推，负的往前推
     * @return
     */
    public String getOtherDay(int diff) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, diff);
        return getDateFormat(mCalendar.getTime());
    }

    /**
     * 将date转成yyyy-MM-dd字符串<br>
     *
     * @param date Date对象
     * @return yyyy-MM-dd
     */
    public static String getDateFormat(Date date) {
        return dateSimpleFormat(date, defaultDateFormat.get());
    }

    /**
     * 将date转成字符串
     *
     * @param date   Date
     * @param format SimpleDateFormat
     *               <br>
     *               注： SimpleDateFormat为空时，采用默认的yyyy-MM-dd HH:mm:ss格式
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String dateSimpleFormat(Date date, SimpleDateFormat format) {
        if (format == null)
            format = defaultDateTimeFormat.get();
        return (date == null ? "" : format.format(date));
    }
}
