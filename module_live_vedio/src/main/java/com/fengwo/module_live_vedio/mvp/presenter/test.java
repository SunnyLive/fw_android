package com.fengwo.module_live_vedio.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @anchor Administrator
 * @date 2020/11/20
 */
public class test {
    public static void main(String[] args)  {
        JSONObject object = null;
        try {
            object = new JSONObject("{\"data\":{\"effectUrl\":\"null\"}}");
            JSONObject jsonData = object.getJSONObject("data");
            System.err.println(jsonData.getString("effectUrl"));
        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println(e+"");
        }

    }
}
