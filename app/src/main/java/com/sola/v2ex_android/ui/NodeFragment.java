package com.sola.v2ex_android.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.NodeChildren;
import com.sola.v2ex_android.model.NodeGroup;
import com.sola.v2ex_android.model.NodeInfo;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.network.NetWork;
import com.sola.v2ex_android.ui.adapter.NodeAdapter;
import com.sola.v2ex_android.ui.base.BaseFragment;
import com.sola.v2ex_android.util.Constants;
import com.sola.v2ex_android.util.ContentUtils;
import com.sola.v2ex_android.util.GlideUtil;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.ToastUtil;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 主页 - 节点
 * Created by wei on 2016/10/27.
 */

public class NodeFragment extends BaseFragment {

    @Bind(R.id.ll_root)
    LinearLayout mContentLl;

    private Map<String, List<NodeInfo>> nodeSortMap = new LinkedHashMap<>();


    Observer<List<NodeInfo>> observer = new Observer<List<NodeInfo>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            LogUtil.d("UserDetialActivity", "e" + e.toString());
            ToastUtil.showShort(R.string.loading_failed);
        }

        @Override
        public void onNext(List<NodeInfo> items) {
            List<NodeInfo> technologyGroup = new ArrayList<>();
            List<NodeInfo> creativeGroup = new ArrayList<>();
            List<NodeInfo> funGroup = new ArrayList<>();
            List<NodeInfo> coolJodGroup = new ArrayList<>();
            List<NodeInfo> transactionGroup = new ArrayList<>();

            for (NodeInfo item : items) {
                if (item.title.equals("程序员") || item.title.equals("Python") || item.title.equals("iDev") ||
                        item.title.equals("Android") || item.title.equals("Linux") || item.title.equals("node.js")
                        || item.title.equals("云计算")
                        ) {
                    technologyGroup.add(item);
                }
                if (item.title.equals("分享创造") || item.title.equals("设计") || item.title.equals("奇思妙想")) {
                    creativeGroup.add(item);
                }
                if (item.title.equals("分享发现") || item.title.equals("电子游戏") || item.title.equals("电影")
                        || item.title.equals("旅行")
                        ) {
                    funGroup.add(item);
                }

                if (item.title.equals("酷工作") || item.title.equals("求职") || item.title.equals("职场话题")
                        ) {
                    coolJodGroup.add(item);
                }
                if (item.title.equals("二手交易") || item.title.equals("物物交换") || item.title.equals("免费赠送") || item.title.equals("域名")
                        ) {
                    transactionGroup.add(item);
                }
            }
            nodeSortMap.put("技术", technologyGroup);
            nodeSortMap.put("创意", creativeGroup);
            nodeSortMap.put("好玩", funGroup);
            nodeSortMap.put("酷工作", coolJodGroup);
            nodeSortMap.put("交易", transactionGroup);

            initView();
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_node;
    }

    @Override
    protected void initViews() {
        loadData();
    }

    private void initView() {
        Iterator iter = nodeSortMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String groupTitle = (String) entry.getKey();
            final List<NodeInfo> childrenList = (List<NodeInfo>) entry.getValue();

            final View groupItem = LayoutInflater.from(getActivity()).inflate(R.layout.layout_node_expan_group_item, mContentLl, false);
            ((TextView) groupItem.findViewById(R.id.tv_group_title)).setText(groupTitle);
            final LinearLayout childrenContent = (LinearLayout) groupItem.findViewById(R.id.ll_expand_children_content);
            final View line =  groupItem.findViewById(R.id.line);

            groupItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    childrenContent.removeAllViews();
                    if (null != view.getTag(R.id.group_item)) {
                        if ((boolean) view.getTag(R.id.group_item)) {
                            //关闭状态
                            view.setTag(R.id.group_item, false);
                            childrenContent.setVisibility(View.GONE);
                            line.setVisibility(View.VISIBLE);
                        } else {
                            //打开状态
                            view.setTag(R.id.group_item, true);
                            childrenContent.setVisibility(View.VISIBLE);
                            line.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        //第一次展开
                        view.setTag(R.id.group_item, true);
                        childrenContent.setVisibility(View.VISIBLE);
                        line.setVisibility(View.INVISIBLE);
                    }

                    for (int i = 0; i < childrenList.size(); i++) {
                        //添加子节点
                        final NodeInfo nodeinfo =  childrenList.get(i);
                        View childContent = LayoutInflater.from(getActivity()).inflate(R.layout.layout_node_expan_child_content, childrenContent, false);
                        ((TextView) childContent.findViewById(R.id.tv_node_title_left)).setText(nodeinfo.title);
                        RichText.from(nodeinfo.header).into(((TextView) childContent.findViewById(R.id.tv_header_left)));

                        ((TextView) childContent.findViewById(R.id.tv_topics_count_left)).setText(String.format(childContent.getResources().getString(R.string.topics_count) ,nodeinfo.topics));
                        childContent.findViewById(R.id.rl_left).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ToastUtil.showShort(nodeinfo.title);
                            }
                        });

                        if (i+ 1 != childrenList.size()){
                            final NodeInfo nextNodeinfo = childrenList.get(i + 1);
                            ((TextView) childContent.findViewById(R.id.tv_node_title_right)).setText(nextNodeinfo.title);
                            RichText.from(nextNodeinfo.header).into(((TextView) childContent.findViewById(R.id.tv_header_right)));
                            ((TextView) childContent.findViewById(R.id.tv_topics_count_left)).setText(String.format(childContent.getResources().getString(R.string.topics_count) ,nextNodeinfo.topics));
                            childContent.findViewById(R.id.rl_right).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ToastUtil.showShort(nextNodeinfo.title);
                                }
                            });

                            i++;
                        }else {
                            childContent.findViewById(R.id.rl_right).setVisibility(View.INVISIBLE);
                        }
                        childrenContent.addView(childContent);
                    }
                }
            });
            mContentLl.addView(groupItem);
        }
    }

    private void loadData() {
        Subscription subscription = NetWork.getV2exApi().getAllNode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addSubscription(subscription);
    }

}
