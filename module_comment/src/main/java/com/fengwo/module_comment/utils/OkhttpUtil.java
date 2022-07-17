package com.fengwo.module_comment.utils;

import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.bean.IsAttentionDto;
import com.fengwo.module_comment.widget.MyFlowLayout;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/23
 */
public class OkhttpUtil {

    private static OkhttpUtil instance;
    public OkHttpClient okHttpClient;

    private OkhttpUtil(){
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (BuildConfig.DEBUG)
            builder.sslSocketFactory(createSSLSocketFactory(), mMyTrustManager);
        okHttpClient = builder.build();
    }
    public static OkhttpUtil getInstance(){
        if (instance == null){
            synchronized (OkhttpUtil.class){
                if (instance ==null){
                    instance = new OkhttpUtil();
                }
            }
        }
        return instance;
    }

    RetrofitUtils.MyTrustManager mMyTrustManager;

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            mMyTrustManager = new RetrofitUtils.MyTrustManager();
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{mMyTrustManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }

    //实现X509TrustManager接口
    public static class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
