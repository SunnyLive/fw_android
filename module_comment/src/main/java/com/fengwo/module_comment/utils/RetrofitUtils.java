package com.fengwo.module_comment.utils;


import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_websocket.Url;
import com.fengwo.module_websocket.security.DataSecurityUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2016-08-18.
 */
public class RetrofitUtils {

    private static Retrofit retrofit;

    private static Retrofit wenboRetrofit;

    DataSecurityUtil dataSecurityUtil;
    Gson gson;

    @Autowired
    static UserProviderService helloService;

    public RetrofitUtils() {
        ARouter.getInstance().inject(this);
        dataSecurityUtil = new DataSecurityUtil();
        gson = new Gson();
    }

    public synchronized <T> T createApi(Class<T> clazz) {
        if (retrofit == null) {
            buildBaseRetrofit();
        }
        T t = retrofit.create(clazz);
        return t;
    }

    public synchronized <T> T createWenboApi(Class<T> clazz) {
        if (wenboRetrofit == null) {
            buildWenboRetrofit();
        }
        T t = wenboRetrofit.create(clazz);
        return t;
    }

    /**
     * 这里配置okhttp
     *
     * @return
     */
    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS).readTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        builder.addInterceptor(chain -> {
//            L.e("usertoken", "Bearer " + helloService.getToken());
            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + helloService.getToken())
                    .build();
            return chain.proceed(request);
        });
        //添加统一验证
        builder.addInterceptor(new SafeCheckInterceptor());
        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.level(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logInterceptor());
            builder.sslSocketFactory(createSSLSocketFactory(), mMyTrustManager);//屏蔽ssl整数验证
        }
        return builder.build();
    }

    private final int logSubLenth = 5000;//每行log长度

    private HttpLoggingInterceptor logInterceptor() {
        //新建log拦截器
        HttpLoggingInterceptor interceptor =
                new HttpLoggingInterceptor(message -> {
                    if (message.length() > logSubLenth) {
                        logSplit(message, 1);
                    } else Log.i("OkHttpClient", "OkHttpMessage:数据：     " + message);
                });
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private void logSplit(String message, int i) {
        if (message.length() <= logSubLenth) {
            Log.i("RetrofitUtils", "OkHttpMessage: 数据" + i + "：     " + message);
            return;
        } else if (message.length() > logSubLenth * 4) {
            return;
        }
        String msg1 = message.substring(0, logSubLenth);
        Log.i("RetrofitUtils", "OkHttpMessage: 数据" + i + "：     " + msg1);
        String msg2 = message.substring(logSubLenth);
        logSplit(msg2, ++i);
    }

    /**
     * 这里配置okhttp
     *
     * @return
     */
    private OkHttpClient provideWenboOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS).readTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        builder.addInterceptor(chain -> {
            //添加公共参数
            HashMap<String, Object> commomParamsMap = new HashMap<>();
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder();
            requestBuilder
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + helloService.getToken());
            String[] urlSplit = request.url().url().getPath().split("/");
            if ("POST".equals(request.method())) { // POST方法
                // 把已有的post参数添加到新的构造器\
                RequestBody reqBody = request.body();
                HashMap<String, Object> rootMap = new HashMap<>();
                if (reqBody instanceof FormBody) {
                    for (int i = 0; i < ((FormBody) reqBody).size(); i++) {
                        rootMap.put(((FormBody) reqBody).encodedName(i), ((FormBody) reqBody).encodedValue(i));
                    }
                    //这里可以添加公共参数
                } else {
                    //目前文播所有接口都走这一步
                    Buffer buffer = new Buffer();
                    reqBody.writeTo(buffer);
                    String oldParamsJson = buffer.readUtf8();
                    rootMap = new Gson().fromJson(oldParamsJson, HashMap.class);  //原始参数
                    KLog.d("("+request.url().url().getPath() + ")###-request params = " + oldParamsJson);

                    if (rootMap == null) {
                        rootMap = new HashMap<>();
                        rootMap.put("params", new HashMap<>());
                    }
                    int index = urlSplit.length - 1;
                    rootMap.put("eventId", urlSplit[index]);  //添加参数
                    rootMap.put("clientType", DeviceUtils.getModel());
                    rootMap.put("clientVersion", AppUtils.getVersionName(BaseApplication.mApp) + "-" + DeviceUtils.getSDKVersionName());
                    rootMap.put("timestamp", System.currentTimeMillis());
//                    Log.i("okhttp",rootMap.get("params"))
                    KLog.d("("+request.url().url().getPath() + ")###-request full params = " + gson.toJson(rootMap));
                    requestBuilder.post(RequestBody.create(dataSecurityUtil.encrypt(gson.toJson(rootMap)), MediaType.parse("application/json")));//加密
//                    requestBuilder.post(RequestBody.create(gson.toJson(rootMap), MediaType.parse("application/json")));//重新组装RequestBody
                }
            }
            return chain.proceed(requestBuilder.build());
        });
        //添加统一验证
        builder.addInterceptor(new SafeCheckInterceptor());
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.level(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
            builder.sslSocketFactory(createSSLSocketFactory(), mMyTrustManager);
        }
        return builder.build();
    }

    private Retrofit buildBaseRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().client(provideOkHttpClient()).baseUrl(BuildConfig.DEBUG ? Url.TEST_BASE_URL : Url.BASE_URL).
                    addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                    addConverterFactory(GsonConverterFactory.create()).addConverterFactory(new FileRequestBodyConverterFactory()).build();//文件上传
        }
        return retrofit;
    }

    private Retrofit buildWenboRetrofit() {
        if (wenboRetrofit == null) {
            wenboRetrofit = new Retrofit.Builder().client(provideWenboOkHttpClient()).baseUrl(BuildConfig.DEBUG ? Url.TEST_BASE_URL : Url.BASE_URL).
                    addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                    addConverterFactory(FwRequestBodyConverterFactory.create()).build();//文件上传
        }
        return wenboRetrofit;
    }


    public static class FileRequestBodyConverterFactory extends Converter.Factory {
        @Override
        public Converter<File, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return new FileRequestBodyConverter();
        }
    }

    static class FileRequestBodyConverter implements Converter<File, RequestBody> {

        @Override
        public RequestBody convert(File file) throws IOException {
            return RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        }

    }


    /**
     * 自定义加解密 ConverterFactory
     */
    public static class FwRequestBodyConverterFactory extends Converter.Factory {
        private final Gson gson;

        private FwRequestBodyConverterFactory(Gson gson) {
            if (null == gson) throw new NullPointerException("gson == null");
            this.gson = gson;
        }

        public static FwRequestBodyConverterFactory create() {
            return create(new Gson());
        }

        public static FwRequestBodyConverterFactory create(Gson gson) {
            return new FwRequestBodyConverterFactory(gson);
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new EncodeRequestConverter<>(gson, adapter);
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new DecodeResponseConverter<>(adapter);
        }
    }

    public static class EncodeRequestConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        public EncodeRequestConverter(Gson gson, TypeAdapter<T> adapter) {
            L.d("加密 Convert  构造方法 执行");
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            L.d("加密 Convert  convert方法 执行 T: %s", value.getClass().getSimpleName());
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            T encrypted = EncryptUtils.<T, T>encrypt(value);
            adapter.write(jsonWriter, encrypted);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    public static class DecodeResponseConverter<T> implements Converter<ResponseBody, T> {
        private TypeAdapter<T> adapter;
        private DataSecurityUtil dataSecurityUtil;

        public DecodeResponseConverter(TypeAdapter<T> adapter) {
            L.d("解密 Convert  构造方法 执行");
            this.adapter = adapter;
            dataSecurityUtil = new DataSecurityUtil();
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
//            //解密,解析
            T responseT = null;
            String response = value.string();
            L.d("解密 Convert  convert方法 执行 T: %s", value.getClass().getSimpleName());
            if (response.contains("\"status\":\"GG\"")
                    || response.contains("\"status\":\"ERROR\"")
                    || response.contains("\"status\":\"FAIL\"")
                    || response.contains("\"status\":\"WARN\"")) {
                return responseT = adapter.fromJson(response);
            }
            String result = dataSecurityUtil.decrypt(response);
            if (BuildConfig.DEBUG)
                Log.i("OkHttp", result);
            return responseT = adapter.fromJson(result);

//            return responseT = adapter.fromJson(response);
        }
    }


    public static class EncryptUtils {
        public static <T, V> V encrypt(T origin) {
            L.d("加密中 ......");
            V encrypted = (V) origin;
            return encrypted;
        }

        public static String decrypt(String encrpyted) {
            L.d("解密中 ......");
            String decrypted = encrpyted;
            return decrypted;
        }
    }

    MyTrustManager mMyTrustManager;

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            mMyTrustManager = new MyTrustManager();
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
