package zerone.myapplication.activity;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.CouponsBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.DateTimeHelper;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.OpenTimeSelected;

/**
 * Created by 刘兴文 on 2018-11-30.
 */

public class AddCouponsActivity extends BaseAppActivity {

    private Spinner couSpinner;
    private TextView typeTitle;
    private EditText money;
    private EditText hmoney;
    private TextView onStartTime;
    private TextView onEndTime;
    private TimePickerView mStartDatePickerView;
    private EditText coupons_name;
    private EditText money1;
    private EditText xfjmoney;
    private EditText hmoney1;
    private EditText couponscount;
    private EditText safePwd;
    private ZLoadingDialog zLoadingDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    int position = (int) msg.obj;
                    if (position == 0) {
                        typeTitle.setText("满立减");
                        money.setHint("消费金额");
                        hmoney.setHint("优惠金额");
                    } else if (position == 1) {
                        typeTitle.setText("折扣比率");
                        money.setHint("消费金额");
                        hmoney.setHint("折扣比率");
                    }
                    break;
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    Toast.makeText(AddCouponsActivity.this, "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        String addJson = (String) msg.obj;
                        Log.i("URL", addJson);
                        JSONObject jsonObject = new JSONObject(addJson);
                        Log.i("URL", jsonObject.toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            tosatMessage("添加成功!");
                            AddCouponsActivity.this.finish();
                        } else {
                            tosatMessage("添加失败，请重试!");
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
    private Intent intent;
    private CouponsBean couponsBean;
    private Button comfig_btn;
    private TextView head;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        intent = getIntent();
        couponsBean = (CouponsBean) intent.getSerializableExtra("couponsBean");
        initView();
        initSpinner();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        comfig_btn = (Button) findViewById(R.id.comfig_btn);
        typeTitle = (TextView) findViewById(R.id.typeTitle);
        head = findViewById(R.id.head);
        money = (EditText) findViewById(R.id.money);
        hmoney = (EditText) findViewById(R.id.hmoney);
        onStartTime = (TextView) findViewById(R.id.onStartTime);
        onEndTime = (TextView) findViewById(R.id.onEndTime);
        coupons_name = (EditText) findViewById(R.id.coupons_name);
        couponscount = (EditText) findViewById(R.id.couponscount);
        safePwd = (EditText) findViewById(R.id.safePwd);

        if (couponsBean != null) {
            titleName.setText("编辑优惠卷");
            comfig_btn.setText("编辑");
            head.setText("编辑优惠卷");
            money.setText(couponsBean.getFull_price());
            hmoney.setText(couponsBean.getRatio() + "");
            coupons_name.setText(couponsBean.getName());
            couponscount.setText(couponsBean.getNum() + "");
        } else {
            titleName.setText("新增优惠卷信息");
        }
    }

    /**
     * Spinner的初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initSpinner() {
        couSpinner = (Spinner) findViewById(R.id.couponstype);
//        productlist.setDropDownWidth(600); //下拉宽度
        couSpinner.setDropDownHorizontalOffset(0); //下拉的横向偏移
        couSpinner.setDropDownVerticalOffset(100); //下拉的纵向偏移
        //mSpinnerSimple.setBackgroundColor(AppUtil.getColor(instance,R.color.wx_bg_gray)); //下拉的背景色
        //spinner mode ： dropdown or dialog , just edit in layout xml
        //mSpinnerSimple.setPrompt("Spinner Title"); //弹出框标题，在dialog下有效

        final String[] spinnerItems = {"满减价", "折扣价"};
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.item_select, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.item_drop);
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        couSpinner.setAdapter(spinnerAdapter);
        couSpinner.setSelection(1);
        couSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("URL", position + "");
                Message message = new Message();
                message.what = -1;
                message.obj = position;
                handler.sendMessage(message);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 选择开始的时间
     *
     * @param view
     */
    public void onStartTime(View view) {
        OpenTimeSelected.initStartTimePicker(this, onStartTime, "开始日期");
    }

    /**
     * 选择结束时间
     *
     * @param view
     */
    public void onEndTime(View view) {
        OpenTimeSelected.initStartTimePicker(this, onEndTime, "结束日期");
    }

    /**
     * 添加优惠卷或者是修改
     *
     * @param view
     */
    public void onAddCoupons(View view) {
        String xfmoney = money.getText().toString().trim();
        String lmhymoney = hmoney.getText().toString().trim();
        String starttime = onStartTime.getText().toString().trim();
        String endtime = onEndTime.getText().toString().trim();
        String couname = coupons_name.getText().toString().trim();
        String coucount = couponscount.getText().toString().trim();
        if (!(xfmoney != null && xfmoney.length() > 0)) {
            tosatMessage("消费金额不能为空");
            return;
        }
        if (!(lmhymoney != null && lmhymoney.length() > 0)) {
            tosatMessage("优惠金额不能为空");
            return;
        }
        if (!(starttime != null && starttime.length() > 0)) {
            tosatMessage("有效期开始时间不能为空");
            return;
        }
        if (!(endtime != null && endtime.length() > 0)) {
            tosatMessage("有效期结束时间不能为空");
            return;
        }
        if (!(couname != null && couname.length() > 0)) {
            tosatMessage("优惠卷名称不能为空");
            return;
        }
        if (!(coucount != null && coucount.length() > 0)) {
            tosatMessage("优惠卷数量不能为空");
            return;
        }


        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("name", couname);
        parmar.put("full_price", xfmoney + "");
        parmar.put("ratio", lmhymoney + "");
        parmar.put("num", coucount + "");
        parmar.put("start_time", starttime + "");
        parmar.put("end_time", endtime + "");

        if (couponsBean != null) {
            if (zLoadingDialog == null) {
                zLoadingDialog = LoadingUtils.openLoading(this, "修改优惠卷");
            }
            zLoadingDialog.show();
            parmar.put("id", couponsBean.getId() + "");
            NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CUOPON_ADD, handler, 1);
        } else {
            if (zLoadingDialog == null) {
                zLoadingDialog = LoadingUtils.openLoading(this, "添加优惠卷");
            }
            zLoadingDialog.show();
            NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CUOPON_EDIT, handler, 1);
        }


    }
}
