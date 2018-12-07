package zerone.myapplication.frament.log;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zerone.myapplication.R;
import zerone.myapplication.adapter.fragment.log.LoginLogAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.Customer;
import zerone.myapplication.domain.LogBean;
import zerone.myapplication.domain.PayInfoBean;
import zerone.myapplication.domain.ShopInfoBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.JsonToUser;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;


/**
 * Created by Administrator on 2018-11-15.
 * 数据统计页面
 */

public class LoginLogFragment extends Fragment {
    private int current_page;
    private int totalPage;
    private View view;
    private RecyclerView recyclerView;
    private SmartRefreshLayout srfresh;
    private LoginLogAdapter recycleAdapter;
    private List<LogBean> mData;
    private ZLoadingDialog zLoadingDialog;
    private ShopInfoBean shopInfoBean;
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
                    try {
                        String ms = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(ms);
                        int statu = jsonObject.getInt("status");
                        if (statu == 1) {
                            String li = jsonObject.getJSONObject("data").getString("data");
                            Gson gson = new Gson();
                            List<LogBean> ps = gson.fromJson(li, new TypeToken<List<LogBean>>() {
                            }.getType());
                            if (ps.size() > 0) {
                                totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                                current_page = jsonObject.getJSONObject("data").getInt("current_page");
                                mData.clear();
                                for (int i = 0; i < ps.size(); i++) {
                                    mData.add(ps.get(i));
                                }
                            }
                            recycleAdapter.notifyDataSetChanged();
                        } else if (statu == 0) {

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
                    String morjson = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(morjson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String li = jsonObject.getJSONObject("data").getString("data");
                            Gson gson = new Gson();
                            List<LogBean> ps = gson.fromJson(li, new TypeToken<List<LogBean>>() {
                            }.getType());
                            if (ps.size() > 0) {
                                totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                                current_page = jsonObject.getJSONObject("data").getInt("current_page");

                                for (int i = 0; i < ps.size(); i++) {
                                    mData.add(ps.get(i));
                                }
                            }
                            recycleAdapter.notifyDataSetChanged();
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_loginlog, container, false);
        }
        initRecycleView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        shopInfoBean = JsonToUser.getShopInfoBean(getContext());
        getLoginLogData();
    }

    private void initRecycleView(View view) {
        mData = new ArrayList<>();
        srfresh = view.findViewById(R.id.srl_fresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.loglistview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new LoginLogAdapter(mData, getActivity());
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new NoLinesRecyclerViewDivider(getContext(), LinearLayoutManager.VERTICAL));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        srfresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                rfreshLoginLogData();
            }
        });
        srfresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                moreLoginLogData();
            }
        });
    }


    public void getLoginLogData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(getContext(), "获取登录数据中···");
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
        NetUtils.netWorkByMethodPost(getContext(), parmar, IPConfig.LOGIN_LOG, handler, 1);
    }

    public void rfreshLoginLogData() {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(getContext(), timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("page", "1");
        NetUtils.netWorkByMethodPost(getContext(), parmar, IPConfig.LOGIN_LOG, handler, 1);
    }


    /**
     * 加载更多
     */
    public void moreLoginLogData() {
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
        NetUtils.netWorkByMethodPost(getContext(), parmar, IPConfig.LOGIN_LOG, handler, 2);
    }
}
