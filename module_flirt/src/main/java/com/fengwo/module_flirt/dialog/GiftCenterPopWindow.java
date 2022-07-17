package com.fengwo.module_flirt.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_comment.utils.StringFormatUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.epf.EPlayerView;
import com.fengwo.module_comment.widget.epf.PlayerScaleType;
import com.fengwo.module_comment.widget.epf.filter.AlphaFrameFilter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.GiftDto;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import razerdp.basepopup.BasePopupWindow;

/**
 * 开启缘分弹窗
 */
public class GiftCenterPopWindow extends BasePopupWindow implements View.OnClickListener {

    public static final int TYPE_NORMAL_GIFT = 0;
    public static final int TYPE_TIME_GIFT = 1;

    private ImageView mIvGift;
    private CompositeDisposable compositeDisposable;
    private List<List<GiftDto>> allGifts;
    private Context mContext;
    private TextView btnSend;
    CountBackUtils countBackUtils;
    private TextView mTvPrice;
    private TextView mTvImpression;
    private RetrofitUtils retrofitUtils;
    private boolean isFast;

    private RxBus bus;
    Disposable disposable;

    private TextView tvName;
    private EPlayerView ePlayerView;
    private FragmentManager fragmentManager;
    private long anchorId;
    private final RelativeLayout flGiftList;
    private boolean isChatRoom;
    private int isFirstEnter;
    private int type;
    private FlirtApiService service;
    private String nickName;
    TextView tvTitle;
    ImageView im_close;
//    private boolean isCommend;

    private GiftDto mGiftDto;   //赠送礼物

    @SuppressLint("CheckResult")
    public GiftCenterPopWindow(Context context, FragmentManager fragmentManager, long anchorId) {
        super(context);
        this.fragmentManager = fragmentManager;
        this.anchorId = anchorId;
        ArouteUtils.inject(this);
        allGifts = new ArrayList<>();
        retrofitUtils = new RetrofitUtils();
        setPopupGravity(Gravity.CENTER);
        mContext = context;
        countBackUtils = new CountBackUtils();
        mIvGift = findViewById(R.id.iv);
        im_close = findViewById(R.id.im_close);
        tvTitle = findViewById(R.id.tv_title);
        flGiftList = findViewById(R.id.fl_gift_list);
        btnSend = findViewById(R.id.btn_send);
        mTvPrice = findViewById(R.id.tv_price);
        ePlayerView = findViewById(R.id.playview);
        tvName = findViewById(R.id.tv_name);
        mTvImpression = findViewById(R.id.tv_impression);
        btnSend.setOnClickListener(this);
        flGiftList.setOnClickListener(this);
        im_close.setOnClickListener(this);
        compositeDisposable = new CompositeDisposable();
        getGifts();
        bus = RxBus.get();

        mHandle = new SafeHandle(getContext()) {
            @Override
            protected void mHandleMessage(Message msg) {
                super.mHandleMessage(msg);
                switch (msg.what) {
                    case WHAT_SHOW_GIFT:
                        showGift();
                        mHandle.sendEmptyMessageDelayed(WHAT_SHOW_GIFT, 500);
                        break;
                }

            }
        };
    }


