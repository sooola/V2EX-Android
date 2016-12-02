package com.sola.v2ex_android.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
//                    mAdapter.highlightItem();
                    //将位置移动到中间位置
                    ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(getScrollPosition(),0);
                     LogUtil.d("PickerView","getScrollPosition()" + getScrollPosition());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                tvage.setText(String.valueOf(getMiddlePosition() + START_NUM));
            }
        });
    }


    /**
     * 获取滑动值, 滑动偏移 / 每个格子宽度
     *
     * @return 当前值
     */
    private int getScrollPosition() {
        return (int) (((double) this.computeVerticalScrollOffset()
                / (double) mAdapter.getItemWidth())+0.5f);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        mHight = getMeasuredHeight();
        mAdapter.setViewHeight(mHight);
        LogUtil.d("PickerView","mHight" + mHight);
    }


    public void setData(List<String> data){
        mAdapter.appendItems(data);
    }


   class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.ItemViewHolder>{

       protected List<String> mData;
       public Context mContext;
       private int mViewHeight;
       private int mHighlight = 0; // 高亮

       public  PickerAdapter(Context context){
           mContext = context;
       }

       /**
        * 显示的数据个数
        */
       public int ITEM_NUM = 3;

       public void setViewHeight(int hight){
           mViewHeight = hight;
       }

       @Override
       public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(mContext).inflate(R.layout.item_pick_view, parent, false);
           //设置宽度
           ViewGroup.LayoutParams params = view.getLayoutParams();
           params.height = (int)getItemWidth();
           LogUtil.d("PickerView","getItemWidth()" + getItemWidth());
           return new ItemViewHolder(view);
       }

       @Override
       public void onBindViewHolder(ItemViewHolder holder, int position) {
           holder.mTextView.setText(mData.get(position));
           // 高亮显示
           if (isSelected(position)) {
               holder.getTextView().setTextSize(30);
               holder.getTextView().setTextColor(Color.parseColor("#000000"));
               holder.mTextView.setBackgroundResource(R.drawable.banch_background_top_bottom_border);
           } else {
               holder.getTextView().setTextSize(20);
               holder.getTextView().setTextColor(Color.parseColor("#666666"));
               holder.mTextView.setBackground(null);
           }
       }

       @Override
       public int getItemCount() {
           return mData.size();
       }

       public float getItemWidth() {
           return mViewHeight / ITEM_NUM;
       }

       // 判断是否是高亮
       public boolean isSelected(int position) {
           return mHighlight == position;
       }

       // 高亮中心, 更新前后位置
       public void highlightItem(int position) {
           mHighlight = position;
//           int offset = ITEM_NUM / 2;
//           for (int i = position - offset; i <= position + offset; ++i)
//               notifyItemChanged(i);
           notifyDataSetChanged();
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

           public TextView getTextView() {
               return mTextView;
           }
       }
   }



}
