package zerone.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.utils.TimeFormat;
import zerone.myapplication.view.ZQImageViewRoundOval;


/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private List<ProductBean> mData;
    private Context mContext;
    /****************************************
     * Listener
     */
    private SalesclerAdapter.OnClickListener mOnClickListener = null;
    private SalesclerAdapter.OnLongClickListener mOnLongClickListener = null;

    public ProductListAdapter(List<ProductBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<ProductBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_product_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.p_catername.setText("");
        holder.p_name.setText(mData.get(position).getName());
        holder.p_price.setText("门店：￥" + mData.get(position).getPrice());
        holder.old_price.setText("平台：￥"+mData.get(position).getOld_price());
        holder.p_px.setText(mData.get(position).getDisplayorder()+"");
        holder.p_time.setText(TimeFormat.time(mData.get(position).getCreated_at()));
        holder.p_des.setText(mData.get(position).getDescription());
        Log.i("URL",mData.get(position).getThumb()+"");
        Glide.with(mContext).load(mData.get(position).getThumb()).error(R.mipmap.photo3).into(holder.p_img);
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

    public void setOnLongClickListener(SalesclerAdapter.OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnClickListener(SalesclerAdapter.OnClickListener listener) {
        mOnClickListener = listener;
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
        public final ZQImageViewRoundOval p_img;
        public final TextView p_name;
        public final TextView p_time;
        public final TextView p_price;
        public final TextView p_id;
        public final TextView p_status;
        public final TextView p_px;
        public final TextView p_catername;
        public final TextView p_des;
        public final TextView old_price;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            p_img = root.findViewById(R.id.p_img);
            p_name = root.findViewById(R.id.p_name);
            p_time = root.findViewById(R.id.p_time);
            p_price = root.findViewById(R.id.p_price);
            p_id = root.findViewById(R.id.p_id);
            p_status = root.findViewById(R.id.p_status);
            p_px = root.findViewById(R.id.p_px);
            p_catername = root.findViewById(R.id.p_catername);
            p_des=root.findViewById(R.id.p_des);
            old_price=root.findViewById(R.id.old_price);
            this.root = root;
        }
    }

}
