package com.fengwo.module_login.eventbus;

public class UpdatePhotoEvent {

    public int position;
    public String url;
    public int status;

    public UpdatePhotoEvent(int position,String url,int status){
        this.position = position;
        this.url = url;
        this.status = status;
    }

}
