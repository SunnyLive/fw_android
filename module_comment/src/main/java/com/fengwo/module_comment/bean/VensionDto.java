package com.fengwo.module_comment.bean;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/17
 */
public class VensionDto {

    /**
     * androidUrl : string
     * appVersion : string
     * createDate : 2020-01-17T02:08:49.332Z
     * forceUpdate : 0
     * id : 0
     * iosUrl : string
     * state : 0
     * updateDate : 2020-01-17T02:08:49.333Z
     * updateInfo : string
     */

    private String androidUrl;
    private String appVersion;
    private String createDate;
    private int forceUpdate;
    private int id;
    private String iosUrl;
    private int state;
    private String updateDate;
    private String updateInfo;





    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}
