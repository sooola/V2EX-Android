package com.sola.v2ex_android.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sola.v2ex_android.R;

/**
 * Created by wei on 2016/10/14.
 *
 */
public abstract class SwipeRefreshToolbarBaseActivity extends BaseActivity {


    public SwipeRefreshLayout mSwipeRefreshLayout;
    public String mLastId = "0";
    public RecyclerView mRecyclerView;
    public Toolbar mToolbar;
    private Handler mHandle;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initToolBar();
    }

    public void initToolBar() {
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            ActionBar actionbar = getSupportActionBar();
//            actionbar.setHomeAsUpIndicator(R.mipmap.nav_icon_back);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(getToolBarTitle());
        }
    }

    protected abstract String getToolBarTitle();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
