package zerone.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.domain.SalesclerBean;
import zerone.myapplication.utils.TimeFormat;

/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class SalesclerAdapter extends RecyclerView.Adapter<SalesclerAdapter.ViewHolder> {

    private List<SalesclerBean> mData;
    private Context mContext;
    private OnClickListener mOnClickListener = null;
    private OnLongClickListener mOnLongClickListener = null;

    public SalesclerAdapter(List<SalesclerBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<SalesclerBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_salescler_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置名
        holder.id.setText(mData.get(position).getId() + "");
        holder.account.setText("账号：" + mData.get(position).getUsername());
        holder.name.setText("店员：" + mData.get(position).getRealname());
        holder.phone.setText(mData.get(position).getMobile());
        holder.time.setText(TimeFormat.time(mData.get(position).getCreated_at()));
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
        public final TextView id;
        public final TextView phone;
        public final TextView time;
        public final TextView name;
        public final TextView account;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            id = root.findViewById(R.id.id);
            phone = root.findViewById(R.id.phone);
            time = root.findViewById(R.id.time);
            name = root.findViewById(R.id.name);
            account = root.findViewById(R.id.account);
            this.root = root;
        }
    }
}
