package com.fengwo.module_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/2
 */
public class NewVideoDto {

    /**
     * id : 28
     * post_title : 蜂窝有好料|马布汗： 生活表演艺术家，治愈你的不愉快。
     * show_type : 2
     * video_url :
     * thumb : http://fwtv.oss-cn-hangzhou.aliyuncs.com/fengwoUploadFile/backend/74029ef281107746c18aef0cb5baf615d513af00b479.jpg
     */

    private String id;
    private String post_title;
    private String show_type;
    private String video_url;
    private String thumb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getShow_type() {
        return show_type;
    }

    public void setShow_type(String show_type) {
        this.show_type = show_type;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
