package zerone.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import zerone.myapplication.adapter.SalesclerAdapter;
import zerone.myapplication.adapter.fragment.log.LoginLogAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.LogBean;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.domain.SalesclerBean;
import zerone.myapplication.interfaces.Salescler;
import zerone.myapplication.message.ConfirmDialog;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;

/**
 * Created by Administrator on 2018-11-16.
 * 添加店员
 */

public class SalesclerLIstActivity extends BaseAppActivity implements Salescler {

    private RecyclerView salRecycleView;
    private SalesclerAdapter recycleAdapter;
    private ConfirmDialog cdialog;
    private List<SalesclerBean> mData;
    private SmartRefreshLayout srfresh;
    private ZLoadingDialog zLoadingDialog;
    private int totalPage;
    private int current_page;
    private EditText realname;
    private int delposition;
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
                    try {
                        String plistJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(plistJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            current_page = jsonObject.getJSONObject("data").getInt("current_page");
                            totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                            String ljson = jsonObject.getJSONObject("data").getString("data");
                            Gson gson = new Gson();
                            List<SalesclerBean> catlist = gson.fromJson(ljson, new TypeToken<List<SalesclerBean>>() {
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
                            List<SalesclerBean> catlist = gson.fromJson(ljson, new TypeToken<List<SalesclerBean>>() {
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
                    String delJson = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(delJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            tosatMessage("移除成功");
                            mData.remove(delposition);
                            recycleAdapter.notifyDataSetChanged();
                        } else if (status == 0) {
                            tosatMessage("移除失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
                        if (cdialog != null) {
                            cdialog.dismiss();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salescler);
        initView();
        initRecycleView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initGetData();
    }

    /**
     * 返回上一级
     */
    public void onBackActivity(View view) {
        this.finish();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("店员列表");
        realname = (EditText) findViewById(R.id.realname);
        srfresh = findViewById(R.id.srl_fresh);
        srfresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                initGetData();
            }
        });

        srfresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                moreLoadData();
            }
        });
    }

    /**
     * 加载更多
     */
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
        parmar.put("realname", "");
        parmar.put("page", current_page + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CLERK_LIST, handler, 2);
    }

    public void initDialog(final int position) {
        if (cdialog == null) {
            cdialog = new ConfirmDialog(this, true);
        }
        cdialog.show();
        cdialog.setOnDialogClickListener(new ConfirmDialog.OnDialogClickListener() {
            @Override
            public void onOKClick(String safepwd) {
                deleteSalescler(position, safepwd);
            }

            @Override
            public void onOKClick() {

            }

            @Override
            public void onCancelClick() {
                cdialog.dismiss();
            }
        });
    }

    /**
     * 移除店员
     */
    private void deleteSalescler(int position, String safepwd) {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "正在移除中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("id", mData.get(position).getId() + "");
        parmar.put("safe_password", safepwd);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CLERK_DELETE, handler, 3);
    }

    private void initRecycleView() {
        mData = new ArrayList<>();
        salRecycleView = (RecyclerView) findViewById(R.id.salRecycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        salRecycleView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new SalesclerAdapter(mData, this);
        salRecycleView.setAdapter(recycleAdapter);
        //设置分隔线
        salRecycleView.addItemDecoration(new NoLinesRecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        //设置增加或删除条目的动画
        salRecycleView.setItemAnimator(new DefaultItemAnimator());
        recycleAdapter.setOnClickListener(new SalesclerAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(SalesclerLIstActivity.this, AddSalesclerkActivity.class);
                intent.putExtra("salesclerBean", mData.get(position));
                startActivity(intent);
            }
        });

        recycleAdapter.setOnLongClickListener(new SalesclerAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                delposition = position;
                initDialog(position);
            }
        });

    }

    /**
     * @param view
     */
    public void onSearchSalescler(View view) {
        String username = realname.getText().toString().trim();
        if (!(username.length() > 0)) {
            tosatMessage("请输入用户名称");
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
        parmar.put("realname", username);
        parmar.put("page", "1");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CLERK_LIST, handler, 1);
    }

    public void initGetData() {
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
        parmar.put("realname", "");
        parmar.put("page", "1");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CLERK_LIST, handler, 1);
    }
}
