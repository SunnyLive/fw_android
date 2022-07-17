package com.fengwo.module_login.mvp.dto;

public class GonghuiListDto {

    /**
     * about : string
     * account : string
     * accountName : string
     * accountNumber : string
     * agentId : 0
     * applyTime : 2019-12-07T08:52:47.642Z
     * bankAddress : string
     * bankBranch : string
     * bankName : string
     * businessLicence : string
     * companyAddress : string
     * companyName : string
     * contact : string
     * contactPhone : string
     * corporateBankingCert : string
     * cycle : 0
     * familyCommission : 0
     * familyImg : string
     * familyIntroduction : string
     * familyLogo : string
     * familyName : string
     * id : 0
     * legalPersonFront : string
     * legalPersonOpposite : string
     * memberNumber : 0
     * mobile : string
     * nickname : string
     * status : 0
     * type : 0
     * userId : 0
     * verifyTime : 2019-12-07T08:52:47.642Z
     *
     *
     *
     *
     * auditTime	string($date-time)
     * 审核时间
     *
     * channelLevel	integer($int32)
     * 主播等级
     *
     * createTime	string($date-time)
     * 创建时间
     *
     * divide	integer($int32)
     * 内部分成比例 单位 %
     *
     * familyId	integer($int32)
     * 所属公会ID
     *
     * familyUid	integer($int32)
     * 所属公会用户ID(冗余字段)
     *
     * id	integer($int32)
     * ID
     * otherDivide	integer($int32)
     * 外部分成比例 单位 %
     *
     * remark	string
     * 备注
     *
     * status	integer($int32)
     * 状态 0：拒绝 1：申请中 2：通过
     *
     * userHeadImg	string
     * 用户头像
     *
     * userId	integer($int32)
     * 用户ID
     *
     * userNickname	string
     * 用户昵称
     *
     */

    private String about;
    private String account;
    private String accountName;
    private String accountNumber;
    private String familyId;
    private int agentId;
    private String applyTime;
    private String bankAddress;
    private String bankBranch;
    private String bankName;
    private String businessLicence;
    private String companyAddress;
    private String companyName;
    private String contact;
    private String contactPhone;
    private String corporateBankingCert;
    private int cycle;
    private int familyCommission;
    private String familyImg;
    private String familyIntroduction;
    private String familyLogo;
    private String familyName;
    private int id;
    private String legalPersonFront;
    private String legalPersonOpposite;
    private int memberNumber;
    private String mobile;
    private String nickname;
    private int status;
    private int type;
    private int userId;
    private String verifyTime;
    private String familyCode;

    public String getFamilyCode(){
        return familyCode;
    }

    public String getFamilyId(){
        return familyId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getCorporateBankingCert() {
        return corporateBankingCert;
    }

    public void setCorporateBankingCert(String corporateBankingCert) {
        this.corporateBankingCert = corporateBankingCert;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getFamilyCommission() {
        return familyCommission;
    }

    public void setFamilyCommission(int familyCommission) {
        this.familyCommission = familyCommission;
    }

    public String getFamilyImg() {
        return familyImg;
    }

    public void setFamilyImg(String familyImg) {
        this.familyImg = familyImg;
    }

    public String getFamilyIntroduction() {
        return familyIntroduction;
    }

    public void setFamilyIntroduction(String familyIntroduction) {
        this.familyIntroduction = familyIntroduction;
    }

    public String getFamilyLogo() {
        return familyLogo;
    }

    public void setFamilyLogo(String familyLogo) {
        this.familyLogo = familyLogo;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLegalPersonFront() {
        return legalPersonFront;
    }

    public void setLegalPersonFront(String legalPersonFront) {
        this.legalPersonFront = legalPersonFront;
    }

    public String getLegalPersonOpposite() {
        return legalPersonOpposite;
    }

    public void setLegalPersonOpposite(String legalPersonOpposite) {
        this.legalPersonOpposite = legalPersonOpposite;
    }

    public int getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(int memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }
}
