package com.sola.v2ex_android.util;

import com.sola.v2ex_android.model.NodeGroup;
import com.sola.v2ex_android.model.NodeInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wei on 2016/10/31.
 */

public class NodeDataUtil {


    /**
     * 筛选分组内容
     * @param items
     * @param nodeGroups
     * @return
     */
    public static Map<String, List<NodeInfo>>  filterDataToGroup (List<NodeInfo> items, NodeGroup... nodeGroups) {
        Map<String, List<NodeInfo>> nodeFilterMap = new LinkedHashMap<>();
        for (NodeGroup nodeGroup : nodeGroups) {
            List<NodeInfo> childrenGroup = new ArrayList<>();
            for (NodeInfo item : items) {
                if (nodeGroup.childrenTitleList.contains(item.title)){
                    childrenGroup.add(item);
                }
            }
            nodeFilterMap.put(nodeGroup.groupTitle ,childrenGroup);
        }
        return nodeFilterMap;
    }

    /**
     * 创建一个分组 包含标题 和子内容
     * @param groupTitle
     * @param childrenTitle
     * @return
     */
    public static NodeGroup createGroup(String groupTitle, String[] childrenTitle) {
        NodeGroup nodeGroup = new NodeGroup();
        nodeGroup.groupTitle = groupTitle;
        nodeGroup.childrenTitleList = Arrays.asList(childrenTitle);
        return nodeGroup;
    }
}
