package zerone.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
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

public class ChangeLoginPassWordActivity extends BaseAppActivity {

    private EditText oldpwd;
    private EditText newpwd;
    private EditText rnewpwd;
    private TextView userInfo;
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
                            tosatMessage("修改成功,请重新登录");
                            Intent intent = new Intent(ChangeLoginPassWordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            ChangeLoginPassWordActivity.this.removeALLActivity();
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        user = (String) AppSharePreferenceMgr.get(this,"username","");
        initView();
    }

    public void onBackActivity(View view) {
        this.finish();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        //设置标题
        titleName.setText("登录密码修改");
        userInfo = (TextView) findViewById(R.id.userInfo);
        userInfo.setText(user);
        oldpwd = (EditText) findViewById(R.id.oldpwd);
        newpwd = (EditText) findViewById(R.id.newpwd);
        rnewpwd = (EditText) findViewById(R.id.rnewpwd);

    }

    public void onSaveInfo(View view) {
        String oldpassword = oldpwd.getText().toString().trim();
        String newassword = newpwd.getText().toString().trim();
        String rnewassword = rnewpwd.getText().toString().trim();
        if (!(oldpassword.length() > 0)) {
            tosatMessage("请输入旧密码");
            return;
        }
        if (!(newassword.length() > 0)) {
            tosatMessage("请输入新密码");
            return;
        }
        if (!(rnewassword.length() > 0)) {
            tosatMessage("请输入确认密码");
            return;
        }
        if (!(newassword.equals(rnewassword))) {
            tosatMessage("两次输入的新密码不一致");
            return;
        }

        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "添加分类···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("password", oldpassword);
        parmar.put("new_password", newassword);
        parmar.put("again_password", rnewassword);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CHANGE_PASSWORD, handler, 1);
    }
}
