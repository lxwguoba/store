package com.zerone.intodata.utils;

import android.app.Activity;
import android.os.Handler;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2018-11-26.
 */

public class MyRunnable implements Runnable {
    private RequestParams params;
    private Handler handler;
    private int responseID;
    private Activity activity;

    public MyRunnable(Activity activity, RequestParams params, Handler handler, int responseID) {
        this.params = params;
        this.handler = handler;
        this.responseID = responseID;
        this.activity = activity;
    }

    @Override
    public void run() {
        UpLoadUtils.xuploadPro(activity,params, handler, responseID);
    }
}
