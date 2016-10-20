package com.sola.v2ex_android.model;

/**
 * Created by wei on 2016/10/20.
 */

public class Replies {

    public int id;
    public int thanks;
    public String content;
    public String content_rendered;
    public MemberEntity member;
    public int created;
    public int last_modified;

    @Override
    public String toString() {
        return "Replies{" +
                "id=" + id +
                ", thanks=" + thanks +
                ", content='" + content + '\'' +
                ", content_rendered='" + content_rendered + '\'' +
                ", member=" + member +
                ", created=" + created +
                ", last_modified=" + last_modified +
                '}';
    }

    public static class MemberEntity {
        public int id;
        public String username;
        public String tagline;
        public String avatar_mini;
        public String avatar_normal;
        public String avatar_large;
    }
}
