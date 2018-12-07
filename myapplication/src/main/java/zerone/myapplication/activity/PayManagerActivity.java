package zerone.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;

/**
 * Created by Administrator on 2018-11-19.
 */

public class PayManagerActivity extends BaseAppActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymanager);
        initView();
    }

    public void onBackActivity(View view) {
        this.finish();
    }

    private void initView() {
        TextView titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("财务管理");
    }

    /**
     * 店铺流水查看
     *
     * @param view
     */
    public void onLookStoreList(View view) {
        Intent intent = new Intent(this, PayStoreListActivity.class);
        startActivity(intent);
    }

    /**
     *
     * 提交收款资料
     * @param view
     *
     */
    public void  onSubmitCollectionData(View view){
        Intent intent = new Intent(this, CollectionInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 查看收款二维码或者是创建二维码
     * @param view
     */
    public void onQrCodeLookOrCreate(View view){
        Intent intent = new Intent(this, CollectionQrCodeActivity.class);
        startActivity(intent);
    }

}
