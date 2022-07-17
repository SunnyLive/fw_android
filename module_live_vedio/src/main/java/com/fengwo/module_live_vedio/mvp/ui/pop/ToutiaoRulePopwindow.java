package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fengwo.module_live_vedio.R;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class ToutiaoRulePopwindow extends PopupWindow {

    private TextView tvRule;
    private TextView tvName;

    public ToutiaoRulePopwindow(Context context) {
        super(context);
        setContentView(View.inflate(context, R.layout.live_pop_toutiao_rule, null));
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        tvRule = getContentView().findViewById(R.id.tv_rule);
        tvName = getContentView().findViewById(R.id.tv_name);
    }

    public void setRule(List<String> rules, String giftPackageName) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rules.size(); i++) {
            builder.append(i + 1 + ".");
            builder.append(rules.get(i));
            if (i != rules.size() - 1) {
                builder.append("\n");
            }
        }
        tvRule.setText(builder.toString());
        tvName.setText(giftPackageName + "包含：");
    }

}
