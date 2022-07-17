package com.fengwo.module_flirt.UI.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.faceunity.entity.Effect;
import com.faceunity.ui.adapter.EffectRecyclerAdapter;
import com.faceunity.ui.control.BeautyControlView;
import com.faceunity.ui.entity.BeautyParameterModel;
import com.faceunity.ui.entity.EffectEnum;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.event.ExitWindowEvent;
import com.fengwo.module_comment.event.SmallWindowEvent;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.floatingview.FloatingMagnetView;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_comment.widget.floatingview.MagnetViewListener;
import com.fengwo.module_comment.base.BeautyDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.ReceiveSocketBean;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.SmallWindowEvent;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_flirt.IM.bean.OrderMessageBean;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.adapter.DatingOrderAdapter;
import com.fengwo.module_flirt.bean.EnterRoomBean;
import com.fengwo.module_flirt.bean.GetAnchorRoomInfo;
import com.fengwo.module_flirt.bean.GiftLevel;
import com.fengwo.module_flirt.dialog.ChatRoomBottomPopwindow;
import com.fengwo.module_flirt.dialog.LiveMorePopwindow;
import com.fengwo.module_flirt.widget.ChatRoomButtonView;
import com.fengwo.module_live_vedio.mvp.ui.pop.NewUserInfoPopwindow;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.tencent.rtmp.ITXLivePlayListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 处理 最外层 按钮  订单列表
 *
 * @Author BLCS
 * @Time 2020/4/2 17:59
 */
public abstract class BaseChatRoomButtonActivity extends BaseChatRoomActivity implements ChatRoomButtonView.OnClickListener {

    private static final String TAG = "BaseChatRoomButtonActivity";
    //    @BindView(R2.id.rv_room_order_list)
//    RecyclerView rvOrderList;
    @BindView(R2.id.iv_bg)
    ImageView ivBg;
    @BindView(R2.id.iv_bgzw)
    ImageView ivBgzw;
    @BindView(R2.id.rl_zw)
    FrameLayout rl_zw;

    @BindView(R2.id.fl_faceunity)
    FrameLayout fl_faceunity;
    @BindView(R2.id.iv_add_gift_show)
    ImageView ivAddGiftShow;
    @BindView(R2.id.iv_close_addTime)
    ImageView ivCloseAddTime;
    @BindView(R2.id.ll_add_gift)
    FrameLayout llAddGift;
    @BindView(R2.id.im_views)
    ImageView im_views;
    @BindView(R2.id.tv_gift_title)
    TextView tv_gift_title;

    private DatingOrderAdapter mAdapter;
    private LiveMorePopwindow liveMorePopwindow;
    protected BeautyControlView mBeautyControlView; //美颜view
    protected ViewStub mBottomViewStub;//美颜 占位view
    protected ViewStub mEffectViewStub;//道具\手势 占位view
    private EffectRecyclerAdapter mEffectRecyclerAdapter; //道具adapter
    protected boolean isMirror = true;
    public String roomId;
    public EnterRoomBean roomBean;

    public boolean isFinish = true;//是否评价
    @Override
    protected void initView() {
        super.initView();
        initUI();
        setInfo();
//        initOrderUI();
//        receiveImNotice();

        initMuch();
        ivCloseAddTime.setOnClickListener(v -> {

            isclone();
        });
    }



