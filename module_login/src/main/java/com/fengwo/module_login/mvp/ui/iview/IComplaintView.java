package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
public interface IComplaintView extends MvpView {

    void returnCommit(HttpResult httpResult);
}
