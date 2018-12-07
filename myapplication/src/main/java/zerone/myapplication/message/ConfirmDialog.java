package zerone.myapplication.message;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import zerone.myapplication.R;

/**
 * Created by Administrator on 2018-11-24.
 */

public class ConfirmDialog extends Dialog {
    private Context context;
    private TextView titleTv, contentTv;
    private View okBtn, cancelBtn;
    private boolean lean;
    private OnDialogClickListener dialogClickListener;
    private EditText edit_view;

    public ConfirmDialog(Context context) {
        super(context);
        this.context = context;
        lean=false;
        initalize();
    }

    public ConfirmDialog(Context context, boolean lean) {
        super(context);
        this.context = context;
        this.lean = lean;
        initalize();
    }

    //初始化View
    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message, null);
        setContentView(view);
        initWindow();
        titleTv = (TextView) findViewById(R.id.title_name);
        contentTv = (TextView) findViewById(R.id.text_view);
        edit_view = (EditText) findViewById(R.id.edit_view);
        if (lean){
            edit_view.setVisibility(View.VISIBLE);
        }
        okBtn = findViewById(R.id.btn_ok);
        cancelBtn = findViewById(R.id.btn_cancel);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lean) {
                    String str = edit_view.getText().toString().trim();
                    if (!(str.length() > 0)) {
                        Toast.makeText(context, "请输入安全密码", Toast.LENGTH_SHORT).show();
                    } else {
                        dismiss();
                        if (dialogClickListener != null) {
                            dialogClickListener.onOKClick(str);
                        }
                    }
                } else {
                    dismiss();
                    if (dialogClickListener != null) {
                        dialogClickListener.onOKClick();
                    }
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (dialogClickListener != null) {
                    dialogClickListener.onCancelClick();
                }
            }
        });
    }

    /**
     * 添加黑色半透明背景
     */
    private void initWindow() {
        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawable(new ColorDrawable(0));//设置window背景
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//设置输入法显示模式
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();//获取屏幕尺寸
        lp.width = (int) (d.widthPixels * 0.8); //宽度为屏幕80%
        lp.gravity = Gravity.CENTER;  //中央居中
        dialogWindow.setAttributes(lp);
    }

    public void setOnDialogClickListener(OnDialogClickListener clickListener) {
        dialogClickListener = clickListener;
    }

    /**
     * 添加按钮点击事件
     */
    public interface OnDialogClickListener {
        void onOKClick(String spwd);
        void onOKClick();
        void onCancelClick();
    }
}
