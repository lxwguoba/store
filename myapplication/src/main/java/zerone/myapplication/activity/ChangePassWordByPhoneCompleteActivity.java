package zerone.myapplication.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.utils.CheckInputValue;

/**
 * Created by Administrator on 2018-11-16.
 */

public class ChangePassWordByPhoneCompleteActivity extends BaseAppActivity {

    private EditText newpwd;
    private EditText rnewpwd;
    private TextView msgg;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    newpwd.setHint("请输入密码");
                    newpwd.setHintTextColor(Color.parseColor("#ff0000"));
                    break;
                case 2:
                    rnewpwd.setHint("重复密码不能为空");
                    rnewpwd.setHintTextColor(Color.parseColor("#ff0000"));
                    break;
                case 3:
                    msgg.setText("两次输入的密码不一致，请重新输入");
                    msgg.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changebyphonepasswordfinal);
        initView();
        initListenner();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initListenner() {
        newpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (msgg.isShown()){
                        msgg.setVisibility(View.GONE);
                    }
                }
            }
        });
        rnewpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (msgg.isShown()){
                        msgg.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public void onBackActivity(View view) {
        this.finish();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        //设置标题
        titleName.setText("修改密码");
        newpwd = (EditText) findViewById(R.id.newpwd);
        rnewpwd = (EditText) findViewById(R.id.rnewpwd);
        msgg = (TextView) findViewById(R.id.msg);
    }

    /**
     * 修改密码的按钮
     *
     * @param view
     */
    public void onChangePassWord(View view) {
        String pwd = newpwd.getText().toString().trim();
        String rpwd = rnewpwd.getText().toString().trim();

        if (!(pwd.length() > 0)) {
            Message m01 = new Message();
            m01.what = 1;
            handler.sendMessage(m01);
            return;
        }
        if (!(rpwd.length() > 0)) {
            Message m01 = new Message();
            m01.what = 2;
            handler.sendMessage(m01);
            return;
        }
        if (!(pwd.equals(rpwd))) {
            Message m01 = new Message();
            m01.what = 3;
            handler.sendMessage(m01);
            return;
        }

    }
}
