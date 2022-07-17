package com.fengbo.module_chatlib.ui.msg;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_comment.R;
import com.fengwo.module_websocket.bean.ChatMessage;

/**
 * 语音消息
 *
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class AudioMessage extends BaseMessageView {


    public AudioMessage(ChatMessage msg, int type) {
        super(msg);
        setItemType(type);
    }

    @Override
    public View obtainMessageView(View parent, int position) {
        TextView tv = new TextView(parent.getContext());
        tv.setText("这是语音语音");
//        tv.setBackgroundResource(R.drawable.rect_purple_round10_no_lt);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, parent.getContext().getResources().getDimension(R.dimen.sp_12));
//        if (getItemType() == 1) {
//            tv.setBackgroundResource(R.drawable.rect_purple_round10_no_lt);
//        } else if (getItemType() == 2) {
//            tv.setBackgroundResource(R.drawable.rect_purple_round10_no_rt);
//        }
//        int dp5 = (int) parent.getContext().getResources().getDimension(com.fengbo.module_chatlib.R.dimen.dp_5);
//        int dp10 = (int) parent.getContext().getResources().getDimension(com.fengbo.module_chatlib.R.dimen.dp_10);
//        tv.setPadding(dp10, dp5, dp10, dp5);
        return tv;
    }


    @Override
    public int getPosition() {
        return 0;
    }
}
