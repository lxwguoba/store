package com.ctlxw;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

public class Welcome extends AppCompatActivity {

    private DrawerLayout viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        viewById = findViewById(R.id.drawaer);
    }

    /**
     * 打开测边栏
     *
     * @param view
     */
    public void onOpenDraLayout(View view) {
        viewById.openDrawer(Gravity.LEFT);
    }

}
