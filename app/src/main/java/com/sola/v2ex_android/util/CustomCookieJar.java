package com.sola.v2ex_android.util;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.internal.Internal;

import static java.util.logging.Level.WARNING;
import static okhttp3.internal.Util.delimiterOffset;
import static okhttp3.internal.Util.trimSubstring;

/**
 * Created by wei on 2016/11/11.
 */

public class CustomCookieJar implements CookieJar {

    private final CookieHandler mCookieHandler;

    public CustomCookieJar(CookieHandler cookieHandler) {
        this.mCookieHandler = cookieHandler;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (mCookieHandler != null) {
            List<String> cookieStrings = new ArrayList<>();
            for (Cookie cookie : cookies) {
                cookieStrings.add(cookie.toString());
            }
            Map<String, List<String>> multimap = Collections.singletonMap("Set-Cookie", cookieStrings);
            try {
                mCookieHandler.put(url.uri(), multimap);
            } catch (IOException e) {
                Internal.logger.log(WARNING, "Saving cookies failed for " + url.resolve("/..."), e);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        // The RI passes all headers. We don't have 'em, so we don't pass 'em!
        Map<String, List<String>> headers = Collections.emptyMap();
        Map<String, List<String>> cookieHeaders;
        try {
            cookieHeaders = mCookieHandler.get(url.uri(), headers);
        } catch (IOException e) {
            Internal.logger.log(WARNING, "Loading cookies failed for " + url.resolve("/..."), e);
            return Collections.emptyList();
        }

        List<Cookie> cookies = null;
        for (Map.Entry<String, List<String>> entry : cookieHeaders.entrySet()) {
            String key = entry.getKey();
            if (("Cookie".equalsIgnoreCase(key) || "Cookie2".equalsIgnoreCase(key))
                    && !entry.getValue().isEmpty()) {
                for (String header : entry.getValue()) {
                    if (cookies == null) cookies = new ArrayList<>();
                    cookies.addAll(decodeHeaderAsJavaNetCookies(url, header));
                }
            }
        }

        return cookies != null
                ? Collections.unmodifiableList(cookies)
                : Collections.<Cookie>emptyList();
    }

    /**
     * Convert a request header to OkHttp's cookies via {@link HttpCookie}. That extra step handles
     * multiple cookies in a single request header, which {@link Cookie#parse} doesn't support.
     */
    private List<Cookie> decodeHeaderAsJavaNetCookies(HttpUrl url, String header) {
        List<Cookie> result = new ArrayList<>();
        for (int pos = 0, limit = header.length(), pairEnd; pos < limit; pos = pairEnd + 1) {
            pairEnd = delimiterOffset(header, pos, limit, ";,");
            int equalsSign = delimiterOffset(header, pos, pairEnd, '=');
            String name = trimSubstring(header, pos, equalsSign);
            if (name.startsWith("$")) continue;

            // We have either name=value or just a name.
            String value = equalsSign < pairEnd
                    ? trimSubstring(header, equalsSign + 1, pairEnd)
                    : "";

            // If the value is "quoted", drop the quotes.
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }

            result.add(new Cookie.Builder()
                    .name(name)
                    .value(value)
                    .domain(url.host())
                    .build());
        }
        return result;
    }
}
