package com.sola.v2ex_android.util;

import com.sola.v2ex_android.model.LoginResult;
import com.sola.v2ex_android.model.MyFollowing;
import com.sola.v2ex_android.model.MyFollowingTopicDetial;
import com.sola.v2ex_android.model.MyNode;
import com.sola.v2ex_android.model.Replies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wei on 2016/11/11.
 */

public class JsoupUtil {


    public static LoginResult parseLoginResult(String response) {
        LogUtil.d("JsoupUtil","response" + response);
        LoginResult loginResult = null;
        Element body = Jsoup.parse(response).body();
        Element td = body.select("div#Rightbar").select("div.box").get(0).getElementsByTag("td").get(0);
        String userId = td.getElementsByTag("a").get(0).attr("href").replace("/member/", "");
        String src = "http:" + td.getElementsByTag("img").get(0).attr("src");
         LogUtil.d("JsoupUtil","userid = " + userId + " src = " + src);
        loginResult = new LoginResult(userId, src);
        return loginResult;
    }

    public static HashMap parseUserNameAndPwd(String stringResponse, String username, String password) {
        Element body = Jsoup.parse(stringResponse);
        Elements boxes = body.getElementsByClass("box");
        HashMap params = new HashMap();
        for (Element el : boxes) {
            Elements cell = el.getElementsByClass("cell");
            for (Element c : cell) {
                String nameVal = c.getElementsByAttributeValue("type", "text").attr("name");
                String passwordVal = c.getElementsByAttributeValue("type", "password").attr("name");
                String once = c.getElementsByAttributeValue("name", "once").attr("value");
                if (nameVal.isEmpty() || passwordVal.isEmpty()) continue;
                params.put(nameVal, username);
                params.put("once", once);
                params.put(passwordVal, password);
                break;
            }
        }
        params.put("next", "/");
        return params;
    }

    /**
     * 解析我关注的节点
     * @param response
     * @return
     */
    public static List<MyNode> parseMyNodeInfo(String response){
        List<MyNode> myNodeList = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Element body = doc.body();
        Elements elements = body.getElementsByAttributeValue("id", "MyNodes");
        for (Element el : elements) {
            Elements aNodes = el.getElementsByTag("a");

            for (Element tdNode : aNodes) {
                MyNode mynode = new MyNode();
                String hrefStr = tdNode.attr("href");
                Elements avatarNode = tdNode.getElementsByTag("img");
                if (avatarNode != null) {
                    String avatarString = avatarNode.attr("src");
                    if (avatarString.startsWith("//")) {
                        mynode.imgSrc = "http:" + avatarString;
                    }
                }
                Elements attentionNode = tdNode.getElementsByClass("fade f12");
                mynode.followCount = attentionNode.text();
                mynode.nodeName = hrefStr.substring(4);
                myNodeList.add(mynode);
            }
        }
        return myNodeList;
    }

    public static List<MyFollowing> parseMyfollowing(String response){
        List<MyFollowing> myfollowingList = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Element body = doc.body();
        Element mainElement = body.getElementById("Main");
        Elements itemElements =  mainElement.getElementsByClass("cell item");
        for (Element itemElement : itemElements) {
            MyFollowing myfollow = new MyFollowing();
            Elements titleElements = itemElement.getElementsByClass("item_title");
            Elements nodeElements = itemElement.getElementsByClass("node");
            Elements hrefElements = itemElement.getElementsByAttribute("href");
            String href = titleElements.select("a").attr("href");
            myfollow.topicId = href.substring(3 , href.indexOf("#"));
            myfollow.title = titleElements.text();
            myfollow.nodeName =  nodeElements.text();
            myfollow.userName = hrefElements.first().attr("href").substring(8);
            myfollow.commentCount = itemElement.getElementsByClass("count_livid").text();
            Elements avatarNode = itemElement.getElementsByTag("img");
            if (avatarNode != null) {
                String avatarString = avatarNode.attr("src");
                if (avatarString.startsWith("//")) {
                    myfollow.userIconUrl = "http:" + avatarString;
                }
            }
            myfollowingList.add(myfollow);
        }
        return myfollowingList;

    }

    public static MyFollowingTopicDetial parseMyfollowingTopicDetial(String response){
        MyFollowingTopicDetial topicDetial = new MyFollowingTopicDetial();
        Document doc = Jsoup.parse(response);
        Element body = doc.body();
        Element mainElement = body.getElementById("Main");
        Elements itemElements =  mainElement.getElementsByClass("cell");
        Elements headerElements =  mainElement.getElementsByClass("header");
        Elements commentElements =  mainElement.getElementsByClass("box");
        for (Element headerElement : headerElements) {
            //头部用户信息
            Elements avatarNode = headerElement.getElementsByTag("img");
            Elements aNode = headerElement.getElementsByTag("a");
            if (avatarNode != null) {
                String avatarString = avatarNode.attr("src");
                if (avatarString.startsWith("//")) {
                    topicDetial.avatar = "http:" + avatarString;
                    topicDetial.userName =  aNode.first().attr("href").substring(8);
                }
            }
            String[] s1 = headerElements.select(".gray").text().split(" · ");
            topicDetial.publishTime = s1[1];
            topicDetial.title = headerElement.getElementsByTag("h1").text();
        }
        topicDetial.content = mainElement.select(".markdown_body").html();

        List<Replies> reploeList = new ArrayList<>();
        for (Element cell : commentElements.get(1).getElementsByClass("cell")) {
            Elements avatarNode = cell.select(".avatar");
            if (ValidateUtil.isNotEmpty(avatarNode)) {
                Replies replie = new Replies();
                Replies.MemberEntity member = new Replies.MemberEntity();
                String avatarString = avatarNode.attr("src");
                if (avatarString.startsWith("//")) {
                    member.avatar_normal = "http:" + avatarString;
                }
                member.username = cell.select(".dark").text();
                replie.publishTime = cell.getElementsByClass("fade small").text();
                replie.content = cell.select(".reply_content").html();
                replie.member = member;
                reploeList.add(replie);
            }
        }
        topicDetial.replies = reploeList;
        topicDetial.replies_count = reploeList.size();

        return topicDetial;
    }
}
