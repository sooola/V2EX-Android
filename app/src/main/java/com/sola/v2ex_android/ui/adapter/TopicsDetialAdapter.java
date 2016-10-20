package com.sola.v2ex_android.ui.adapter;

import android.content.Context;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.Replies;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerAdapter;
import com.sola.v2ex_android.ui.base.adapter.BaseRecyclerViewHolder;
import com.sola.v2ex_android.util.ContentUtils;
import com.sola.v2ex_android.util.GlideUtil;
import com.sola.v2ex_android.util.TimeUtil;
import com.zzhoujay.richtext.RichText;

/**
 * Created by wei on 2016/10/20.
 */

public class TopicsDetialAdapter extends BaseRecyclerAdapter<Replies> {

    private Topics mTopics;

    public TopicsDetialAdapter(Context context , Topics topics) {
        super(context);
        mTopics = topics;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 ){
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
            Topics.MemberEntity member = mTopics.member;
            GlideUtil.glideWithImg(mContext ,"http:"+member.avatar_normal ,holder.getImageView(R.id.iv_user_icon));
            holder.setText(R.id.tv_username , member.username);
            holder.setText(R.id.tv_title , mTopics.title);
            holder.setText(R.id.tv_publish_time , TimeUtil.friendlyFormat(mTopics.created * 1000));
            RichText.from(ContentUtils.formatContent(mTopics.content_rendered)).into(holder.getTextView(R.id.tv_content));
            holder.setText(R.id.tv_replies_count , String.format(mContext.getResources().getString(R.string.replies_count) ,mTopics.replies));
        }else {
            Replies.MemberEntity repliMember = item.member;
            GlideUtil.glideWithImg(mContext ,"http:"+repliMember.avatar_normal ,holder.getImageView(R.id.iv_user_icon));
            holder.setText(R.id.tv_username , repliMember.username);
            holder.setText(R.id.tv_comment_content , item.content);
        }
    }
}
