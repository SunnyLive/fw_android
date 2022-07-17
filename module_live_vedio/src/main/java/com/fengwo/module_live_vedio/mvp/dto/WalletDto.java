package com.fengwo.module_live_vedio.mvp.dto;

public class WalletDto {
    public int balance;
    public int preBalance;//花钻 =  balance+preBalance
    public int charge;
    public String createTime;
    public int id;
    public int preCharge;
    public int preReceive;//
    public int preSpend;
    public double profit;
    public int spend;
    public double totalProfit;
    public String updateTime;
    public int userId;
    public int receive;

    public int getBalance() {
        return balance + preBalance;
    }
}
