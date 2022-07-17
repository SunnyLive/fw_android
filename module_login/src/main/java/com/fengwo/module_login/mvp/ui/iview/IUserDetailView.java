package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.UserDetailDto;

public interface IUserDetailView extends MvpView {
    void getUserDetail(UserDetailDto data);
}
