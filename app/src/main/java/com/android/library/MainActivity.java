package com.android.library;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.baselibrary.basedata.EventBusData;
import com.android.baselibrary.baseui.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

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
    }

    @OnClick({R.id.btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                go(FullActivity.class);
                break;
        }
    }

}
