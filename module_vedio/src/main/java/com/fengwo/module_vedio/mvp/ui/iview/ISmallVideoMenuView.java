package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_vedio.mvp.dto.SmallVideoMenuDto;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/9
 */
public interface ISmallVideoMenuView extends IBaseVideoView {

    void  setSmallVideoMenu(BaseListDto<SmallVideoMenuDto> listDto);
}
