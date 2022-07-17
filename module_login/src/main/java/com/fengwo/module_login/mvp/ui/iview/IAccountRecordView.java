package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.RechargeDto;
import com.fengwo.module_login.mvp.dto.RecordDto;
import com.fengwo.module_login.mvp.dto.WithDrawDto;

import java.util.List;

public interface IAccountRecordView extends MvpView {
    void setRechargeRecordData(List<RecordDto> data, int page);
    void setWithDrawRecordData(List<WithDrawDto> data, int page);
}
