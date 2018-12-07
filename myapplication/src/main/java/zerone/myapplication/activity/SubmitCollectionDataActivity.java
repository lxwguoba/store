package zerone.myapplication.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.Area;
import zerone.myapplication.domain.CategoryBean;
import zerone.myapplication.domain.City;
import zerone.myapplication.domain.CollectionData;
import zerone.myapplication.domain.CollectionType;
import zerone.myapplication.domain.JsonBean;
import zerone.myapplication.domain.Province;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.GetJsonDataUtil;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;

/**
 * Created by 刘兴文 on 2018-12-01.
 * 提交收款资料
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SubmitCollectionDataActivity extends BaseAppActivity {
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    //省份列表
    private ArrayList<Province> optionsPro = new ArrayList<Province>();
    private ArrayList<City> optionsCity = new ArrayList<City>();
    private ArrayList<Area> optionsArea = new ArrayList<Area>();
    private ArrayList<City> coptionsCity = new ArrayList<City>();
    private ArrayList<Area> coptionsArea = new ArrayList<Area>();
    //省
    private Spinner provincebtn;
    //市
    private Spinner citybtn;
    //县
    private Spinner countybtn;
    //创建适配器三个
    private ArrayAdapter<String> p_adapter, c_adapter, county_adapter;
    //创建对应的资源数组,城市和景点采用二维数组
    private Spinner collectionType;
    private boolean isLoaded = false;
    private Thread thread;
    private String[] provinces;
    private String[] cityss;
    private String[] countys;
    //选择省份的位置
    private int provincePos = -1;
    //选择市的位置
    private int cityPos = -1;
    //选择区
    private int areaPos = -1;
    //选择了账户类型的position
    private int spPosition;
    private EditText bank_account_num;
    private EditText bank_reserved_phone;
    private EditText bank_user_name;
    private EditText bank_account;
    private EditText bank_open_name;
    private ZLoadingDialog zLoadingDialog;
    private Button btn;
    private List<CollectionType> tlist;
    private EditText aqpwd;
    private EditText address_info;
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
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
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
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
                            List<Area> proList = gson.fromJson(list, new TypeToken<List<Area>>() {
                            }.getType());
                            if (proList.size() > 0) {
                                for (int i = 0; i < proList.size(); i++) {
                                    optionsArea.add(proList.get(i));
                                }
                                //初始spinner里的
                                initData();
                                initGetData();
                            }
                        } else if (code == 0) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
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
                            CollectionData collectionData = gson.fromJson(list, CollectionData.class);
                            Log.i("URL", collectionData.toString());
                            setView(collectionData);

                        } else if (code == 0) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
                    }
                    break;

                case 5:

                    try {
                        String applyJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(applyJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            tosatMessage("资料提交成功，正在审核中");
                            SubmitCollectionDataActivity.this.finish();
                        } else if (status == 0) {
                            tosatMessage("资料提交失败，重新审核资料在提交");
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
        setContentView(R.layout.activity_submitcollectiondata);
        initView();
        initSpinner();
        initGetProvinceData();
    }

    /**
     * 获取省数据
     */
    private void initGetProvinceData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "数据处理中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.PROVINCE, handler, 1);
    }

    /**
     * 获取市数据
     */
    private void initGetCityData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "数据处理中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CITY, handler, 2);

    }

    /**
     * 获取区数据
     */
    private void initGetAreaData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "数据处理中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.AREA, handler, 3);
    }

    /**
     * 地区的初始化
     */
    public void initData() {
        provincebtn = (Spinner) findViewById(R.id.province);
        citybtn = (Spinner) findViewById(R.id.city);
        countybtn = (Spinner) findViewById(R.id.county);
        provinces = new String[optionsPro.size()];
        for (int i = 0; i < optionsPro.size(); i++) {
            provinces[i] = optionsPro.get(i).getProvince_name();
        }
        //构建适配器
        p_adapter = new ArrayAdapter<String>(this, R.layout.item_select, provinces);
        provincebtn.setAdapter(p_adapter);
        provincebtn.setSelection(0);
        c_adapter = new ArrayAdapter<String>(this, R.layout.item_select);
        county_adapter = new ArrayAdapter<String>(this, R.layout.item_select);
        //设置字体
        p_adapter.setDropDownViewResource(R.layout.item_drop);
        c_adapter.setDropDownViewResource(R.layout.item_drop);
        county_adapter.setDropDownViewResource(R.layout.item_drop);
        //将适配器和Spinner链接
        citybtn.setAdapter(c_adapter);
        countybtn.setAdapter(county_adapter);
        provincebtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Spinner默认选择重置为第一个
//                //清空适配器
                c_adapter.clear();
//               /添加数组资源,跟组对应的位置
                initCityAndArea(position);
                c_adapter.addAll(cityss);
                citybtn.setSelection(0);
                county_adapter.clear();
                initArea(0);
                county_adapter.addAll(countys);
                countybtn.setSelection(0);
                SubmitCollectionDataActivity.this.provincePos = position;
                SubmitCollectionDataActivity.this.cityPos = 0;
                if (countys.length > 0) {
                    SubmitCollectionDataActivity.this.areaPos = 0;
                } else {
                    SubmitCollectionDataActivity.this.areaPos = -1;
                }
                Log.i("URL", "pp" + position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citybtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                county_adapter.clear();
                initArea(position);
                county_adapter.addAll(countys);
                countybtn.setSelection(0);
                SubmitCollectionDataActivity.this.cityPos = position;
                if (countys.length > 0) {
                    SubmitCollectionDataActivity.this.areaPos = 0;
                } else {
                    SubmitCollectionDataActivity.this.areaPos = -1;
                }
                Log.i("URL", "cp" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        countybtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("URL", "ap" + position);
                SubmitCollectionDataActivity.this.areaPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
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


    private void initSpinner() {
        tlist = new ArrayList<CollectionType>();
        CollectionType co = new CollectionType("0", "请选择结算类型");
        CollectionType co1 = new CollectionType("1", "对公账户");
        CollectionType co2 = new CollectionType("2", "双账户法人入账");
        CollectionType co3 = new CollectionType("3", "双账户非法人入账");
        tlist.add(co);
        tlist.add(co1);
        tlist.add(co2);
        tlist.add(co3);
//        productlist.setDropDownWidth(600); //下拉宽度
        collectionType.setDropDownHorizontalOffset(0); //下拉的横向偏移
        collectionType.setDropDownVerticalOffset(100); //下拉的纵向偏移
        final String[] spinnerItems = {"请选择结算类型", "对公账户", "双账户法人入账", "双账户非法人入账"};
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
                Log.i("URL", "tpos" + position);
                spPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("收款资料");
        aqpwd = (EditText) findViewById(R.id.anquan_pwd);
        address_info = (EditText) findViewById(R.id.address_info);
        btn = (Button) findViewById(R.id.btn);
        //开户行编号
        bank_account_num = (EditText) findViewById(R.id.bank_account_num);
        //预留手机号码
        bank_reserved_phone = (EditText) findViewById(R.id.bank_reserved_phone);
        //开户人姓名
        bank_user_name = (EditText) findViewById(R.id.bank_user_name);
        //银行卡账号
        bank_account = (EditText) findViewById(R.id.bank_account);
        //开户行名称
        bank_open_name = (EditText) findViewById(R.id.bank_open_name);
        collectionType = (Spinner) findViewById(R.id.collectionType);
    }

    /**
     * 把资料上传
     *
     * @param view
     */
    public void onSaveInfo(View view) {
        //开户行编号
        String account_num = bank_account_num.getText().toString().trim();
        //预留手机号码
        String phone = bank_reserved_phone.getText().toString().trim();
        //开户人姓名
        String username = bank_user_name.getText().toString().trim();
        //银行卡账号
        String account = bank_account.getText().toString().trim();
        //开户行名称
        String open_name = bank_open_name.getText().toString().trim();

        String pwd = aqpwd.getText().toString().trim();
        String address = address_info.getText().toString().trim();

        if (!(account_num.length() > 0)) {
            tosatMessage("开户行编号不能为空");
            return;
        }
        if (!(phone.length() > 0)) {
            tosatMessage("预留手机号码不能为空");
            return;
        }


        if (!(username.length() > 0)) {
            tosatMessage("开户人姓名不能为空");
            return;
        }
        if (!(account.length() > 0)) {
            tosatMessage("银行卡账号不能为空");
            return;
        }
        if (!(open_name.length() > 0)) {
            tosatMessage("开户行名称不能为空");
            return;
        }
        if (!(address.length() > 0)) {
            tosatMessage("详细地址不能为空");
            return;
        }
        if (!(pwd.length() > 0)) {
            tosatMessage("请输入安全密码");
            return;
        }
        int pid = optionsPro.get(provincePos).getId();
        if (cityPos != -1) {
            int cid = coptionsCity.get(cityPos).getId();
        }

        if (areaPos != -1) {
            int aid = coptionsArea.get(areaPos).getId();
        }
        String tid = tlist.get(spPosition).getValue();

        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "资料提交中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");

        parmar.put("sub_type", tid + "");

        parmar.put("sub_name", username);

        parmar.put("sub_idcard", account);

        parmar.put("sub_address", address);

        parmar.put("sub_bankname", open_name);
        parmar.put("sub_bankider", account_num);

        parmar.put("bank_mobile", phone);

        parmar.put("bank_province_id", optionsPro.get(provincePos).getId() + "");

        parmar.put("bank_city_id", coptionsCity.get(cityPos).getId() + "");

        if (areaPos > -1) {
            parmar.put("bank_area_id", coptionsArea.get(areaPos).getId() + "");
        } else {
            parmar.put("bank_area_id", "0");
        }
        parmar.put("safe_password", pwd);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.APPLY_DATA_CHECK, handler, 5);
    }


    private void initGetData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "获取数据中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.APPLY_DATA, handler, 4);
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setView(CollectionData data) {

        if (data.getIs_apply() == 0) {
            btn.setText("提交申请");
        } else if (data.getIs_apply() == 1) {
            btn.setText("申请中");
            btn.setEnabled(false);
            btn.setBackgroundResource(R.drawable.btn_normal_gree);
        } else if (data.getIs_apply() == 2) {
            btn.setText("完成");
            btn.setEnabled(false);
            btn.setBackgroundResource(R.drawable.btn_normal_gree);
        } else if (data.getIs_apply() == 3) {
            btn.setText("重新提交");
        }
        //开户行编号
        bank_account_num.setText(data.getSub_bankider());
        //预留手机号码
        bank_reserved_phone.setText(data.getBank_mobile());
        //开户人姓名
        bank_user_name.setText(data.getSub_name());
        //银行卡账号
        bank_account.setText(data.getSub_idcard());
        //开户行名称
        bank_open_name.setText(data.getSub_bankname());
        //结算类型
        collectionType.setSelection(data.getSub_type());
        //详细地址
        address_info.setText(data.getSub_address());
        spPosition = data.getSub_type();
        for (int i = 0; i < optionsPro.size(); i++) {
            if (optionsPro.get(i).getId() == data.getBank_province_id()) {
                SubmitCollectionDataActivity.this.cityPos = i;
                c_adapter.clear();
//               /添加数组资源,跟组对应的位置
                initCityAndArea(i);
                c_adapter.addAll(cityss);
                provincebtn.setSelection(i);
                for (int j = 0; j < coptionsCity.size(); j++) {
                    if (coptionsCity.get(i).getId() == data.getBank_city_id()) {
                        SubmitCollectionDataActivity.this.cityPos = j;
                        citybtn.setSelection(j);
                        county_adapter.clear();
                        initArea(j);
                        county_adapter.addAll(countys);
                        for (int l = 0; l < coptionsArea.size(); l++) {
                            if (coptionsArea.get(l).getCity_id() == data.getBank_area_id()) {
                                SubmitCollectionDataActivity.this.areaPos = l;
                                countybtn.setSelection(l);
                                return;
                            }
                        }
                        return;
                    }
                }
                return;
            }
        }
    }
}
