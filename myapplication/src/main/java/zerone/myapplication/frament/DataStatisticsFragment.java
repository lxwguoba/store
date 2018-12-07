package zerone.myapplication.frament;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zerone.myapplication.R;
import zerone.myapplication.adapter.TransactionsListItemAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.MessageEvent;
import zerone.myapplication.domain.PayInfoBean;
import zerone.myapplication.domain.ShopInfoBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.DoubleUtils;
import zerone.myapplication.utils.JsonToUser;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;

/**
 * Created by Administrator on 2018-11-15.
 * 数据统计页面
 */

public class DataStatisticsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TransactionsListItemAdapter recycleAdapter;
    private TextView todaymoney;
    private TextView todayTrans;
    private TextView allmoney;
    private TextView allTrans;
    //总页数
    private int totalPage = 0;

    //当前页面
    private int current_page = -1;

    private List<PayInfoBean> mData;
    private ZLoadingDialog zLoadingDialog;
    private SmartRefreshLayout srfresh;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    Toast.makeText(getContext(), "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    String ms = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(ms);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String money = jsonObject.getJSONObject("data").getString("today_price");
                            String count = jsonObject.getJSONObject("data").getString("today_num");
                            String moneys = jsonObject.getJSONObject("data").getString("price");
                            String counts = jsonObject.getJSONObject("data").getString("num");
                            String li = jsonObject.getJSONObject("data").getJSONObject("order_list").getString("data");
                            Gson gson = new Gson();
                            List<PayInfoBean> ps = gson.fromJson(li, new TypeToken<List<PayInfoBean>>() {
                            }.getType());
                            if (ps.size() > 0) {
                                totalPage = jsonObject.getJSONObject("data").getJSONObject("order_list").getInt("last_page");
                                current_page = jsonObject.getJSONObject("data").getJSONObject("order_list").getInt("current_page");
                                mData.clear();
                                for (int i = 0; i < ps.size(); i++) {
                                    mData.add(ps.get(i));
                                }
                            }
                            todayTrans.setText("" + count);
                            allTrans.setText("" + counts);
                            todaymoney.setText(money + "");
                            allmoney.setText(moneys + "");
                            recycleAdapter.notifyDataSetChanged();
                        } else if (status == 0) {

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
                        String m = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(m);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            int oldcount_today = Integer.parseInt(todayTrans.getText().toString().trim());
                            int oldcount_all = Integer.parseInt(allTrans.getText().toString().trim());
                            double oldmoney_today = Double.parseDouble(todaymoney.getText().toString().trim());
                            double oldmoney_all = Double.parseDouble(allmoney.getText().toString().trim());
                            double money = Double.parseDouble(jsonObject.getJSONObject("data").getString("today_price"));
                            int count = Integer.parseInt(jsonObject.getJSONObject("data").getString("today_num"));
                            double moneys = Double.parseDouble(jsonObject.getJSONObject("data").getString("price"));
                            int counts = Integer.parseInt(jsonObject.getJSONObject("data").getString("num"));
                            String li = jsonObject.getJSONObject("data").getJSONObject("order_list").getString("data");
                            Gson gson = new Gson();
                            List<PayInfoBean> ps = gson.fromJson(li, new TypeToken<List<PayInfoBean>>() {
                            }.getType());
                            if (ps.size() > 0) {
                                totalPage = jsonObject.getJSONObject("data").getJSONObject("order_list").getInt("last_page");
                                current_page = jsonObject.getJSONObject("data").getJSONObject("order_list").getInt("current_page");
                                for (int i = 0; i < ps.size(); i++) {
                                    mData.add(ps.get(i));
                                }
                            }
                            todayTrans.setText("" + (count));
                            allTrans.setText("" + (counts));
                            todaymoney.setText(DoubleUtils.setDouble(money) + "");
                            allmoney.setText(DoubleUtils.setDouble((moneys)) + "");
                            recycleAdapter.notifyDataSetChanged();
                        } else if (status == 0) {

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
    private ShopInfoBean shopInfoBean;

    /**
     * 页面重新启动时获取数据
     */
    @Override
    public void onResume() {
        super.onResume();
        shopInfoBean = JsonToUser.getShopInfoBean(getContext());
        initGetData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_datastatistics, container, false);
        }
        initView(view);
        return view;
    }


    private void initGetData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(getContext(), "获取数据中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(getContext(), timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("page", "1");
        NetUtils.netWorkByMethodPost(getContext(), parmar, IPConfig.PAY_ORDER_INFO, handler, 1);
    }


    /**
     *
     */
    private void initView(View view) {
        mData = new ArrayList<>();
        todaymoney = view.findViewById(R.id.todaymoney);
        todayTrans = view.findViewById(R.id.todayTrans);
        allmoney = view.findViewById(R.id.allmoney);
        allTrans = view.findViewById(R.id.allTrans);
        srfresh = view.findViewById(R.id.srl_fresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewInfo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new TransactionsListItemAdapter(mData, getContext());
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new NoLinesRecyclerViewDivider(getContext(), 0));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        srfresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                initGetData();
            }
        });
        srfresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                moreData();

            }
        });
    }


    private void moreData() {
        if (current_page < totalPage) {
            current_page++;
        } else {
            srfresh.setNoMoreData(true);
            srfresh.finishLoadMore();
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(getContext(), timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("page", current_page + "");
        NetUtils.netWorkByMethodPost(getContext(), parmar, IPConfig.PAY_ORDER_INFO, handler, 2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEvent(MessageEvent messageEvent) {
        Message message = new Message();
        message.what = 1;
        message.obj = messageEvent.getMessage();
        handler.sendMessage(message);
    }
}
