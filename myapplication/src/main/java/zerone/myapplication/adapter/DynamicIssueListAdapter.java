package zerone.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.adapter.fragment.MyGridItemAdapter;
import zerone.myapplication.domain.DynamicBean;
import zerone.myapplication.domain.ResponseBean;
import zerone.myapplication.utils.GridViewUtils;
import zerone.myapplication.utils.TimeFormat;


/**
 * Created by on 2018/4/9 0009 17 17.
 * Author  LiuXingWen
 */

public class DynamicIssueListAdapter extends RecyclerView.Adapter<DynamicIssueListAdapter.ViewHolder> {

    private List<DynamicBean> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口

    public DynamicIssueListAdapter(List<DynamicBean> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(List<DynamicBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_dynamicissue_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = 26)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //最后一个不显示底线
//        if (position == mData.size() - 1) {
//            holder.fg_lines.setVisibility(View.GONE);
//        }
        holder.contents.setText("\u3000\u3000" + mData.get(position).getContent());
        holder.contens_time.setText(TimeFormat.time(mData.get(position).getCreated_at()));
        holder.contents_look.setText("("+mData.get(position).getViews()+")人看过");
        List<DynamicBean.ThumbBean> list = mData.get(position).getThumb();
        if (list != null && list.size() > 0) {
            MyGridItemAdapter adapter = new MyGridItemAdapter(mContext, mData.get(position).getThumb());
            holder.gridview.setAdapter(adapter);
            GridViewUtils.setListViewHeightBasedOnChildren(holder.gridview, 3);
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView contents_look;
        public final TextView contents;
        public final TextView contens_time;
        public final GridView gridview;
        public final View fg_lines;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            contents_look = root.findViewById(R.id.contents_look);
            contents = root.findViewById(R.id.contents);
            contens_time = root.findViewById(R.id.contens_time);
            gridview = root.findViewById(R.id.gridview);
            fg_lines = root.findViewById(R.id.fg_lines);
            this.root = root;
        }
    }
}
