package com.fengwo.module_comment.base;

import com.fengwo.module_comment.bean.MyOrderDto;

import java.util.List;

/**
 * mvp activity实现接口父类
 */
public interface MvpView {
    void showLoadingDialog();

    boolean isLoadingDialogShow();

    void hideLoadingDialog();

    void toastTip(int msgId);

    void toastTip(CharSequence msg);

    void netError();

    void tokenIInvalid();

    void jump();

    void setDialogProgressPercent(String precent);




}