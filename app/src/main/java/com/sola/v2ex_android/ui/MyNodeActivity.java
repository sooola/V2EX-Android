package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.network.LoginService;
import com.sola.v2ex_android.ui.adapter.MyNodeAdapter;
import com.sola.v2ex_android.ui.base.BaseSwipeRefreshActivity;
import com.sola.v2ex_android.util.JsoupUtil;

import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wei on 2016/11/14.
 * 我收藏的节点
 */

public class MyNodeActivity extends BaseSwipeRefreshActivity {

    @Bind(R.id.recyclerView)
    RecyclerView mRecycleview;

    MyNodeAdapter mAdapter;
    
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,MyNodeActivity.class);
        return intent;
    }

    Observer<String> mObservable = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String stringResponse) {
            mAdapter.appendItems(JsoupUtil.parseMyNodeInfo(stringResponse));
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_node;
    }

    @Override
    protected void initViews() {
        setupRecyclerView();
    }

    @Override
    public void loadData() {
        Subscription subscription =  LoginService.getInstance().auth().myCollectNode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObservable);
        addSubscription(subscription);
    }


    private void setupRecyclerView() {
        mAdapter = new MyNodeAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this , 3);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.setAdapter(mAdapter);
    }
}
