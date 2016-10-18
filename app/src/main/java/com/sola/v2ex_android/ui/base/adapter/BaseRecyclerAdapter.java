package com.sola.v2ex_android.ui.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sola.v2ex_android.ui.base.callback.OnItemClickListener;
import com.sola.v2ex_android.util.ValidateUtil;

import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;

    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected OnItemClickListener mClickListener;

    private int mTypeCount = 1;

    public BaseRecyclerAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseRecyclerViewHolder holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(getItemLayoutId(viewType), parent, false));
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            });
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        bindData((holder), position, mData.get(position));
    }

    public void cleanData()
    {
        if(ValidateUtil.isNotEmpty(mData))
            mData.clear();
        notifyDataSetChanged();
    }

    public void appendItems(List<T> items) {
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

    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addMoreData(List<T> data) {
        int startPos = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(startPos, data.size());
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return bindViewType(position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public abstract int getItemLayoutId(int viewType);

    public abstract void bindData(BaseRecyclerViewHolder holder, int position, T item);

    protected int bindViewType(int position) {
        return 0;
    }

}
