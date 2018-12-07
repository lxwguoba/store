package zerone.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.scrat.app.selectorlibrary.ImageSelector;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.adapter.MyGridAdapter;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.interfaces.AddPictures;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.GridViewUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.MyRunnable;
import zerone.myapplication.utils.PicturesUtils;
import zerone.myapplication.view.MyGridView;

/**
 * Created by Administrator on 2018-11-26.
 */

public class AddPicturesActivity extends BaseAppActivity implements AddPictures {
    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT =5;
    private List<String> paths;
    private List<Bitmap> list;
    private MyGridView gridview;
    private MyGridAdapter adapter;
    private RequestParams params;
    private ZLoadingDialog zLoadingDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    tosatMessage("图片添加失败，请重试");
                    break;
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    break;
                case 10:
                    //上传图片返回来的信息
                    try {
                        String ms = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(ms);
                        int status = jsonObject.getInt("status");
                        if(status==1){
                            tosatMessage("图片添加成功");
                            AddPicturesActivity.this.finish();
                        }else if(status==0){
                            tosatMessage("图片添加失败，请重试");
                        };
                        Log.i("URL", jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /**
                     * 关闭加载动画
                     */
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    break;
            }
        }
    };
    private Intent intent;
    private String goods_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpictures);
        intent = getIntent();
        goods_id = intent.getStringExtra("goods_id");
        Log.i("URL", "商品ID" + goods_id);
        initView();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("添加图片");
        list = new ArrayList<>();
        gridview = findViewById(R.id.gridview);
        adapter = new MyGridAdapter(this, list);
        gridview.setAdapter(adapter);
        GridViewUtils.setListViewHeightBasedOnChildren(gridview, 3);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == list.size()) {
                    //启动拍照功能
                    ImageSelector.show(AddPicturesActivity.this, REQUEST_CODE_SELECT_IMG, MAX_SELECT_COUNT);
                }
            }
        });
    }

    /**
     * 返回上一级
     */
    public void onBackActivity(View view) {
        this.finish();
    }

    /**
     * 启动图片选择器
     *
     * @param view
     */
    @Override
    public void onOpenPhotos(View view) {
        //启动拍照功能
//        ImageSelector.show(AddPicturesActivity.this, REQUEST_CODE_SELECT_IMG, MAX_SELECT_COUNT);
    }

    /**
     * 保存选中的图片
     *
     * @param view
     */
    @Override
    public void onSavePictures(View view) {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        //https://ctwxl.com/upload/M_mUpLoad.do  java测试可以上传
        params = new RequestParams(IPConfig.ADD_PRODUCT_PICTURE);
        params.setMultipart(true);
        params.addBodyParameter("user_id", shopInfoBean.getUser_id() + "");
        params.addBodyParameter("timestamp", timestamp);
        params.addBodyParameter("token", token);
        params.addBodyParameter("store_id", shopInfoBean.getStore_id() + "");
        params.addBodyParameter("goods_id", goods_id);
        if (paths != null && paths.size() > 0) {
            for (int i = 0; i < paths.size(); i++) {
                //压缩后在上传
                File file = new File(paths.get(i));
                params.addBodyParameter("file[]", file);
            }
        }
        Log.i("BBB", "" + params.toString());
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "图片上传中···");
        }
        zLoadingDialog.show();
        new Thread(new MyRunnable(params, handler, 10)).start();
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
            Bitmap bitmap = PicturesUtils.getCompressedBitmap(paths.get(i), 192, 192);
//            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
            list.add(bitmap);
        }
        return list;
    }
}
