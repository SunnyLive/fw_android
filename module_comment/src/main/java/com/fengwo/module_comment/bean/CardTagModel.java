package com.fengwo.module_comment.bean;

import java.io.Serializable;

public class CardTagModel implements Serializable {
    public String createTime;
    public String name;
    public int id;
    public int sort;
    public int status; // 状态：0关闭，1启用
    public int times; // 使用次数
    public int type; // 类型：1圈子

    // 自定义属性
    public boolean selected = false; // 是否选中
    public int originPosition; // 在原本的数据源中的位置
}
