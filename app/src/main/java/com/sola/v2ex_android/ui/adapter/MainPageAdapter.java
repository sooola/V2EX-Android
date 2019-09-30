package com.sola.v2ex_android.ui.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sola.v2ex_android.ui.NewestTopicsFragment;
import com.sola.v2ex_android.ui.NodeFragment;

/**
 * Created by wei on 2016/10/18.
 */

public class MainPageAdapter extends FragmentPagerAdapter {

    private final String[] titles = { "主题", "节点"};

    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (0 == position){
            return NewestTopicsFragment.newInstance();
        }else {
            return NodeFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
