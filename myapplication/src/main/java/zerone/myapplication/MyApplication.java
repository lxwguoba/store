package zerone.myapplication;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018-11-10.
 */

public class MyApplication extends Application {
    private static final String TAG = "JIGUANG-Example";
    public static RequestQueue queues;
    //用于存放所有启动的Activity的集合 便于删除
    private List<Activity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush         // 初始化 JPush
        String  registrationID = JPushInterface.getRegistrationID(this);
        queues = Volley.newRequestQueue(getApplicationContext());
        activityList=new ArrayList<Activity>();
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
        Log.i("URL",registrationID);


    }
    public static RequestQueue getQueues() {
        return queues;
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
}
