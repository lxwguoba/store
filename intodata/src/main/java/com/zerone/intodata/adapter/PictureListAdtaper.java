package com.zerone.intodata.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zerone.intodata.R;
import com.zerone.intodata.domain.PirtureListBean;

import java.util.List;


/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class PictureListAdtaper extends RecyclerView.Adapter<PictureListAdtaper.ViewHolder> {

    private List<PirtureListBean> mData;
    private Context mContext;
    private OnClickListener mOnClickListener = null;
    private OnLongClickListener mOnLongClickListener = null;

    /****************************************
     * Listener
     */
    private int itemHeight;

    public PictureListAdtaper(List<PirtureListBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;

    }

    public void updateData(List<PirtureListBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picturelist_item, parent, false);
        parent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                itemHeight = v.getMeasuredHeight();
                return true;
            }
        });
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    /**
     * 获取item的高度
     *
     * @return
     */
    public int getItemHeight() {
        return itemHeight;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.p_name.setText(mData.get(position).getPname());
        Glide.with(mContext).load(mData.get(position).getPurl()).error(R.mipmap.login_head).into(holder.p_img);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnClickListener) {
                    mOnClickListener.onClick(v, position);
                }
            }
        });

        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mOnLongClickListener) {
                    mOnLongClickListener.onLongClick(v, position);
                }
                // 消耗事件，否则长按逻辑执行完成后还会进入点击事件的逻辑处理
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    /****************************************
     * Listener
     */

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }


    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 手动添加点击事件
     */
    public interface OnClickListener {
        void onClick(View view, int position);
    }

    /**
     * 手动添加长按事件
     */
    public interface OnLongClickListener {
        void onLongClick(View view, int position);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView p_img;
        public final TextView p_name;
        public final View root;

        public ViewHolder(final View root) {
            super(root);
            p_img = root.findViewById(R.id.images);
            p_name = root.findViewById(R.id.text);
            this.root = root;
        }
    }

}
