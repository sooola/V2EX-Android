package com.sola.v2ex_android.ui.base.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.util.TDevice;


public class EmptyView extends RelativeLayout{

    private View mBindView;
    private View mRootView;

    private final Context context;
    public ImageView img;
    private OnClickListener listener;
    private boolean clickEnable = true;
    private TextView tv;
    private int defEmptyImg = R.mipmap.pagefailed_bg;
    private String defEmptyText = "暂无数据";
    private ProgressWheel animProgress;

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EmptyView, 0, 0);
//        String text = ta.getString(R.styleable.EmptyView_android_text);
//        String buttonText = ta.getString(R.styleable.EmptyView_buttonText);
//        mLoadingText = ta.getString(R.styleable.EmptyView_loadingText);
//        ta.recycle();
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.view_error_layout, null);
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tv = (TextView) view.findViewById(R.id.tv_error_layout);
        mRootView = view.findViewById(R.id.rl_root);
        animProgress = (ProgressWheel) view.findViewById(R.id.animProgress);

        setBackgroundColor(-1);
        img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });
        addView(view);
    }

    public void setRootViewBackgroundColor(String colorString){
        mRootView.setBackgroundColor(Color.parseColor(colorString));
    }


    public void bindView(View view) {
        mBindView = view;
    }

    public void loading() {
        if (mBindView != null) {
            mBindView.setVisibility(View.INVISIBLE);
        }
        setVisibility(View.VISIBLE);
        animProgress.setVisibility(View.VISIBLE);
        img.setVisibility(View.GONE);
        tv.setText("");
        clickEnable = false;
    }

    public void success() {
        setVisibility(View.GONE);
        if (mBindView != null) {
            mBindView.setVisibility(View.VISIBLE);
        }
        clickEnable = false;
    }

    public void empty() {
        if (mBindView != null) {
            mBindView.setVisibility(View.INVISIBLE);
        }
        setVisibility(View.VISIBLE);
        if (TDevice.hasInternet()) {
            //如果有网络
            tv.setText(defEmptyText);
            img.setBackgroundResource(defEmptyImg);
        } else {
            tv.setText(R.string.error_view_network_error_click_to_refresh);
            img.setBackgroundResource(R.mipmap.page_icon_network);
        }
        img.setVisibility(View.VISIBLE);
        animProgress.setVisibility(View.GONE);
        clickEnable = true;
    }


    /**
     * 设置无数据的图片
     */
    public void setDefEmptyContent(int resId , String text){
        defEmptyImg = resId;
        defEmptyText = text;
    }

    public void setDefEmptyContent(String text){
        defEmptyText = text;
    }

    /**
     * 点击图片的事件
     * @param listener
     */
    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
