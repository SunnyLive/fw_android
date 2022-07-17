//package com.fengwo.module_comment.utils;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import okhttp3.OkHttpClient;
//
//public class UserManager {
//
//    private final static String TOKEN_KEY = "token";
//
//    private static UserManager manager;
//    private Context mContext;
//    private String token;
//
//    private UserManager(Context c) {
//        mContext = c;
//    }
//
//    public static UserManager getInstance(Context c) {
//        if (null == manager) {
//            manager = new UserManager(c);
//        }
//        return manager;
//
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//        SPUtils1.put(mContext, TOKEN_KEY, token);
//    }
//
//    public String getToken() throws RuntimeException {
//        if (TextUtils.isEmpty(token)) {
//            token = (String) SPUtils1.get(mContext, TOKEN_KEY, "");
//        }
//        if (TextUtils.isEmpty(token)) {
//            throw new RuntimeException("token is null");
//        }
//        return token;
//    }
//
//    public String getUser() {
//        return null;
//    }
//    public void updateUser(){
//
//    }
//}
