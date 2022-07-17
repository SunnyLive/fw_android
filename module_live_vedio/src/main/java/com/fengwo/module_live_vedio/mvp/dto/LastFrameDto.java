package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/8/18
 */
public class LastFrameDto {

    /**
     * lastInnerFrame : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/activityList/1597734697000*activityList2532295244.png
     * lastFrame : null
     * lastLable : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/activityList/1597734708000*activityList754293282.png
     * lastInnerLable : null
     */

    private String lastInnerFrame;
    private String lastFrame;
    private String lastLable;
    private String lastInnerLable;
    private String lastChannelInnerFrame;
    private String lastChannelInnerLable;

    public String getLastInnerFrame() {
        return lastInnerFrame;
    }

    public void setLastInnerFrame(String lastInnerFrame) {
        this.lastInnerFrame = lastInnerFrame;
    }

    public String getLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(String lastFrame) {
        this.lastFrame = lastFrame;
    }

    public String getLastLable() {
        return lastLable;
    }

    public void setLastLable(String lastLable) {
        this.lastLable = lastLable;
    }

    public String getLastInnerLable() {
        return lastInnerLable;
    }

    public void setLastInnerLable(String lastInnerLable) {
        this.lastInnerLable = lastInnerLable;
    }

    public String getLastChannelInnerFrame() {
        return lastChannelInnerFrame;
    }

    public void setLastChannelInnerFrame(String lastChannelInnerFrame) {
        this.lastChannelInnerFrame = lastChannelInnerFrame;
    }

    public String getLastChannelInnerLable() {
        return lastChannelInnerLable;
    }

    public void setLastChannelInnerLable(String lastChannelInnerLable) {
        this.lastChannelInnerLable = lastChannelInnerLable;
    }
}
