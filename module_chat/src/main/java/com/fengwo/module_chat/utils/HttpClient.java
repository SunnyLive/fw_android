package com.fengwo.module_chat.utils;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {

    public static String userController = "https://118.31.68.230:443/UserController";
    public static String CommController = "https://118.31.68.230:443/CommController";
    public static String FileController = "https://118.31.68.230:443/FileController";
    private static HttpClient mClient;
    private OkHttpClient client;
    private Context context;

    private HttpClient(Context c) {
        context = c;
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
    }

    public static HttpClient getInstance(Context c) {
        if (mClient == null) {
            mClient = new HttpClient(c);
        }
        return mClient;
    }


    // GET方法
    public void get(String url, HashMap<String, String> param, final MyCallback callback) {
        // 拼接请求参数
        if (!param.isEmpty()) {
            StringBuffer buffer = new StringBuffer(url);
            buffer.append('?');
            for (Map.Entry<String, String> entry : param.entrySet()) {
                buffer.append(entry.getKey());
                buffer.append('=');
                buffer.append(entry.getValue());
                buffer.append('&');
            }
            buffer.deleteCharAt(buffer.length() - 1);
            url = buffer.toString();
        }
        Request.Builder builder = new Request.Builder().url(url);
        builder.method("GET", null);
        Request request = builder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });
    }

    public void get(String url, final MyCallback callback) {
        get(url, new HashMap<String, String>(), callback);
    }

    // POST 方法
    public void post(String url, HashMap<String, String> param, final MyCallback callback) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (!param.isEmpty()) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                formBody.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody form = formBody.build();
        Request.Builder builder = new Request.Builder();

        Request request = builder.post(form)
                .url(url)
                .addHeader("EventId","")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });
    }

    public interface MyCallback {
        void success(Response res) throws IOException;

        void failed(IOException e);
    }


    public void uploadAudioFiles(String filePath, String filename, HashMap<String, String> param, final MyCallback callback) {

        //上传文件：
        File file = new File(filePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"username\""),
                        RequestBody.create(null, "HGR"))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mFile\"; filename=\"" + filename + "\""), fileBody)
                .addFormDataPart("json",param.get("json"))
                .addFormDataPart("function",param.get("function"))
                .build();
        Request request = new Request.Builder()
                .url(FileController)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Logger.e("failure upload!");
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Logger.e("success upload!");
                callback.success(response);
            }
        });
    }

    public void uploadImgFiles(String filePath, String filename, HashMap<String, String> param, final MyCallback callback) {

        //上传文件：
        File file = new File(filePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"username\""),
                        RequestBody.create(null, "HGR"))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mFile\"; filename=\"" + filename + "\""), fileBody)
                .addFormDataPart("json",param.get("json"))
                .addFormDataPart("function",param.get("function"))
                .build();
        Request request = new Request.Builder()
                .url(FileController)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Logger.e("failure upload!");
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Logger.e("success upload!");
                callback.success(response);
            }
        });
    }

}
