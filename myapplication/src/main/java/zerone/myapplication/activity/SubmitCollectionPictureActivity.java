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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.config.IPConfig;
import zerone.myapplication.domain.ImageBean;
import zerone.myapplication.utils.CreateTokenUtils;
import zerone.myapplication.utils.LoadingUtils;
import zerone.myapplication.utils.NetUtils;
import zerone.myapplication.utils.UpLoadUtils;
import zerone.myapplication.view.BarProgress;
import zerone.myapplication.view.GuoBaProgressBar;

/**
 * Created by 刘兴文 on 2018-12-01.
 * 收款资料-图片
 */

public class SubmitCollectionPictureActivity extends BaseAppActivity implements View.OnClickListener {
    //使用照相机拍照获取图片
    public static final int TAKE_PHOTO_CODE = 1;
    //使用相册中的图片
    public static final int SELECT_PIC_CODE = 2;
    //图片裁剪
    private static final int PHOTO_CROP_CODE = 3;
    @Bind(R.id.p_license)
    ImageView pLicense;
    @Bind(R.id.p_idcard_pos)
    ImageView pIdcardPos;
    @Bind(R.id.p_idcard_oth)
    ImageView pIdcardOth;
    @Bind(R.id.p_opening_permit)
    ImageView pOpeningPermit;
    @Bind(R.id.p_bank_card)
    ImageView pBankCard;
    @Bind(R.id.p_door_photo)
    ImageView pDoorPhoto;
    @Bind(R.id.p_shop_photo)
    ImageView pShopPhoto;
    @Bind(R.id.p_cashier_photo)
    ImageView pCashierPhoto;
    @Bind(R.id.p_increment_agree)
    ImageView pIncrementAgree;

