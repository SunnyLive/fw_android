package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_login.mvp.dto.RankLevelDto;
import com.fengwo.module_login.mvp.ui.iview.IRankDerailView;

public class RankDetailPresenter extends BaseLoginPresenter<IRankDerailView> {

    public void getRankLevelData() {
        service.getRankLevel().compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<RankLevelDto>() {
                    @Override
                    public void _onNext(RankLevelDto data) {
                        getView().setRankLevelData(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }
}
