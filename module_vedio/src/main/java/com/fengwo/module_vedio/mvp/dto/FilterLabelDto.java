package com.fengwo.module_vedio.mvp.dto;

import java.util.List;

public class FilterLabelDto {

    public List<VideoLab> video;
    public List<VideoLab> label;
    public List<VideoLab> movie;
    public List<VideoLab> shortVideo;

    public class VideoLab {
        public int id;
        public String videoType;
        public String videoDetail;
    }
}
