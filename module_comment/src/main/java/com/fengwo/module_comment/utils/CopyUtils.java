package com.fengwo.module_comment.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class CopyUtils {

    /**
     * 复制文本到粘贴板
     */
    public static boolean copy2Board(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            try {
                ClipData item = ClipData.newPlainText("copy", content);
                cm.setPrimaryClip(item);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else return false;
    }

    /**
     * 获取复制的文本
     */
    public static CharSequence getContentFromBoard(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm == null) {
            return "";
        } else {
            ClipData clipData = cm.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                return clipData.getItemAt(0).getText();
            } else return "";
        }
    }
}
