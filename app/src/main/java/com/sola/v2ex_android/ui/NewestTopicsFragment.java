package com.sola.v2ex_android.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.model.TopicsData;
import com.sola.v2ex_android.network.NetWork;
import com.sola.v2ex_android.ui.adapter.TopicsAdapter;
import com.sola.v2ex_android.ui.base.BaseFragment;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.TextMatcher;
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
 * 最新话题
 * Created by wei on 2016/10/18.
 */
public class NewestTopicsFragment extends BaseFragment {

    @Bind(R.id.recycleview)
    RecyclerView mRecycleview;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private TopicsAdapter mAdapter;

    Observer<TopicsData> observer = new Observer<TopicsData>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            mSwipeRefreshLayout.setRefreshing(false);
            LogUtil.d("NewestTopicsFragment", "e" + e.toString());
            ToastUtil.showShort( R.string.loading_failed);
        }

        @Override
        public void onNext(TopicsData items) {
            mSwipeRefreshLayout.setRefreshing(false);
            int hotTopicsSize = items.hotTopics.size();
            mAdapter.setHotTopicsSize(hotTopicsSize);
            items.hotTopics.add(hotTopicsSize , items.hotTopics.get(hotTopicsSize - 1));
             LogUtil.d("NewestTopicsFragment","items.allTopics"  + items.allTopics.size());
            mAdapter.appendItems(items.allTopics);
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
        Subscription subscription = Observable.zip(NetWork.getV2exApi().getTopicHot(), NetWork.getV2exApi().getTopicLatest(), new Func2<List<Topics>, List<Topics>, TopicsData>() {
            @Override
            public TopicsData call(List<Topics> hotTopics, List<Topics> latestTopics) {
                setImagData(hotTopics);
                setImagData(latestTopics);
                TopicsData topicsData = new TopicsData();
                topicsData.hotTopics = hotTopics;
//                topicsData.latestTopics = latestTopics;
                hotTopics.addAll(latestTopics);
//                Collections.addAll(hotTopics , latestTopics)
//                topicsData.allTopics = ;
                return topicsData;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

//        Subscription subscription = NetWork.getV2exApi()
//                .getTopicHot()
//                .doOnNext(new Action1<List<Topics>>() {
//                    @Override
//                    public void call(List<Topics> topicses) {
//                        setImagData(topicses);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
        addSubscription(subscription);
    }

    private void setImagData(List<Topics> topicses) {
        for (Topics topics : topicses) {
            topics.imgList = TextMatcher.matchImg(topics.content_rendered);
        }
    }


    private void setupRecyclerView() {
        mAdapter = new TopicsAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.setAdapter(mAdapter);
    }
}
