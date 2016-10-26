package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.NodeDetial;
import com.sola.v2ex_android.model.Replies;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.network.NetWork;
import com.sola.v2ex_android.ui.adapter.TopicsDetialAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecycleActivity;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;

import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 主题详情
 * Created by wei on 2016/10/20.
 */

public class TopicsDetialActivity extends BaseRecycleActivity<Replies> {

    public static final String KEY_TOPIC = "key_topic";
    private Topics mTopics;

    @Bind(R.id.tv_node_name)
    TextView mNodeName;
    @Bind(R.id.tv_join_count)
    TextView mJoinCount;


    public static Intent getIntent(Context context, Topics topic) {
        Intent intent = new Intent(context, TopicsDetialActivity.class);
        intent.putExtra(KEY_TOPIC, topic);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_topics_detial;
    }

    @Override
    public void onRefresh() {
        refreshing();
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
        Subscription subscription = NetWork.getV2exApi().getNodeDetial(mTopics.node.name).flatMap(new Func1<NodeDetial, Observable<List<Replies>>>() {
            @Override
            public Observable<List<Replies>> call(NodeDetial nodeDetial) {
                mJoinCount.setText("已经有 " + nodeDetial.stars + " 人关注");

                return NetWork.getRepliesApi().getReplise(mTopics.id);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addSubscription(subscription);
    }

    @Override
    protected void onViewCreate() {
        mTopics = (Topics) getIntent().getSerializableExtra(KEY_TOPIC);
        mNodeName.setText(mTopics.node.title);
    }

    @Override
    protected String getToolBarTitle() {
        return "主题详情";
    }

    @Override
    protected BaseRecyclerAdapter getAdapter() {
        return new TopicsDetialAdapter(this, mTopics);
    }
}
