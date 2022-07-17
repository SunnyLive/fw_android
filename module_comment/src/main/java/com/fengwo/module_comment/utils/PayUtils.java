package com.fengwo.module_comment.utils;

import android.text.TextUtils;

import com.fengwo.module_websocket.BuildConfig;
import com.fengwo.module_websocket.Url;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class PayUtils {
    public interface PayCallback {
        void onSuccess();

        void onFailed();
    }

    public static void notifyPayNo(String payNo, PayCallback callback) {
        if (TextUtils.isEmpty(payNo)||payNo.equals("null")){
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.level(HttpLoggingInterceptor.Level.BODY);
//        builder.sslSocketFactory(createSSLSocketFactory(),mMyTrustManager);//屏蔽ssl整数验证
        builder.addInterceptor(logging);
        Request request = new Request.Builder()
                .url((BuildConfig.DEBUG? Url.TEST_BASE_URL:Url.BASE_URL) + "api/pay/charge_orders/" + payNo)
                .get()
                .build();
        Call call = builder.build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        JSONObject resultJs = new JSONObject(result);
                        if ("success".equals(resultJs.getString("data"))) {
                            callback.onSuccess();
                        } else {
                            callback.onFailed();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
