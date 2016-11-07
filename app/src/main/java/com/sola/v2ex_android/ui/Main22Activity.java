package com.sola.v2ex_android.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.sola.v2ex_android.R;
import com.sola.v2ex_android.network.NetWork;
import com.sola.v2ex_android.util.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Main22Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        MainActivity fragment = new MainActivity();
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

    private void loadData(final String username, final String password) {
        NetWork.getLoginApi().loginWithUsername()
                .map(new Func1<Response<String>, RequestBody>() {
                    @Override
                    public RequestBody call(Response<String> stringResponse) {
                        return saveUserNameAndPwd(stringResponse, username, password);
                    }
                }).flatMap(new Func1<RequestBody, Observable<Response<String>>>() {
            @Override
            public Observable<Response<String>> call(RequestBody requestBody) {
                return NetWork.getPostLoginApi().postLogin(requestBody);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Response<String>>() {
                    @Override
                    public void call(Response<String> stringResponse) {
                         LogUtil.d("Main22Activity","stringResponse.code()" + stringResponse.code());
                        LogUtil.d("Main22Activity", "stringResponse.isSuccessful()" + stringResponse.isSuccessful());
                        getProfile();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.d("Main22Activity", "throwable" + throwable);
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
        NetWork.getLoginApi().getProfileInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                LogUtil.d("Main22Activity","code()" + stringResponse.code());
                 LogUtil.d("Main22Activity","body" + stringResponse.body());
            }
        });


    }

    public RequestBody saveUserNameAndPwd(Response<String> stringResponse, String username, String password) {
        String content = new String(stringResponse.body());
        Element body = Jsoup.parse(content);
        Elements boxes = body.getElementsByClass("box");
        Map params = new HashMap();
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
        Gson gson = new Gson();

        String strEntity = gson.toJson(params);

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        return requestBody;
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
