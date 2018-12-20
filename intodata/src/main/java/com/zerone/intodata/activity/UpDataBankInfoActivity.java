package com.zerone.intodata.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zerone.intodata.BaseAppActivity;
import com.zerone.intodata.R;
import com.zerone.intodata.config.IPConfig;
import com.zerone.intodata.domain.Area;
import com.zerone.intodata.domain.BankInfo;
import com.zerone.intodata.domain.City;
import com.zerone.intodata.domain.CollectionType;
import com.zerone.intodata.domain.Province;
import com.zerone.intodata.utils.CreateTokenUtils;
import com.zerone.intodata.utils.LoadingUtils;
import com.zerone.intodata.utils.MessageShow;
import com.zerone.intodata.utils.NetUtils;
import com.zerone.intodata.utils.UserConfig;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘兴文 on 2018-12-17.
 * 修改银行卡信息
 */

public class UpDataBankInfoActivity extends BaseAppActivity {
    @Bind(R.id.collectionType)
    Spinner collectionType;
    @Bind(R.id.province)
    Spinner province;
    @Bind(R.id.city)
    Spinner city;
    @Bind(R.id.county)
    Spinner county;
    @Bind(R.id.btn)
    Button btn;

    @Bind(R.id.linkman_name)
    EditText linkmanName;
    @Bind(R.id.linkman_phone)
    EditText linkmanPhone;
    @Bind(R.id.linkman_email)
    EditText linkmanEmail;
    @Bind(R.id.bank_user_name)
    EditText bankUserName;
    @Bind(R.id.bank_account_name)
    EditText bankAccountName;
    @Bind(R.id.bank_account)
    EditText bankAccount;
    @Bind(R.id.realame)
    EditText realame;
    @Bind(R.id.bank_open_name)
    EditText bankOpenName;
    @Bind(R.id.bank_reserved_phone)
    EditText bankReservedPhone;
    private List<CollectionType> tlist;

    //省份列表
    private ArrayList<Province> optionsPro = new ArrayList<Province>();
    private ArrayList<City> optionsCity = new ArrayList<City>();
    private ArrayList<Area> optionsArea = new ArrayList<Area>();
    private ArrayList<City> coptionsCity = new ArrayList<City>();
    private ArrayList<Area> coptionsArea = new ArrayList<Area>();
    //创建适配器三个
    private ArrayAdapter<String> p_adapter, c_adapter, county_adapter;
    private String[] provinces;
    private String[] cityss;
    private String[] countys;
    //选择省份的位置
    private int provincePos = -1;
    //选择市的位置
    private int cityPos = -1;
    //选择区
    private int areaPos = -1;
    private String store_id;
    private int spPosition;
    private ZLoadingDialog zLoadingDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:


                    break;
                case 1:


                    break;
                case 2:
                    try {
                        String catJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catJson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            String list = jsonObject.getString("data");
                            Gson gson = new Gson();
                            List<Province> proList = gson.fromJson(list, new TypeToken<List<Province>>() {
                            }.getType());
                            if (proList.size() > 0) {
                                for (int i = 0; i < proList.size(); i++) {
                                    optionsPro.add(proList.get(i));
                                }
                                //调用市的接口
                                initGetCityData();
                            }
                        } else if (code == 0) {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                    break;
                case 3:
                    try {
                        String catJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catJson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            String list = jsonObject.getString("data");
                            Gson gson = new Gson();
                            List<City> proList = gson.fromJson(list, new TypeToken<List<City>>() {
                            }.getType());
                            if (proList.size() > 0) {
                                for (int i = 0; i < proList.size(); i++) {
                                    optionsCity.add(proList.get(i));
                                }
                                //调用市的接口
                                initGetAreaData();
                            }
                        } else if (code == 0) {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                    break;
                case 4:
                    try {
                        String catJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catJson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            String list = jsonObject.getString("data");
                            Gson gson = new Gson();
                            List<Area> proList = gson.fromJson(list, new TypeToken<List<Area>>() {
                            }.getType());
                            if (proList.size() > 0) {
                                for (int i = 0; i < proList.size(); i++) {
                                    optionsArea.add(proList.get(i));
                                }
                                //初始spinner里的
                                initData();
                                getBankInfo();
                            }
                        } else if (code == 0) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                    break;
                case 5:
                    String bankinfo = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(bankinfo);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String data = jsonObject.getString("data");
                            if ("false".equals(data)) {
                                MessageShow.initErrorDialog(UpDataBankInfoActivity.this, "您还没有填收款信息，请填写提交", 0);
                            } else {
                                Gson gson = new Gson();
                                BankInfo bankInfo = gson.fromJson(data, BankInfo.class);
                                todoSetViewData(bankInfo);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        String mess = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(mess);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            MessageShow.initErrorDialog(UpDataBankInfoActivity.this, jsonObject.getString("msg"), 1);
                        } else {
                            MessageShow.initErrorDialog(UpDataBankInfoActivity.this, jsonObject.getString("msg"), 0);
                        }

                    } catch (Exception e) {
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
    private int cid = 0;
    private int aid = 0;

    /**
     * 给页面设置值
     *
     * @param bankInfo
     */
    private void todoSetViewData(BankInfo bankInfo) {
        linkmanName.setText(bankInfo.getContact_name());
        linkmanPhone.setText(bankInfo.getContact_mobile());
        linkmanEmail.setText(bankInfo.getContact_email());
        bankUserName.setText(bankInfo.getSub_name());
        bankAccountName.setText(bankInfo.getSub_bankider());
        bankAccount.setText(bankInfo.getSub_idcard());
        bankOpenName.setText(bankInfo.getSub_bankname());

        for (int i = 0; i < tlist.size(); i++) {
            if (tlist.get(i).getValue().equals(bankInfo.getSub_type() + "")) {
                collectionType.setSelection(i);
                spPosition = i;
                break;
            }
        }

        for (int i = 0; i < optionsPro.size(); i++) {
            if (bankInfo.getBank_province_id() == optionsPro.get(i).getId()) {
                provincePos = i;
                province.setSelection(i);
                c_adapter.clear();
//               /添加数组资源,跟组对应的位置
                initCityAndArea(i);
                c_adapter.addAll(cityss);
                break;
            }
        }
        for (int j = 0; j < coptionsCity.size(); j++) {
            if (bankInfo.getBank_city_id() == coptionsCity.get(j).getId()) {
                cityPos = j;
                city.setSelection(j);
                initArea(j);
                break;
            }
        }
        for (int l = 0; l < coptionsArea.size(); l++) {
            if (bankInfo.getBank_area_id() == coptionsArea.get(l).getId()) {
                areaPos = l;
                county.setSelection(l);
                break;
            }
        }
    }

    /**
     * 获取收款资料信息
     */
    private void getBankInfo() {
        String userName = UserConfig.getUserName(this);
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        parmar.put("store_id", store_id);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.GET_STORE_DATACULATION, handler, 5);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_bankinfo);
        ButterKnife.bind(this);
        initSpinner();
        initGetProvinceData();
        store_id = getIntent().getStringExtra("store_id");
    }

