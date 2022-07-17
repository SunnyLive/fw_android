package com.fengbo.module_chatlib.ui.msg;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fengbo.module_chatlib.R;
import com.fengbo.module_chatlib.utils.FaceConversionUtil;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_websocket.bean.ChatMessage;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class TextMessage extends BaseMessageView {

    private int position;

    public TextMessage(ChatMessage msg, int type) {
        super(msg);
        setItemType(type);
    }

    @Override
    public View obtainMessageView(View parent, int position) {
        TextView tv = new TextView(parent.getContext());
        tv.setMaxWidth(ScreenUtils.getScreenWidth(parent.getContext()) / 2);
        tv.setText(FaceConversionUtil.getSmiledText(tv.getContext(), chatMessage.getMessage()));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        tv.setLayoutParams(lp);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, parent.getContext().getResources().getDimension(R.dimen.sp_12));


//        if (getItemType() == 1) {
//            tv.setBackgroundResource(R.drawable.app_lvjian_chat_right);
//        } else if (getItemType() == 2) {
//            tv.setBackgroundResource(R.drawable.app_lvjian_other_chat_background);
//        }
//        int dp5 = (int) parent.getContext().getResources().getDimension(R.dimen.dp_5);
//        int dp10 = (int) parent.getContext().getResources().getDimension(R.dimen.dp_10);
//        tv.setPadding(dp10, dp5, dp10, dp5);
        return tv;
    }


    @Override
    public String getType() {
        return chatMessage.getMessageType();
    }

    @Override
    public int getPosition() {
        return 0;
    }
}
