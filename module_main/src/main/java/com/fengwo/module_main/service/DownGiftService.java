package com.fengwo.module_main.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.utils.DownloadHelper;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_websocket.Url;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.http.HTTP;

public class DownGiftService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getGifts();
        return super.onStartCommand(intent, flags, startId);
    }


    private void getGifts() {
        OkHttpClient c = new OkHttpClient();
        Request request = new Request.Builder().url(BuildConfig.DEBUG? Url.TEST_BASE_URL:Url.BASE_URL + "api/virtual_goods/source/list").build();
        c.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getGifts();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();
                try {
                    JSONObject root = new JSONObject(responseStr);
                    JSONArray data = root.getJSONArray("data");
                    downGiftRes(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void downGiftRes(JSONArray data) throws JSONException {
        List<String> swfList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            String giftSwf = data.getString(i);
            if (TextUtils.isEmpty(giftSwf) || !giftSwf.startsWith("http")) {
                continue;
            }
            swfList.add(giftSwf);
        }
        DownloadHelper.download(swfList);
    }

}
