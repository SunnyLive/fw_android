package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_login.mvp.dto.AllCarDto;
import com.fengwo.module_login.mvp.dto.MyCarDto;
import com.fengwo.module_login.mvp.ui.iview.IMyCarView;

import java.util.List;

import cc.shinichi.library.tool.ui.ToastUtil;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/16
 */
public class MyCarPresenter extends BaseLoginPresenter<IMyCarView> {

    public void getMyCarList() {
        addNet(
                service.getMyCarList()
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<MyCarDto>(getView()) {
                            @Override
                            public void _onNext(MyCarDto data) {
                                getView().setMyCarData(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        }));
    }

    public void getAllCarList() {
        addNet(
                service.getAllCarList()
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<AllCarDto>>() {

                            @Override
                            public void _onNext(List<AllCarDto> data) {
                                getView().setAllCarData(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        }));
    }

    public void buyCar(String id) {
        addNet(service.buyCar(id)
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult<PopoDto>>(getView()) {

                    @Override
                    public void _onNext(HttpResult<PopoDto> data) {
                        getView().buyCarReturn(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        HttpResult httpResult = new HttpResult();
                        httpResult.description = msg;
                        getView().buyCarReturn(httpResult);
                    }
                }));
    }

    public void openCar(int id) {
        addNet(service.openCar(id, 1)
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>(getView()) {

                    @Override
                    public void _onNext(HttpResult data) {
                        getView().openCarReturn(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        HttpResult httpResult = new HttpResult();
                        httpResult.description = msg;
                        getView().openCarReturn(httpResult);
                    }
                }));
    }

    public void getCarDetail(int id) {
        addNet(service.getCarDetail(id)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<AllCarDto>() {
                    @Override
                    public void _onNext(AllCarDto data) {
                        getView().getcarDetail(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }
}
