package com.fengwo.module_comment.bean;

import java.math.BigDecimal;

/**
 * @anchor Administrator
 * @date 2020/10/23
 */
public class RefreshProcessesBean {
    /**
     * customsTotalValue : 0
     * customsValue : 0
     * giftId : 0
     * giftName : string
     */

    private BigDecimal customsTotalValue;
    private BigDecimal customsValue;
    private int giftId;
    private String giftName;
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public BigDecimal getCustomsTotalValue() {
        return customsTotalValue;
    }

    public void setCustomsTotalValue(BigDecimal customsTotalValue) {
        this.customsTotalValue = customsTotalValue;
    }

    public BigDecimal getCustomsValue() {
        return customsValue;
    }

    public void setCustomsValue(BigDecimal customsValue) {
        this.customsValue = customsValue;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }
}