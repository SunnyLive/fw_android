package com.fengwo.module_websocket.bean;

/**
 * @anchor Administrator
 * @date 2020/11/2
 */
public class BaseChatMsgBean {
    private SocketRequest<WenboWsChatDataBean> wenboWsChatDataBean;
    WenboGiftMsg gift;

    public BaseChatMsgBean(SocketRequest<WenboWsChatDataBean> wenboWsChatDataBean, WenboGiftMsg gift) {
        this.wenboWsChatDataBean = wenboWsChatDataBean;
        this.gift = gift;
    }

    public SocketRequest<WenboWsChatDataBean> getWenboWsChatDataBean() {
        return wenboWsChatDataBean;
    }

    public void setWenboWsChatDataBean(SocketRequest<WenboWsChatDataBean> wenboWsChatDataBean) {
        this.wenboWsChatDataBean = wenboWsChatDataBean;
    }

    public WenboGiftMsg getGift() {
        return gift;
    }

    public void setGift(WenboGiftMsg gift) {
        this.gift = gift;
    }
}
