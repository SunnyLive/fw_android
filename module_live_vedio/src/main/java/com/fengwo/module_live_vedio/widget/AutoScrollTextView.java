package com.fengwo.module_live_vedio.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.TextView;

import com.fengwo.module_comment.utils.KLog;
import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;

/**
 * @anchor Administrator
 * @date 2020/12/3
 */
public class AutoScrollTextView extends TextView {
    public final static String TAG = AutoScrollTextView.class.getSimpleName();

    private float textLength = 0f;//文本长度
    private float viewWidth = 0f;
    private float step = 0f;//文字的横坐标
    private float y = 0f;//文字的纵坐标
    private float temp_view_plus_text_length = 0.0f;//用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量
    public boolean isStarting = false;//是否开始滚动
    private Paint paint = null;//绘图样式
    private String text = "";//文本内容
    private String color = "#FFEA7F";
    private String wht = "#ffffff";
    private Paint namePaint = getPaint();
    private Bitmap span;
    private ArrayList<String> msg = new ArrayList<>();

    public void setWht(String wht) {
        this.wht = wht;
    }

    public AutoScrollTextView(Context context) {
        super(context);
        initView();
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    private void initView() {
        //  setOnClickListener(this);
    }


    public void init(WindowManager windowManager, String sendUserName, String receiveUserName, Bitmap span, String sendNum) {
        paint = getPaint();
    KLog.e("tag","text="+text+"sendUserName"+sendUserName);
        text = getText().toString();
        final String text2 = text;
        textLength = paint.measureText(text);
        viewWidth = getWidth();
        this.span = span;
        try {
            //自己和对方的名字不为空或者空字符串
            if (!TextUtils.isEmpty(sendUserName) && !TextUtils.isEmpty(receiveUserName)) {
                msg = new ArrayList<>();
                msg.add(" ");
                msg.add(sendUserName + "  ");
                msg.add("在直播间赠送给 ");
                msg.add(receiveUserName);
                //sendNum + "个" + giftName
                msg.add(text + " ");
                msg.add("       X" + sendNum);
                String size = "";
                for (int i = 0; i < msg.size(); i++) {
                    size += msg.get(i);
                }

                textLength = paint.measureText(size);
            }
        } catch (Exception e) {
            BuglyLog.e(TAG, "直播公告异常数据 message: " + text2 + ", sendUserName: " + sendUserName + ", receiveUserName: " + receiveUserName);
            CrashReport.postCatchedException(e);
            return;
        }


//        if (viewWidth == 0) {
//            if (windowManager != null) {
//                Display display = windowManager.getDefaultDisplay();
//                viewWidth = display.getWidth();
//            }
//        }
        viewWidth = 15;
        step = textLength;
        temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_two_text_length = viewWidth + textLength * 2;
        y = getTextSize() + getPaddingTop();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.step = step;
        ss.isStarting = isStarting;

        return ss;

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        step = ss.step;
        isStarting = ss.isStarting;

    }

    public static class SavedState extends BaseSavedState {
        public boolean isStarting = false;
        public float step = 0.0f;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{isStarting});
            out.writeFloat(step);
        }


        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
            boolean[] b = in.createBooleanArray();
            //in.readBooleanArray(b);
            if (b != null && b.length > 0)
                isStarting = b[0];
            step = in.readFloat();
        }
    }


    public void startScroll() {
        isStarting = true;
        invalidate();
    }


    public void stopScroll() {
        isStarting = false;
        invalidate();
        color = "#FFEA7F";
        msg.clear();
    }

    private IAddListListener listener;

    public interface IAddListListener {
        void deleteBank();
    }

    public void setIDeleteListener(IAddListListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (null == msg || msg.size() == 0) {
            if (paint == null) {
                paint = getPaint();
            }
            paint.setColor(Color.parseColor(wht));
            canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
        } else {
            float length = 0;
            try {
                for (int i = 0; i < msg.size(); i++) {
                    if (i % 2 != 0) {
                        paint.setColor(Color.parseColor(color));
                        if (msg.size() > i) {
                            canvas.drawText(msg.get(i), temp_view_plus_text_length + length + msg.get(i).length() - step, y, paint);
                        }
                    } else {
                        namePaint.setColor(Color.parseColor(wht));
                        if (i == msg.size() - 2 && null != span) {
                            canvas.drawText(msg.get(i), temp_view_plus_text_length + length + msg.get(i).length() - step, y, namePaint);
                            canvas.drawBitmap(span, temp_view_plus_text_length + length + msg.get(i).length() + paint.measureText(msg.get(i)) - step, 0, namePaint);
                        } else {
                            canvas.drawText(msg.get(i), temp_view_plus_text_length + length + msg.get(i).length() - step, y, namePaint);
                        }
                    }
                    length = length + paint.measureText(msg.get(i));

                }
            } catch (IndexOutOfBoundsException e) {
            } catch (NullPointerException e) {
            }
        }
        if (!isStarting) {
            return;
        }
        step += 2.5;//0.5为文字滚动速度。
        if (step > temp_view_plus_two_text_length) {
            step = textLength;
            stopScroll();

            msg.clear();
            if (null != listener) listener.deleteBank();

        }

        invalidate();

    }


}