    private Button take_photo_btn;
    private Button select_photo_btn;
    //定义图片的Uri
    private Uri photoUri;
    //图片文件路径
    private String picPath;
    private Button quxiao;
    private Dialog bottomDialog;
    private File file;
    private GuoBaProgressBar round_flikerbar;
    private ZLoadingDialog zLoadingDialog;
    private int ptype = -1;
    private BarProgress pb;
    private TextView bfb;
    private Dialog pbDialog;
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
                        String catJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(catJson);
                        int code = jsonObject.getInt("status");
                        if (code == 1) {
                            String list = jsonObject.getString("data");
                            Gson gson = new Gson();
                            ImageBean imagesBean = gson.fromJson(list, ImageBean.class);
                            Log.i("URL", imagesBean.toString());
                            if (imagesBean != null) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(imagesBean.getLicense()).error(R.mipmap.login_head).into(pLicense);
                                Glide.with(SubmitCollectionPictureActivity.this).load(imagesBean.getIdcard_pos()).error(R.mipmap.login_head).into(pIdcardPos);
                                Glide.with(SubmitCollectionPictureActivity.this).load(imagesBean.getIdcard_oth()).error(R.mipmap.login_head).into(pIdcardOth);
                                Glide.with(SubmitCollectionPictureActivity.this).load(imagesBean.getOpening_permit()).error(R.mipmap.login_head).into(pOpeningPermit);
                                Glide.with(SubmitCollectionPictureActivity.this).load(imagesBean.getBank_card()).error(R.mipmap.login_head).into(pBankCard);
                                Glide.with(SubmitCollectionPictureActivity.this).load(imagesBean.getDoor_photo()).error(R.mipmap.login_head).into(pDoorPhoto);
                                Glide.with(SubmitCollectionPictureActivity.this).load(imagesBean.getShop_photo()).error(R.mipmap.login_head).into(pShopPhoto);
                                Glide.with(SubmitCollectionPictureActivity.this).load(imagesBean.getCashier_photo()).error(R.mipmap.login_head).into(pCashierPhoto);
                                Glide.with(SubmitCollectionPictureActivity.this).load(imagesBean.getIncrement_agree()).error(R.mipmap.login_head).into(pIncrementAgree);
                                if (imagesBean.getIs_apply() == 0) {
                                    comfigBtn.setText("提交申请");
                                } else if (imagesBean.getIs_apply() == 1) {
                                    comfigBtn.setText("申请中");
                                    comfigBtn.setEnabled(false);
                                    comfigBtn.setBackgroundResource(R.drawable.btn_normal_gree);
                                } else if (imagesBean.getIs_apply() == 2) {
                                    comfigBtn.setText("完成");
                                    comfigBtn.setEnabled(false);
                                    comfigBtn.setBackgroundResource(R.drawable.btn_normal_gree);
                                } else if (imagesBean.getIs_apply() == 3) {
                                    comfigBtn.setText("重新提交");
                                }
                            }
                        } else if (code == 0) {

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
                        String upJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(upJson);
                        int stauts = jsonObject.getInt("status");
                        if (stauts == 1) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            int t = data.getInt("type");
                            if (t == 1) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(data.getString("img")).error(R.mipmap.login_head).into(pLicense);
                            } else if (t == 2) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(data.getString("img")).error(R.mipmap.login_head).into(pIdcardPos);
                            } else if (t == 3) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(data.getString("img")).error(R.mipmap.login_head).into(pIdcardOth);
                            } else if (t == 4) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(data.getString("img")).error(R.mipmap.login_head).into(pOpeningPermit);
                            } else if (t == 5) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(data.getString("img")).error(R.mipmap.login_head).into(pBankCard);
                            } else if (t == 6) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(data.getString("img")).error(R.mipmap.login_head).into(pDoorPhoto);
                            } else if (t == 7) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(data.getString("img")).error(R.mipmap.login_head).into(pShopPhoto);
                            } else if (t == 8) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(data.getString("img")).error(R.mipmap.login_head).into(pCashierPhoto);
                            } else if (t == 9) {
                                Glide.with(SubmitCollectionPictureActivity.this).load(data.getString("img")).error(R.mipmap.login_head).into(pIncrementAgree);
                            }
                            tosatMessage("上传成功");
                        } else {
                            tosatMessage("上传不成功,请重试");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (zLoadingDialog != null) {
                            zLoadingDialog.dismiss();
                        }
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
    private Button comfigBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectionpicture);
        ButterKnife.bind(this);
        initView();
        initGetPictureData();
    }

    public void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("基本资料-照片");
        comfigBtn = findViewById(R.id.comfig_btn);
    }

    public void onSaveInfo(View view) {
        this.finish();
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

    private void initPbDialog() {
        pbDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.prog, null);
        pb = contentView.findViewById(R.id.pb);
        bfb = contentView.findViewById(R.id.bfb);
        pbDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        pbDialog.getWindow().setGravity(Gravity.CENTER);
        pbDialog.show();
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
//                    startPhotoZoom(photoUri, PHOTO_CROP_CODE);
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
//                    startPhotoZoom(photoUri, PHOTO_CROP_CODE);
                } else {
                    Toast.makeText(this, "图片选择失败", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == PHOTO_CROP_CODE) {
                if (photoUri != null) {
                    bottomDialog.dismiss();
                    Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                    Log.i("URL", "图片路径：" + picPath);
                    if (bitmap != null) {
                        //这里可以把图片进行上传到服务器操作
                    }
                    ss(picPath);
                }
            }
        }
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

    public void ss(String s) {
        bottomDialog.dismiss();
        file = new File(s);
        initPbDialog();
        new Thread(new MyRunnable()).start();
    }

    /**
     * 获取图片
     */
    private void initGetPictureData() {
        if (zLoadingDialog == null) {
            zLoadingDialog = LoadingUtils.openLoading(this, "数据获取中···");
        }
        zLoadingDialog.show();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateTokenUtils.createToken(this, timestamp);
        Map<String, String> parmar = new HashMap<>();
        parmar.put("user_id", shopInfoBean.getUser_id() + "");
        parmar.put("timestamp", timestamp);
        parmar.put("token", token);
        parmar.put("store_id", shopInfoBean.getStore_id() + "");
        NetUtils.netWorkByMethodPost(this, parmar, IPConfig.APPLY_IMG, handler, 1);
    }

    @OnClick({R.id.p_license, R.id.p_idcard_pos, R.id.p_idcard_oth, R.id.p_opening_permit, R.id.p_bank_card, R.id.p_door_photo, R.id.p_shop_photo, R.id.p_cashier_photo, R.id.p_increment_agree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.p_license:
//                1-营业执照,2-法人身份证正面,3-法人身份证反面,4-开户许可证,5-银行卡正面照片,6-店铺门头照,7-店内照,8-收银台,9-增值协议正面照
                ptype = 1;
                initDialog();
                break;
            case R.id.p_idcard_pos:
                ptype = 2;
                initDialog();
                break;
            case R.id.p_idcard_oth:
                ptype = 3;
                initDialog();
                break;
            case R.id.p_opening_permit:
                ptype = 4;
                initDialog();
                break;
            case R.id.p_bank_card:
                ptype = 5;
                initDialog();
                break;
            case R.id.p_door_photo:
                ptype = 6;
                initDialog();
                break;
            case R.id.p_shop_photo:
                ptype = 7;
                initDialog();
                break;
            case R.id.p_cashier_photo:
                ptype = 8;
                initDialog();
                break;
            case R.id.p_increment_agree:
                ptype = 9;
                initDialog();
                break;
        }
    }

    public class MyRunnable implements Runnable {

        @Override
        public void run() {
            String timestamp = System.currentTimeMillis() + "";
            String token = CreateTokenUtils.createToken(SubmitCollectionPictureActivity.this, timestamp);
            RequestParams params = new RequestParams(IPConfig.APPLY_IMG_CHECK);
            params.setMultipart(true);
            params.addBodyParameter("user_id", shopInfoBean.getUser_id() + "");
            params.addBodyParameter("timestamp", timestamp);
            params.addBodyParameter("token", token);
            params.addBodyParameter("store_id", shopInfoBean.getStore_id() + "");
            params.addBodyParameter("type", ptype + "");
            params.addBodyParameter("thumb", file);
            UpLoadUtils.xuploadPro(params, handler, 2);
        }
    }
}
