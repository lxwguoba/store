package com.zerone.intodata.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone.intodata.BaseAppActivity;
import com.zerone.intodata.R;
import com.zerone.intodata.adapter.PictureListAdtaper;
import com.zerone.intodata.config.IPConfig;
import com.zerone.intodata.domain.PirtureListBean;
import com.zerone.intodata.utils.AppSharePreferenceMgr;
import com.zerone.intodata.utils.CreateTokenUtils;
import com.zerone.intodata.utils.ItemGridLayoutManager;
import com.zerone.intodata.utils.MessageShow;
import com.zerone.intodata.utils.MyRunnable;
import com.zerone.intodata.utils.NoLinesRecyclerViewDivider;
import com.zerone.intodata.utils.UserConfig;
import com.zerone.intodata.view.BarProgress;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘兴文 on 2018-12-17.
 * 证件资料上传 图片
 */

public class DocumentsDataActivity extends BaseAppActivity implements View.OnClickListener {
    //使用照相机拍照获取图片
    public static final int TAKE_PHOTO_CODE = 1;
    //使用相册中的图片
    public static final int SELECT_PIC_CODE = 2;
    //图片裁剪
    private static final int PHOTO_CROP_CODE = 3;
    @Bind(R.id.picturelist)
    RecyclerView picturelist;
    @Bind(R.id.collectionType)
    TextView collectionType;
    private Dialog bottomDialog;
    //定义图片的Uri
    private Uri photoUri;
    //图片文件路径
    private String picPath;
    private ZLoadingDialog zLoadingDialog;
    private BarProgress pb;
    private TextView bfb;
    private Dialog pbDialog;
    private int selectedPos = -1;
    private List<PirtureListBean> plist;
    private PictureListAdtaper recycleAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    String error = (String) msg.obj;
                    Log.i("URL", error);
                    MessageShow.initErrorDialog(DocumentsDataActivity.this, error, 0);
                    pbDialog.dismiss();
                    break;
                case 0:

