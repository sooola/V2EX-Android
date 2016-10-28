package com.sola.v2ex_android.ui;

import android.view.View;
import android.widget.ExpandableListView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.ui.adapter.NodeAdapter;
import com.sola.v2ex_android.ui.base.BaseFragment;
import com.sola.v2ex_android.util.ToastUtil;

import butterknife.Bind;

/**
 * 主页 - 节点
 * Created by wei on 2016/10/27.
 */

public class NodeFragment extends BaseFragment {

    @Bind(R.id.expand_list)
    ExpandableListView mExpandableListView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_node;
    }

    @Override
    protected void initViews() {
        mExpandableListView.setAdapter(new NodeAdapter(getActivity()));

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                ToastUtil.showShort("i" + i);
                return false;
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                ToastUtil.showShort("groupPosition" + groupPosition + " , childPosition" + childPosition);
                return false;
            }
        });

    }

}
