package com.fengwo.module_login.mvp.dto;

public class LoginDto {
    public String token;
    public String mobile;
    public boolean register;
    public int writeOffStatus = 0;
    public int userId;
    public String fwId;
    public boolean isEditInfo;//是否可以编辑这个信息
}
