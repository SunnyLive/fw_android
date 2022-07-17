package com.fengwo.module_flirt.bean;

import com.fengwo.module_comment.bean.PostCardItem;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/28
 */
public class CerMsgBean {
    /**
     * anchorId : 0
     * audioLength : 0
     * audioPath : string
     * occuName : string
     * resList : [{"anchorId":0,"coverImg":"string","id":0,"rPath":"string","rType":0}]
     * tagName : string
     */

    private int anchorId;
    private int audioLength;
    private String audioPath;
    private String occuName;
    private String tagName;
    private String remark;
    public String getRemark(){
        return remark;
    }

    private List<PostCardItem> resList;

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public int getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(int audioLength) {
        this.audioLength = audioLength;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getOccuName() {
        return occuName;
    }

    public void setOccuName(String occuName) {
        this.occuName = occuName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<PostCardItem> getResList() {
        return resList;
    }

    public void setResList(List<PostCardItem> resList) {
        this.resList = resList;
    }

}
