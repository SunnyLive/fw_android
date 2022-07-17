package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;
import com.fengwo.module_vedio.mvp.dto.SmallVedioListDto;

import java.util.List;

public interface ISquareView extends IBaseVideoView {
    void setGuessLike(List<ShortVedioListDto.ShortVedio> records);

    void setSmallVedioList(List<SmallVedioListDto.Record> records);
}
