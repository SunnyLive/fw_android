package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_login.mvp.dto.UserDetailDto;
import com.fengwo.module_login.mvp.ui.iview.IUserDetailView;

public class UserDetailPresenter extends BaseLoginPresenter<IUserDetailView> {

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     */
    public void getUserDetail(String userId) {
        service.getUserDetail(userId)
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<UserDetailDto>() {
                    @Override
                    public void _onNext(UserDetailDto data) {
                        getView().getUserDetail(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }
}
