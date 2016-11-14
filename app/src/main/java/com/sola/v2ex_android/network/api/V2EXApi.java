package com.sola.v2ex_android.network.api;

import com.sola.v2ex_android.model.NodeInfo;
import com.sola.v2ex_android.model.Replies;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.model.UserInfo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wei on 2016/10/14.
 */
public interface V2EXApi {

    @GET("topics/hot.json")     //每天热门
    Observable<List<Topics>> getTopicHot();

    @GET("topics/latest.json")     //每天最新
    Observable<List<Topics>> getTopicLatest();

    @GET("nodes/show.json")     //节点详情
    Observable<NodeInfo> getNodeDetial(@Query("name") String name);

    @GET("nodes/all.json")     //获取所有节点
    Observable<List<NodeInfo>> getAllNode();

    @GET("topics/show.json")       //根据节点名获取所有帖子
    Observable<List<Topics>> getTopicsByNodeName(@Query("node_name") String node_name);

    @GET("replies/show.json")      //帖子回复
    Observable<List<Replies>> getReplise(@Query("topic_id") int topic_id);

    @GET("members/show.json")     //获取用户详情
    Observable<UserInfo> getUserInfo(@Query("username") String username);

    @GET("topics/show.json")   //用户发的帖子
    Observable<List<Topics>> getTopicsByUserName(@Query("username") String username);
}
