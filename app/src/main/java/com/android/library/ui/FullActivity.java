package com.android.library.ui;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.android.baselibrary.basedata.EventBusData;
import com.android.baselibrary.baseui.BaseActivity;
import com.android.baselibrary.weight.MyRecyclerView;
import com.android.library.R;
import com.android.library.recyclerview.AnimationAdapter;
import com.android.library.recyclerview.entity.Status;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import static com.android.library.recyclerview.AnimationAdapter.getSampleData;

public class FullActivity extends BaseActivity {

    private static final int PAGE_SIZE = 6;

    @BindView(R.id.rv_list)
    MyRecyclerView mRecyclerView;

    private AnimationAdapter mDemoAdapter;
    private int mNextRequestPage = 1;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_full;
    }

    @Override
    protected void initViewsAndEvents() {
        EventBus.getDefault().post(EventBusData.Action.DELETE_ALL_MESSAGE_IN_SESSION.createEventBusData("王舒铭"));
        init();
    }

    private void init() {
        initAdapter();
        refresh();
        mRecyclerView.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                refresh();
            }
        });
    }

    private void initAdapter() {
        mDemoAdapter = new AnimationAdapter();
        mRecyclerView.setAdapter(mDemoAdapter);

        mRecyclerView.setHeadView(FullActivity.this, R.layout.head_view);
        //        mRecyclerView.setFootView(FullActivity.this, R.layout.head_view);

        mRecyclerView.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(FullActivity.this, position + "", Toast.LENGTH_SHORT)
                    .show();
            }
        });
        mRecyclerView.setOnItemChildClickListener(
            new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    String content = null;
                    Status status = (Status) adapter.getItem(position);
                    switch (view.getId()) {
                        case R.id.img:
                            content = "img:" + status.getUserAvatar();
                            Toast
                                .makeText(FullActivity.this, content, Toast.LENGTH_LONG)
                                .show();
                            break;
                        case R.id.tweetName:
                            content = "name:" + status.getUserName();
                            Toast
                                .makeText(FullActivity.this, content, Toast.LENGTH_LONG)
                                .show();
                            break;
                        case R.id.tweetText:
                            content = "tweetText:" + status.getUserName();
                            Toast
                                .makeText(FullActivity.this, content, Toast.LENGTH_LONG)
                                .show();
                            break;

                    }
                }
            });

        mRecyclerView.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });

    }



    private void refresh() {
        mNextRequestPage = 1;
        mRecyclerView.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        new Request(mNextRequestPage, new RequestCallBack() {
            @Override
            public void success(List<Status> data) {
                setData(true, data);
                mRecyclerView.setEnableLoadMore(true);
                mRecyclerView.finishRefreshing();
            }

            @Override
            public void fail(Exception e) {
                mRecyclerView.setEnableLoadMore(true);
                mRecyclerView.finishRefreshing();
            }
        }).start();
    }

    private void setData(boolean isRefresh, List data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mRecyclerView.setNewData(data);
        } else {
            if (size > 0) {
                mRecyclerView.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mRecyclerView.loadMoreEnd(isRefresh);
            Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show();
        } else {
            mRecyclerView.loadMoreComplete();
        }
    }



    private void loadMore() {
        new Request(mNextRequestPage, new RequestCallBack() {
            @Override
            public void success(List<Status> data) {
                setData(false, data);
            }

            @Override
            public void fail(Exception e) {
                mRecyclerView.loadMoreFail();
            }
        }).start();
    }


}

interface RequestCallBack {
    void success(List<Status> data);

    void fail(Exception e);
}

class Request extends Thread {
    private static final int PAGE_SIZE = 6;
    private int mPage;
    private RequestCallBack mCallBack;
    private Handler mHandler;

    private static boolean mFirstPageNoMore;
    private static boolean mFirstError = true;

    public Request(int page, RequestCallBack callBack) {
        mPage = page;
        mCallBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        if (mPage == 2 && mFirstError) {
            mFirstError = false;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.fail(new RuntimeException("fail"));
                }
            });
        } else {
            int size = PAGE_SIZE;
            if (mPage == 1) {
                if (mFirstPageNoMore) {
                    size = 1;
                }
                mFirstPageNoMore = !mFirstPageNoMore;
                if (!mFirstError) {
                    mFirstError = true;
                }
            } else if (mPage == 4) {
                size = 1;
            }

            final int dataSize = size;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.success(getSampleData(dataSize));
                }
            });
        }
    }
}