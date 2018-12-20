package com.zerone.intodata;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.xutils.x;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-11-10.
 */

public class MyApplication extends Application {
    private static final String TAG = "JIGUANG-Example";
    //用于存放所有启动的Activity的集合 便于删除
    private List<Activity> activityList;
    public static RequestQueue queues;
    @Override
    public void onCreate() {
        super.onCreate();
        activityList=new ArrayList<Activity>();
        queues = Volley.newRequestQueue(getApplicationContext());
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
    }

    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!activityList.contains(activity)) {
            activityList.add(activity);//把当前Activity添加到集合中
        }
    }
    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
        //判断当前集合中存在该Activity
        if (activityList.contains(activity)) {
            activityList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public static RequestQueue getQueues() {
        return queues;
    }
}
