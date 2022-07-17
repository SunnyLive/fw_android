package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.SignUtils;
import com.fengwo.module_login.mvp.dto.BankDto;
import com.fengwo.module_login.mvp.dto.BindCardDto;
import com.fengwo.module_login.mvp.dto.CodeDto;
import com.fengwo.module_login.mvp.ui.iview.IBindcardView;
import com.fengwo.module_login.utils.UserManager;

import java.util.List;

public class BindCardPresenter extends BaseLoginPresenter<IBindcardView> {

    public void getBankList(boolean showLoading) {
        MvpView v = showLoading ? getView() : null;
        service.getBankList()
                .compose(handleResult())
                .subscribe(new LoadingObserver<List<BankDto>>(v) {
                    @Override
                    public void _onNext(List<BankDto> data) {
                        getView().setBankList(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }


    public void getCode(String mobile) {
        String nonce = SignUtils.getNonce();
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.sign(nonce, mobile, timestamp);
        service.getCode(nonce, sign, new ParamsBuilder().put("mobile", mobile)
                .put("timestamp", timestamp + "")
                .put("type", "bank").build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().toastTip("短信发送成功");
                            getView().setCode();
                        } else {
                            getView().toastTip("短信发送失败");
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip("短信发送失败");
                    }
                });
    }

    public void submitBindCard(String name, String idCard, String bank, String bankBranch, String cardNo, String mobile, String code) {
        service.bindCard(new ParamsBuilder()
                .put("bankCardNumber", cardNo)
                .put("bankName", bank)
                .put("code", code)
                .put("idCard", idCard)
                .put("mobile", mobile)
                .put("realName", name)
                .put("bankBranch", bankBranch)
                .build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            UserInfo u = UserManager.getInstance().getUser();
                            u.isBank = 1;
                            UserManager.getInstance().setUserInfo(u);
                            getView().toastTip("绑定成功！");
                            getView().bindSuccess();
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });
    }
}
