package zerone.myapplication.adapter.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import zerone.myapplication.R;
import zerone.myapplication.domain.DynamicBean;

/**
 * Created by Administrator on 2018-11-19.
 */

public class MyGridItemAdapter extends BaseAdapter {

    private Context context;
    private List<DynamicBean.ThumbBean> list;

    public MyGridItemAdapter(Context context, List<DynamicBean.ThumbBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.dynamicissue_gridview_item_item, null);
            holder = new ViewHolder();
            holder.img_show = view.findViewById(R.id.img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(list.get(i).getThumb()).into(holder.img_show);
        return view;
    }

    class ViewHolder {
        PhotoView img_show;
    }
}
