package com.zerone.intodata.utils;

import android.content.Context;
import android.graphics.Color;

import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

/**
 * Created by Administrator on 2018-11-23.
 */

public class LoadingUtils {
    /**
     * 加载动画
     *
     * @param context
     * @param message 提示消息
     * @return
     */
    public static ZLoadingDialog openLoading(Context context, String message) {
        ZLoadingDialog dialog = new ZLoadingDialog(context);
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.parseColor("#09bb07"))//颜色
                .setHintText(message)
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.parseColor("#CC111111")); // 设置背景色，默认白色
        return dialog;
    }

    /**
     * @param context
     * @param message   提示消息
     * @param type      加载动画的类型
     * @param color     加载的颜色
     * @param textcolor 加载的字体颜色
     * @param bgcolor   背景颜色
     * @return
     */
    public static ZLoadingDialog openLoading(Context context, String message, Z_TYPE type, int color, int textcolor, int bgcolor) {
        ZLoadingDialog dialog = new ZLoadingDialog(context);
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(color)//颜色
                .setHintText(message)
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(textcolor)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(bgcolor); // 设置背景色，默认白色
        return dialog;
    }
}
