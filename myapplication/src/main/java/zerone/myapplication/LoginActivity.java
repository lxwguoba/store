package zerone.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zerone.myapplication.activity.ChangePassWordByPhoneActivity;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.ShopInfoBean;
import zerone.myapplication.utils.AppSharePreferenceMgr;
import zerone.myapplication.utils.AppUPDataUtils;
import zerone.myapplication.utils.JsonToUser;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;

/**
 * Created by Administrator on 2018-11-09.
 */

public class LoginActivity extends BaseAppActivity {

    private ZLoadingDialog zLoadingDialog;
    private EditText pwd;
    private EditText username;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    Toast.makeText(LoginActivity.this, "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    try {
                        String loingjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(loingjson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            String userStr = jsonObject.getString("data");
                            Gson gson = new Gson();
                            ShopInfoBean shopInfoBean = gson.fromJson(userStr, ShopInfoBean.class);
                            JsonToUser.saveShopInfo(LoginActivity.this, shopInfoBean);
                            AppSharePreferenceMgr.put(LoginActivity.this, "username", username.getText().toString());
                            Intent intent = new Intent(LoginActivity.this, Main_Activity.class);
                            startActivity(intent);
                            finish();
                        } else if (code == 0) {
                            //登录失败
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
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




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkVersion();
        initView();
        initPermission();
    }

    private void initView() {
        pwd = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.user_name);

    }

    private void checkVersion() {
        try {
            String version = AppUPDataUtils.getVersionName(this);
            Log.i("URL", version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void onGotoMain(View view) {
        String password = pwd.getText().toString().trim();
        String name = username.getText().toString().trim();

        if (!(name.length() > 0)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(password.length() > 0)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "登录中···");
        }
        zLoadingDialog.show();
        Map<String, String> parmar = new HashMap<String, String>();
        parmar.put("username", name);
        parmar.put("password", password);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.LOGIN, handler, 3);
    }
    public void changePassWord(View view) {
        Intent intent = new Intent(this, ChangePassWordByPhoneActivity.class);
        startActivity(intent);
    }

    private void initPermission() {
        String[] permissions = {
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_SETTINGS,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.CHANGE_WIFI_STATE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_CALENDAR,
                android.Manifest.permission.WRITE_CALENDAR,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
        };
        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }
    }

}
