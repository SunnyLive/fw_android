package com.fengwo.module_comment.utils;

import com.fengwo.module_comment.base.BasePresenter;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HttpUtils {

    public static RequestBody createRequestBody(Map map) {
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }

    public static class ParamsBuilder {
        private Map<String, String> params = new HashMap<>();

        public HttpUtils.ParamsBuilder put(String key, String value) {
            params.put(key, value);
            return this;
        }

        public RequestBody build() {
            return createRequestBody(params);
        }
    }
}
