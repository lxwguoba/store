package com.zerone.intodata;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zerone.intodata.fragment.IntoDataFragment;
import com.zerone.intodata.fragment.IntoDataListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppActivity {

    @Bind(R.id.viewpage)
    ViewPager viewpage;
    @Bind(R.id.select_into_data)
    RadioButton selectIntoData;
    @Bind(R.id.select_into_list)
    RadioButton selectIntoList;
    @Bind(R.id.select_group)
    RadioGroup selectGroup;

    private List<Fragment> list = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragemnt();
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), list);
        viewpage.setAdapter(adapter);

        selectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.select_into_data:
                        viewpage.setCurrentItem(0);
                        break;
                    case R.id.select_into_list:
                        viewpage.setCurrentItem(1);
                        break;
                }
            }
        });
        viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        selectIntoData.setChecked(true);
                        break;
                    case 1:
                        selectIntoList.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * fragment的初始化
     */
    private void initFragemnt() {
        IntoDataFragment intoDataFragment = new IntoDataFragment();
        IntoDataListFragment intoDataListFragment = new IntoDataListFragment();
        list.add(intoDataFragment);
        list.add(intoDataListFragment);
    }

    /**
     * 对话框的初始化
     */
    private void initdialog() {
        dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_out_system, null);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        //注意一定要是MATCH_PARENT
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        initdialog();
    }

    /**
     * 确定退出
     *
     * @param view
     */
    public void onSuerOut(View view) {
        this.removeALLActivity();
    }

    /**
     * 关闭退出页面
     *
     * @param view
     */
    public void onBack(View view) {
        if (dialog != null) {
            dialog.dismiss();
            ;
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> list;

        public PagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
