package zerone.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.domain.Customer;
import zerone.myapplication.domain.LogBean;
import zerone.myapplication.view.CircleImageView;


/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private List<Customer> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE     = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE     = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;
    public CustomerAdapter(List<Customer> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<Customer> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_datas_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置名

        holder.userName.setText(Html.fromHtml(mData.get(position).getUsername()+"向您支付了"+mData.get(position).getPaymoney()+"元"));
        holder.userlogintime.setText(mData.get(position).getLogintime());
//        Glide.with(mContext).load(mData.get(position).getUserhead()).into(holder.userhead);
        if (position==(mData.size()-1)){
            holder.lines.setVisibility(View.INVISIBLE);
        }
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
        public final CircleImageView userhead;
        public final TextView userName;
        public final TextView userlogintime;
        public final View lines;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            userhead = root.findViewById(R.id.userhead);
            userName = root.findViewById(R.id.userName);
            userlogintime = root.findViewById(R.id.userlogintime);
            lines=root.findViewById(R.id.lines);
            this.root = root;
        }
    }

    public void AddHeaderItem(Customer items){
        mData.add(0,items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(Customer items){
        mData.add(items);
        notifyDataSetChanged();
    }
    /**
     * 更新加载更多状态
     * @param status
     */
    public void changeMoreStatus(int status){
        mLoadMoreStatus=status;
        notifyDataSetChanged();
    }
}
