package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.NodeInfo;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.network.V2exService;
import com.sola.v2ex_android.ui.adapter.TopicsAdapter;
import com.sola.v2ex_android.ui.base.BaseSwipeRefreshActivity;
import com.sola.v2ex_android.util.Constants;
import com.sola.v2ex_android.util.ContentUtils;
import com.sola.v2ex_android.util.GlideUtil;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.ToastUtil;
import com.zzhoujay.richtext.RichText;

import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 节点详情
 * Created by wei on 2016/10/27.
 */

public class NodeDetialActivity extends BaseSwipeRefreshActivity {

    public static final String KEY_NODENAME = "key_nodename";
    private String mNodeName;
    private TopicsAdapter mAdapter;

    @BindView(R.id.appbarlayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecycleview;
    @BindView(R.id.coll_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.iv_node_icon)
    ImageView mUserIcon;
    @BindView(R.id.tv_nodename)
    TextView mNodeNameTv;
    @BindView(R.id.tv_header)
    TextView mHeader;

    public static Intent getIntent(Context context, String nodeName) {
        Intent intent = new Intent(context, NodeDetialActivity.class);
        intent.putExtra(KEY_NODENAME, nodeName);
        return intent;
    }


    Observer<NodeInfo> observer = new Observer<NodeInfo>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            setSwipeRefreshLayoutRefresh(false);
            LogUtil.d("UserDetialActivity","e" +e.toString());
            ToastUtil.showShort(R.string.loading_failed);
        }

        @Override
        public void onNext(NodeInfo items) {
            GlideUtil.glideWithCircleImg(NodeDetialActivity.this, Constants.makeUserLogo(items.avatar_large), mUserIcon);
            mNodeNameTv.setText(mNodeName);
            RichText.from(ContentUtils.formatContent(items.header)).into(mHeader);
            mAdapter.appendItems(items.topicsList);
            setSwipeRefreshLayoutRefresh(false);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_node_detial;
    }



    @Override
    public void initViews() {
        super.initViews();
        setSwipeRefreshLayoutRefresh(true);
        mNodeName = getIntent().getStringExtra(KEY_NODENAME);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                LogUtil.d("UserDetialActivity", "verticalOffset" + verticalOffset);
                if (-verticalOffset >= 142) {
                    mCollapsingToolbarLayout.setTitle(mNodeName);
                } else {
                    mCollapsingToolbarLayout.setTitle("");
                }
            }
        });
        setupRecyclerView();
    }

    @Override
    public void loadData() {
        setSwipeRefreshLayoutRefresh(true);
        Subscription subscription = Observable.zip(V2exService.getInstance().getV2exApi().getNodeDetial(mNodeName), V2exService.getInstance().getV2exApi().getTopicsByNodeName(mNodeName), new Func2<NodeInfo, List<Topics>, NodeInfo>() {
            @Override
            public NodeInfo call(NodeInfo userInfo, List<Topics> topicses) {
                userInfo.topicsList = topicses;
                return userInfo;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addSubscription(subscription);
    }

    private void setupRecyclerView() {
        mAdapter = new TopicsAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        mAdapter.cleanData();
        loadData();
    }
}
