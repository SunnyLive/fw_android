package com.fengwo.module_live_vedio.mvp.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/7
 */
public class TeamPkResultDto extends PkRankDto implements Serializable {
    /**
     * list : [{"channel":{"headImg":"string","nickname":"string","pullUrlFLV":"string","pullUrlHLS":"string","pullUrlRTMP":"string","pushUrl":"string"},"isOver":0,"isWin":0,"memberId":0,"otherChannel":{"headImg":"string","nickname":"string","pullUrlFLV":"string","pullUrlHLS":"string","pullUrlRTMP":"string","pushUrl":"string"},"otherId":0,"otherIsOver":0,"otherIsWin":0}]
     * otherTeamPoint : 0
     * teamIsWin : 0
     * teamPoint : 0
     * userId : 0
     */

    private int otherTeamPoint;
    private int teamIsWin;
    private int teamPoint;
    private int normal;//1:正常  0投降
    private int userId;
    private long totalPoint;
    private long otherTotalPoint;
    private List<TeamPkItemResultDto> list;

    public int getOtherTeamPoint() {
        return otherTeamPoint;
    }

    public void setOtherTeamPoint(int otherTeamPoint) {
        this.otherTeamPoint = otherTeamPoint;
    }

    public int getTeamIsWin() {
        return teamIsWin;
    }

    public void setTeamIsWin(int teamIsWin) {
        this.teamIsWin = teamIsWin;
    }

    public int getTeamPoint() {
        return teamPoint;
    }

    public void setTeamPoint(int teamPoint) {
        this.teamPoint = teamPoint;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(long totalPoint) {
        this.totalPoint = totalPoint;
    }

    public long getOtherTotalPoint() {
        return otherTotalPoint;
    }

    public void setOtherTotalPoint(long otherTotalPoint) {
        this.otherTotalPoint = otherTotalPoint;
    }

    public List<TeamPkItemResultDto> getList() {
        return list;
    }

    public void setList(List<TeamPkItemResultDto> list) {
        this.list = list;
    }
}
