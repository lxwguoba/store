package zerone.myapplication.utils;

import android.os.Handler;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2018-11-26.
 */

public class MyRunnable implements Runnable {
    private RequestParams params;
    private Handler handler;
    private int responseID;

    public MyRunnable(RequestParams params, Handler handler, int responseID) {
        this.params = params;
        this.handler = handler;
        this.responseID = responseID;
    }
    @Override
    public void run() {
        UpLoadUtils.xuploadPro(params, handler, responseID);
    }
}
