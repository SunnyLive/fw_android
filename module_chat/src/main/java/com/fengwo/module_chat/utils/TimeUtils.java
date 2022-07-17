package com.fengwo.module_chat.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fan on 2016/6/23.
 */
public class TimeUtils {

    //毫秒转秒
    public static String long2String(long time) {

        //毫秒转秒
        int sec = (int) time / 1000;
        int min = sec / 60;    //分钟
        sec = sec % 60;        //秒
        if (min < 10) {    //分钟补0
            if (sec < 10) {    //秒补0
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {    //秒补0
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }

    }

    //毫秒转秒
    public static String int2String(long sec) {

        //毫秒转秒
        long min = sec / 60;    //分钟
        sec = sec % 60;        //秒
        if (min < 10) {    //分钟补0
            if (sec < 10) {    //秒补0
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {    //秒补0
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }

    }

    /**
     * 返回当前时间的格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(System.currentTimeMillis());
    }


    public static String getTimestamp() {
        return Calendar.getInstance().getTimeInMillis() + "";
    }


    /**
     * 计算秒差
     *
     * @param date1
     * @param date2
     * @return double
     */
    public static double timeCalculateSeconds(Date date1, Date date2) {
        double millisecond = date2.getTime() - date1.getTime();
        double seconds = millisecond / 1000;
        return seconds;
    }

    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }


    public static String replaceSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";


        //可以在中括号内加上任何想要替换的字符

        String aa = "";//这里是将特殊字符换为aa字符串,""代表直接去掉

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);//这里把想要替换的字符串传进来
        String newString = m.replaceAll(aa).trim();

        return newString;
    }


}
