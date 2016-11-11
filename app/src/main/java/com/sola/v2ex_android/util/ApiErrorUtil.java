package com.sola.v2ex_android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wei on 2016/11/11.
 */

public class ApiErrorUtil {

    /**
     * 登陆报错解析
     *
     * @param response
     * @return
     */
    public static String getErrorMsg(String response) {
        Pattern errorPattern = Pattern.compile("<div class=\"problem\">(.*)</div>");
        Matcher errorMatcher = errorPattern.matcher(response);
        String errorContent;
        if (errorMatcher.find()) {
            errorContent = errorMatcher.group(1).replaceAll("<[^>]+>", "");
        } else {
            errorContent = null;
        }

        return errorContent;
    }
}
