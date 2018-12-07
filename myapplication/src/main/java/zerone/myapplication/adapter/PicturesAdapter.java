package zerone.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.domain.ImagesBean;
import zerone.myapplication.domain.SalesclerBean;
import zerone.myapplication.view.ZQImageViewRoundOval;

/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder> {

    private List<ImagesBean> mData;
    private Context mContext;
    private OnClickListener mOnClickListener = null;
    private OnLongClickListener mOnLongClickListener = null;

    public PicturesAdapter(List<ImagesBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<ImagesBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pictures_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(mContext).load(mData.get(position).getThumb()).into(holder.img);
        holder.address.setText(mData.get(position).getThumb());
        holder.px.setText(mData.get(position).getDisplayorder()+"");
        int sta= mData.get(position).getStatus();
        if (sta==1){
            holder.status.setText("主图");
        }else if (sta==0){
            holder.status.setText("幅图");
        }
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
        public final TextView px;
        public final TextView address;
        public final ZQImageViewRoundOval img;
        public final TextView status;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            px = root.findViewById(R.id.px);
            address = root.findViewById(R.id.address);
            img = root.findViewById(R.id.img);
            status=root.findViewById(R.id.status);
            this.root = root;
        }
    }
}
