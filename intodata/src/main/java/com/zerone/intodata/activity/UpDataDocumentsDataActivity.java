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

import com.google.gson.Gson;
import com.zerone.intodata.BaseAppActivity;
import com.zerone.intodata.R;
import com.zerone.intodata.adapter.PictureListAdtaper;
import com.zerone.intodata.config.IPConfig;
import com.zerone.intodata.domain.BankInfo;
import com.zerone.intodata.domain.PictureInfoBean;
import com.zerone.intodata.domain.PirtureListBean;
import com.zerone.intodata.utils.CreateTokenUtils;
import com.zerone.intodata.utils.ItemGridLayoutManager;
import com.zerone.intodata.utils.MessageShow;
import com.zerone.intodata.utils.MyRunnable;
import com.zerone.intodata.utils.NetUtils;
import com.zerone.intodata.utils.NoLinesRecyclerViewDivider;
import com.zerone.intodata.utils.UserConfig;
import com.zerone.intodata.view.BarProgress;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘兴文 on 2018-12-17.
 * 修改 证件资料上传 图片
 */

public class UpDataDocumentsDataActivity extends BaseAppActivity implements View.OnClickListener {
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


    private List<PirtureListBean> plist;
    private Dialog bottomDialog;
    //定义图片的Uri
    private Uri photoUri;
    //图片文件路径
    private String picPath;
    private String store_id;
    private PictureListAdtaper recycleAdapter;
    private Dialog pbDialog;
    private BarProgress pb;
    private TextView bfb;
    private int selectedPos = -1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    break;
                case 1:
                    try {
                        String pjson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(pjson);
                        Log.i("URL", jsonObject.toString());
                        int status = jsonObject.getInt("status");
                        String sub_typ = jsonObject.getString("sub_type");
                        if ("false".equals(sub_typ)) {
                            MessageShow.initErrorDialog(UpDataDocumentsDataActivity.this, "您还没有填写收款信息", 1);
                        } else {
                            int sub_type = Integer.parseInt(sub_typ);
                            if (sub_type == 0) {
                                //这个不能操作这个页面 需要先提交资料
                            } else if (sub_type == 1) {
                                collectionType.setText("交易结算类型：对公账户");
                            } else if (sub_type == 2) {
                                collectionType.setText("交易结算类型：对私法人入账");
                            } else if (sub_type == 3) {
                                collectionType.setText("交易结算类型：对私非法人入账");
                            }

                            if (status == 1) {
                                String data = jsonObject.getString("data");
                                if ("false".equals(data)) {
                                    MessageShow.initErrorDialog(UpDataDocumentsDataActivity.this, "您还没有上传图片，请及时上传", 0);
                                } else {
                                    Gson gson = new Gson();
                                    PictureInfoBean pictureInfoBean = gson.fromJson(data, PictureInfoBean.class);
                                    todoSetViewData(pictureInfoBean, sub_type);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
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
                            MessageShow.initErrorDialog(UpDataDocumentsDataActivity.this, jsonObject.getString("msg"), 0);
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

    /**
     * 图片的初始化
     *
     * @param pictureInfoBean
     */
    private void todoSetViewData(PictureInfoBean pictureInfoBean, int sub_type) {
        plist.clear();
        // 7-店内照,8-商户协议左图,9-商户协议右图,10-入账人身份证正面,11-入账人身份证反面,12-法人关系证明
        //"营业执照
        PirtureListBean pb1 = new PirtureListBean(pictureInfoBean.getLicense(), "营业执照", 1);
        //法人身份证正面
        PirtureListBean pb2 = new PirtureListBean(pictureInfoBean.getIdcard_pos(), "法人身份证正面", 2);
        //法人身份证背面
        PirtureListBean pb3 = new PirtureListBean(pictureInfoBean.getIdcard_oth(), "法人身份证背面", 3);
        //开户许可证
        PirtureListBean pb4 = new PirtureListBean(pictureInfoBean.getOpening_permit(), "开户许可证", 4);
        //银行卡正面
        PirtureListBean pb5 = new PirtureListBean(pictureInfoBean.getBank_card(), "银行卡正面", 5);
        //门头照
        PirtureListBean pb6 = new PirtureListBean(pictureInfoBean.getDoor_photo(), "门头照", 6);
        //店内照
        PirtureListBean pb7 = new PirtureListBean(pictureInfoBean.getShop_photo(), "店内照", 7);
        //商户协议左图
        PirtureListBean pb8 = new PirtureListBean(pictureInfoBean.getIncrement_agree(), "商户协议左图", 8);
        //商户协议右图
        PirtureListBean pb9 = new PirtureListBean(pictureInfoBean.getSpecial_agree(), "商户协议右图", 9);
        //入账人身份证正面照
        PirtureListBean pb10 = new PirtureListBean(pictureInfoBean.getFas_idcard_pos(), "入账人身份证正面", 10);
        //入账人身份证反面
        PirtureListBean pb11 = new PirtureListBean(pictureInfoBean.getFas_idcard_oth(), "入账人身份证反面", 11);
        //法人关系证明
        PirtureListBean pb12 = new PirtureListBean(pictureInfoBean.getPerson_prove(), "法人关系证明", 12);
        plist.add(pb1);
        plist.add(pb2);
        plist.add(pb3);
        if (sub_type == 1) {
            plist.add(pb4);
        }
        plist.add(pb5);
        plist.add(pb6);
        plist.add(pb7);
        plist.add(pb8);
        plist.add(pb9);
        if (sub_type == 3) {
            plist.add(pb10);
            plist.add(pb11);
            plist.add(pb12);
        }
        recycleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_documentsdta);
        ButterKnife.bind(this);
        initRecycleView();
        store_id = getIntent().getStringExtra("store_id");
        getBankInfo();
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
                //启动拍照
//                1-营业执照,2-法人身份证正面,3-法人身份证反面,4-开户许可证,5-银行卡正面照片,6-店铺门头照,7-店内照,8-收银台,9-增值协议正面照
                selectedPos = position;
                initDialog();
            }
        });

    }

