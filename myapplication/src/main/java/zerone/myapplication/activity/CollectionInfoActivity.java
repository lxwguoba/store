package zerone.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import zerone.myapplication.BaseAppActivity;
import zerone.myapplication.R;
import zerone.myapplication.interfaces.CollectionInfo;

/**
 * Created by 刘兴文 on 2018-12-01.
 * 收款资料信息
 */

public class CollectionInfoActivity extends BaseAppActivity implements CollectionInfo {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectioninfo);
    }

    /**
     * 资料信息展示-修改
     * @param view
     */
    public void onCollectionData(View view){
        Intent intent = new Intent(this, SubmitCollectionDataActivity.class);
        startActivity(intent);
    }

    /**
     * 资料信息图片-展示&修改
     * @param view
     */
    public void onCollectionPicture(View view) {
        Intent intent = new Intent(this, SubmitCollectionPictureActivity.class);
        startActivity(intent);
    }
}
