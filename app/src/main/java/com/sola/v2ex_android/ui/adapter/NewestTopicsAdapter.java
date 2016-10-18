package com.sola.v2ex_android.ui.adapter;

import android.content.Context;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerViewHolder;

/**
 * 最新话题
 * Created by wei on 2016/10/18.
 */

public class NewestTopicsAdapter extends BaseRecyclerAdapter<Topics>  {


    public NewestTopicsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_newest_topics;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, Topics item) {
        holder.setText(R.id.tv_title ,item.title);
    }
}
