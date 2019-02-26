package com.android.library.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.baselibrary.baseui.BaseActivity;
import com.android.baselibrary.baseui.BasePresenter;
import com.android.baselibrary.weight.picker.entity.JsonBean;
import com.android.baselibrary.weight.picker.listener.OnOptionsSelectListener;
import com.android.baselibrary.weight.picker.listener.OnTimeSelectListener;
import com.android.baselibrary.weight.picker.util.GetJsonDataUtil;
import com.android.baselibrary.weight.picker.util.PickerUtils;
import com.android.baselibrary.weight.picker.view.OptionsPickerView;
import com.android.baselibrary.weight.picker.view.TimePickerView;
import com.android.library.R;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.android.baselibrary.weight.picker.util.PickerUtils.parseData;

public class PickerActivity extends BaseActivity {

    @BindView(R.id.btn2)
    Button btn2;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private boolean isLoaded = false;

    private TimePickerView mTimePickerView;
    private OptionsPickerView mOptionsPickerView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_picker;
    }

    @Override
    protected void initViewsAndEvents() {
        mTimePickerView = PickerUtils.showTimePicker(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(PickerActivity.this, PickerUtils.getTime(date), Toast.LENGTH_SHORT)
                    .show();
            }
        });

        //子线程解析省市区的json数据
        Observable
            .create((ObservableOnSubscribe<Boolean>) emitter -> emitter
                .onNext(initJsonData()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean aBoolean) {
                    isLoaded = true;
                    Toast.makeText(PickerActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();

                    mOptionsPickerView = PickerUtils
                        .showPickerView(PickerActivity.this, new OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int options2, int options3,
                                View v) {
                                //返回的分别是三个级别的选中位置
                                String tx = options1Items.get(options1).getPickerViewText() +
                                            options2Items.get(options1).get(options2) +
                                            options3Items.get(options1).get(options2).get(options3);

                                Toast.makeText(PickerActivity.this, tx, Toast.LENGTH_SHORT).show();
                            }
                        });
                    mOptionsPickerView
                        .setPicker(options1Items, options2Items, options3Items);//三级选择器
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(PickerActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
    }

    @OnClick({R.id.btn1, R.id.btn2})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                if (isLoaded) {
                    mOptionsPickerView.show(v);
                } else {
                    Toast.makeText(PickerActivity.this, "Please waiting until the data is parsed",
                        Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn2:
                mTimePickerView.show(v);
                break;
        }
    }

    public boolean initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData =
            new GetJsonDataUtil()
                .getJson(PickerActivity.this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                    || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        return true;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
