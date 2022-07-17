package com.fengwo.module_login.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.utils.DownloadHelper;
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

public class DownGiftService extends Service {


    private String giftStr;
    private int count;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (TextUtils.isEmpty(giftStr)) {
            getGifts();
        } else {
            download(giftStr);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(String str) {
        try {
            JSONObject root = new JSONObject(str);
            JSONArray data = root.getJSONArray("data");
            count = data.length();
            downGiftRes(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int scanGiftCount() {

        int count = 0;
        File file = new File(DownloadHelper.FWHY_FILE_PATH);
        File[] files = file.listFiles();
        if (null == files) return 0;
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isFile()) {
                count++;
            }
        }
        return count;
    }


    private void getGifts() {
        OkHttpClient c = new OkHttpClient();
        Request request = new Request.Builder().url(BuildConfig.DEBUG?Url.TEST_BASE_URL: Url.BASE_URL + "api/virtual_goods/source/list").build();
        c.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                giftStr = response.body().string();
                download(giftStr);
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
