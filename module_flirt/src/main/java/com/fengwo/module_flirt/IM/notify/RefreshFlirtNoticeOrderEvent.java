package com.fengwo.module_flirt.IM.notify;

import com.fengwo.module_flirt.IM.bean.OrderMessageBean;

/**
 * 主播订单通知
 *
 * @Author BLCS
 * @Time 2020/4/30 17:52
 */
public class RefreshFlirtNoticeOrderEvent {
    public OrderMessageBean orderMessageBean;
    public RefreshFlirtNoticeOrderEvent(OrderMessageBean orderMessageBean) {
        this.orderMessageBean = orderMessageBean;
    }
}
