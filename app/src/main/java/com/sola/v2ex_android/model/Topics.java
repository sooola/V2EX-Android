package com.sola.v2ex_android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wei on 2016/10/18.
 */

public class Topics implements Serializable {

    public int id;
    public String title;
    public String nodeTitle;
    public String url;
    public String content;
    public String content_rendered;
    public int replies;
    public MemberEntity member;
    public NodeEntity node;
    public long created;
    public int last_modified;
    public int last_touched;
    public List<String> imgList;

    @Override
    public String toString() {
        return "Topics{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", content_rendered='" + content_rendered + '\'' +
                ", replies=" + replies +
                ", member=" + member +
                ", node=" + node +
                ", created=" + created +
                ", last_modified=" + last_modified +
                ", last_touched=" + last_touched +
                ", imgList=" + imgList +
                '}';
    }

    public static class MemberEntity implements Serializable {
        public int id;
        public String username;
        public String tagline;
        public String avatar_mini;
        public String avatar_normal;
        public String avatar_large;
    }

    public static class NodeEntity implements Serializable {
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
