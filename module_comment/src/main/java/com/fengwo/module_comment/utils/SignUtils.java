package com.fengwo.module_comment.utils;

import com.fengwo.module_websocket.security.DataSecurityUtil;

import java.util.Random;

public class SignUtils {
    private static long sumDigits(long n) {
        long tmp;
        long result = 0;
        tmp = n;
        while (tmp > 0) {
            result = tmp % 10 + result;
            tmp = tmp / 10;
        }
        return result;
    }

    public static String getNonce() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random1 = new Random();
        //指定字符串长度，拼接字符并toString
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            //获取指定长度的字符串中任意一个字符的索引值
            int number = random1.nextInt(str.length());
            //根据索引值获取对应的字符
            char charAt = str.charAt(number);
            sb.append(charAt);
        }
        String str1 = sb.toString();
        return str1;
    }

    public static String sign(String nonce, String mobile, long timestamp) {
        long mobLong = sumDigits(Long.parseLong(mobile));
        String signStr = mobile + mobLong + nonce + timestamp;
        DataSecurityUtil des = new DataSecurityUtil();
        String result = des.encrypt(signStr);
        return result;
    }
}
