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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.adapter.PicturesAdapter;
import zerone.myapplication.adapter.SalesclerAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.ImagesBean;
import zerone.myapplication.domain.ProductBean;
import zerone.myapplication.domain.SalesclerBean;
import zerone.myapplication.message.ConfirmDialog;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.NoLinesRecyclerViewDivider;

/**
 * Created by Administrator on 2018-11-26.
 */

public class PictureListAndDoActivity extends BaseAppActivity {

    private PicturesAdapter recycleAdapter;
    private ConfirmDialog cdialog;
    private List<ImagesBean> mData;
    private ZLoadingDialog zLoadingDialog;
    private String goodsid;
    private int imgposition = -1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    Toast.makeText(PictureListAndDoActivity.this, "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        String imgs = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(imgs);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String ljson = jsonObject.getString("data");
                            Gson gson = new Gson();
                            List<ImagesBean> catlist = gson.fromJson(ljson, new TypeToken<List<ImagesBean>>() {
                            }.getType());
                            if (catlist.size() > 0) {
                                mData.clear();
                                for (int i = 0; i < catlist.size(); i++) {
                                    mData.add(catlist.get(i));
                                }
                                recycleAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(PictureListAndDoActivity.this, "您还没有上传图片!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (status == 0) {
                            Toast.makeText(PictureListAndDoActivity.this, "获取图片失败!", Toast.LENGTH_SHORT).show();
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
                        String delJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(delJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            tosatMessage("图片删除成功！");
                            if (imgposition != -1) {
                                mData.remove(imgposition);
                                recycleAdapter.updateData(mData);
                            }
                        } else if (status == 0) {
                            tosatMessage("图片删除失败,请重试！");

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picturelistanddo);
        goodsid = getIntent().getStringExtra("goodsid");
        initRecycleView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getPicture();
    }

    public void onBackActivity(View view) {
        this.finish();
    }

    private void initRecycleView() {
        mData = new ArrayList<>();
        RecyclerView precycleView = (RecyclerView) findViewById(R.id.precycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        precycleView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new PicturesAdapter(mData, this);
        precycleView.setAdapter(recycleAdapter);
        //设置分隔线
        precycleView.addItemDecoration(new NoLinesRecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        //设置增加或删除条目的动画
        precycleView.setItemAnimator(new DefaultItemAnimator());
        recycleAdapter.setOnClickListener(new PicturesAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                imgposition = position;
                initDialog();
            }
        });
    }

    public void initDialog() {
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
                deletePicture();

            }
            @Override
            public void onCancelClick() {
                cdialog.dismiss();
            }
        });
    }

    /**
     * 移除图片
     */
    private void deletePicture() {

        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "删除图片中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("goods_id", goodsid);
        parmar.put("id",mData.get(imgposition).getId()+"");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.PICTURE_DELETE, handler, 2);
    }

    public void onChangePicture(View view) {
        Intent intent = new Intent(this, AddPicturesActivity.class);
        intent.putExtra("goods_id", goodsid);
        startActivity(intent);
    }

    /**
     * 搜索商品
     */
    public void getPicture() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "图片获取中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        parmar.put("id", goodsid);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.PRODUCT_IMG, handler, 1);
    }
}
