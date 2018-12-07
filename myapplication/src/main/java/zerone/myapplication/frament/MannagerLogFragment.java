package zerone.myapplication.frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import zerone.myapplication.R;
import zerone.myapplication.adapter.fragment.MyViewPageAdapter;
import zerone.myapplication.frament.log.DealLogFragment;
import zerone.myapplication.frament.log.LoginLogFragment;
import zerone.myapplication.utils.TabLayoutUtils;

/**
 * Created by Administrator on 2018-11-15.
 */

public class MannagerLogFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null){
            view = inflater.inflate(R.layout.fragment_logmanager, container, false);
        }
        createTablaout();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void createTablaout(){
        TabLayout tl_coupon  =(TabLayout)view.findViewById(R.id.tl_coupon);
        TabLayoutUtils.setTabWidth(tl_coupon,40);
        ViewPager vp_coupon =(ViewPager)view.findViewById(R.id.vp_coupon);
        ArrayList<String> titleDatas=new ArrayList<>();
        titleDatas.add("登录日志");
        titleDatas.add("操作日志");
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new LoginLogFragment());
        fragmentList.add(new DealLogFragment());
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getChildFragmentManager(), titleDatas, fragmentList);
        vp_coupon.setAdapter(myViewPageAdapter);
        tl_coupon.setupWithViewPager(vp_coupon);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
