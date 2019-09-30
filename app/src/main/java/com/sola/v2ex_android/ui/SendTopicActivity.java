package com.sola.v2ex_android.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.model.Pickers;
import com.sola.v2ex_android.ui.base.BaseActivity;
import com.sola.v2ex_android.ui.widget.PickerScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wei on 2016/12/1.
 */

public class SendTopicActivity extends BaseActivity {
    
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,SendTopicActivity.class);
        return intent;
    }

    @BindView(R.id.et_title)
     EditText etTitle;

    @BindView(R.id.et_content)
     EditText etContent;

    @BindView(R.id.pickview)
    PickerScrollView mPickView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_topic;
    }

    @Override
    protected void initViews() {
        List<Pickers> ss = new ArrayList();
        ss.add(new Pickers("11111","11111"));
        ss.add(new Pickers("22222","22222"));
        ss.add(new Pickers("33333","33333"));
        ss.add(new Pickers("44444","44444"));
        ss.add(new Pickers("55555","55555"));
        ss.add(new Pickers("66666","66666"));
        mPickView.setData(ss);
        mPickView.setSelected(0);
    }


}
