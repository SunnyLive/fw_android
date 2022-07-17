package com.fengwo.module_flirt.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.fengwo.module_flirt.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 筛选
 */
public class FilterFlirtPopwindow extends BasePopupWindow implements View.OnClickListener {

    private  TextView tvAll;
    private  TextView tvBoy;
    private  TextView tvGirl;
    private  TextView tvAgeRange;

    private int valueA;
    private int valueB;
    private int sex;
//    private int value1Age;
//    private int value2Age;
    private int sexA;

    public FilterFlirtPopwindow(Context context, int value1Age, int value2Age, int sexA) {
        super(context);
//        this.value1Age = value1Age;
//        this.value2Age = value2Age;
        this.sexA = sexA;
        setPopupGravity(Gravity.BOTTOM);
        initUI();
    }

    private void initUI() {
        findViewById(R.id.tv_filter_cancel).setOnClickListener(this);
        tvAll = findViewById(R.id.tv_filter_sex_all);
        tvBoy = findViewById(R.id.tv_filter_sex_boy);
        tvGirl = findViewById(R.id.tv_filter_sex_girl);
        tvAgeRange = findViewById(R.id.tv_filter_age);
        tvAll.setOnClickListener(this);
        tvBoy.setOnClickListener(this);
        tvGirl.setOnClickListener(this);
        findViewById(R.id.tv_filter_finish).setOnClickListener(this);
        SelectedRangeView ageRange = findViewById(R.id.age_range);
//        if (value1Age==-1){
//            ageRange.setValueA(value1Age);
//            ageRange.setValueA(value2Age);
//        }
        ageRange.addOnChangeListener(new SelectedRangeView.OnChangeListener() {

            @Override
            public void getValueA(int value) {
                valueA = value;
                tvAgeRange.setText(String.format("（%s-%s）",valueA,valueB));
            }

            @Override
            public void getValueB(int value) {
                valueB =value;
                tvAgeRange.setText(String.format("（%s-%s）",valueA,valueB));
            }
        });
        if (sexA==2){
            checkSex(2);
        }else if(sexA==1){
            checkSex(1);
        }else{
            checkSex(0);
        }
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_filter_flirt);
        return v;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_filter_cancel) {//取消
            dismiss();
        } else if (id == R.id.tv_filter_sex_all) {//全部
            checkSex(0);
        } else if (id == R.id.tv_filter_sex_boy) {//男
            checkSex(1);
        } else if (id == R.id.tv_filter_sex_girl) { //女
            checkSex(2);
        } else if (id == R.id.tv_filter_finish) {//完成
            if (onClickListener != null) onClickListener.complete(valueA,valueB,sex);
            dismiss();
        }
    }

    private void checkSex(int i) {
        tvAll.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        tvBoy.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        tvGirl.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        tvAll.setTextColor(getContext().getResources().getColor(R.color.gray_bbbbbb));
        tvBoy.setTextColor(getContext().getResources().getColor(R.color.gray_bbbbbb));
        tvGirl.setTextColor(getContext().getResources().getColor(R.color.gray_bbbbbb));
        switch (i) {
            case 0: {
                tvAll.setBackgroundResource(R.drawable.shape_purple_corner);
                tvAll.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            }
            break;
            case 1: {
                tvBoy.setBackgroundResource(R.drawable.shape_purple_corner);
                tvBoy.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            }
            break;
            case 2: {
                tvGirl.setBackgroundResource(R.drawable.shape_purple_corner);
                tvGirl.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            }
            break;
        }
        sex = i;
    }

    public OnClickListener onClickListener;

    public interface OnClickListener {
        void complete(int valueA,int valueB,int sex);
    }

    public void addOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

}
