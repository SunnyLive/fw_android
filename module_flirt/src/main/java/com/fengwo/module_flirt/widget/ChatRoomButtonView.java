package com.fengwo.module_flirt.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.ViewSizeChangeAnimation;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhy.view.flowlayout.TagFlowLayout.dip2px;

/**
 * 聊天室 外层
 *
 * @Author BLCS
 * @Time 2020/4/2 16:56
 */
public class ChatRoomButtonView extends LinearLayoutCompat {

    @BindView(R2.id.civ_header)
    CircleImageView mHeader;
    @BindView(R2.id.tv_name)
    TextView mName;
    @BindView(R2.id.tv_attention)
    TextView tvAttention;
    @BindView(R2.id.tv_zoom)
    TextView tvZoom;
    @BindView(R2.id.btn_show_small)
    ImageView btnShowSmall;
//        @BindView(R2.id.iv_close)
//    ImageView ivClose;
    @BindView(R2.id.iv_time)
    ImageView ivTime;
    @BindView(R2.id.rl_video_time1)
    RelativeLayout rl_video_time1;
    @BindView(R2.id.tv_video_time)
    TextView tvVideoTime;
    @BindView(R2.id.tv_video_add)
    TextView tvVideoAdd;
    @BindView(R2.id.cl_button_list)
    ConstraintLayout clButtonList;
    @BindView(R2.id.tv_about_single)
    TextView tvAboutSingle;
    @BindView(R2.id.tv_present_record)
    TextView tvPresentRecord;
    @BindView(R2.id.ll_user_info)
    ConstraintLayout llUserInfo;
    @BindView(R2.id.tv_much)
    TextView tvMuch;
    @BindView(R2.id.rl_1)
    RelativeLayout rl_1;
    @BindView(R2.id.tv_zsc)
    TextView tvZsc;

    @BindView(R2.id.tv_last_gift_value)
    TextView tvLastGiftPrice;
    @BindView(R2.id.tv_time_value)
    TextView tvLastTime;
    @BindView(R2.id.tv_duration_value)
    TextView tvLastDuration;
    @BindView(R2.id.rl_video_time2)
    RelativeLayout rl_video_time2;
    @BindView(R2.id.tv_total_gift_title)
    TextView tvTotalGiftTitle;
    @BindView(R2.id.tv_total_gift_value)
    TextView tvTotalGiftValue;
    @BindView(R2.id.iv_receive_comment)
    AppCompatImageView ivReceiveComment;


    @BindView(R2.id.ims_gd)
    ImageSwitcher imsGd;

    private boolean isup = false;
    private long videoTime;

    public ChatRoomButtonView(Context context) {
        super(context);
        init();
    }

    public ChatRoomButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        setOrientation(LinearLayoutCompat.VERTICAL);