    private void initUI() {
        /*加时礼物点击事件*/
        im_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAddGift.setVisibility(View.GONE);
                crvRoom.getChatRcv().setVisibility(View.VISIBLE);
                isclone();
                showCenterGiftPop();
            }
        });
    }

    /**
     * 初始化悬浮窗
     */

    public static final String MSG_ZXQY = "再续前缘";
    public static final String MSG_ZDSS = "缘定三生";
    public static final String MSG_KQYF = "开启缘分";
    public void isclone(){
        if(orderNum<3){
        if (orderNum == 1) {
            crvRoom.showOpenGiftBtn(true, MSG_ZXQY, "再续缘");
            /*隐藏礼物弹窗*/
            showAddTimeGif(false, 1);
        } else if (orderNum == 2) {
            crvRoom.showOpenGiftBtn(true, MSG_ZDSS, "再续缘");
            /*隐藏礼物弹窗*/
            showAddTimeGif(false, 1);
        } else {
            crvRoom.showOpenGiftBtn(true, MSG_KQYF, "开启");
            /*隐藏礼物弹窗*/
            showAddTimeGif(false, 0);
        }
        }else {
            crvRoom.showOpenGiftBtn(true, MSG_KQYF, "开启");
            showAddTimeGif(false, 0);
//            if(expireTime>0){
//                showAddTimeGif(false, 0);
//            }else {
//                crvRoom.showOpenGiftBtn(true, MSG_KQYF, "开启");
//            }

        }
        /*显示背景*/
        crvRoom.showFreeBg(true, bgUrl);
    }

    public void clickClose(boolean isShowComment) {
        if (!isPay) {
            CommonDialog.getInstance(null, "您这么快就要变心了吗", "无情离开", "继续聊天", false).addOnDialogListener(new CommonDialog.OnDialogListener() {
                @Override
                public void cancel() {
                    p.quitRoom(roomId + "", 1);
                }

                @Override
                public void sure() {
                    //    showCenterGiftPop();
                }
            }).show(getSupportFragmentManager(), "离开聊天室");
        } else {
//            p.quitRoom(roomId + "", 0);
            quitRoomSuccess(null, false);
        }
    }

    /**
     * 显示加时蒙版
     * *
     *
     * @param isShow
     * @param status
     */
    public void showAddTimeGif(boolean isShow, int status) {
        if(status > 2){
            status = 0;
        }

        isEnterFirstGift = status == 0 ? true : false;//

        llAddGift.setVisibility(isShow ? View.VISIBLE : View.GONE);
        crvRoom.getChatRcv().setVisibility(isShow?View.GONE:View.VISIBLE);
        if (!isShow) return;
        GiftLevel value = GiftLevel.values()[status];
        tv_gift_title.setText(value.getTitle());
        switch (value.getTitle()){
            case MSG_KQYF:
                ImageLoader.loadGif(ivAddGiftShow,  R.drawable.ic_gift_first);
                break;
            case MSG_ZXQY:
                ImageLoader.loadGif(ivAddGiftShow,  R.drawable.ic_gift_second);
                break;
            case MSG_ZDSS:
                ImageLoader.loadGif(ivAddGiftShow,  R.drawable.ic_gift_three);
                break;

        }



    }

    /**
     * 主播点击更多
     */
    private void initMuch() {
        initBeautiView();
        liveMorePopwindow = new LiveMorePopwindow(this, isMirror);
        liveMorePopwindow.setOnItemClickListener((adapter, view, position) -> {
            if (position >= 0 && position <= 2) {
                fl_faceunity.setVisibility(View.VISIBLE);
            } else {
                fl_faceunity.setVisibility(View.GONE);
            }
            switch (position) {
                case 0:
                    mEffectViewStub.setVisibility(View.GONE);
                    mBottomViewStub.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mBottomViewStub.setVisibility(View.GONE);
                    mEffectViewStub.setVisibility(View.VISIBLE);
                    setEffectData(1);
                    break;
                case 2:
                    mBottomViewStub.setVisibility(View.GONE);
                    mEffectViewStub.setVisibility(View.VISIBLE);
                    setEffectData(6);
                    break;
                case 3:
                    //是否开启观众端镜像观看
                    mLivePusher.setMirror(!isMirror);
                    SPUtils1.put(this, "isMirror", !isMirror);
                    isMirror = !isMirror;
                    break;
                case 4:
                    switchCamera(beautyDto.isFront);
                    break;

            }
        });
    }

    private List<Effect> mSSEffects;
    private List<Effect> mDJEffects;
    int mSSselice = 0;
    int mDJselice = 0;

    private void setEffectData(int effectType) {
        if (effectType == 6) {
            if (null == mSSEffects) {
                mSSEffects = EffectEnum.getEffectsByEffectType(6);
            }
            RecyclerView recyclerView = findViewById(com.fengwo.module_live_vedio.R.id.fu_effect_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mEffectRecyclerAdapter = new EffectRecyclerAdapter(
                    this, mSSEffects, mFURenderer, mSSselice, new EffectRecyclerAdapter.OnDescriptionChangeListener() {

                @Override
                public void onDescriptionChangeListener(int description) {
                    mSSselice = description;
                }
            }));
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }
        if (effectType == 1) {
            if (null == mDJEffects) {
                mDJEffects = EffectEnum.getEffectsByEffectType(1);
            }
            RecyclerView recyclerView = findViewById(com.fengwo.module_live_vedio.R.id.fu_effect_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mEffectRecyclerAdapter = new EffectRecyclerAdapter(this, mDJEffects, mFURenderer, mDJselice, new EffectRecyclerAdapter.OnDescriptionChangeListener() {

                @Override
                public void onDescriptionChangeListener(int description) {
                    mDJselice = description;
                }
            }));

            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }

    }


    private void initBeautiView() {
        BeautyParameterModel.init(this);
        mBottomViewStub = (ViewStub) findViewById(com.fengwo.module_live_vedio.R.id.fu_base_bottom);
        mBottomViewStub.setInflatedId(com.fengwo.module_live_vedio.R.id.fu_base_bottom);
        mEffectViewStub = (ViewStub) findViewById(com.fengwo.module_live_vedio.R.id.fu_effect_bottom);
        mEffectViewStub.setInflatedId(com.fengwo.module_live_vedio.R.id.fu_effect_bottom);
        //美颜
        mBottomViewStub.setLayoutResource(com.fengwo.module_live_vedio.R.layout.layout_fu_beauty);
        mBeautyControlView = (BeautyControlView) mBottomViewStub.inflate();
        mBeautyControlView.setOnFUControlListener(mFURenderer);
        //道具 、手势
        mEffectViewStub.setLayoutResource(com.fengwo.module_live_vedio.R.layout.layout_fu_effect);
        mEffectViewStub.inflate();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBeautyControlView != null) {
            mBeautyControlView.onResume();
        }
        if (mEffectRecyclerAdapter != null) {
            mEffectRecyclerAdapter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mEffectRecyclerAdapter != null) {
            mEffectRecyclerAdapter.onPause();
        }
    }

    @Override
    public void onBackPressed() {
        if (fl_faceunity.getVisibility() == View.VISIBLE) {
            fl_faceunity.setVisibility(View.GONE);
            BeautyParameterModel.save();
            return;
        }
    }

    /*废弃*/
    public void receiveImNotice() {
        //主播 收到订单消息
//        netManager.add(RxBus.get().toObservable(RefreshFlirtNoticeOrderEvent.class)
//                .compose(bindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(event -> {
//                    L.e("==========" + event.orderMessageBean);
//                    startRingTip();
//                    //显示订单消息
//                    event.orderMessageBean.setShowTime(System.currentTimeMillis() / 1000);
//                    mAdapter.addData(event.orderMessageBean);
//                    rvOrderList.setVisibility(View.VISIBLE);
//                    rvOrderList.smoothScrollToPosition(mAdapter.getData().size() - 1);
//                }, Throwable::printStackTrace));

    }

    @Override
    public void getOrderNum(int orderNum, boolean clickAdd) {

    }

    private void setInfo() {
        crbUser.setVideoScaleVisibility(false);
        crbUser.addOnClickListener(this);
        crvRoom.setHost(isHost());
        if (!isHost()) {
            hideOrderAndPresent();
        } else {
            crvRoom.hideChatView(true);
        }
    }

    /**
     * 主播订单UI
     */
