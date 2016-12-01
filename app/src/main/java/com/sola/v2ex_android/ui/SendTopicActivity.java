package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.ui.base.BaseActivity;
import com.sola.v2ex_android.ui.widget.PickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wei on 2016/12/1.
 */

public class SendTopicActivity extends BaseActivity {
    
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,SendTopicActivity.class);
        return intent;
    }

    @Bind(R.id.et_title)
     EditText etTitle;

    @Bind(R.id.et_content)
     EditText etContent;

    @Bind(R.id.pickview)
    PickerView mPickView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_topic;
    }

    @Override
    protected void initViews() {
        List<String> ss = new ArrayList();
        ss.add("1111111");
        ss.add("2222222");
        ss.add("3333333");
        ss.add("4444444");
        ss.add("5555555");
        mPickView.setData(ss);
    }


}
