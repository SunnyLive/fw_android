package com.fengwo.module_login.mvp.dto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/3/28
 */
public class NickNameRuleBean {

    /**
     * modifyNameCost : 0
     * nickname : 用户512084
     * modifyTimes : 0
     * rules : ["规则说明：","每个用户都有一次免费改昵称的机会，之后改昵称都需要\n收费，收费规则如下：","第一次修改昵称需要10000花钻；","第二次修改昵称需要20000花钻；","第三次修改昵称需要40000花钻；","第四次（含）起修改昵称需要50000花钻；"]
     */

    private String modifyNameCost;
    private String nickname;
    private int modifyTimes;
    private List<String> rules;

    public String getModifyNameCost() {
        return modifyNameCost;
    }

    public void setModifyNameCost(String modifyNameCost) {
        this.modifyNameCost = modifyNameCost;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getModifyTimes() {
        return modifyTimes;
    }

    public void setModifyTimes(int modifyTimes) {
        this.modifyTimes = modifyTimes;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }
}