    private void showGift() {
        getContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (bigGiftList) {
                    if (null == ePlayerView || ePlayerView.getVisibility() == View.VISIBLE) return;
                    //TODO
//                    if (viewGiftMsg != null && viewGiftMsg.getVisibility() == View.VISIBLE) return;
                    if (null == simpleExoPlayer) {
                        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext().getApplicationContext());
                        bigGiftListener = new Player.EventListener() {

                            @Override
                            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                switch (playbackState) {
                                    case ExoPlayer.STATE_READY:
                                        ePlayerView.setVisibility(View.VISIBLE);
                                        L.e("========" + (ePlayerView.getVisibility() == View.VISIBLE));
                                        break;
                                    case ExoPlayer.STATE_ENDED:
                                    case ExoPlayer.STATE_IDLE:

                                        ePlayerView.setVisibility(View.GONE);
                                        if (bigGiftList == null || bigGiftList.size() == 0) {
                                            dismiss();
                                        }
                                        break;
                                    default:
                                        L.e("aaaaaaaaaaaaaaaaonononon" + playbackState);
                                        break;
                                }
                            }


                            @Override
                            public void onPlayerError(ExoPlaybackException error) {
                                L.e("aaaaaaaaaaaaaaaaononoonPlayerError");
                            }
                        };
                        simpleExoPlayer.addListener(bigGiftListener);
                        ePlayerView.setSimpleExoPlayer(simpleExoPlayer);
                        ePlayerView.setGlFilter(new AlphaFrameFilter());
                    }
                    WenboGiftMsg gift;
                    if (bigGiftList.size() > 0) {
                        gift = bigGiftList.poll();
                    } else {
                        return;
                    }
                    //TODO 显示
//                    showGuardAnimation(gift);
                    String path = gift.getGift().getBigImgPath();
                    int length = path.length();
                    int startIndex = path.lastIndexOf("/") + 1;
                    String giftFileName = path.substring(startIndex, length);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/fwhynew/" + giftFileName.split("\\.")[0]);
                    if (file.exists()) {
                        ePlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_FIT_CENTER);
                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getContext().getPackageName()));
                        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(file));
                        simpleExoPlayer.prepare(mediaSource);
                        simpleExoPlayer.setPlayWhenReady(true);
                    }
                }
            }
        });

    }

    /**
     * 聊天室调用
     */
    public void setIsFisrt(int isFirstEnter) {
        this.isFirstEnter = isFirstEnter;
        if(isFirstEnter>2){
            isFirstEnter = 0;
        }
      switch (isFirstEnter){
          case 1:
              btnSend.setText("再续缘");
              tvTitle.setText("再续前缘");
              break;
          case 2:

              btnSend.setText("再续缘");
              tvTitle.setText("缘定三生");
              break;
          case 0:
              btnSend.setText("开启");
              tvTitle.setText("开启缘分");

              break;
      }
        compositeDisposable.add(service.getGiftTime2(new WenboParamsBuilder().put("anchorId", String.valueOf(anchorId)).build()) .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<List<GiftDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<GiftDto>> data) {
                        if(data.data!=null&&data.data.size()>0){
                            mGiftDto = data.data.get(0);
                            setGift(mGiftDto);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    private void getGifts() {
        service = retrofitUtils.createWenboApi(FlirtApiService.class);
        compositeDisposable.add(service.getGiftTime2(new WenboParamsBuilder().put("anchorId", String.valueOf(anchorId)).build()) .compose(RxUtils.applySchedulers())
                        .subscribeWith(new LoadingObserver<HttpResult<List<GiftDto>>>() {
            @Override
            public void _onNext(HttpResult<List<GiftDto>> data) {
                if(data.data!=null&&data.data.size()>0){
                    mGiftDto = data.data.get(0);
                    setGift(mGiftDto);
                }
            }

            @Override
            public void _onError(String msg) {

            }
        }));

    }

    /**
     * 设置礼物
     */
    private void setGift(GiftDto data){
        if(data!=null){
            ImageLoader.loadImgFitCenter(mIvGift, data.smallImgPath);
            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            scaleAnimation.setRepeatCount(Animation.INFINITE);
            scaleAnimation.setDuration(400);
            mIvGift.startAnimation(scaleAnimation);
            tvName.setText(data.giftName);
            mTvImpression.setText("印象值"+data.addDurationNum);
            mTvPrice.setText(StringFormatUtils.formatDouble(data.price));
        }

    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_center_flirt_gifts);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_send) {
            if (null == listener || mGiftDto == null) return;
            if (mGiftDto.gears < 3) {
                listener.onTimeGiftSend(mGiftDto, false);
            } else {
                ToastUtils.showShort(getContext(), "本场加时用尽 需重新进入");
            }
            dismiss();
        } else if (id == R.id.fl_gift_list) {
            dismiss();
        }else if(id == R.id.im_close){
            dismiss();
        }
    }

    OnGiftSendListener listener;

    public interface OnGiftSendListener {
        void onTimeGiftSend(GiftDto id, boolean isFirstEnter);
    }

    public void setOnGiftSendListener(OnGiftSendListener l) {
        listener = l;

    }

    @Autowired
    UserProviderService userProviderService;
    private SimpleExoPlayer simpleExoPlayer;
    private Player.EventListener bigGiftListener;
    private Queue<WenboGiftMsg> bigGiftList = new ConcurrentLinkedQueue<>();//礼物队列
    private SafeHandle mHandle;
    private final static int WHAT_SHOW_GIFT = 1;

    @Override
    public void dismiss() {
        super.dismiss();
        if (mHandle != null) mHandle.removeMessages(WHAT_SHOW_GIFT);
    }
}
