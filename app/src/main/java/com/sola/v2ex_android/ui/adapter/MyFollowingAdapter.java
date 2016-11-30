package com.sola.v2ex_android.ui.adapter;

import android.content.Context;
import android.view.View;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.MyFollowing;
import com.sola.v2ex_android.ui.MyFollowingTopicDetialActivity;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerViewHolder;
import com.sola.v2ex_android.util.GlideUtil;

/**
 * Created by wei on 2016/11/15.
 */

public class MyFollowingAdapter extends BaseRecyclerAdapter<MyFollowing> {

    public MyFollowingAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_following;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, final MyFollowing item) {
        holder.setText(R.id.tv_title , item.title);
        holder.setText(R.id.tv_username , item.userName);
        holder.setText(R.id.tv_review_count , item.commentCount);
        holder.setText(R.id.tv_node_name , item.nodeName);
        GlideUtil.glideWithImg(mContext , item.userIconUrl , holder.getImageView(R.id.tv_user_icon));

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(MyFollowingTopicDetialActivity.getIntent(v.getContext(),item.topicId));
            }
        });
    }
}
