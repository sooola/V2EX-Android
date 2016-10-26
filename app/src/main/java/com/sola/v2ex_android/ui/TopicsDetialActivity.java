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
import com.sola.v2ex_android.util.LogUtil;

import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
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

    Observer<NodeDetial> observer = new Observer<NodeDetial>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
             LogUtil.d("TopicsDetialActivity","e" + e.toString());
            loadDataCompleteNoData();
        }

        @Override
        public void onNext(NodeDetial items) {
            mJoinCount.setText(String.format(TopicsDetialActivity.this.getResources().getString(R.string.node_start) ,items.stars));
            loadDataSuccess(items.replies);
        }
    };

    @Override
    protected void sendRequestData() {
        loadData();
    }

    private void loadData() {
        Subscription subscription = Observable.zip(NetWork.getV2exApi().getNodeDetial(mTopics.node.name), NetWork.getUserApi().getReplise(mTopics.id), new Func2<NodeDetial, List<Replies>, NodeDetial>() {
            @Override
            public NodeDetial call(NodeDetial nodeDetial, List<Replies> replies) {
                nodeDetial.replies = replies;
                return nodeDetial;
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
