package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.PkRankMember;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/11
 */
public class PkSingleAdapter extends BaseQuickAdapter<PkRankMember, BaseViewHolder> {

    private boolean isLeft;
    private int result;//1 胜， 0 平，-1 负

    public PkSingleAdapter(@Nullable List<PkRankMember> data, boolean isLeft,int result,ILocationistener listener) {
        super(R.layout.live_item_pk_single,data);
        this.isLeft = isLeft;
        this.result = result;
        this.listener = listener;
    }

    private ILocationistener listener;//声明成员变量

    public interface ILocationistener {//创建抽象类
        void getLocation(int position,int[] location ,View view );//添加抽象方法，可任意添加多个可带参数如void test(String cibtext);往下加就行
    }
    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, PkRankMember item) {
        int positon = helper.getLayoutPosition();
        CircleImageView circleImageView = helper.getView(R.id.iv_header);
        ImageView ivGuard = helper.getView(R.id.iv_guard);
        ImageView ivGuardRank = helper.getView(R.id.iv_guard_rank);
        ImageView ivMvp = helper.getView(R.id.iv_mvp);
        ivMvp.setVisibility(View.INVISIBLE);
        ivGuard.setVisibility(item.isIsGuard()?View.VISIBLE:View.GONE);
        if (isLeft){
            if (positon == 0){
                if (result < 0){
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.gray_999999));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_3_gray);
                    ivGuard.setImageResource(R.drawable.ic_guard_gray);
                }else {
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.yellow_E9985C));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_3);
                    ivGuard.setImageResource(R.drawable.ic_guard);
                }
            }else if (positon == 1){
                if (result < 0){
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.gray_999999));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_2_gray);
                    ivGuard.setImageResource(R.drawable.ic_guard_gray);
                }else {
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.green_0FE8F0));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_2);
                    ivGuard.setImageResource(R.drawable.ic_guard);
                }
            }else {
                if (result < 0){
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.gray_999999));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_1_gray);
                    ivGuard.setImageResource(R.drawable.ic_guard_gray);
                }else {
                    if (result == 1) {
                        ivMvp.setVisibility(View.VISIBLE);
                    }
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.yellow_FFD67F));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_1);
                    ivGuard.setImageResource(R.drawable.ic_guard);
                }
            }
        }else {
            if (positon == 0){
                if (result > 0){
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.gray_999999));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_1_gray);
                    ivGuard.setImageResource(R.drawable.ic_guard_gray);
                }else {
                    if (result ==-1) { //对面输了 我方胜
                        ivMvp.setVisibility(View.VISIBLE);
                    }
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.yellow_FFD67F));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_1);
                    ivGuard.setImageResource(R.drawable.ic_guard);
                }

            }else if (positon == 1){
                if (result > 0){
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.gray_999999));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_2_gray);
                    ivGuard.setImageResource(R.drawable.ic_guard_gray);
                }else {
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.green_0FE8F0));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_2);
                    ivGuard.setImageResource(R.drawable.ic_guard);
                }
            }else {
                if (result > 0){
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.gray_999999));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_3_gray);
                    ivGuard.setImageResource(R.drawable.ic_guard_gray);
                }else {
                    circleImageView.setBorderColor(mContext.getResources().getColor(R.color.yellow_E9985C));
                    ivGuardRank.setImageResource(R.drawable.ic_pk_guard_3);
                    ivGuard.setImageResource(R.drawable.ic_guard);
                }
            }
        }
        if (item.getUserId()>0) {
            ImageLoader.loadImg(helper.getView(R.id.iv_header), item.getHeadImg(), R.drawable.ic_default_deep);
        }else {
            ImageLoader.loadImg(helper.getView(R.id.iv_header), item.getHeadImg(), R.drawable.ic_pk_rank_empty);
        }
        if(positon == 2){
            ivGuardRank.post(new Runnable() {
                @Override
                public void run() {
                    int[] imFirst = new int[2];
                    ivGuardRank.getLocationOnScreen(imFirst);
                    if(null!=listener){
                        listener.getLocation(positon,imFirst,ivGuardRank);
                    }
                }
            });
        }



    }
}
