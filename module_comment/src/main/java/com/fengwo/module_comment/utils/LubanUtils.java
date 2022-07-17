package com.fengwo.module_comment.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class LubanUtils {
    private final static String FILE_COMPRESS = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fwhynew" + File.separator + "compress";

    public interface OnCompress {
        void success(File f);

        void error(String msg);
    }

    /**
     * 尝试用鲁班压缩，压缩失败则返回原图
     *
     * @param context
     * @param file
     * @param l
     */
    public static void compress(Context context, File file, OnCompress l) {
        File f = new File(FILE_COMPRESS);
        if (!f.exists()) {
            f.mkdir();
        }
        Luban.with(context)
                .load(file)
                .ignoreBy(1024)
                .setTargetDir(FILE_COMPRESS)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        L.e("luban  start");
                    }

                    @Override
                    public void onSuccess(File file) {
                        L.e("luban  onSuccess");
                        l.success(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("luban  onError");
                        //如果压缩失败 则原图返回
                        l.success(file);
                    }
                }).launch();
    }
}
