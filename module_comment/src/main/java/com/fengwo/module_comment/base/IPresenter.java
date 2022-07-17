package com.fengwo.module_comment.base;

public interface IPresenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}