package com.zerone.intodata.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zerone.intodata.R;
import com.zerone.intodata.domain.IntoListBean;
import com.zerone.intodata.utils.TimeFormat;

import java.util.List;

/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class IntoListAdapter extends RecyclerView.Adapter<IntoListAdapter.ViewHolder> {

    private List<IntoListBean> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口
    private Handler handler;

    public IntoListAdapter(List<IntoListBean> data, Context mContext, Handler handler) {
        this.mData = data;
        this.mContext = mContext;
        this.handler = handler;
    }

    public void updateData(List<IntoListBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置名
        if (mData.get(position).getIs_apply() == 0) {
            holder.layout04.setVisibility(View.GONE);
            holder.store_edit.setEnabled(true);
            holder.store_edit.setBackgroundResource(R.drawable.btn_selector);
            holder.store_edit.setText("提交");
            holder.store_edit.setTextColor(Color.parseColor("#ffffff"));
        } else if (mData.get(position).getIs_apply() == 1) {
            holder.layout04.setVisibility(View.GONE);
            holder.store_edit.setText("审核中");
            holder.store_edit.setBackgroundResource(R.drawable.btn_normal_bg_sh);
            holder.store_edit.setEnabled(false);
            holder.store_edit.setTextColor(Color.parseColor("#464646"));
        } else if (mData.get(position).getIs_apply() == 2) {
            holder.store_edit.setEnabled(false);
            holder.layout04.setVisibility(View.GONE);
            holder.store_edit.setText("已审核");
            holder.store_edit.setBackgroundResource(R.drawable.btn_normal_bg_wc);
            holder.store_edit.setTextColor(Color.parseColor("#ffffff"));
        } else if (mData.get(position).getIs_apply() == 3) {
            holder.store_edit.setEnabled(true);
            holder.store_edit.setBackgroundResource(R.drawable.btn_selector);
            holder.store_edit.setText("编辑");
            holder.store_edit.setTextColor(Color.parseColor("#ffffff"));
            holder.layout04.setVisibility(View.VISIBLE);
        }
        holder.store_bh.setText(mData.get(position).getRemark());
        holder.store_address.setText(mData.get(position).getStroe_address());
        holder.store_create_time.setText(TimeFormat.time(mData.get(position).getStroe_create_tme()));
        holder.store_name.setText(mData.get(position).getStroe_name());
        holder.store_lmzk.setText("联盟折扣：" + mData.get(position).getStroe_lmzk() + "%");

        holder.store_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message meess = new Message();
                meess.what = 1;
                meess.obj = position;
                handler.sendMessage(meess);

            }
        });


        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.root, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView store_name;
        public final TextView store_lmzk;
        public final TextView store_address;
        public final TextView store_fz_name;
        public final TextView store_fz_pwd;
        public final TextView store_fz_number;
        public final TextView store_create_time;
        public final TextView store_bh;
        public final LinearLayout layout;
        public final LinearLayout layout01;
        public final LinearLayout layout02;
        public final LinearLayout layout03;
        public final LinearLayout layout04;
        public final Button store_edit;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            store_name = root.findViewById(R.id.store_name);
            store_lmzk = root.findViewById(R.id.store_lmzk);
            store_address = root.findViewById(R.id.store_address);
            store_fz_name = root.findViewById(R.id.store_fz_name);
            store_fz_pwd = root.findViewById(R.id.store_fz_pwd);
            store_fz_number = root.findViewById(R.id.store_fz_number);
            store_create_time = root.findViewById(R.id.store_create_time);
            store_bh = root.findViewById(R.id.store_bh);
            layout = root.findViewById(R.id.layout);
            layout01 = root.findViewById(R.id.layout01);
            layout02 = root.findViewById(R.id.layout02);
            layout03 = root.findViewById(R.id.layout03);
            layout04 = root.findViewById(R.id.layout04);
            store_edit = root.findViewById(R.id.store_edit);
            this.root = root;
        }
    }
}
