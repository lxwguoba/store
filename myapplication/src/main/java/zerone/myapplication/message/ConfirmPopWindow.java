package zerone.myapplication.message;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import zerone.myapplication.R;

/**
 * Created by Administrator on 2018-11-24.
 */

public class ConfirmPopWindow extends PopupWindow {
    private Context context;
    private TextView titleTv,contentTv;
    private View okBtn,cancelBtn;
    private OnDialogClickListener dialogClickListener;

    public ConfirmPopWindow(Context context) {
        super(context);
        this.context = context;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message , null);
        setContentView(view);
        initWindow();

        titleTv = (TextView) view.findViewById(R.id.title_name);
        contentTv = (TextView) view.findViewById(R.id.text_view);
        okBtn = view.findViewById(R.id.btn_ok);
        cancelBtn = view.findViewById(R.id.btn_cancel);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if(dialogClickListener != null){
                    dialogClickListener.onOKClick();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if(dialogClickListener != null){
                    dialogClickListener.onCancelClick();
                }
            }
        });
    }

    private void initWindow() {
        this.setBackgroundDrawable(new ColorDrawable(0));
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        this.setWidth((int) (d.widthPixels * 0.8));
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
    }

    public void showAtBottom(View view){
        showAsDropDown(view, Math.abs((view.getWidth() - getWidth())/2), 20);
    }

    public void setOnDialogClickListener(OnDialogClickListener clickListener){
        dialogClickListener = clickListener;
    }

    public interface OnDialogClickListener{
        void onOKClick();
        void onCancelClick();
    }
}
