package com.fengwo.module_comment.utils;

import android.content.Context;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.fengwo.module_comment.R;

public class PickerViewFactory {
    public static OptionsPickerView getPickerView(Context c, String title, OnOptionsSelectListener l) {
        OptionsPickerView optionsPickerView = new OptionsPickerBuilder(c, l)
                .setCancelColor(c.getResources().getColor(R.color.purple_9966ff))
                .setSubmitColor(c.getResources().getColor(R.color.purple_9966ff))
                .setTitleText(title)
                .setContentTextSize(20)
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();
        return optionsPickerView;
//        int length = data.length;
//        switch (length) {
//            case 1:
//                optionsPickerView.setPicker(data[0]);
//                break;
//            case 2:
//                optionsPickerView.setPicker(data[0], data[1]);
//            case 3:
//                optionsPickerView.setPicker(data[0], data[1], data[2]);
//                break;
//        }
    }
}
