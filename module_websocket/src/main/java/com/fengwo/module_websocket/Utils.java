package com.fengwo.module_websocket;

import android.os.Build;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return Build.BRAND + "-" + model;
    }

    public static String createMsgId(String uid) {
        DateFormat df = new SimpleDateFormat("yyyyMMDDHHmmssSSS");
        Date date = new Date();
        int i = new Random().nextInt(99999);
        String id = String.format(Locale.CHINA, "%010d", Integer.parseInt(uid));
        String random = String.format(Locale.CHINA, "%05d", i);
        return df.format(date) + id + random;
    }
}
