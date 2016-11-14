package com.sola.v2ex_android.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sola.v2ex_android.network.api.V2EXApi;
import com.sola.v2ex_android.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wei on 2016/11/14.
 */

public class V2exService {

    private static V2exService mInstance;
    private Gson mGson;
    private OkHttpClient mClient;
    private V2EXApi mV2exApi;

    public static V2exService getInstance() {
        if (mInstance == null) {
            mInstance = new V2exService();
        }
        return mInstance;
    }

    public V2exService(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mClient = new OkHttpClient.Builder()
                .readTimeout(12, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .serializeNulls()
                .create();
    }

    public V2EXApi getV2exApi() {
        if (mV2exApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.V2EX_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .client(mClient)
                    .build();
            mV2exApi = retrofit.create(V2EXApi.class);
        }
        return mV2exApi;
    }
}
