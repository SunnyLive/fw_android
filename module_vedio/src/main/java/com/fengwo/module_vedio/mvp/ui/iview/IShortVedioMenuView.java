package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;

import java.util.List;

public interface IShortVedioMenuView extends IBaseVideoMenuView {
    void setHotShortVedio(List<ShortVedioListDto.ShortVedio> data);

    void setLikeShortVedio(List<ShortVedioListDto.ShortVedio> data);
}
