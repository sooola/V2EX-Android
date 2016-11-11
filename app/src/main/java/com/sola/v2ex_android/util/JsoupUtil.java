package com.sola.v2ex_android.util;

import com.sola.v2ex_android.model.LoginResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

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
}
