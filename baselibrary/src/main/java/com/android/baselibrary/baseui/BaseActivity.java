package com.android.baselibrary.baseui;

import com.android.baselibrary.base.BaseAppCompatActivity;

public class BaseActivity extends BaseAppCompatActivity{

    @Override
    protected void onNavigateClick() {

    }

    @Override
    protected void setCustomTitle(CharSequence title) {

    }

    @Override
    protected boolean hasTitleBar() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected boolean isOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getTransitionMode() {
        return null;
    }
}
