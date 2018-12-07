package zerone.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.domain.PostersBean;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.utils.TimeFormat;
import zerone.myapplication.view.ZQImageViewRoundOval;


/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class PostersListAdapter extends RecyclerView.Adapter<PostersListAdapter.ViewHolder> {

    private List<PostersBean> mData;
    private Context mContext;
    /****************************************
     * Listener
     */
    private SalesclerAdapter.OnClickListener mOnClickListener = null;
    private SalesclerAdapter.OnLongClickListener mOnLongClickListener = null;

    public PostersListAdapter(List<PostersBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<PostersBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_posters_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.posters_id.setText("ID：" + (position + 1));
        holder.posters_time.setText(TimeFormat.time(mData.get(position).getCreated_at()));
        holder.posters_number.setText(mData.get(position).getOrdersn());
        holder.posters_name.setText(mData.get(position).getName());
        holder.posters_pay_money.setText(mData.get(position).getOrder_price() + "￥");
        holder.posters_product_money.setText(mData.get(position).getProduct_price() + "￥");
        holder.posters_look.setText(mData.get(position).getShop_num() + "");
        holder.posters_tostroe_count.setText(mData.get(position).getAd_num() + "");
        holder.posters_look_today.setText(mData.get(position).getDay_num() + "");
        holder.posters_tostroe_count_today.setText(mData.get(position).getDay_person() + "");
        holder.posters_order_type.setText(mData.get(position).getStatus() + "");
        if (mData.get(position).getType() == 1) {
            holder.posters_address_img.setVisibility(View.VISIBLE);
            holder.posters_address_text.setVisibility(View.GONE);
            Glide.with(mContext).load(mData.get(position).getShare_code()).into(holder.posters_address_img);
        } else if (mData.get(position).getType() == 0) {
            holder.posters_address_text.setVisibility(View.VISIBLE);
            holder.posters_address_img.setVisibility(View.GONE);
            holder.posters_address_text.setText(mData.get(position).getShare_code());

        }


    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setOnLongClickListener(SalesclerAdapter.OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    public void setOnClickListener(SalesclerAdapter.OnClickListener listener) {
        mOnClickListener = listener;
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
        public final ImageView posters_address_img;
        public final TextView posters_id;
        public final TextView posters_time;
        public final TextView posters_number;
        public final TextView posters_name;
        public final TextView posters_pay_money;
        public final TextView posters_product_money;
        public final TextView posters_look;
        public final TextView posters_tostroe_count;
        public final TextView posters_look_today;
        public final TextView posters_tostroe_count_today;
        public final TextView posters_order_type;
        public final TextView posters_address_text;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            posters_address_img = root.findViewById(R.id.posters_address_img);
            posters_id = root.findViewById(R.id.posters_id);
            posters_time = root.findViewById(R.id.posters_time);
            posters_number = root.findViewById(R.id.posters_number);
            posters_name = root.findViewById(R.id.posters_name);
            posters_pay_money = root.findViewById(R.id.posters_pay_money);
            posters_product_money = root.findViewById(R.id.posters_product_money);
            posters_look = root.findViewById(R.id.posters_look);
            posters_tostroe_count = root.findViewById(R.id.posters_tostroe_count);
            posters_look_today = root.findViewById(R.id.posters_look_today);
            posters_tostroe_count_today = root.findViewById(R.id.posters_tostroe_count_today);
            posters_order_type = root.findViewById(R.id.posters_order_type);
            posters_address_text = root.findViewById(R.id.posters_address_text);
            this.root = root;
        }
    }

}
