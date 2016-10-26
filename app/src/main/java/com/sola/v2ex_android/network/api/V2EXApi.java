package com.sola.v2ex_android.network.api;

import com.sola.v2ex_android.model.NodeDetial;
import com.sola.v2ex_android.model.Topics;

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

    @GET("nodes/show.json")     //节点详情
    Observable<NodeDetial> getNodeDetial(@Query("name") String name);
}
