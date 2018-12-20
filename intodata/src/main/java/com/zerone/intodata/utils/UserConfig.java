package com.zerone.intodata.utils;

import android.content.Context;

/**
 * Created by 刘兴文 on 2018-12-18.
 */

public class UserConfig {
    /**
     * 获取用户名
     * @param context
     * @return
     */
    public static  String getUserName(Context context) {
        String user = (String) AppSharePreferenceMgr.get(context, "username", "");
        return user;
    }
}
