package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_vedio.mvp.dto.SmallVedioListDto;

import java.util.List;

public interface ISmallVedioMenuView extends IBaseVideoMenuView {
    void setSmallVedioList(List<SmallVedioListDto.Record> datas, int page);

}
