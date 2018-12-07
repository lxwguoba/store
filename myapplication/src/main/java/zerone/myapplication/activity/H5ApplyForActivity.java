package zerone.myapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.PostersCateBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;

/**
 * Created by 刘兴文 on 2018-11-26.
 */

public class H5ApplyForActivity extends BaseAppActivity {

    private Spinner spiner;
    private LinearLayout pay_haibao;
    private LinearLayout freelayout;
    private ImageView imageView;
    private Object postersBean;
    private List<PostersCateBean> posters;
    private ZLoadingDialog zLoadingDialog;
    private int posterPos = -1;
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
                    int position = (int) msg.obj;
                    if (position == 0) {
                        pay_haibao.setVisibility(View.GONE);
                        freelayout.setVisibility(View.GONE);
                    } else if (position == 1) {
                        pay_haibao.setVisibility(View.VISIBLE);
                        freelayout.setVisibility(View.GONE);
                        Glide.with(H5ApplyForActivity.this).load("https://ctwxl.com/test/images/payqrcode.png").into(imageView);
                    } else if (position == 2) {
                        pay_haibao.setVisibility(View.GONE);
                        freelayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    try {
                        String imgs = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(imgs);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String ljson = jsonObject.getString("data");
                            Gson gson = new Gson();
                            posters = gson.fromJson(ljson, new TypeToken<List<PostersCateBean>>() {
                            }.getType());
                            if (posters.size() > 0) {
                                String[] post = new String[posters.size() + 1];
                                post[0] = "请选择";
                                for (int i = 0; i < posters.size(); i++) {
                                    post[i + 1] = posters.get(i).getName();
                                }
                                initSpinner(post);
                            }
                        } else if (status == 0) {
                            Toast.makeText(H5ApplyForActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
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
                        String applyforJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(applyforJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            tosatMessage(jsonObject.getString("msg"));
                            H5ApplyForActivity.this.finish();
                        } else if (status == 0) {
                            tosatMessage(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
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
    private EditText remark;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5applyfor);
        initView();
        getPostersBean();


    }

    /**
     * 返回上一级
     */
    public void onBackActivity(View view) {
        this.finish();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("海报申请");
        remark = (EditText) findViewById(R.id.remark);
        pay_haibao = (LinearLayout) findViewById(R.id.pay_haibao);
        freelayout = (LinearLayout) findViewById(R.id.free);
        imageView = (ImageView) findViewById(R.id.payqrcode);
    }


    /**
     * Spinner的初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initSpinner(String[] spinnerItems) {
//        spiner.setDropDownWidth(600); //下拉宽度
        spiner = (Spinner) findViewById(R.id.productlist);
        spiner.setDropDownHorizontalOffset(0); //下拉的横向偏移
        spiner.setDropDownVerticalOffset(100); //下拉的纵向偏移
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
        spiner.setAdapter(spinnerAdapter);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("URL", position + "");
                posterPos = position;
                Message message = new Message();
                message.obj = position;
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getPostersBean() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "获取海报分类");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.POSTER, handler, 2);
    }

    /**
     * 申请海报
     */
    public void onApplyFor(View view) {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "海报申请中··");
        }
        zLoadingDialog.show();
        String remarks = remark.getText().toString().trim();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        if (posterPos != -1) {
            parmar.put("product_id", posters.get(posterPos - 1).getId() + "");
        }
        if (remarks.length() > 0) {
            parmar.put("remark", remarks);
        }
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.POSTER_ADD, handler, 3);
    }
}
