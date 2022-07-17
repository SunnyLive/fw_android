package com.fengwo.module_comment.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.fengwo.module_comment.PublicService;
import com.fengwo.module_comment.base.HttpResult;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UploadHelper {

    public static final String TYPE_IMAGE = "images";
    public static final String TYPE_S_VIDEOS = "svideos";//小视频
    public static final String TYPE_M_VIDEOS = "mvideos";//短片
    public static final String TYPE_AUDIOS = "audios";//
    private OSSAsyncTask task;


    public static UploadHelper getInstance(Context c) {
        if (null == helper) {
            helper = new UploadHelper(c);
        }
        return helper;
    }

    private static UploadHelper helper;
    private Context mContext;
    private OssTokenDto preOssToken;
    private String preType;
    OSS oss;
    InitOssSuccessListener l;

    private UploadHelper(Context c) {
        mContext = c;
    }

    OnUploadListener listener;

    public interface OnUploadListener {
        void onStart();

        void onLoading(long cur, long total);

        void onSuccess(String url);

        void onError();
    }

    //2019-09-21T07:06:17Z
    public void doUpload(String type, File file, OnUploadListener l) {
        listener = l;
        if (null != preOssToken && System.currentTimeMillis() < TimeUtils.dealDateFormatTolongForUpload(preOssToken.expiration) && type.equals(preType)) {
            if (TextUtils.equals(TYPE_IMAGE, type))
                compressBeforeUpload(type, file);
            else {
                String name = preOssToken.useDir + file.getName();
                upload(name, file.getAbsolutePath());
            }
        } else {
            getOssInfo(type, () -> {
                if (TextUtils.equals(TYPE_IMAGE, type)) compressBeforeUpload(type, file);
                else {
                    String name = preOssToken.useDir + file.getName();
                    upload(name, file.getAbsolutePath());
                }
            });
        }
    }

    /**
     * 上传前 压缩图片
     *
     * @param type
     * @param file
     */
    private void compressBeforeUpload(String type, File file) {
        String name = "upload/" + type + "/" + file.getName();
        LubanUtils.compress(mContext, file, new LubanUtils.OnCompress() {
            @Override
            public void success(File f) {
                upload(name, f.getAbsolutePath());
            }

            @Override
            public void error(String msg) {
//                ToastUtils.showShort(mContext, "上传失败，请重试！");
            }
        });
    }

    /**
     * 阿里云文件上传
     *
     * @param fileName
     * @param filePath
     */
    private void upload(String fileName, String filePath) {
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(preOssToken.bucketName, fileName, filePath);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback((request, currentSize, totalSize) -> {
            Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            if (null != listener) {
                listener.onLoading(currentSize, totalSize);
            }

        });
        listener.onStart();
        task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                String fileUrl = preOssToken.host + "/" + fileName;
                Log.d("file Url", preOssToken.host + "/" + fileName);
                if (null != listener) {
                    listener.onSuccess(fileUrl);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                preOssToken = null;
                if (null != listener) {
                    listener.onError();
                }
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    L.e("ErrorCode", serviceException.getErrorCode());
                    L.e("RequestId", serviceException.getRequestId());
                    L.e("HostId", serviceException.getHostId());
                    L.e("RawMessage", serviceException.getRawMessage());
                }
                //                ToastUtils.showShort(mContext, "上传失败，请重试！");
            }
        });

    }

    /**
     * 取消上传
     */
    public void cancleUpLoad() {
        if (task != null) {
            task.cancel();
        }
    }

    /**
     * 获取阿里云oss 必要信息
     *
     * @param type
     * @param l
     */
    private void getOssInfo(String type, InitOssSuccessListener l) {
        preType = type;
        new RetrofitUtils().createApi(PublicService.class)
                .getOssToken(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<OssTokenDto>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        if (httpResult.isSuccess()) {
                            preOssToken = (OssTokenDto) httpResult.data;
                            String endpoint = preOssToken.endpoint;
                            //if null , default will be init
                            ClientConfiguration conf = new ClientConfiguration();
                            conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
                            conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
                            conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
                            conf.setMaxErrorRetry(2); // retry，default 2
                            OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
                            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(preOssToken.accessKeyId, preOssToken.accessKeySecret, preOssToken.securityToken);
                            oss = new OSSClient(mContext, endpoint, credentialProvider, conf);
                            l.initSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    interface InitOssSuccessListener {
        void initSuccess();
    }


    public class OssTokenDto {
        public String accessKeyId;
        public String accessKeySecret;
        public String bucketName;
        public String endpoint;
        public String errorCode;
        public String errorMsg;
        public String expiration;//超时时间，未过超时时间 不需要重新请求该接口
        public String host;
        public String securityToken;
        public String useDir;
    }
}
