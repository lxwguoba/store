package zerone.myapplication.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ChangePassWordByPhoneActivity extends BaseAppActivity {

    private EditText phoneEdt;
    private Button getcode;
    private EditText code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changebyphonepassword);
        initView();
    }
    public void onBackActivity(View view) {
        this.finish();
    }
    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        //设置标题
        titleName.setText("修改密码");
        phoneEdt = (EditText) findViewById(R.id.phoneEdt);
        getcode = (Button) findViewById(R.id.getcode);
        code = (EditText) findViewById(R.id.code);
        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone= phoneEdt.getText().toString();
                /**
                 * 判断是否输入手机号码
                 */
                if (!TextUtils.isEmpty(phone)){
                    //判断输入的手机号码是否合法
                    if (CheckInputValue.isPhone2(ChangePassWordByPhoneActivity.this,phone)){
                        new countDownTimer<Button>(getcode);
                    }else {
                        Toast.makeText(ChangePassWordByPhoneActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ChangePassWordByPhoneActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onNextChangePassWord(View view){
      String codes=  code.getText().toString().trim();
        if (codes.length()<0){
            Toast.makeText(ChangePassWordByPhoneActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }
        //发送验证码 进入下一步修改密码
        Intent intent  = new Intent(this,ChangePassWordByPhoneCompleteActivity.class);
        startActivity(intent);
    }

    /**
     * 倒计时
     * @param <T>
     */
    public class countDownTimer<T> extends CountDownTimer {
        private Button mTextView;
        public countDownTimer(T text) {
            super(60000,1000);
            this.mTextView = ((Button)text);
            mTextView.setClickable(false);
            start();
        }
        /**
         * start后开始执行的方法，按照间隔时间执行一次
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {
            mTextView.setText(String.format("%s秒后重新获取验证码",millisUntilFinished/1000));
        }
        /**
         * 时间结束时调用的方法
         */
        @Override
        public void onFinish() {
            mTextView.setText("获取验证码");
            mTextView.setClickable(true);
        }
    }
}
