package zerone.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 刘兴文 on 2018-11-29.
 */

public class TimeFormat {
    public static String time(String lt) {
        Date date = new Date(Long.parseLong(lt) * 1000);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateformat.format(date);
    }
}
