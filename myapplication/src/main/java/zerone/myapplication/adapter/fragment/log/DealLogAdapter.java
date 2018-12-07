package zerone.myapplication.adapter.fragment.log;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.domain.DoOperationBean;
import zerone.myapplication.domain.LogBean;
import zerone.myapplication.utils.TimeFormat;

/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class DealLogAdapter extends RecyclerView.Adapter<DealLogAdapter.ViewHolder> {

    private List<DoOperationBean> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口

    public DealLogAdapter(List<DoOperationBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<DoOperationBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_deallog_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置名
        holder.id.setText((position + 1) + "");
        holder.time.setText(TimeFormat.time(mData.get(position).getCreated_at()));
        String op = mData.get(position).getOperation_info();
        if (op != null && op.length() > 0) {
            holder.updata.setText(mData.get(position).getOperation_info());
        } else {
            holder.updata.setText("暂无操作");
        }
        holder.account.setText(mData.get(position).getUsername());
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
        public final TextView id;
        public final TextView time;
        public final TextView updata;
        public final TextView account;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            id = root.findViewById(R.id.id);
            time = root.findViewById(R.id.time);
            updata = root.findViewById(R.id.updata);
            account = root.findViewById(R.id.account);
            this.root = root;
        }
    }
}
