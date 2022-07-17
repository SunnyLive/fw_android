package com.fengwo.module_comment.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.fengwo.module_comment.utils.L;


/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/17
 */
public class UpdateUtil {

    /**
     * 跳转应用商店.
     *
     * @param context   {@link Context}
     * @param appPkg    包名
     * @param marketPkg 应用商店包名
     * @return {@code true} 跳转成功 <br> {@code false} 跳转失败
     */
    public static boolean toMarket(Context context, String appPkg, String marketPkg) {
        try {
            Uri uri =null;
            if(Build.MANUFACTURER.toLowerCase().contains("samsung")){
                uri = Uri.parse("samsungapps://ProductDetail/"+ appPkg);
            }else {
                uri = Uri.parse("market://details?id=" + appPkg);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (marketPkg != null) {// 如果没给市场的包名，则系统会弹出市场的列表让你进行选择。
                intent.setPackage(marketPkg);
            }
            context.startActivity(intent);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 获取版本号
     * @return
     * @throws Exception
     */
    public static int getVersionCode(Context context)
    {
        int version = 0;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            version = packInfo.versionCode;
            L.e("lgl","当前版本号:"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

}
