package com.sola.v2ex_android.ui;

import android.support.v7.widget.RecyclerView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.ui.adapter.NewestThemeAdapter;
import com.sola.v2ex_android.ui.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wei on 2016/10/18.
 */

public class NewestTopicsFragment extends BaseFragment {


    @Bind(R.id.recycleview)
    RecyclerView mRecycleview;

    private NewestThemeAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_newset_topics;
    }

    @Override
    protected void initViews() {
//        mAdapter = new
    }

    private void setupRecyclerView() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        mRecycleview.setLayoutManager(layoutManager);
//        mMeizhiListAdapter = new MeizhiListAdapter(this, mMeizhiList);
//        mRecycleview.setAdapter(mMeizhiListAdapter);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
