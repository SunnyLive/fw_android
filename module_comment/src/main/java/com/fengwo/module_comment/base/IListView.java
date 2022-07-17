package com.fengwo.module_comment.base;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface IListView<T> extends MvpView {
    void setData(List<T> datas, int page);

    Flowable httpRequest();
}
