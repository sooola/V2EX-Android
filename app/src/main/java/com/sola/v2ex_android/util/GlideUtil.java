package com.sola.v2ex_android.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sola.v2ex_android.R;

/**
 *
 * Created by wei on 2016/10/20.
 */

public class GlideUtil {

    public static void glideWithImg(Context context , String url , int placeholder , ImageView imageview ){
        Glide
                .with(context)
                .load(url)
                .placeholder(placeholder)
                .crossFade()
                .centerCrop()
                .into(imageview);
    }

    public static void glideWithImg(Context context , String url , ImageView imageview  ){
        Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.loading_color)
                .crossFade()
                .centerCrop()
                .into(imageview);
    }


}
