package com.sola.v2ex_android.ui.base.application;

import android.app.Application;
import android.content.Context;

import com.sola.v2ex_android.model.V2exUser;
import com.sola.v2ex_android.util.SharedPreferencesUtils;
import com.sola.v2ex_android.util.ToastUtil;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //获取Context
        context = getApplicationContext();
        ToastUtil.init(this);
        SharedPreferencesUtils.init(this);
        V2exUser.init();
    }

    //返回
    public static Context getContextObject() {
        return context;
    }



}
