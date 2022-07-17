package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;

import java.util.List;

public interface IThemeListMenuView extends IBaseVideoMenuView {
    void setThemeList(List<ShortVedioListDto.ShortVedio> records, int page);
}