        initUI();

    }

    private void initUI() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.include_chat_room_external, this, true);
        ButterKnife.bind(this, inflate);
        imsGd.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView i = new ImageView(getContext());
                i.setImageResource(R.mipmap.pic_don);
                return i;
            }
        });
        imsGd.setInAnimation(getContext(), android.R.anim.fade_in);
        imsGd.setOutAnimation(getContext(), android.R.anim.fade_out);
    }

    /**
     * 印象值快结束 只剩1的时候开始跳动
     */
    public void upDataAddTimeEndUI(boolean isGif) {
        if (isGif) {
            ImageLoader.loadGif(ivTime, R.drawable.ic_chat_room_addtime_gif);
        } else {
            ivTime.setImageResource(R.mipmap.ic_chat_room_addtime);
        }
    }

    /**
     * 设置头像
     */
    public void setHeader(String url) {
        ImageLoader.loadImg(mHeader, url);
    }

    /**
     * 设置名字
     */
    public void setName(String name) {
        mName.setText(name);
    }
    public String getName() {


   return mName.getText()+"";
    }

    /**
     * 设置视频时长
     */
    public void setVideoTime(long videoTime) {
        this.videoTime = videoTime;
        tvVideoTime.setText(String.valueOf(videoTime));
    }

    public void resetVideoTime(long videoTime) {
        rl_video_time1.setBackgroundResource(R.drawable.radius_black_alpha_bg);
        ivTime.setImageResource(R.mipmap.ic_chat_room_addtime);
        tvVideoAdd.setText(R.string.impress_value);
        tvVideoAdd.setTextColor(ContextCompat.getColor(getContext(), R.color.color_fe4497));
        tvVideoTime.setText(String.valueOf(videoTime));
    }

    /**
     * 设置视频时长
     */
    public long getVideoTime() {
        return this.videoTime;
    }

    /**
     * 是否显示视频时长
     */
    public void setVideoTimeVisibility(boolean visibility) {
        rl_video_time1.setVisibility(visibility ? VISIBLE : GONE);
        imsGd.setVisibility(visibility ? VISIBLE : GONE);
        imsGd.setImageResource(R.mipmap.pic_don);

    }

    public void setLastVideoTimeVisibility(boolean visibility) {
        rl_video_time2.setVisibility(visibility ? VISIBLE : GONE);
        imsGd.setVisibility(visibility ? VISIBLE : GONE);
        if (visibility) {
            showImpressPop();
        }
    }
    public void setLastVideoTimeGone() {
        imsGd.setImageResource(R.mipmap.pic_don);
        Animation animationa = new ViewSizeChangeAnimation(rl_video_time2, dip2px(getContext(), 34));
        animationa.setDuration(500);
        rl_video_time2.startAnimation(animationa);
        isup = false;
    }
    /**
     * 开始倒计时
     */
    public void beginCountDown(int drawableId, int textId) {
        rl_video_time1.setBackgroundResource(drawableId);
        ivTime.setImageResource(R.mipmap.ic_wenbo_clock);
        tvVideoAdd.setText(textId);
        tvVideoAdd.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
    }

    public void endCountDown() {
        rl_video_time1.setBackgroundResource(R.drawable.radius_black_alpha_bg);
        ivTime.setImageResource(R.mipmap.ic_chat_room_addtime);
        tvVideoAdd.setText(R.string.impress_value);
        tvVideoAdd.setTextColor(ContextCompat.getColor(getContext(), R.color.color_fe4497));
        tvVideoTime.setText(String.valueOf(0));
    }

    public void setCountDownTime(long countDownTime) {
        tvVideoTime.setText(TimeUtils.long2String((int)countDownTime));
    }



    public void isShowAnchorInfo(boolean visibility) {
        llUserInfo.setVisibility(visibility ? VISIBLE : GONE);
    }

    /**
     * 是否显示缩放窗口
     */
    public void setVideoScaleVisibility(boolean visibility) {
        tvZoom.setVisibility(visibility ? VISIBLE : GONE);
    }

    /**
     * 是否显示礼物按钮
     */
    public void setVisiblePresent(boolean visible) {
//        clButtonList.setVisibility(visible ? VISIBLE : GONE);
//        tvMuch.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * 是否显示关注
     */
    public void setAttentionVisibility(boolean visibility) {
        tvAttention.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setBtnShowSmallVisibility(boolean visibility) {
        btnShowSmall.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setCommentViewVisible(boolean visibility) {
        if (ivReceiveComment.getVisibility() == View.VISIBLE) return;
        ivReceiveComment.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
    public void setCommentViewVisiblegone(boolean visibility) {

        ivReceiveComment.setVisibility( View.GONE);
    }

    @OnClick({R2.id.civ_header, R2.id.tv_attention, R2.id.tv_zoom, R2.id.tv_video_add, R2.id.rl_video_time1,
            R2.id.rl_video_time2, R2.id.tv_about_single, R2.id.tv_present_record, R2.id.tv_much, R2.id.btn_show_small,
            R2.id.ims_gd, R2.id.rl_1, R2.id.iv_receive_comment})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.civ_header) {//头像
            if (onClickListener != null) onClickListener.clickHeader();
        } else if (id == R.id.tv_attention) {//关注
            if (onClickListener != null) onClickListener.clickAttention();
//        } else if (id == R.id.tv_zoom) {//小窗
//            setVideoScaleVisibility(false);
//            if (onClickListener != null) onClickListener.clickNarrowWindow();
//        } else
//            if (id == R.id.iv_close) {//关闭
//            if (onClickListener != null) onClickListener.clickfinish();
        } else
            if (id == R.id.tv_video_add || id == R.id.rl_video_time1 || id == R.id.rl_video_time2) {//加时.
            if (onClickListener != null) onClickListener.clickAddVideoTime();
            } else if (id == R.id.tv_about_single) {//约单人
                if (onClickListener != null) onClickListener.clickAboutSingle();
            } else if (id == R.id.tv_present_record) {//礼物记录
                if (onClickListener != null) onClickListener.clickPresentRecord();
            } else if (id == R.id.tv_much) {//更多
                if (onClickListener != null) onClickListener.clickMuch();
            } else if (id == R.id.btn_show_small) {
                setBtnShowSmallVisibility(false);
                if (onClickListener != null) onClickListener.clickShowSmall();
            } else if (id == R.id.ims_gd) {
                showImpressPop();
            }
            else if (id == R.id.rl_1) {
           //  if (onClickListener != null) onClickListener.showAddGift();
            } else if (id == R.id.iv_receive_comment) {
               if (onClickListener != null)  onClickListener.showCommentListDialog();
            }

    }

    private void showImpressPop() {
        if (rl_video_time1.getVisibility() == View.VISIBLE) {
            if (isup) {
                imsGd.setImageResource(R.mipmap.pic_don);
                Animation animationa = new ViewSizeChangeAnimation(rl_video_time1, (int) getResources().getDimension(R.dimen.dp_32));
                animationa.setDuration(500);
                rl_video_time1.startAnimation(animationa);
                isup = false;
            } else {
                imsGd.setImageResource(R.mipmap.pic_up);
                Animation animationa = new ViewSizeChangeAnimation(rl_video_time1, tvTotalGiftTitle.getVisibility() == View.VISIBLE ?
                        (int) getResources().getDimension(R.dimen.dp_114) : (int) getResources().getDimension(R.dimen.dp_70));
                animationa.setDuration(500);
                rl_video_time1.startAnimation(animationa);
                isup = true;
            }
        } else {
            if (isup) {
                imsGd.setImageResource(R.mipmap.pic_don);
                Animation animationa = new ViewSizeChangeAnimation(rl_video_time2, (int) getResources().getDimension(R.dimen.dp_34));
                animationa.setDuration(500);
                rl_video_time2.startAnimation(animationa);
                isup = false;
            } else {
                imsGd.setImageResource(R.mipmap.pic_up);
                Animation animationa = new ViewSizeChangeAnimation(rl_video_time2, (int) getResources().getDimension(R.dimen.dp_114));
                animationa.setDuration(500);
                rl_video_time2.startAnimation(animationa);
                isup = true;
            }
        }
    }

    /**
     * 是否显示赠送总价
     * @param visible
     */
    public void setGiftValueVisible(boolean visible) {
        tvTotalGiftValue.setVisibility(visible ? View.VISIBLE : View.GONE);
        tvTotalGiftTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setGiftValue(int giftValue) {
        tvTotalGiftValue.setText(String.valueOf(giftValue));
    }

    public void setExpireTime(String expireTime) {
        if(!TextUtils.isEmpty(expireTime)){
            tvZsc.setText(expireTime+"");
        }
    }

    public void setLastTime(String time) {
        if (TextUtils.isEmpty(time)) return;
        String format = TimeUtils.getDayAndTime(Long.parseLong(time));
        tvLastTime.setText(format);
    }

    public void setLastDuration(String duration) {
        tvLastDuration.setText(duration);
    }

    public void setLastGiftPrice(String lastGiftPrice) {
        tvLastGiftPrice.setText(lastGiftPrice);
    }

    public interface OnClickListener {
        void clickHeader();

        void clickAttention();



        void clickfinish();

        void clickAddVideoTime();

        void clickAboutSingle();

        void clickPresentRecord();

        void clickMuch();

        void clickShowSmall();

        void showAddGift();

        void showCommentListDialog();
    }

    public OnClickListener onClickListener;

    public void addOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }
}
