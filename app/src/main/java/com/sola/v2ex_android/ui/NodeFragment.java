package com.sola.v2ex_android.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.NodeChildren;
import com.sola.v2ex_android.model.NodeGroup;
import com.sola.v2ex_android.ui.adapter.NodeAdapter;
import com.sola.v2ex_android.ui.base.BaseFragment;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 主页 - 节点
 * Created by wei on 2016/10/27.
 */

public class NodeFragment extends BaseFragment {

    @Bind(R.id.ll_root)
    LinearLayout mContentLl;

    private List<NodeGroup> nodeGroup = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_node;
    }

    @Override
    protected void initViews() {

        for (int i = 0; i < 2; i++) {
            NodeGroup group = new NodeGroup();
            group.groupTitle = "西游记";
            group.childrenList = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                NodeChildren children = new NodeChildren();
                children.title = "唐三藏";
                group.childrenList.add(children);
            }
            nodeGroup.add(group);
        }
        for (int i = 0; i < nodeGroup.size(); i++) {
           View groupItem = LayoutInflater.from(getActivity()).inflate(R.layout.layout_node_expan_group_item, mContentLl, false);
            ((TextView)groupItem.findViewById(R.id.tv_group_title)).setText(nodeGroup.get(i).groupTitle);
            final LinearLayout childrenContent = (LinearLayout) groupItem.findViewById(R.id.ll_expand_children_content);
            groupItem.setTag(i);

            groupItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    childrenContent.removeAllViews();
                    int groupPosition = (int) view.getTag();
                    if (null != view.getTag(R.id.group_item)){
                        if ((boolean)view.getTag(R.id.group_item)){
                            view.setTag(R.id.group_item, false);
                            childrenContent.setVisibility(View.GONE);
                        }else {
                            view.setTag(R.id.group_item, true);
                            childrenContent.setVisibility(View.VISIBLE);
                        }
                    }else {
                        //第一次展开
                        view.setTag(R.id.group_item, true);
                        childrenContent.setVisibility(View.VISIBLE);
                    }


                    List<NodeChildren> nodechildrenList = nodeGroup.get(groupPosition).childrenList;
                    for (int i = 0; i < nodechildrenList.size(); i++) {
                        LogUtil.d("NodeAdapter","i" + i);
                        View childContent =  LayoutInflater.from(getActivity()).inflate(R.layout.layout_node_expan_child_content , childrenContent , false);
                        ((TextView)childContent.findViewById(R.id.tv_node_title)).setText(nodechildrenList.get(i).title);
                        childrenContent.addView(childContent);
                    }
                }
            });
            mContentLl.addView(groupItem);
        }


//        mExpandableListView.setAdapter(new NodeAdapter(getActivity()));
//
//        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
//                ToastUtil.showShort("i" + i);
//                return false;
//            }
//        });
//        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                ToastUtil.showShort("groupPosition" + groupPosition + " , childPosition" + childPosition);
//                return true;
//            }
//        });

    }

}
