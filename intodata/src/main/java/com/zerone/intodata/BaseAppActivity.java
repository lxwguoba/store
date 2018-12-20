package com.zerone.intodata;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-11-17.
 */

public class BaseAppActivity extends AppCompatActivity {
    /**
     * 360dp宽度的设计搞
     *
     * @param activity
     * @param application
     */
    private static float sNoncompatDensity;
    private static float sNoncompatScaleDensity;
    public BaseAppActivity oContext;
    private MyApplication application;
    //状态栏高度
    private int statusBarHight = 0;

    private static void adaptScreen(final Activity activity,
                                    final float sizeInDp,
                                    final boolean isVerticalSlide, final Application application) {
        final DisplayMetrics appDm = application.getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();

        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDm.density;
            sNoncompatScaleDensity = appDm.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        if (isVerticalSlide) {
            activityDm.density = activityDm.widthPixels / sizeInDp;
        } else {
            activityDm.density = activityDm.heightPixels / sizeInDp;
        }
        activityDm.scaledDensity = activityDm.density * (appDm.scaledDensity / appDm.density);
        activityDm.densityDpi = (int) (160 * activityDm.density);

    }









    private static void setCustomDensity(Activity activity, final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaleDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        final float targetDensity = appDisplayMetrics.widthPixels / 360;
        final int targetDensityDpi = (int) (160 * targetDensity);
        appDisplayMetrics.density = appDisplayMetrics.scaledDensity = targetDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = activityDisplayMetrics.scaledDensity = targetDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除title
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#0eb468"));
        if (application == null) {
            // 得到Application对象
            application = (MyApplication) getApplication();
        }
        // 把当前的上下文对象赋值给BaseActivity
        oContext = this;
        statusBarHight = getTitleHight();
        // 调用添加方法
        addActivity();
        initPermission();
        /**
         * 按照设计搞的400dp去做app
         */
        adaptScreen(this, 400, true, application);

    }

    /**
     * 添加Activity方法
     */
    public void addActivity() {
        //调用myApplication的添加Activity方法
        application.addActivity(oContext);
    }

    /**
     * 销毁当个Activity方法
     */
    public void removeActivity() {
        application.removeActivity(oContext);// 调用myApplication的销毁单个Activity方法
    }

    /**
     * 销毁所有Activity方法
     */
    public void removeALLActivity() {
        // 调用myApplication的销毁所有Activity方法
        application.removeALLActivity();
    }

    /**
     * 把Toast定义成一个方法  可以重复使用，使用时只需要传入需要提示的内容即可
     *
     * @param text
     */
    public void showToast(String text) {
        Toast.makeText(oContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 返回上一级
     */
    public void onBackActivity(View view) {
        this.finish();
    }

    public void tosatMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取状态栏的高度
     *
     * @return
     */
    private int getTitleHight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initPermission() {
        String[] permissions = {
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_SETTINGS,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.CHANGE_WIFI_STATE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_CALENDAR,
                android.Manifest.permission.WRITE_CALENDAR,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
        };
        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }
    }
}