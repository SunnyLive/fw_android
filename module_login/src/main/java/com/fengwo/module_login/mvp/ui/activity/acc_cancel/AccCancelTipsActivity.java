package com.fengwo.module_login.mvp.ui.activity.acc_cancel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;

import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;

import butterknife.OnClick;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/30
 */
public class AccCancelTipsActivity extends BaseMvpActivity {

    @Override
    protected void initView() {
        new ToolBarBuilder().showBack(true)
            .setTitle("注销账号")
            .setTitleColor(R.color.text_33)
            .setBackIcon(R.drawable.ic_back_black)
            .build();

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_acc_cancel_tips;
    }

    @OnClick(R2.id.tv_sure)
    void onClick(){
        startActivity(AccCancelReasonActivity.class);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
