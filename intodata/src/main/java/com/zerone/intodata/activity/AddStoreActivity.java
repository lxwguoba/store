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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zerone.intodata.BaseAppActivity;
import com.zerone.intodata.R;
import com.zerone.intodata.config.IPConfig;
import com.zerone.intodata.domain.Area;
import com.zerone.intodata.domain.City;
import com.zerone.intodata.domain.HCatering;
import com.zerone.intodata.domain.Province;
import com.zerone.intodata.utils.AppSharePreferenceMgr;
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
 */

public class AddStoreActivity extends BaseAppActivity {
    @Bind(R.id.titleName)
    TextView titleName;
    @Bind(R.id.store_name)
    EditText storeName;
    @Bind(R.id.store_phone)
    EditText storePhone;
    @Bind(R.id.realame)
    EditText realame;
    @Bind(R.id.bank_open_name)
    EditText bankOpenName;
    @Bind(R.id.store_Catering)
    Spinner storeCatering;
    @Bind(R.id.province)
    Spinner province;
    @Bind(R.id.city)
    Spinner city;
    @Bind(R.id.county)
    Spinner county;
    @Bind(R.id.address_info)
    EditText addressInfo;
    @Bind(R.id.anquan_pwd)
    EditText anquanPwd;
    @Bind(R.id.store_discount)
    EditText storeDiscount;
    @Bind(R.id.store_password)
    EditText storePasswrod;
    @Bind(R.id.btn)
    Button btn;
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
    private List<HCatering> hclist = new ArrayList<HCatering>();
    private String[] spinnerItems;
    private ArrayAdapter<String> spinnerAdapter;
    //选中分类的position
    private int caterPosition;
    private int cid;
    private String c_name = "";
    private int aid;
    private String a_name = "";
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

                    break;
                case 1:
                    try {
                        String catJson = (String) msg.obj;
                        Log.i("URL", catJson);
                        JSONObject jsonObject = new JSONObject(catJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String data = jsonObject.getString("data");
                            Gson gson = new Gson();
                            List<HCatering> catlist = gson.fromJson(data, new TypeToken<List<HCatering>>() {
                            }.getType());
                            if (catlist.size() > 0) {
                                hclist.clear();
                                spinnerItems = new String[catlist.size()];
                                for (int i = 0; i < catlist.size(); i++) {
                                    hclist.add(catlist.get(i));
                                    spinnerItems[i] = catlist.get(i).getName();
                                }
                                initHCateringSpinner();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                            }
                        } else if (code == 0) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                    break;
                case 5:
                    try {
                        String mss = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(mss);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            MessageShow.initErrorDialog(AddStoreActivity.this, jsonObject.getString("msg"), 1);
                            AppSharePreferenceMgr.put(AddStoreActivity.this, "store_id", jsonObject.getString("store_id"));
                        } else {
                            MessageShow.initErrorDialog(AddStoreActivity.this, jsonObject.getString("msg"), 0);
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
    private String store_id;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstore);
        ButterKnife.bind(this);
        store_id = (String) AppSharePreferenceMgr.get(AddStoreActivity.this, "store_id", "");
        initGetDataHCateringSpinner();
        initGetProvinceData();
        initListenner();
        if (store_id.length()>0){
            MessageShow.initErrorDialog(AddStoreActivity.this,"您还有店铺没有提交审核，请及时提交",0);
        }
    }

    /**
     * 关闭页面
     * @param view
     */
    public  void onCloseActivity(View view){
        this.finish();
    }
    /**
     * 监听点击事件
     */
    private void initListenner() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddStore();
            }
        });
    }

    /**
     * 添加
     */
    private void gotoAddStore() {
        String s_name = storeName.getText().toString().trim();
        String s_phone = storePhone.getText().toString().trim();
        String s_password = storePasswrod.getText().toString().trim();
        String s_real_name = bankOpenName.getText().toString().trim();
        String s_discount = storeDiscount.getText().toString().trim();
        if (!(s_name.length() > 0)) {
            MessageShow.initErrorDialog(AddStoreActivity.this, "请输入商户名称", 0);
            return;
        }
        if (!(s_phone.length() > 0)) {
            MessageShow.initErrorDialog(AddStoreActivity.this, "请输入手机号码", 0);
            return;
        }
        if (!(s_password.length() > 0)) {
            MessageShow.initErrorDialog(AddStoreActivity.this, "请设置密码", 0);
            return;
        }
        if (!(s_real_name.length() > 0)) {
            MessageShow.initErrorDialog(AddStoreActivity.this, "请输入真实姓名", 0);
            return;
        }
        if (!(s_discount.length() > 0)) {
            MessageShow.initErrorDialog(AddStoreActivity.this, "请输入联盟折扣", 0);
            return;
        }


        int pid = optionsPro.get(provincePos).getId();
        String p_name = optionsPro.get(provincePos).getProvince_name();
        if (cityPos != -1) {
            cid = coptionsCity.get(cityPos).getId();
            c_name = coptionsCity.get(cityPos).getCity_name();
        }

        if (areaPos != -1) {
            aid = coptionsArea.get(areaPos).getId();
            a_name = coptionsArea.get(areaPos).getArea_name();
        }
        String tid = hclist.get(caterPosition).getId() + "";
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        String userName = UserConfig.getUserName(AddStoreActivity.this);
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        parmar.put("store_name", s_name);
        parmar.put("mobile", s_phone);
        parmar.put("realname", s_real_name);
        parmar.put("industry", tid);
        parmar.put("province_id", pid + "");
        parmar.put("province_name", p_name);
        parmar.put("city_id", cid + "");
        parmar.put("city_name", c_name);
        parmar.put("area_id", aid + "");
        parmar.put("area_name", a_name);
        parmar.put("pay_rate", s_discount);
        parmar.put("password", s_password);
        zLoadingDialog = LoadingUtils.openLoading(this, "提交资料中···");
        zLoadingDialog.show();
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.STORE_APPLY_CHECK, handler, 5);
    }

    private void initGetProvinceData() {
        Map<String, String> parmar = new HashMap<>();
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.PROVINCE, handler, 2);
    }

    /**
     * 获取行业分类
     */
    private void initGetDataHCateringSpinner() {
        String userName = UserConfig.getUserName(AddStoreActivity.this);
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.GET_STORE_INDUSTRY, handler, 1);
    }

    /**
     * 行业分类的获取
     */
    private void initHCateringSpinner() {
//        productlist.setDropDownWidth(600); //下拉宽度
        storeCatering.setDropDownHorizontalOffset(0); //下拉的横向偏移
        storeCatering.setDropDownVerticalOffset(100); //下拉的纵向偏移
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.item_select, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.item_drop);
        //这个在不同的Theme下，显示的效果是不同的
        storeCatering.setAdapter(spinnerAdapter);
        storeCatering.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                caterPosition = position;
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
                AddStoreActivity.this.provincePos = position;
                AddStoreActivity.this.cityPos = 0;
                if (countys.length > 0) {
                    AddStoreActivity.this.areaPos = 0;
                } else {
                    AddStoreActivity.this.areaPos = -1;
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
                AddStoreActivity.this.cityPos = position;
                if (countys.length > 0) {
                    AddStoreActivity.this.areaPos = 0;
                } else {
                    AddStoreActivity.this.areaPos = -1;
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
                AddStoreActivity.this.areaPos = position;
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
}
