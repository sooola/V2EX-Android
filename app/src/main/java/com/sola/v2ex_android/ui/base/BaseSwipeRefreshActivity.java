package com.sola.v2ex_android.ui.base;

import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.sola.v2ex_android.R;
import java.lang.ref.WeakReference;

/**
 * Created by wei on 2016/10/26.
 */

public abstract class BaseSwipeRefreshActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipeRefreshLayout;
    public Handler mHandle;


    @Override
    public void onRefresh() {

    }

    @Override
    public void initViews(){
        mHandle = new MyHandler(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        if (null != mSwipeRefreshLayout){
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_green_light, android.R.color.holo_blue_bright,
                    android.R.color.holo_orange_light, android.R.color.holo_red_light);
        }
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
