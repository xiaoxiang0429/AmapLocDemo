package com.tz.amaplocdemo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tz
 * @date 2019/7/23
 */
public class DateUtils {
    public static final String simpleFormat = "MM-dd HH:mm:ss";

    /**
     * 获取当前的时间戳
     * @return
     */
    public static String getTimeStame() {
        //获取当前的毫秒值
        long time = System.currentTimeMillis();
        //将毫秒值转换为String类型数据
        String time_stamp = String.valueOf(time);
        //返回出去
        return time_stamp;
    }
    /**
     * 获取当前的时间戳
     * @return
     */
    public static long getTimeLong() {
        //获取当前的毫秒值
        long time = System.currentTimeMillis();
        //将毫秒值转换为String类型数据
        return  System.currentTimeMillis();
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String stamp2Date(long seconds, String format) {
        if(seconds <= 0){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

}
