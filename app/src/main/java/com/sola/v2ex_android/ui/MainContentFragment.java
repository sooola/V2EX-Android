package com.sola.v2ex_android.ui;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.ui.adapter.MainPageAdapter;
import com.sola.v2ex_android.ui.base.BaseFragment;

import butterknife.Bind;

public class MainContentFragment extends BaseFragment {

    @Bind(R.id.vp_main)
    ViewPager mVpMain;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.appbarlayout)
    AppBarLayout mAppbarlayout;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_content;
    }

    @Override
    protected void initViews() {
        MainPageAdapter adapter = new MainPageAdapter(getFragmentManager());
        mVpMain.setAdapter(adapter);
        mTabs.setupWithViewPager(mVpMain);
    }


}
