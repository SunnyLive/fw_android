package com.fengwo.module_comment.utils;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadHelper {
    public final static String FWHY_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fwhynew";

    public interface DownloadListener {
        void completed();

        void failed();

        void onProgress(int progress);
    }

    DownloadListener l;

    public static void init(Application context) {
        FileDownloader.setupOnApplicationOnCreate(context)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15000) // set connection timeout.
                        .readTimeout(15000) // set read timeout.
                ))
                .commit();
    }

    public static int downCount() {
        File f = new File(FWHY_FILE_PATH);
        if (f.exists()) {
            File[] fs = f.listFiles();
            if (null == fs) return 0;
            int count = 0;
            for (int i = 0; i < fs.length; i++) {
                if (fs[i].isFile()) {
                    count++;
                }
            }
            return count;
        }
        return 0;
    }

    public static void downloadGift(String url) {
        List<String> urls = new ArrayList<>();
        urls.add(url);
        download(urls);
    }

    public static void downloadGift(String url, DownloadListener l) {
        List<String> urls = new ArrayList<>();
        urls.add(url);
        download(urls, l);
    }

    public static void download(List<String> urls) {
        download(urls, null);
    }

    public static void download(List<String> urls, DownloadListener listener) {
        FileDownloadQueueSet queueSet = new FileDownloadQueueSet(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                int pro = (int) (1f * soFarBytes / totalBytes / 100);
                if (null != listener)
                    listener.onProgress(pro);

            }

            @Override
            protected void completed(BaseDownloadTask task) {
                String realName = getFileNameFromFileUrl(task.getUrl());
                String filePath = task.getTargetFilePath();
                if (TextUtils.isEmpty(filePath)) {
                    return;
                }
                File f = new File(filePath);
                File newFile = new File(FWHY_FILE_PATH + File.separator + realName.split("\\.")[0]);
                f.renameTo(newFile);
                if (null != listener) {
                    listener.completed();
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if (null != listener) {
                    listener.failed();
                }
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        });
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            if (!isAlreadyDownload(urls.get(i)))
                tasks.add(FileDownloader.getImpl().create(urls.get(i)).setTag(i + 1));
        }
        queueSet.setDirectory(FWHY_FILE_PATH);
        queueSet.disableCallbackProgressTimes();
        // 所有任务在下载失败的时候都自动重试一次
        queueSet.setAutoRetryTimes(10);
        // 并行执行该任务队列
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }

    private static String getFileNameFromFileUrl(String url) {
        int index = url.lastIndexOf("/");
        int length = url.length();
        return url.substring(index + 1, length);
    }

    public static boolean isAlreadyDownload(String url) {
        String str = getFileNameFromFileUrl(url);
        String[] fileNames = str.split("\\.");
        if (null == fileNames || fileNames.length <= 0) {
            return false;
        }
        File f = new File(FWHY_FILE_PATH + File.separator + fileNames[0]);
        if (f.exists()) {
            return true;
        }
        return false;
    }
}
