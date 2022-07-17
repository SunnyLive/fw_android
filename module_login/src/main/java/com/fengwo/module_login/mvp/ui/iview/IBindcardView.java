package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.BankDto;

import java.util.List;

public interface IBindcardView extends MvpView {
    void setBankList(List<BankDto> data);

    void bindSuccess();

    void setCode();
}
