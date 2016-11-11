package com.sola.v2ex_android.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by wei on 2016/11/11.
 */

public class StringConverter extends Converter.Factory  {

    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (String.class.equals(type)) {
            return new Converter<ResponseBody, String>() {
                @Override
                public String convert(ResponseBody value) throws IOException {
                    return value.string();
                }
            };
        }
        return null;
    }
}
