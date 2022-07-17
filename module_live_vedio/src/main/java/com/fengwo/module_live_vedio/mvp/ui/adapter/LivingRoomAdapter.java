package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.ext.ViewExtKt;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_comment.widget.LivingRoomFrameLayout;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.PkRankDto;
import com.fengwo.module_live_vedio.mvp.dto.PkRankMember;
import com.fengwo.module_live_vedio.mvp.dto.PkResultDto;
import com.fengwo.module_live_vedio.utils.MatchPkResultUtil;
import com.fengwo.module_live_vedio.utils.PkAnimaUtil;
import com.fengwo.module_live_vedio.widget.RadarProgressView;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Collections;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class LivingRoomAdapter extends BaseQuickAdapter<ZhuboDto, BaseViewHolder> {
    SparseArray<TXCloudVideoView> textureViewSparseArray;
    SparseArray<TXCloudVideoView> pkMainVideoList;
    SparseArray<TXCloudVideoView> pkOhterVideoList;

    SparseArray<FrameLayout> loadingViews;
    SparseArray<FrameLayout> flPkLeft;
    SparseArray<FrameLayout> flPkRight;
    private int firstPlayPosition = -1;
    private boolean isFirstFound = false;
    private OnFirstFoundListener listener;
    private boolean pkResultShowing = false;
    PkAnimaUtil pkResultAnima;
    private int preIndex;
    private int bottomHeight;
    private LivingRoomFrameLayout liveRoomFrameLayout;
    public void setLiveRoomFrameLayout(LivingRoomFrameLayout liveRoomFrameLayout){
        this.liveRoomFrameLayout = liveRoomFrameLayout;
    }
    public LivingRoomAdapter(@Nullable List<ZhuboDto> data, int firstPlayPosition) {
        super(R.layout.live_item_livingroom, data);
        textureViewSparseArray = new SparseArray<>();
        pkMainVideoList = new SparseArray<>();
        pkOhterVideoList = new SparseArray<>();
        loadingViews = new SparseArray<>();
        flPkLeft = new SparseArray<>();
        flPkRight = new SparseArray<>();

        this.firstPlayPosition = firstPlayPosition;
        pkResultAnima = new PkAnimaUtil();
    }



    public void setBottomHeight(int bottomHeight) {
        KLog.e("pk","bottomHeight="+bottomHeight);
        this.bottomHeight = bottomHeight;
    }

    public TXCloudVideoView getMineVedioView(int preIndex) {

        return (TXCloudVideoView) getViewByPosition(preIndex,R.id.pk_main_view);
    }

    public TXCloudVideoView getOtherVedioView(int preIndex) {
        return (TXCloudVideoView) getViewByPosition(preIndex,R.id.pk_other_view);
    }

    public void startLoading(int position) {
        FrameLayout layout = loadingViews.get(position);
        if (null == layout) return;
        ImageLoader.loadImg((ImageView) getViewByPosition(position, R.id.iv_header_loading), mData.get(position).headImg);
        layout.setVisibility(View.VISIBLE);
        TextView tv = loadingViews.get(position).findViewById(R.id.tv_no_online);
        tv.setVisibility(View.GONE);
//        TextView retry = loadingViews.get(position).findViewById(R.id.tv_no_online_retry);
//        retry.setVisibility(View.GONE);
        RadarProgressView diffuseView = loadingViews.get(position).findViewById(R.id.radar_view);
        diffuseView.setVisibility(View.VISIBLE);
        diffuseView.startRippleAnimation();
    }

    public void showPlayend(int position) {
        FrameLayout layout = loadingViews.get(position);
        if (layout == null) return;
        layout.setVisibility(View.VISIBLE);
        RadarProgressView diffuseView = loadingViews.get(position).findViewById(R.id.radar_view);
        if (null == diffuseView) return;
        TextView tv = loadingViews.get(position).findViewById(R.id.tv_no_online);
        tv.setVisibility(View.VISIBLE);
//        TextView retry = loadingViews.get(position).findViewById(R.id.tv_no_online_retry);
//        retry.setVisibility(View.VISIBLE);
        diffuseView.stopRippleAnimation();
        diffuseView.setVisibility(View.GONE);
    }

    public void hideLoading(int position) {
        FrameLayout view = loadingViews.get(position);
        if (null == view) return;
        view.setVisibility(View.GONE);
        RadarProgressView diffuseView = view.findViewById(R.id.radar_view);
        diffuseView.stopRippleAnimation();
    }

    public void startPkMineLoading(int position, boolean isLeftShow) {
        ProgressBar minePb = (ProgressBar) getViewByPosition(position, R.id.pb_mine);
        if (null != minePb)
            minePb.setVisibility(isLeftShow ? View.VISIBLE : View.GONE);
    }

    public void startPkOtherLoading(int position, boolean isRightShow) {
        ProgressBar otherPb = (ProgressBar) getViewByPosition(position, R.id.pb_other);
        if (null != otherPb)
            otherPb.setVisibility(isRightShow ? View.VISIBLE : View.GONE);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder vh = super.onCreateViewHolder(parent, viewType);
        View view = vh.itemView.findViewById(R.id.ll_pk_video_view);
        view.post(() -> {
            view.getLayoutParams().height = parent.getHeight() - view.getTop() - bottomHeight - DensityUtils.dp2px(mContext, 70);
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
//        Log.d("lucas","onBindViewHolder:"+position);
        TXCloudVideoView textureView = holder.getView(R.id.video_view);
        FrameLayout flLoading = holder.getView(R.id.fl_loading);
        FrameLayout frame_view = holder.getView(R.id.frame_view);
        frame_view.setTag(position+"");
        flLoading.setVisibility(View.VISIBLE);
        ImageLoader.loadImgBlur(holder.getView(R.id.iv_cover), mData.get(position).thumb, R.drawable.live_bg_livingroom);
        if (null == loadingViews.get(position, null)) {
            loadingViews.put(position, flLoading);
        }
        if (preIndex == position){//第一次进入 显示加载框头像
            ImageLoader.loadImg(holder.getView(R.id.iv_header_loading), mData.get(position).headImg);
            preIndex =-1;
        }
//        holder.addOnClickListener(R.id.tv_no_online_retry);
        if (null == textureViewSparseArray.get(position, null)) {
            textureViewSparseArray.put(position, textureView);
            if (position == firstPlayPosition) {
                if (!isFirstFound) {
                    if (null != listener) {
                        listener.onFound(position);
                        isFirstFound = true;
                    }

                }
            }
        }
        if (null == pkMainVideoList.get(position, null)) {

            pkMainVideoList.put(position, holder.getView(R.id.pk_main_view));
        }
        if (null == pkOhterVideoList.get(position, null)) {
            pkOhterVideoList.put(position, holder.getView(R.id.pk_other_view));
        }
        if (null == flPkLeft.get(position, holder.getView(R.id.fl_pk_left))) {
            flPkLeft.put(position, holder.getView(R.id.fl_pk_left));
        }
        if (null == flPkRight.get(position, holder.getView(R.id.fl_pk_right))) {
            flPkRight.put(position, holder.getView(R.id.fl_pk_right));
        }
        holder.setVisible(R.id.pk_fl, false);
        RadarProgressView radarProgressView = holder.getView(R.id.radar_view);
        radarProgressView.startRippleAnimation();
    }

    public TXCloudVideoView getTextureView(int position) {
        TXCloudVideoView videoView = (TXCloudVideoView) getViewByPosition(position, R.id.video_view);
      //  TXCloudVideoView videoView = textureViewSparseArray.get(position, null);
        return videoView;
    }

    private float xLeft;
    private float yLeft;
    private float x;
    private float y;

    public void showPkResult(int position, PkResultDto pkResultDto, boolean isSingle, int punishTime, PkAnimaUtil.MAnimatorListener listener) {
        KLog.e("pk","pk结束 动画调用");
        pkResultAnima.cancle();
        TextView tv = (TextView) getViewByPosition(position, R.id.tv_time);
        if (null != tv && tv.getText().toString().contains("PK")) {//后台推送结果，如果还没到惩罚时间，pk倒计时清零，进入惩罚时间
            uploadTime("惩罚中  ", punishTime / 1000, position);
        }
        if (TextUtils.isEmpty(String.valueOf(pkResultDto.getResult())))
            pkResultDto.setResult(0);//如果为null 默认设为0
        FrameLayout flLeft = flPkLeft.get(position);
        FrameLayout flRight = flPkRight.get(position);
        if (null == flLeft) {
            flLeft = (FrameLayout) getViewByPosition(position, R.id.fl_pk_left);
            flPkLeft.put(position, flLeft);
        }
        if (null == flRight) {
            flRight = (FrameLayout) getViewByPosition(position, R.id.fl_pk_right);
            flPkRight.put(position, flRight);
        }
        if (null == flLeft || null == flRight) return;
        ImageView leftStart = flLeft.findViewById(R.id.iv_pk_result_left);
        ImageView leftEnd = flLeft.findViewById(R.id.iv_pk_result_left_end);
        ImageView rightStart = flRight.findViewById(R.id.iv_pk_result_right);
        ImageView rightEnd = flRight.findViewById(R.id.iv_pk_result_right_end);
        //销毁未完成动画
        leftStart.clearAnimation();
        leftEnd.clearAnimation();
        rightStart.clearAnimation();
        rightEnd.clearAnimation();
        leftStart.setVisibility(View.VISIBLE);
        rightStart.setVisibility(View.VISIBLE);
        if (pkResultDto.getResult() == 0) {//平
            leftStart.setImageResource(R.drawable.ic_pk_draw);
            rightStart.setImageResource(R.drawable.ic_pk_draw);
        } else if (pkResultDto.getResult() > 0) {//胜
            leftStart.setImageResource(R.drawable.ic_pk_success);
            rightStart.setImageResource(R.drawable.ic_pk_fail);
        } else {
            leftStart.setImageResource(R.drawable.ic_pk_fail);
            rightStart.setImageResource(R.drawable.ic_pk_success);
        }
        leftStart.setTranslationX(0);
        leftStart.setTranslationY(0);
        rightStart.setTranslationX(0);
        rightStart.setTranslationY(0);
        pkResultAnima.start(leftStart, leftEnd, rightStart, rightEnd, listener);
        if (!isSingle && pkBtnClickListener != null && pkResultDto.getTeamPkResultDto().getNormal() != 0) {
            pkBtnClickListener.showTeamPkResult(pkResultDto);
        }
        if (isSingle){
            showSingleProgressView(position,pkResultDto.getSinglePkResultDto(),pkResultDto.getResult());
        }
    }

    public void hidePkResult(int position) {
        FrameLayout flLeft = flPkLeft.get(position);
        FrameLayout flRight = flPkRight.get(position);
        if (null == flLeft) {
            flLeft = (FrameLayout) getViewByPosition(position, R.id.fl_pk_left);
            flPkLeft.put(position, flLeft);
        }
        if (null == flRight) {
            flRight = (FrameLayout) getViewByPosition(position, R.id.fl_pk_right);
            flPkRight.put(position, flRight);
        }
        if (null == flLeft || null == flRight) return;
        ImageView leftStart = flLeft.findViewById(R.id.iv_pk_result_left);
        ImageView rightStart = flRight.findViewById(R.id.iv_pk_result_right);
        leftStart.clearAnimation();
        rightStart.clearAnimation();
        leftStart.setVisibility(View.GONE);
        rightStart.setVisibility(View.GONE);
        pkResultShowing = false;
    }

    public void isHidePkAttention(int position, boolean b) {
        View pkFl = getViewByPosition(position, R.id.pk_fl);
        if (null == pkFl) return;
        GradientTextView tvAttention = pkFl.findViewById(R.id.tv_pk_attention);
        if (b) {
            tvAttention.setVisibility(View.GONE);
        } else {
            tvAttention.setVisibility(View.VISIBLE);
        }
    }

    public void startPk(int position, boolean isSingle, MatchPkResultUtil matchPkResultUtil) {
        View pkFl = getViewByPosition(position, R.id.pk_fl);
        if (null == pkFl) return;
        pkFl.setVisibility(View.VISIBLE);
        TextView tvName = pkFl.findViewById(R.id.tv_pk_nickname);
        tvName.setText(matchPkResultUtil.getOtherInfo().getNickname());
        GradientTextView tvAttention = pkFl.findViewById(R.id.tv_pk_attention);

        if(null!=tvAttention.getParent()){
            tvAttention.getParent().requestDisallowInterceptTouchEvent(true);
        }

//        tvAttention.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (pkBtnClickListener != null) {
//                    pkBtnClickListener.attentionClick(matchPkResultUtil.getOtherInfo().getUserId() + "");
//                }
//            }
//        });
        //fixme 暂时先这样处理，后面再优化 相关类 @LivingRoomFrameLayout
        if (liveRoomFrameLayout!=null){
            tvAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            View.OnTouchListener onTouchListener = ViewExtKt.customClick(tvAttention, new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    if (pkBtnClickListener != null) {
                        pkBtnClickListener.attentionClick(matchPkResultUtil.getOtherInfo().getUserId() + "");
                    }
                    return null;
                }
            });
            liveRoomFrameLayout.addOnCustomTouchListener(tvAttention,onTouchListener);
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            View.OnTouchListener onTouchListener2 = ViewExtKt.customClick(tvName, new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    if (pkBtnClickListener != null) {
                        pkBtnClickListener.nickNameClick(matchPkResultUtil.getOtherInfo().getUserId() + "");
                    }
                    return null;
                }
            });
            liveRoomFrameLayout.addOnCustomTouchListener(tvName,onTouchListener2);
        }

