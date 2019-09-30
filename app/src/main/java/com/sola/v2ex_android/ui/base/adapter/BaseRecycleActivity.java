package com.sola.v2ex_android.ui.base.adapter;

import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.ui.base.BaseActivity;
import com.sola.v2ex_android.ui.base.view.BaseSpacesItemDecoration;
import com.sola.v2ex_android.util.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 列表通用模板
 * Created by wei on 2016/10/20.
 */

public abstract class BaseRecycleActivity <T> extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener  {

    SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;

    private ActionBar mActionbar;
    public String mLastId = "0";
    protected BaseRecyclerAdapter mAdapter;
    public FixEndlessRecyclerViewAdapter mLoadMoreAdapter;
    private Handler mHandle;
    private boolean misFirst = true;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pull_refresh_recycleview;
    }

    @Override
    protected void initViews() {
        mHandle = new MyHandler(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        onViewCreate();
        initToolBar(toolbar);
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_green_light, android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        setSwipeRefreshLayoutRefresh(true);
    }

    public void initToolBar(Toolbar toolbar) {
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            mActionbar = getSupportActionBar();
            mActionbar.setHomeAsUpIndicator(R.mipmap.nav_icon_back);
            mActionbar.setDisplayHomeAsUpEnabled(true);
            mActionbar.setTitle(getToolBarTitle());
        }
    }

    public void addRecycleViewItemDecoration(){
        mRecyclerView.addItemDecoration(new BaseSpacesItemDecoration());
    }

    public boolean isNeedLoadMore(){
        return false;
    }


    @Override
    public void onRefresh() {

    }

    /**
     * 当读取玩数据后调用
     *
     * @param list
     */
    protected void loadDataSuccess(List<T> list) {
        setSwipeRefreshLayoutRefresh(false);
        if (misFirst) {
            misFirst = false;
            mAdapter.cleanData();
            if (null == mLoadMoreAdapter && isNeedLoadMore()) {
                mLoadMoreAdapter = new FixEndlessRecyclerViewAdapter(
                        this,
                        mAdapter,
                        new FixEndlessRecyclerViewAdapter.RequestToLoadMoreListener() {

                            @Override
                            public void onLoadMoreRequested() {
                                loadData();
                            }
                        }
                );
                mRecyclerView.setAdapter(mLoadMoreAdapter);
            }
        }
        if (isNeedLoadMore()){
            mLoadMoreAdapter.onDataReady(true);
        }

        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.appendItems(list);
    }

    /**
     * 当刷新的时候调用
     */
    protected void refreshing() {
        requestData();
    }


    public void setSwipeRefreshEnabled(boolean isEnable){
        mSwipeRefreshLayout.setEnabled(isEnable);
    }

    public void setSwipeRefreshLayoutRefresh(boolean refresh){
        if (refresh){
            if (!mSwipeRefreshLayout.isRefreshing()){
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                });
            }
        }else {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    /**
     * 没有数据的情况
     */
    protected void loadDataCompleteNoData() {
        mSwipeRefreshLayout.setRefreshing(false);
        if (mAdapter.getItemCount() == 0) {
            //列表为空
            ToastUtil.showShort(R.string.loading_failed);
        }else if (null != mLoadMoreAdapter && isNeedLoadMore()) {
            //加载更多时没有数据
            mLoadMoreAdapter.onDataReady(false);
        }
    }

    protected void requestData() {
        // 取新的数据
        loadData();
    }

    private static class MyHandler extends Handler {

        private WeakReference<BaseRecycleActivity> mReference;

        public MyHandler(BaseRecycleActivity activity) {
            mReference = new WeakReference<>(activity);
        }
    }



    /**
     * 给子类实现，请求数据
     */

    protected abstract void onViewCreate();

    protected abstract String getToolBarTitle();

    /**
     * 获取数据的适配器
     *
     * @return
     */
    protected abstract BaseRecyclerAdapter getAdapter();
}
