package com.fengwo.module_login.mvp.dto;

public class UnionInfo {
    /**
     * familyVO : {"familyCode":59999,"familyName":"59999红帆公会","familyLogo":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/guild/1581574348000*1002268.jpg","familyIntroduction":"主播MCN机构","applyStatus":0}
     * wenFamilyVO : {"familyCode":58888,"familyName":"58888众星聚","familyLogo":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/platGuild/1596867345000*platGuild6445937221.png","familyIntroduction":"非常棒的公会，期待你的加入！","applyStatus":2
     *
     *
     *
     *
     * applyStatus	integer($int32)
     * 申请状态 0：拒绝 1：申请中 2：通过
     *
     * familyCode	integer($int32)
     * 公会席位号
     *
     * familyIntroduction	string
     * 公会简介
     *
     * familyLogo	string
     * 公会LOGO
     *
     * familyName	string
     * 公会名称
     *
     *
     *
     */

    public Union familyVO;
    public Union wenFamilyVO;

    public static class Union{
        public int familyCode;
        public String familyName;
        public String familyLogo;
        public String familyIntroduction;
        public int applyStatus;
    }


    /**
     *
     *
     *
     *
     * {
     * "familyVO": {
     *   "familyCode": 59999,
     *   "familyName": "59999红帆公会",
     *   "familyLogo": "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/guild/1581574348000*1002268.jpg",
     *   "familyIntroduction": "主播MCN机构",
     *   "applyStatus": 0
     * },
     * "wenFamilyVO": {
     *   "familyCode": 58888,
     *   "familyName": "58888众星聚",
     *   "familyLogo": "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/platGuild/1596867345000*platGuild6445937221.png",
     *   "familyIntroduction": "非常棒的公会，期待你的加入！",
     *   "applyStatus": 2
     * }
     *   }
     *
     *
     *
     *
     *
     *
     */








}
