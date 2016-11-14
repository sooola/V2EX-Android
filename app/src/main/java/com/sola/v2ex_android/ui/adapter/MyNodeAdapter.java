package com.sola.v2ex_android.ui.adapter;

import android.content.Context;
import android.view.View;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.MyNode;
import com.sola.v2ex_android.ui.NodeDetialActivity;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerViewHolder;
import com.sola.v2ex_android.util.GlideUtil;

/**
 * Created by wei on 2016/11/14.
 */

public class MyNodeAdapter extends BaseRecyclerAdapter<MyNode> {

    public MyNodeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_node;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, final MyNode item) {
        holder.setText(R.id.tv_node_name , item.nodeName);
        holder.setText(R.id.tv_follow_count , item.followCount);
        GlideUtil.glideWithImg(mContext ,item.imgSrc , holder.getImageView(R.id.iv_node_icon));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(NodeDetialActivity.getIntent(view.getContext() ,item.nodeName));
            }
        });
    }
}
