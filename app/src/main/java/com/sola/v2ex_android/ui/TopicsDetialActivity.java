package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;

import com.sola.v2ex_android.model.Replies;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.network.NetWork;
import com.sola.v2ex_android.ui.adapter.TopicsDetialAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecycleActivity;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *主题详情
 * Created by wei on 2016/10/20.
 */

public class TopicsDetialActivity extends BaseRecycleActivity<Replies> {

    public static final String KEY_TOPIC = "key_topic";
    private Topics mTopics;

    public static Intent getIntent(Context context , Topics topic){
        Intent intent = new Intent(context,TopicsDetialActivity.class);
        intent.putExtra(KEY_TOPIC ,topic);
        return intent;
    }

    Observer<List<Replies>> observer = new Observer<List<Replies>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            loadDataCompleteNoData();
        }

        @Override
        public void onNext(List<Replies> items) {
            loadDataSuccess(items);
        }
    };

    @Override
    protected void sendRequestData() {
        Subscription subscription = NetWork.getRepliesApi()
                .getReplise(mTopics.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addSubscription(subscription);
    }

    @Override
    protected void onViewCreate() {
        mTopics = (Topics) getIntent().getSerializableExtra(KEY_TOPIC);
    }

    @Override
    protected String getToolBarTitle() {
        return "主题详情";
    }

    @Override
    protected BaseRecyclerAdapter getAdapter() {
        return new TopicsDetialAdapter(this , mTopics);
    }
}
