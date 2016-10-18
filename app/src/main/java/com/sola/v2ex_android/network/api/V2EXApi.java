package com.sola.v2ex_android.network.api;

import com.sola.v2ex_android.model.Topics;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by wei on 2016/10/14.
 */
public interface V2EXApi {
//    @GET("data/福利/{number}/{page}")
    @GET("topics/hot.json")
    Observable<List<Topics>> getTopicHot();
}
