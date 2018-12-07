package zerone.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;

import zerone.myapplication.activity.ProductListActivity;
import zerone.myapplication.domain.ShopInfoBean;
import zerone.myapplication.utils.JsonToUser;

/**
 * Created by Administrator on 2018-11-17.
 */

public class BaseAppActivity extends AppCompatActivity {
    public ShopInfoBean shopInfoBean;
    private MyApplication application;
    private BaseAppActivity oContext;
    //状态栏高度
    private int statusBarHight = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#0eb468"));
        if (application == null) {
            // 得到Application对象
            application = (MyApplication) getApplication();
        }
        shopInfoBean = JsonToUser.getShopInfoBean(this);
        // 把当前的上下文对象赋值给BaseActivity
        oContext = this;
        statusBarHight = getTitleHight();
        // 调用添加方法
        addActivity();

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
}