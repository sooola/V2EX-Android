package com.sola.v2ex_android.util;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wei on 2016/12/1.
 */

public class MyBehavior extends CoordinatorLayout.Behavior<View>{

    float mOffsetTotal = 0;
    boolean mScrolling = false;
    private int mChildTotalHeight;

    public MyBehavior() {
    }

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    // 向上滑动dyConsumed为正，向下滑动dyConsumed为负
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        mOffsetTotal = ViewCompat.getTranslationY(child);

        float old = mOffsetTotal;
        float top = mOffsetTotal + dyConsumed / 2;

        if (mChildTotalHeight == 0) {
            mChildTotalHeight = child.getHeight() + ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).bottomMargin;
        }

        top = Math.min(top, mChildTotalHeight);
        top = Math.max(top, 0);

        if (top == old) {
            mScrolling = false;
            return;
        }

        mOffsetTotal = top;

        mScrolling = true;

        ViewCompat.setTranslationY(child, mOffsetTotal);
        ViewCompat.setAlpha(child, 1 - mOffsetTotal * 1.0f / mChildTotalHeight);

    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        if (mChildTotalHeight == 0) {
            mChildTotalHeight = child.getHeight() + ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).bottomMargin;
        }
        if (velocityY > 1 && mOffsetTotal != mChildTotalHeight) {
            // 向上快速滑动，隐藏按钮
            child.animate().translationY(mChildTotalHeight).alpha(0).setDuration((long) (500 * (1 - mOffsetTotal * 1.0f / mChildTotalHeight))).start();

            mOffsetTotal = mChildTotalHeight;
            return true;
        } else if (velocityY < -1 && mOffsetTotal != 0) {
            // 向下快速滑动，显示按钮
            child.animate().translationY(0).alpha(1).setDuration((long) (500 * (mOffsetTotal * 1.0f / mChildTotalHeight))).start();

            mOffsetTotal = 0;
            return true;
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

}
