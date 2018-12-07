package zerone.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.domain.PayInfoBean;
import zerone.myapplication.utils.TimeFormat;


/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 * 流水记录的适配器
 */

public class PackTransactionsListItemAdapter extends RecyclerView.Adapter<PackTransactionsListItemAdapter.ViewHolder> {
    private List<PayInfoBean> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口

    public PackTransactionsListItemAdapter(List<PayInfoBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<PayInfoBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moneypack_trans_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置名
        holder.nickname.setText(mData.get(position).getNickname());
        holder.rzmoney.setText(mData.get(position).getPayment_price() + "￥");
        holder.id.setText((position + 1) + "");
        holder.time.setText(TimeFormat.time(mData.get(position).getCreated_at()));
        holder.ordremoney.setText(mData.get(position).getOrder_price() + "￥");
        if ("1".equals(mData.get(position).getStatus())) {
            holder.status.setText("入账");
        }

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

    //下面两个方法提供给页面刷新和加载时调用
    public void refresh(List<PayInfoBean> addList) {
        //增加数据
        int position = mData.size();
        mData.addAll(position, addList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView time;
        public final TextView id;
        public final TextView nickname;
        public final TextView rzmoney;
        public final TextView status;
        public final TextView ordremoney;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            time = root.findViewById(R.id.time);
            id = root.findViewById(R.id.id);
            nickname = root.findViewById(R.id.nickname);
            status = root.findViewById(R.id.status);
            rzmoney = root.findViewById(R.id.rzmoney);
            ordremoney = root.findViewById(R.id.ordremoney);
            this.root = root;
        }
    }
}
