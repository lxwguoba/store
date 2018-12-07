package zerone.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.activity.VipCustomerDetailsActivity;
import zerone.myapplication.domain.CategoryBean;
import zerone.myapplication.domain.ConsumersBean;
import zerone.myapplication.utils.TimeFormat;
import zerone.myapplication.view.XCRoundRectImageView;

/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class CasteringListAdapter extends RecyclerView.Adapter<CasteringListAdapter.ViewHolder> {
    private SalesclerAdapter.OnClickListener mOnClickListener = null;
    private SalesclerAdapter.OnLongClickListener mOnLongClickListener = null;
    private List<CategoryBean> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口

    public CasteringListAdapter(List<CategoryBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<CategoryBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_catering_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.c_id.setText("ID："+mData.get(position).getId());
        holder.c_name.setText("分类名称："+mData.get(position).getName());
        holder.c_px.setText("排序："+mData.get(position).getDisplayorder());
        holder.c_time.setText("添加时间："+ TimeFormat.time(mData.get(position).getCreated_at()));
        //设置名
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /****************************************
     * Listener
     */

    public void setOnClickListener(SalesclerAdapter.OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void setOnLongClickListener(SalesclerAdapter.OnLongClickListener listener) {
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

        public final TextView c_time;
        public final TextView c_name;
        public final TextView c_id;
        public final TextView c_px;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            c_time = root.findViewById(R.id.c_time);
            c_name = root.findViewById(R.id.c_name);
            c_id = root.findViewById(R.id.c_id);
            c_px = root.findViewById(R.id.c_px);
            this.root = root;
        }
    }
}
