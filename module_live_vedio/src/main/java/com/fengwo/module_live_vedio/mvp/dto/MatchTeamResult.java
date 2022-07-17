package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

/**尼玛币的刘贵廉 还尼玛不写备注
 * @author GuiLianL
 * @intro
 * @date 2019/11/7
 */
public class MatchTeamResult extends MatchSingleResult implements Serializable {

    private List<PkTeamInfo> objectMembers;
    private List<PkTeamInfo> members;
    //("PK类型 1：单人 2： 组团 3：战队")
    private int type;
   //("用户PK胜负结果 1：胜 0：平 -1：负")
    private int userResult;
    private int attentionObject;//0 未关注  1 已关注
    private String pkId;

    public String getPkId() {
        return TextUtils.isEmpty(pkId)?"":pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public int getAttentionObject() {
        return attentionObject;
    }

    public void setAttentionObject(int attentionObject) {
        this.attentionObject = attentionObject;
    }

    public List<PkTeamInfo> getObjectMembers() {
        return objectMembers;
    }

    public void setObjectMembers(List<PkTeamInfo> objectMembers) {
        this.objectMembers = objectMembers;
    }

    public List<PkTeamInfo> getMembers() {
        return members;
    }

    public void setMembers(List<PkTeamInfo> members) {
        this.members = members;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserResult() {
        return userResult;
    }

    public void setUserResult(int userResult) {
        this.userResult = userResult;
    }

}
