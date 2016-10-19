package com.sola.v2ex_android.ui.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerViewHolder;
import com.sola.v2ex_android.util.LogUtil;
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
    public void bindData(BaseRecyclerViewHolder holder, int position, Topics item) {
        holder.setText(R.id.tv_title, item.title);
        holder.setText(R.id.tv_subtitle, item.content);
        holder.setText(R.id.tv_review_count, item.replies);
        holder.setText(R.id.tv_username, item.member.username);
        holder.setText(R.id.tv_node_name, item.node.title);
        if (ValidateUtil.isNotEmpty(item.imgList)) {
            LogUtil.d("NewestTopicsAdapter", "item.imgList.get(0)" + item.imgList.get(0));
            for (int i = 0; i < item.imgList.size(); i++) {
                if (0 == i) {
                    Glide
                            .with(mContext)
                            .load(item.imgList.get(0))
                            .placeholder(R.drawable.loading_color)
                            .crossFade()
                            .centerCrop()
                            .into(holder.getImageView(R.id.iv_img1));
                    holder.setViewGone(R.id.iv_img1 , false);
                     LogUtil.d("NewestTopicsAdapter","item.imgList.get(0)" + item.imgList.get(0));
                }else if (1 == i){
                    Glide
                            .with(mContext)
                            .load(item.imgList.get(1))
                            .placeholder(R.drawable.loading_color)
                            .crossFade()
                            .centerCrop()
                            .into(holder.getImageView(R.id.iv_img2));
                    holder.setViewGone(R.id.iv_img2 , false);
                    LogUtil.d("NewestTopicsAdapter","item.imgList.get(1)" + item.imgList.get(1));
                }else if (2 == i){
                    Glide
                            .with(mContext)
                            .load(item.imgList.get(2))
                            .placeholder(R.drawable.loading_color)
                            .crossFade()
                            .centerCrop()
                            .into(holder.getImageView(R.id.iv_img3));
                    holder.setViewGone(R.id.iv_img3 , false);
                    LogUtil.d("NewestTopicsAdapter","item.imgList.get(2)" + item.imgList.get(2));
                }
            }
        } else {
            holder.setViewGone(R.id.iv_img1 , true);
            holder.setViewGone(R.id.iv_img2 , true);
            holder.setViewGone(R.id.iv_img3 , true);
        }
    }
}
