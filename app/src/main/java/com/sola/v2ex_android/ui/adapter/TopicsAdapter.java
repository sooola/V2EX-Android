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
 * 帖子适配器
 * Created by wei on 2016/10/18.
 */

public class TopicsAdapter extends BaseRecyclerAdapter<Topics> {

    public static final int TYPE_LATEST = 3;
    private int mHotTopicsSize;

    public TopicsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int bindViewType(int position) {
        if (position == mHotTopicsSize){
            return TYPE_LATEST;
        }else {
            return TYPE_ITEM;
        }
    }

    public void setHotTopicsSize(int hotTopicsSize){
        mHotTopicsSize = hotTopicsSize;
    }


    @Override
    public int getItemLayoutId(int viewType) {
        switch (viewType) {
            case TYPE_LATEST:
                return R.layout.item_newest_head;
            case TYPE_ITEM:
                return R.layout.item_newest_topics;
        }

        return 0;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, final Topics item) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM:
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
                break;
            case TYPE_LATEST:
                holder.setText(R.id.tv_head_title, "最新帖子");
                break;
        }
    }

}
