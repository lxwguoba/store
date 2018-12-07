package zerone.myapplication.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.adapter.ConsumersAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.ConsumersBean;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.interfaces.Consumers;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.DateTimeHelper;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;
import zerone.myapplication.utils.OpenTimeSelected;

/**
 * Created by Administrator on 2018-11-21.
 * 会员管理页面
 */

public class ConsumerstActivity extends BaseAppActivity implements Consumers {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    private SmartRefreshLayout srfresh;
    private RecyclerView recyclerView;
    private ConsumersAdapter recycleAdapter;
    private EditText editText;
    private TextView onStartTime;
    private TextView onEndTime;
    private String nickname;
    private String starttime;
    private String endTime;
    private ZLoadingDialog zLoadingDialog;
    private int current_page = -1;
    private int totalPage = 0;
    private List<ConsumersBean> mData;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    Toast.makeText(ConsumerstActivity.this, "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        String plistJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(plistJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            current_page = jsonObject.getJSONObject("data").getInt("current_page");
                            totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                            String ljson = jsonObject.getJSONObject("data").getString("data");
                            Gson gson = new Gson();
                            List<ConsumersBean> catlist = gson.fromJson(ljson, new TypeToken<List<ConsumersBean>>() {
                            }.getType());
                            if (catlist.size() > 0) {
                                mData.clear();
                                for (int i = 0; i < catlist.size(); i++) {
                                    mData.add(catlist.get(i));
                                }
                                recycleAdapter.notifyDataSetChanged();
                            }

                        } else if (status == 0) {
                            Toast.makeText(ConsumerstActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
                        if (srfresh.isShown()) {
                            srfresh.finishRefresh();
                        }
                    }
                    break;
                case 2:
                    try {
                        String plistJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(plistJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            current_page = jsonObject.getJSONObject("data").getInt("current_page");
                            totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                            String ljson = jsonObject.getJSONObject("data").getString("data");
                            Gson gson = new Gson();
                            List<ConsumersBean> catlist = gson.fromJson(ljson, new TypeToken<List<ConsumersBean>>() {
                            }.getType());
                            for (int i = 0; i < catlist.size(); i++) {
                                mData.add(catlist.get(i));
                            }
                            recycleAdapter.notifyDataSetChanged();
                        } else if (status == 0) {
                            Toast.makeText(ConsumerstActivity.this, "加载数据失败!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (srfresh.isShown()) {
                            srfresh.finishLoadMore();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membermanagement);
        initView();
        doListenner();
    }

    public void onBackActivity(View view) {
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        srfresh = findViewById(R.id.srl_fresh);

        editText = (EditText) findViewById(R.id.wx_nickname);
        onStartTime = (TextView) findViewById(R.id.onStartTime);
        onEndTime = (TextView) findViewById(R.id.onEndTime);
        //设置标题
        titleName.setText("会员管理");
        //数据模拟
        mData = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.viplistview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new ConsumersAdapter(mData, this);
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new NoLinesRecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void doListenner() {
        srfresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                //下拉刷新数据
                initData();
            }
        });

        srfresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                //刷新数据中
                moreLoadData();
            }
        });
    }

    /**
     * 启动查询消费者的dialog
     *
     * @param view
     */
    public void onSearchConsumer(View view) {
        nickname = editText.getText().toString().trim();
        starttime = onStartTime.getText().toString().trim();
        endTime = onEndTime.getText().toString().trim();
        setSearchProduct(nickname, starttime, endTime);

    }

    /**
     * 选择开始的时间
     *
     * @param view
     */
    public void onStartTime(View view) {
        OpenTimeSelected.initStartTimePicker(ConsumerstActivity.this, onStartTime, "开始日期");
    }

    /**
     * 选择结束时间
     *
     * @param view
     */
    public void onEndTime(View view) {
        OpenTimeSelected.initStartTimePicker(ConsumerstActivity.this, onEndTime, "结束日期");
    }

    /**
     * 获取数据
     */
    public void initData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "获取会员···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.USER_LIST, handler, 1);

    }

    /**
     * 加载更多
     */
    public void moreLoadData() {
        if (current_page <totalPage) {
            current_page++;
        }else {
            srfresh.setNoMoreData(true);
            srfresh.finishLoadMore();
            return;
        }

        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("name", "");
        parmar.put("page", current_page + "");

        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.USER_LIST, handler, 2);
    }

    /**
     * 搜索商品
     */
    public void setSearchProduct(String pname, String starttime, String endTime) {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "搜索数据中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("page", "1");
        if (nickname.length() > 0) {
            parmar.put("nickname", pname);
        }
        if (starttime.length() > 0) {
            parmar.put("time_st", starttime);
        }
        if (endTime.length() > 0) {
            parmar.put("time_nd", endTime);
        }
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.USER_LIST, handler, 1);
    }


}
