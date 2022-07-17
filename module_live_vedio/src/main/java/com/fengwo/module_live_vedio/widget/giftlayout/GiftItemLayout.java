package com.fengwo.module_live_vedio.widget.giftlayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.GiftEffectDto;
import com.fengwo.module_live_vedio.utils.PrivilegeEffectUtils;
import com.fengwo.module_live_vedio.widget.OrangeStrokeTextView;
import com.fengwo.module_live_vedio.widget.giftlayout.bean.GiftBean;

import de.hdodenhof.circleimageview.CircleImageView;


public class GiftItemLayout extends LinearLayout implements Animation.AnimationListener {
    public final String TAG = GiftItemLayout.class.getSimpleName();
    //    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//        }
//    };
//
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    state = GIFTITEM_DEFAULT;
                    handler.removeCallbacksAndMessages(null);
                    if (animListener == null) break;
//                    L.e("GiftItemLayout礼物显示完毕",giftBean.getUserName()+giftBean.getGiftName()+"x"+giftBean.group+", num ="+num);
                    if (giftBean == null) return;
                    animListener.giftAnimEnd(index, giftBean);
                    break;
            }
        }
    };

    public static final int SHOW_TIME = 3000;
    public static final int GIFTITEM_DEFAULT = 0;
    public static final int GIFTITEM_SHOW = 1;

    /**
     * 当前显示状态
     */
    public int state = GIFTITEM_DEFAULT;
    /**
     * 当前显示位置
     */
    public int index;
    /**
     * 当前tag
     */
    public String tag;

    private GiftBean giftBean;

    private CircleImageView crvheadimage;
    private TextView tvUserName;
    private TextView tvMessage;
    private ImageView ivgift;
    private OrangeStrokeTextView giftNum, giftNum2;
    private Context mContext;
    private LinearLayout bgLl;

    /**
     * 透明度动画(200ms), 连击动画(200ms)
     */
    private Animation translateAnim, numAnim;
    private GiftAnimListener animListener;

    public GiftItemLayout(Context context) {
        super(context);
        init(context, null);
    }

    public GiftItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GiftItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

//    public GiftItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(context, attrs);
//    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.item_gift4, this);
        crvheadimage = findViewById(R.id.crvheadimage);
        tvUserName = findViewById(R.id.tv_UserName);
        tvMessage = findViewById(R.id.tv_Message);
        ivgift = findViewById(R.id.ivgift);
        giftNum = findViewById(R.id.giftNum);
        giftNum2 = findViewById(R.id.giftNum2);
        bgLl = findViewById(R.id.left_view);
        initTranslateAnim();
        initNumAnim();
        mContext = context;
//        initFlowerShoot();

        if (null == attrs) return;
        TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.GiftItemLayout, 0, 0);
        if (null == typed) return;
        index = typed.getInteger(R.styleable.GiftItemLayout_gift_index, 0);
        typed.recycle();
    }

    /**
     * 设置礼物item显示的数据
     *
     * @param giftBean
     */
    public void setData(GiftBean giftBean) {
        this.giftBean = giftBean;
        tag = giftBean.getUserName() + giftBean.getGiftName() + giftBean.getGroupNum();
        tvUserName.setText(giftBean.userName);
        tvMessage.setText(giftBean.giftName);
        ImageLoader.loadImg(crvheadimage, giftBean.hederUrl);
        giftNum2.setText("x " + giftBean.group);
        int price = giftBean.price * giftBean.group;
        GiftEffectDto dto = PrivilegeEffectUtils.getInstance().getEffectsWithPrice(price);
        bgLl.setBackgroundResource(dto.getComboRes());
        ImageLoader.loadImg(ivgift, giftBean.getGiftImage(), R.drawable.trans_bg);
    }

    @Override
    public void clearAnimation() {
        state = GIFTITEM_DEFAULT;
        super.clearAnimation();
        if (crvheadimage != null) {
            crvheadimage.clearAnimation();
        }
        if (giftNum2 != null) {
            giftNum2.clearAnimation();
        }

        clearData();
    }

    public void reset() {
        state = GIFTITEM_DEFAULT;
    }

    public void clearData() {
        handler.removeCallbacksAndMessages(null);
        this.giftBean = null;
    }

    /**
     * 执行了礼物数量连接效果
     *
     * @param group
     */
    public void addCount(int group) {
        handler.removeMessages(0);
        if (giftBean == null) return;
        giftBean.group = giftBean.group + group;
        giftNum2.setText("x " + giftBean.group);
        int price = giftBean.price * giftBean.group;
        GiftEffectDto dto = PrivilegeEffectUtils.getInstance().getEffectsWithPrice(price);
        bgLl.setBackgroundResource(dto.getComboRes());
        giftNum2.startAnimation(numAnim);// 执行礼物数量动画

    }


    public void startAnimation() {
        crvheadimage.startAnimation(translateAnim);
        state = GIFTITEM_SHOW;
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == translateAnim) {// 头像渐变动画执行完毕
            crvheadimage.clearAnimation();
            giftNum.startAnimation(numAnim);// 执行礼物数量动画
        } else {
            handler.sendEmptyMessageDelayed(0, SHOW_TIME);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    /**
     * 初始化位移动画
     */
    private void initTranslateAnim() {
        translateAnim = new TranslateAnimation(-300, 0, 0, 0);
        translateAnim.setDuration(200);
        translateAnim.setFillAfter(true);
        translateAnim.setAnimationListener(this);
    }

    /**
     * 初始化礼物数字动画
     */
    private void initNumAnim() {
        numAnim = new ScaleAnimation(1f, 1.6f, 1f, 1.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        numAnim.setDuration(200);
        numAnim.setAnimationListener(this);
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMyTag() {
        return tag;
    }

    public void setMyTag(String tag) {
        this.tag = tag;
    }

    public GiftAnimListener getAnimListener() {
        return animListener;
    }

    public void setAnimListener(GiftAnimListener animListener) {
        this.animListener = animListener;
    }
}