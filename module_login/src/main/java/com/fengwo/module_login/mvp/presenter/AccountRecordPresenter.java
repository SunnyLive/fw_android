package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_login.mvp.dto.RecordDto;
import com.fengwo.module_login.mvp.dto.WithDrawDto;
import com.fengwo.module_login.mvp.ui.iview.IAccountRecordView;


public class AccountRecordPresenter extends BaseLoginPresenter<IAccountRecordView> {
    /**
     * 充值记录
     * @param page
     */
    public void getRechargeRecords(int page) {
        service.getRechargeRecords(page + "," + PAGE_SIZE)
                .compose(handleResult())
                .subscribe(new LoadingObserver<BaseListDto<RecordDto>>() {
                    @Override
                    public void _onNext(BaseListDto<RecordDto> data) {
                        getView().setRechargeRecordData(data.records, page);
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }
    /**
     * 提现记录
     * @param page
     */
    public void getWithDrawRecords(int page) {
        service.getWithDrawRecords(page + "," + PAGE_SIZE)
                .compose(handleResult())
                .subscribe(new LoadingObserver<BaseListDto<WithDrawDto>>() {
                    @Override
                    public void _onNext(BaseListDto<WithDrawDto> data) {
                        getView().setWithDrawRecordData(data.records, page);
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }
}
