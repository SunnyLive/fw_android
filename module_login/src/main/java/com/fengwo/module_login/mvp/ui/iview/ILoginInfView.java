package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;

public interface ILoginInfView extends MvpView {

    void success();
    void fail(String message);
    void error(String error);

}
