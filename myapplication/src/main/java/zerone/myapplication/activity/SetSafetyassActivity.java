package zerone.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.LoginActivity;
import zerone.myapplication.R;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.utils.AppSharePreferenceMgr;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;

/**
 * Created by Administrator on 2018-11-16.
 */

public class SetSafetyassActivity extends BaseAppActivity {
    private EditText rnewan_pwd;
    private EditText newan_pwd;
    private TextView userInfo;
    private EditText oldpwd_an;
    private ZLoadingDialog zLoadingDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    tosatMessage("网络出错了，请检查后重试");
                    break;

                case 1:

                    try {
                        String cjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(cjson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            tosatMessage("修改成功");
                            SetSafetyassActivity.this.finish();
                        } else if (status == 0) {
                            tosatMessage("修改没有成功哦，请重试");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
                    }
                    break;
            }
        }
    };
    private String user;
    private LinearLayout oldlayout;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setsafetypassword);
        user = (String) AppSharePreferenceMgr.get(this, "username", "");
        initView();
    }
    public void onBackActivity(View view) {
        this.finish();
    }
    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        //设置标题
        btn = (Button) findViewById(R.id.btn);
        oldlayout = (LinearLayout) findViewById(R.id.oldlayout);
        if (shopInfoBean.getSafe_password()!=null ) {
            oldlayout.setVisibility(View.VISIBLE);
            titleName.setText("安全密码修改");
        } else {
            oldlayout.setVisibility(View.GONE);
            titleName.setText("设置");
        }
        userInfo = (TextView) findViewById(R.id.userInfo);
        userInfo.setText(user);
        oldpwd_an = (EditText) findViewById(R.id.oldpwd_an);
        newan_pwd = (EditText) findViewById(R.id.newan_pwd);
        rnewan_pwd = (EditText) findViewById(R.id.rnewan_pwd);

    }

    public void onSaveSafetyInfo(View view) {
        String newan_pwds = newan_pwd.getText().toString().trim();
        String rnewan_pwds = rnewan_pwd.getText().toString().trim();
        String oldpwd_ans = oldpwd_an.getText().toString().trim();
        if (shopInfoBean.getSafe_password()!=null) {
            if (!(oldpwd_ans.length() > 0)) {
                tosatMessage("请输入旧密码");
                return;
            }
        }
        if (!(newan_pwds.length() > 0)) {
            tosatMessage("请输入新密码");
            return;
        }
        if (!(rnewan_pwds.length() > 0)) {
            tosatMessage("请输入确认密码");
            return;
        }
        if (!(rnewan_pwds.equals(newan_pwds))) {
            tosatMessage("两次输入的新密码不一致");
            return;
        }

        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "修改安全密码···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        if (shopInfoBean.getSafe_password()!=null) {
            parmar.put("safe_password", oldpwd_ans);
        }
        parmar.put("new_safe_password", newan_pwds);
        parmar.put("re_safe_password", rnewan_pwds);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.SAFE_PASSWORD_EDIT, handler, 1);
    }
}
