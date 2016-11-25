package com.sola.v2ex_android.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.event.UserLoginEvent;
import com.sola.v2ex_android.model.LoginResult;
import com.sola.v2ex_android.model.V2exUser;
import com.sola.v2ex_android.network.LoginService;
import com.sola.v2ex_android.ui.base.BaseSwipeRefreshActivity;
import com.sola.v2ex_android.util.ApiErrorUtil;
import com.sola.v2ex_android.util.JsoupUtil;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.RxBus;
import com.sola.v2ex_android.util.ToastUtil;
import com.sola.v2ex_android.util.ValidateUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.sola.v2ex_android.R.id.account;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseSwipeRefreshActivity {

    // UI references.
    @Bind(account)
    AutoCompleteTextView mAccountView;

    @Bind(R.id.password)
    EditText mPasswordView;

    @Bind(R.id.login_progress)
    View mProgressView;

    @Bind(R.id.login_form)
    View mLoginFormView;

    @OnClick(R.id.sign_in_button)
    public void login() {
        attemptLogin();
    }


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    Observer<LoginResult> observer = new Observer<LoginResult>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            showProgress(false);
            ToastUtil.showShort(R.string.loading_failed);
        }

        @Override
        public void onNext(LoginResult items) {
            showProgress(false);
            if (ValidateUtil.isNotEmpty(items) && ValidateUtil.isNotEmpty(items.userId)) {
                ToastUtil.showShort("登录成功");
                V2exUser.saveCurrentUser(items);
                if (RxBus.getInstance().hasObservers()) {
                    RxBus.getInstance().post(new UserLoginEvent(items.userId, items.userAvatar));
                }
                finish();
            }else {
                ToastUtil.showShort("账号或密码错误");
            }
            LogUtil.d("LoginActivity", "items" + items);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {

    }

    private void login(final String username, final String password) {
        Subscription subscription = LoginService.getInstance().auth().login()
                .map(new Func1<String, HashMap>() {
                    @Override
                    public HashMap call(String stringResponse) {
                        return JsoupUtil.parseUserNameAndPwd(stringResponse, username, password);
                    }
                }).flatMap(new Func1<HashMap, Observable<String>>() {
                    @Override
                    public Observable<String> call(HashMap requestMap) {
                        return LoginService.getInstance().auth().postLogin(requestMap);
                    }
                }).map(new Func1<String, LoginResult>() {
                    @Override
                    public LoginResult call(String response) {
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
                .subscribe(observer);
        addSubscription(subscription);
    }




    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_account_required));
            focusView = mAccountView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            login(account, password);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

}

