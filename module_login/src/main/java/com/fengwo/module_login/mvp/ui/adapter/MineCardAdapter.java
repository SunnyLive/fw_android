package com.fengwo.module_login.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.widget.ClickableRecyclerView;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.MineCardDto;
import com.fengwo.module_login.mvp.ui.activity.MineCardDetailActivity;
import com.fengwo.module_login.utils.UserManager;

import java.io.Serializable;

public class MineCardAdapter extends BaseQuickAdapter<MineCardDto.RecordsBean, BaseViewHolder> {

    private Context mContext;
    private int childRvWidth;
    private int childItemGap;
    private float scrollX, scrollY;


    public MineCardAdapter(Context mContext) {
        super(R.layout.item_mine_card);
        this.mContext = mContext;
        //根据list中的布局计算出这个展示图片的recyclerView的宽度
        //最外层recyclerView左右各15margin，头像宽度44dp,自身marginLeft12dp
        int dp15 = (int) mContext.getResources().getDimension(R.dimen.dp_15);
        int dp44 = (int) mContext.getResources().getDimension(R.dimen.dp_44);
        int dp12 = (int) mContext.getResources().getDimension(R.dimen.dp_12);
        int dp8 = (int) mContext.getResources().getDimension(R.dimen.dp_8);
        childRvWidth = ScreenUtils.getScreenWidth(mContext) - (dp15 + dp15 + dp44 + dp12);
        childItemGap = dp8;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, MineCardDto.RecordsBean item) {
        //cardStatus	integer($int32)
        //审核状态：状态：0审核中，1成功，2私密，3封禁，4草稿，5拒审

        TextView mTvCheck = helper.getView(R.id.tv_mine_card_check);
        helper.addOnClickListener(
                R.id.iv_mine_card_more,     //更多操作
                R.id.tv_mine_card_transmit, //转发
                R.id.tv_mine_card_give,     //点赞事件
                R.id.tv_mine_card_comment   //评论
        );
        switch (item.getCardStatus()) {
            case 0:
                mTvCheck.setText("审核中,仅自己可见");
                mTvCheck.setTextColor(ContextCompat.getColor(mContext, R.color.blue_63A5FF));
                break;
            case 1:
                helper.getView(R.id.tv_mine_card_transmit).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_mine_card_give).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_mine_card_comment).setVisibility(View.VISIBLE);
                helper.getView(R.id.rl_mine_card_bottom).setVisibility(View.VISIBLE);
                mTvCheck.setText("审核通过");
                mTvCheck.setTextColor(ContextCompat.getColor(mContext, R.color.gray_DDDDDD));
                break;
            case 2:
                mTvCheck.setText("私密");
                mTvCheck.setTextColor(ContextCompat.getColor(mContext, R.color.gray_DDDDDD));
                break;
            case 3:
                mTvCheck.setText("内容涉及违规已封禁");
                mTvCheck.setTextColor(ContextCompat.getColor(mContext, R.color.red_ff5a6d));
                break;
            case 4:
            case 5:
                mTvCheck.setText("草稿");
                mTvCheck.setTextColor(ContextCompat.getColor(mContext, R.color.gray_DDDDDD));
                break;

        }

        //如果是别人的动态
        //就影藏《更多操作》 动态状态
        boolean isState = UserManager.getInstance().getUser().id == item.getUserId();
        helper.setGone(R.id.iv_mine_card_more,isState);
        helper.setGone(R.id.tv_mine_card_check,isState);

        helper.setText(R.id.tv_mine_card_time, item.getTime()); //发布时间
        TextView mTvContent = helper.getView(R.id.tv_mine_card_content);//发布内容
        TextView tv_qb = helper.getView(R.id.tv_qb);//发布内容
        tv_qb.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(item.getExcerpt())) {
            mTvContent.setVisibility(View.VISIBLE);
            mTvContent.setText(item.getExcerpt());
            mTvContent.post(new Runnable() {
                @Override
                public void run() {
                    if(mTvContent.getLineCount()>3&&mTvContent.getMaxLines()==3){
                        tv_qb.setVisibility(View.VISIBLE);
                        tv_qb.setSelected(false);
                    }
                }
            });

            tv_qb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tv_qb.isSelected()){
                        mTvContent.setMaxLines(3);
                        tv_qb.setText("展开");
                        tv_qb.setSelected(false);
                    }else {
                        mTvContent.setMaxLines(Integer.MAX_VALUE);
                        tv_qb.setText("收起");
                        tv_qb.setSelected(true);
                    }

                }
            });
        }
        TextView mTvLocation = helper.getView(R.id.tv_mine_card_location);
        if (!TextUtils.isEmpty(item.getPosition())) {
            mTvLocation.setText(item.getPosition());
            mTvLocation.setVisibility(View.VISIBLE);
        }
        setTextValue(helper.getView(R.id.tv_mine_card_transmit), item.getShares(),"分享");//分享数量
        TextView mTvGive = helper.getView(R.id.tv_mine_card_give);
        mTvGive.setSelected(item.getIsLike());//是否是自己点赞
        setTextValue(mTvGive, item.getLikes(),"点赞");//点赞数量
        setTextValue(helper.getView(R.id.tv_mine_card_comment), item.getComments(),"评论");//评论数量
        ClickableRecyclerView mRvGalleryList = helper.getView(R.id.rv_mine_card_list);
        mRvGalleryList.setOnClickListener(v -> {
            //跳转到详情通用模块
            //如果是用户自己的动态就往自己的详情跳转
            //如果不是 就跳转到别人的详情
            if (item.getUserId() == UserManager.getInstance().getUser().id) {
                ARouter.getInstance().build(ArouterApi.MINE_DETAIL_CARD_ACTION)
                        .withInt(MineCardDetailActivity.CHAR_CARD_ID, item.getId())
                        .navigation((Activity) mContext, 10010);
            } else {
                ARouter.getInstance().build(ArouterApi.FLIRT_FIND_DETAIL_ACTION)
                        .withInt(MineCardDetailActivity.CHAR_CARD_ID, item.getId())
                        .navigation((Activity) mContext, 10010);
            }
        });
        if (!item.getCover().isEmpty()) {
            //点击图片整个区域进入详情页面
            mRvGalleryList.setVisibility(View.VISIBLE);
            CardGalleryAdapter mAdapter = new CardGalleryAdapter(childRvWidth, childItemGap);
            //点击item 进入图片浏览
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                ARouter.getInstance().build(ArouterApi.FLIRT_DETAIL_CARD_ACTION)
                        .withSerializable("bannerList", (Serializable) item.getCover())
                        .withInt("position", position)
                        .navigation();
            });
            if (item.getCover().size() == 1) {
                mRvGalleryList.setLayoutManager(new GridLayoutManager(mContext, 1));
            } else if (item.getCover().size() == 2 || item.getCover().size() == 4) {
                mRvGalleryList.setLayoutManager(new GridLayoutManager(mContext, 2));
            } else {
                mRvGalleryList.setLayoutManager(new GridLayoutManager(mContext, 3));
            }
            mRvGalleryList.setAdapter(mAdapter);
            mAdapter.setNewData(item.getCover());
            mAdapter.bindToRecyclerView(mRvGalleryList);
        } else {
            mRvGalleryList.setVisibility(View.GONE);
        }

    }
    private void setTextMax(TextView view , String content){
        SpannableStringBuilder style = new SpannableStringBuilder();
        //设置文字
        style.append(content+"全部");
        style.setSpan(new ClickableSpan() {
                          @Override
                          public void onClick(@NonNull View widget) {

                              view.setMaxLines(Integer.MAX_VALUE);

                          }

                          @Override
                          public void updateDrawState(@NonNull TextPaint ds) {
                              ds.setUnderlineText(false);
                          }
                      },
                content.length() - 2,
                content.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#9966FF"));
        style.setSpan(foregroundColorSpan, 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void setTextValue(TextView tv, int value, String defaultValue) {
        tv.setText(value > 99 ? "99+" : value <= 0 ? defaultValue : value + "");
    }

}
