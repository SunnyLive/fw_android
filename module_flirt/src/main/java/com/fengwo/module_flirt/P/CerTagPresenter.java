package com.fengwo.module_flirt.P;

import com.fengwo.module_chat.mvp.presenter.BaseChatPresenter;
import com.fengwo.module_chat.mvp.ui.contract.ICardTagView;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_flirt.Interfaces.ICerTagView;
import com.fengwo.module_flirt.bean.CerTagBean;

import java.util.List;

import okhttp3.RequestBody;

public class CerTagPresenter extends BaseFlirtPresenter<ICerTagView> {

    public void getTagList(int page,int pageSize) {
        RequestBody build = new WenboParamsBuilder()
                .put("current", page+"")
                .put("size", pageSize+"")
                .build();
        addNet(service.getCerTagList(build).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<CerTagBean>>() {
                    @Override
                    public void _onNext(BaseListDto<CerTagBean> data) {
                        getView().setAllTag(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }
}