//    private void initOrderUI() {
//        rvOrderList.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new DatingOrderAdapter();
//        rvOrderList.setAdapter(mAdapter);
//        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                int id = view.getId();
//                OrderMessageBean bean = (OrderMessageBean) baseQuickAdapter.getData().get(i);
//                switch (bean.getActionType()) {
//                    case OrderMessageBean.TYPE_ORDER:
//                        p.anchorOrderReceive(bean.getOrder().getConsNo(), id == R.id.tv_accept_order ? 1 : 0);
//                        break;
//                    case OrderMessageBean.TYPE_ADD_TIME:
//                        p.anchorAddTimeOrderReceive(bean.getOrder().getConsNo(), id == R.id.tv_accept_order ? 1 : 0);
//                        break;
//                }
//                mAdapter.getData().remove(i);
//                if (id == R.id.tv_accept_order) {//接单
//                } else if (id == R.id.tv_refuse) {//拒接
//
//                }
//                rvOrderList.setVisibility(baseQuickAdapter.getData().size() > 0 ? View.VISIBLE : View.GONE);
//
//            }
//        });

//        mAdapter.addTimeOut(new DatingOrderAdapter.OnTimeOutListener() {
//            @Override
//            public void over(String orderNo, int pos) {
    //超时拒单
//                p.anchorOrderReceive(orderNo, 0);
//            }
//        });

