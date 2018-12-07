package zerone.myapplication.utils;

import android.content.Context;
import android.util.Log;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 刘兴文 on 2018-11-28.
 */

public class CreateTokenUtils {

    /**
     *
     * @param context 上下文
     * @param timestamp 时间搓
     * @return
     */
    public static String createToken(Context context, String timestamp) {
        StringBuffer s = new StringBuffer();
        String user = (String) AppSharePreferenceMgr.get(context,"username","");
        Log.i("URL",user);
        try {
            String[] customerifo = {user, timestamp};
            String[] cinfo = CreateTokenUtils.stringSort(customerifo);
            for (int i = 0; i < cinfo.length; i++) {
                s.append(cinfo[i]);
            }
            s.append("lingyi2018");
            byte[] info64 = Base64Utils.decrypt(s.toString());
            String inss = new String(info64, "utf-8");
            StringBuffer info = new StringBuffer(inss);
            info.append("lingyi2018");
            String da = replaceBlank(info.toString());
            String md = getMD5(da);
            return md;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * md5加密
     * @param str
     * @return
     * @throws Exception
     */
    public static String getMD5(String str) throws Exception {
        /** 创建MD5加密对象 */
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        /** 进行加密 */
        md5.update(str.getBytes());
        /** 获取加密后的字节数组 */
        byte[] md5Bytes = md5.digest();
        String res = "";
        for (int i = 0; i < md5Bytes.length; i++) {
            int temp = md5Bytes[i] & 0xFF;
            if (temp <= 0XF) { // 转化成十六进制不够两位，前面加零
                res += "0";
            }
            res += Integer.toHexString(temp);
        }
        return res;
    }

    /**
     * 去除空格和回车
     *
     * @param str 需要去除的空格和回车的字符串
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    public static String[] stringSort(String[] s) {
        List<String> list = new ArrayList<String>(s.length);
        for (int i = 0; i < s.length; i++) {
            list.add(s[i]);
        }
        Collections.sort(list);
        return list.toArray(s);
    }
}
