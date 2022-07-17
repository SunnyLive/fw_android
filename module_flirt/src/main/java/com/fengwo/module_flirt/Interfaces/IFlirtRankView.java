package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.FlirtRankBean;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/5/12
 */
public interface IFlirtRankView extends MvpView {

    void setFlirtRankList(BaseListDto<FlirtRankBean> list);
}
