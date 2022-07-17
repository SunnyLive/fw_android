package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;

public interface IRealAnchorView extends MvpView {

    void onPostAnchorIDCardSuccess(String msg);

    void onPostAnchorIDCardError(String msg);

    void onUpLoadSuccess(String url,int type);

}
