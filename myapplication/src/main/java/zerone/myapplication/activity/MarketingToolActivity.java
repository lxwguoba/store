package zerone.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;

/**
 * Created by 刘兴文 on 2018-11-26.
 * 营销工具管理
 */

public class MarketingToolActivity extends BaseAppActivity {
    private boolean openlean = true;
    private boolean aopenlean = true;
    private LinearLayout showPositionLayout;
    private LinearLayout showPosterApplyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketingtool);
        intiView();
    }

    /**
     *
     */
    private void intiView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("营销工具管理");
        showPositionLayout = (LinearLayout) findViewById(R.id.showPositionLayout);
        showPosterApplyLayout = (LinearLayout) findViewById(R.id.showPosterApplyLayout);
    }

    /**
     *
     */
    public void onH5ApplyFor(View view) {
        Intent intent = new Intent(this, H5ApplyForActivity.class);
        startActivity(intent);
    }

    public void onH5ApplyForList(View view) {
        Intent intent = new Intent(this, PostersListActivity.class);
        startActivity(intent);
    }

    /**
     * 添加优惠卷
     *
     * @param view
     */
    public void onAddHYJ(View view) {
        Intent intent = new Intent(this, AddCouponsActivity.class);
        startActivity(intent);
    }

    /**
     * 优惠卷列表
     *
     * @param view
     */
    public void onOpenHYJList(View view) {
        Intent intent = new Intent(this, CouponsListActivity.class);
        startActivity(intent);
    }

    /**
     * 打开下拉列表
     *
     * @param view
     */
    public void onOpenHYJ(View view) {
        if (openlean) {
            showPositionLayout.setVisibility(View.VISIBLE);
            openlean = false;
        } else {
            showPositionLayout.setVisibility(View.GONE);
            openlean = true;
        }
    }

    public void onShowApplyForAction(View view) {
        if (aopenlean) {
            showPosterApplyLayout.setVisibility(View.VISIBLE);
            aopenlean = false;
        } else {
            showPosterApplyLayout.setVisibility(View.GONE);
            aopenlean = true;
        }
    }
}
