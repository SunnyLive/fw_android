package com.fengwo.module_vedio.mvp.dto;

import java.io.Serializable;
import java.util.List;

public class SearchResultDto implements Serializable {
    public List<ShortVedioInfo> movieInfoDTOList;
    public List<SmallVedioInfo> shortVideoInfoDTOList;
}
