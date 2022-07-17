package com.fengwo.module_login.mvp.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.CashOutDto;
import com.fengwo.module_login.mvp.presenter.CashOutPresenter;
import com.fengwo.module_login.mvp.ui.iview.ICashOutView;
import com.fengwo.module_login.utils.UserManager;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现
 */
public class CashOutActivity extends BaseMvpActivity<ICashOutView, CashOutPresenter> implements ICashOutView {

    @BindView(R2.id.tv_profit)
    TextView tvProfit;
    @BindView(R2.id.et_withdraw)
    EditText etWithdraw;

    private double max;
    private double min;

    @Override
    public CashOutPresenter initPresenter() {
        return new CashOutPresenter();
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().showBack(true)
                .setTitle("提现")
                .setBackIcon(R.drawable.ic_back_black)
                .setTitleColor(R.color.text_33)
                .build();
        p.getPageData();
        etWithdraw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_cashout;
    }

    @OnClick(R2.id.tv_withdraw)
    void onClick(){
        String money = etWithdraw.getText().toString().trim();
        if (TextUtils.isEmpty(money)){
            toastTip("请输入提现金额");
            return;
        }else if (!DataFormatUtils.isInteger(money)) {
            toastTip("暂时只支持提整数");
            return;
        }else if (min>Double.parseDouble(money)){
            toastTip("提现不能小于最低提现额");
            return;
        }else if (Double.parseDouble(money)>max){
            toastTip("提现不能大于最大提现额");
            return;
        }
        try {
            p.cashOutCommit(Integer.parseInt(money));
        }catch (Exception e){
            toastTip("暂时只支持提整数");
        }

    }

    @Override
    public void returnPageDate(CashOutDto cashOutDto) {
        BigDecimal Profit = new BigDecimal(cashOutDto.getProfit()+"");
        BigDecimal exchangeRate = new BigDecimal(cashOutDto.getExchangeRate()+"");
        BigDecimal bigDecimal = Profit.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_DOWN);
        tvProfit.setText("可提现余额:"+bigDecimal.toString());//花蜜值*人民币转换率 保留两位小数
        etWithdraw.setHint("最低提现"+cashOutDto.getWithdrawLimit()+"￥");
        max = cashOutDto.getProfit()*Double.parseDouble(cashOutDto.getExchangeRate());
        min = Double.parseDouble(cashOutDto.getWithdrawLimit()+"");
    }

    @Override
    public void returnCommitResult(HttpResult httpResult) {
        toastTip(httpResult.description);
        if (httpResult.isSuccess()) {
            UserManager.getInstance().updateUser(new LoadingObserver<UserInfo>() {
                @Override
                public void _onNext(UserInfo data) {
                    UserManager.getInstance().setUserInfo(data);
                }

                @Override
                public void _onError(String msg) {
                    if (!TextUtils.isEmpty(msg) && msg.contains("重新登录")) {
                        tokenIInvalid();
                    }
                }
            });
            finish();
        }
    }


}
