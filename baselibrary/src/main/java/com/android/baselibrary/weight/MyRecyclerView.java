package com.android.baselibrary.weight;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.baselibrary.R;
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

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

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
     * 设置LayoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }

    /**
     * 获取adapter的ItemViewType
     */
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    /**
     * 设置头部，set一个资源文件
     */
    public void setHeadView(Activity activity, int res) {
        View headView = activity.getLayoutInflater()
            .inflate(res, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.addHeaderView(headView);
    }

    /**
     * 设置头部，set一个view
     */
    public void setHeadView(View v) {
        mAdapter.addHeaderView(v);
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
     * 设置默认的分割线
     */
    public void setCustomDivider() {
        CustomDividerItem divider =
            new CustomDividerItem(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.custom_divider));
        mRecyclerView.addItemDecoration(divider);
    }

    public void setCustomDivider(int res) {
        CustomDividerItem divider =
            new CustomDividerItem(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext,res));
        mRecyclerView.addItemDecoration(divider);
    }

    /**
     * 设置自定义分割线,res: 自定义drawable文件
     */
    public void setDivider(int res) {
        DividerItemDecoration divider =
            new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, res));
        mRecyclerView.addItemDecoration(divider);
    }

    /**
     * GridLayoutManager模式下，中间的白色分割线
     */
    public void setHorizontalDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDecoration(mContext, 2, 3));
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
     * 设置数据为空是的缺省页面
     */
    public void setEmptyView(int resId) {
        mAdapter.setEmptyView(resId, (ViewGroup) mRecyclerView.getParent());
    }

    /**
     * 设置是否开启加载更多的功能，默认开启，设置为false之后不会显示加载更多的view
     */
    public void setEnableLoadMore(boolean enable) {
        mAdapter.setEnableLoadMore(enable);
    }

    /**
     * 设置是否开启下拉刷新的功能，默认开启
     */
    public void setEnableRefreshing(boolean isEnable){
        setEnableRefresh(isEnable);
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
     * 刷新 Adapter
     */
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();

    }

    /**
     *  刷新 制定位置的Adapter数据
     */
    public void notifyItemChanged(int pos){
        mAdapter.notifyItemChanged(pos);
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

    /**
     * 滑动到最顶端
     */
    public void scrollToFirst() {
        smoothMoveToPosition(0);
    }

    /**
     * 滑动到指定位置
     */
    public void smoothMoveToPosition(int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView
            .getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    /**
     * 设置滑动监听
     */
    public void setOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(mToPosition);
                }
            }
        });
    }

}
