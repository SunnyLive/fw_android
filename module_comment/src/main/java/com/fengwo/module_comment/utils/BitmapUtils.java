package com.fengwo.module_comment.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() + "/fengwoImg/";


    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * 保存图片到存储盘
     */
    public static void saveBitmap(Bitmap bmp, Context context) {
        String savePath;
        String fileName = System.currentTimeMillis() + ".JPEG";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            Toast.makeText(context, "保存失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        File filePic = new File(savePath + fileName);
        try {
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + savePath + fileName)));
    }

    public static File saveBitmap(Context context, Bitmap bitmap, String name) {
        File appDir = context.getFilesDir();
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
