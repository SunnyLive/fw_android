package com.fengwo.module_flirt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.iservice.FlirtProviderService;
import com.fengwo.module_comment.iservice.UserMedalService;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.UI.activity.DetailCardActivity;
import com.fengwo.module_flirt.UI.activity.FindDetailActivity;
import com.fengwo.module_flirt.bean.FindListDto;
import com.fengwo.module_login.utils.UserManager;

import java.util.ArrayList;

/**
 * @Author BLCS
 * @Time 2020/8/12 12:17
 */
public class FindListAdapter extends BaseQuickAdapter<FindListDto, BaseViewHolder> {
    private int childRvWidth;
    private int childItemGap;

    @Autowired
    UserProviderService userProviderService;

    private OnImageItemClickListener onImageItemClickListener;

    public FindListAdapter(Context context) {
        super(R.layout.adapter_find_list);

        ARouter.getInstance().inject(this);
        //根据list中的布局计算出这个展示图片的recyclerView的宽度
        //最外层recyclerView左右各15margin，头像宽度44dp,自身marginLeft12dp
        int dp15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        int dp44 = (int) context.getResources().getDimension(R.dimen.dp_44);
        int dp12 = (int) context.getResources().getDimension(R.dimen.dp_12);
        int dp8 = (int) context.getResources().getDimension(R.dimen.dp_8);
        childRvWidth = ScreenUtils.getScreenWidth(context) - (dp15 + dp15 + dp44 + dp12);
        childItemGap = dp8;
    }

    public void setLike(boolean isLike, int position) {
        ImageView ivLike = (ImageView) getViewByPosition(position + 1, R.id.iv_find_like);
        TextView tvNum = (TextView) getViewByPosition(position + 1, R.id.tv_find_like);
        if (ivLike == null || tvNum == null) return;
        ivLike.setSelected(isLike);
        int likes = getItem(position).getLikes();
        tvNum.setText(likes + "");
    }

    public void setLike(int position) {
        ImageView ivLike = (ImageView) getViewByPosition(position, R.id.iv_find_like);
        TextView tvNum = (TextView) getViewByPosition(position, R.id.tv_find_like);
        if (ivLike == null || tvNum == null) return;
        ivLike.setSelected(getItem(position).getIsLike() == 1);
        int likes = getItem(position).getLikes();
        tvNum.setText(likes == 0 ? mContext.getString(R.string.string_star) : String.valueOf(likes));
    }

    public void setComments(int position) {
        TextView tvFindComments = (TextView) getViewByPosition(position, R.id.tv_find_comments);
        if (tvFindComments == null) return;
        int comments = getItem(position).getComments();
        tvFindComments.setText(comments == 0 ? mContext.getString(R.string.string_comment) : String.valueOf(comments));
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, FindListDto item) {

        //审核状态
        // tv_mine_card_check
        TextView mTvCheck = holder.getView(R.id.tv_mine_card_check);
        if (item.getUserId() == UserManager.getInstance().getUser().id) {
            mTvCheck.setVisibility(View.VISIBLE);
            switch (item.getCardStatus()) {
                case 0:
                    mTvCheck.setText("审核中,仅自己可见");
                    mTvCheck.setTextColor(ContextCompat.getColor(mContext, com.fengwo.module_login.R.color.blue_63A5FF));
                    break;
                case 1:
                    mTvCheck.setText("审核通过");
                    mTvCheck.setTextColor(ContextCompat.getColor(mContext, com.fengwo.module_login.R.color.gray_DDDDDD));
                    break;
                case 2:
                    mTvCheck.setText("私密");
                    mTvCheck.setTextColor(ContextCompat.getColor(mContext, com.fengwo.module_login.R.color.gray_DDDDDD));
                    break;
                case 3:
                    mTvCheck.setText("内容涉及违规已封禁");
                    mTvCheck.setTextColor(ContextCompat.getColor(mContext, com.fengwo.module_login.R.color.red_ff5a6d));
                    break;
                case 4:
                case 5:
                    mTvCheck.setText("草稿");
                    mTvCheck.setTextColor(ContextCompat.getColor(mContext, com.fengwo.module_login.R.color.gray_DDDDDD));
                    break;
            }
        } else {
            mTvCheck.setVisibility(View.GONE);
        }
        ImageView avator = holder.getView(R.id.civ_head);
        ImageLoader.loadCircleImg(avator, item.getHeadImg());
        holder.setText(R.id.tv_name, item.getNickname());
//        TextView tvAge = holder.getView(R.id.tv_age);
//        CommentUtils.setSexAndAge(mContext, item.getAnchorId(), item.getSex(), item.getAge(), tvAge);
        holder.setText(R.id.tv_signature, item.getExcerpt());
        holder.setGone(R.id.tv_flirt, item.getWenboLiveStatus() == 1);
        holder.setGone(R.id.tv_living, item.getLiveStatus() == 2);

        /*图片展示*/
        RecyclerView mRv = holder.getView(R.id.rv_icon);
        if (item.getCover().size() == 1) {
            mRv.setLayoutManager(new GridLayoutManager(mContext, 1));
        } else if (item.getCover().size() == 2 || item.getCover().size() == 4) {
            mRv.setLayoutManager(new GridLayoutManager(mContext, 2));
        } else {
            mRv.setLayoutManager(new GridLayoutManager(mContext, 3));
        }
        NearbyImageAdapter nearbyImageAdapter = new NearbyImageAdapter(childRvWidth, childItemGap);
        mRv.setAdapter(nearbyImageAdapter);
        nearbyImageAdapter.setNewData(item.getCover());
        OnItemClickListener onParentItemClickListener = FindListAdapter.this.getOnItemClickListener();
        if (onParentItemClickListener != null) {
            mRv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onParentItemClickListener.onItemClick(FindListAdapter.this, holder.itemView, holder.getAdapterPosition());
                }
            });
        }

        nearbyImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onImageItemClickListener != null) {
                    onImageItemClickListener.onImageItemClick(item, position);
                }
