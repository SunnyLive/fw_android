package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_vedio.mvp.dto.MenuDto;

import java.util.List;

public interface IBaseVideoMenuView extends MvpView {
    void setTypeMenu(List<MenuDto> data);
}
