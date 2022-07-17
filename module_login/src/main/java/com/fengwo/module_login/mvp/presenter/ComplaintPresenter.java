package com.fengwo.module_login.mvp.presenter;

import android.app.Activity;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.mvp.ui.iview.IComplaintView;

import java.util.Map;

import cc.shinichi.library.tool.ui.ToastUtil;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
public class ComplaintPresenter extends BaseLoginPresenter<IComplaintView> {

    public void complaintCommit(Map map){
        service.complaintCommit(createRequestBody(map))
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().returnCommit(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort((Activity)getView(),msg);
                    }
                });
    }
}
