package com.zerone.intodata.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.zerone.intodata.adapter.PictureListAdtaper;


/**
 * Created by 刘兴文 on 2018-12-11.
 */

public class ItemGridLayoutManager extends GridLayoutManager {
    PictureListAdtaper adapter;
    TypedArray a;
    Drawable mDivider;
    ViewTreeObserver obs;

    /**
     * @param context      上下文
     * @param spanCount    列数
     * @param adapter      数据适配器
     * @param recyclerView 当前的RecyclerView
     */
    public ItemGridLayoutManager(Context context, int spanCount, PictureListAdtaper adapter, final RecyclerView recyclerView) {
        super(context, spanCount);
        this.adapter = adapter;
        a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        mDivider = a.getDrawable(0);
        obs = recyclerView.getViewTreeObserver();
        obs.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                calculeRecyclerViewFullHeight(recyclerView);
                obs = recyclerView.getViewTreeObserver();
                obs.removeOnPreDrawListener(this);
                return true;
            }
        });
    }
    /**
     * 刷新高度，使RecyclerView得高度为wrap_content
     */
    private void calculeRecyclerViewFullHeight(RecyclerView recyclerView) {
        int height = 0;
        View view = recyclerView.getChildAt(0);
        if (view != null) {
            height = view.getHeight();
        }
        int line = adapter.getItemCount() / getSpanCount();
        if (adapter.getItemCount() % getSpanCount() > 0) {
            line++;
        }
        SwipeRefreshLayout.LayoutParams params = recyclerView.getLayoutParams();
        params.height = height * line + (line - 1) * mDivider.getIntrinsicWidth();
        recyclerView.setLayoutParams(params);
    }
}
