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
import zerone.myapplication.adapter.ProductListAdapter;
import zerone.myapplication.adapter.SalesclerAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.CategoryBean;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.message.ConfirmDialog;
import zerone.myapplication.message.ConfirmDoDialog;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;
import zerone.myapplication.utils.RecyclerViewDivider;

/**
 * Created by Administrator on 2018-11-16.
 */

public class ProductListActivity extends BaseAppActivity {
    private SmartRefreshLayout srfresh;
    private ProductListAdapter recycleAdapter;
    private ConfirmDialog cdialog;
    private List<ProductBean> mData;
    private ConfirmDoDialog ddialog;
    private int current_page = -1;
    private int totalPage = 0;
    private ZLoadingDialog zLoadingDialog;
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
                            List<ProductBean> catlist = gson.fromJson(ljson, new TypeToken<List<ProductBean>>() {
                            }.getType());
                            if (catlist.size() > 0) {
                                mData.clear();
                                for (int i = 0; i < catlist.size(); i++) {
                                    mData.add(catlist.get(i));
                                }
                                recycleAdapter.notifyDataSetChanged();
                            }

                        } else if (status == 0) {
                            Toast.makeText(ProductListActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
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
                            List<ProductBean> catlist = gson.fromJson(ljson, new TypeToken<List<ProductBean>>() {
                            }.getType());
                            for (int i = 0; i < catlist.size(); i++) {
                                mData.add(catlist.get(i));
                            }
                            recycleAdapter.notifyDataSetChanged();
                        } else if (status == 0) {
                            Toast.makeText(ProductListActivity.this, "加载数据失败!", Toast.LENGTH_SHORT).show();
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
    private EditText pnameEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        initView();
        initRecycleView();
    }

    public void onBackActivity(View view) {
        this.finish();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        pnameEdit = (EditText) findViewById(R.id.pname);
        //设置标题
        titleName.setText("商品列表");
        srfresh = findViewById(R.id.srl_fresh);
        srfresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                //下拉刷新数据
                getData();
            }
        });

        srfresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                //刷新数据中
                refreshData();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void initRecycleView() {
        mData = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new ProductListAdapter(mData, ProductListActivity.this);
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new NoLinesRecyclerViewDivider(this, 0));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /**
         * 长按删除
         */
        recycleAdapter.setOnLongClickListener(new SalesclerAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                initDialog(position);
            }
        });
        /**
         * 点击进入编辑
         */
        recycleAdapter.setOnClickListener(new SalesclerAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {

                initDoDialog(mData.get(position).getId());
            }
        });
    }

    public void initDialog(final int position) {
        if (cdialog == null) {
            cdialog = new ConfirmDialog(this);
        }
        cdialog.show();
        cdialog.setOnDialogClickListener(new ConfirmDialog.OnDialogClickListener() {
            @Override
            public void onOKClick(String spwd) {
                //这个是需要输入安全密码的
            }
            @Override
            public void onOKClick() {
                Log.i("URL", "long position" + position);
                mData.remove(position);
                recycleAdapter.updateData(mData);
            }
            @Override
            public void onCancelClick() {
                cdialog.dismiss();
            }
        });
    }

    /**
     * 获取数据
     */
    public void getData() {
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
        parmar.put("page", "1");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.PRODUCT_LIST, handler, 1);
    }

    /**
     * 加载更多
     */
    public void refreshData() {

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

        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.PRODUCT_LIST, handler, 2);
    }

    /**
     * 搜索商品
     */
    public void setSearchProduct(String pname) {
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
        parmar.put("name", pname);
        parmar.put("page", "1");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.PRODUCT_LIST, handler, 1);
    }

    public void onSearchProduct(View view) {
        String pname = pnameEdit.getText().toString().trim();
        if (!(pname.length() > 0)) {
            tosatMessage("请输入商品名称");
            return;
        }
        setSearchProduct(pname);
    }

    public void initDoDialog(final int goodsid) {
        if (ddialog == null) {
            ddialog = new ConfirmDoDialog(this);
        }
        ddialog.show();
        ddialog.setOnDialogClickListener(new ConfirmDoDialog.OnDialogClickListener() {
            @Override
            public void onCancelClick() {
                ddialog.dismiss();
            }

            @Override
            public void onContentEdit() {
                ddialog.dismiss();
                Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
                intent.putExtra("doaction", "1");
                Log.i("URL", "goodsid" + goodsid);
                intent.putExtra("goodsid", goodsid + "");
                startActivity(intent);
            }

            @Override
            public void onPictureEdit() {
                ddialog.dismiss();
                Intent intent = new Intent(ProductListActivity.this, PictureListAndDoActivity.class);
                intent.putExtra("doaction", "1");
                intent.putExtra("goodsid", goodsid + "");
                startActivity(intent);
            }
        });

    }
}
