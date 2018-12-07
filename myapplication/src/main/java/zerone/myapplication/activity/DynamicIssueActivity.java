package zerone.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scrat.app.selectorlibrary.ImageSelector;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.adapter.DynamicIssueListAdapter;
import zerone.myapplication.adapter.MyGridAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.DynamicBean;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.domain.ResponseBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.GridViewUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;
import zerone.myapplication.utils.UpLoadUtils;


/**
 * Created by Administrator on 2018-11-19.
 * 发布动态
 */

public class DynamicIssueActivity extends BaseAppActivity {
    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;
    private GridView gridview;
    private List<Bitmap> list;
    private MyGridAdapter adapter;
    private EditText content;
    private List<String> paths;
    private DynamicIssueListAdapter recycleAdapter;
    private RequestParams params;
    private List<DynamicBean> mData;
    private BitmapFactory.Options o;
    private RecyclerView recyclerView;
    private TextView store_address;
    private TextView store_name;
    private TextView store_phone;
    private ZLoadingDialog zLoadingDialog;
    private int current_page;
    private int totalPage;
    private TextView ddcount;
    private TextView attentioncount;
    private TextView dycount;
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
                    Toast.makeText(DynamicIssueActivity.this, "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        String catjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catjson);

                        int code = jsonObject.getInt("status");
                        String attention_num = jsonObject.getString("attention_num");
                        String dynamic_num = jsonObject.getString("dynamic_num");
                        String user_num = jsonObject.getString("user_num");
                        String logo=jsonObject.getString("logo");

                        Glide.with(DynamicIssueActivity.this).load(logo).error(R.mipmap.login_head).into(accountimg);
                        ddcount.setText(user_num);
                        attentioncount.setText(attention_num);
                        dycount.setText(dynamic_num);
                        if (code == 1) {
                            String listjson = jsonObject.getJSONObject("data").getString("data");
                            current_page = jsonObject.getJSONObject("data").getInt("current_page");
                            totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                            Gson gson = new Gson();
                            List<DynamicBean> mdlsit = gson.fromJson(listjson, new TypeToken<List<DynamicBean>>() {
                            }.getType());
                            if (mdlsit.size() > 0) {
                                mData.clear();
                                for (int i = 0; i < mdlsit.size(); i++) {
                                    mData.add(mdlsit.get(i));
                                }
                                recycleAdapter.notifyDataSetChanged();
                            }
                        } else if (code == 0) {
                            tosatMessage("没有数据");
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
                        String catjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catjson);
                        Log.i("URL",jsonObject.toString());
                        int code = jsonObject.getInt("status");
                        String attention_num = jsonObject.getString("attention_num");
                        String dynamic_num = jsonObject.getString("dynamic_num");
                        String user_num = jsonObject.getString("user_num");
                        ddcount.setText(user_num);
                        attentioncount.setText(attention_num);
                        dycount.setText(dynamic_num);
                        if (code == 1) {
                            String listjson = jsonObject.getJSONObject("data").getString("data");
                            current_page = jsonObject.getJSONObject("data").getInt("current_page");
                            totalPage = jsonObject.getJSONObject("data").getInt("last_page");
                            Gson gson = new Gson();
                            List<DynamicBean> mdlsit = gson.fromJson(listjson, new TypeToken<List<DynamicBean>>() {
                            }.getType());
                            if (mdlsit.size() > 0) {
                                for (int i = 0; i < mdlsit.size(); i++) {
                                    mData.add(mdlsit.get(i));
                                }
                                recycleAdapter.notifyDataSetChanged();
                            }
                        } else if (code == 0) {
                            tosatMessage("没有数据");
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
                    String json = (String) msg.obj;

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        Log.i("URL", jsonObject.toString());
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            Toast.makeText(DynamicIssueActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            content.setText("");
                            list.clear();
                            adapter.notifyDataSetChanged();
                            GridViewUtils.setListViewHeightBasedOnChildren(gridview, 3);
                            initData();
                        }
                    } catch (Exception e) {
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
    private CircleImageView accountimg;

    /**
     * 通过图片的绝对路径来获取对应的压缩后的Bitmap对象
     */
    public static Bitmap getCompressedBitmap(String filePath, int requireWidth,
                                             int requireHeight) {
        // 第一次解析将inJustDecodeBounds设置为true,用以获取图片大小,并且不需要将Bitmap对象加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options); // 第一次解析
        // 计算inSampleSize的值,并且赋值给Options.inSampleSize
        options.inSampleSize = calculateInSampleSize(options, requireWidth,
                requireHeight);
        // 使用获取到的inSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算压缩的inSampleSize的值,该值会在宽高上都进行压缩(也就是压缩前后比例是inSampleSize的平方倍)
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int requireWidth, int requireHeight) {
        // 获取源图片的实际的宽度和高度
        int realWidth = options.outWidth;
        int realHeight = options.outHeight;

        int inSampleSize = 1;
        if (realWidth > requireWidth || realHeight > requireHeight) {
            // 计算出实际的宽高与目标宽高的比例
            int widthRatio = Math.round((float) realWidth
                    / (float) requireWidth);
            int heightRatio = Math.round((float) realHeight
                    / (float) requireHeight);
            // 选择宽高比例最小的值赋值给inSampleSize,这样可以保证最终图片的宽高一定会大于或等于目标的宽高
            inSampleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicissue);
        initView();
        initRecycleView();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * 返回上一级
     */
    public void onBackActivity(View view) {
        this.finish();
    }

    private void initRecycleView() {
        mData = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycledynam);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //高度自适应
        layoutManager.setAutoMeasureEnabled(true);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new DynamicIssueListAdapter(mData, DynamicIssueActivity.this);
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new NoLinesRecyclerViewDivider(this, 0));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recycleAdapter.setOnItemClickListener(new DynamicIssueListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("URL", "position" + position);
            }
        });
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        accountimg = (CircleImageView) findViewById(R.id.accountimg);
        store_address = findViewById(R.id.store_address);
        store_name = findViewById(R.id.store_name);
        store_phone = findViewById(R.id.store_phone);
        ddcount = (TextView) findViewById(R.id.ddcount);
        attentioncount = (TextView) findViewById(R.id.attentioncount);
        dycount = (TextView) findViewById(R.id.dycount);
        store_address.setText(shopInfoBean.getStore_address());
        store_name.setText(shopInfoBean.getStore_name());
        store_phone.setText(shopInfoBean.getMobile());
        titleName.setText("商家动态");
        content = (EditText) findViewById(R.id.content);
        gridview = (GridView) findViewById(R.id.gridview);