//    }
    private void hideOrderAndPresent() {
        //隐藏订单
        crbUser.setVisiblePresent(false);
    }


    /**
     * 开启视频小窗
     */
    public void openSmallWindow() {
        FloatingView floatingView = FloatingView.getInstance();
        floatingView.setHeadImg(roomBean.getHeadImg());
        floatingView.setNickname(roomBean.getNickname());
        floatingView.setGear(getGears());
        floatingView.setRoomId(roomId);
        floatingView.startCountTime();
        if (!floatingView.isShow()) {
            SmallWindowEvent event = new SmallWindowEvent();
            event.setPlayUrl(pullStreamUrl);
            RxBus.get().post(event);
        }

    }
    private int pos = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            FloatingView.getInstance().hide();
        }
    }

    @Override
    public void isAttention(String stringData) {
        // isAttention  0：未关注 1:已关注
        crbUser.setAttentionVisibility(stringData.contains("0") ? true : false);
    }

    @Override
    public void clickAttention() {
        /*TODO 点击关注*/
        AttentionUtils.addAttention(anchorId, new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                L.e("==============");
                if (data.isSuccess()) {
                    toastTip("关注成功");
                    crbUser.setAttentionVisibility(false);
                }
            }

            @Override
            public void _onError(String msg) {
                toastTip(msg);
            }
        });

    }

    ITXLivePlayListener iTXLivePlayListener;
//
    @Override
    public void clickNarrowWindow() {
        if (isPay) {//免费播放不可以小窗
            /*TODO 点击小窗*/
//            if(swWindow.getVisibility()==View.GONE){
            if (!FloatingView.getInstance().isShow()) {

            crvRoom.hideVideoView();
            //   crvRoom.setSpaceweight(1); //设置聊天区域 大小
            crvRoom.hideChatView(false);
            KLog.e("tag","clickNarrowWindow == 续费播放视频");
//            startPlay(pullStreamUrl, (TXCloudVideoView) swWindow.getVideoView(), iTXLivePlayListener = new ITXLivePlayListener() {
//                @Override
//                public void onPlayEvent(int event, Bundle bundle) {
//                    if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
//                        swWindow.setVisibility(View.VISIBLE);
//                    }
//                }
//
//                @Override
//                public void onNetStatus(Bundle bundle) {
//
//                }
//            });
                startPlay(pullStreamUrl, FloatingView.getInstance().getVideoView(), null);
                FloatingView.getInstance().show();
            }else {
//                swWindow.setVisibility(View.GONE);
                FloatingView.getInstance().hide();
                clickSwitch();
            }
        } else {
            ToastUtils.showShort(getApplicationContext(), "请开启缘分");
        }
    }

    @Override
    public void clickfinish() {

    }

    @Override
    public void clickMuch() {
        // TODO点击更多
        liveMorePopwindow.showPopupWindow();
    }


    @Override
    public void clickAddVideoTime() {
//        p.getAddTimeNum(anchorId,true);
    }

    @Override
    public void clickAboutSingle() {
        /*TODO 点击约单人*/
//        ChatRoomBottomPopwindow chatRoomBottomPopwindow = new ChatRoomBottomPopwindow(this, 0);
//        chatRoomBottomPopwindow.showPopupWindow();
    }

    @Override
    public void clickPresentRecord() {
        /*TODO 点击礼物记录*/
        ChatRoomBottomPopwindow chatRoomBottomPopwindow = new ChatRoomBottomPopwindow(this);
        chatRoomBottomPopwindow.showPopupWindow();
    }

    @Override
    public void clickShowSmall() {
        swWindow.setVisibility(View.VISIBLE);
    }
    //    @OnClick(R2.id.ll_wait)
