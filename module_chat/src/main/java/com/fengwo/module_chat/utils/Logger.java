package com.fengwo.module_chat.utils;

import java.util.Date;

public class Logger {

    public static void e(String info) {
        Date date = new Date();
        System.out.println(date + "-" + info);
    }
}
