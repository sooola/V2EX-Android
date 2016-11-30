package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.MyFollowingTopicDetial;
import com.sola.v2ex_android.model.Replies;
import com.sola.v2ex_android.network.LoginService;
import com.sola.v2ex_android.ui.adapter.MyFollowingTopicDetialAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecycleActivity;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;
import com.sola.v2ex_android.util.JsoupUtil;
import com.sola.v2ex_android.util.LogUtil;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wei on 2016/11/28.
 * 我关注的人详情
 */

public class MyFollowingTopicDetialActivity extends BaseRecycleActivity<Replies> {

    public static final String KEY_TOPICID = "key_topicId";
    private String mTopicId;
    private MyFollowingTopicDetialAdapter mAdapter;

    public static Intent getIntent(Context context, String topicId) {
        Intent intent = new Intent(context, MyFollowingTopicDetialActivity.class);
        intent.putExtra(KEY_TOPICID, topicId);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_following_topic_detial;
    }

    @Override
    public void onRefresh() {
        mAdapter.cleanData();
        refreshing();
    }

    Observer<MyFollowingTopicDetial> mObserver = new Observer<MyFollowingTopicDetial>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            LogUtil.d("TopicsDetialActivity","e" + e.toString());
            loadDataCompleteNoData();
        }

        @Override
        public void onNext(MyFollowingTopicDetial items) {
            mAdapter.setTopicDetial(items);
            loadDataSuccess(items.replies);
        }
    };

    @Override
    public void loadData() {
        Subscription subscription = LoginService.getInstance().auth().myFollowingTopicDetial(mTopicId).map(new Func1<String, MyFollowingTopicDetial>() {
            @Override
            public MyFollowingTopicDetial call(String response) {
                return JsoupUtil.parseMyfollowingTopicDetial(response);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
        addSubscription(subscription);
    }

    @Override
    protected void onViewCreate() {
        mTopicId = (String) getIntent().getSerializableExtra(KEY_TOPICID);
        mAdapter = new MyFollowingTopicDetialAdapter(this);
         LogUtil.d("MyFollowingTopicDetialActivity","mTopicId" + mTopicId);
    }

    @Override
    protected String getToolBarTitle() {
        return "主题详情";
    }

    @Override
    protected BaseRecyclerAdapter getAdapter() {
        return mAdapter;
    }
}
