package com.fengwo.module_comment.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/9
 */
public class ActBannerBean {

    public boolean isPush;//是否主播

    /*心愿数据*/
    public String icon;//礼物图标
    public int type;//类型 1：积分 2：心愿 3：签到 4:航仓（红包雨入口） 5:pk排位赛活动 6：普通活动  7:宝箱
    public int factSpeed;
    public int wishSpeed;
    public int wishType;// 1：本日 2：本周 3：本月
    public int wishStatus;//心愿礼物状态 1：已许愿 2：未许愿

    /*活动数据*/
    public String backgroundImg;//活动背景
    public String address;// h5地址
    public String levelIcon;// 段位图片
    public int combatScore;// 战力值
    public int winningStreakNum;// 连胜次数
    public int losingStreakNum;// 连败次数

    /*普通活动数据*/
    public int integral;//积分
    public int sort;//排名
    public int castleSort;

    //航仓数据
    public int planeOpenSecond;//航仓开启倒计时
    public int boxId;//航仓id
    public boolean userIsOpen;//用户是否开启过

    public String integralInfo;//
    public String sortInfo;//
    public boolean isFinish = false;
    public boolean isChance;



    public List<RefreshProcessesBean> refreshProcesses;
//    public List<HourProcessesBean> hourProcesses;
//    public List<ResidueProcessesBean> residueProcesses;
}
