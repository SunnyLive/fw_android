package com.fengwo.module_login.mvp.ui.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.PickerViewFactory;
import com.fengwo.module_comment.utils.RegexUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.BankDto;
import com.fengwo.module_login.mvp.presenter.BindCardPresenter;
import com.fengwo.module_login.mvp.ui.iview.IBindcardView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BindCardActivity extends BaseMvpActivity<IBindcardView, BindCardPresenter> implements IBindcardView {
    @BindView(R2.id.et_name)
    EditText etName;
    @BindView(R2.id.et_idcard)
    EditText etIdcard;
    @BindView(R2.id.et_choosebank)
    TextView etChoosebank;
    @BindView(R2.id.et_zhihang)
    EditText etZhihang;
    @BindView(R2.id.et_cardnumber)
    EditText etCardnumber;
    @BindView(R2.id.et_phoen)
    EditText etPhoen;
    @BindView(R2.id.btn_getcode)
    TextView btnGetcode;
    @BindView(R2.id.et_code)
    EditText etCode;

    private CountBackUtils countBackUtils;
    private OptionsPickerView optionsPickerView;

    @Override
    public BindCardPresenter initPresenter() {
        return new BindCardPresenter();
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("绑定银行卡")
                .setTitleColor(R.color.text_33)
                .build();
        countBackUtils = new CountBackUtils();
        p.getBankList(false);

    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_bindcard;
    }


    @OnClick({R2.id.btn_getcode, R2.id.btn_submit})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.et_choosebank) {
//            KeyBoardUtils.closeKeybord(etName, this);
//            optionsPickerView.show();
        } else if (id == R.id.btn_getcode) {
            String mobile = etPhoen.getText().toString();
            if (TextUtils.isEmpty(mobile)) {
                toastTip("请输入手机号码");
                return;
            }
            p.getCode(mobile);

        } else if (id == R.id.btn_submit) {
            String name = etName.getText().toString();
            String idCard = etIdcard.getText().toString();
            String bank = etChoosebank.getText().toString();
            String bankBranch = etZhihang.getText().toString();
            String bandCard = etCardnumber.getText().toString();
            String mobile = etPhoen.getText().toString();
            String code = etCode.getText().toString();
            if (TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(idCard) ||
                    TextUtils.isEmpty(bank) ||
                    TextUtils.isEmpty(bandCard) ||
                    TextUtils.isEmpty(mobile) ||
                    TextUtils.isEmpty(code)) {
                toastTip("请输入完整信息!");
                return;
            }
            if (!RegexUtils.isChinese(name)) {
                toastTip("请输入正确的姓名！");
                return;
            }
            if (!RegexUtils.isIDCard18(idCard)) {
                toastTip("请输入正确的身份证号码！");
                return;
            }
            if (!RegexUtils.isChinese(bank)) {
                toastTip("请输入正确的银行！");
                return;
            }
//            if (!RegexUtils.isChinese(bankBranch)) {
//                toastTip("请输入正确的支行！");
//                return;
//            }
            if (bandCard.length() > 19 || bandCard.length() < 16) {
                toastTip("请输入正确的银行卡号！");
                return;
            }
            p.submitBindCard(name, idCard, bank, bankBranch, bandCard, mobile, code);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countBackUtils.destory();
    }

    @Override
    public void setBankList(List<BankDto> data) {
        if (null == optionsPickerView) {
            optionsPickerView = PickerViewFactory.getPickerView(this, "选择银行", new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    etChoosebank.setText(data.get(options1).getPickerViewText());
                }
            });
            optionsPickerView.setPicker(data);
        }

    }

    @Override
    public void bindSuccess() {
        finish();
    }

    @Override
    public void setCode() {
        btnGetcode.setEnabled(false);
        countBackUtils.countBack(60, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                btnGetcode.setText(time + "s");
            }

            @Override
            public void finish() {
                btnGetcode.setEnabled(true);
                btnGetcode.setText("获取验证码");
            }
        });
    }
}
