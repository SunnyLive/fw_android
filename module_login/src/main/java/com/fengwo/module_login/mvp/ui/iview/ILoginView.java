package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_login.mvp.dto.LoginDto;

public interface ILoginView extends MvpView {
    void setCode(String code);

    void loginSuccess(UserInfo userInfo,boolean isEditInfo);

    void toBindPhone(String token);

    void toAccDestroyCancel(LoginDto loginDto);
}
