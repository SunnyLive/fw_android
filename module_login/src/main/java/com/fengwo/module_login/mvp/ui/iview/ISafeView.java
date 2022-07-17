package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.BindBankDTO;

public interface ISafeView extends MvpView {
    void setBankStatus(int status);

    void setBankInfo(BindBankDTO data);
}
