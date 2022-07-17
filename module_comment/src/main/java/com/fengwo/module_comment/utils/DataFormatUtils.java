package com.fengwo.module_comment.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class DataFormatUtils {

    public static DecimalFormat df = new DecimalFormat("###.##");
    public static DecimalFormat df1 = new DecimalFormat("###.#");

    public static String formatNumbers(int originNumber) {
        if (originNumber > 10000) {
            double current = new BigDecimal(originNumber).divide(new BigDecimal(10000), 1, BigDecimal.ROUND_DOWN).doubleValue();
            return df.format(current) + "W";
        } else {
            return df.format(originNumber);
        }
    }

    public static String formatNumbersHot(int originNumber) {
        if (originNumber > 100000000) {
            double current = new BigDecimal(originNumber).divide(new BigDecimal(100000000), 1, BigDecimal.ROUND_DOWN).doubleValue();
            return df.format(current) + "亿";
        } else if (originNumber > 10000) {
            double current = new BigDecimal(originNumber).divide(new BigDecimal(10000), 1, BigDecimal.ROUND_DOWN).doubleValue();
            return df.format(current) + "W";
        } else {
            return df.format(originNumber);
        }
    }

    public static String formatNumbers(double originNumber) {
        if (originNumber > 10000) {
            double current = new BigDecimal(originNumber).divide(new BigDecimal(10000), 1, BigDecimal.ROUND_DOWN).doubleValue();
            return df.format(current) + "W";
        } else {
            return df.format(originNumber);
        }
    }


    public static String formatNumberGift(double originNumber) {
        if (originNumber > 10000) {
            double current = new BigDecimal(originNumber).divide(new BigDecimal(10000), 1, BigDecimal.ROUND_DOWN).doubleValue();
            return df1.format(current) + "W";
        } else {
            return df1.format(originNumber);
        }
    }


    public static String formatDouble(double originNumber) {
        DecimalFormat df = new DecimalFormat("###.#");
        return df.format(originNumber);
    }

    public static String formatNumberKm(double originNumber) {
        if (originNumber > 1000) {
            double current = new BigDecimal(originNumber).divide(new BigDecimal(1000), 1, BigDecimal.ROUND_DOWN).doubleValue();
            return df.format(current) + "km";
        } else if (originNumber > 100) {
            return df.format(originNumber) + "m";
        } else {
            return "<100m";
        }
    }

    public static String formatNumbersChinaUnit(int originNumber) {
        if (originNumber > 100000000) {
            double current = new BigDecimal(originNumber).divide(new BigDecimal(100000000), 1, BigDecimal.ROUND_DOWN).doubleValue();
            return df.format(current) + "亿";
        } else if (originNumber > 10000) {
            double current = new BigDecimal(originNumber).divide(new BigDecimal(10000), 1, BigDecimal.ROUND_DOWN).doubleValue();
            return df.format(current) + "万";
        } else {
            return df.format(originNumber);
        }
    }


    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        if(TextUtils.isEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}