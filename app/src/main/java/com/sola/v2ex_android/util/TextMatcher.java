package com.sola.v2ex_android.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wei on 2016/10/19.
 */

public class TextMatcher {

    private static Pattern IMAGE_TAG_PATTERN = Pattern.compile("\\<img(.*?)\\>");
    private static Pattern IMAGE_SRC_PATTERN = Pattern.compile("src=\"(.*?)\"");

    /**
     * 匹配富文本中的图片
     * @param content_rendered
     * @return
     */
    public static List<String>  matchImg(String content_rendered){
        Matcher imageMatcher, srcMatcher;
        List<String> imgList = new ArrayList<>();
        imageMatcher = IMAGE_TAG_PATTERN.matcher(content_rendered);
        while(imageMatcher.find()) {
            String image = imageMatcher.group().trim();
            srcMatcher = IMAGE_SRC_PATTERN.matcher(image);
            String src = null;
            if (srcMatcher.find()) {
                src = getTextBetweenQuotation(srcMatcher.group().trim().substring(4));
            }
            if (TextUtils.isEmpty(src)) {
                continue;
            }
            imgList.add(src);
        }
        return imgList;
    }


    private static String getTextBetweenQuotation(String text) {
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
