package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;

import de.hdodenhof.circleimageview.CircleImageView;
import razerdp.basepopup.BasePopupWindow;

public class LeavePopwindow extends BasePopupWindow implements View.OnClickListener {

    CircleImageView ivHeader;
    TextView tvName, btnLeave, btnAttention;

    public LeavePopwindow(Context context, String headImg, String nickname, int isAttention) {
        super(context);
        ivHeader = findViewById(R.id.iv_header);
        tvName = findViewById(R.id.tv_name);
        btnLeave = findViewById(R.id.btn_leave);
        btnAttention = findViewById(R.id.tv_attention);
        ImageLoader.loadImg(ivHeader, headImg);
        tvName.setText(nickname);
        btnLeave.setTextColor(context.getResources().getColor(R.color.text_66));
        if (isAttention > 0) {
            btnAttention.setVisibility(View.GONE);
        } else {
            btnAttention.setText("关注");
        }
        btnLeave.setOnClickListener(this);
        btnAttention.setOnClickListener(this);
        setPopupGravity(Gravity.CENTER);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_leave);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_leave) {
            if (null != listener) {
                listener.leaveClick();
            }
        } else if (id == R.id.tv_attention) {
            if (null != listener) {
                listener.attentionClick();
            }
        }
    }

    public void setAttention(boolean isAttention) {
        if (isAttention) {
            btnAttention.setVisibility(View.GONE);
        } else {
            btnAttention.setVisibility(View.VISIBLE);
            btnAttention.setText("关注");
        }
    }

    public interface LeavePopClickListener {
        void leaveClick();

        void attentionClick();
    }

    static LeavePopClickListener listener;

    public void setLeavePopClickListener(LeavePopClickListener l) {
        listener = l;
    }

}
