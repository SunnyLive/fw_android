package com.fengwo.module_comment.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @anchor Administrator
 * @date 2021/1/25
 */
public class GsonUtils {
    private static final String TAG = GsonUtils.class.getName();
    private static Gson gson = (new GsonBuilder()).create();

    public GsonUtils() {
    }





    public static <T> T json2Obj(Gson var0, String var1, Class<T> var2) {
        try {
            return var0.fromJson(var1, var2);
        } catch (Throwable var4) {
         //   KLog.e(TAG, var4);
            return null;
        }
    }

    public static <T> T json2Obj(String var0, Class<T> var1) {
        return json2Obj(gson, var0, var1);
    }

    public static <T> String obj2Json(Gson var0, T var1) {
        String var2 = null;

        try {
            var2 = var0.toJson(var1);
        } catch (Exception var4) {
      //      KLog.e(var4);
        }

        return var2;
    }

    public static <T> String obj2Json(T var0) {
        return obj2Json(gson, var0);
    }

    public static <T> String obj2Json(Gson var0, T var1, Type var2) {
        String var3 = null;

        try {
            var3 = var0.toJson(var1, var2);
        } catch (Exception var5) {
      //      KLog.e(var5);
        }

        return var3;
    }

    public static <T> String obj2Json(T var0, Type var1) {
        return obj2Json(gson, var0, var1);
    }

    public static <T> List<T> json2ObjList(Gson var0, String var1, Class<T> var2) {
        ArrayList var3 = null;

        try {
            JsonParser var4 = new JsonParser();
            JsonArray var5 = var4.parse(var1).getAsJsonArray();
            var3 = new ArrayList();
            int var6 = 0;

            for(int var7 = var5.size(); var6 < var7; ++var6) {
                var3.add(var0.fromJson(var5.get(var6), var2));
            }
        } catch (Exception var8) {
          //  KLog.e(var8);
        }

        return var3;
    }

    public static <T> List<T> json2ObjList(String var0, Class<T> var1) {
        return json2ObjList(gson, var0, var1);
    }

    public static <T> String objList2Json(Gson var0, List<T> var1) {
        String var2 = null;

        try {
            var2 = var0.toJson(var1);
        } catch (Exception var4) {
        //    KLog.e(var4);
        }

        return var2;
    }

    public static <T> String objList2Json(List<T> var0) {
        return objList2Json(gson, var0);
    }

    public static void confirmValueIsArray(JsonObject var0, String var1) {
        if (var0 != null && var1 != null) {
            JsonElement var2 = var0.get(var1);
            if (var2 != null) {
                if (!var2.isJsonArray()) {
                    JsonArray var3 = new JsonArray();
                    var3.add(var2);
                    var0.remove(var1);
                    var0.add(var1, var3);
                }
            }
        }
    }
}
