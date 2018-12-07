package zerone.myapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.domain.CashFlowBean;

/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<CashFlowBean> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口

    public MyAdapter( List<CashFlowBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<CashFlowBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cashflow_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置名
        holder.orderNumber.setText(mData.get(position).getOrderNumber());

        holder.userName.setText("消费者："+mData.get(position).getCustomerName());
        holder.time.setText("创建时间："+mData.get(position).getOrderCreateTime());
        holder. money.setText("订单金额："+mData.get(position).getOrederMoney()+"￥");
        holder.price.setText("￥"+mData.get(position).getOrderPrice());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                if (mOnItemClickListener!=null){
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
        public final TextView orderNumber;
        public final TextView userName;
        public final TextView time;
        public final TextView money;
        public final TextView price;
        public final TextView status;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            orderNumber = root.findViewById(R.id.orderNumber);
            userName = root.findViewById(R.id.userName);
            time = root.findViewById(R.id.time);
            money = root.findViewById(R.id.money);
            price = root.findViewById(R.id.price);
            status = root.findViewById(R.id.status);
            this.root = root;
        }
    }
}
