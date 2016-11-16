package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.V2exUser;
import com.sola.v2ex_android.network.LoginService;
import com.sola.v2ex_android.ui.adapter.MyFollowingAdapter;
import com.sola.v2ex_android.ui.base.BaseSwipeRefreshActivity;
import com.sola.v2ex_android.util.JsoupUtil;
import com.sola.v2ex_android.util.ToastUtil;

import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wei on 2016/11/15.
 * 我关注的人
 */

public class MyFollowingActivity extends BaseSwipeRefreshActivity {

    @Bind(R.id.recycleview)
    RecyclerView mRecyclerView;

    MyFollowingAdapter mAdapter;
    
    public static Intent getIntent(Context context){
        Intent intent;
        if (null == V2exUser.getCurrentUser()){
            intent = LoginActivity.getIntent(context);
        }else {
            intent = new Intent(context,MyFollowingActivity.class);
        }
        return intent;
    }

    Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            setSwipeRefreshLayoutRefresh(false);
            ToastUtil.showShort(getString(R.string.tip_network_error));
        }

        @Override
        public void onNext(String stringResponse) {
            setSwipeRefreshLayoutRefresh(false);
            mAdapter.appendItems(JsoupUtil.parseMyfollowing(stringResponse));
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_following;
    }

    @Override
    public void initViews() {
        super.initViews();
        setSwipeRefreshLayoutRefresh(true);
        setupRecyclerView();
    }

    @Override
    public void loadData() {
        Subscription subscription =  LoginService.getInstance().auth().myFollowing()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
        addSubscription(subscription);
    }

    private void setupRecyclerView() {
        mAdapter = new MyFollowingAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        mAdapter.cleanData();
        loadData();
    }
}
