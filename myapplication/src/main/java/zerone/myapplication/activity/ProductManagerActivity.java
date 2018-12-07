package zerone.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;

/**
 * Created by Administrator on 2018-11-16.
 */

public class ProductManagerActivity extends BaseAppActivity {

    private RelativeLayout addcatering;
    private RelativeLayout cateringlist;
    private RelativeLayout addproduct;
    private RelativeLayout productlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productmanager);
        initView();
        initListennering();
    }


    /**
     *
     */
    private void initView() {
        TextView titleName= (TextView) findViewById(R.id.titleName);
        titleName.setText("商品管理");
        addcatering = (RelativeLayout) findViewById(R.id.addcatering);
        cateringlist = (RelativeLayout) findViewById(R.id.cateringlist);
        addproduct = (RelativeLayout) findViewById(R.id.addproduct);
        productlist = (RelativeLayout) findViewById(R.id.productlist);
    }

    /**
     *
     *返回上一级
     *
     */
    public void onBackActivity(View view) {
        this.finish();
    }

    private void initListennering() {
        addcatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(ProductManagerActivity.this,AddCaterActivity.class));
            }
        });
        cateringlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductManagerActivity.this,CaterListActivity.class));
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductManagerActivity.this,AddProductActivity.class));
            }
        });
        productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductManagerActivity.this,ProductListActivity.class));
            }
        });

    }
}
