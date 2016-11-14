package com.sola.v2ex_android.model;

/**
 * Created by wei on 2016/11/14.
 */

public class MyNode {

    public String imgSrc;
    public String nodeName;
    public String followCount;

    @Override
    public String toString() {
        return "MyNode{" +
                "imgSrc='" + imgSrc + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", collectNumber='" + followCount + '\'' +
                '}';
    }
}
