package com.zerone.intodata.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zerone.intodata.BaseAppActivity;
import com.zerone.intodata.MainActivity;
import com.zerone.intodata.R;
import com.zerone.intodata.config.IPConfig;
import com.zerone.intodata.utils.AppSharePreferenceMgr;
import com.zerone.intodata.utils.LoadingUtils;
import com.zerone.intodata.utils.NetUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘兴文 on 2018-12-17.
 */

public class LoginActivity extends BaseAppActivity {
    @Bind(R.id.messageError)
    TextView messageError;
    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.userpassword)
    EditText userpassword;
    @Bind(R.id.remberUser)
    CheckBox remberUser;
    @Bind(R.id.login_btn)
    Button loginBtn;
    private ZLoadingDialog zLoadingDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                        zLoadingDialog.cancel();
                    }
                    messageError.setVisibility(View.VISIBLE);
                    messageError.setText("系统正在升级中");
                    break;
                case 1:
                    messageError.setVisibility(View.VISIBLE);
                    String merror = (String) msg.obj;
                    messageError.setText(merror);
                    break;
                case 2:
                    try {
                        String mresponse = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(mresponse);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            AppSharePreferenceMgr.put(LoginActivity.this, "username", username.getText().toString());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            messageError.setVisibility(View.VISIBLE);
                            messageError.setText(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                            zLoadingDialog.cancel();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initListenner();
    }

    private void initListenner() {
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    messageError.setVisibility(View.GONE);
                }
            }
        });
        userpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    messageError.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick(R.id.login_btn)
    public void onViewClicked() {
        String name = username.getText().toString().trim();
        String password = userpassword.getText().toString().trim();
        if (!(name != null && name.length() > 0)) {
            Message message = new Message();
            message.what = 1;
            message.obj = "请输入用户名";
            handler.sendMessage(message);
            return;
        }

        if (!(password != null && password.length() > 0)) {
            Message message = new Message();
            message.what = 1;
            message.obj = "请输入密码";
            handler.sendMessage(message);
            return;
        }
        gotoLogin(name, password);

    }

    private void gotoLogin(String name, String password) {
        zLoadingDialog = LoadingUtils.openLoading(this, "登录中···");
        zLoadingDialog.show();
        Map<String, String> parmar = new HashMap<>();
        parmar.put("username", name);
        parmar.put("password", password);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.LOGIN, handler, 2);
    }


}
