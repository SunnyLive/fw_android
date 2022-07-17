package com.fengwo.module_chat.base;

/**
 * @anchor Administrator
 * @date 2020/12/4
 */
public class OffcialBean {

    /**
     * id : 23
     * title : 5
     * subTitle : 子标题
     * content : <p><img src="https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/editorNews/1606294682000*editorNews5070614726.png"></p>
     * isCreateUrl : 0
     * url : null
     * sendTime : 2020-12-03 17:39
     * status : 0
     */

    private int id;
    private String title;
    private String subTitle;
    private String content;
    private int isCreateUrl;
    private Object url;
    private String sendTime;
    private int status;
    private long sendTimestamp;

    public long getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(long sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsCreateUrl() {
        return isCreateUrl;
    }

    public void setIsCreateUrl(int isCreateUrl) {
        this.isCreateUrl = isCreateUrl;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
