package com.fengwo.module_flirt.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.DateUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.HostOrderBean;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


/**
 * 礼物记录 /看过我 / 约单人 列表
 *
 * @Author BLCS
 * @Time 2020/4/3 11:00
 */
public class ChatRoomPopListAdapter extends BaseQuickAdapter<HostOrderBean, BaseViewHolder> {

    public static final int REWARD = 0x00001;   //这个是打赏
    public static final int IMPRESSION = 0x00002;   //这个是印象
    public static final int TOTAL = 0x00003;   //这个是明细

    private int mChooseType = REWARD;


    private Drawable mDrawableRecord;
    private Drawable mDrawableImpression;


    public ChatRoomPopListAdapter() {
        super(R.layout.adapter_chatroom_pop_list);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateDefViewHolder(parent, viewType);

    }

    public void setChooseType(int mChooseType) {
        this.mChooseType = mChooseType;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //弹框底部的total view
        mDrawableRecord = ContextCompat.getDrawable(parent.getContext(), R.drawable.icon_record);
        mDrawableImpression = ContextCompat.getDrawable(parent.getContext(), R.drawable.icon_move_house_heart);
        return super.onCreateViewHolder(parent, viewType);
    }


    @SuppressLint("DefaultLocale")
    private void setText(TextView tv, int value) {
        String mTotal = value + "";
        if (value > 9999) {
            double total = value / 10000.0;
            String format = String.format("%.2f", total);
            int index = format.lastIndexOf(".");
            mTotal = format.substring(0, index + 2) + " w";
        }
        tv.setText(mTotal);
    }


    private void setDate(TextView v, String time) {
        //这个是你要转成后的时间的格式
        if (TextUtils.isEmpty(time)) {
            return;
        }
        if(DateUtil.isSameDay(new Date(Long.parseLong(time)))){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String sd = sdf.format(new Date(Long.parseLong(time)));
            v.setText("今天 "+sd);
            return;
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日  HH:mm");
        // 时间戳转换成时间
        String sd = sdf.format(new Date(Long.parseLong(time)));
        v.setText(sd);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NonNull BaseViewHolder mHolder, HostOrderBean bean) {

        int position = mHolder.getAdapterPosition();
        int ranking = position + 1;
        ImageLoader.loadImg(mHolder.getView(R.id.civ_header), bean.headImg);
        mHolder.setText(R.id.tv_name, bean.nickname);

        /*
         *
         *
         * 这里面要区分 打赏榜单的数据展示方式
         *
         * type = 1  打赏榜单  右边的小图标变成          android:drawableEnd="@drawable/icon_record"
         * type = 2  印象榜单  右边的小图标变成          android:drawableEnd="@drawable/icon_move_house_heart"
         * type = 3  收礼明细  右边展示日期    收礼物的图片  并且要把 type = 1 type = 2 的右边部分变成 type = 3
         *
         *
         *
         *
         * */

        //头像相框
        ImageView ivPhotoFrame = mHolder.getView(R.id.iv_photo_frame);
        //排名
        TextView tvRanking = mHolder.getView(R.id.iv_ranking);
        //印象值 打赏值 最右边的view
        TextView tvImpressionValue = mHolder.getView(R.id.tv_impression_value);
        //礼物图片
        ImageView ivGift = mHolder.getView(R.id.iv_gift);
        //礼物名称
        TextView tvGiftName = mHolder.getView(R.id.tv_gift_name);
        //礼物数量
        TextView tvGiftValue = mHolder.getView(R.id.tv_gift_value);
        //这个是日期
        TextView tvTime = mHolder.getView(R.id.tv_date_time);
        //性别年龄
        TextView tvSexAndAge = mHolder.getView(R.id.tv_age);
        //用户等级
        ImageView mLevel = mHolder.getView(R.id.iv_level);


        tvImpressionValue.setVisibility(View.VISIBLE);
        ivPhotoFrame.setVisibility(View.VISIBLE);
        tvSexAndAge.setVisibility(View.VISIBLE);
        tvRanking.setVisibility(View.VISIBLE);
        mLevel.setVisibility(View.VISIBLE);
        tvGiftValue.setVisibility(View.GONE);
        tvGiftName.setVisibility(View.GONE);
        ivGift.setVisibility(View.GONE);
        tvTime.setVisibility(View.GONE);

        if (position > 2) {
            tvImpressionValue.setTextColor(ContextCompat.getColor(mContext, R.color.text_66));
        } else {
            tvImpressionValue.setTextColor(ContextCompat.getColor(mContext, R.color.red_FE3C9C));
        }

        switch (mChooseType) {

            case REWARD:   //打赏榜单
                tvImpressionValue.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, mDrawableRecord, null);
                setText(tvImpressionValue, bean.totalBalance);
                break;
            case IMPRESSION: //印象榜单
                tvImpressionValue.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, mDrawableImpression, null);
                setText(tvImpressionValue, bean.totalImpression);
                break;
            case TOTAL:   //收礼明细
                ivGift.setVisibility(View.VISIBLE);
                tvTime.setVisibility(View.VISIBLE);
                tvGiftName.setVisibility(View.VISIBLE);
                tvGiftValue.setVisibility(View.VISIBLE);
                tvRanking.setVisibility(View.GONE);
                ivPhotoFrame.setVisibility(View.GONE);
                tvImpressionValue.setVisibility(View.GONE);
                tvSexAndAge.setVisibility(View.GONE);
                mLevel.setVisibility(View.GONE);
                setDate(tvTime, bean.createTime);
                ImageLoader.loadImg(ivGift, bean.smallImgPath);
                tvGiftName.setText(bean.conNotes);
                //tvGiftValue.setText(bean.giftQuantity);
                break;
        }

        //用户等级
        if (mLevel.getVisibility() == View.VISIBLE) {
            int levelRes = ImageLoader.getResId("login_ic_v" + bean.userLevel,
                    com.fengwo.module_login.R.drawable.class);
            mLevel.setImageResource(levelRes);
        }

        //用户性别 0：保密，1：男；2：女
        tvSexAndAge.setEnabled(bean.sex == 2);
        tvSexAndAge.setText(bean.age + "");
        // 这个是排名标志
        int mRankingResId = ImageLoader.getResId("icon_ranking" + ranking, com.fengwo.module_comment.R.drawable.class);
        Drawable mRankingDrawable = null;
        if (mRankingResId != -1) {
            mRankingDrawable = ContextCompat.getDrawable(mContext, mRankingResId);
        }

        // 这个是排名的相框
        int mPhotoFrameResId = ImageLoader.getResId("icon_photo_frame_ranking" + ranking, com.fengwo.module_comment.R.drawable.class);
        //Drawable mPhotoFrame = ContextCompat.getDrawable(mContext, R.drawable.bg_chat_photo_frame);
        if (mPhotoFrameResId != -1) {
            Drawable mPhotoFrame = ContextCompat.getDrawable(mContext, mPhotoFrameResId);
            ivPhotoFrame.setBackground(mPhotoFrame);
        }


        if (null == mRankingDrawable) {
            tvRanking.setText(ranking + ".");
        }

        tvRanking.setCompoundDrawablesWithIntrinsicBounds(mRankingDrawable, null, null, null);

    }
}
