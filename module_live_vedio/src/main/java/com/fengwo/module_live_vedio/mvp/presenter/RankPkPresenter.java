package com.fengwo.module_live_vedio.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.RankSinglePkDto;
import com.fengwo.module_live_vedio.mvp.ui.iview.IRankPkView;

import java.util.List;

import cc.shinichi.library.tool.ui.ToastUtil;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/19
 */
public class RankPkPresenter extends BaseLivePresenter<IRankPkView> {

    public void getSinglePk() {
        addNet(
                service.getRankSinglePk()
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<RankSinglePkDto>>() {
                            @Override
                            public void _onNext(List<RankSinglePkDto> data) {
                                if (getView() == null) return;
                                getView().singlePk(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        }));
    }

    public void getTeamPk() {
        addNet(
                service.getRankTeamPk()
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<RankSinglePkDto>>() {
                            @Override
                            public void _onNext(List<RankSinglePkDto> data) {
                                if (getView() == null) return;
                                getView().teamPk(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        }));
    }

    public void getGuildPk() {
        addNet(
                service.getRankGuildPk()
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<RankSinglePkDto>>() {
                            @Override
                            public void _onNext(List<RankSinglePkDto> data) {
                                if (getView() == null) return;
                                getView().guildPk(data);
                            }

                            @Override
                            public void _onError(String msg) {
                                if (getView() != null) {
                                    getView().finishRefresh();
                                }
                            }
                        }));
    }

    //关注
    public void attention(int id) {
        addNet(
                service.addAttention(String.valueOf(id))
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>(getView()) {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (getView() == null) return;
                                getView().addAttention(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        }));

    }

    //取消关注
    public void removeAttention(int id) {
        addNet(
                service.removeAttention(String.valueOf(id))
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>(getView()) {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (getView() == null) return;
                                getView().removeAttention(data);
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        }));

    }
}
