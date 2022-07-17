package com.fengbo.module_chatlib.ui.msg;

import android.view.View;
import android.widget.ImageView;

import com.fengbo.module_chatlib.rxevent.ShowAllImgEvent;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_websocket.bean.ChatMessage;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class ImageMessage extends BaseMessageView {

    private int position;

    public ImageMessage(ChatMessage msg, int type) {

        super(msg);
        setItemType(type);
    }

    @Override
    public View obtainMessageView(View parent, final int position) {
        this.position = position;
        ImageView tv = new ImageView(parent.getContext());
        ImageLoader.loadImg(tv, chatMessage.getMessage());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.get().post(new ShowAllImgEvent(position));
            }
        });
        return tv;
    }

    @Override
    public int getPosition() {
        return position;
    }


}
