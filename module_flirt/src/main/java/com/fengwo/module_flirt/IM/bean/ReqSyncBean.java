package com.fengwo.module_flirt.IM.bean;

import com.fengwo.module_comment.utils.AppUtils;
import com.fengwo.module_login.utils.UserManager;
import com.google.gson.Gson;

/**
 * @Author BLCS
 * @Time 2020/5/5 10:41
 */
public class ReqSyncBean {
    public static String build(){
        int userId= UserManager.getInstance().getUser().getId();
        ReqSyncBean reqSyncBean = new ReqSyncBean();
        reqSyncBean.toUid = "-1";
        reqSyncBean.msgId = AppUtils.createMsgId(String.valueOf(userId));
        reqSyncBean.version = "2.0";
        reqSyncBean.vendor = AppUtils.getModel();
        reqSyncBean.description = "消息同步";
        reqSyncBean.timestamp = System.currentTimeMillis() + "";
        reqSyncBean.busiEvent = "req";
        DataBean dataBean = new DataBean();
        dataBean.setUserId(String.valueOf(userId));
        dataBean.setAction("sync");
        reqSyncBean.setData(dataBean);
        return new Gson().toJson(reqSyncBean);
    }
    /**
     * fromUid :
     * toUid : -1
     * msgId : 202004231425558571253208463557091322
     * version : 2.0
     * vendor : Huawei P30
     * description : 消息同步
     * timestamp : 15846568956
     * busiEvent : req
     * data : {"userId":"512695","action":"sync"}
     */

    private String fromUid;
    private String toUid;
    private String msgId;
    private String version;
    private String vendor;
    private String description;
    private String timestamp;
    private String busiEvent;
    private DataBean data;

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBusiEvent() {
        return busiEvent;
    }

    public void setBusiEvent(String busiEvent) {
        this.busiEvent = busiEvent;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 512695
         * action : sync
         */

        private String userId;
        private String action;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }
}
