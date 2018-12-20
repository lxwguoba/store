package com.zerone.intodata.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zerone.intodata.R;
import com.zerone.intodata.activity.AddStoreActivity;
import com.zerone.intodata.activity.UpDataIntoInfoActivity;
import com.zerone.intodata.adapter.IntoListAdapter;
import com.zerone.intodata.config.IPConfig;
import com.zerone.intodata.domain.IntoListBean;
import com.zerone.intodata.domain.StoreInfoBean;
import com.zerone.intodata.utils.AppSharePreferenceMgr;
import com.zerone.intodata.utils.CreateTokenUtils;
import com.zerone.intodata.utils.LoadingUtils;
import com.zerone.intodata.utils.NetUtils;
import com.zerone.intodata.utils.NoLinesRecyclerViewDivider;
import com.zerone.intodata.utils.UserConfig;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘兴文 on 2018-12-17.
 * 进件列表
 */

public class IntoDataListFragment extends Fragment {

    @Bind(R.id.datalist)
    RecyclerView datalist;
    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.srl_fresh)
    SmartRefreshLayout srlFresh;
    private View view;
    private List<IntoListBean> list;
    private IntoListAdapter recycleAdapter;
    private ZLoadingDialog zLoadingDialog;
    private int position = 0;
    private ZLoadingDialog dialog;
    private int type = 0;
    private int totalPage;
    private int current_page;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int pos = (int) msg.obj;
                    Intent intent = new Intent(getContext(), UpDataIntoInfoActivity.class);
                    intent.putExtra("store_id", list.get(pos).getStroe_id());
                    startActivity(intent);
                    break;
                case 2:
                    try {
                        String listJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(listJson);
                        Log.i("URL", jsonObject.toString());
                        int status = jsonObject.getInt("status");
                        list.clear();
                        if (status == 1) {
                            String li = jsonObject.getJSONObject("data").getString("data");
                            Gson gson = new Gson();
                            List<StoreInfoBean> ps = gson.fromJson(li, new TypeToken<List<StoreInfoBean>>() {
                            }.getType());
                            if (ps.size() > 0) {
                                totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                                current_page = jsonObject.getJSONObject("data").getInt("current_page");
                                for (int i = 0; i < ps.size(); i++) {
                                    IntoListBean intoListBean = new IntoListBean();
                                    intoListBean.setIs_apply(ps.get(i).getIs_apply());
                                    intoListBean.setRemark(ps.get(i).getRemark());
                                    intoListBean.setStroe_name(ps.get(i).getName());
                                    intoListBean.setStroe_id(ps.get(i).getId() + "");
                                    intoListBean.setStroe_create_tme(ps.get(i).getCreated_at());
                                    intoListBean.setStroe_address(ps.get(i).getProvince_name() + "-" + ps.get(i).getCity_name() + (ps.get(i).getArea_name() != null ? ("-" + ps.get(i).getArea_name()) : ""));
                                    intoListBean.setStroe_lmzk(ps.get(i).getThirdpay_rate() + "");
                                    list.add(intoListBean);
                                }
                            }
                            recycleAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                            zLoadingDialog.cancel();
                        }
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                        if (srlFresh.isShown()) {
                            srlFresh.finishRefresh();
                        }
                    }
                    break;
                case 3:
                    try {
                        String listJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(listJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String li = jsonObject.getJSONObject("data").getString("data");
                            Gson gson = new Gson();
                            List<StoreInfoBean> ps = gson.fromJson(li, new TypeToken<List<StoreInfoBean>>() {
                            }.getType());
                            if (ps.size() > 0) {
                                totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                                current_page = jsonObject.getJSONObject("data").getInt("current_page");
                                for (int i = 0; i < ps.size(); i++) {
                                    IntoListBean intoListBean = new IntoListBean();
                                    intoListBean.setIs_apply(intoListBean.getIs_apply());
                                    intoListBean.setStroe_name(ps.get(i).getName());
                                    intoListBean.setStroe_id(ps.get(i).getId() + "");
                                    intoListBean.setStroe_create_tme(ps.get(i).getCreated_at());
                                    intoListBean.setStroe_address(ps.get(i).getProvince_name() + ps.get(i).getCity_name() + ps.get(i).getArea_name());
                                    intoListBean.setStroe_lmzk(ps.get(i).getThirdpay_rate() + "");
                                    list.add(intoListBean);
                                }
                            }
                            recycleAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (srlFresh.isShown()) {
                            srlFresh.finishLoadMore();
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
            view = inflater.inflate(R.layout.fragment_intodatalist, container, false);
        }
        ButterKnife.bind(this, view);
        TabLayoutViewListenner();
        initRecycleView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getStoreListData("0");
    }

    private void initRecycleView() {
        list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            IntoListBean ilb = new IntoListBean();
            ilb.setType(0);
            list.add(ilb);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        datalist.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new IntoListAdapter(list, getContext(), handler);
        datalist.setAdapter(recycleAdapter);
        //设置分隔线
        datalist.addItemDecoration(new NoLinesRecyclerViewDivider(getContext(), 0));
        //设置增加或删除条目的动画
        datalist.setItemAnimator(new DefaultItemAnimator());

        srlFresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                getStoreListData("" + type);
            }
        });
        srlFresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
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
            srlFresh.setNoMoreData(true);
            srlFresh.finishLoadMore();
            return;
        }
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(getActivity(), timestamp);
        String userName = UserConfig.getUserName(getActivity());
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        parmar.put("type", type + "");
        parmar.put("page", current_page + "");
        NetUtils.netWorkByMethodPost(getActivity(), parmar, IPConfig.GET_STORE_LIST, handler, 3);


    }

    private void TabLayoutViewListenner() {
        for (int i = 0; i < tab.getTabCount(); i++) {
            TabLayout.Tab tabb = tab.getTabAt(i);
            if (tabb == null) return;
            //这里使用到反射，拿到Tab对象后获取Class
            Class c = tabb.getClass();
            try {
                //Filed “字段、属性”的意思,c.getDeclaredField 获取私有属性。
                //"mView"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                Field field = c.getDeclaredField("mView");
                //值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
                //如果不这样会报如下错误
                // java.lang.IllegalAccessException:
                //Class com.test.accessible.Main
                //can not access
                //a member of class com.test.accessible.AccessibleTest
                //with modifiers "private"
                field.setAccessible(true);
                final View view = (View) field.get(tabb);
                if (view == null) return;
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position = (int) view.getTag();
                        //这里就可以根据业务需求处理点击事件了。
                        if (position == 0) {
                            getPressStoreListData("0");
                            type = 0;
                        } else if (position == 1) {
                            getPressStoreListData("1");
                            type = 1;
                        } else if (position == 2) {
                            getPressStoreListData("2");
                            type = 2;
                        } else if (position == 3) {
                            getPressStoreListData("3");
                            type = 3;
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void getStoreListData(String type) {
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(getActivity(), timestamp);
        String userName = UserConfig.getUserName(getActivity());
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        parmar.put("type", type);
        parmar.put("page", "1");
        NetUtils.netWorkByMethodPost(getActivity(), parmar, IPConfig.GET_STORE_LIST, handler, 2);
    }

    public void getPressStoreListData(String type) {
        dialog = LoadingUtils.openLoading(getActivity(), "获取资料中···");
        dialog.show();
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(getActivity(), timestamp);
        String userName = UserConfig.getUserName(getActivity());
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        parmar.put("type", type);
        parmar.put("page", "1");
        NetUtils.netWorkByMethodPost(getActivity(), parmar, IPConfig.GET_STORE_LIST, handler, 2);
    }
}
