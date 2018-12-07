package zerone.myapplication.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2018-10-27.
 *
 */


public class CheckInputValue {
    /**
     * 判断是否是符合的手机号码验证
     * @param mContext
     * @param str
     * @return
     */
    public static boolean isPhone2(Context mContext, String str) {
        if (!TextUtils.isEmpty(str)){
            Pattern pattern = Pattern.compile("^1[3|4|5|7|8]{1}[0-9]{9}");
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches()){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 输入的是否是合法的邮箱
     * @param mContext
     * @param str
     * @return
     */
    public static boolean isEmails(Context mContext, String str) {
        if (!TextUtils.isEmpty(str)){
            Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches()){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
