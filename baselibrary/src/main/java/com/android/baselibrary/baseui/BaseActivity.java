package com.android.baselibrary.baseui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baselibrary.R;
import com.android.baselibrary.base.BaseAppCompatActivity;
import com.android.baselibrary.basedata.EventBusData;

import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends
    BaseAppCompatActivity implements IBaseView {

    private Toast toast;
    protected Dialog loadingPop;

    protected T mPresenter;

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mPresenter) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    protected void initPresenter() {
        //创建presenter
        mPresenter = createPresenter();
    }

    /**
     * 创建Presenter 对象
     */
    protected abstract T createPresenter();

    @Override
    protected void onNavigateClick() {
        if (hasTitleBar()) {
            ImageButton backView = findViewById(R.id.actionbar_back);
            if (backView != null) {
                backView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }
    }

    @Override
    protected void setCustomTitle(CharSequence title) {
        if (hasTitleBar()) {
            TextView titleView = findViewById(R.id.title_tv_message);
            if (titleView != null) {
                titleView.setText(title);
                setTitle("");
            }
        }
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (hasTitleBar()) {
            mImmersionBar.titleBar(R.id.title_bar).init();
        } else {
            if (!isStatusBarOverlap()) {
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.colorWhite).init();
            }
        }
    }

    @Override
    protected boolean hasTitleBar() {
        return findViewById(R.id.title_bar) != null;
    }

    @Override
    protected boolean isOverridePendingTransition() {
        return false;
    }

    public synchronized void hideLoading() {
        if (loadingPop != null) {
            loadingPop.dismiss();
        }
    }

    public synchronized void showLoading() {
        if (loadingPop == null) {
            loadingPop = new Dialog(this, R.style.NoTitleDialogStyle);
            loadingPop.setContentView(R.layout.popup_loading);
            loadingPop.setCanceledOnTouchOutside(false);
        }
        loadingPop.show();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onDestroy() {
        if (loadingPop != null && loadingPop.isShowing()) {
            loadingPop.dismiss();
        }
        toast = null;

        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    @Override
    protected void onPause() {
        if (toast != null) {
            toast.cancel();
        }
        hideLoading();
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
        super.onPause();
    }

    @Override
    protected TransitionMode getTransitionMode() {
        return TransitionMode.NONE;
    }

    @Override
    public void showToast(String msg) {
        if (null != msg) {
            if (toast == null) {
                toast = new Toast(this);
                @SuppressLint("InflateParams") View view =
                    getLayoutInflater().inflate(R.layout.toast_view, null, true);
                TextView messageTv = view.findViewById(R.id.message_tv);
                messageTv.setText(msg);
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                TextView messageTv = toast.getView().findViewById(R.id.message_tv);
                messageTv.setText(msg);
            }
            toast.show();
        }
    }

    @Override
    protected void eventBusPost(EventBusData data) {
        EventBus.getDefault().post(data);
    }

    public void showToast(int msg) {
        showToast(String.valueOf(msg));
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void showEmpty(String msg) {

    }

    @Override
    public void showError(String msg) {
        showToast(msg);
    }
}
