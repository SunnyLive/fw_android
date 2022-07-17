package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/30
 */
public interface IAccCancelConfirmView extends MvpView {

    void setCode(String code);
    void destroy();
    void cancelDestroy();
    void setMobile(String mobile);
}