//                DetailCardActivity.start(mContext, item.getCover(), position);
            }
        });

        /* 实名认证 */
        holder.setGone(R.id.tv_cer_mine, item.getMyIsCard() == 1);
        /* 地址*/
        if (!TextUtils.isEmpty(item.getDistance())) {
            double distance = Double.parseDouble(item.getDistance());
            holder.setText(R.id.tv_find_adress, item.getPosition() + " " + DataFormatUtils.formatNumberKm(distance));
        } else {
            holder.setText(R.id.tv_find_adress, item.getPosition());
        }
        holder.setGone(R.id.tv_find_adress, !TextUtils.isEmpty(item.getPosition()));
        /*时间*/
        holder.setText(R.id.tv_find_time, item.getTime());
        /*点赞*/

        holder.setText(R.id.tv_find_like, item.getLikes() == 0
                ? mContext.getString(R.string.string_star)
                : item.getLikes() > 99 ? "99+" : item.getLikes() + "");
        ImageView view = holder.getView(R.id.iv_find_like);
        view.setSelected(item.getIsLike() == 1);
        /*分享*/
        holder.setText(R.id.tv_find_shares, item.getShares() == 0 ? mContext.getString(R.string.string_share) :
                item.getShares() > 99 ? "99+" : item.getShares() + "");

        /*评论*/
        holder.setText(R.id.tv_find_comments, item.getComments() == 0 ? mContext.getString(R.string.string_comment) :
                item.getComments() > 99 ? "99+" : item.getComments() + "");

        /*自己是否有违规评论*/
        holder.setVisible(R.id.iv_warn, item.isBannedComment());

        //如果是本人隐藏更多入口
        View ivFindMuch = holder.getView(R.id.iv_find_much);
        View tvMine = holder.getView(R.id.tv_mine);
        if (item.getUserId() == userProviderService.getUserInfo().getId()) {
            ivFindMuch.setVisibility(View.GONE);
            tvMine.setVisibility(View.VISIBLE);
        } else {
            ivFindMuch.setVisibility(View.VISIBLE);
            tvMine.setVisibility(View.GONE);
        }

        holder.addOnClickListener(R.id.iv_find_much, R.id.iv_find_like, R.id.tv_find_like,
                R.id.tv_find_shares, R.id.tv_find_comments, R.id.civ_head, R.id.tv_flirt, R.id.tv_living);
    }

    public void setOnImageItemClickListener(OnImageItemClickListener onImageItemClickListener) {
        this.onImageItemClickListener = onImageItemClickListener;
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(FindListDto item, int imagePosition);
    }
}
