package com.sola.v2ex_android.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.network.NetWork;
import com.sola.v2ex_android.ui.adapter.NewestTopicsAdapter;
import com.sola.v2ex_android.ui.base.BaseFragment;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.TextMatcher;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 最新话题
 * Created by wei on 2016/10/18.
 */

public class NewestTopicsFragment extends BaseFragment {

    @Bind(R.id.recycleview)
    RecyclerView mRecycleview;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private NewestTopicsAdapter mAdapter;

    Observer<List<Topics>> observer = new Observer<List<Topics>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            mSwipeRefreshLayout.setRefreshing(false);
            LogUtil.d("NewestTopicsFragment", "e" + e.toString());
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<Topics> items) {
            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.appendItems(items);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_newset_topics;
    }

    @Override
    protected void initViews() {
        setupRecyclerView();
        loadData();
    }

    public void loadData() {
        Subscription subscription = NetWork.getV2exApi()
                .getTopicHot()
                .doOnNext(new Action1<List<Topics>>() {
                    @Override
                    public void call(List<Topics> topicses) {
                        setImagData(topicses);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addSubscription(subscription);
    }

    private void setImagData(List<Topics> topicses) {
        for (Topics topics : topicses) {
            topics.imgList = TextMatcher.matchImg(topics.content_rendered);
        }
    }


    private void setupRecyclerView() {
        mAdapter = new NewestTopicsAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.setAdapter(mAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
