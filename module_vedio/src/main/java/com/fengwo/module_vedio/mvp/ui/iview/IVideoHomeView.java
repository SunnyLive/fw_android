package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_vedio.mvp.dto.AdvertiseBean;
import com.fengwo.module_vedio.mvp.dto.VideoHomeCategoryDto;

import java.util.ArrayList;

/**
 * @author Zachary
 * @date 2019/12/24
 */
public interface IVideoHomeView extends IBaseVideoView {
    void setCategory(ArrayList<VideoHomeCategoryDto> records);

    void setBannerData(ArrayList<AdvertiseBean> records);
}
