package com.sola.v2ex_android.ui.adapter;

import android.content.Context;
import android.view.View;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.ui.TopicsDetialActivity;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerViewHolder;
import com.sola.v2ex_android.util.GlideUtil;
import com.sola.v2ex_android.util.ValidateUtil;

/**
 * 最新话题
 * Created by wei on 2016/10/18.
 */

public class NewestTopicsAdapter extends BaseRecyclerAdapter<Topics> {


    public NewestTopicsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_newest_topics;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, final Topics item) {
        holder.setText(R.id.tv_title, item.title);
        holder.setText(R.id.tv_subtitle, item.content);
        holder.setText(R.id.tv_review_count, String.valueOf(item.replies));
        holder.setText(R.id.tv_username, item.member.username);
        holder.setText(R.id.tv_node_name, item.node.title);
        holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(TopicsDetialActivity.getIntent(v.getContext() , item));
            }
        });
        if (ValidateUtil.isNotEmpty(item.imgList)) {
            for (int i = 0; i < item.imgList.size(); i++) {
                if (0 == i) {
                    GlideUtil.glideWithImg(mContext , item.imgList.get(0) , holder.getImageView(R.id.iv_img1));
                    holder.setViewGone(R.id.iv_img1 , false);
                }else if (1 == i){
                    GlideUtil.glideWithImg(mContext , item.imgList.get(1) , holder.getImageView(R.id.iv_img2));
                    holder.setViewGone(R.id.iv_img2 , false);
                }else if (2 == i){
                    GlideUtil.glideWithImg(mContext , item.imgList.get(2) , holder.getImageView(R.id.iv_img3));
                    holder.setViewGone(R.id.iv_img3 , false);
                }
            }
        } else {
            holder.setViewGone(R.id.iv_img1 , true);
            holder.setViewGone(R.id.iv_img2 , true);
            holder.setViewGone(R.id.iv_img3 , true);
        }
    }
}
