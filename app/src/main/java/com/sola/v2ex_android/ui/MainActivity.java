package com.sola.v2ex_android.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.LoginResult;
import com.sola.v2ex_android.model.ProfileModel;
import com.sola.v2ex_android.network.LoginService;
import com.sola.v2ex_android.ui.base.BaseActivity;
import com.sola.v2ex_android.util.ApiErrorUtil;
import com.sola.v2ex_android.util.JsoupUtil;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Observer<LoginResult> observer = new Observer<LoginResult>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            ToastUtil.showShort(R.string.loading_failed);
        }

        @Override
        public void onNext(LoginResult items) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        MainContentFragment fragment = new MainContentFragment();
        fragmentTransaction.add(R.id.content_main, fragment);
        fragmentTransaction.commit();
        navigationView.getHeaderView(0).findViewById(R.id.iv_user_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData("a329377653", "329377653");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

    }

    private void loadData(final String username, final String password) {
        LoginService.getInstance().auth().loginWithUsername()
                .map(new Func1<String, HashMap>() {
                    @Override
                    public HashMap call(String stringResponse) {
                        LogUtil.d("Main22Activity","call stringResponse" + stringResponse);
                        return saveUserNameAndPwd(stringResponse, username, password);
                    }
                }).flatMap(new Func1<HashMap, Observable<String>>() {
            @Override
            public Observable<String> call(HashMap requestMap) {
                LogUtil.d("Main22Activity","call" + requestMap);
                return LoginService.getInstance().auth().postLogin(requestMap);
            }
        }).map(new Func1<String, LoginResult>() {
                    @Override
                    public LoginResult call(String response) {
                        Log.d("LoginPresenter","response" + response);
                        String errorMsg = ApiErrorUtil.getErrorMsg(response);
                        if (errorMsg == null) {
                            return JsoupUtil.parseLoginResult(response);
                        } else {
                            LoginResult result = new LoginResult();
                            result.setMessage(errorMsg);
                            return result;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("Main22Activity22","e" + e);
                    }

                    @Override
                    public void onNext(LoginResult result) {
                        LogUtil.d("Main22Activity22","result" + result);
                        getProfile();
                    }
                });
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

    private void getProfile() {
         LogUtil.d("Main22Activity22","getProfile()");
        LoginService.getInstance().auth().getProfileInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                LogUtil.d("Main22Activity22","stringResponse.body()" + stringResponse.body());
                ProfileModel profile = new ProfileModel();
                try {
                    profile.parse(stringResponse.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                 LogUtil.d("Main22Activity22","profile" + profile);
            }
        });


    }

    public HashMap saveUserNameAndPwd(String stringResponse, String username, String password) {
        Element body = Jsoup.parse(stringResponse);
        Elements boxes = body.getElementsByClass("box");
        HashMap params = new HashMap();
        for (Element el : boxes) {
            Elements cell = el.getElementsByClass("cell");
            for (Element c : cell) {
                String nameVal = c.getElementsByAttributeValue("type", "text").attr("name");
                String passwordVal = c.getElementsByAttributeValue("type", "password").attr("name");
                String once = c.getElementsByAttributeValue("name", "once").attr("value");
                if (nameVal.isEmpty() || passwordVal.isEmpty()) continue;
                params.put(nameVal, username);
                params.put("once", once);
                params.put(passwordVal, password);
                break;
            }
        }
        params.put("next", "/");
        return params;
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
