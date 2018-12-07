package zerone.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.sql.Time;
import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.activity.VipCustomerDetailsActivity;
import zerone.myapplication.domain.ConsumersBean;
import zerone.myapplication.utils.TimeFormat;
import zerone.myapplication.view.XCRoundRectImageView;
import zerone.myapplication.view.ZQImageViewRoundOval;

/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class ConsumersAdapter extends RecyclerView.Adapter<ConsumersAdapter.ViewHolder> {

    private List<ConsumersBean> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口

    public ConsumersAdapter(List<ConsumersBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<ConsumersBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_consumers_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置名
        holder.m_id.setText("ID：" +(position+1));
        holder.m_wx_nickname.setText("微信昵称：" + mData.get(position).getNickname());
        holder.m_phone.setText("手机号码：" + mData.get(position).getMobile());
        holder.m_xf_count.setText(mData.get(position).getShopping_times()+"");
        holder.m_xf_money.setText(mData.get(position).getAmount()+ "￥");
        holder.firstTime.setText(TimeFormat.time(mData.get(position).getFirst_time()));
        holder.lastTime.setText(TimeFormat.time(mData.get(position).getLast_time()));
        Glide.with(mContext).load(mData.get(position).getHeadimgurl()).error(R.mipmap.photo3).into(holder.m_wx_img);
        //事件会冲突
        holder.btn_goto_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VipCustomerDetailsActivity.class);
                mContext.startActivity(intent);
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
        public final ZQImageViewRoundOval m_wx_img;
        public final TextView m_wx_nickname;
        public final TextView m_id;
        public final TextView m_phone;
        public final TextView firstTime;
        public final TextView lastTime;
        public final TextView m_xf_count;
        public final TextView m_xf_money;
        public final Button btn_goto_details;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            m_wx_img = root.findViewById(R.id.m_wx_img);
            m_wx_nickname = root.findViewById(R.id.m_wx_nickname);
            m_id = root.findViewById(R.id.m_id);
            m_phone = root.findViewById(R.id.m_phone);
            firstTime = root.findViewById(R.id.firstTime);
            lastTime = root.findViewById(R.id.lastTime);
            m_xf_count = root.findViewById(R.id.m_xf_count);
            m_xf_money = root.findViewById(R.id.m_xf_money);
            btn_goto_details = root.findViewById(R.id.btn_goto_details);
            this.root = root;
        }
    }
}