    /**
     * 关闭页面
     *
     * @param view
     */
    public void onCloseActivity(View view) {
        this.finish();
    }

    private void initSpinner() {
        tlist = new ArrayList<CollectionType>();
        CollectionType co1 = new CollectionType("1", "对公账户");
        CollectionType co2 = new CollectionType("2", "对私法人入账");
        CollectionType co3 = new CollectionType("3", "对私非法人入账");
        tlist.add(co1);
        tlist.add(co2);
        tlist.add(co3);
//        productlist.setDropDownWidth(600); //下拉宽度
        collectionType.setDropDownHorizontalOffset(0); //下拉的横向偏移
        collectionType.setDropDownVerticalOffset(100); //下拉的纵向偏移
        final String[] spinnerItems = {"对公账户", "对私法人入账", "对私非法人入账"};
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.item_select, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.item_drop);
        //这个在不同的Theme下，显示的效果是不同的
        collectionType.setAdapter(spinnerAdapter);
        collectionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 地区的初始化
     */
    public void initData() {
        provinces = new String[optionsPro.size()];
        for (int i = 0; i < optionsPro.size(); i++) {
            provinces[i] = optionsPro.get(i).getProvince_name();
        }
        //构建适配器
        p_adapter = new ArrayAdapter<String>(this, R.layout.item_select, provinces);
        province.setAdapter(p_adapter);
        province.setSelection(0);
        c_adapter = new ArrayAdapter<String>(this, R.layout.item_select);
        county_adapter = new ArrayAdapter<String>(this, R.layout.item_select);
        //设置字体
        p_adapter.setDropDownViewResource(R.layout.item_drop);
        c_adapter.setDropDownViewResource(R.layout.item_drop);
        county_adapter.setDropDownViewResource(R.layout.item_drop);
        //将适配器和Spinner链接
        city.setAdapter(c_adapter);
        county.setAdapter(county_adapter);
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Spinner默认选择重置为第一个
//                //清空适配器
                c_adapter.clear();
//               /添加数组资源,跟组对应的位置
                initCityAndArea(position);
                c_adapter.addAll(cityss);
                city.setSelection(0);
                county_adapter.clear();
                initArea(0);
                county_adapter.addAll(countys);
                county.setSelection(0);
                UpDataBankInfoActivity.this.provincePos = position;
                UpDataBankInfoActivity.this.cityPos = 0;
                if (countys.length > 0) {
                    UpDataBankInfoActivity.this.areaPos = 0;
                } else {
                    UpDataBankInfoActivity.this.areaPos = -1;
                }
                Log.i("URL", "pp" + position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                county_adapter.clear();
                initArea(position);
                county_adapter.addAll(countys);
                county.setSelection(0);
                UpDataBankInfoActivity.this.cityPos = position;
                if (countys.length > 0) {
                    UpDataBankInfoActivity.this.areaPos = 0;
                } else {
                    UpDataBankInfoActivity.this.areaPos = -1;
                }
                Log.i("URL", "cp" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        county.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("URL", "ap" + position);
                UpDataBankInfoActivity.this.areaPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
    }

    /**
     * @param position
     */
    public void initArea(int position) {
        coptionsArea.clear();
        for (int i = 0; i < optionsArea.size(); i++) {
            if (optionsArea.get(i).getCity_id() == coptionsCity.get(position).getId()) {
                coptionsArea.add(optionsArea.get(i));
            }
        }
        if (coptionsCity.size() > 0) {
            countys = new String[coptionsArea.size()];
            for (int i = 0; i < coptionsArea.size(); i++) {
                countys[i] = coptionsArea.get(i).getArea_name();
            }
        }
    }

    public void initCityAndArea(int position) {
        coptionsCity.clear();
        for (int i = 0; i < optionsCity.size(); i++) {
            if (optionsCity.get(i).getProvince_id() == optionsPro.get(position).getId()) {
                coptionsCity.add(optionsCity.get(i));
            }
        }
        cityss = new String[coptionsCity.size()];
        for (int i = 0; i < coptionsCity.size(); i++) {
            cityss[i] = coptionsCity.get(i).getCity_name();
        }
    }

    private void initGetProvinceData() {
        Map<String, String> parmar = new HashMap<>();
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.PROVINCE, handler, 2);
    }

    /**
     * 获取区县
     */
    private void initGetAreaData() {
        Map<String, String> parmar = new HashMap<>();
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.AREA, handler, 4);
    }

