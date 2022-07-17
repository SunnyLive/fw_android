package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_comment.bean.GreetTipsBean;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.LiveStatusDto;
import com.fengwo.module_login.mvp.ui.iview.IMineDetailView;

import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import okhttp3.RequestBody;

public class MineDetailPresenter extends BaseLoginPresenter<IMineDetailView> {

    public void getCardByUserId(int page, int userId) {
        addNet(service.getCardByUser(page + "," + PAGE_SIZE, userId + "")
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<CircleListBean>>() {
                    @Override
                    public void _onNext(BaseListDto<CircleListBean> data) {

                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    public void addBlack(int userId) {
        addNet(service.addBlack(userId)
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {

                    @Override
                    public void _onNext(HttpResult data) {
                        getView().toastTip(data.description);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    public void greetDiscuss(String userId, String content) {
        addNet(service.greetDiscuss(new ParamsBuilder()
                .put("acceptUserId", userId)
                .put("content", content).build())
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {

                    @Override
                    public void _onNext(HttpResult data) {
//                        getView().greet(content);
                        if (data.isSuccess()) {
                            getView().toastTip("打招呼成功");
                            getView().greet(content);
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }
    public void getLiveStatus(int userId) {
        addNet(service.getLiveStatus( userId + "")
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult<LiveStatusDto>>() {

                    @Override
                    public void _onNext(HttpResult<LiveStatusDto> data) {
                        if (data.isSuccess()) {
                            getView().getLiveStatus(data.data);
                        }

                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    public void greetTipsList(int page) {
        RequestBody build = new WenboParamsBuilder()
                .put("current", page + "")
                .put("size", 10 + "")
                .build();
        LoginApiService  service = new RetrofitUtils().createWenboApi(LoginApiService.class);
        compositeDisposable.add(service
                .greetTipsList(build)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<GreetTipsBean>>() {
                    @Override
                    public void _onNext(BaseListDto<GreetTipsBean> data) {
                        List<String> dataList = StreamSupport.stream(data.records).map(e -> e.title).collect(Collectors.toList());
                        getView().setGreetTips(dataList,data.pages);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }
}
