package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.ContributeTodayDto;

public interface IContributeTodayView extends MvpView {

    void setData(ContributeTodayDto contributeTodayDto);
}
