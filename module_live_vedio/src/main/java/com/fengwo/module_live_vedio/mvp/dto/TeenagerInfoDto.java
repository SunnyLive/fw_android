/**
  * Copyright 2020 bejson.com 
  */
package com.fengwo.module_live_vedio.mvp.dto;

/**
 * Auto-generated: 2020-12-09 11:46:8
 *青少年模式信息请求数据
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TeenagerInfoDto {

    private int id;
    private int teenagerDuration;
    private String teenagerMode;
    private String teenagerPassword;

    public TeenagerInfoDto() {
        super();
    }

    public TeenagerInfoDto(int id, int teenagerDuration, String teenagerMode, String teenagerPassword) {
        this.id = id;
        this.teenagerDuration = teenagerDuration;
        this.teenagerMode = teenagerMode;
        this.teenagerPassword = teenagerPassword;
    }

    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setTeenagerDuration(int teenagerDuration) {
         this.teenagerDuration = teenagerDuration;
     }
     public int getTeenagerDuration() {
         return teenagerDuration;
     }

    public void setTeenagerMode(String teenagerMode) {
         this.teenagerMode = teenagerMode;
     }
     public String getTeenagerMode() {
         return teenagerMode;
     }

    public void setTeenagerPassword(String teenagerPassword) {
         this.teenagerPassword = teenagerPassword;
     }
     public String getTeenagerPassword() {
         return teenagerPassword;
     }

}