    /**
     * 获取市
     */
    private void initGetCityData() {
        Map<String, String> parmar = new HashMap<>();
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CITY, handler, 3);
    }

    /**
     * 提交信息
     * @param view
     */
    public void onSaveInfo(View view) {
        gotoSaveInfo();
    }

    /**
     * 保存信息
     */
    private void gotoSaveInfo() {
        String linkman_name = linkmanName.getText().toString().trim();
        String linkman_phone = linkmanPhone.getText().toString().trim();
        String linkman_email = linkmanEmail.getText().toString().trim();
        String bank_UserName = bankUserName.getText().toString().trim();
        String bank_AccountName = bankAccountName.getText().toString().trim();
        String bank_Account = bankAccount.getText().toString().trim();
        String bank_OpenName = bankOpenName.getText().toString().trim();

        if (!(linkman_name.length() > 0)) {
            MessageShow.initErrorDialog(UpDataBankInfoActivity.this, "请输入联系人姓名", 0);
            return;
        }
        if (!(linkman_phone.length() > 0)) {
            MessageShow.initErrorDialog(UpDataBankInfoActivity.this, "请输入联系人电话", 0);
            return;
        }
        if (!(linkman_email.length() > 0)) {
            MessageShow.initErrorDialog(UpDataBankInfoActivity.this, "请输入联系人邮箱", 0);
            return;
        }
        if (!(bank_UserName.length() > 0)) {
            MessageShow.initErrorDialog(UpDataBankInfoActivity.this, "请输入开户人姓名", 0);
            return;
        }
        if (!(bank_AccountName.length() > 0)) {
            MessageShow.initErrorDialog(UpDataBankInfoActivity.this, "请输入开户人身份证", 0);
            return;
        }

        if (!(bank_Account.length() > 0)) {
            MessageShow.initErrorDialog(UpDataBankInfoActivity.this, "请输入银行卡账号", 0);
            return;
        }
        if (!(bank_OpenName.length() > 0)) {
            MessageShow.initErrorDialog(UpDataBankInfoActivity.this, "请输入开户行全称", 0);
            return;
        }

        int pid = optionsPro.get(provincePos).getId();
        String p_name = optionsPro.get(provincePos).getProvince_name();
        if (cityPos != -1) {
            cid = coptionsCity.get(cityPos).getId();
        }

        if (areaPos != -1) {
            aid = coptionsArea.get(areaPos).getId();
        }

        String sub_type = tlist.get(spPosition).getValue();
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        String userName = UserConfig.getUserName(UpDataBankInfoActivity.this);
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        parmar.put("store_id", store_id);
        parmar.put("contact_name", linkman_name);
        parmar.put("contact_email", linkman_email);
        parmar.put("contact_mobile", linkman_phone);
        parmar.put("sub_type", sub_type);
        parmar.put("sub_name", bank_UserName);
        parmar.put("sub_bankider", bank_AccountName);
        parmar.put("sub_idcard", bank_Account);
        parmar.put("province_id", pid + "");
        parmar.put("city_id", cid + "");
        parmar.put("area_id", aid + "");
        parmar.put("sub_bankname", bank_OpenName);
        zLoadingDialog = LoadingUtils.openLoading(this, "提交资料中···");
        zLoadingDialog.show();
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.APPLY_DATA_CHECK, handler, 6);
    }
}
