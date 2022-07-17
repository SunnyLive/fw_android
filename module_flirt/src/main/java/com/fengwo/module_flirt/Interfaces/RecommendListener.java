package com.fengwo.module_flirt.Interfaces;

/**
 * @Author BLCS
 * @Time 2020/4/27 12:22
 */
public interface RecommendListener {
    void onRefrsh();
    void onLoadMore();
    void getVideoDating(String location, int page,int status,String tabelName);
}
