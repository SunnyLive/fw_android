package com.fengwo.module_login.mvp.presenter;

import android.app.Activity;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.mvp.dto.ReportLabelDto;
import com.fengwo.module_login.mvp.ui.iview.IReportView;

import java.util.Map;

import cc.shinichi.library.tool.ui.ToastUtil;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
public class ReportPresenter extends BaseLoginPresenter<IReportView> {

    public void getLabel() {
        addNet(service.getReportLabel()
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<ReportLabelDto>>() {
                    @Override
                    public void _onNext(BaseListDto<ReportLabelDto> data) {
                        getView().returnLabel(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    public void reportCommit(Map map) {
        addNet(
                service.rePortCommit(createRequestBody(map))
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                getView().returnCommit(data);
                            }

                            @Override
                            public void _onError(String msg) {
                                ToastUtils.showShort((Activity) getView(), msg);
                            }
                        }));
    }
}
