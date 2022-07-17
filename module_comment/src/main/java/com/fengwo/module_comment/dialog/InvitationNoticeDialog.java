package com.fengwo.module_comment.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;
import razerdp.basepopup.BasePopupWindow;

/**
 * @anchor Administrator
 * @date 2020/10/30
 */
public class InvitationNoticeDialog extends BasePopupWindow {
    CircleImageView iv_header;
    TextView tv_name;
    TextView tv_context;
    ImageView im_btn;
    ImageView im_clone;

    private IAddListListener listener;//声明成员变量

    public interface IAddListListener {//创建抽象类
        void deleteBank();//添加抽象方法，可任意添加多个可带参数如void test(String cibtext);往下加就行
    }
    public InvitationNoticeDialog(Context mContext, String pic, String roomId, String nickname, String text,IAddListListener listener) {
        super(mContext);
        this.listener=listener;
        startCountDownTime(5);
        setShowAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.chat_down_in));
        setDismissAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.chat_rise_out));
        iv_header = findViewById(R.id.iv_header);
        tv_name = findViewById(R.id.tv_name);
        tv_context = findViewById(R.id.tv_context);
        im_btn = findViewById(R.id.im_btn);
        im_clone = findViewById(R.id.im_clone);
        setPopupGravity(Gravity.TOP);
        initData();
        im_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteBank();


            }
        });
        tv_context.setText(text);
        tv_name.setText(nickname);
        ImageLoader.loadImg(iv_header, pic);

    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_invtation);

    }
//    @Override
//    protected View createDialogView(LayoutInflater inflater, @Nullable ViewGroup container) {
//        View view = inflater.inflate(R.layout.dialog_invtation, container, false);
//
//        return view;
//    }

    private void initData() {
        im_clone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                timer.onFinish();
                dismiss();
            }
        });

    }

    CountDownTimer timer;

    private void startCountDownTime(long time) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 有onTick，onFinsh、cancel和start方法
         */
        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dismiss();
            }
        };
        timer.start();// 开始计时
        //timer.cancel(); // 取消
    }


}