//    public void onClick(View view) {
//        if (view.getId() == R.id.ll_wait) {
//        }
//    }


    @Override
    public void showCommentListDialog() {

    }

    @Override
    protected BeautyDto getBeautyDto() {
        return gson.fromJson((String) SPUtils1.get(this, "beauty", gson.toJson(new BeautyDto())), BeautyDto.class);
    }

    @Override
    protected boolean getMirrir() {
        return (boolean) SPUtils1.get(this, "isMirror", false);
    }

    @Override
    public void orderReceiveSuccess(HttpResult data) {
        // 主播接单/拒接成功 回调
    }

    @Override
    public void updatetime(long time) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRingTip();
        if (mAdapter != null) mAdapter.cancelAllTimers();
        if (iTXLivePlayListener != null) iTXLivePlayListener = null;
    }

    @Override
    public void anchorClose(ReceiveSocketBean bean) {

    }

    @Override
    public void acceptOrder(ReceiveSocketBean bean) {
        L.e("======" + bean.getResult().getAccept());
    }

    @Override
    public void refuseOrder(String value) {

    }

    @Override
    public void toastWarn(ReceiveSocketBean bean1) { //直播间警告
        CommonDialog.getInstance(bean1.getContent().getValue(), "知道了", "警告通知").show(getSupportFragmentManager(), "warn");
    }


    @OnClick({R2.id.fl_faceunity, R2.id.ll_tips})
    public void click(View view) {
        if (view.getId() == R.id.fl_faceunity) {
            if (fl_faceunity.getVisibility() == View.VISIBLE) {
                fl_faceunity.setVisibility(View.GONE);
                BeautyParameterModel.save();
            }
        } else if (view.getId() == R.id.ll_tips) {
            llTips.setVisibility(View.GONE);
        }

    }


    private List<OrderMessageBean> datas = new ArrayList<>();

    /*已经没有点单流程 已废弃*/
    @Override
    public void receiveCancelOrder(ReceiveSocketBean cancelOrder) {
        //主播接收到取消订单的消息
//        datas.clear();
//        datas.addAll(mAdapter.getData());
//        try {
//            for (OrderMessageBean bean : datas) {
//                if (bean.getUser().getUserId().equals(cancelOrder.getUser().getUserId())) {
//                    mAdapter.getData().remove(bean);
////                    rvOrderList.setVisibility(mAdapter.getData().size() > 0 ? View.VISIBLE : View.GONE);
//                }
//            }
//        } catch (Exception e) {
//            L.e("============");
//        }
    }

    @Override
    public void isWaitStatus(String msg) {

    }


    @Override
    public void getAnchorRoomInfoFailure(String description) {

    }

    @Override
    public void getAnchorRoomInfoSuccess(GetAnchorRoomInfo data) {

    }

}
