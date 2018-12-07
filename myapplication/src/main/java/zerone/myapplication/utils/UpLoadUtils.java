package zerone.myapplication.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import zerone.myapplication.domain.ProgressBarBean;

/**
 * Created by Administrator on 2018-11-16.
 */

public class UpLoadUtils {
    /**
     * xutils上传图片
     */
    public static void xupload(File file) {
//        M_mUpLoad.do file_fileUpload.do
        RequestParams params = new RequestParams("https://ctwxl.com/upload/M_mUpLoad.do");
        params.setMultipart(true);
        params.addBodyParameter("file1", file);
        params.addBodyParameter("username", "guoba");
        params.addBodyParameter("password", "015277");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("URL", result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("URL", isOnCallback + "错误了" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("URL", "上传结束");
            }
        });
    }


    /**
     * 上传多张图片到服务器 并接受数据内容
     *
     * @param params 参数的封装
     */
    public static void xuploadMany(RequestParams params, final Handler handler, final int responseID) {
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("URL", result);
                Message message = new Message();
                message.what = responseID;
                message.obj = result;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("URL", isOnCallback + "错误了" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("URL", "上传结束");
            }
        });
    }


    /**
     * 上传多张图片到服务器 并接受数据内容
     *
     * @param params 参数的封装
     */
    public static void xuploadPro(RequestParams params, final Handler handler, final int responseID) {
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("URL", result);
                Message message = new Message();
                message.what = responseID;
                message.obj = result;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = new Message();
                message.what = -1;
                message.obj = ex.getMessage();
                handler.sendMessage(message);
                Log.i("URL", "88888888888888888888888888888888"+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (!isDownloading) {
                    int s = (int) (((int) current / (float) total) * 100);
                    Message message = new Message();
                    ProgressBarBean pbb = new ProgressBarBean();
                    pbb.setCurrent(current);
                    pbb.setTotal(total);
                    message.what = 11;
                    message.obj = s;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
