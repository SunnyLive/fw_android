package com.fengwo.module_live_vedio.utils;

import com.fengwo.module_comment.bean.AnchorWishBean;
import com.fengwo.module_comment.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/7/9 17:06
 */
public class WishCacheMr {

    private  List<AnchorWishBean> anchorWishBeans;
    private  List<AnchorWishBean> wishInfo;
    private static WishCacheMr wishCacheMr;

    public static WishCacheMr getInstance() {
        if (wishCacheMr == null) {
            synchronized (WishCacheMr.class) {
                if (wishCacheMr == null) {
                    wishCacheMr = new WishCacheMr();
                }
            }
        }
        return wishCacheMr;
    }

    /**
     * 缓存未提交的心愿
     * @param wishBeans
     */
    public void cacheWish(List<AnchorWishBean> wishBeans) {
        anchorWishBeans = wishBeans;
    }

    public void cleanCache() {
        if (anchorWishBeans != null) anchorWishBeans.clear();
        if (wishInfo != null) wishInfo.clear();
    }

    public void deleteCache(int wishType) {
        AnchorWishBean anchorWishBean = null;
        for (AnchorWishBean bean : anchorWishBeans) {
            if (bean.wishType == wishType) {
                anchorWishBean = bean;
                break;
            }
        }
        anchorWishBeans.remove(anchorWishBean);
    }

    public List<AnchorWishBean> getWish() {
        if (anchorWishBeans == null) {
            return new ArrayList<>();
        } else {
            return anchorWishBeans;
        }
    }

    /**
     * 缓存已许愿的心愿
     * @param wishInfo
     */
    public  void cacheExistWish(List<AnchorWishBean> wishInfo) {
        L.e("====="+wishInfo);
       this.wishInfo = wishInfo;
    }

    public List<AnchorWishBean> getExistWish() {
        L.e("=======" + wishInfo);
        return wishInfo==null?new ArrayList<>():wishInfo;
    }

    /**
     * 判断礼物ID 是否存在
     */
    public boolean isExitGiftId(int giftId){
        ArrayList<AnchorWishBean> beans = new ArrayList<>();
        if (anchorWishBeans!=null)  beans.addAll(anchorWishBeans);
        if (wishInfo!=null)  beans.addAll(wishInfo);
        for (AnchorWishBean anchorWish :beans){
            if (anchorWish.giftId==giftId){
                return true;
            }
        }
        return false;
    }
}
