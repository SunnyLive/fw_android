package com.fengwo.module_login.mvp.ui.activity;

import android.view.View;

import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.widget.MenuItem;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.BindBankDTO;
import com.fengwo.module_login.mvp.presenter.SafePresenter;
import com.fengwo.module_login.mvp.ui.activity.acc_cancel.AccCancelTipsActivity;
import com.fengwo.module_login.mvp.ui.iview.ISafeView;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.Url;

import butterknife.BindView;
import butterknife.OnClick;

public class SafeActivity extends BaseMvpActivity<ISafeView, SafePresenter> implements ISafeView {

    @BindView(R2.id.btn_bindcard)
    MenuItem bankView;

    @Override
    public SafePresenter initPresenter() {
        return new SafePresenter();
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setTitle("账号与安全")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .build();

    }

    @Override
    public void onResume() {
        super.onResume();
        L.e("========");
        p.getBankBindStatus();
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_safe;
    }

    @OnClick({R2.id.btn_resetpwd, R2.id.btn_bindphone, R2.id.btn_bindwechat, R2.id.btn_bindqq, R2.id.btn_bindcard, R2.id.btn_cancel_acc})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_resetpwd) {
        } else if (id == R.id.btn_bindphone) {
        } else if (id == R.id.btn_bindwechat) {
        } else if (id == R.id.btn_bindqq) {
        } else if (id == R.id.btn_bindcard) {
//          startActivity(BindCardActivity.class);
            BrowserActivity.start(this, "", (BuildConfig.DEBUG ? Url.TEST_BASE_PAY_URL : Url.BASE_PAY_URL) + "myBank?channel=app&token=" + UserManager.getInstance().getToken());
        } else if (id == R.id.btn_cancel_acc) {
            startActivity(AccCancelTipsActivity.class);
        }
    }

    @Override
    public void setBankStatus(int status) {
        L.e("========status " + status);
        if (status == 1) {
            p.getBankInfo();
        } else bankView.setRightText("未绑定");
    }

    @Override
    public void setBankInfo(BindBankDTO data) {
        if (data != null)
            bankView.setRightText(String.format("%s（%s）", data.bankName,
                    data.bankCardNumber.substring(data.bankCardNumber.length() - 4)));
    }
}
