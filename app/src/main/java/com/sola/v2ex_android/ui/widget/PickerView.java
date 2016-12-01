package com.sola.v2ex_android.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sola.v2ex_android.R;
import com.sola.v2ex_android.util.LogUtil;
import com.sola.v2ex_android.util.ValidateUtil;

import java.util.List;

/**
 * Created by wei on 2016/12/1.
 *
 */

public class PickerView extends RecyclerView {



    private PickerAdapter mAdapter;
    private int mHight;

    public PickerView(Context context) {
        super(context);
        initView();
    }

    public PickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PickerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        this.setLayoutManager(layoutManager);
        mAdapter = new PickerAdapter(this.getContext());
        this.setAdapter(mAdapter);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        mHight = getHeight();
        LogUtil.d("PickerView","mHight" + mHight);
    }


    public void setData(List<String> data){
        mAdapter.appendItems(data);
    }


   class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.ItemViewHolder>{

       protected List<String> mData;
       public Context mContext;

       public  PickerAdapter(Context context){
           mContext = context;
       }

       /**
        * 显示的数据个数
        */
       public int ITEM_NUM = 3;

       @Override
       public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(mContext).inflate(R.layout.item_pick_view, parent, false);
           //设置宽度
           ViewGroup.LayoutParams params = view.getLayoutParams();
           params.width =(int)getItemWidth();
           LogUtil.d("PickerView","getItemWidth()" + getItemWidth());
           return new ItemViewHolder(view);
       }

       @Override
       public void onBindViewHolder(ItemViewHolder holder, int position) {
           holder.mTextView.setText(mData.get(position));
       }

       @Override
       public int getItemCount() {
           return mData.size();
       }

       public float getItemWidth() {
           DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
           return displayMetrics.widthPixels / ITEM_NUM;
       }


       public void appendItems(List<String> items) {
           if(ValidateUtil.isNotEmpty(items))
           {
               if (ValidateUtil.isNotEmpty(mData))
               {
                   int count = getItemCount();
                   mData.addAll(items);
                   notifyItemRangeChanged(count ,items.size());
               }
               else
               {
                   mData = items;
                   notifyDataSetChanged();
               }
           }
       }

       public class ItemViewHolder extends RecyclerView.ViewHolder {
           private TextView mTextView;

           public ItemViewHolder(View itemView) {
               super(itemView);
               mTextView = (TextView) itemView.findViewById(R.id.tv_title);
               mTextView.setTag(this);
           }

           public TextView getmTextView() {
               return mTextView;
           }
       }
   }



}