//        tvName.setOnClickListener(new FastClickListener() {
//            @Override
//            public void onNoFastClick(View v) {
//                if (pkBtnClickListener != null) {
//                    pkBtnClickListener.nickNameClick(matchPkResultUtil.getOtherInfo().getUserId() + "");
//                }
//            }
//        });

        textureViewSparseArray.get(position).setVisibility(View.GONE);
        if (!isSingle) {
            showTeamProgressView(position, matchPkResultUtil);
        }else {
            showSingleProgressView(position,matchPkResultUtil.matchTeamResult,matchPkResultUtil.matchTeamResult.getUserResult());
        }
    }
    public void showSingleProgressView(int position, PkRankDto pkRankDto,int result) {
        if (pkRankDto==null||pkRankDto.contributionRank==null&&pkRankDto.objectContributionRank==null) return;
        int weSize = pkRankDto.contributionRank.size();
        int otherSize = pkRankDto.objectContributionRank.size();
        if (weSize<3){
            for (int i=0;i<3-weSize;++i){
                pkRankDto.contributionRank.add(new PkRankMember());
            }
        }
        Collections.reverse(pkRankDto.contributionRank);//倒序
        if (otherSize<3){
            for (int i=0;i<3-otherSize;++i){
                pkRankDto.objectContributionRank.add(new PkRankMember());
            }
        }
        RelativeLayout rvSingleBottom = (RelativeLayout) getViewByPosition(position, R.id.rl_pk_single_bottom);
        LinearLayout llHotWe = (LinearLayout) getViewByPosition(position,R.id.ll_hot_we);
        TextView tvHotWe = (TextView) getViewByPosition(position,R.id.tv_hot_we);
        TextView tvHotOther = (TextView) getViewByPosition(position,R.id.tv_hot_other);
        ImageView ivWipeWe = (ImageView) getViewByPosition(position,R.id.iv_wipe_we);
        ImageView ivWipeOther = (ImageView) getViewByPosition(position,R.id.iv_wipe_other);
        tvHotWe.setText(pkRankDto.memberPopularity+"");
        tvHotOther.setText(pkRankDto.objectMemberPopularity+"");
        LinearLayout llHotOther = (LinearLayout) getViewByPosition(position,R.id.ll_hot_other);
        if (liveRoomFrameLayout!=null){
            llHotWe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            liveRoomFrameLayout.addOnCustomTouchListener(llHotWe, ViewExtKt.customClick(llHotWe, new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    if (pkBtnClickListener != null&&rvSingleBottom!=null&&rvSingleBottom.getVisibility() == View.VISIBLE) {
                        pkBtnClickListener.pkContributeClick(pkRankDto.getUserInfo().getUserId(),true);
                    }
                    return null;
                }
            }));
            llHotOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            liveRoomFrameLayout.addOnCustomTouchListener(llHotOther, ViewExtKt.customClick(llHotOther, new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    if (pkBtnClickListener != null&&rvSingleBottom!=null&&rvSingleBottom.getVisibility() == View.VISIBLE) {
                        pkBtnClickListener.pkContributeClick(pkRankDto.getObjectInfo().getUserId(),false);
                    }
                    return null;
                }
            }));
        }
