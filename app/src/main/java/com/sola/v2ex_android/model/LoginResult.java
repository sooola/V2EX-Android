package com.sola.v2ex_android.model;

/**
 * Created by wei on 2016/11/11.
 */

public class LoginResult {

    public LoginResult() {

    }

    public LoginResult(String userId, String userAvatar) {
        this.userId = userId;
        this.userAvatar = userAvatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String userId;
    public String userAvatar;
    public String message;

    @Override
    public String toString() {
        return "LoginResult{" +
                "userId='" + userId + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
