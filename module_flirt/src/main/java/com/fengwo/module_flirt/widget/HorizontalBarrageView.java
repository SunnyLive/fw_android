package com.fengwo.module_flirt.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.widget.UrlImageSpan;
import com.fengwo.module_flirt.R;
import com.fengwo.module_websocket.bean.BaseChatMsgBean;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;

import java.util.LinkedList;
import java.util.Queue;

public class HorizontalBarrageView extends FrameLayout {

    /*消息队列*/
    private Queue<BaseChatMsgBean> barrageQueue = new LinkedList<>();

    /*弹幕view缓存池*/
    private Queue<View> barrageViewCache = new LinkedList<>();

    /*单条弹幕滚入到滚出的事件*/
    private int duration = 8000;

    /*两条弹幕间最小间距*/
    private int barrageMinDx;

    /*当前弹幕是否正在滚动*/
    private boolean isRolling = false;

    /*整个控件大小*/
    private int width;
    private LayoutInflater layoutInflater;
    private OnTextBarrageClickListener onTextBarrageClickListener;

    public HorizontalBarrageView(@NonNull Context context) {
        super(context);
        init();
    }

    public HorizontalBarrageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalBarrageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        layoutInflater = LayoutInflater.from(getContext());
        barrageMinDx = (int) getContext().getResources().getDimension(R.dimen.dp_60);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
    }

    /**
     * 新增弹幕
     */
    public void addBarrage(SocketRequest<WenboWsChatDataBean> barrage, WenboGiftMsg gift) {
        BaseChatMsgBean baseChatMsgBean = new BaseChatMsgBean(barrage, gift);
        barrageQueue.offer(baseChatMsgBean);
        if (!isRolling) {
            animBarrage(barrageQueue.poll());
        }
    }

    /**
     * 开启弹幕
     *
     * @param chatData
     */
    private void animBarrage(BaseChatMsgBean chatData) {
        isRolling = chatData != null;
        if (chatData == null) return;
        //获取弹幕view，优先从缓存池中获取
        View barrageView = barrageViewCache.poll();
        BarrageViewHolder holder;
        if (barrageView == null) {
            barrageView = layoutInflater.inflate(R.layout.item_chat_msg, null);
            holder = new BarrageViewHolder(barrageView);
            barrageView.setTag(holder);
        } else {
            holder = (BarrageViewHolder) barrageView.getTag();
        }

        //数据绑定
        bindBarrageView(holder, chatData);
        addView(barrageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //将barrageView移动到左边
        barrageView.setX(width);

        //获取到barrageView宽度后开启动画
        View finalBarrageView = barrageView;
        barrageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                finalBarrageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //获取view宽度
                int barrageWidth = finalBarrageView.getWidth();
                ObjectAnimator anim = ObjectAnimator.ofFloat(finalBarrageView, "translationX", width, -barrageWidth);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float dx = (float) animation.getAnimatedValue();
                        //间距足够后poll下一条弹幕
                        if (dx < width - barrageWidth - barrageMinDx) {
                            //只要poll出下一条弹幕就移除监听，下一次poll会发生在下一个弹幕的动画监听中
                            anim.removeUpdateListener(this);
                            animBarrage(barrageQueue.poll());
                        }
                    }
                });
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //弹幕动画结束后，将弹幕view移出viewGroup，加入缓存池
                        super.onAnimationEnd(animation);
                        removeView(finalBarrageView);
                        barrageViewCache.offer(finalBarrageView);
                    }
                });
                anim.setDuration(duration);
                anim.setInterpolator(new LinearInterpolator());
                anim.start();
            }
        });
    }

    /**
     * 绑定视图
     *
     * @param holder
     * @param chatData
     */
    private void bindBarrageView(BarrageViewHolder holder, BaseChatMsgBean chatData) {
        WenboWsChatDataBean barrage = chatData.getWenboWsChatDataBean().data;
        ImageLoader.loadImg(holder.ivChatHeader, barrage.getFromUser().getHeadImg());
        holder.tvChatMsg1.setText(barrage.getFromUser().getNickname());
        if (null != barrage.getGears()&&barrage.getIsGears()!=null&&barrage.getIsGears().equals("1")) {
            holder.tvChatType.setVisibility(View.VISIBLE);
            String gears;
            if(barrage.getAction().equals("chat")){
                 gears = barrage.getGears();
            }else {
                 gears = String.valueOf(chatData.getGift().getGift().getGears());
            }
            switch (gears) {
                case "0":
                    holder.tvChatMsg1.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_63A5FF));
                    holder.tvChatType.setBackgroundResource(R.drawable.bg_chat_msg_type_0);
                    holder.tvChatType.setText("开启缘分");
                    break;
                case "2":
                    holder.tvChatMsg1.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_4DC6E0));
                    holder.tvChatType.setBackgroundResource(R.drawable.bg_chat_msg_type_1);
                    holder.tvChatType.setText("缘定三生");
                    break;
                case "1":
                    holder.tvChatMsg1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_FFC147));
                    holder.tvChatType.setBackgroundResource(R.drawable.bg_chat_msg_type_2);
                    holder.tvChatType.setText("再续前缘");
                    break;
            }

        } else {
            holder.tvChatType.setVisibility(View.GONE);
        }
        switch (barrage.getContent().getType()) {
            case "text":
                holder.tvChatMsg2.setText(barrage.getContent().getValue());
                holder.tvChatMsg2.setTextColor(ContextCompat.getColor(getContext(), R.color.text_white));
                holder.mRootView.setOnClickListener(v -> {
                    if (onTextBarrageClickListener != null) {
                        onTextBarrageClickListener.onTextBarrageClick(chatData.getWenboWsChatDataBean());
                    }
                });
                break;
            case "voice":
                holder.tvChatMsg2.setText("ta给你发了一条语音");
                holder.tvChatMsg2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_FFC147));
                holder.mRootView.setOnClickListener(v -> {
                    onSwitchChatUser(chatData.getWenboWsChatDataBean(), holder);
                });
                break;
            case "dice":
                holder.tvChatMsg2.setText("ta给你发了骰子");
                holder.tvChatMsg2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_FFC147));
                holder.mRootView.setOnClickListener(v -> {
                    onSwitchChatUser(chatData.getWenboWsChatDataBean(), holder);
                });
                break;
            case "finger-guessing":
                holder.tvChatMsg2.setText("ta给你发了猜拳");
                holder.tvChatMsg2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_FFC147));
                holder.mRootView.setOnClickListener(v -> {
                    onSwitchChatUser(chatData.getWenboWsChatDataBean(), holder);
                });
                break;
            default:// 礼物消息 拼接图片
                if (null != chatData.getGift()) {
                //    String msg = "TA赠送了你|";
                    holder.tvChatMsg2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_FFC147));
                    try {
                        UrlImageSpan span = new UrlImageSpan(getContext(), chatData.getGift().getGift().getSmallImgPath(), holder.tvChatMsg2);
                        //     UrlImageSpan span = new UrlImageSpan(mContext, "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1589870813000*%E5%A4%9A%E5%B7%B4%E8%83%BA%E7%82%B8%E5%BC%B9.png", mHolder.getView(R.id.tv_content_right));
                        int iconStart = barrage.getContent().getValue().lastIndexOf("|");
                        SpannableStringBuilder spanString = new SpannableStringBuilder(barrage.getContent().getValue());
                        spanString.setSpan(span, iconStart, iconStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                     //   spanString.append("x1");
//                    int toutiaoStart = item.data.getContent().getValue().indexOf("|");
//                    spanString.setSpan( toutiaoStart, toutiaoStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.tvChatMsg2.setText(spanString);
                    } catch (Exception e) {

                    }
                    holder.mRootView.setOnClickListener(v -> {
                        onSwitchChatUser(chatData.getWenboWsChatDataBean(), holder);
                    });
                }
                break;
        }
    }

    private void onSwitchChatUser(SocketRequest<WenboWsChatDataBean> chat, BarrageViewHolder holder) {
        VoiceNotifyView mVoiceNotifyView = new VoiceNotifyView(getContext(), chat, holder.tvChatMsg2.getText().toString());
        mVoiceNotifyView.setOnChangeChatUserListener(() -> {
            if (onTextBarrageClickListener != null) {
                return onTextBarrageClickListener.onSwitchChatUser(chat);
            }
            return false;
        });
        mVoiceNotifyView.showPopupWindow();
    }

    /**
     * 设置文本弹幕的点击事件
     */
    public void setOnTextBarrageClickListener(OnTextBarrageClickListener onTextBarrageClickListener) {
        this.onTextBarrageClickListener = onTextBarrageClickListener;
    }

    private static class BarrageViewHolder {

        final View mRootView;
        final ImageView ivChatHeader;
        final TextView tvChatMsg1;
        final TextView tvChatMsg2;
        final TextView tvChatType;

        BarrageViewHolder(View rootView) {
            mRootView = rootView;
            ivChatHeader = rootView.findViewById(R.id.iv_chat_header);
            tvChatMsg1 = rootView.findViewById(R.id.tv_chat_msg1);
            tvChatMsg2 = rootView.findViewById(R.id.tv_chat_msg2);
            tvChatType = rootView.findViewById(R.id.tv_chat_type);
        }
    }

    public interface OnTextBarrageClickListener {
        void onTextBarrageClick(SocketRequest<WenboWsChatDataBean> chatData);

        /**
         * 点击切换聊天室
         *
         * @param chatData
         * @return 是否拦截事件
         */
        boolean onSwitchChatUser(SocketRequest<WenboWsChatDataBean> chatData);
    }
}
