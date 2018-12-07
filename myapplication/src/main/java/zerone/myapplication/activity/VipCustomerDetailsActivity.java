package zerone.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;

/**
 * Created by Administrator on 2018-11-21.
 */

public class VipCustomerDetailsActivity extends BaseAppActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vipcustomerinfo);
        initView();
    }
    public void onBackActivity(View view) {
        this.finish();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        //设置标题
        titleName.setText("会员信息");
    }
    public void onSaveInfo(View view){

    }
}
