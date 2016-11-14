package com.sola.v2ex_android.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;

import com.sola.v2ex_android.R;

import java.lang.ref.WeakReference;

/**
 * Created by wei on 2016/10/26.
 */

public abstract class BaseSwipeRefreshActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipeRefreshLayout;
    public Handler mHandle;
    Toolbar mToolbar;

    @Override
    public void onRefresh() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandle = new MyHandler(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != mSwipeRefreshLayout){
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_green_light, android.R.color.holo_blue_bright,
                    android.R.color.holo_orange_light, android.R.color.holo_red_light);
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setSwipeRefreshEnabled(boolean isEnable){
        if (null != mSwipeRefreshLayout)
            mSwipeRefreshLayout.setEnabled(isEnable);
    }

    public void setSwipeRefreshLayoutRefresh(boolean refresh){
        if (null != mSwipeRefreshLayout){
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
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<BaseSwipeRefreshActivity> mReference;

        public MyHandler(BaseSwipeRefreshActivity activity) {
            mReference = new WeakReference<>(activity);
        }
    }


}
