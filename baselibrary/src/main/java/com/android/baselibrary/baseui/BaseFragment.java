package com.android.baselibrary.baseui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.baselibrary.R;
import com.android.baselibrary.basedata.EventBusData;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.Objects;

import butterknife.ButterKnife;

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment implements IBaseView {

    protected String TAG = null;
    private static final int NON_CODE = -1;
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    protected float mScreenDensity;
    protected int mScreenHeight;
    protected int mScreenWidth;

    protected Context mContext;
    protected View rootView;
    private Toast mToast;
    protected Dialog loadingPop;

    protected boolean isVisible;

    protected T mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            getExtraArguments(getArguments());
        }
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
        TAG = this.getClass().getSimpleName();

        mPresenter = createPresenter();//创建presenter
    }

    /**
     * 创建Presenter对象
     */
    protected abstract T createPresenter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentViewLayout(), container, false);
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
        initViewsAndEvents();
    }

    protected void eventBusPost(EventBusData data) {
        EventBus.getDefault().post(data);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//实现懒加载
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onInvisible() {

    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {

    }

    protected abstract void initViewsAndEvents();

    protected boolean isBindEventBusHere() {
        return false;
    }

    protected abstract int getContentViewLayout();

    protected void getExtraArguments(Bundle arguments) {

    }

    protected void showToast(boolean bool) {
        showToast(String.format("%s", bool));
    }

    @SuppressLint("DefaultLocale")
    protected void showToast(int number) {
        showToast(String.format("%d", number));
    }


    @SuppressLint("ShowToast")
    public void showToast(String msg) {
        if (null != msg) {
            if (mToast == null) {
                mToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                mToast.setText(msg);
            }
            mToast.show();
        }
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

    /**
     * startActivity
     *
     * @param clazz target Activity
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void go(Class<? extends Activity> clazz) {
        _goActivity(clazz, null, NON_CODE, false);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz target Activity
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void go(Class<? extends Activity> clazz, Bundle bundle) {
        _goActivity(clazz, bundle, NON_CODE, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void goForResult(Class<? extends Activity> clazz, int requestCode) {
        _goActivity(clazz, null, requestCode, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void goForResult(Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
        _goActivity(clazz, bundle, requestCode, false);
    }

    /**
     * Activity 跳转
     *
     * @param clazz  目标activity
     * @param bundle 传递参数
     * @param finish 是否结束当前activity
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void _goActivity(Class<? extends Activity> clazz, Bundle bundle, int requestCode,
        boolean finish) {
        if (null == clazz) {
            throw new IllegalArgumentException("you must pass a target activity where to go.");
        }
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        if (requestCode > NON_CODE) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        if (finish) {
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mToast = null;
        hideLoading();
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mPresenter) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void onDestroy() {
        if (loadingPop != null && loadingPop.isShowing()) {
            loadingPop.dismiss();
        }
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    public synchronized void hideLoading() {
        if (loadingPop != null) {
            loadingPop.dismiss();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public synchronized void showLoading() {
        if (loadingPop == null) {
            loadingPop = new Dialog(Objects.requireNonNull(getActivity()), R.style.NoTitleDialogStyle);
            loadingPop.setContentView(R.layout.popup_loading);
            loadingPop.setCanceledOnTouchOutside(false);
        }
        loadingPop.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field mChildFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            mChildFragmentManager.setAccessible(true);
            mChildFragmentManager.set(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
