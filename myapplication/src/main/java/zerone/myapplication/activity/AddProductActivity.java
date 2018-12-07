package zerone.myapplication.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.CategoryBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.UpLoadUtils;

import static com.alibaba.fastjson.serializer.CurrencyCodec.instance;

/**
 * Created by Administrator on 2018-11-16.
 * 添加商品分类
 */

public class AddProductActivity extends BaseAppActivity {
    private Spinner productlist;
    private Intent intent;
    private String doactionon;
    private ZLoadingDialog zLoadingDialog;
    private List<CategoryBean> catlist;
    private int spinPos = -1;
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
                    Toast.makeText(AddProductActivity.this, "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        String catJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catJson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            String list = jsonObject.getString("data");
                            Gson gson = new Gson();
                            catlist = gson.fromJson(list, new TypeToken<List<CategoryBean>>() {
                            }.getType());
                            String[] spinnerItems = new String[catlist.size()];
                            for (int i = 0; i < catlist.size(); i++) {
                                spinnerItems[i] = catlist.get(i).getName();
                            }
                            initSpinner(spinnerItems);
                        } else if (code == 0) {
                            Toast.makeText(AddProductActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
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
                        String pjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(pjson);
                        Log.i("URL",jsonObject.toString());
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            String goods_id = jsonObject.getJSONObject("data").getString("goods_id");
                            if (doactionon == null) {
                                Toast.makeText(AddProductActivity.this, "商品添加成功，请重试!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddProductActivity.this, AddPicturesActivity.class);
                                intent.putExtra("goods_id", goods_id);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AddProductActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                            }
                            AddProductActivity.this.finish();
                        } else if (code == 0) {
                            Toast.makeText(AddProductActivity.this, "保存商品信息失败，请重试!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
                    }


                    break;
            }
        }
    };
    private EditText productName;
    private EditText ptmoney;
    private EditText mdmoney;
    private EditText px;
    private EditText productinfo;
    private String goodsid;
    private TextView head;
    private Button btn;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        intent = getIntent();
        doactionon = intent.getStringExtra("doaction");
         goodsid = intent.getStringExtra("goodsid");
        initGetCatData();
        initView();
    }

    /**
     * Spinner的初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initSpinner(final String[] spinnerItems) {
//        productlist.setDropDownWidth(600); //下拉宽度
        productlist.setDropDownHorizontalOffset(0); //下拉的横向偏移
        productlist.setDropDownVerticalOffset(100); //下拉的纵向偏移
        //mSpinnerSimple.setBackgroundColor(AppUtil.getColor(instance,R.color.wx_bg_gray)); //下拉的背景色
        //spinner mode ： dropdown or dialog , just edit in layout xml
        //mSpinnerSimple.setPrompt("Spinner Title"); //弹出框标题，在dialog下有效
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.item_select, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.item_drop);
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        productlist.setAdapter(spinnerAdapter);
        productlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("URL", position + "");
                spinPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("添加商品");
        head = (TextView) findViewById(R.id.head);
        btn = (Button) findViewById(R.id.btn);
        if (doactionon!=null){
            head.setText("修改商品信息");
            btn.setText("保存修改");
        }
        productlist = (Spinner) findViewById(R.id.productlist);
        productName = (EditText) findViewById(R.id.productName);
        ptmoney = (EditText) findViewById(R.id.ptmoney);
        mdmoney = (EditText) findViewById(R.id.mdmoney);
        px = (EditText) findViewById(R.id.px);
        productinfo = (EditText) findViewById(R.id.productinfo);
    }

    /**
     * 返回上一级
     */
    public void onBackActivity(View view) {
        this.finish();
    }

    /**
     * @param view
     */
    public void onAddProduct(View view) {
        addOrChange();
    }

    /**
     * 获取分类
     */
    private void initGetCatData() {
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
        parmar.put("name", "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CATLIST, handler, 1);
    }

    public void addOrChange() {
        String name = productName.getText().toString().trim();
        String price = ptmoney.getText().toString().trim();
        String old_price = mdmoney.getText().toString().trim();
        String displayorder = px.getText().toString().trim();
        String description = productinfo.getText().toString().trim();
        if (!(name.length() > 0)) {
            Toast.makeText(AddProductActivity.this, "请输入商品名称!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(price.length() > 0)) {
            Toast.makeText(AddProductActivity.this, "请输入门店价!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(description.length() > 0)) {
            Toast.makeText(AddProductActivity.this, "请输入商品描述!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "添加商品中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        if (spinPos != -1) {
            parmar.put("category_id", catlist.get(spinPos).getId() + "");
        }
        parmar.put("name", name);
        //平台价
        parmar.put("price", price);
        if (old_price.length() > 0) {
            //门店价
            parmar.put("old_price", old_price);
        }
        if (displayorder.length() > 0) {
            parmar.put("displayorder", displayorder);
        }
        Log.i("URL","goodsid"+goodsid);
        if (goodsid != null && goodsid.length() > 0) {
            parmar.put("goods_id", goodsid);
        }
        parmar.put("description", description);
        if (doactionon==null){
            //添加
            NetUtils.netWorkByMethodPost(this, parmar, IPConfig.ADDPRODUCT, handler, 2);
        }else {
            //编辑
            NetUtils.netWorkByMethodPost(this, parmar, IPConfig.PRODUCT_EDIT, handler, 2);
        }
    }
}

