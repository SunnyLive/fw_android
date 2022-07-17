package com.fengwo.module_comment.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 倒计时工具
 *
 * @author
 * @time 2018/3/13 11:07
 */
public class CountDownTimerUtil {

    private static CountDownTimerUtil _instance = null;

    private CountDownTimer timer;

    /**
     * 私有构造方法
     */
    private CountDownTimerUtil() {
    }

    // 对外提供一个该类的实例，考虑多线程问题，进行同步操作
    public static CountDownTimerUtil getInstance() {
        if (_instance == null) {
            synchronized (CountDownTimerUtil.class) {
                if (_instance == null) {
                    _instance = new CountDownTimerUtil();
                }
            }
        }
        return _instance;
    }

    public  void startCountdown(final TextView view, long nextSendTime){
         timer = new CountDownTimer(nextSendTime*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                view.setText(TimeUtils.fromSecond((int) (millisUntilFinished/1000)));
            }

            @Override
            public void onFinish() {
                view.setText("00:00:00");
            }
        };
        timer.start();
    }

    /**
     * 启动
     *
     * @param view
     * @param nextSendTime
     */
    public void startTimer(final TextView view, long nextSendTime) {
        timer = new CountDownTimer(nextSendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                view.setEnabled(false);
                view.setText(millisUntilFinished / 1000 + "秒后重试");
            }

            @Override
            public void onFinish() {
                view.setEnabled(true);
                view.setText("重新获取");
            }
        };
        timer.start();
    }

    /**
     * 销毁
     */
    public void stopTimer() {
        if (timer != null) timer.cancel();
    }

}
