package zerone.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import zerone.myapplication.domain.ShopInfoBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.JsonToUser;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;

/**
 * Created by Administrator on 2018-11-16.
 * 添加商品分类
 */

public class AddCaterActivity extends BaseAppActivity {

    private Intent intent;
    private String cname;
    private EditText cterName;
    // i=0  是从主页面跳转到this  i=1 是从列表跳转到this
    private Button button;
    private int i = 0;
    private EditText catname;
    private EditText cat_sort;
    private String cname1;
    private String cpx;
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
                    Toast.makeText(AddCaterActivity.this, "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        String catjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catjson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            Toast.makeText(AddCaterActivity.this, "添加成功。", Toast.LENGTH_SHORT).show();
                            AddCaterActivity.this.finish();
                        } else if (code == 0) {
                            Toast.makeText(AddCaterActivity.this, "添加失败，请重试!!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
                    }
                    break;
                case 2:
                    try {
                        String edtjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(edtjson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            Toast.makeText(AddCaterActivity.this, "修改成功。", Toast.LENGTH_SHORT).show();
                            AddCaterActivity.this.finish();
                        } else if (code == 0) {
                            Toast.makeText(AddCaterActivity.this, "修改失败，请重试!!", Toast.LENGTH_SHORT).show();
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
    private String id;
    private TextView cathead;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcatering);
        intent = getIntent();
        cname = intent.getStringExtra("c_name");
        id = intent.getStringExtra("id");
        initView();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        cathead = (TextView) findViewById(R.id.cathead);
        catname = (EditText) findViewById(R.id.catname);
        cat_sort = (EditText) findViewById(R.id.cat_sort);
        button = (Button) findViewById(R.id.comfig_btn);
        titleName.setText("添加商品分类");
        if (cname != null && cname.length() > 0) {
            cathead.setText("修改商品分类");
            button.setText("修改分类");
            i = 1;
        }
    }
    public void onSaveInfo(View view) {
        cname1 = catname.getText().toString().trim();
        cpx = cat_sort.getText().toString().trim();
        if (i == 0) {
            gotoAddCat();
        } else if (i == 1) {
            gotoChangeCat();
        }
    }

    /**
     * 修改分类信息
     */
    private void gotoChangeCat() {
        if (!(cname1.length() > 0)) {
            Toast.makeText(AddCaterActivity.this, "请输入分类名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(cpx.length() > 0)) {
            Toast.makeText(AddCaterActivity.this, "请输入排序", Toast.LENGTH_SHORT).show();
            return;
        }
//        "name":"饮料", "displayorder":"1",
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
        parmar.put("name", cname1);
        parmar.put("id", id);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.EDITCAT, handler, 2);
    }

    /**
     * 添加分类
     */
    private void gotoAddCat() {
        Log.i("URL","8777777789999999999999");
        if (!(cname1.length() > 0)) {
            Toast.makeText(AddCaterActivity.this, "请输入分类名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(cpx.length() > 0)) {
            Toast.makeText(AddCaterActivity.this, "请输入排序", Toast.LENGTH_SHORT).show();
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
        parmar.put("name", cname1);
        parmar.put("displayorder", cpx);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.ADDCAT, handler, 1);
    }
}
