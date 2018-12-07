package zerone.myapplication.frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import zerone.myapplication.R;
import zerone.myapplication.activity.AddSalesclerkActivity;
import zerone.myapplication.activity.ChangeLoginPassWordActivity;
import zerone.myapplication.activity.SalesclerLIstActivity;
import zerone.myapplication.activity.SetSafetyassActivity;

/**
 * Created by Administrator on 2018-11-15.
 */

public class AccountManagerFragment extends Fragment {
    private View view;
    private RelativeLayout cpwd;
    private RelativeLayout setSalfPwd;
    private RelativeLayout addnewaccount;
    private RelativeLayout accountlist;
    private boolean lean = true;
    private LinearLayout showHelperHeand;
    private RelativeLayout onOpenHelperHeand;
    private ImageView img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_accountmanager, container, false);
        }
        initView(view);
        initListenner();
        return view;
    }

    /**
     * view的初始化
     *
     * @param view
     */
    private void initView(View view) {
        cpwd = (RelativeLayout) view.findViewById(R.id.changePwd);
        showHelperHeand = view.findViewById(R.id.showHelperHeand);
        setSalfPwd = (RelativeLayout) view.findViewById(R.id.setSalfPwd);
        addnewaccount = (RelativeLayout) view.findViewById(R.id.addnewaccount);
        accountlist = (RelativeLayout) view.findViewById(R.id.accountlist);
        onOpenHelperHeand = (RelativeLayout) view.findViewById(R.id.onOpenHelperHeand);
        img = (ImageView) view.findViewById(R.id.img);
    }

    public void initListenner() {
        cpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangeLoginPassWordActivity.class));
            }
        });
        setSalfPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SetSafetyassActivity.class));
            }
        });
        addnewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddSalesclerkActivity.class));
            }
        });
        accountlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SalesclerLIstActivity.class));
            }
        });
        onOpenHelperHeand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lean) {
                    showHelperHeand.setVisibility(View.VISIBLE);
                    showHelperHeand.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate));
                    rotateAnim();
                    lean = !lean;
                } else {
                    showHelperHeand.setVisibility(View.GONE);
                    showHelperHeand.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translates));
                    rotateCloseAnim();
                    lean = !lean;
                }
            }
        });
    }

    private void rotateCloseAnim() {
        /**
         * float fromDegrees,
         * float toDegrees,
         * int pivotXType,
         * float pivotXValue,
         * int pivotYType,
         * float pivotYValue
         */
        Animation anim =new RotateAnimation(90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(300); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        img.startAnimation(anim);
    }

    /**
     * 旋转动画
     */
    public void rotateAnim() {
        Animation anim =new RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(300); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        img.startAnimation(anim);
    }
}