//        llHotWe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (pkBtnClickListener != null) {
//                    pkBtnClickListener.pkContributeClick(pkRankDto.getUserInfo().getUserId(),true);
//                }
//            }
//        });

//        llHotOther.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        RecyclerView rvWeSingle = (RecyclerView) getViewByPosition(position, R.id.rv_we_single);
        RecyclerView rvOtherSingle = (RecyclerView) getViewByPosition(position, R.id.rv_other_single);
        rvWeSingle.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        rvOtherSingle.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        rvWeSingle.setAdapter(new PkSingleAdapter(pkRankDto.contributionRank, true,result, new PkSingleAdapter.ILocationistener() {
            @Override
            public void getLocation(int position, int[] x, View view) {
//                myFirstOne[0] = x;
//                myFirstOne[1] = y;
//                FirstView = view;

            }
        }));
        rvOtherSingle.setAdapter(new PkSingleAdapter(pkRankDto.objectContributionRank, false,result, new PkSingleAdapter.ILocationistener() {
            @Override
            public void getLocation(int position, int[] x, View view) {
//                myFirstOne[0] = x;
//                myFirstOne[1] = y;
//                FirstView = view;

            }
        }));
        rvWeSingle.setLayoutFrozen(true);
        rvOtherSingle.setLayoutFrozen(true);
        if (result==1){
            ivWipeWe.setVisibility(View.GONE);
            ivWipeOther.setVisibility(View.VISIBLE);
        }else if (result == -1){
            ivWipeWe.setVisibility(View.VISIBLE);
            ivWipeOther.setVisibility(View.GONE);
        }else {
            ivWipeWe.setVisibility(View.GONE);
            ivWipeOther.setVisibility(View.GONE);
        }
        rvSingleBottom.setVisibility(View.VISIBLE);
    }
    private void hideSingleProgressView(int position) {
        RelativeLayout rvSingleBottom = (RelativeLayout) getViewByPosition(position, R.id.rl_pk_single_bottom);
        if (rvSingleBottom != null) rvSingleBottom.setVisibility(View.GONE);
    }

    private void showTeamProgressView(int position, MatchPkResultUtil matchPkResultUtil) {
        TextView tvWeScore = (TextView) getViewByPosition(position, R.id.tv_we_team_score);
        TextView tvOtherScore = (TextView) getViewByPosition(position, R.id.tv_other_team_score);
        if (tvWeScore == null || tvOtherScore == null) return;
        tvWeScore.setText("团队：0");
        tvOtherScore.setText("0：团队");
        RelativeLayout rvTeamBottom = (RelativeLayout) getViewByPosition(position, R.id.rl_pk_team_bottom);
        RecyclerView rvWeTeam = (RecyclerView) getViewByPosition(position, R.id.rv_we_team);
        RecyclerView rvOtherTeam = (RecyclerView) getViewByPosition(position, R.id.rv_other_team);
        rvWeTeam.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        rvOtherTeam.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        rvWeTeam.setAdapter(new PkTeamAdapter(matchPkResultUtil.getUserTeamInfo(), true));
        rvOtherTeam.setAdapter(new PkTeamAdapter(matchPkResultUtil.getOtherTeamInfo(), false));
        rvTeamBottom.setVisibility(View.VISIBLE);
        tvWeScore.setVisibility(View.VISIBLE);
        tvOtherScore.setVisibility(View.VISIBLE);
    }

    private void hideTeamProgressView(int position) {
        TextView tvWeScore = (TextView) getViewByPosition(position, R.id.tv_we_team_score);
        TextView tvOtherScore = (TextView) getViewByPosition(position, R.id.tv_other_team_score);
        if (tvWeScore != null) {
            tvWeScore.setText("团队：0");
            tvWeScore.setVisibility(View.GONE);
        }
        if (tvOtherScore != null) {
            tvOtherScore.setText("0：团队");
            tvOtherScore.setVisibility(View.GONE);
        }
        RelativeLayout rvTeamBottom = (RelativeLayout) getViewByPosition(position, R.id.rl_pk_team_bottom);
        if (rvTeamBottom != null) rvTeamBottom.setVisibility(View.GONE);
    }

    public void stopPk(int preIndex) {
        View v = getViewByPosition(preIndex, R.id.pk_fl);
        if (null != v) {
            v.setVisibility(View.GONE);
        }
        hidePkResult(preIndex);
        hideTeamProgressView(preIndex);
        hideSingleProgressView(preIndex);
        uploadScore(preIndex, 0, 0, 0, 0, false);//pk结束 重置值
        if (textureViewSparseArray != null && textureViewSparseArray.size() > 0)
            textureViewSparseArray.get(preIndex).setVisibility(View.VISIBLE);
        oldLeft = 0;
        oldRight = 0;
    }

    private FrameLayout.LayoutParams normalParams;

    public void setVedioType(int position, int type) {
        TXCloudVideoView videoView = textureViewSparseArray.get(position);
        if (null == videoView) return;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
        if (type == 2) {
            layoutParams.height = (ScreenUtils.getStatusHeight(mContext) + ScreenUtils.getScreenHeight(mContext)) * 1 / 3;
            layoutParams.topMargin = (int) mContext.getResources().getDimension(R.dimen.dp_80);
        } else {
//            layoutParams.height = ScreenUtils.getStatusHeight(mContext) + ScreenUtils.getScreenHeight(mContext);
            layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
            layoutParams.topMargin = 0;
        }
        videoView.setLayoutParams(layoutParams);
    }

    int oldLeft = 0, oldRight = 0;

    public void clearPkOldPoint() {
        oldLeft = 0;
        oldRight = 0;
    }

    /**
     * 更新pk礼物值
     *
     * @param position
     * @param left
     * @param right    当前按照 礼物最少值 20计算 ， 当一方没收到礼物一方收到任何礼物的时候，没收到礼物的一方保留1.5权值 另一方20权值
     */
    public void uploadScore(int position, int left, int right, int groupPoint, int otherGroupPoint, boolean isPk) {
        if (isPk) {
            if (left + right < oldLeft + oldRight) {//pk过程中 避免数值回退
                return;
            }
        } else { //pk 结束 初始化这两个值
            oldLeft = 0;
            oldRight = 0;
        }
        View sLeft = getViewByPosition(position, R.id.view_score_left);
        View sRight = getViewByPosition(position, R.id.view_score_right);
        TextView tvWeScore = (TextView) getViewByPosition(position, R.id.tv_we_team_score);
        TextView tvOtherScore = (TextView) getViewByPosition(position, R.id.tv_other_team_score);
        if (sLeft == null || sRight == null || tvWeScore == null || tvOtherScore == null)
            return;//控件未初始化则返回
        if (tvWeScore != null) {
            tvWeScore.setText("团队：" + DataFormatUtils.formatNumbers(groupPoint));
        }
        if (tvOtherScore != null) {
            tvOtherScore.setText(DataFormatUtils.formatNumbers(otherGroupPoint) + "：团队");
        }
        LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) sLeft.getLayoutParams();
        LinearLayout.LayoutParams rightParams = (LinearLayout.LayoutParams) sRight.getLayoutParams();
        float weightLeft = 1, weightRight = 1;
        if (left == 0 && right == 0) {

        } else if (1f * left / right < 0.2 || 1f * right / left < 0.2) {
            if (left > right) {
                weightLeft = 8;
                weightRight = 2;
            } else {
                weightLeft = 2;
                weightRight = 8;
            }
        } else {
            weightLeft = left;
            weightRight = right;
        }
        leftParams.weight = weightLeft;
        rightParams.weight = weightRight;
        sLeft.setLayoutParams(leftParams);
        sRight.setLayoutParams(rightParams);
        TextView tvSLeft = (TextView) getViewByPosition(position, R.id.tv_score_mine);
        TextView tvSRight = (TextView) getViewByPosition(position, R.id.tv_score_other);

        tvSLeft.setText("我方 " + left);
        tvSRight.setText(right + " 对方");
        oldLeft = left;
        oldRight = right;
    }

    public void uploadTime(String text, int time, int posiiton) {
        TextView tv = (TextView) getViewByPosition(posiiton, R.id.tv_time);
        if (null != tv) {
            if (time > 0) {
                tv.setText(text + TimeUtils.fromSecond(time));
            } else {
                tv.setText(text);
            }
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ZhuboDto item) {
    }

    public RecyclerView getRvChat(int position) {
//        RecyclerView rv = (RecyclerView) getViewByPosition(position, R.id.rv_chat);
        return null;
    }

    public void setAttention(int position) {
        View pkFl = getViewByPosition(position, R.id.pk_fl);
        if (pkFl == null) return;
        GradientTextView tvAttention = pkFl.findViewById(R.id.tv_pk_attention);
        tvAttention.setVisibility(View.GONE);
    }

    public void setCurrentPos(int preIndex) {

        this.preIndex = preIndex;
    }


    public interface OnFirstFoundListener {
        void onFound(int position);
    }

    private void initMsgRv(RecyclerView rv) {
//        rv.setLayoutManager(new LinearLayoutManager(mContext));
//        List<LivingRoomTextMsg> chatList = new ArrayList<>();
//        LivingRoomTextMsg d1 = new LivingRoomTextMsg(mContext.getResources().getString(R.string.live_room_first), LivingMsgDto.TYPE_SYSTEM);
//        LivingRoomTextMsg d2 = new LivingRoomTextMsg(mContext.getResources().getString(R.string.live_room_second), LivingMsgDto.TYPE_SYSTEM);
//        chatList.add(d1);
//        chatList.add(d2);
//        LivingMsgAdapter livingMsgAdapter = new LivingMsgAdapter(chatList);
//        rv.setAdapter(livingMsgAdapter);
//        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                rv.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
    }

    public void setOnFirstFoundListener(OnFirstFoundListener l) {
        listener = l;
    }

    public interface OnPkBtnClickListener {
        void nickNameClick(String s);

        void attentionClick(String s);

        void showTeamPkResult(PkResultDto pkResultDto);

        void pkContributeClick(int id,boolean isWe);
    }

    OnPkBtnClickListener pkBtnClickListener;

    public void setPkBtnClickListener(OnPkBtnClickListener l) {
        pkBtnClickListener = l;
    }

}
