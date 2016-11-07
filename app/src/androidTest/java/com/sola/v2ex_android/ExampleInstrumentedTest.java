package com.sola.v2ex_android;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.sola.v2ex_android.util.LogUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.sola.v2ex_android", appContext.getPackageName());

        String url = "http://www.douban.com/accounts/login";

        Connection.Response res = Jsoup.connect(url)
                .data("form_email","xxx@163.com","form_password","xxx")
                .method(Connection.Method.POST)
                .execute();

         LogUtil.d("ExampleInstrumentedTest",res.body());
    }
}
