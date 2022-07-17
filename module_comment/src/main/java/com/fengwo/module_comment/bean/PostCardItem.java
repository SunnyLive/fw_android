package com.fengwo.module_comment.bean;

import java.io.Serializable;

public class PostCardItem implements Serializable {

    private static final long serialVersionUID = -1511159133327556670L;

    public PostCardItem(String filePath, int width, int height, boolean isVideo) {
        this.filePath = filePath;
        this.width = width;
        this.height = height;
        this.isVideo = isVideo;
    }

    public String filePath;
    public int width;
    public int height;
    public boolean isVideo;
    public String videoPath;
    public String fileUrl;


    public int anchorId;
    public String coverImg;
    public int id;
    public String rPath = "";
    public int rType;//1:图片 2:视频
}
