package com.fengwo.module_login.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/29
 */
public class CashOutDto {

    /**
     * exchangeRate : string
     * id : 0
     * profit : 0
     * totalProfit : 0
     * userId : 0
     * withdrawLimit : 0
     */

    private String exchangeRate;
    private int id;
    private double profit;
    private double totalProfit;
    private int userId;
    private int withdrawLimit;
    private int withdrawMaxLimit;

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWithdrawLimit() {
        return withdrawLimit;
    }

    public void setWithdrawLimit(int withdrawLimit) {
        this.withdrawLimit = withdrawLimit;
    }

    public int getWithdrawMaxLimit() {
        return withdrawMaxLimit;
    }

    public void setWithdrawMaxLimit(int withdrawMaxLimit) {
        this.withdrawMaxLimit = withdrawMaxLimit;
    }
}
