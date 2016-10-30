package com.sola.v2ex_android.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.NodeChildren;
import com.sola.v2ex_android.model.NodeGroup;
import com.sola.v2ex_android.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei on 2016/10/28.
 */

public class NodeAdapter implements ExpandableListAdapter {

    private List<NodeGroup> nodeGroup = new ArrayList<>();
    private LayoutInflater mInflater;

    private Context mContext;

    public NodeAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

//        for (int i = 0; i < 2; i++) {
//            NodeGroup group = new NodeGroup();
//            group.groupTitle = "西游记";
//            group.childrenList = new ArrayList<>();
//            for (int j = 0; j < 2; j++) {
//                NodeChildren children = new NodeChildren();
//                children.title = "唐三藏";
//                group.childrenList.add(children);
//            }
//            nodeGroup.add(group);
//        }
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return nodeGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return nodeGroup.get(groupPosition).childrenList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return nodeGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return nodeGroup.get(groupPosition).childrenList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return this.nodeGroup.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_node_expan_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
//            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        LogUtil.d("NodeAdapter", "groupPosition" + groupPosition);
        groupViewHolder.tvTitle.setText(nodeGroup.get(groupPosition).groupTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        ChildViewHolder childViewHolder;
//        if (convertView != null) {
//            return convertView;
//        }
//        convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_node_expan_child_item, parent, false);
//        childViewHolder = new ChildViewHolder();
//        childViewHolder.childContent = (LinearLayout) convertView.findViewById(R.id.child_content);
//        convertView.setTag(childViewHolder);
//        return convertView;

//        ChildViewHolder childViewHolder;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_node_expan_child_item, parent, false);
//            childViewHolder = new ChildViewHolder();
//            childViewHolder.childContent = (LinearLayout) convertView.findViewById(R.id.child_content);
//            convertView.setTag(childViewHolder);
//        } else {
//            childViewHolder = (ChildViewHolder) convertView.getTag();
//        }
//        childViewHolder.childContent.removeAllViews();
//        List<NodeChildren> nodechildrenList = nodeGroup.get(groupPosition).childrenList;
//        LogUtil.d("NodeAdapter"," getChildView -- groupPosition" + groupPosition);
//         LogUtil.d("NodeAdapter","getChildView -- nodechildrenList size" + nodechildrenList.size());
//        for (int i = 0; i < nodechildrenList.size(); i++) {
//             LogUtil.d("NodeAdapter","i" + i);
//           View childContent =  mInflater.inflate(R.layout.layout_node_expan_child_content , childViewHolder.childContent , false);
//            ((TextView)childContent.findViewById(R.id.tv_node_title)).setText(nodechildrenList.get(i).title);
//            childViewHolder.childContent.addView(childContent);
//        }

//        childViewHolder.tvTitle.setText( nodeGroup.get(groupPosition).childrenList.get(childPosition).title);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }

    static class ChildViewHolder {
        LinearLayout childContent;
    }
}
