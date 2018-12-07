package zerone.myapplication.utils;

import android.content.Context;

import com.google.gson.Gson;
import zerone.myapplication.domain.ShopInfoBean;

/**
 * Created by 刘兴文 on 2018-11-28.
 */

public class JsonToUser {

    public static void saveShopInfo(Context context, ShopInfoBean cus) {
        Gson gson = new Gson();
        String str = gson.toJson(cus);
        AppSharePreferenceMgr.put(context, "user", str);
    }

    public static ShopInfoBean getShopInfoBean(Context context) {
        String str = (String) AppSharePreferenceMgr.get(context, "user", "");
        Gson gson = new Gson();
        if (str.length() > 0) {
            return gson.fromJson(str, ShopInfoBean.class);
        } else {
            return null;
        }
    }
}
