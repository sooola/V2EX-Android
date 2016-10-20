package com.sola.v2ex_android.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Context mContext;
    public static void init(Context context)
    {
        mContext = context;
    }

    public static void showShort(CharSequence message) {
        if (ValidateUtil.isEmpty(mContext) || ValidateUtil.isEmpty(message)) return;
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(int messageRes) {
        if (ValidateUtil.isEmpty(mContext) || ValidateUtil.isEmpty(messageRes)) return;
        Toast.makeText(mContext, messageRes, Toast.LENGTH_SHORT).show();
    }

}
