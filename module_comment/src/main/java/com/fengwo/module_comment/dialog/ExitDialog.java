package com.fengwo.module_comment.dialog;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.api.CommentService;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.widget.RoundImageView;
import com.fengwo.module_comment.widget.floatingview.FloatingView;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.RequestBody;

/**
 * 小窗退出对话框
 */
public class ExitDialog extends BaseDialogFragment implements View.OnClickListener {

    public static final int EXIT_NORMAL = 1;        // 正常退出
    public static final int ENTER_LIVING = 2;       // 进入直播间，关闭对话框

    private OnClickListener mOnClickListener;
    private OnDismissListener mOnDismissListener;

    private LinearLayoutCompat llSureCancel;
    private AppCompatTextView tvTips;
    private AppCompatTextView tvImpressValue;
    private RoundImageView rvHeader;
    private AppCompatTextView tvStatus;
    private AppCompatTextView tvNickname;
    private AppCompatTextView tvExitTip;
    private AppCompatTextView tvEnterLivingRoomTip;

    private String cancelText;  //  取消按钮文本
    private String sureText;    // 确定按钮文本
    private String gear;   // 0 开启缘分  1 再续前缘 2 缘定三生
    private long expireTime; // 印象值倒计时
    private String nickname;// 主播昵称
    private String headImg; // 主播头像
    private String tip;     // 提示
    private int exitType;   // 1 点击关闭退出 2 进入直播间退出
    private String roomId;  // 房间id

    private CountBackUtils mCountBackUtils;
    private CompositeDisposable mCompositeDisposable;
    private CommentService service;
    private RetrofitUtils retrofitUtils;
    private boolean mNeedCountdown = true;

    public void setNeedCountdown(boolean needCountdown){
        this.mNeedCountdown = needCountdown;
    }


    protected int getDialogWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    protected int getDialogHeight() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    public void setOnDismissListener(OnDismissListener mOnDismissListener){
        this.mOnDismissListener = mOnDismissListener;
    }



    @Override
    protected View createDialogView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.dialog_small_window, container, false);
        view.findViewById(R.id.dialog).setOnClickListener(v -> {});
        view.findViewById(R.id.close).setOnClickListener(this);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        tvCancel.setText(cancelText != null ? cancelText : "");
        TextView tvSure = view.findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(this);
        tvSure.setText(sureText != null ? sureText : "");
        llSureCancel = view.findViewById(R.id.ll_sure_cancel);
        tvTips = view.findViewById(R.id.tips);
        tvImpressValue = view.findViewById(R.id.impress_value);
        rvHeader = view.findViewById(R.id.header);
        tvStatus = view.findViewById(R.id.status);
        tvNickname = view.findViewById(R.id.nickname);
        tvTips.setText(tip != null ? tip : "");
        tvExitTip = view.findViewById(R.id.exit_tip);
        tvEnterLivingRoomTip = view.findViewById(R.id.enter_living_tip);
        initData();
        return view;
    }

    private void initData() {
        tvNickname.setText(nickname + "的房间");
        ImageLoader.loadCircleImg(rvHeader, headImg);
        if (!TextUtils.isEmpty(gear)) {
            if (gear.equals(Common.VIDEO_CHANCE_THIRD + "")) {
                tvStatus.setText("缘定三生");
            } else if (gear.equals(Common.VIDEO_CHANCE_SECOND + "")) {
                tvStatus.setText("再续前缘");
            } else {
                tvStatus.setText("开启缘分");
            }
        }
        showImpressTime(expireTime);
        startCountTime();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        RxBus.get().post(new ExitAllDialogEvent());
        if (id == R.id.close) {
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss();
            }
            dismiss();
        } else if (id == R.id.tv_cancel) {
            dismiss();
            if (mOnClickListener != null) {
                mOnClickListener.onCancel();
            }
        } else if (id == R.id.tv_sure) {
            mCountBackUtils.destory();
            showExitTip();
            showImpressTime(0);
            FloatingView.getInstance().hide();
            quitRoom(roomId, 1);
            if (exitType == ENTER_LIVING) {
                tvEnterLivingRoomTip.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 启动倒计时
     */
    public void startCountTime() {
        if (mCountBackUtils == null) {
            mCountBackUtils = new CountBackUtils();
        }
        mCountBackUtils.countBack(expireTime, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                showImpressTime(time);
            }

            @Override
            public void finish() {
                if (mNeedCountdown) {
                    showExitTip();
                }
            }
        });
    }

    private void showImpressTime(long time) {
        tvImpressValue.setVisibility(mNeedCountdown ? View.VISIBLE : View.GONE);
        String impressText = " 缘分印象值剩余：<font color=\"#F12890\">" + time + "</font>";
        tvImpressValue.setText(Html.fromHtml(impressText));
    }

    private void showExitTip() {
        // 隐藏小窗
        llSureCancel.setVisibility(View.GONE);
        tvExitTip.setVisibility(View.VISIBLE);
        tvTips.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) rvHeader.getLayoutParams();
        params.topMargin = (int)getResources().getDimension(R.dimen.dp_85);
        rvHeader.setLayoutParams(params);
        ConstraintLayout.LayoutParams statusParams = (ConstraintLayout.LayoutParams) tvStatus.getLayoutParams();
        statusParams.topMargin = (int)getResources().getDimension(R.dimen.dp_74);
        tvStatus.setLayoutParams(statusParams);
        llSureCancel.postDelayed(()->{
            if (mOnClickListener != null) {
                mOnClickListener.onConfirm();
            }
            dismiss();
            FloatingView.getInstance().hide();
        }, 3000);
    }

    /**
     * 退出直播间
     * @param anchorId
     * @param gotoLive  退出直播间时 0 默认不缘分值 1 清除
     */
    private void quitRoom(String anchorId, int gotoLive) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        if (retrofitUtils == null) {
            retrofitUtils = new RetrofitUtils();
        }
        if (service == null) {
            service = retrofitUtils.createWenboApi(CommentService.class);
        }
        RequestBody build = new WenboParamsBuilder()
                .put("roomId", anchorId)
                .put("goToLive", gotoLive + "")
                .build();
        mCompositeDisposable.add(service.quitRoom(build)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        KLog.d("退出达人房间成功");
                    }

                    @Override
                    public void _onError(String msg) {
                        KLog.d("退出达人房间失败");
                    }
                }));
    }
    /**
     *
     * @param onClickListener
     */
    public ExitDialog addDialogClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        return this;
    }

    public ExitDialog setNegativeButtonText(@StringRes int textId) {
        cancelText = getString(textId);
        return this;
    }

    public ExitDialog setNegativeButtonText(String text) {
        cancelText = text;
        return this;
    }

    public ExitDialog setPositiveButtonText(@StringRes int textId) {
        sureText = getString(textId);
        return this;
    }

    public ExitDialog setPositiveButtonText(String text) {
        sureText = text;
        return this;
    }

    public ExitDialog setGear(String gear) {
        this.gear = gear;
        return this;
    }

    public ExitDialog setExpireTime(long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public ExitDialog setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public ExitDialog setHeadImg(String headImg) {
        this.headImg = headImg;
        return this;
    }
    public ExitDialog setTip(String tip) {
        this.tip = tip;
        return this;
    }

    public ExitDialog setExitType(int exitType) {
        this.exitType = exitType;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public ExitDialog setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }
}
