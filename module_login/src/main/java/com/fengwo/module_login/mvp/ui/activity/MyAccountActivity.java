package com.fengwo.module_login.mvp.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.utils.UserManager;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAccountActivity extends BaseMvpActivity {
    @BindView(R2.id.tv_huazuan)
    TextView tvHuazuan;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        setWhiteTitle("我的账户");
    }

    @Override
    public void onResume() {
        super.onResume();

        UserInfo userInfo = UserManager.getInstance().getUser();
        tvHuazuan.setText(String.valueOf(userInfo.balance));
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_myaccount;
    }


    @OnClick({ R2.id.btn_tomygift, R2.id.btn_tosendgift})
    public void onViewClicked(View view) {
        int id = view.getId();
       if (id == R.id.btn_tomygift) {
            startActivity(ReceiveGiftActivity.class);
        } else if (id == R.id.btn_tosendgift) {
            startActivity(SendGiftHistoryActivity.class);
        }
    }
}
