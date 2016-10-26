package com.sola.v2ex_android.network.api;

import com.sola.v2ex_android.model.Topics;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wei on 2016/10/25.
 */

public interface UserApi {

    @GET("members/show.json")     //获取用户详情
    Observable<List<Topics>> getUserInfo(@Query("username") String username);
}