    /**
     * 获取图片
     */
    private void getBankInfo() {
        String userName = UserConfig.getUserName(this);
        Map<String, String> parmar = new HashMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        parmar.put("timestamp", timestamp);
        parmar.put("username", userName);
        parmar.put("token", token);
        parmar.put("store_id", store_id);
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.GET_STORE_DATAPHOTO, handler, 1);

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
        String userName = UserConfig.getUserName(UpDataDocumentsDataActivity.this);
        RequestParams params = new RequestParams(IPConfig.APPLY_PHOTO_CHECK);
        params.setMultipart(true);
        params.addBodyParameter("username", userName);
        params.addBodyParameter("timestamp", timestamp);
        params.addBodyParameter("token", token);
        params.addBodyParameter("store_id", store_id);
        if (selectedPos == -1) {
            MessageShow.initErrorDialog(UpDataDocumentsDataActivity.this, "点击上传图片", 0);
            return;
        }
        params.addBodyParameter("type", plist.get(selectedPos).getP_type() + "");
        bottomDialog.dismiss();
        File file = new File(s);
        params.addBodyParameter("thumb", file);
        initPbDialog();
        new Thread(new MyRunnable(UpDataDocumentsDataActivity.this, params, handler, 2)).start();
    }

    /**
     *
     */
    public void onSaveInfo(View view) {
        UpDataDocumentsDataActivity.this.finish();
    }

    private void initPbDialog() {
        pbDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.prog, null);
        pb = contentView.findViewById(R.id.pb);
        bfb = contentView.findViewById(R.id.bfb);
        pbDialog.setContentView(contentView);
        bottomDialog.setContentView(contentView);
        Window dialogWindow = bottomDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        //注意一定要是MATCH_PARENT
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        pbDialog.show();
    }
}
