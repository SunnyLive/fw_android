package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.fengwo.module_flirt.R;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/27
 */
public class CerIdentityPop extends BasePopupWindow {

    private WheelView wheelView;
    private OnIdentityListener onIdentityListener;
    private String selectText;

    public CerIdentityPop(Context context,OnIdentityListener onIdentityListener) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        this.onIdentityListener = onIdentityListener;
        wheelView = findViewById(R.id.wheelview);
        wheelView.setCyclic(false); //是否循环展示

        final List<String> mOptionsItems = new ArrayList<>();
        mOptionsItems.add("学生");
        mOptionsItems.add("模特");
        mOptionsItems.add("演员");
        mOptionsItems.add("舞蹈老师");
        mOptionsItems.add("自由职业");
        selectText = mOptionsItems.get(0);
        wheelView.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                selectText = mOptionsItems.get(index);
            }
        });

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIdentityListener.onIdentitySelect(selectText);
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_cer_identity);
    }

    public interface OnIdentityListener{
        void onIdentitySelect(String identity);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }
}
