package com.fengwo.module_chat.enums;

import com.fengwo.module_chat.R;
import com.fengwo.module_comment.Constants;

public enum MessageHeaderEnum {

    NOTICE_TYPE_APPOINT("约会助手", R.drawable.ic_appoinment, Constants.NOTICE_TYPE_APPOINT),
    NOTICE_TYPE_INTERACT("互动通知", R.drawable.ic_interact, Constants.NOTICE_TYPE_INTERACT),
    NOTICE_TYPE_RECENT_VISITORS("最近访客", R.drawable.ic_recent_visitor, Constants.NOTICE_TYPE_RECENT_VISITORS),
    NOTICE_TYPE_SYSTEM("发现消息", R.drawable.ic_default_system, Constants.NOTICE_TYPE_SYSTEM),
    NOTICE_TYPE_GREET("收到的招呼", R.drawable.ic_default_greet, Constants.NOTICE_TYPE_GREET),
    NOTICE_TYPE_OFFICIAL("官方消息", R.drawable.pic_official, Constants.NOTICE_TYPE_OFFICIAL);
    private String name;
    private int index;
    private int icon;

    MessageHeaderEnum(String name, int icon, int index) {
        this.name = name;
        this.index = index;
        this.icon = icon;
    }

    public static String getName(int index) {
        for (MessageHeaderEnum c : MessageHeaderEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static int getIcon(int index) {
        for (MessageHeaderEnum c : MessageHeaderEnum.values()) {
            if (c.getIndex() == index) {
                return c.icon;
            }
        }
        return R.drawable.ic_launcher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
