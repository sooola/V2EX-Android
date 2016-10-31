package com.sola.v2ex_android.network;

import com.sola.v2ex_android.network.api.UserApi;
import com.sola.v2ex_android.network.api.V2EXApi;
import com.sola.v2ex_android.util.Constants;

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


    private static V2EXApi v2exApi;
    private static UserApi userApi;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static V2EXApi getV2exApi() {
        if (v2exApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Constants.V2EX_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            v2exApi = retrofit.create(V2EXApi.class);
        }
        return v2exApi;
    }

    public static UserApi getUserApi() {
        if (userApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Constants.V2EX_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            userApi = retrofit.create(UserApi.class);
        }
        return userApi;
    }

}
