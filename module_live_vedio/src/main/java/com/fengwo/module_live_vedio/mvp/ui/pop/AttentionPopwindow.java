package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.eventbus.AttentionChangeEvent;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_live_vedio.utils.AttentionUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import razerdp.basepopup.BasePopupWindow;

public class AttentionPopwindow extends BasePopupWindow {

    private CircleImageView ivHeader;
    private TextView tvName;
    private boolean attention;
    private TextView btnSubmit;
    private int uid;

    public AttentionPopwindow(Context context) {
        super(context);
        ivHeader = findViewById(R.id.iv_header);
        tvName = findViewById(R.id.tv_name);
        btnSubmit = findViewById(R.id.btn_submit);
        setAttentionStatus();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!attention) {
                    AttentionUtils.addAttention(uid, new LoadingObserver<HttpResult>() {
                        @Override
                        public void _onNext(HttpResult data) {
                            if (data.isSuccess()) {
                                attention = true;
                                setAttentionStatus();
                                RxBus.get().post(new AttentionChangeEvent(attention));
                            }
                        }

                        @Override
                        public void _onError(String msg) {

                        }
                    });
                } else {
                    AttentionUtils.delAttention(uid, new LoadingObserver<HttpResult>() {
                        @Override
                        public void _onNext(HttpResult data) {
                            if (data.isSuccess()) {
                                attention = false;
                                setAttentionStatus();
                                RxBus.get().post(new AttentionChangeEvent(attention));
                            }
                        }

                        @Override
                        public void _onError(String msg) {

                        }
                    });
                }
            }
        });
    }

    private void setAttentionStatus() {
        if (attention) {
            btnSubmit.setText("取消关注");
        } else {
            btnSubmit.setText("关注");
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_attention);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    public void setZhuboInfo(ZhuboDto zhuboDto) {
        uid = zhuboDto.channelId;
        ImageLoader.loadImg(ivHeader, zhuboDto.headImg);
        tvName.setText(zhuboDto.nickname);
    }

    public void setAttention(boolean isAttention) {
        attention = isAttention;
        setAttentionStatus();
    }
}
