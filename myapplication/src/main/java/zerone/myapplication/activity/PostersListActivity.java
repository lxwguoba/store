package zerone.myapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.adapter.PostersListAdapter;
import zerone.myapplication.adapter.ProductListAdapter;
import zerone.myapplication.adapter.SalesclerAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.PostersBean;
import zerone.myapplication.domain.PostersCateBean;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;

/**
 * Created by 刘兴文 on 2018-12-05.
 */

public class PostersListActivity extends BaseAppActivity {
    private int current_page = -1;
    private int totalPage = 0;
    private PostersListAdapter recycleAdapter;
    private ZLoadingDialog zLoadingDialog;
    private List<PostersBean> mData;
    private RecyclerView recyclerView;
    private SmartRefreshLayout srfresh;
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
                        String plistJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(plistJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            current_page = jsonObject.getJSONObject("data").getInt("current_page");
                            totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                            String ljson = jsonObject.getJSONObject("data").getString("data");
                            Gson gson = new Gson();
                            List<PostersBean> catlist = gson.fromJson(ljson, new TypeToken<List<PostersBean>>() {
                            }.getType());
                            if (catlist.size() > 0) {
                                mData.clear();
                                for (int i = 0; i < catlist.size(); i++) {
                                    mData.add(catlist.get(i));
                                }
                                recycleAdapter.notifyDataSetChanged();
                            }
                        } else if (status == 0) {
                            tosatMessage("获取数据失败!");
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
                            List<PostersBean> catlist = gson.fromJson(ljson, new TypeToken<List<PostersBean>>() {
                            }.getType());
                            if (catlist.size() > 0) {
                                for (int i = 0; i < catlist.size(); i++) {
                                    mData.add(catlist.get(i));
                                }
                                recycleAdapter.notifyDataSetChanged();
                            }
                        } else if (status == 0) {
                            tosatMessage("获取数据失败!");
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
                case 3:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posterslist);
        initView();
        initRecycleView();
        getPostersList();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("海报列表");
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
    }

    /**
     * 加载更多
     */
    public void moreLoadData() {
        current_page++;
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("name", "");
        parmar.put("page", current_page + "");
        if (current_page > totalPage) {
            srfresh.setNoMoreData(true);
            srfresh.finishLoadMore();
            return;
        }
        Log.i("URL", "nextIndex=" + current_page);
        Log.i("URL", "totalPage=" + totalPage);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.POSTER_LIST, handler, 2);
    }

    private void initRecycleView() {
        mData = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new PostersListAdapter(mData, PostersListActivity.this);
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new NoLinesRecyclerViewDivider(this, 0));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 获取列表数据
     */
    public void getPostersList() {
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
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.POSTER_LIST, handler, 1);
    }
    /**
     * 获取列表数据
     */
    public void refreshData() {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.POSTER_LIST, handler, 1);
    }
}
