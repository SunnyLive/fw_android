package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.ProfitDto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/18
 */
public interface IProfitView extends MvpView {

    void setProfitData(ProfitDto profitData);
}
