package com.sola.v2ex_android.network.api;

import com.sola.v2ex_android.model.Replies;
import com.sola.v2ex_android.model.Topics;
import com.sola.v2ex_android.model.UserInfo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wei on 2016/10/20.
 */

public interface UserApi {

    @GET("replies/show.json")      //帖子回复
    Observable<List<Replies>> getReplise(@Query("topic_id") int topic_id);

    @GET("members/show.json")     //获取用户详情
    Observable<UserInfo> getUserInfo(@Query("username") String username);

    @GET("topics/show.json")   //用户发的帖子
    Observable<List<Topics>> getTopicsByUserName(@Query("username") String username);
}
