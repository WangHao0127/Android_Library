package com.android.baselibrary.weight.picker.util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.baselibrary.R;
import com.android.baselibrary.weight.picker.builder.TimePickerBuilder;
import com.android.baselibrary.weight.picker.listener.OnTimeSelectChangeListener;
import com.android.baselibrary.weight.picker.listener.OnTimeSelectListener;
import com.android.baselibrary.weight.picker.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: WangHao
 * Created On: 2018/10/23 0023 16:31
 * Description:
 */
public class PickerUtils {

    public static TimePickerView showTimePicker(final Context context) {
        TimePickerView pickerView = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(context, getTime(date), Toast.LENGTH_SHORT).show();
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
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

    private static String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
