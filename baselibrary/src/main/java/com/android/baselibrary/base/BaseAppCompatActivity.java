package com.android.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.baselibrary.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import com.gyf.barlibrary.ImmersionBar;

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    public static final int NON_CODE = -1;

    protected String TAG = null;

    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    protected ImmersionBar mImmersionBar;
    private InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TAG = this.getClass().getSimpleName();
        /*设定屏幕方向为垂直*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*判断是否设置转场动画*/
        if (isOverridePendingTransition()) {
            _overridePendingTransition();
        }

        super.onCreate(savedInstanceState);

        /*getBundleExtras*/
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }

        /*EventBus.register*/
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }

        /*加入到Activity栈*/
        BaseAppManager.getInstance().addActivity(this);

        /* 获取屏幕信息 */
        getDisplayInfo();

        /* 判断是否使用沉浸式状态栏 */
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException(
                "You must return a right contentView layout resource Id");
        }
        if (hasTitleBar()) {
            setCustomTitle(getTitle());
            onNavigateClick();
        }
        initViewsAndEvents();

    }

    protected abstract void onNavigateClick();

    protected abstract void setCustomTitle(CharSequence title);

    protected abstract boolean hasTitleBar();

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * init activity view and bind event
     */
    protected abstract void initViewsAndEvents();

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 是否注册EventBus
     */
    protected boolean isBindEventBusHere() {
        return false;
    }

    /**
     * @param extras
     */
    protected void getBundleExtras(Bundle extras) {}

    /*页面进入的动画效果*/
    protected abstract boolean isOverridePendingTransition();

    /**
     * 转场动画
     *
     * @return TransitionMode
     */
    protected abstract TransitionMode getTransitionMode();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    private void getDisplayInfo() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        if (ImmersionBar.isSupportStatusBarDarkFont()) {
            mImmersionBar.statusBarDarkFont(true,0.2f).keyboardEnable(true).init();
        }
    }

    /**
     * 设置转场动画
     */
    private void _overridePendingTransition() {
        switch (getTransitionMode()) {
            case LEFT:
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case RIGHT:
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case TOP:
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
            case BOTTOM:
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                break;
            case SCALE:
                overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                break;
            case FADE:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case NONE:
            default:
                break;
        }
    }

    /**
     * overridePendingTransition mode: 转场动画
     */
    public enum TransitionMode {
        NONE, LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyBoard();
        if (isOverridePendingTransition()) {
            _overridePendingTransition();
        }
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Override
    protected void onDestroy() {
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
        this.imm = null;
        if (mImmersionBar != null) {
            mImmersionBar.destroy();//必须调用该方法，防止内存泄漏
        }
        BaseAppManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    /**
     * Activity 跳转
     *
     * @param clazz  目标activity
     * @param bundle 传递参数
     * @param finish 是否结束当前activity
     */
    private void _goActivity(Class<? extends Activity> clazz, Bundle bundle, int requestCode,
        boolean finish) {
        if (null == clazz) {
            throw new IllegalArgumentException("you must pass a target activity where to go.");
        }
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        if (requestCode > NON_CODE) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        if (finish) {
            finish();
        }
    }

    /**
     * startActivity
     *
     * @param clazz target Activity
     */
    protected void go(Class<? extends Activity> clazz) {
        _goActivity(clazz, null, NON_CODE, false);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz target Activity
     */
    protected void go(Class<? extends Activity> clazz, Bundle bundle) {
        _goActivity(clazz, bundle, NON_CODE, false);
    }

    /**
     * startActivity then finish this
     *
     * @param clazz target Activity
     */
    protected void goAndFinish(Class<? extends Activity> clazz) {
        _goActivity(clazz, null, NON_CODE, true);
    }

    /**
     * startActivity with bundle and then finish this
     *
     * @param clazz  target Activity
     * @param bundle bundle extra
     */
    protected void goAndFinish(Class<? extends Activity> clazz, Bundle bundle) {
        _goActivity(clazz, bundle, NON_CODE, true);
    }

    /**
     * startActivityForResult
     */
    protected void goForResult(Class<? extends Activity> clazz, int requestCode) {
        _goActivity(clazz, null, requestCode, false);
    }

    /**
     * startActivityForResult with bundle
     */
    protected void goForResult(Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
        _goActivity(clazz, bundle, requestCode, false);
    }

    /**
     * startActivityForResult then finish this
     */
    protected void goForResultAndFinish(Class<? extends Activity> clazz, int requestCode) {
        _goActivity(clazz, null, requestCode, true);
    }

    /**
     * startActivityForResult with bundle and then finish this
     */
    protected void goForResultAndFinish(Class<? extends Activity> clazz, Bundle bundle,
        int requestCode) {
        _goActivity(clazz, bundle, requestCode, true);
    }
}
