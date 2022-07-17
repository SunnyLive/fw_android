package com.fengwo.module_flirt.Interfaces;

/**
 * @Author BLCS
 * @Time 2020/3/30 17:54
 */
public interface NearByListener {
    void onRefrsh();
    void onLoadMore();
    void getPeopleNearby(String longitude, String latitude,String maxAge, String minAge, int page, String sex,String city);
}