        list = new ArrayList<>();
        adapter = new MyGridAdapter(this, list);
        gridview.setAdapter(adapter);
        GridViewUtils.setListViewHeightBasedOnChildren(gridview, 3);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == list.size()) {
                    //启动拍照功能
                    ImageSelector.show(DynamicIssueActivity.this, REQUEST_CODE_SELECT_IMG, MAX_SELECT_COUNT);
                }
            }
        });


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
                //上拉加载
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
        parmar.put("page", current_page + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.DYNAMIC_LIST, handler, 2);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMG) {
            showContent(data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showContent(Intent data) {
        paths = ImageSelector.getImagePaths(data);

        if (paths.isEmpty()) {
            Toast.makeText(this, "选择有问题", Toast.LENGTH_SHORT).show();
            return;
        } else {
            list.clear();
            List<Bitmap> mdata = diaplayImage(paths);
            for (int i = 0; i < mdata.size(); i++) {
                list.add(mdata.get(i));
            }
            adapter.notifyDataSetChanged();
            GridViewUtils.setListViewHeightBasedOnChildren(gridview, 3);
        }
    }

    private List<Bitmap> diaplayImage(List<String> paths) {
        List<Bitmap> list = new ArrayList<>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        for (int i = 0; i < paths.size(); i++) {
            //压缩后的图片
            Bitmap bitmap = getCompressedBitmap(paths.get(i), 192, 192);
//            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
            list.add(bitmap);
        }
        return list;
    }

    public void initData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "内容加载中");
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
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.DYNAMIC_LIST, handler, 1);

    }


    public void refreshData() {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("page", "1");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.DYNAMIC_LIST, handler, 1);

    }

    public void doSendMessage(View view) {
        params = new RequestParams(IPConfig.DYNAMIC_ADD);
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        params.addBodyParameter("user_id", shopInfoBean.getUser_id() + "");
        params.addBodyParameter("timestamp", timestamp);
        params.addBodyParameter("token", token);
        params.addBodyParameter("store_id", shopInfoBean.getStore_id() + "");

        params.setMultipart(true);
        String contents = content.getText().toString().trim();
        if ((contents != null && contents.length() > 0)) {
            params.addBodyParameter("content", contents);
        }

        if (paths != null && paths.size() > 0) {
            for (int i = 0; i < paths.size(); i++) {
                //压缩后在上传
//                File file = new File(BitmapUtil.compressImage(paths.get(i)));
                File file = new File(paths.get(i));
                params.addBodyParameter("thumb[]", file);
            }
        }
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "发布动态");
        }
        zLoadingDialog.show();
        new Thread(new DynamicIssueActivity.MyRunnable()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            UpLoadUtils.xuploadPro(params, handler, 3);
        }
    }
}
