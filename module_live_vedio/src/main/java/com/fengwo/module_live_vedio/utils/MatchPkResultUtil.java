package com.fengwo.module_live_vedio.utils;

import com.fengwo.module_live_vedio.mvp.dto.MatchSingleResult;
import com.fengwo.module_live_vedio.mvp.dto.MatchTeamResult;
import com.fengwo.module_live_vedio.mvp.dto.PkTeamInfo;
import com.fengwo.module_live_vedio.mvp.dto.PkUserInfo;
import com.fengwo.module_live_vedio.mvp.dto.SinglePkResultDto;

import java.util.ArrayList;
import java.util.List;

/** pk状态 下面这个sb也不写个备注 还让老子写
 * @author GuiLianL
 * @intro
 * @date 2019/11/7
 */
public class MatchPkResultUtil {

    PkType pkType = PkType.SINGLE;//pk类型
    public MatchTeamResult matchTeamResult;//pk的状态和对方的信息

    public void setPkType(PkType pkType) {
        this.pkType = pkType;
    }

    public void setMatchTeamResult(MatchTeamResult matchTeamResult) {
        this.matchTeamResult = matchTeamResult;
        pkType = PkType.TEAM;
    }

    public void setMatchSingleResult(MatchTeamResult matchTeamResult) {
        this.matchTeamResult = (MatchTeamResult) matchTeamResult;
        pkType = PkType.SINGLE;
    }
    public MatchTeamResult getMatchTeamResult() {
        return matchTeamResult;
    }

    public PkUserInfo getUserInfo(){
        return matchTeamResult.getUserInfo();
    }
    public PkUserInfo getOtherInfo(){
        return matchTeamResult.getObjectInfo();
    }
    public List<PkTeamInfo> getUserTeamInfo(){
        List<PkTeamInfo> teamInfos = new ArrayList<>();
        if (pkType == PkType.SINGLE) {
            if (matchTeamResult.getUserInfo()==null)return teamInfos;
            PkTeamInfo pkTeamInfo = new PkTeamInfo();
            if (matchTeamResult.getUserInfo().getHeadImg()!=null){
                pkTeamInfo.setHeadImg(matchTeamResult.getUserInfo().getHeadImg());
            }
            pkTeamInfo.setNickname(matchTeamResult.getUserInfo().getNickname());
            pkTeamInfo.setUserId(matchTeamResult.getUserInfo().getUserId());
            teamInfos.add(pkTeamInfo);
        }else if (pkType == PkType.TEAM){
            teamInfos.addAll(matchTeamResult.getMembers());
        }
        return teamInfos;
    }
    public  List<PkTeamInfo> getOtherTeamInfo(){
        List<PkTeamInfo> teamInfos = new ArrayList<>();
        if (pkType == PkType.SINGLE) {
            if (matchTeamResult.getObjectInfo()==null)return teamInfos;
            PkTeamInfo pkTeamInfo = new PkTeamInfo();
            if (matchTeamResult.getObjectInfo().getHeadImg()!=null)
            pkTeamInfo.setHeadImg(matchTeamResult.getObjectInfo().getHeadImg());
            pkTeamInfo.setNickname(matchTeamResult.getObjectInfo().getNickname());
            pkTeamInfo.setUserId(matchTeamResult.getObjectInfo().getUserId());
            teamInfos.add(pkTeamInfo);
        }else if (pkType == PkType.TEAM){
            teamInfos.addAll(matchTeamResult.getObjectMembers());
        }
        return teamInfos;
    }

    //获取除自己以外的我方成员
    public List<PkTeamInfo> getUserTeamInfoWithoutUser(){
        List<PkTeamInfo> teamInfos = matchTeamResult.getMembers();
        if (pkType == PkType.TEAM){
            for (int i = 0;i<teamInfos.size();i++) {
                if (matchTeamResult.getUserInfo().getUserId() == teamInfos.get(i).getUserId()){
                    teamInfos.remove(i);
                    break;
                }
            }
        }
        return teamInfos;
    }
    //获取除对方以外的对方成员
    public  List<PkTeamInfo> getOtherTeamInfoWithoutOther(){
        List<PkTeamInfo> teamInfos = matchTeamResult.getObjectMembers();
        if (pkType == PkType.TEAM){
            for (int i = 0;i<teamInfos.size();i++) {
                if (matchTeamResult.getObjectInfo().getUserId() == teamInfos.get(i).getUserId()){
                    teamInfos.remove(i);
                    break;
                }
            }
        }
        return teamInfos;
    }

    public int getPkTime(){
        return matchTeamResult.getPkTime();
    }
    public int getPunishTime(){
        return matchTeamResult.getPunishTime();
    }

    public long getStartTime(){
        return  matchTeamResult.getStartTime();
    }

    public void clear(){
        matchTeamResult = null;
    }

}
