package zerone.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;

/**
 * Created by 刘兴文 on 2018-12-06.
 * 收款二维码
 */

public class CollectionQrCodeActivity extends BaseAppActivity {

    private ImageView qrcode;
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
                            String url = jsonObject.getString("data");
                            Glide.with(CollectionQrCodeActivity.this).load(url).into(qrcode);
                        }else if (code==0){
                            createQrCode.setVisibility(View.VISIBLE);
                            tosatMessage("您暂时没有收款二维码，点击右上角按钮生成二维码");
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
                        String catjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catjson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            createQrCode.setVisibility(View.GONE);
                            tosatMessage("二维码创建成功");
                            String url = jsonObject.getString("data");
                            Glide.with(CollectionQrCodeActivity.this).load(url).into(qrcode);
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
    private Button createQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectionqrcode);
        initView();
        initGetQrCode();
    }

    /**
     * 获取收款二维码
     */
    private void initGetQrCode() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "二维码加载中");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.RECEIPT_CODE, handler, 1);
    }

    private void initView() {
        qrcode = (ImageView) findViewById(R.id.qrcode);
        createQrCode = (Button) findViewById(R.id.createQrCode);
    }

    /**
     * 生成二维码
     *
     * @param view
     */
    public void onCreateQeCode(View view) {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "二维码生成中");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.RECEIPT_CODE_ADD, handler, 2);
    }
}
