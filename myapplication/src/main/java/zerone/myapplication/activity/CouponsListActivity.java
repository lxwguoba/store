package zerone.myapplication.activity;

import android.content.Intent;
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
import zerone.myapplication.adapter.CouponsAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.CouponsBean;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;
import zerone.myapplication.utils.OpenTimeSelected;

/**
 * Created by 刘兴文 on 2018-11-30.
 */

public class CouponsListActivity extends BaseAppActivity {
    private SmartRefreshLayout srfresh;
    private RecyclerView recyclerView;
    private CouponsAdapter recycleAdapter;
    private TextView onStartTime;
    private TextView onEndTime;
    private ZLoadingDialog zLoadingDialog;
    private int current_page = -1;
    private int totalPage = 0;
    private List<CouponsBean> mData;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -2:
                    //编辑按钮的点击事件
                    int position = (int) msg.obj;
                    CouponsBean couponsBean = mData.get(position);
                    Intent intent = new Intent(CouponsListActivity.this, AddCouponsActivity.class);
                    intent.putExtra("couponsBean",couponsBean);
                    startActivity(intent);

                    break;
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
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
                            List<CouponsBean> catlist = gson.fromJson(ljson, new TypeToken<List<CouponsBean>>() {
                            }.getType());
                            if (catlist.size() > 0) {
                                mData.clear();
                                for (int i = 0; i < catlist.size(); i++) {
                                    mData.add(catlist.get(i));
                                }
                                recycleAdapter.notifyDataSetChanged();
                            }
                        } else if (status == 0) {
                            tosatMessage("获取数据失败");
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
                            List<CouponsBean> catlist = gson.fromJson(ljson, new TypeToken<List<CouponsBean>>() {
                            }.getType());
                            if (catlist.size() > 0) {
                                for (int i = 0; i < catlist.size(); i++) {
                                    mData.add(catlist.get(i));
                                }
                                recycleAdapter.notifyDataSetChanged();
                            }
                        } else if (status == 0) {
                            tosatMessage("获取数据失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
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
        setContentView(R.layout.activity_coupons_list);
        initView();
        refreshData();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        onStartTime = findViewById(R.id.onStartTime);
        onEndTime = findViewById(R.id.onEndTime);
        //设置标题
        titleName.setText("优惠卷列表");
        //数据模拟
        mData = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.viplistview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new CouponsAdapter(mData, this, handler);
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new NoLinesRecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
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
                loadMoreData();
            }
        });
    }

    /**
     * 加载更多
     */
    private void loadMoreData() {
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
        parmar.put("page", current_page + "");
        parmar.put("start_time", "");
        parmar.put("end_time", "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CUOPON_LIST, handler, 2);
    }


    /**
     * 选择开始的时间
     *
     * @param view
     */
    public void onStartTime(View view) {
        OpenTimeSelected.initStartTimePicker(CouponsListActivity.this, onStartTime, "开始日期");
    }

    /**
     * 选择结束时间
     *
     * @param view
     */
    public void onEndTime(View view) {
        OpenTimeSelected.initStartTimePicker(CouponsListActivity.this, onEndTime, "结束日期");
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
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CUOPON_LIST, handler, 1);
    }

    /**
     * 刷新列表数据
     */
    public void refreshData() {
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
        parmar.put("page", "1");
        parmar.put("start_time", "");
        parmar.put("end_time", "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CUOPON_LIST, handler, 1);

    }
}
