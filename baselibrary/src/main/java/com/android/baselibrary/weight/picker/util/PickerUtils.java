package com.android.baselibrary.weight.picker.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.baselibrary.R;
import com.android.baselibrary.weight.picker.builder.OptionsPickerBuilder;
import com.android.baselibrary.weight.picker.builder.TimePickerBuilder;
import com.android.baselibrary.weight.picker.entity.JsonBean;
import com.android.baselibrary.weight.picker.listener.OnOptionsSelectListener;
import com.android.baselibrary.weight.picker.listener.OnTimeSelectChangeListener;
import com.android.baselibrary.weight.picker.listener.OnTimeSelectListener;
import com.android.baselibrary.weight.picker.view.OptionsPickerView;
import com.android.baselibrary.weight.picker.view.TimePickerView;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;

/**
 * Author: WangHao
 * Created On: 2018/10/23 0023 16:31
 * Description:
 */
public class PickerUtils {

    public static TimePickerView showTimePicker(final Context context,
        OnTimeSelectListener listener) {
        TimePickerView pickerView = new TimePickerBuilder(context, listener)
            .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                @Override
                public void onTimeSelectChanged(Date date) {

                }
            })
            .setType(new boolean[]{true, true, true, false, false, false})
            .isDialog(true)
            .build(); //默认设置false ，内部实现将DecorView 作为它的父控件。
        Dialog mDialog = pickerView.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pickerView.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }

        return pickerView;
    }

    public static OptionsPickerView showPickerView(Context context,
        OnOptionsSelectListener listener) {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, listener)

            .setTitleText("城市选择")
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(20)
            .isDialog(true)
            .build();

        Dialog mDialog = pvOptions.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvOptions.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(
                    com.android.baselibrary.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//                pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        //        pvOptions.show();
        return pvOptions;
    }



    public static ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
