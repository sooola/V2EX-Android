package com.sola.v2ex_android.model;

import com.sola.v2ex_android.network.LoginService;
import com.sola.v2ex_android.util.SharedPreferencesUtils;

/**
 * Created by wei on 2016/11/14.
 */

public class V2exUser {

    private static V2exUser currentUser;

    public String message;

    public String userId;

    public String userAvatar;

    public static V2exUser getCurrentUser() {
        return currentUser;
    }

    public static void init(){
        if (null == currentUser && !"".equals((String)SharedPreferencesUtils.getParam("user_id" , ""))){
            V2exUser v2exUser = new V2exUser();
            v2exUser.userId = (String) SharedPreferencesUtils.getParam("user_id" , "");
            v2exUser.userAvatar = (String) SharedPreferencesUtils.getParam("user_avatar" , "");
            v2exUser.message = (String) SharedPreferencesUtils.getParam("message" , "");
            currentUser = v2exUser;
        }
    }

    public static void saveCurrentUser(LoginResult loginResult){
        SharedPreferencesUtils.setParam("user_id" , loginResult.userId);
        SharedPreferencesUtils.setParam("user_avatar" , loginResult.userAvatar);
        SharedPreferencesUtils.setParam("message" , loginResult.message);
        V2exUser v2exUser = new V2exUser();
        v2exUser.userId = loginResult.userId;
        v2exUser.userAvatar = loginResult.userAvatar;
        v2exUser.message = loginResult.message;
        currentUser = v2exUser;
    }

    public static void logout(){
        SharedPreferencesUtils.clear();
        currentUser = null;
        LoginService.getInstance().clearCookies();
    }

}
