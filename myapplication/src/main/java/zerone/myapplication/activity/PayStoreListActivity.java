package zerone.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.adapter.PackTransactionsListItemAdapter;
import zerone.myapplication.adapter.TransactionsListItemAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.PayInfoBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;
import zerone.myapplication.utils.OpenTimeSelected;

/**
 * Created by Administrator on 2018-11-26.
 * 财务管理-店铺流水
 */

public class PayStoreListActivity extends BaseAppActivity {
    private TextView onStartTime;
    private TextView onEndTime;
    private ZLoadingDialog zLoadingDialog;
    private SmartRefreshLayout srfresh;
    private RecyclerView recyclerView;
    private int current_page = -1;
    private int totalPage = 0;
    private PackTransactionsListItemAdapter recycleAdapter;
    /**
     *
     */
    private List<PayInfoBean> mData;
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
                    String ms = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(ms);
                        Log.i("URL",jsonObject.toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String li = jsonObject.getJSONObject("data").getString("data");
                            current_page = jsonObject.getJSONObject("data").getInt("current_page");
                            totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                            Gson gson = new Gson();
                            List<PayInfoBean> ps = gson.fromJson(li, new TypeToken<List<PayInfoBean>>() {
                            }.getType());
                            mData.clear();
                            for (int i = 0; i < ps.size(); i++) {
                                mData.add(ps.get(i));
                            }
                            recycleAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
                        if (srfresh.isShown()){
                            srfresh.finishRefresh();
                        }
                    }
                    break;
                case 2:
                    String m = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(m);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String li = jsonObject.getJSONObject("data").getString("data");
                            current_page = jsonObject.getJSONObject("data").getInt("current_page");
                            totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                            Gson gson = new Gson();
                            List<PayInfoBean> ps = gson.fromJson(li, new TypeToken<List<PayInfoBean>>() {
                            }.getType());
                            for (int i = 0; i < ps.size(); i++) {
                                mData.add(ps.get(i));
                            }
                            recycleAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (srfresh.isShown()){
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
        setContentView(R.layout.activity_paystorelist);
        initView();
    }

    /**
     * 返回上一级
     */
    public void onBackActivity(View view) {
        this.finish();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("店铺流水");
        onStartTime = findViewById(R.id.onStartTime);
        onEndTime = findViewById(R.id.onEndTime);
        mData = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewInfo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new PackTransactionsListItemAdapter(mData, this);
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new NoLinesRecyclerViewDivider(this, 0));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        srfresh = findViewById(R.id.srl_fresh);
        srfresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                //下拉刷新数据
                refreshData();
            }
        });

        srfresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                //刷新数据中
                moreLoadData();
            }
        });
        initGetData();
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




    private void refreshData() {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("page", "1");
        parmar.put("start_time", "");
        parmar.put("end_time", "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.STREAM_LOG, handler, 1);

    }

    private void moreLoadData() {
        if (current_page < totalPage) {
            current_page++;
        } else {
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
        parmar.put("start_time", "");
        parmar.put("end_time", "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.STREAM_LOG, handler, 2);

    }

    private void initGetData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "店铺流水数据获取中···");
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
        parmar.put("start_time", "");
        parmar.put("end_time", "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.STREAM_LOG, handler, 1);
    }

    /**
     * 查询
     *
     * @param view
     */
    public void onSearchListenner(View view) {
        String starttime = onStartTime.getText().toString().trim();
        String endTime = onEndTime.getText().toString().trim();
        if (!(starttime.length() > 0)) {
            tosatMessage("请选择开始时间");
            return;
        }
        if (!(endTime.length() > 0)) {
            tosatMessage("请选择结束时间");
            return;
        }
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "搜索中···");
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
        parmar.put("start_time", starttime);
        parmar.put("end_time", endTime);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.STREAM_LOG, handler, 1);
    }
}
