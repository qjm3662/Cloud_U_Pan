package com.qjm3662.cloud_u_pan.Tool;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by qjm3662 on 2016/9/26 0026.
 */

public class TimeUtils {
    public static final Long ONE_DAY = Long.valueOf(60*60*24);

    //时间操作
    public static SimpleDateFormat sdr_hm = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat sdr_year = new SimpleDateFormat("yy");
    public static SimpleDateFormat sdr_month = new SimpleDateFormat("MM");
    public static SimpleDateFormat sdr_day = new SimpleDateFormat("dd");
    public static SimpleDateFormat sdr_yM = new SimpleDateFormat("yy-MM");
    public static SimpleDateFormat sdr_Md = new SimpleDateFormat("MM-dd");

    private static void initTime() {
        sdr_hm.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_year.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_month.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_day.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_yM.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_Md.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
    }

    //时间操作
    public static String returnTime(long times){
        initTime();
        Date date = new Date(times);
        Date cur_date = new Date(System.currentTimeMillis());
        String s_date = "";
        if(sdr_year.format(date).equals(sdr_year.format(cur_date))){
            if(sdr_month.format(date).equals(sdr_month.format(cur_date)) && sdr_day.format(date).equals(sdr_day.format(cur_date))){
                s_date = sdr_hm.format(date);
            }else{
                s_date = sdr_Md.format(date);
            }
        }else{
            s_date = sdr_yM.format(date);
        }
        return s_date;
    }
}
