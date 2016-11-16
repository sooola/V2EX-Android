package com.sola.v2ex_android.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.NodeInfo;
import com.sola.v2ex_android.network.V2exService;
import com.sola.v2ex_android.ui.base.BaseSwipeRefreshFragment;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.NodeDataUtil;
import com.sola.v2ex_android.util.ToastUtil;
import com.zzhoujay.richtext.RichText;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 主页 - 节点
 * Created by wei on 2016/10/27.
 */

public class NodeFragment extends BaseSwipeRefreshFragment {

    private String[] mTechnologyGroupTitle = new String[] { "程序员", "Python", "iDev", "Android","Linux" ,"node.js"};
    private String[] mCreativeGroupTitle = new String[] { "分享创造", "设计", "奇思妙想"};
    private String[] mFunGroupTitle = new String[] { "分享发现", "电子游戏", "电影" , "旅行"};
    private String[] mCoolGroupTitle = new String[] { "酷工作", "求职", "职场话题"};
    private String[] mTransactionGroupTitle = new String[] { "二手交易", "物物交换", "免费赠送" , "域名"};
    private boolean mIsFirst = true;


    public static NodeFragment newInstance() {
        return new NodeFragment();
    }

    @Bind(R.id.ll_root)
    LinearLayout mContentLl;

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
            Map<String, List<NodeInfo>> nodeSortMap = NodeDataUtil.filterDataToGroup(items , NodeDataUtil.createGroup("技术" ,mTechnologyGroupTitle)
                    ,NodeDataUtil.createGroup("创意" ,mCreativeGroupTitle)
                    ,NodeDataUtil.createGroup("好玩" ,mFunGroupTitle)
                    ,NodeDataUtil.createGroup("酷工作" ,mCoolGroupTitle)
                    ,NodeDataUtil.createGroup("交易" ,mTransactionGroupTitle)
            );
            initData(nodeSortMap);
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_node;
    }

    @Override
    public void initViews(View view) {
        super.initViews(view);
    }

    private void initData(Map<String, List<NodeInfo>> nodeSortMap) {
        Iterator iter = nodeSortMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String groupTitle = (String) entry.getKey();
            final List<NodeInfo> childrenList = (List<NodeInfo>) entry.getValue();

            final View groupItem = LayoutInflater.from(getActivity()).inflate(R.layout.layout_node_expan_group_item, mContentLl, false);
            ((TextView) groupItem.findViewById(R.id.tv_group_title)).setText(groupTitle);
            final LinearLayout childrenContent = (LinearLayout) groupItem.findViewById(R.id.ll_expand_children_content);
            final ImageView arrowIcon = (ImageView) groupItem.findViewById(R.id.iv_arrow_icon);
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
                            arrowIcon.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                        } else {
                            //打开状态
                            view.setTag(R.id.group_item, true);
                            childrenContent.setVisibility(View.VISIBLE);
                            line.setVisibility(View.INVISIBLE);
                            arrowIcon.setImageResource(R.drawable.ic_expand_more_black_24dp);
                        }
                    } else {
                        //第一次展开
                        view.setTag(R.id.group_item, true);
                        childrenContent.setVisibility(View.VISIBLE);
                        arrowIcon.setImageResource(R.drawable.ic_expand_more_black_24dp);
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
                                view.getContext().startActivity(NodeDetialActivity.getIntent(view.getContext() ,nodeinfo.name));
                            }
                        });

                        //一排显示2个子内容
                        if (i+ 1 != childrenList.size()){
                            final NodeInfo nextNodeinfo = childrenList.get(i + 1);
                            ((TextView) childContent.findViewById(R.id.tv_node_title_right)).setText(nextNodeinfo.title_alternative);
                            RichText.from(nextNodeinfo.header).into(((TextView) childContent.findViewById(R.id.tv_header_right)));
                            ((TextView) childContent.findViewById(R.id.tv_topics_count_left)).setText(String.format(childContent.getResources().getString(R.string.topics_count) ,nextNodeinfo.topics));
                            childContent.findViewById(R.id.rl_right).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    view.getContext().startActivity(NodeDetialActivity.getIntent(view.getContext() ,nextNodeinfo.name));
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
            if (mIsFirst){
                //默认展开第一项
                groupItem.performClick();
                mIsFirst = false;
            }

            mContentLl.addView(groupItem);
        }
    }

    @Override
    public void loadData() {
        Subscription subscription = V2exService.getInstance().getV2exApi().getAllNode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addSubscription(subscription);
    }

    @Override
    public void onRefresh() {
        mContentLl.removeAllViews();
        loadData();
    }
}
