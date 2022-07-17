package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.ReportLabelDto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
public interface IReportView extends MvpView {

    void returnLabel(BaseListDto<ReportLabelDto> listDto);
    void returnCommit(HttpResult httpResult);
}
