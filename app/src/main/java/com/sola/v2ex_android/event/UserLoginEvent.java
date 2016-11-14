package com.sola.v2ex_android.event;

/**
 * Created by wei on 2016/11/14.
 */

public class UserLoginEvent {

    public String userName;
    public String userAvatar;

    public UserLoginEvent(String userName , String userAvatar){
        this.userName = userName;
        this.userAvatar = userAvatar;
    }
}
