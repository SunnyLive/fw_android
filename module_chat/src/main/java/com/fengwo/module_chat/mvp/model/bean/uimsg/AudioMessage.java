package com.fengwo.module_chat.mvp.model.bean.uimsg;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_chat.R;
import com.fengwo.module_websocket.bean.ChatMessage;

/**
 * 语音消息
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class AudioMessage extends BaseMessageView {


    public AudioMessage(ChatMessage msg) {
        super(msg);
    }

    @Override
    public View obtainMessageView(View parent) {
        TextView tv = new TextView(parent.getContext());
        tv.setText("语音");
        tv.setBackgroundResource(R.drawable.rect_purple_round10_no_lt);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, parent.getContext().getResources().getDimension(R.dimen.sp_12));
        if (getItemType() == 1) {
            tv.setBackgroundResource(R.drawable.rect_purple_round10_no_lt);
        } else if (getItemType() == 2) {
            tv.setBackgroundResource(R.drawable.rect_purple_round10_no_rt);
        }
        return tv;
    }
}
