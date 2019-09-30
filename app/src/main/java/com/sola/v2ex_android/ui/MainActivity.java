package com.sola.v2ex_android.ui;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.sola.v2ex_android.R;
import com.sola.v2ex_android.event.UserLoginEvent;
import com.sola.v2ex_android.event.UserLogoutEvent;
import com.sola.v2ex_android.model.V2exUser;
import com.sola.v2ex_android.ui.base.BaseActivity;
import com.sola.v2ex_android.util.GlideUtil;
import com.sola.v2ex_android.util.RxBus;
import com.sola.v2ex_android.util.ToastUtil;

import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView mUserIcon;
    private TextView mUserName;
    private View mLogoutBtn;
    @OnClick(R.id.fab) void submit(View view) {
        view.getContext().startActivity(SendTopicActivity.getIntent(view.getContext()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        MainContentFragment fragment = new MainContentFragment();
        fragmentTransaction.add(R.id.content_main, fragment);
        fragmentTransaction.commit();

        View headView =  navigationView.getHeaderView(0);
        mLogoutBtn = headView.findViewById(R.id.bt_logout);
        mUserIcon = (ImageView) headView.findViewById(R.id.iv_user_icon);
        mUserName = (TextView) headView.findViewById(R.id.tv_username);

        if (null != V2exUser.getCurrentUser()){
            V2exUser currentUser = V2exUser.getCurrentUser();
            GlideUtil.glideWithCircleImg(MainActivity.this , currentUser.userAvatar , mUserIcon);
            mUserName.setText(currentUser.userId);
            mLogoutBtn.setVisibility(View.VISIBLE);
        }
        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (V2exUser.getCurrentUser() == null){
                    v.getContext().startActivity(LoginActivity.getIntent(v.getContext()));
                }
            }
        });



        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort("退出登录成功");
                V2exUser.logout();
                RxBus.getInstance().post(new UserLogoutEvent());
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        rxBusObservers();
    }

    private void rxBusObservers() {
        Subscription subscription = RxBus.getInstance()
                .toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event instanceof UserLoginEvent) {
                            UserLoginEvent userLoginEvent = (UserLoginEvent)event;
                            GlideUtil.glideWithCircleImg(MainActivity.this , userLoginEvent.userAvatar , mUserIcon);
                            mUserName.setText(userLoginEvent.userName);
                            mLogoutBtn.setVisibility(View.VISIBLE);
                        }else if (event instanceof  UserLogoutEvent){
                            mUserIcon.setImageResource(R.drawable.ic_account_circle_black_48dp);
                            mUserName.setText("请登录");
                            mLogoutBtn.setVisibility(View.GONE);
                        }
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_node_collecct) {
            startActivity(MyNodeActivity.getIntent(this));
        } else if (id == R.id.nav_my_following) {
            startActivity(MyFollowingActivity.getIntent(this));
        } else if (id == R.id.nav_about) {
            ToastUtil.showShort(getString(R.string.github_address));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
