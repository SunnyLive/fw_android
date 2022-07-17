package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.CashOutDto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/29
 */
public interface ICashOutView extends MvpView {

    void returnPageDate(CashOutDto cashOutDto);
    void returnCommitResult(HttpResult httpResult);
}
