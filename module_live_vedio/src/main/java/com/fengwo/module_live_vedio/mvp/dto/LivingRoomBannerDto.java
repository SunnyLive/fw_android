package com.fengwo.module_live_vedio.mvp.dto;

import com.fengwo.module_comment.bean.ActBannerBean;
import com.fengwo.module_comment.bean.AnchorWishBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author BLCS 直播间轮播图
 * @Time 2020/7/8 12:15
 */
public class LivingRoomBannerDto {
    public EnterLivingRoomPkActivityDto pkActivityDto;//pk排位赛
    public EnterLivingRoomActDto activityDto;//普通活动
    public List<AnchorWishBean> wishInfo;
    public NewActivityDto newActivityDto;//普通活动
    /**
     * @return 轮播图数据   心愿礼物不能连续出现。
     * @isPush 是否是主播
     */
    public ArrayList<ActBannerBean> getActBanner(boolean isPush) {
        boolean addUnWish = true;//判断是否已经添加过未许愿数据
        ArrayList<ActBannerBean> anchorWishBeans = new ArrayList<>();
        //  anchorWishBeans.add(addWsjActView());// TODO 临时测试增加
        if (wishInfo != null && wishInfo.size() > 0) {
            for (AnchorWishBean anchorWishBean : wishInfo) {
                if (anchorWishBean.id > 0) {
                    anchorWishBeans.add(addWishView(anchorWishBean));//添加心愿
                    if (pkActivityDto != null && pkActivityDto.getActStatus() == 1) {//活动进行中
                        anchorWishBeans.add(addPkActView());//添加pk排位赛活动
                    }
                    if (activityDto != null && activityDto.getActStatus() == 1) {
                        anchorWishBeans.add(addnormalActView());//普通活动
                    }

                    if (newActivityDto != null && newActivityDto.getActStatus() == 1) {
                        anchorWishBeans.add(setActivityTypeView(newActivityDto.getActivityId()));
                    }
                    addUnWish = false;
                }
            }
            if (isPush && addUnWish) {//主播显示未许愿
                ActBannerBean actBannerBean = new ActBannerBean();
                actBannerBean.type = 2;
                actBannerBean.wishStatus = 0;
                anchorWishBeans.add(actBannerBean);
                if (pkActivityDto != null && pkActivityDto.getActStatus() == 1) {//活动进行中
                    anchorWishBeans.add(addPkActView());//添加pk排位赛活动
                }
                if (activityDto != null && activityDto.getActStatus() == 1) {
                    anchorWishBeans.add(addnormalActView());//普通活动
                }
                if (newActivityDto != null && newActivityDto.getActStatus() == 1) {
                    anchorWishBeans.add(setActivityTypeView(newActivityDto.getActivityId()));
                }
            }
        } else {
            if (pkActivityDto != null && pkActivityDto.getActStatus() == 1) {//活动进行中
                anchorWishBeans.add(addPkActView());//添加pk排位赛活动
            }
            if (activityDto != null && activityDto.getActStatus() == 1) {
                anchorWishBeans.add(addnormalActView());//普通活动
            }
            if (newActivityDto != null && newActivityDto.getActStatus() == 1) {
                anchorWishBeans.add(setActivityTypeView(newActivityDto.getActivityId()));
            }
        }
        return anchorWishBeans;
    }
    private ActBannerBean setActivityTypeView(int activityid){
        switch (newActivityDto.getActivityId()){
            case 7://万圣节
                return addWsjActView();
            case  8://圣诞节
                return addSdActView();
            case 9://元旦
                return  addSdActViewTy();
            case 10://新年
                return addSdActView();
            case 11://新年
                return addSdActView();
            case 12://新年
                return addSdActView();
            case 13://新年
                return addSdActView();
            case 14://新年
                return addSdActView();
            case 15://新年
                return addSdActView();
            case 16://新年
                return addSdActView();
            case 17://女神节
                return addSdActView();
        }
        return null;
    }

