package zerone.myapplication.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.utils.Utils;
import zerone.myapplication.view.MyProgressBar;

/**
 * Created by 刘兴文 on 2018-12-06.
 */

public class AboutUsActivity extends BaseAppActivity {

    private Dialog dialog;
    private MyProgressBar round_flikerbar;
    private TextView version;
    private TextView content;
    private Button left;
    private Button right;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    int pro = (int) msg.obj;
                    round_flikerbar.setProgress(pro);
                    if (pro == 100) {
                        right.setEnabled(true);
                        round_flikerbar.finishLoad();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                    break;
                case 4:
                    break;
            }
        }
    };
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        initView();
        showDialog();

    }

    public void onGOTOCompanyInfo(View view) {

    }

    private void initView() {
        try {
            TextView ver = findViewById(R.id.version);
            String version = Utils.getVersionName(this);
            ver.setText(version + "版本");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCheckVersion(View view) {
        getVersionInfoFromServer();
    }

    /**
     * 从服务器获取版本最新的版本信息
     */
    private void getVersionInfoFromServer() {
        //模拟从服务器获取信息  模拟更新王者荣耀
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        sharedPreferences.edit().putString("url", "");
        sharedPreferences.edit().putString("path", "");
        //getExternalCacheDir获取到的路径 为系统为app分配的内存 卸载app后 该目录下的资源也会删除
        //比较版本信息
        try {
            int result = Utils.compareVersion(Utils.getVersionName(this), "1.0.1");
            if (result == -1) {//不是最新版本
                round_flikerbar.setVisibility(View.VISIBLE);
                dialog.show();
            } else {
                Toast.makeText(AboutUsActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        dialog = new Dialog(AboutUsActivity.this);
        LayoutInflater inflater = (LayoutInflater) AboutUsActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.version_update, null, false);
        round_flikerbar = (MyProgressBar) view.findViewById(R.id.round_flikerbar);
        version = (TextView) view.findViewById(R.id.version);
        content = (TextView) view.findViewById(R.id.content);
        left = (Button) view.findViewById(R.id.left);
        right = (Button) view.findViewById(R.id.right);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content.setText(Html.fromHtml("更新中", Html.FROM_HTML_MODE_LEGACY));

        } else {

            content.setText(Html.fromHtml("更新中"));

        }

        version.setText("版本号：" + "1.0.1");


        content.setMovementMethod(LinkMovementMethod.getInstance());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right.setEnabled(false);
                new Thread(new MyRunnable()).start();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        //dialogWindow.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager wm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);
        lp.width = wm.getDefaultDisplay().getWidth() / 10 * 9;
        dialogWindow.setAttributes(lp);
    }


    private void updateApk() {
        /**
         * 上传多张图片到服务器 并接受数据内容
         *
         * @param params 参数的封装
         */
        RequestParams params = new RequestParams("https://ctwxl.com/xwapp/sj.apk");
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                //下载成功 打开安装界面
                installApk(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {


            }

            @Override
            public void onFinished() {


            }


            @Override
            public void onWaiting() {


            }

            @Override
            public void onStarted() {


            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (isDownloading) {
                    int s = (int) (((int) current / (float) total) * 100);
                    Message message = new Message();
                    message.what = 3;
                    message.obj = s;
                    Log.i("URL", s + "");
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 安装下载的新版本
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = Uri.fromFile(file);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        this.startActivity(intent);
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            updateApk();
        }
    }
}
