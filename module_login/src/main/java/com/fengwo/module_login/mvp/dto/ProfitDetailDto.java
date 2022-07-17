package com.fengwo.module_login.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/18
 */
public class ProfitDetailDto {

    /**
     * afterMoney : 0
     * beforeMoney : 0
     * createTime : 2019-10-18T08:59:33.904Z
     * id : 0
     * money : 0
     * orderNo : string
     * otherId : 0
     * otherNickname : string
     * profitType : 0
     * type : 0
     * updateTime : 2019-10-18T08:59:33.904Z
     * userId : 0
     * userNickname : string
     */

    private double afterMoney;
    private double beforeMoney;
    private String createTime;
    private int id;
    private double money;
    private String orderNo;
    private int otherId;
    private String otherNickname;
    private int profitType;
    private int type;
    private String updateTime;
    private int userId;
    private String userNickname;

    public String getOriginType(){
        String s;
        switch (type){
            case 107:
                s = "退还";
                break;
            case 200:
                s= "提现";
                break;
            case 100:
            case 101:
            case 104:
            case 105:
            case 106:
            case 108:
                s= "收益";
                break;
                default:
                    s= "其他";
        }
        return s;
    }

    public double getAfterMoney() {
        return afterMoney;
    }

    public void setAfterMoney(double afterMoney) {
        this.afterMoney = afterMoney;
    }

    public double getBeforeMoney() {
        return beforeMoney;
    }

    public void setBeforeMoney(double beforeMoney) {
        this.beforeMoney = beforeMoney;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOtherId() {
        return otherId;
    }

    public void setOtherId(int otherId) {
        this.otherId = otherId;
    }

    public String getOtherNickname() {
        return otherNickname;
    }

    public void setOtherNickname(String otherNickname) {
        this.otherNickname = otherNickname;
    }

    public int getProfitType() {
        return profitType;
    }

    public void setProfitType(int profitType) {
        this.profitType = profitType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