    @NotNull
    private ActBannerBean addPkActView() {
        ActBannerBean actBean = new ActBannerBean();
        actBean.backgroundImg = pkActivityDto.getBackgroundImg();
        actBean.address = pkActivityDto.getAddress();
        actBean.levelIcon = pkActivityDto.getLevelIcon();
        actBean.combatScore = pkActivityDto.getCombatScore();
        actBean.winningStreakNum = pkActivityDto.getWinningStreakNum();
        actBean.losingStreakNum = pkActivityDto.getLosingStreakNum();

        actBean.type = 5;
        return actBean;
    }
    //圣诞活动
    @NotNull
    private ActBannerBean addSdActViewTy() {
        ActBannerBean actBean = new ActBannerBean();
        actBean.sort = newActivityDto.getSort();
        actBean.castleSort = newActivityDto.getCastleSort();
        actBean.refreshProcesses = newActivityDto.getRefreshProcesses();
        actBean.planeOpenSecond = newActivityDto.getPlaneOpenSecond();
        actBean.isFinish = newActivityDto.isFinish();
        actBean.isChance = newActivityDto.isChance();
        actBean.address = newActivityDto.getAddress();
        actBean.integral = newActivityDto.getIntegral();
//        actBean.hourProcesses =  newActivityDto.getHourProcesses();
//        actBean.residueProcesses =  newActivityDto.getResidueProcesses();
        actBean.type = newActivityDto.getActivityId();
        return actBean;
    }
    // 女神节
    @NotNull
    private ActBannerBean addSdActView() {
        ActBannerBean actBean = new ActBannerBean();
        actBean.sort = newActivityDto.getSort();
        actBean.castleSort = newActivityDto.getCastleSort();
        actBean.refreshProcesses = newActivityDto.getRefreshProcesses();
        actBean.planeOpenSecond = newActivityDto.getPlaneOpenSecond();
        actBean.isFinish = newActivityDto.isFinish();
        actBean.address = newActivityDto.getAddress();
        actBean.integral = newActivityDto.getIntegral();
        actBean.type = 17;
        return actBean;
    }
    //万圣节活动
    @NotNull
    private ActBannerBean addWsjActView() {
        ActBannerBean actBean = new ActBannerBean();
        actBean.sort = newActivityDto.getSort();
        actBean.castleSort = newActivityDto.getCastleSort();
        actBean.refreshProcesses = newActivityDto.getRefreshProcesses();
        actBean.planeOpenSecond = newActivityDto.getPlaneOpenSecond();
        actBean.isFinish = newActivityDto.isFinish();
        actBean.address = newActivityDto.getAddress();
        actBean.type = 7;
        return actBean;
    }
    @NotNull
    private ActBannerBean addnormalActView() {
//        List<ActBannerBean> actBeanList = new ArrayList<>();
//        for (int i = 0;i<activityDto.getUserLives().size();i++) {
        ActBannerBean actBean = new ActBannerBean();
        actBean.backgroundImg = activityDto.getBackgroundImg();
        actBean.address = activityDto.getUserLives().get(0).getAddress();
        actBean.integral = activityDto.getUserLives().get(0).getIntegral();
        actBean.sort = activityDto.getUserLives().get(0).getSort();
        actBean.integralInfo = activityDto.getUserLives().get(0).getIntegralInfo();
        actBean.sortInfo = activityDto.getUserLives().get(0).getSortInfo();
//            actBean.winningStreakNum = activityDto.getWinningStreakNum();
//            actBean.losingStreakNum = activityDto.getLosingStreakNum();
        actBean.type = 6;
//            actBeanList.add(actBean);
//        }
        return actBean;
    }

    @NotNull
    private ActBannerBean addWishView(AnchorWishBean anchorWishBean) {
        ActBannerBean wishBean = new ActBannerBean();
        wishBean.icon = anchorWishBean.icon;
        wishBean.wishSpeed = anchorWishBean.wishQuantity;
        wishBean.factSpeed = anchorWishBean.factQuantity;
        wishBean.type = 2;
        wishBean.wishStatus = 1;
        wishBean.wishType = anchorWishBean.wishType;
        return wishBean;
    }

}
