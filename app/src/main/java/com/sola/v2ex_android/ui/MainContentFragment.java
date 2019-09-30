package com.sola.v2ex_android.ui;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sola.v2ex_android.R;
import com.sola.v2ex_android.ui.adapter.MainPageAdapter;
import com.sola.v2ex_android.ui.base.BaseFragment;

import butterknife.BindView;

public class MainContentFragment extends BaseFragment {

    @BindView(R.id.vp_main)
    ViewPager mVpMain;
    @BindView(R.id.tabs)
    TabLayout mTabs;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_content;
    }

    @Override
    protected void initViews(View view) {
        MainPageAdapter adapter = new MainPageAdapter(getFragmentManager());
        mVpMain.setAdapter(adapter);
        mTabs.setupWithViewPager(mVpMain);
    }

    @Override
    protected void loadData() {

    }
}
