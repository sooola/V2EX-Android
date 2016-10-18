package com.sola.v2ex_android.model;

/**
 * Created by wei on 2016/10/18.
 */

public class Topics {

    public int id;
    public String title;
    public String url;
    public String content;
    public String content_rendered;
    public int replies;
    public MemberEntity member;
    public NodeEntity node;
    public int created;
    public int last_modified;
    public int last_touched;

    public static class MemberEntity {
        public int id;
        public String username;
        public String tagline;
        public String avatar_mini;
        public String avatar_normal;
        public String avatar_large;
    }

    public static class NodeEntity {
        public int id;
        public String name;
        public String title;
        public String title_alternative;
        public String url;
        public int topics;
        public String avatar_mini;
        public String avatar_normal;
        public String avatar_large;
    }
}
