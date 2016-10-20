package com.sola.v2ex_android.network;

import com.sola.v2ex_android.network.api.RepliesApi;
import com.sola.v2ex_android.network.api.V2EXApi;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wei on 2016/10/14.
 *
 */
public class NetWork {
    public static String V2EX_URL = "https://www.v2ex.com/api/";

    private static V2EXApi v2exApi;
    private static RepliesApi repliesApi;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static V2EXApi getV2exApi() {
        if (v2exApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(V2EX_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            v2exApi = retrofit.create(V2EXApi.class);
        }
        return v2exApi;
    }

    public static RepliesApi getRepliesApi() {
        if (repliesApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(V2EX_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            repliesApi = retrofit.create(RepliesApi.class);
        }
        return repliesApi;
    }

}
