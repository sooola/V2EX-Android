package com.sola.v2ex_android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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

    public static void glideWithCircleImg(final Context context , String url , final ImageView imageview  ){

        Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageview) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageview.setImageDrawable(circularBitmapDrawable);
            }
        });
    }


}
