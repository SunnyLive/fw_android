package com.fengwo.module_comment.base;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseListDto<T> implements Serializable {
    public ArrayList<T> records;
    public ArrayList<T> list;
    public String total;
    public int pages;
}
