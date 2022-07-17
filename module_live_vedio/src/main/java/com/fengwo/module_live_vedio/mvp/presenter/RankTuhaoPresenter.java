package com.fengwo.module_live_vedio.mvp.presenter;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_live_vedio.mvp.dto.RankTuhaoDto;
import com.fengwo.module_live_vedio.mvp.ui.iview.IRankTuhaoView;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/19
 */
public class RankTuhaoPresenter extends BaseLivePresenter<IRankTuhaoView> {

    public void getRankTuhaoData(int cycle) {
        addNet(service.getRankTuhao(cycle)
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<List<RankTuhaoDto>>() {
                    @Override
                    public void _onNext(List<RankTuhaoDto> data) {
                        if (getView() == null) return;
                        getView().rankTuhaoDate(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        getView().toastTip(msg);
                    }
                }));
    }
}
