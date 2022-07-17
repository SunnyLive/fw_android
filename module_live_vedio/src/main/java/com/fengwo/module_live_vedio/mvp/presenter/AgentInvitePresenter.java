package com.fengwo.module_live_vedio.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.AgentInviteDto;
import com.fengwo.module_live_vedio.mvp.ui.iview.IAgentInvite;

public class AgentInvitePresenter extends BasePresenter<IAgentInvite> {

    LiveApiService service;

    public AgentInvitePresenter() {
        service = new RetrofitUtils().createApi(LiveApiService.class);
    }

    /**
     * 获取列表
     */
    public void getInvite(int page) {
        addNet(service.getInviteList(page+",30")
                .compose(handleResult())
                .subscribeWith(
                        new LoadingObserver<BaseListDto<AgentInviteDto>>() {
                            @Override
                            public void _onNext(BaseListDto<AgentInviteDto> data) {
                                if (null == getView()) return;
                                if (null != data.records) {
                                    getView().setData(data.records,data.total);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
//                                if ("请重新登录".equals(msg)) {
//                                    getView().tokenIInvalid();
//                                }
                            }
                        }
                ));

    }

}
