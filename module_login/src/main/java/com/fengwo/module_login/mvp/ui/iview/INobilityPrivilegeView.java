package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_login.mvp.dto.NobilityDTO;
import com.fengwo.module_login.mvp.dto.NobilityTypeDTO;

import java.util.List;

public interface INobilityPrivilegeView extends MvpView {
    void setPrivilegeTypeList(List<NobilityTypeDTO> data);

    void setPrivilegeList(List<NobilityDTO> nobilityList);

    void buyNobilitySuccess(HttpResult<PopoDto> data);

    void buyNobilityFail(String msg);

    void updateWalletInfo(Long amount);
}
