package zerone.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.activity.VipCustomerDetailsActivity;
import zerone.myapplication.domain.ConsumersBean;
import zerone.myapplication.domain.CouponsBean;
import zerone.myapplication.utils.TimeFormat;
import zerone.myapplication.view.XCRoundRectImageView;

/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {

    private List<CouponsBean> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口
    private Handler handler;

    public CouponsAdapter(List<CouponsBean> data, Context mContext, Handler handler) {
        this.mData = data;
        this.mContext = mContext;
        this.handler = handler;
    }

    public void updateData(List<CouponsBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_coupons_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.coupons_count.setText(mData.get(position).getNum() + "");
        holder.coupons_discount.setText(mData.get(position).getRatio() + "");
        holder.coupons_id.setText("" + (position + 1));
        holder.coupons_money.setText(mData.get(position).getFull_price());
        holder.coupons_name.setText(mData.get(position).getName());
        holder.coupons_type.setText("折扣卷");
        holder.coupons_dis.setText("");
        holder.time.setText(TimeFormat.time(mData.get(position).getEnd_time() + ""));

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message =new Message();
                message.what=-2;
                message.obj=position;
                handler.sendMessage(message);
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
        public final TextView coupons_id;
        public final TextView coupons_name;
        public final TextView coupons_type;
        public final TextView coupons_discount;
        public final TextView coupons_money;
        public final TextView coupons_mz;
        public final TextView coupons_count;
        public final TextView time;
        public final TextView coupons_dis;
        public final Button edit_btn;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            coupons_id = root.findViewById(R.id.coupons_id);
            coupons_name = root.findViewById(R.id.coupons_name);
            coupons_type = root.findViewById(R.id.coupons_type);
            coupons_discount = root.findViewById(R.id.coupons_discount);
            coupons_money = root.findViewById(R.id.coupons_money);
            coupons_mz = root.findViewById(R.id.coupons_mz);
            coupons_count = root.findViewById(R.id.coupons_count);
            time = root.findViewById(R.id.time);
            coupons_dis = root.findViewById(R.id.coupons_dis);
            edit_btn = root.findViewById(R.id.edit_btn);
            this.root = root;
        }
    }
}
