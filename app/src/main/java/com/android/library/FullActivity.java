package com.android.library;

import com.android.baselibrary.basedata.EventBusData;
import com.android.baselibrary.baseui.BaseActivity;

public class FullActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_full;
    }

    @Override
    protected void initViewsAndEvents() {
        eventBusPost(EventBusData.Action.DELETE_ALL_MESSAGE_IN_SESSION.createEventBusData("宝宝快点好"));
    }
}
