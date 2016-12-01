package com.sola.v2ex_android.ui.adapter;

import android.content.Context;
import android.view.View;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.MyFollowingTopicDetial;
import com.sola.v2ex_android.model.Replies;
import com.sola.v2ex_android.ui.UserDetialActivity;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerViewHolder;
import com.sola.v2ex_android.util.ContentUtils;
import com.sola.v2ex_android.util.GlideUtil;
import com.sola.v2ex_android.util.ValidateUtil;
import com.zzhoujay.richtext.RichText;

/**
 * Created by wei on 2016/11/28.
 */

public class MyFollowingTopicDetialAdapter  extends BaseRecyclerAdapter<Replies> {

    private MyFollowingTopicDetial mTopics;

    public MyFollowingTopicDetialAdapter(Context context) {
        super(context);
    }

    public void setTopicDetial(MyFollowingTopicDetial topics){
        mTopics = topics;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && ValidateUtil.isNotEmpty(mTopics)){
            return TYPE_HEADER;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() + 1:0;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == TYPE_HEADER){
            return R.layout.item_topics_detial_head;
        }else {
            return R.layout.item_topics_detial;
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER){
            bindData(holder, position, null);
        }else {
            int relativePosition = position - 1;
            bindData(holder, position, mData.get(relativePosition));
        }
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, Replies item) {
        if (getItemViewType(position) == TYPE_HEADER){
            GlideUtil.glideWithImg(mContext , mTopics.avatar ,holder.getImageView(R.id.iv_user_icon));
            holder.setText(R.id.tv_username , mTopics.userName);
            holder.setText(R.id.tv_title , mTopics.title);
            holder.setText(R.id.tv_publish_time , mTopics.publishTime);
            RichText.from(ContentUtils.formatContent(mTopics.content)).into(holder.getTextView(R.id.tv_content));
            holder.setText(R.id.tv_replies_count , String.format(mContext.getResources().getString(R.string.replies_count) ,mTopics.replies_count));
            holder.setOnClickListener(R.id.iv_user_icon, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(UserDetialActivity.getIntent(v.getContext() ,mTopics.userName ));
                }
            });
        }else {
            final Replies.MemberEntity repliMember = item.member;
            GlideUtil.glideWithImg(mContext ,repliMember.avatar_normal ,holder.getImageView(R.id.iv_user_icon));
            holder.setText(R.id.tv_username , repliMember.username);
            RichText.from(ContentUtils.formatContent(item.content)).into(holder.getTextView(R.id.tv_comment_content));
            holder.setText(R.id.tv_time , item.publishTime);
            holder.setOnClickListener(R.id.iv_user_icon, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(UserDetialActivity.getIntent(v.getContext() ,repliMember.username ));
                }
            });
        }
    }
}
