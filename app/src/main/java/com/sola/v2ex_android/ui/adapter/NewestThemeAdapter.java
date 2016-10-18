package com.sola.v2ex_android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerViewHolder;

/**
 * Created by wei on 2016/10/18.
 */

public class NewestThemeAdapter extends BaseRecyclerAdapter {


    public NewestThemeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return 0;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, Object item) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
