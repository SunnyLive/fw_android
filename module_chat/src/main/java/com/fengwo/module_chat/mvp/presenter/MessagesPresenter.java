package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.ui.contract.IMessageActivityView;
import com.fengwo.module_chat.utils.DataSecurityUtil;
import com.fengwo.module_chat.utils.SSLSocketClient;
import com.fengwo.module_comment.base.BasePresenter;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessagesPresenter extends BasePresenter<IMessageActivityView> {
    DataSecurityUtil desUtils = new DataSecurityUtil();

    public void getMessageList(int fromUid) {
        HashMap para = new HashMap();
        JSONObject allJson = new JSONObject();
        try {
            allJson.put("fromUid", "" + fromUid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();
        //第二步创建RequestBody（Form表达）
        RequestBody body = new FormBody.Builder()
                .add("function", "60000")
                .add("json", "a1Cw+7sx8MHGI8ls09dmMA==")
                .build();
        Request request = new Request.Builder()
                .url("https://118.31.68.230:443/UserController")
                .post(body)
                .build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                result = desUtils.decrypt(result);
                FriednDto f = new Gson().fromJson(result, FriednDto.class);
                getView().setMessageList(f.data);
            }
        });
    }


    public class FriednDto {
        public List<Friend> data;
    }

    public class Friend {
        public String fri_ids;
        public String user_ids;
        public String f_type;
        public String g_name;
        public String g_uids;
        public String nike_name;
        public String head_img;
        public String head_img_ip;
    }
}
