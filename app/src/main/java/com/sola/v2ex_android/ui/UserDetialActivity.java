package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.ui.base.BaseActivity;
import com.sola.v2ex_android.util.LogUtil;

import butterknife.Bind;

/**
 * 用户详情
 * Created by wei on 2016/10/21.
 */

public class UserDetialActivity extends BaseActivity {

    public static final String KEY_USERNAME = "key_username";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.appbarlayout)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.coll_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    public static Intent getIntent(Context context , String userName){
        Intent intent = new Intent(context,UserDetialActivity.class);
        intent.putExtra(KEY_USERNAME , userName);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_detial;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                 LogUtil.d("UserDetialActivity","verticalOffset" + verticalOffset);
                if (- verticalOffset >= 280 ) {
                    mCollapsingToolbarLayout.setTitle("aaaaaxxxss");
                } else {
                    mCollapsingToolbarLayout.setTitle("");
                }
            }
        });

    }
}
