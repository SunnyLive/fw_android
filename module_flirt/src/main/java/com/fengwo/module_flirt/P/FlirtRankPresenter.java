package com.fengwo.module_flirt.P;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_flirt.Interfaces.ICerTagView;
import com.fengwo.module_flirt.Interfaces.IFlirtRankView;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.FlirtRankBean;

import okhttp3.RequestBody;

public class FlirtRankPresenter extends BaseFlirtPresenter<IFlirtRankView> {

    public void getRankData(int page,int charmType,String location) {
        RequestBody build = new WenboParamsBuilder()
                .put("current", page+"")
                .put("size", 30+"")
                .put("charmType", charmType+"")
                .put("location", location+"")
                .build();
        addNet(service.flirtRank(build).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<FlirtRankBean>>() {
                    @Override
                    public void _onNext(BaseListDto<FlirtRankBean> data) {
                        getView().setFlirtRankList(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }
}
