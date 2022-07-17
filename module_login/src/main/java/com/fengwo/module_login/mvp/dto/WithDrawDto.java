package com.fengwo.module_login.mvp.dto;

public class WithDrawDto {

    /**
     * id : 61
     * orderNo : 2020032817181243829899627655169
     * userId : 511999
     * userNickname : null
     * targetId : 0
     * type : 1
     * money : 6
     * fee : 0.42
     * thirdFee : 0
     * actualMoney : 5.58
     * withdrawType : 1
     * status : 2
     * remark : 123
     * auditId : 511493
     * thirdNo :
     * bankName : 中国农业银行
     * bankMobile : 13752227900
     * bankCardNumber : 6228480028110988178
     * bankTrueName : 林津津
     * createTime : 2020-03-28T09:18:52Z
     * updateTime : 2020-03-28T09:19:46Z
     */

    private int id;
    private String orderNo;
    private int userId;
    private String userNickname;
    private int targetId;
    private int type;
    private double money;
    private double fee;
    private double thirdFee;
    private double actualMoney;
    private int withdrawType;
    private int status;
    private String remark;
    private int auditId;
    private String thirdNo;
    private String bankName;
    private String bankMobile;
    private String bankCardNumber;
    private String bankTrueName;
    private String createTime;
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getThirdFee() {
        return thirdFee;
    }

    public void setThirdFee(double thirdFee) {
        this.thirdFee = thirdFee;
    }

    public double getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(double actualMoney) {
        this.actualMoney = actualMoney;
    }

    public int getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(int withdrawType) {
        this.withdrawType = withdrawType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public String getThirdNo() {
        return thirdNo;
    }

    public void setThirdNo(String thirdNo) {
        this.thirdNo = thirdNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankTrueName() {
        return bankTrueName;
    }

    public void setBankTrueName(String bankTrueName) {
        this.bankTrueName = bankTrueName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
