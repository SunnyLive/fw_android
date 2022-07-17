package com.fengwo.module_live_vedio.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;

/**
 * @anchor Administrator
 * @date 2020/9/5
 */
public class LocalCacheUtils {
    private static final String PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/WerbNews";

    /**
     * 从本地读取图片
     * @param url
     */
    public Bitmap getBitmapFromLocal(String url){
        String fileName = null;//把图片的url当做文件名,并进行MD5加密
        try {
            fileName = encode(url);
            File file=new File(PATH,fileName);

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从网络获取图片后,保存至本地缓存
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url,Bitmap bitmap){
        try {
            String fileName = encode(url);//把图片的url当做文件名,并进行MD5加密
            File file=new File(PATH,fileName);

            //通过得到文件的父文件,判断父文件是否存在
            File parentFile = file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }

            //把图片保存至本地
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static String encode(String string) throws Exception {
        byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
