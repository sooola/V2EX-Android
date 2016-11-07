package com.sola.v2ex_android.network;

import com.sola.v2ex_android.network.api.UserApi;
import com.sola.v2ex_android.network.api.V2EXApi;
import com.sola.v2ex_android.util.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by wei on 2016/10/14.
 *
 */
public class NetWork {


    private static V2EXApi v2exApi;
    private static UserApi userApi;
    private static UserApi userLoginApi;
    private static UserApi postLoginApi;

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

    public static UserApi getLoginApi() {
        if (userLoginApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Constants.V2EX_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(genericClient())
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            userLoginApi = retrofit.create(UserApi.class);
        }
        return userLoginApi;
    }

    public static UserApi getPostLoginApi() {
        if (postLoginApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Constants.V2EX_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            postLoginApi = retrofit.create(UserApi.class);
        }
        return postLoginApi;
    }

    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .removeHeader("User-Agent")
                                .addHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.2.1; en-us; M040 Build/JOP40D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
                                .addHeader("Cache-Control", "max-age=0")
                                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                                .addHeader("Accept-Charset", "utf-8, iso-8859-1, utf-16, *;q=0.7")
                                .addHeader("Accept-Language", "zh-CN, en-US")
                                .addHeader("Host", "www.v2ex.com")
                                .addHeader("X-Requested-With", "com.android.browser")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }

}
