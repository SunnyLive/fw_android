package com.fengwo.module_comment.base;

import com.fengwo.module_comment.utils.L;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BaseListPresenter<T> extends BasePresenter<IListView<T>> {
    private Flowable listObservable;

    private CompositeDisposable container = new CompositeDisposable();

    public BaseListPresenter(Flowable o) {
        listObservable = o;
    }

    public void getListData(int page) {
        if (null == listObservable) {
            getView().setData(new ArrayList<>(), 1);
            return;
        }
        Disposable subscribe = getView().httpRequest()
                .compose(io_main())
                .compose(handleResult())
                .subscribe((Consumer<BaseListDto<T>>) data -> {
                    if (null == getView()) return;
                    if (null != data.records) {
                        getView().setData(data.records, page);
                    } else {
                        getView().setData(new ArrayList<>(), 1);
                    }
                }, throwable -> {
                    L.e("列表页错误", throwable.toString());
                    getView().setData(new ArrayList<>(), 1);
                });
        container.add(subscribe);
    }

    @Override
    public void detachView() {
        super.detachView();
        container.dispose();
    }
}
