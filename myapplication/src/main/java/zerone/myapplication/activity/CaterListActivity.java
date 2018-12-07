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
import zerone.myapplication.LoginActivity;
import zerone.myapplication.R;
import zerone.myapplication.adapter.CasteringListAdapter;
import zerone.myapplication.adapter.ProductListAdapter;
import zerone.myapplication.adapter.SalesclerAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.CategoryBean;
import zerone.myapplication.domain.PayInfoBean;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.domain.ShopInfoBean;
import zerone.myapplication.message.ConfirmDialog;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;

/**
 * Created by Administrator on 2018-11-16.
 * 添加商品分类
 */

public class CaterListActivity extends BaseAppActivity {
    private SmartRefreshLayout srfresh;
    private CasteringListAdapter recycleAdapter;
    private ConfirmDialog cdialog;
    private List<CategoryBean> mData;
    private ZLoadingDialog zLoadingDialog;
    private int pos = -1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    Toast.makeText(CaterListActivity.this, "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:

                    try {
                        String catJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catJson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            String list = jsonObject.getString("data");
                            Gson gson = new Gson();
                            List<CategoryBean> catlist = gson.fromJson(list, new TypeToken<List<CategoryBean>>() {
                            }.getType());
                            mData.clear();
                            for (int i = 0; i < catlist.size(); i++) {
                                mData.add(catlist.get(i));
                            }
                            recycleAdapter.notifyDataSetChanged();
                        } else if (code == 0) {
                            Toast.makeText(CaterListActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
                    }
                    break;
                case 2:
                    try {
                        String deteJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(deteJson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            Toast.makeText(CaterListActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                            if (pos != -1) {
                                mData.remove(pos);
                                recycleAdapter.notifyDataSetChanged();
                            }
                        } else if (code == 0) {
                            Toast.makeText(CaterListActivity.this, "删除失败，请重试!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
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
    private EditText ipnut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caterlist);
        initView();
        initRecycleView();
        initGetCatData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initGetCatData();
    }

    /**
     * 获取分类
     */
    private void initGetCatData() {
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
        parmar.put("name", "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CATLIST, handler, 1);
    }

    /**
     * 返回上一级
     */
    public void onBackActivity(View view) {
        this.finish();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        ipnut = (EditText) findViewById(R.id.ipnut);
        titleName.setText("商品分类列表");
        srfresh = findViewById(R.id.srl_fresh);
        srfresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                //延时展示，延时2秒
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        initData();
//                        recycleAdapter.refresh(mData);
                        refreshlayout.finishRefresh();
                    }
                }, 2000);

            }
        });

        srfresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        initData();
//                        recycleAdapter.refresh(mData);
                        refreshlayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }

    private void initRecycleView() {
        mData = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.crecycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new CasteringListAdapter(mData, this);
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new NoLinesRecyclerViewDivider(this, 0));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recycleAdapter.setOnClickListener(new SalesclerAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                //跳转到编辑页面
                Intent intent = new Intent(CaterListActivity.this, AddCaterActivity.class);
                intent.putExtra("c_name", mData.get(position).getName());
                intent.putExtra("id", mData.get(position).getId() + "");
                startActivity(intent);
            }
        });
        recycleAdapter.setOnLongClickListener(new SalesclerAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                initDialog(position);
                pos = position;
            }
        });
    }

    /**
     * 提示框的操作
     *
     * @param position
     */
    public void initDialog(final int position) {
        if (cdialog == null) {
            cdialog = new ConfirmDialog(this);
        }
        cdialog.show();
        cdialog.setOnDialogClickListener(new ConfirmDialog.OnDialogClickListener() {
            @Override
            public void onOKClick(String spwd) {

            }

            @Override
            public void onOKClick() {
                gotoDelete(position);
            }

            @Override
            public void onCancelClick() {
                cdialog.dismiss();
            }

        });
    }

    /**
     * 移除分类
     */
    private void gotoDelete(int position) {
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
        parmar.put("id", mData.get(position).getId() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.DELETECAT, handler, 2);
    }

    /**
     * 查询分类
     */
    public void onSearchCate(View view) {
        String inname = ipnut.getText().toString().trim();
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
        if (inname.length() > 0) {
            parmar.put("name", inname);
        } else {
            parmar.put("name", "");
        }
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.CATLIST, handler, 1);
    }
}
