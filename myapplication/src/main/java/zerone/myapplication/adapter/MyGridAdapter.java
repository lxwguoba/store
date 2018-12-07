package zerone.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import zerone.myapplication.R;

/**
 * Created by Administrator on 2018-11-19.
 */

public class MyGridAdapter extends BaseAdapter {

    private Context context;
    private List<Bitmap> list;

    public MyGridAdapter(Context context, List<Bitmap> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size()+1;
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
            view = LayoutInflater.from(context).inflate(R.layout.dynamicissue_gridview_item, null);
            holder = new ViewHolder();
            holder.img_show = view.findViewById(R.id.img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (i== list.size()) {
            holder.img_show.setImageResource(R.mipmap.add);
        } else {
//            Glide.with(context).load(monster).into(holder.img_show);
            holder.img_show.setImageBitmap(list.get(i));
        }

        return view;
    }

    class ViewHolder {
        ImageView img_show;
    }
}
