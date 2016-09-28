package com.qjm3662.cloud_u_pan.Tool;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by qjm3662 on 2016/9/26 0026.
 */

public class TimeUtils {
    public static final Long ONE_DAY = Long.valueOf(60*60*24);
    //时间操作
    public static String returnTime(long times){
        SimpleDateFormat sdr_hm = new SimpleDateFormat("HH:mm");
        Date date = new Date(times);
        String s_date = sdr_hm.format(date);
        return s_date;
    }
}
