package com.fengwo.module_flirt.UI.certification;

import android.graphics.Color;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_flirt.R;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/3/31
 */
public class AddCerTagActivity extends BaseMvpActivity {
    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true).setTitle("添加标签")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black).build();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_cer_tag;
    }
}
