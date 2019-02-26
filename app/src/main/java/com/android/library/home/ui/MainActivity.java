package com.android.library.home.ui;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.baselibrary.basedata.EventBusData;
import com.android.baselibrary.baseui.BaseActivity;
import com.android.baselibrary.helper.DialogHelper;
import com.android.library.R;
import com.android.library.entity.WeatherData;
import com.android.library.home.contract.WeatherContract;
import com.android.library.home.presenter.WeatherPresenter;
import com.android.library.ui.FullActivity;
import com.android.library.ui.ImageActivity;
import com.android.library.ui.MapActivity;
import com.android.library.ui.PickerActivity;
import com.android.library.ui.XUpdateActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;

public class MainActivity extends BaseActivity<WeatherContract.WeatherView, WeatherPresenter>
    implements WeatherContract.WeatherView {

    @BindView(R.id.actionbar_back)
    ImageButton actionbarBack;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.title_tv_message)
    TextView titleTvMessage;
    @BindView(R.id.title_right)
    TextView titleRight;
    @BindView(R.id.title_iv_right)
    ImageButton titleIvRight;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setText(EventBusData data) {
        switch (data.getAction()) {
            case DELETE_ALL_MESSAGE_IN_SESSION:
                tv.setText((String) data.getData());
                break;
        }
    }

    @Override
    protected WeatherPresenter createPresenter() {
        return new WeatherPresenter();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {
        setCustomTitle("王舒铭");
        mPresenter.getWeatherInfo(this);
    }

    @OnClick({R.id.btn, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                PermissionUtils.permission(PermissionConstants.STORAGE)
                    .rationale(DialogHelper::showRationaleDialog)  /*用户之前点了拒绝之后的再次点击*/
                    /*用户点了同意之后*/
                    .callback(new PermissionUtils.FullCallback() {
                        @Override
                        public void onGranted(List<String> permissionsGranted) {
                            go(FullActivity.class);
                            LogUtils.d(permissionsGranted);
                        }

                        /*用户点了拒绝 ，或者点了rationale弹窗之后的取消*/
                        @Override
                        public void onDenied(List<String> permissionsDeniedForever,
                            List<String> permissionsDenied) {
                            if (!permissionsDeniedForever.isEmpty()) {
                                DialogHelper.showOpenAppSettingDialog();
                            }
                            LogUtils.d(permissionsDeniedForever, permissionsDenied);
                        }
                    })
                    .request();
                break;
            case R.id.btn2:
                go(PickerActivity.class);
                break;
            case R.id.btn3:
                go(XUpdateActivity.class);
                break;
            case R.id.btn4:
                go(ImageActivity.class);
                break;
            case R.id.btn5:
                go(MapActivity.class);
                break;
        }
    }

    @Override
    public void onGetSuccess(WeatherData weatherData) {
        showToast(weatherData.getWeatherinfo().getCity());
    }

    @Override
    public void onGetFail(String errorInfo) {
        showToast(errorInfo);
    }
}
