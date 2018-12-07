package zerone.myapplication.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.EditShopInfoBean;
import zerone.myapplication.interfaces.EditShopInfo;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.MyRunnable;
import zerone.myapplication.utils.NetUtils;

/**
 * Created by Administrator on 2018-11-23.
 * 商家资料编辑
 */

public class EditShopInfoActvitiy extends BaseAppActivity implements EditShopInfo, View.OnClickListener {

    //=========================tupian====================================
    //使用照相机拍照获取图片
    public static final int TAKE_PHOTO_CODE = 1;
    //使用相册中的图片
    public static final int SELECT_PIC_CODE = 2;
    //图片裁剪
    private static final int PHOTO_CROP_CODE = 3;
    private String addresinfo;
    private TextView address;
    private Button take_photo_btn;
    private Button select_photo_btn;
    private ImageView photo_iv;
    private LinearLayout photo_iv_layout;
    private ImageView photo_miv;
    private LinearLayout photo_iv_mlayout;
    //定义图片的Uri
    private Uri photoUri;
    //图片文件路径
    private String picPath;
    private Button quxiao;
    private Dialog bottomDialog;
    private boolean mlean = false;
    private ZLoadingDialog zLoadingDialog;
    private TextView shopName;
    private TextView telePhone;
    private TextView phone;
    private TextView username;
    private EditShopInfoBean editShopInfoBean;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (zLoadingDialog != null) {
                        zLoadingDialog.dismiss();
                    }
                    Toast.makeText(EditShopInfoActvitiy.this, "没有网络或者是网络出错了，请检查!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        String editJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(editJson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            String list = jsonObject.getString("data");
                            Gson gson = new Gson();
                            editShopInfoBean = gson.fromJson(list, EditShopInfoBean.class);
                            shopName.setText(editShopInfoBean.getName());
                            phone.setText(editShopInfoBean.getMobile());
                            telePhone.setText(editShopInfoBean.getKf_mobile());
                            username.setText(editShopInfoBean.getRealname());
                            if (editShopInfoBean.getLogo() != null) {
                                photo_iv_layout.setVisibility(View.VISIBLE);
                                Glide.with(EditShopInfoActvitiy.this).load(editShopInfoBean.getLogo()).into(photo_iv);
                            }
                            if (editShopInfoBean.getTheme_img() != null) {
                                photo_iv_mlayout.setVisibility(View.VISIBLE);
                                Glide.with(EditShopInfoActvitiy.this).load(editShopInfoBean.getTheme_img()).into(photo_miv);
                            }
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
                        String upinfo = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(upinfo);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                          tosatMessage("修改成功");
                            EditShopInfoActvitiy.this.finish();
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
    private String logoPath = "";
    private String menDianPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editshopinfo);
        intiView();
        getShopInfo();
    }

    private void getShopInfo() {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.SHOPINFO, handler, 1);
    }

    private void intiView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("店铺信息");
        shopName = findViewById(R.id.shopName);
        address = (TextView) findViewById(R.id.address);
        telePhone = findViewById(R.id.telePhone);
        phone = findViewById(R.id.phone);
        username = (TextView) findViewById(R.id.user_name);

        photo_iv_layout = (LinearLayout) findViewById(R.id.photo_iv_layout);
        photo_iv = (ImageView) findViewById(R.id.photo_iv);
        photo_iv_mlayout = (LinearLayout) findViewById(R.id.photo_iv_mlayout);
        photo_miv = (ImageView) findViewById(R.id.photo_miv);
    }

    public void onBackActivity(View view) {
        this.finish();
    }

    /**
     * 修改地址的方法
     *
     * @param view
     */
    public void onAddressUpData(View view) {
        Intent intent = new Intent(this, ChangeAddressActivity.class);
        startActivityForResult(intent, 600);
    }

    /**
     * 保存修改信息的方法
     *
     * @param view
     */
    public void onSaveChangeInfo(View view) {
        String storeName = shopName.getText().toString().trim();
        String kfphone = telePhone.getText().toString().trim();
        String dh = phone.getText().toString().trim();
        String fr = username.getText().toString().trim();
        if (!(kfphone != null && kfphone.length() > 0)) {
            tosatMessage("客服电话不能为空");
            return;
        }
        if (!(storeName != null && storeName.length() > 0)) {
            tosatMessage("店铺名称不能为空");
            return;
        }
        if (!(dh != null && dh.length() > 0)) {
            tosatMessage("电话不能为空");
            return;
        }
        if (!(fr != null && fr.length() > 0)) {
            tosatMessage("法人不能为空");
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        RequestParams params = new RequestParams(IPConfig.STORE_EDIT);
        params.setMultipart(true);
        params.addBodyParameter("user_id", shopInfoBean.getUser_id() + "");
        params.addBodyParameter("timestamp", timestamp);
        params.addBodyParameter("token", token);
        params.addBodyParameter("store_id", shopInfoBean.getStore_id() + "");
        //店铺名
        params.addBodyParameter("name", storeName);
        //手机号
        params.addBodyParameter("mobile", dh);
        //负责人姓名
        params.addBodyParameter("realname", fr);
        //客服电话
        params.addBodyParameter("kf_mobile", kfphone);
        if (logoPath.length() > 0) {
            params.addBodyParameter("logo", new File(logoPath));
        }
        if (menDianPath.length() > 0) {
            params.addBodyParameter("theme_img", new File(menDianPath));
        }
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "店铺信息修改中···");
        }
        zLoadingDialog.show();
        new Thread(new MyRunnable(params, handler, 2)).start();
    }

    /**
     * 修改门店LOGO的方法
     *
     * @param view
     */
    public void onChangeLOGO(View view) {
        mlean = false;
        initDialog();
    }

    /**
     * 修改门店头像的方法
     *
     * @param view
     */
    public void onChangeShopPhoto(View view) {
        mlean = true;
        initDialog();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            if (requestCode == 600) {
                addresinfo = data.getStringExtra("address");
                address.setText(addresinfo);
            }
        } else if (resultCode == Activity.RESULT_OK) {
            //从相册取图片，有些手机有异常情况，请注意
            if (requestCode == SELECT_PIC_CODE) {
                if (null != data && null != data.getData()) {
                    photoUri = data.getData();
                    picPath = uriToFilePath(photoUri);
                    if (mlean) {
                        menDianPath = picPath;
                        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                        if (bitmap != null) {
                            //这里可以把图片进行上传到服务器操作
                            photo_iv_mlayout.setVisibility(View.VISIBLE);
                            photo_miv.setImageBitmap(bitmap);
                        }
                    } else {
                        startPhotoZoom(photoUri, PHOTO_CROP_CODE);
                    }

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
                    if (mlean) {
                        menDianPath = picPath;
                        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                        if (bitmap != null) {
                            //这里可以把图片进行上传到服务器操作
                            photo_iv_mlayout.setVisibility(View.VISIBLE);
                            photo_miv.setImageBitmap(bitmap);
                        }
                    } else {
                        //这个是需要裁剪的
                        startPhotoZoom(photoUri, PHOTO_CROP_CODE);
                    }
                } else {
                    Toast.makeText(this, "图片选择失败", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == PHOTO_CROP_CODE) {
                if (photoUri != null) {
                    bottomDialog.dismiss();
                    Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                    if (mlean) {
                        //门店照地路径
                        menDianPath = picPath;
                    } else {
                        //门店logo的路径
                        logoPath = picPath;
                    }
                    if (bitmap != null) {
                        //这里可以把图片进行上传到服务器操作
                        photo_iv_layout.setVisibility(View.VISIBLE);
                        photo_iv.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    private void initDialog() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_photoandpicture, null);
        select_photo_btn = contentView.findViewById(R.id.select_photo_btn);
        select_photo_btn.setOnClickListener(this);
        take_photo_btn = contentView.findViewById(R.id.take_photo_btn);
        take_photo_btn.setOnClickListener(this);
        quxiao = contentView.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(this);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
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
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
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

    /**
     * 图片裁剪
     *
     * @param uri
     * @param REQUE_CODE_CROP
     */
    private void startPhotoZoom(Uri uri, int REQUE_CODE_CROP) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // aspectX aspectY 是宽高的比例，根据自己情况修改
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高像素
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        //设置返回的uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //设置为不返回数据
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUE_CODE_CROP);
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

}
