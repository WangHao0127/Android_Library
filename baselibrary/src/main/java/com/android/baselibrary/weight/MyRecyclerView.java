package com.android.baselibrary.weight;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.baselibrary.helper.CustomLoadMoreView;

import java.util.Collection;
import java.util.List;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

/**
 * Author: WangHao
 * Created On: 2018/9/30 0030 17:35
 * Description:
 */
public class MyRecyclerView<T> extends TwinklingRefreshLayout {

    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private Context mContext;

    private LinearLayout.LayoutParams params;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        mContext = context;
        mRecyclerView = new RecyclerView(context);

        setEnableLoadmore(false);
        setEnableOverScroll(false);
        setHeaderView(new SinaRefreshView(context));

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT); //加上这段，否则会出现ui上的不协调
        addView(mRecyclerView, params);
    }

    /**
     * 适用于LinearLayoutManager
     */
    public void setAdapter(BaseQuickAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        openLoadAnimation();
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 适用于GridLayoutManager
     */
    public void setAdapter(BaseQuickAdapter adapter, RecyclerView.LayoutManager layout) {
        mAdapter = adapter;
        if (layout == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            mRecyclerView.setLayoutManager(layout);
        }
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        openLoadAnimation();
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 设置头部
     */
    public void setHeadView(Activity activity, int res) {
        View headView = activity.getLayoutInflater()
            .inflate(res, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.addHeaderView(headView);
    }

    /**
     * 设置尾部
     */
    public void setFootView(Activity activity, int res) {
        View headView = activity.getLayoutInflater()
            .inflate(res, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.addFooterView(headView);
    }

    /**
     * 设置加载更多的监听
     */
    public void setOnLoadMoreListener(
        BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener) {
        if (requestLoadMoreListener != null) {
            mAdapter.setOnLoadMoreListener(requestLoadMoreListener, mRecyclerView);
        }
    }

    /**
     * 设置item中具体id的点击事件
     */
    public void setOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener listener) {
        if (listener != null) {
            mAdapter.setOnItemChildClickListener(listener);
        }
    }

    /**
     * 设置item的整个点击事件
     */
    public void setOnItemClickListener(@Nullable BaseQuickAdapter.OnItemClickListener listener) {
        if (listener != null) {
            mAdapter.setOnItemClickListener(listener);
        }
    }

    /**
     * 设置是否开启加载更多的功能，默认开启，设置为false之后不会显示加载更多的view
     */
    public void setEnableLoadMore(boolean enable) {
        mAdapter.setEnableLoadMore(enable);
    }

    /**
     * 设置新数据
     */
    public void setNewData(@Nullable List<T> data) {
        mAdapter.setNewData(data);
    }

    /**
     * 在原有数据基础上增添新数据
     */
    public void addData(@NonNull Collection<? extends T> newData) {
        mAdapter.addData(newData);
    }

    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    public void loadMoreEnd(boolean gone) {
        mAdapter.loadMoreEnd(gone);
    }

    /**
     * 加载更多完成
     */
    public void loadMoreComplete() {
        mAdapter.loadMoreComplete();
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(BaseAnimation animation) {
        mAdapter.openLoadAnimation(animation);
    }

    /**
     * To open the animation when loading
     */
    public void openLoadAnimation() {
        mAdapter.openLoadAnimation();
    }

    /**
     * Refresh failed
     */
    public void loadMoreFail() {
        mAdapter.loadMoreFail();
    }
}
