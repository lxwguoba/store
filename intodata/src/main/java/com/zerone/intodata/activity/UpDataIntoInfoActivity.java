package com.zerone.intodata.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.zerone.intodata.BaseAppActivity;
import com.zerone.intodata.R;
import com.zerone.intodata.config.IPConfig;
import com.zerone.intodata.utils.AppSharePreferenceMgr;
import com.zerone.intodata.utils.CreateTokenUtils;
import com.zerone.intodata.utils.LoadingUtils;
import com.zerone.intodata.utils.MessageShow;
import com.zerone.intodata.utils.NetUtils;
import com.zerone.intodata.utils.UserConfig;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘兴文 on 2018-12-18.
 */

public class UpDataIntoInfoActivity extends BaseAppActivity {

    private String store_id;
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
                    break;
                case 1:
                    try {
                        String shJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(shJson);
                        Log.i("URL", jsonObject.toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            MessageShow.initErrorDialog(UpDataIntoInfoActivity.this, jsonObject.getString("msg"), 1);
                            AppSharePreferenceMgr.put(UpDataIntoInfoActivity.this, "store_id", "");
                        } else {
                            MessageShow.initErrorDialog(UpDataIntoInfoActivity.this, jsonObject.getString("msg"), 0);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_intoinfo);
        store_id = (String) getIntent().getStringExtra("store_id");
    }

    /**
     * 修改进件的照片
     */
    public void onUpDataAddDocumentsData(View view) {
        Intent intent = new Intent(this, UpDataDocumentsDataActivity.class);
        intent.putExtra("store_id", store_id);
        startActivity(intent);

    }

    /**
     * 提交审核
     *
     * @param view
     */
    public void onApplyForDo(View view) {
        gotoShengHe();
    }

    /**
     * 关闭页面
     *
     * @param view
     */
    public void onCloseActivity(View view) {
        this.finish();
    }

    /**
     * 修改银行卡信息
     */
    public void onUpDtaAddBankInfo(View view) {
        Intent intent = new Intent(this, UpDataBankInfoActivity.class);
        intent.putExtra("store_id", store_id);
        startActivity(intent);
    }

    /**
     * 修改新增店铺信息
     */
    public void onUpDataAddStore(View view) {
        Intent intent = new Intent(this, UpDataAddStoreActivity.class);
        intent.putExtra("store_id", store_id);
        startActivity(intent);
    }

    /**
     * 提交审核
     */
    private void gotoShengHe() {
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        String userName = UserConfig.getUserName(this);
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        parmar.put("store_id", store_id);
        zLoadingDialog = LoadingUtils.openLoading(this, "提交审核中···");
        zLoadingDialog.show();
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.UPDATE_STATUS, handler, 1);
    }
}
