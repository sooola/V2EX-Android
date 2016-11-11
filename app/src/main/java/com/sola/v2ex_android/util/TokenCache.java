package com.sola.v2ex_android.util;

/**
 * Created by wei on 2016/11/11.
 */

public class TokenCache {

    public static void saveToken(String token) {
        SharedPreferencesUtils.setParam("tokenCache", token);
    }

    public static String getToken() {
        return (String) SharedPreferencesUtils.getParam("tokenCache", "");
    }


    public static void clearToken() {
        SharedPreferencesUtils.setParam("tokenCache", "");
    }
}
