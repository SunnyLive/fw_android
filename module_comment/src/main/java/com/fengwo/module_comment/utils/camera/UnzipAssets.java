package com.fengwo.module_comment.utils.camera;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Created by wh on 2017/3/22.
 */

public class UnzipAssets {

    UnZipTask mUnZipTask;
    private static Context mContext;

    public UnzipAssets() {
    }

    public void unZipRequest(Context context, String zipName, IUnZipCallBack callBack) {
        mUnZipTask = new UnZipTask(callBack);
        mUnZipTask.execute(zipName);
        mContext = context;
    }

    /*
     * 异步解压zip
     * */
    public class UnZipTask extends AsyncTask<String, Void, String> {

        IUnZipCallBack mCallBack;

        public UnZipTask(IUnZipCallBack callBack) {
            mCallBack = callBack;
        }


        @Override
        //在界面上显示进度条
        protected void onPreExecute() {
            mCallBack.onStart();
        }

        @Override
        protected String doInBackground(String... zipName) {

            if (isFileEmpty(Config.STICKER_LOCAL_PATH + Config.STICKER_NAME)) {
                try {
                    unZip(zipName[0], Config.STICKER_LOCAL_PATH);
                    createNoMediaFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return Config.STICKER_LOCAL_PATH;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mCallBack.Response(result);

        }
    }

    public static void createNoMediaFile() {
        File file = new File(Config.STICKER_NOMEDIA_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 注入数据对象
    public interface IUnZipCallBack {
        /**
         * 开始解压数据
         */
        void onStart();

        /**
         * @param outFileDirectory
         */
        void Response(String outFileDirectory);

        /**
         * 解压失败
         */
        void onError();
    }


    /**
     * 解压Assets中的文件
     *
     * @param assetName       压缩包文件名
     * @param outputDirectory 输出目录
     * @throws IOException
     */
    public static void unZip(String assetName, String outputDirectory) throws IOException {
        //创建解压目标目录
        File file = new File(outputDirectory);
        //如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        InputStream inputStream = null;
        //打开压缩文件
        inputStream = mContext.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        //使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        //解压时字节计数
        int count = 0;
        //如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            //如果是一个目录
            if (zipEntry.isDirectory()) {
                //String name = zipEntry.getName();
                //name = name.substring(0, name.length() - 1);
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                file.mkdir();
            } else {
                //如果是文件
                file = new File(outputDirectory + File.separator
                        + zipEntry.getName());
                //创建该文件
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((count = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.close();
            }
            //定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }



    public boolean isFileEmpty(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length > 0) return false;
        }
        return true;
    }


        public String get(Context context, int id) {
            InputStream stream = context.getResources().openRawResource(id);
            return read(stream);
        }

        public String read(InputStream stream) {
            return read(stream, "utf-8");
        }

        public String read(InputStream is, String encode) {
            if (is != null) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, encode));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    return sb.toString();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "";
        }
}