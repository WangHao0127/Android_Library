package com.android.library;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baselibrary.basedata.EventBusData;
import com.android.baselibrary.baseui.BaseActivity;
import com.android.baselibrary.helper.CustomLoadMoreView;
import com.android.baselibrary.retrofitbasenet.MyObserver;
import com.android.library.dealnet.WeatherSubscribe;
import com.android.library.recyclerview.AnimationAdapter;
import com.android.library.recyclerview.entity.Status;

import java.util.List;

import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import static com.android.library.recyclerview.AnimationAdapter.getSampleData;

public class FullActivity extends BaseActivity {

    private static final int PAGE_SIZE = 6;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private AnimationAdapter mAnimationAdapter;
    private int mFirstPageItemCount = 3;
    private int mNextRequestPage = 1;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_full;
    }

    @Override
    protected void initViewsAndEvents() {
        eventBusPost(EventBusData.Action.DELETE_ALL_MESSAGE_IN_SESSION.createEventBusData("YES"));

        WeatherSubscribe.getData(new MyObserver<WeatherData>(this) {

            @Override
            public void onNext(WeatherData weatherData) {
                showToast(weatherData.getWeatherinfo().getCity());
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        addHeadView();
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        refresh();
      /*  FriendService
            .Factory
            .getFriendService()
            .getWeather()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MyObserver<WeatherData>() {
                @Override
                public void onNext(WeatherRealm weatherData) {
                    showToast(weatherData.getWeatherinfo().getCity());
                }
            });*/

    }

    private void refresh() {
        mNextRequestPage = 1;
        mAnimationAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        new Request(mNextRequestPage, new RequestCallBack() {
            @Override
            public void success(List<Status> data) {
                setData(true, data);
                mAnimationAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void fail(Exception e) {
                mAnimationAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }).start();
    }

    private void setData(boolean isRefresh, List data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAnimationAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAnimationAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAnimationAdapter.loadMoreEnd(isRefresh);
            Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show();
        } else {
            mAnimationAdapter.loadMoreComplete();
        }
    }


    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        mAnimationAdapter.addHeaderView(headView);
    }
    private void initAdapter() {
        mAnimationAdapter = new AnimationAdapter();
        mAnimationAdapter.openLoadAnimation();
        mAnimationAdapter.setNotDoAnimationCount(mFirstPageItemCount);
        mAnimationAdapter
            .setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    String content = null;
                    Status status = (Status) adapter.getItem(position);
                    switch (view.getId()) {
                        case R.id.img:
                            content = "img:" + status.getUserAvatar();
                            Toast.makeText(FullActivity.this, content, Toast.LENGTH_LONG).show();
                            break;
                        case R.id.tweetName:
                            content = "name:" + status.getUserName();
                            Toast.makeText(FullActivity.this, content, Toast.LENGTH_LONG).show();
                            break;
                        case R.id.tweetText:
                            content = "tweetText:" + status.getUserName();
                            Toast.makeText(FullActivity.this, content, Toast.LENGTH_LONG).show();
                            break;

                    }
                }
            });
        mAnimationAdapter.setLoadMoreView(new CustomLoadMoreView());
        mRecyclerView.setAdapter(mAnimationAdapter);

        mAnimationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });

    }

    private void loadMore() {
        new Request(mNextRequestPage, new RequestCallBack() {
            @Override
            public void success(List<Status> data) {
                setData(false, data);
            }

            @Override
            public void fail(Exception e) {
                mAnimationAdapter.loadMoreFail();
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
        try {Thread.sleep(500);} catch (InterruptedException e) {}

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