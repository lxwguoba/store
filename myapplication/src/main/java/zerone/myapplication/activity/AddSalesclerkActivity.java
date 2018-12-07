package zerone.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.SalesclerBean;
import zerone.myapplication.utils.CheckInputValue;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;

/**
 * Created by Administrator on 2018-11-16.
 * 添加小助手店员
 */

public class AddSalesclerkActivity extends BaseAppActivity {

    private EditText user_name;
    private EditText user_phone;
    private EditText user_passwrod;
    private EditText user_rpasswrod;
    private EditText user_safetyPwd;
    private String name;
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
                    tosatMessage("没有网络或者是网络出错了，请检查!");
                    break;
                case 1:
                    try {
                        String catjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catjson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            tosatMessage("店员添加成功!");
                            AddSalesclerkActivity.this.finish();
                        } else if (code == 0) {
                            tosatMessage("店员添加失败，请重试!");
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
    private Button btn;
    private SalesclerBean salesclerBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsalesclerk);
        salesclerBean = (SalesclerBean) getIntent().getSerializableExtra("salesclerBean");
        initView();

    }

    /**
     * 初始化view
     */
    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        user_name = (EditText) findViewById(R.id.user_name);
        user_phone = (EditText) findViewById(R.id.user_phone);
        user_passwrod = (EditText) findViewById(R.id.user_passwrod);
        user_rpasswrod = (EditText) findViewById(R.id.user_rpasswrod);
        user_safetyPwd = (EditText) findViewById(R.id.user_safetyPwd);
        if (salesclerBean != null) {
            btn.setText("编辑店员");
            user_name.setText(salesclerBean.getRealname());
            user_phone.setText(salesclerBean.getMobile());
        }
    }

    /**
     * 保存新的店员
     *
     * @param view
     */
    public void onAddSalescler(View view) {
        String name = user_name.getText().toString().trim();
        String phone = user_phone.getText().toString().trim();
        String passwrod = user_passwrod.getText().toString().trim();
        String rpasswrod = user_rpasswrod.getText().toString().trim();
        String safetyPwd = user_safetyPwd.getText().toString().trim();
        if (!(name.length() > 0)) {
            tosatMessage("请输入用户名");
            return;
        }
        if (!(phone.length() > 0)) {
            tosatMessage("请输入手机号");
            return;
        }
        //判断输入的手机号码是否合法
        if (!CheckInputValue.isPhone2(AddSalesclerkActivity.this, phone)) {
            tosatMessage("请输入正确的手机号");
            return;
        }

        if (!(passwrod.length() > 0)) {
            tosatMessage("请输入密码");
            return;
        }
        if (!(rpasswrod.length() > 0)) {
            tosatMessage("请输入重复密码");
            return;
        }
        if (!(safetyPwd.length() > 0)) {
            tosatMessage("请输入安全密码");
            return;
        }
        if (!passwrod.equals(rpasswrod)) {
            tosatMessage("两次输入的密码不一致，请重新输入");
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
        parmar.put("realname", name);
        parmar.put("mobile", phone);
        parmar.put("password", passwrod);
        parmar.put("again_password", rpasswrod);
        parmar.put("safe_password", safetyPwd);
        if (salesclerBean != null) {
            parmar.put("id", salesclerBean.getId() + "");
            NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CLERK_EDIT, handler, 1);
        } else {
            NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CLERK_ADD, handler, 1);
        }
    }
}
