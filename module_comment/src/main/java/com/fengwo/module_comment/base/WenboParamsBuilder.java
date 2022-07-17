package com.fengwo.module_comment.base;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WenboParamsBuilder {
    private Map<String, String> params = new HashMap<>();
    private Gson gson;

    public WenboParamsBuilder put(String key, String value) {
        params.put(key, value);
        return this;
    }

    public RequestBody build() {
        return createWenboRequestBody(params);
    }

    public RequestBody createWenboRequestBody(Map map) {
        if (null == gson)
            gson = new Gson();
        Map params = new HashMap();
        params.put("params", map);
        RequestBody requestBody = RequestBody.create(gson.toJson(params), MediaType.parse("application/json"));
        return requestBody;
    }
}
