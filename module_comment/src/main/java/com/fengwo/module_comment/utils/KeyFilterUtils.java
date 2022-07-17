package com.fengwo.module_comment.utils;

import android.text.TextUtils;

import java.util.LinkedList;

/**
 * 键值过滤工具
 *
 * @Author gukaihong
 * @Time 2020/12/16
 */
public class KeyFilterUtils {
    private int max = 3;
    private LinkedList<String> keys = new LinkedList<>();

    public KeyFilterUtils() {
    }

    public KeyFilterUtils(int max) {
        this.max = max;
    }

    public boolean put(String id) {
        for (String key : keys) {
            if (TextUtils.equals(id, key)) {
                return false;
            }
        }

        if (keys.size() == 3) {
            keys.removeFirst();
        } else {
            keys.add(id);
        }

        return true;
    }
}
