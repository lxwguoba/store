package zerone.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import zerone.myapplication.activity.AboutUsActivity;
import zerone.myapplication.activity.DynamicIssueActivity;
import zerone.myapplication.activity.EditShopInfoActvitiy;
import zerone.myapplication.activity.ConsumerstActivity;
import zerone.myapplication.activity.MarketingToolActivity;
import zerone.myapplication.activity.PayManagerActivity;
import zerone.myapplication.activity.ProductManagerActivity;
import zerone.myapplication.adapter.ViewPagerAdapter;
import zerone.myapplication.domain.ShopInfoBean;
import zerone.myapplication.frament.AccountManagerFragment;
import zerone.myapplication.frament.DataStatisticsFragment;
import zerone.myapplication.frament.MannagerLogFragment;
import zerone.myapplication.utils.BottomNavigationViewHelper;
import zerone.myapplication.utils.JsonToUser;

public class Main_Activity extends BaseAppActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static Boolean isExit = false;

    private DrawerLayout drawer;
    private ShopInfoBean shopInfoBean;
    private View headerView;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.nav_data_statistics:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_manage_account:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_manage_log:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };
    private TextView shopName;
    private TextView username;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        shopInfoBean = JsonToUser.getShopInfoBean(this);
        init();
        intiView();
    }

    public void onOpenDraLayout(View view) {
        drawer.openDrawer(Gravity.LEFT);
    }

    private void intiView() {
        shopName = (TextView) headerView.findViewById(R.id.shopName);
        username = (TextView) headerView.findViewById(R.id.user_name);
        address = (TextView) headerView.findViewById(R.id.address);
        shopName.setText(shopInfoBean.getStore_name());
        username.setText(shopInfoBean.getUser_name() + " " + shopInfoBean.getMobile());
        address.setText(shopInfoBean.getStore_address());
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        List<Fragment> list = new ArrayList<>();
        list.add(new DataStatisticsFragment());
        list.add(new AccountManagerFragment());
        list.add(new MannagerLogFragment());
        viewPagerAdapter.setList(list);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_manager_system) {
            startActivity(new Intent(Main_Activity.this, EditShopInfoActvitiy.class));
        } else if (id == R.id.nav_manage_product) {
            startActivity(new Intent(Main_Activity.this, ProductManagerActivity.class));
        } else if (id == R.id.nav_manage_pay) {
            startActivity(new Intent(Main_Activity.this, PayManagerActivity.class));
        } else if (id == R.id.nav_manage_attention) {
            startActivity(new Intent(Main_Activity.this, DynamicIssueActivity.class));
        } else if (id == R.id.nav_manage_yxgj) {
            startActivity(new Intent(Main_Activity.this, MarketingToolActivity.class));
        } else if (id == R.id.nav_manage_vip) {
            startActivity(new Intent(Main_Activity.this, ConsumerstActivity.class));
        }else if (id==R.id.nav_about_me){
            startActivity(new Intent(Main_Activity.this, AboutUsActivity.class));
        }else if (id==R.id.nav_out_system){

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        JPushInterface.init(getApplicationContext());
    }

    @Override
    protected void onResume() {
        //获取数据
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 监听手机按键的回调
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //按下了返回键 提示用户
                exitBy2Click();
                break;
        }
        return true;
    }

    /**
     * 在1s内联系点击两次 才能推出
     */
    public void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
        }
    }
}
