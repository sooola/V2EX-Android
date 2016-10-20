package com.sola.v2ex_android.network.api;

import com.sola.v2ex_android.model.Replies;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wei on 2016/10/20.
 */

public interface RepliesApi {

    @GET("/api/replies/show.json")
    Observable<List<Replies>> getReplise(@Query("topic_id") int topic_id);
}
