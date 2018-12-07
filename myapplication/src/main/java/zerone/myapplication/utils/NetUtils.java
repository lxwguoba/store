package zerone.myapplication.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import zerone.myapplication.MyApplication;

/**
 * Created by Administrator on 2017/6/23.
 */

public class NetUtils {

    /**
     * pos
     *
     * @param context    ���� 上下文��
     * @param map        参数
     * @param url        ��      网址
     * @param handler    ���ͷ� handler机制
     * @param responseID ���ܷ��返回码
     */
    public static void netWorkByMethodPost(final Context context, final Map<String, String> map, String url, final Handler handler, final int responseID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Message message = new Message();
                            message.what = responseID;
                            message.obj = response;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = new Message();
                message.what = 0;
                message.obj = error;
                handler.sendMessage(message);
            }
        }) {
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                return map;
            }
        };
        //清除缓存
        File cacheDir = new File(context.getCacheDir(), "volley");
        DiskBasedCache cache = new DiskBasedCache(cacheDir);
        MyApplication.getQueues().start();
        // clear all volley caches.  清楚所有缓存
        MyApplication.getQueues().add(new ClearCacheRequest(cache, null));
        MyApplication.getQueues().add(stringRequest);
    }


    public static void netWorkerGoodsid(Context context, final Map<String, String> map, String url, final Handler handler, final int responseID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Message message = new Message();
                        message.what = responseID;
                        message.obj = response;
                        handler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = new Message();
                message.what = 1;
                message.obj = error.toString();
                handler.sendMessage(message);

            }
        }) {
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                return map;
            }
        };
        //清除缓存
        File cacheDir = new File(context.getCacheDir(), "volley");
        DiskBasedCache cache = new DiskBasedCache(cacheDir);
        MyApplication.getQueues().start();
        // clear all volley caches.  清楚所有缓存
        MyApplication.getQueues().add(new ClearCacheRequest(cache, null));
        MyApplication.getQueues().add(stringRequest);
    }

    /**
     * get������������
     *
     * @param context    ��� 上下文������
     * @param url        �      get访问的url
     * @param handler    ���� handler 机制
     * @param responseID 返回码
     */
    public static void netWorkByMethodGet(final Context context, String url, final Handler handler, final int responseID) {
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Message message = new Message();
                        message.what = responseID;
                        message.obj = response;
                        handler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = new Message();
                message.what = 0;
                message.obj = error.toString();
                handler.sendMessage(message);
            }
        });
        //清除缓存
        File cacheDir = new File(context.getCacheDir(), "volley");
        DiskBasedCache cache = new DiskBasedCache(cacheDir);
        MyApplication.getQueues().start();
        // clear all volley caches.  清楚所有缓存
        MyApplication.getQueues().add(new ClearCacheRequest(cache, null));
        MyApplication.getQueues().add(stringRequest);
    }
}
