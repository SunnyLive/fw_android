package com.fengwo.module_flirt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ClickUtil;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.widget.CircularProgressView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.MessageListVO;
import com.fengwo.module_flirt.manager.ChatHistroySQLHelper;
import com.fengwo.module_live_vedio.widget.RadarProgressView;
import com.fengwo.module_websocket.bean.StatFateMsg;
import com.fengwo.module_websocket.bean.WenboGiftMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ChatUserAdapter extends BaseQuickAdapter<MessageListVO, BaseViewHolder> {

    /**
     * 进度条刷新的时间间隔
     */
    private static final int PROGRESS_REFRESH_INTERVAL = 1000;

    /*接口返回的原始数据*/
    private List<MessageListVO> originalData = new ArrayList<>();
    /*当前展示的数据*/
    private List<MessageListVO> displayData = new ArrayList<>();

    /*当前聊天的用户*/
    private MessageListVO currentChatUser;
    /*监听当前聊天对象发生变化*/
    private OnChatUserChangeListener onChatUserChangeListener;
    /*180s或300s触发通知显示公告*/
    private OnNotificationListener onNotificationListener;

    private Disposable observable;
    /*需要进度条动画的个数*/
    private int needProgressCount = 0;
    private Runnable giftRun;

    public ChatUserAdapter(Context context) {
        super(R.layout.item_chat_user);
        //为首条item的透明添加默认位置
        View topPaddingView = new View(context);
        topPaddingView.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, DensityUtils.dp2px(context, 20)));
        addHeaderView(topPaddingView);

        initClickListener();
    }

    /**
     * 初始化点击事件，切换聊天对象
     */
    private void initClickListener() {
        setOnItemClickListener((adapter, view, position) -> {
            if (position != 0) {
                if(ClickUtil.canClick(800)) {
                    currentChatUser = displayData.get(position);
                    displayData.clear();
                    displayData.addAll(originalData);
                    displayData.remove(currentChatUser);
                    displayData.add(0, currentChatUser);
                    setNewData(displayData);
                    notifyChatUserChanged();
                }
            }
        });
    }

    /**
     * 切换当前聊天对象
     *
     * @param chatUserId 对方id
     * @return 是否存在该用户
     */
    public boolean changeCurrentChatUser(String chatUserId) {
        if (TextUtils.isEmpty(chatUserId)) return false;
        for (int i = 0; i < originalData.size(); i++) {
            if (chatUserId.equals(String.valueOf(originalData.get(i).getAudienceUid()))) {
                currentChatUser = originalData.get(i);
                displayData.clear();
                displayData.addAll(originalData);
                displayData.remove(currentChatUser);
                displayData.add(0, currentChatUser);
                setNewData(displayData);
                notifyChatUserChanged();
                return true;
            }
        }
        return false;
    }

    /**
     * 刷新列表数据
     */
    public MessageListVO refreshUsers(List<MessageListVO> users) {
        //重置原始数据
        originalData.clear();
        originalData.addAll(users);

        //重置显示数据
        displayData.clear();
        displayData.addAll(users);
        if (users.size() > 0) {
            //如果当前没有聊天对象，或者当前聊天对象已下线，默认第一个为聊天对象
            if (currentChatUser == null || displayData.indexOf(currentChatUser) < 0) {
                currentChatUser = originalData.get(0);
                notifyChatUserChanged();
            } else {
                //将上次的聊天对象调整到第一个位置,这里不能用之前的currentChatUser，因为他的礼物数据可能已经刷新
                int currentIndex = displayData.indexOf(currentChatUser);
                MessageListVO tChatUser = displayData.get(currentIndex);
                displayData.remove(currentChatUser);
                currentChatUser = tChatUser;
                displayData.add(0, currentChatUser);
            }
        } else {
            currentChatUser = null;
            notifyChatUserChanged();
        }
        setNewData(displayData);
        return currentChatUser;
    }

    /**
     * 回调聊天对象发生改变
     */
    private void notifyChatUserChanged() {
        if (onChatUserChangeListener != null) {
            onChatUserChangeListener.onChatUserChange(currentChatUser);
        }
    }

    /**
     * 获取当前的聊天对象
     *
     * @return
     */
    public MessageListVO getCurrentChatUser() {
        return currentChatUser;
    }

    /**
     * 更新指定观众的消息未读数
     *
     * @param audienceId 观众id
     */
    public void refreshMsgUnReadCount(final String audienceId) {
        int index = -1;
        for (int i = 0; i < displayData.size(); i++) {
            if (String.valueOf(displayData.get(i).getAudienceUid()).equals(audienceId)) {
                index = i;
                break;
            }
        }
        //刷新对应用户的消息未读数
        if (index >= 0) {
            TextView tvMsgCount = (TextView) getViewByPosition(index + 1, R.id.tv_msg_count);
            refreshCountView(tvMsgCount, index);
        }
    }

    @SuppressLint("CheckResult")
    private void refreshCountView(TextView tvMsgCount, int position) {
        if (tvMsgCount == null) return;
        if (position == 0) {
            tvMsgCount.setVisibility(View.GONE);
        } else {
            ChatHistroySQLHelper.getInstance().getUnReadMsgCount(String.valueOf(displayData.get(position).getAudienceUid()))
                    .subscribe(integer -> {
                        if (integer == 0) {
                            tvMsgCount.setVisibility(View.GONE);
                        } else {
                            tvMsgCount.setVisibility(View.VISIBLE);
                            if (integer <= 99) {
                                tvMsgCount.setText(String.valueOf(integer));
                            } else {
                                tvMsgCount.setText("99+");
                            }
                        }
                    });
        }
    }

    /**
     * 为进度条开启轮询,每100ms刷新一次进度
     */
    private void countdown() {
        if (observable == null || observable.isDisposed())
            observable = Observable.interval(PROGRESS_REFRESH_INTERVAL, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                                //    KLog.d("hexj", "interval-needProgressCount=" + needProgressCount);
                                needProgressCount = 0;
                                for (int i = 0; i < displayData.size(); i++) {
                                    if (displayData.get(i).isIsPay()) {
                                        //如果剩余时间大于时间间隔，则将时间减少时间间隔
                                        if (displayData.get(i).getExpireTime() > PROGRESS_REFRESH_INTERVAL) {
                                            displayData.get(i).setExpireTime(displayData.get(i).getExpireTime() - PROGRESS_REFRESH_INTERVAL);
                                            needProgressCount++;
                                        }
                                        //如果剩余时间不足时间间隔，则将剩余时间设置为0
                                        else if (displayData.get(i).getExpireTime() > 0
                                                && displayData.get(i).getExpireTime() <= PROGRESS_REFRESH_INTERVAL) {
                                            displayData.get(i).setExpireTime(0);
                                            displayData.get(i).setIsPay(false);
                                            needProgressCount++;
                                        } else {
                                            displayData.get(i).setIsPay(false);
                                        }
                                    }
                                }
                                for (int i = 0; i < displayData.size(); i++) {
                                    refreshProgress(i, displayData.get(i).getExpireTime() / 1000);
                                }
                                //如果当前不需要更新进度则停止定时器
                                if (needProgressCount == 0) {
                                    observable.dispose();
                                    observable = null;
                                }
                            }
                    );
    }

    /**
     * 刷新进度条
     */
    private void refreshProgress(int position, long time) {
        MessageListVO messageListVO = displayData.get(position);

        //因为添加了默认头部，所以下标+1,(CircularProgressView中的最大进度为1000)
        CircularProgressView cpv = (CircularProgressView) getViewByPosition(position + 1, R.id.cpv);
        if (cpv != null) {
            int progress = (int) ((messageListVO.getExpireTime() * 1F / messageListVO.getTotalOrderTime()) * 1000);
            cpv.setProgress(progress);
        }

        //刷新礼物等级
        refreshGears((TextView) getViewByPosition(position + 1, R.id.tv_title), position);

        RadarProgressView radarView = (RadarProgressView) getViewByPosition(position + 1, R.id.radar_view);
        if (radarView != null) {
            //根据不同的礼物等级，开启停止水波纹动画
            long radarCritical = 0;
            switch (messageListVO.getGears()) {
                case 0:
                case 1:
                    radarCritical = Common.BULLETIN_180 * 1000;
                    break;
                case 2:
                    radarCritical = Common.BULLETIN_360 * 1000;
                    break;
            }
            if (onNotificationListener != null) {
                onNotificationListener.onNotification(messageListVO, (int)time);
            }
            if (messageListVO.getExpireTime() > 0 && messageListVO.getExpireTime() < radarCritical) {
                if (!radarView.isRippleAnimationRunning()) {
                    radarView.startRippleAnimation();
                }
            } else {
                if (radarView.isRippleAnimationRunning()) {
                    radarView.stopRippleAnimation();
                }
            }
        }
    }

    /**
     * 刷新礼物等级
     */
    private void refreshGears(TextView tvGears, int position) {
        if (tvGears == null) return;
        if (position == 0) {
            tvGears.setVisibility(View.VISIBLE);
            tvGears.setText("当前聊天");
            tvGears.setBackgroundResource(R.drawable.shape_pink_round_9dp);
        } else {
            MessageListVO messageListVO = displayData.get(position);
            if (messageListVO.isIsPay()) {
                tvGears.setVisibility(View.VISIBLE);
                switch (messageListVO.getGears()) {
                    case 0:
                        tvGears.setText("开启缘分");
                        tvGears.setBackgroundResource(R.drawable.shape_chat_gears0);
                        break;
                    case 1:
                        tvGears.setText("再续前缘");
                        tvGears.setBackgroundResource(R.drawable.shape_chat_gears1);
                        break;
                    case 2:
                        tvGears.setText("缘定三生");
                        tvGears.setBackgroundResource(R.drawable.shape_chat_gears2);
                        break;
                }
            } else {
                tvGears.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 接受到缘分礼物
     *
     * @param statFate
     */
    public void onStatFate(StatFateMsg statFate) {
        //收到礼物 开启定时器
        countdown();
        String audienceUid = statFate.getUser().getUserId();
        for (int i = 0; i < displayData.size(); i++) {
            MessageListVO audience = displayData.get(i);
            //找到送礼物的用户,修改礼物状态
            if (audienceUid.equals(String.valueOf(audience.getAudienceUid()))) {
                audience.setIsPay(statFate.isPay());
                audience.setGears(statFate.getGears());
                audience.setExpireTime(statFate.getExpireTime());
                audience.setTotalOrderTime(statFate.getTotalOrderTime());

                //刷新进度
                refreshProgress(i, statFate.getExpireTime());
                //刷新礼物等级
                TextView tvGears = (TextView) getViewByPosition(i + 1, R.id.tv_title);
                refreshGears(tvGears, i);
                break;
            }
        }
    }

    /**
     * 接受到礼物
     *
     * @param gift
     */
    public void onReceivedGift(WenboGiftMsg gift) {
        if (gift != null) {
            String audienceUid = gift.getUser().getUserId();
            for (int i = 1; i < displayData.size(); i++) {
                MessageListVO audience = displayData.get(i);
                //找到送礼物的用户,修改礼物状态
                if (audienceUid.equals(String.valueOf(audience.getAudienceUid()))) {
                    int finalI = i;
                    ImageView ivGift = (ImageView) getViewByPosition(finalI + 1, R.id.iv_gift);
                    TextView tvMsgCount = (TextView) getViewByPosition(finalI + 1, R.id.tv_msg_count);
                    if (ivGift != null && tvMsgCount != null) {
                        tvMsgCount.setVisibility(View.GONE);
                        ivGift.setVisibility(View.VISIBLE);
                        ImageLoader.loadImageNoDefault(ivGift, gift.getGift().getSmallImgPath());

                        //先清空上次回调
                        ivGift.removeCallbacks(giftRun);
                        //5秒后礼物消失
                        giftRun = () -> {
                            ivGift.setVisibility(View.GONE);
                            ivGift.setImageDrawable(null);
                            refreshCountView(tvMsgCount, finalI);
                        };
                        ivGift.postDelayed(giftRun, 5000);
                    }
                    break;
                }
            }
        }
    }

    //防止礼物复用
    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        View ivGift = holder.getView(R.id.iv_gift);
        if (ivGift != null) {
            ivGift.setVisibility(View.GONE);
            holder.getView(R.id.iv_gift).removeCallbacks(giftRun);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, MessageListVO item) {
        ImageLoader.loadImg(helper.getView(R.id.iv_head), item.getAudienceHeadImg());

        //刷新消息数
        TextView tvMsgCount = helper.getView(R.id.tv_msg_count);
        refreshCountView(tvMsgCount, helper.getAdapterPosition() - 1);
        //刷新礼物等级
        TextView tvGears = helper.getView(R.id.tv_title);
        refreshGears(tvGears, helper.getAdapterPosition() - 1);

        //开启定时器
        if (item.isIsPay()) {
            countdown();
        }
    }


    /**
     * 释放定时器
     */
    public void release() {
        if (observable != null && !observable.isDisposed()) {
            observable.dispose();
        }
    }

    public void setOnChatUserChangeListener(OnChatUserChangeListener onChatUserChangeListener) {
        this.onChatUserChangeListener = onChatUserChangeListener;
    }

    public interface OnChatUserChangeListener {
        void onChatUserChange(MessageListVO user);
    }

    /**
     * 180s 或 300s 触发回调，显示公告
     */
    public interface  OnNotificationListener {
        void onNotification(MessageListVO messageListVO, int time);
    }

    public void setOnNotificationListener(OnNotificationListener onNotificationListener) {
        this.onNotificationListener = onNotificationListener;
    }
}