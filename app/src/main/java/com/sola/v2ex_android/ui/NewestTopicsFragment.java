package com.sola.v2ex_android.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.model.TopicsData;
import com.sola.v2ex_android.network.V2exService;
import com.sola.v2ex_android.ui.adapter.TopicsAdapter;
import com.sola.v2ex_android.ui.base.BaseSwipeRefreshFragment;
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
 * 主页 -  话题
 * Created by wei on 2016/10/18.
 */
public class NewestTopicsFragment extends BaseSwipeRefreshFragment {

    @Bind(R.id.recycleview)
    RecyclerView mRecycleview;

    private TopicsAdapter mAdapter;

    public static NewestTopicsFragment newInstance() {
        return new NewestTopicsFragment();
    }

    Observer<TopicsData> observer = new Observer<TopicsData>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            setSwipeRefreshLayoutRefresh(false);
            ToastUtil.showShort( R.string.loading_failed);
        }

        @Override
        public void onNext(TopicsData items) {
            setSwipeRefreshLayoutRefresh(false);
            int hotTopicsSize = items.hotTopics.size();
            mAdapter.setHotTopicsSize(hotTopicsSize);
            items.allTopics.add(hotTopicsSize , items.hotTopics.get(hotTopicsSize - 1));
            mAdapter.appendItems(items.allTopics);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_newset_topics;
    }


    @Override
    public void initViews(View view) {
        super.initViews(view);
        setSwipeRefreshLayoutRefresh(true);
        setupRecyclerView();
    }

    @Override
    public void loadData() {
        Subscription subscription = Observable.zip(V2exService.getInstance().getV2exApi().getTopicHot(), V2exService.getInstance().getV2exApi().getTopicLatest(), new Func2<List<Topics>, List<Topics>, TopicsData>() {
            @Override
            public TopicsData call(List<Topics> hotTopics, List<Topics> latestTopics) {
                setImagData(hotTopics);
                setImagData(latestTopics);
                TopicsData topicsData = new TopicsData();
                topicsData.hotTopics = hotTopics;
                latestTopics.addAll(0 , hotTopics);
                topicsData.allTopics = latestTopics;
                return topicsData;
            }
        }).subscribeOn(Schedulers.io())
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
        mAdapter = new TopicsAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
