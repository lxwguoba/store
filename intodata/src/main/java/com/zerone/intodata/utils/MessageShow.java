package com.zerone.intodata.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zerone.intodata.R;


/**
 * Created by 刘兴文 on 2018-12-18.
 */

public class MessageShow {

    /**
     * @param context       上下文
     * @param messs         提示消息
     * @param closeActivity 是否需要关闭页面  0不关闭 1为关闭
     * @return
     */
    public static Dialog initErrorDialog(final Activity context, String messs, final int closeActivity) {
        final Dialog dialog = new Dialog(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.show_message, null);
        TextView text_view = contentView.findViewById(R.id.text_view);
        dialog.setContentView(contentView);
        Button btn = contentView.findViewById(R.id.btn_ok);
        Button btn_c = contentView.findViewById(R.id.btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (closeActivity == 1) {
                    context.finish();
                }
            }
        });
        btn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (messs.length() > 0) {
            text_view.setText(messs);
        }
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        //注意一定要是MATCH_PARENT
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }
}