                    break;
                case 1:
                    try {
                        String pjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(pjson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            int anInt = jsonObject.getJSONObject("data").getInt("type");
                            String url = jsonObject.getJSONObject("data").getString("img");
                            if (selectedPos != -1) {
                                if (anInt == plist.get(selectedPos).getP_type()) {
                                    plist.get(selectedPos).setPurl(url);
                                }
                            }
                            recycleAdapter.notifyDataSetChanged();
                        } else {
                            MessageShow.initErrorDialog(DocumentsDataActivity.this, jsonObject.getString("msg"), 0);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 11:
                    //上传图片时的进度条
                    int p = (int) msg.obj;
                    Log.i("URL", "上传进度" + p + "%");
                    pb.setProgress(p);
                    bfb.setText("上传进度" + p + "%");
                    if (p == 100) {
                        pb.setProgress(0);
                        bfb.setText("上传进度" + 0 + "%");
                        pbDialog.dismiss();
                    }
                    break;
            }
        }
    };
    private String store_id;
    private String sub_type_name;
    private String sub_type_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentsdta);
        ButterKnife.bind(this);
        store_id = (String) AppSharePreferenceMgr.get(this, "store_id", "");
        sub_type_name = (String) AppSharePreferenceMgr.get(DocumentsDataActivity.this, "sub_type_name", "");
        sub_type_id = (String) AppSharePreferenceMgr.get(DocumentsDataActivity.this, "sub_type_id", "");
        if (store_id.length() == 0) {
            MessageShow.initErrorDialog(this, "您暂时还没有店铺，请先添加店铺。", 1);
        }
        if (sub_type_id.length() == 0) {
            MessageShow.initErrorDialog(this, "请先添加收款资料，再上传图片。", 1);
        }
        if (sub_type_name.length() > 0) {
            collectionType.setText("结算类型：" + sub_type_name);
        }
        initRecycleView();
    }

    /**
     * 关闭页面
     *
     * @param view
     */
    public void onCloseActivity(View view) {
        this.finish();
    }

    private void initRecycleView() {
        plist = new ArrayList<PirtureListBean>();
//        7-店内照,8-商户协议左图,9-商户协议右图,10-入账人身份证正面,11-入账人身份证反面,12-法人关系证明
        PirtureListBean pb1 = new PirtureListBean("", "营业执照", 1);
        PirtureListBean pb2 = new PirtureListBean("", "法人身份证正面", 2);
        PirtureListBean pb3 = new PirtureListBean("", "法人身份证背面", 3);
        PirtureListBean pb4 = new PirtureListBean("", "开户许可证", 4);
        PirtureListBean pb5 = new PirtureListBean("", "银行卡正面", 5);
        PirtureListBean pb6 = new PirtureListBean("", "门头照", 6);
        PirtureListBean pb7 = new PirtureListBean("", "店内照", 7);
        PirtureListBean pb8 = new PirtureListBean("", "商户协议左图", 8);
        PirtureListBean pb9 = new PirtureListBean("", "商户协议右图", 9);
        PirtureListBean pb10 = new PirtureListBean("", "入账人身份证正面", 10);
        PirtureListBean pb11 = new PirtureListBean("", "入账人身份证反面", 11);
        PirtureListBean pb12 = new PirtureListBean("", "法人关系证明", 12);
        plist.clear();
        plist.add(pb1);
        plist.add(pb2);
        plist.add(pb3);
        Log.i("URL", "1".equals(sub_type_id) + "");
        Log.i("URL", sub_type_id);
        if ("1".equals(sub_type_id)) {
            plist.add(pb4);

        }
        plist.add(pb5);
        plist.add(pb6);
        plist.add(pb7);
        plist.add(pb8);
        plist.add(pb9);
        if ("3".equals(sub_type_id)) {
            plist.add(pb10);
            plist.add(pb11);
            plist.add(pb12);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setAutoMeasureEnabled(true);
        //设置布局管理器setAutoMeasureEnabled
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recycleAdapter = new PictureListAdtaper(plist, this);
        //这个东西又问题  不能兼容 需要解决
        picturelist.setLayoutManager(new ItemGridLayoutManager(this, 2, recycleAdapter, picturelist));
        picturelist.setAdapter(recycleAdapter);
        //设置分隔线
        picturelist.addItemDecoration(new NoLinesRecyclerViewDivider(this, 0));
        //设置增加或删除条目的动画
        picturelist.setItemAnimator(new DefaultItemAnimator());
        recycleAdapter.setOnClickListener(new PictureListAdtaper.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                selectedPos = position;
                initDialog();
            }
        });
    }


    /**
     *
     */
    public void onSaveInfo(View view) {
        DocumentsDataActivity.this.finish();
    }

    private void initDialog() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_photoandpicture, null);
        Button select_photo_btn = contentView.findViewById(R.id.select_photo_btn);
        select_photo_btn.setOnClickListener(this);
        Button take_photo_btn = contentView.findViewById(R.id.take_photo_btn);
        take_photo_btn.setOnClickListener(this);
        Button quxiao = contentView.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(this);
        bottomDialog.setContentView(contentView);
        Window dialogWindow = bottomDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        //注意一定要是MATCH_PARENT
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //拍照
            case R.id.take_photo_btn:
                picTyTakePhoto();
                break;
            //选择图库
            case R.id.select_photo_btn:
                pickPhoto();
                break;
            case R.id.quxiao:
                bottomDialog.dismiss();
                break;
        }

    }

    /**
     * 拍照获取图片
     */
    private void picTyTakePhoto() {
        //判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            /***
             * 使用照相机拍照，拍照后的图片会存放在相册中。使用这种方式好处就是：获取的图片是拍照后的原图，
             * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图有可能不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, TAKE_PHOTO_CODE);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, SELECT_PIC_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //从相册取图片，有些手机有异常情况，请注意
            if (requestCode == SELECT_PIC_CODE) {
                if (null != data && null != data.getData()) {
                    photoUri = data.getData();
                    picPath = uriToFilePath(photoUri);
                    ss(picPath);
                } else {
                    Toast.makeText(this, "图片选择失败", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == TAKE_PHOTO_CODE) {
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    if (Build.VERSION.SDK_INT < 14) {
                        cursor.close();
                    }
                }
                if (picPath != null) {
                    photoUri = Uri.fromFile(new File(picPath));
                    ss(picPath);
                } else {
                    Log.i("URL", "您已退出图片选择");
                }
            }
        }
    }

    private String uriToFilePath(Uri uri) {
        //获取图片数据
        String[] proj = {MediaStore.Images.Media.DATA};
        //查询
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        //获得用户选择的图片的索引值
        int image_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        //返回图片路径
        return cursor.getString(image_index);
    }

    /**
     * 图片上传
     *
     * @param s
     */
    public void ss(String s) {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        String userName = UserConfig.getUserName(DocumentsDataActivity.this);
        RequestParams params = new RequestParams(IPConfig.APPLY_PHOTO_CHECK);
        params.setMultipart(true);
        params.addBodyParameter("username", userName);
        params.addBodyParameter("timestamp", timestamp);
        params.addBodyParameter("token", token);
        params.addBodyParameter("store_id", store_id);
        if (selectedPos == -1) {
            MessageShow.initErrorDialog(DocumentsDataActivity.this, "点击上传图片", 0);
            return;
        }
        params.addBodyParameter("type", plist.get(selectedPos).getP_type() + "");
        bottomDialog.dismiss();
        File file = new File(s);
        params.addBodyParameter("thumb", file);
        initPbDialog();
        new Thread(new MyRunnable(DocumentsDataActivity.this, params, handler, 1)).start();
    }

    private void initPbDialog() {
        pbDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.prog, null);
        pb = contentView.findViewById(R.id.pb);
        bfb = contentView.findViewById(R.id.bfb);
        pbDialog.setContentView(contentView);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = pbDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        pbDialog.getWindow().setAttributes(lp);
        pbDialog.getWindow().setGravity(Gravity.CENTER);
        pbDialog.show();
    }
}
