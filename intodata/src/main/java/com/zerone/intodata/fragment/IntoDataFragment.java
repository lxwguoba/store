package com.zerone.intodata.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.zerone.intodata.R;
import com.zerone.intodata.activity.AddStoreActivity;
import com.zerone.intodata.activity.BankInfoActivity;
import com.zerone.intodata.activity.DocumentsDataActivity;
import com.zerone.intodata.config.IPConfig;
import com.zerone.intodata.utils.AppSharePreferenceMgr;
import com.zerone.intodata.utils.CreateTokenUtils;
import com.zerone.intodata.utils.LoadingUtils;
import com.zerone.intodata.utils.MessageShow;
import com.zerone.intodata.utils.NetUtils;
import com.zerone.intodata.utils.UserConfig;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.converter.DoubleColumnConverter;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘兴文 on 2018-12-17.
 * 进件页面
 */

public class IntoDataFragment extends Fragment {

    @Bind(R.id.onAddStore)
    RelativeLayout onAddStore;
    @Bind(R.id.onAddBankInfo)
    RelativeLayout onAddBankInfo;
    @Bind(R.id.applybtn)
    Button applybtn;
    @Bind(R.id.onAddDocumentsData)
    RelativeLayout onAddDocumentsData;
    private View view;
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
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            MessageShow.initErrorDialog(getActivity(), jsonObject.getString("msg"), 0);
                            AppSharePreferenceMgr.put(getActivity(), "store_id", "");
                        } else {
                            MessageShow.initErrorDialog(getActivity(), jsonObject.getString("msg"), 0);
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
    private String store_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_intodata, container, false);
        }
        ButterKnife.bind(this, view);
        store_id = (String) AppSharePreferenceMgr.get(getContext(), "store_id", "");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.onAddStore, R.id.onAddBankInfo, R.id.applybtn, R.id.onAddDocumentsData})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.onAddStore:
                Intent intent = new Intent(getContext(), AddStoreActivity.class);
                startActivity(intent);
                break;
            case R.id.onAddBankInfo:
                Intent intent01 = new Intent(getContext(), BankInfoActivity.class);
                startActivity(intent01);
                break;
            case R.id.onAddDocumentsData:
                Intent intent02 = new Intent(getContext(), DocumentsDataActivity.class);
                startActivity(intent02);
                break;
            case R.id.applybtn:
                gotoShengHe();
                break;
        }
    }

    /**
     * 提交审核
     */
    private void gotoShengHe() {
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(getActivity(), timestamp);
        String userName = UserConfig.getUserName(getActivity());
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        parmar.put("store_id", store_id);
        if (store_id.length() > 0) {
            zLoadingDialog = LoadingUtils.openLoading(getActivity(), "提交审核中···");
            zLoadingDialog.show();
            NetUtils.netWorkByMethodPost(getActivity(), parmar, IPConfig.UPDATE_STATUS, handler, 1);
        } else {
            MessageShow.initErrorDialog(getActivity(), "您还没有填写信息呢，请填写后在申请", 0);
        }
    }
}
