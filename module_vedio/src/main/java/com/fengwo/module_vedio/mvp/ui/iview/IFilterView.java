package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_vedio.mvp.dto.FilterLabelDto;

public interface IFilterView extends IBaseVideoView {
    void setLabs(FilterLabelDto data);
}
