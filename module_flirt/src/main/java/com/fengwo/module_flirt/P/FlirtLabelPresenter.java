package com.fengwo.module_flirt.P;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_flirt.Interfaces.IFlirtLabelView;
import com.fengwo.module_flirt.bean.LabelTalentDto;

import okhttp3.RequestBody;

public class FlirtLabelPresenter extends BaseFlirtPresenter<IFlirtLabelView>{
    /**
     * 获取组合后的标签
     */
    public void getLabelTalent(String maxAge,String minAge,String sex,int page,String tagName) {
        RequestBody build = new WenboParamsBuilder()
                .put("maxAge", maxAge)
                .put("minAge", minAge)
                .put("sex", sex)
                .put("tagName", tagName)
                .put("page", page+",10")
                .build();
        addNet(service.getLabelTalent(build).compose(handleResult())
                        .subscribeWith(new LoadingObserver<BaseListDto<LabelTalentDto>>() {
                            @Override
                            public void _onNext(BaseListDto<LabelTalentDto> data) {
                        getView().setLabelTalent(data);
                            }
                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        })
        );
    }

}
