package com.sola.v2ex_android.ui.base.view;

import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.sola.v2ex_android.ui.base.application.MyApplication;
import com.sola.v2ex_android.util.DisplayUtil;

public class BaseSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public BaseSpacesItemDecoration() {
        this.mSpace = DisplayUtil.dip2px(MyApplication.getContextObject(),
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 8 : 4);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.top = 0;
        outRect.bottom = mSpace;
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)) {
            outRect.bottom = DisplayUtil.dip2px(view.getContext(), 2);
        }

        if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
            if (position < ((StaggeredGridLayoutManager) parent.getLayoutManager())
                    .getSpanCount()) {
                // 利用item的margin配合RecyclerView的margin值使得间隔相等，这里只需设第一行item的相对顶部的高度
                outRect.top = mSpace;
            }
        } else if (parent.getLayoutManager() instanceof GridLayoutManager) {
            if (position < ((GridLayoutManager) parent.getLayoutManager()).getSpanCount()) {
                // 保证第一行有相对顶部有高度
                outRect.top = mSpace;
            }
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (position == 0) {
                // 保证第一行有相对顶部有高度
                outRect.top = mSpace;
            }
        }
    }
}