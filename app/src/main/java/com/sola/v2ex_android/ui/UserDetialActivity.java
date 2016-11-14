package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.model.UserInfo;
import com.sola.v2ex_android.network.V2exService;
import com.sola.v2ex_android.ui.adapter.TopicsAdapter;
import com.sola.v2ex_android.ui.base.BaseSwipeRefreshActivity;
import com.sola.v2ex_android.util.Constants;
import com.sola.v2ex_android.util.GlideUtil;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.TimeUtil;
import com.sola.v2ex_android.util.ToastUtil;

import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 用户详情
 * Created by wei on 2016/10/21.
 */

public class UserDetialActivity extends BaseSwipeRefreshActivity {

    public static final String KEY_USERNAME = "key_username";
    private String mUserName;
    private TopicsAdapter mAdapter;

    @Bind(R.id.appbarlayout)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.recyclerView)
    RecyclerView mRecycleview;
    @Bind(R.id.coll_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.iv_user_icon)
    ImageView mUserIcon;
    @Bind(R.id.tv_username)
    TextView mUserNameTv;
    @Bind(R.id.tv_already_join_day)
    TextView mAlreadyJoinDay;

    public static Intent getIntent(Context context, String userName) {
        Intent intent = new Intent(context, UserDetialActivity.class);
        intent.putExtra(KEY_USERNAME, userName);
        return intent;
    }


    Observer<UserInfo> observer = new Observer<UserInfo>() {
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
        public void onNext(UserInfo items) {
            GlideUtil.glideWithCircleImg(UserDetialActivity.this, Constants.makeUserLogo(items.avatar_normal), mUserIcon);
            mUserNameTv.setText(mUserName);
            mAlreadyJoinDay.setText(TimeUtil.alreadyJoinData(items.created * 1000));
            mAdapter.appendItems(items.topicsList);
            setSwipeRefreshLayoutRefresh(false);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_detial;
    }

    @Override
    protected void initViews() {
        mUserName = getIntent().getStringExtra(KEY_USERNAME);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                LogUtil.d("UserDetialActivity", "verticalOffset" + verticalOffset);
                if (-verticalOffset >= 245) {
                    mCollapsingToolbarLayout.setTitle(mUserName);
                } else {
                    mCollapsingToolbarLayout.setTitle("");
                }
            }
        });
        setupRecyclerView();
    }

    public void loadData() {
        setSwipeRefreshLayoutRefresh(true);
        Subscription subscription = Observable.zip(V2exService.getInstance().getV2exApi().getUserInfo(mUserName), V2exService.getInstance().getV2exApi().getTopicsByUserName(mUserName), new Func2<UserInfo, List<Topics>, UserInfo>() {
            @Override
            public UserInfo call(UserInfo userInfo, List<Topics> topicses) {
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
