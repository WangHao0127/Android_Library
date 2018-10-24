package com.android.library.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.baselibrary.baseui.BaseActivity;
import com.android.library.R;
import com.android.library.utils.CProgressDialogUtils;
import com.android.library.utils.CustomUpdateParser;
import com.android.library.utils.CustomUpdatePrompter;
import com.android.library.utils.HProgressDialogUtils;

import java.io.File;

import butterknife.OnClick;
import com.blankj.utilcode.util.ToastUtils;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateChecker;
import com.xuexiang.xupdate.service.OnFileDownloadListener;

public class XUpdateActivity extends BaseActivity {

    private String mUpdateUrl = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_test.json";

    private String mUpdateUrl2 = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_forced.json";

    private String mUpdateUrl3 = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_custom.json";

    private String mDownloadUrl = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/apk/xupdate_demo_1.0.2.apk";

    private String mUpdateUrl4 =  "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_test.json";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_xupdate;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                XUpdate.newBuild(this)
                    .updateUrl(mUpdateUrl)
                    .update();
                break;
            case R.id.btn2:
                XUpdate.newBuild(this)
                    .updateUrl(mUpdateUrl)
                    .isAutoMode(true) //如果需要完全无人干预，自动更新，需要root权限【静默安装需要】
                    .update();
                break;
            case R.id.btn3:
                XUpdate.newBuild(this)
                    .updateUrl(mUpdateUrl2)
                    .update();
                break;
            case R.id.btn4:
                XUpdate.newBuild(this)
                    .updateUrl(mUpdateUrl4)
                    .themeColor(getResources().getColor(R.color.update_theme_color))
                    .topResId(R.mipmap.bg_update_top)
                    .update();
                break;
            case R.id.btn5:
                XUpdate.newBuild(this)
                    .updateUrl(mUpdateUrl3)
                    .updateParser(new CustomUpdateParser())
                    .update();
                break;
            case R.id.btn6:
                XUpdate.newBuild(this)
                    .updateUrl(mUpdateUrl3)
                    .updateChecker(new DefaultUpdateChecker() {
                        @Override
                        public void onBeforeCheck() {
                            super.onBeforeCheck();
                            CProgressDialogUtils.showProgressDialog(XUpdateActivity.this, "查询中...");
                        }
                        @Override
                        public void onAfterCheck() {
                            super.onAfterCheck();
                            CProgressDialogUtils.cancelProgressDialog(XUpdateActivity.this);
                        }
                    })
                    .updateParser(new CustomUpdateParser())
                    .updatePrompter(new CustomUpdatePrompter(XUpdateActivity.this))
                    .update();
                break;
            case R.id.btn7:
                XUpdate.newBuild(this)
                    .apkCacheDir(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .getAbsolutePath())
                    .build()
                    .download(mDownloadUrl, new OnFileDownloadListener() {
                        @Override
                        public void onStart() {
                            HProgressDialogUtils
                                .showHorizontalProgressDialog(XUpdateActivity.this, "下载进度", false);
                        }

                        @Override
                        public void onProgress(float progress, long total) {
                            HProgressDialogUtils.setProgress(Math.round(progress * 100));
                        }

                        @Override
                        public boolean onCompleted(File file) {
                            HProgressDialogUtils.cancel();
                            ToastUtils.showShort("apk下载完毕，文件路径：" + file.getPath());
                            return false;
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            HProgressDialogUtils.cancel();
                        }
                    });
                break;
        }
    }

}
