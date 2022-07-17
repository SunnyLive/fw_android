package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.base.BaseEachAttention;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.UserMedalBean;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserMedalService;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.EventIntervalUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.SendGiftToUserEvent;
import com.fengwo.module_live_vedio.eventbus.ShowSendMsgEditEvent;
import com.fengwo.module_live_vedio.mvp.dto.IsBlackDto;
import com.fengwo.module_live_vedio.utils.PrivilegeEffectUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

public class UserInfoPopwindow extends BasePopupWindow {

    @Autowired
    UserProviderService service;
    @Autowired
    UserMedalService userMedalService;

    @BindView(R2.id.tv_jubao)
    TextView tvJubao;
    @BindView(R2.id.tv_lahei)
    TextView tvLahei;
    @BindView(R2.id.tv_jinyan)
    TextView tvJinyan;
    @BindView(R2.id.tv_tichu)
    TextView tvTichu;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.iv_level1)
    ImageView ivLevel1;
    @BindView(R2.id.iv_level2)
    ImageView ivLevel2;
    @BindView(R2.id.iv_level3)
    ImageView ivLevel3;
    @BindView(R2.id.ll_level)
    LinearLayout llLevel;
    @BindView(R2.id.tv_fengwohao)
    TextView tvFengwohao;
    @BindView(R2.id.line)
    View line;
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.tv_attention)
    TextView tvAttention;
    @BindView(R2.id.tv_fans)
    TextView tvFans;
    @BindView(R2.id.tv_send)
    TextView tvSend;
    @BindView(R2.id.tv_fengmizhi)
    TextView tvFengmizhi;
    @BindView(R2.id.ll_str)
    LinearLayout llStr;
    @BindView(R2.id.line_horizontal)
    View lineHorizontal;
    @BindView(R2.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R2.id.btn_sisin)
    TextView btnSisin;
    @BindView(R2.id.btn_addmanager)
    TextView btnAddManager;
    @BindView(R2.id.btn_at)
    TextView btnAt;
    @BindView(R2.id.btn_attention)
    TextView btnAttention;
    @BindView(R2.id.btn_gift)
    TextView btnGift;
    @BindView(R2.id.iv_activity_tag)
    ImageView ivActivityTag;
    @BindView(R2.id.iv_sex)
    ImageView ivSex;
    @BindView(R2.id.iv_head_board)
    ImageView ivHeadBoard;
    private Context context;
    ChooseTimePopwindow chooseTimePopwindow;
    private String userId;  // 当前查看用户的uid
    private String userName;
    private int channelId;
    private int attention;//0:未关注，>0:已关注
    private String headImg;
    private Context mContext;

    private UserInfo userInfo;
    private UserMedalBean userMedalBean;//活动勋章信息

    private final ConstraintLayout.LayoutParams layoutParams;
    private final CompositeDisposable compositeDisposable;
    private Gson gson = new Gson();

    public UserInfoPopwindow(Context context, int uid, int channelId, int roomManager) {
        super(context);
        mContext = context;
        setPopupGravity(Gravity.BOTTOM);
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this, getContentView());
        this.context = context;
        this.userId = uid + "";
        this.channelId = channelId;
        if (service.getUserInfo().id == uid) {
            findViewById(R.id.ll_bottom).setVisibility(View.GONE);
        }
        compositeDisposable = new CompositeDisposable();

        getUserInfo(userId);

        if (uid == channelId) {//查看的这个人就是当前主播
            btnGift.setVisibility(View.GONE);
        }
        btnSisin.setVisibility(service.getUserInfo().privateLetter == 0 ? View.GONE : View.VISIBLE);
        //拉黑默认位置
        layoutParams = (ConstraintLayout.LayoutParams) tvLahei.getLayoutParams();
        layoutParams.topToTop = R.id.cl_layout;
        layoutParams.rightToRight = 1;
        layoutParams.leftToRight = R.id.tv_jubao;
        tvLahei.setLayoutParams(layoutParams);
        if (roomManager == 2) {//主播
            tvTichu.setVisibility(View.VISIBLE);
            tvJinyan.setVisibility(View.VISIBLE);
            btnAddManager.setVisibility(View.VISIBLE);
            btnAt.setVisibility(View.GONE);
            tvLahei.setVisibility(View.VISIBLE);
        } else if (roomManager == 1) {//是房管
            tvTichu.setVisibility(View.VISIBLE);
            tvJinyan.setVisibility(View.VISIBLE);
            btnAddManager.setVisibility(View.GONE);
            btnAt.setVisibility(View.VISIBLE);
            tvLahei.setVisibility(View.VISIBLE);
        } else {//普通用户
            tvTichu.setVisibility(View.GONE);
            tvJinyan.setVisibility(View.GONE);
            btnAddManager.setVisibility(View.GONE);
            btnAt.setVisibility(View.VISIBLE);
            tvLahei.setVisibility(View.VISIBLE);
            //改变按钮位置
            layoutParams.rightToRight = R.id.cl_layout;
            layoutParams.topToTop = R.id.cl_layout;
            layoutParams.leftToRight = 1;
            layoutParams.rightMargin = DensityUtils.dp2px(getContext(), 12);
            tvLahei.setLayoutParams(layoutParams);
        }
        if (Integer.parseInt(userId) == service.getUserInfo().id) {
            tvJubao.setVisibility(View.GONE);
            tvLahei.setVisibility(View.GONE);
            tvTichu.setVisibility(View.GONE);
            tvJinyan.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.live_pop_userinfo);
        return v;
    }

    private void showChooseTimePop(int type) {
        chooseTimePopwindow = new ChooseTimePopwindow(context, type, channelId, Integer.parseInt(userId));
        chooseTimePopwindow.showPopupWindow();
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
    public void showPopupWindow() {
        super.showPopupWindow();
    }

    @OnClick({R2.id.btn_gift, R2.id.tv_jubao, R2.id.tv_lahei, R2.id.tv_jinyan, R2.id.tv_tichu, R2.id.btn_attention, R2.id.btn_sisin, R2.id.btn_at, R2.id.btn_zhuye, R2.id.btn_addmanager})
    public void onViewClicked(View view) {
        if (null == userInfo) {
            return;
        }
        int id = view.getId();
        if (id == R.id.tv_jubao) {
            ArouteUtils.toPathWithId(ArouterApi.REPORT_ACTIVITY, userId);
        } else if (id == R.id.tv_lahei) {
            if (tvLahei.getText().toString().equals("已拉黑")) return;
            new CustomerDialog.Builder(context)
                    .setMsg("确认把用户" + tvName.getText().toString() + "拉黑吗")
                    .setPositiveButton(new CustomerDialog.onPositiveInterface() {
                        @Override
                        public void onPositive() {
                            addBlackList();
                        }
                    }).create().show();
        } else if (id == R.id.tv_jinyan) {
            showChooseTimePop(1);
        } else if (id == R.id.tv_tichu) {
            showChooseTimePop(2);
        } else if (id == R.id.btn_attention) {
            if (attention == 0) {
                attention(userId);
            } else {
//                removeAttention(userId);
                ToastUtils.showShort(context, "您已关注");
            }
        } else if (id == R.id.btn_sisin) {
            if (!EventIntervalUtil.canOperate()) return;
            String currentUserId = service.getUserInfo().fwId;
            if (userId.equals(currentUserId)) {
                ToastUtils.showShort(getContext(), "当前用户为您自己");
                return;
            }
            checkoutChat(currentUserId);
        } else if (id == R.id.btn_at) {
            if (PrivilegeEffectUtils.getInstance().isMsgEnable(service.getUserInfo().userLevel)) {
                RxBus.get().post(new ShowSendMsgEditEvent("@" + tvName.getText().toString()));
            } else {
                ToastUtils.showShort(BaseApplication.mApp, PrivilegeEffectUtils.getInstance().msgLevelNoEnough());
            }
            dismiss();
        } else if (id == R.id.btn_zhuye) {
            ARouter.getInstance().build(ArouterApi.USER_DETAIL).withInt("id", Integer.parseInt(this.userId)).navigation();
        } else if (id == R.id.btn_addmanager) {
            new CustomerDialog.Builder(context)
                    .setMsg("添加" + tvName.getText().toString() + "为房管")
                    .setPositiveButton(new CustomerDialog.onPositiveInterface() {
                        @Override
                        public void onPositive() {
                            addRoomManager();
                        }
                    }).create().show();
        } else if (id == R.id.btn_gift) {
            RxBus.get().post(new SendGiftToUserEvent(Integer.parseInt(this.userId), userName));
        }
    }

    private void checkoutChat(String currentUserId) {
        new RetrofitUtils().createApi(LiveApiService.class)
                .getEachAttention(Integer.parseInt(userId))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<BaseEachAttention>>() {
                    @Override
                    public void _onNext(HttpResult<BaseEachAttention> data) {
                        if (data.isSuccess() && data.data != null) {
                            L.e("state " + data.data.state);
                            if (data.data.state == 2) {
                                ArouteUtils.toChatSingleActivity(currentUserId, userId, userName, headImg);
                            } else {
                                ToastUtils.showShort(context, "互相关注后才能私信");
                            }
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                });
    }


    private void getUserInfo(String uid) {
        int userId = Integer.parseInt(uid)<<3;
        Map map =new HashMap();
        map.put("userId",userId);
        compositeDisposable.add(new RetrofitUtils().createApi(LiveApiService.class)
                .getUserinfoByIdNew(createRequestBody(map))
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<UserInfo>>() {
                    @Override
                    public void _onNext(HttpResult<UserInfo> data) {
                        if (data.isSuccess()) {
                            initUserInfo(data.data);
                            userInfo = data.data;
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));

        compositeDisposable.add( //判断是否已拉黑
                new RetrofitUtils().createApi(LiveApiService.class)
                        .judgeBlack(uid)
                        .compose(RxUtils.applySchedulers())
                        .subscribeWith(new LoadingObserver<HttpResult<IsBlackDto>>() {
                            @Override
                            public void _onNext(HttpResult<IsBlackDto> data) {
                                if (data.isSuccess()) {
                                    IsBlackDto bean = data.data;
                                    tvLahei.setText(bean.isBlack == 1 ? "已拉黑" : "拉黑");
                                }
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        }));


    }

    @Override
    public void dismiss() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.dismiss();
    }

    private void addRoomManager() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .AddRoomManager(Integer.parseInt(userId))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(context, data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                });

    }

    private void addBlackList() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .AddBlackList(Integer.parseInt(userId))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(context, data.description);
                        tvLahei.setText("已拉黑");
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                });

    }

    //关注
    public void attention(String id) {
        new RetrofitUtils().createApi(LiveApiService.class).addAttention(id)
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(context, data.description);
                        if (data.isSuccess())
                            getUserInfo(id);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }

    //取消关注
    public void removeAttention(String id) {
        new RetrofitUtils().createApi(LiveApiService.class).removeAttention(id)
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(context, data.description);
                        if (data.isSuccess())
                            getUserInfo(id);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }

    private void initUserInfo(UserInfo data) {
//        if (!CommentUtils.isListEmpty(data.userMedalsList)) {
//            ivActivityTag.setVisibility(View.VISIBLE);
//            int medalId = data.userMedalsList.get(0).medalId - 1;
//            int res = ImageLoader.getResId("ic_angel_" + (medalId < 4 ? medalId : medalId % 4), R.drawable.class);
//            ivActivityTag.setImageResource(res);
//        } else {
//            ivActivityTag.setVisibility(View.GONE);
//        }
        getUserMedal();
        userName = data.getNickname();
        headImg = data.getHeadImg();
        ImageLoader.loadImg(ivHeader, headImg);
        tvName.setText(data.getNickname());
        int levelRes = ImageLoader.getResId("login_ic_v" + data.getLevel(), R.drawable.class);
        ivLevel1.setImageResource(levelRes);
        if (data.getLiveLevel() > 0) {
            int level2Res = ImageLoader.getResId("login_ic_type3_v" + data.getLiveLevel(), R.drawable.class);
            ivLevel2.setImageResource(level2Res);
        } else {
            ivLevel2.setVisibility(View.GONE);
        }
        ivLevel3.setVisibility(data.getVipLevel()>0?View.VISIBLE:View.GONE);
        ivLevel3.setImageResource(ImageLoader.getVipLevel(data.getMyVipLevel()));
        ivSex.setImageResource(data.getSex() == 2 ? R.drawable.ic_girl_sex : R.drawable.ic_boy_sex);
        ivSex.setVisibility(data.getSex() == 0 ? View.GONE : View.VISIBLE);
        tvLocation.setText("坐标: " + data.getLocation());
        tvFengwohao.setText("蜂窝号: " + data.getFwId());
        tvAttention.setText(data.getAttention() + "");
        tvFans.setText(data.getFans() + "");
        tvSend.setText(String.valueOf(data.getSendGiftTotal()));
        tvFengmizhi.setText(DataFormatUtils.formatNumbers(data.getReceive()));
        if (data.getIsAttention() >= 1) {
            btnAttention.setText("已关注");
        } else {
            btnAttention.setText("+关注");
        }
        attention = data.getIsAttention();
    }

    private void getUserMedal() {
        userMedalService.getUserMedal(Integer.parseInt(userId), 0, new LoadingObserver<HttpResult<UserMedalBean>>() {
            @Override
            public void _onNext(HttpResult<UserMedalBean> data) {
                userMedalBean = data.data;
//                if (userMedalBean.medalOneId>0)
//                    ivActivityTag.setImageResource(ImageLoader.getResId("ic_medal_"+userMedalBean.medalOneId,R.drawable.class));
//                if (userMedalBean.medalOneId == 12){ //蜂国蜂后
//                    ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_"+1,R.drawable.class));
//                }else if (userMedalBean.medalOneId == 13){//蜂国储君
//                    ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_"+2,R.drawable.class));
//                }else if (userMedalBean.medalOneId == 14){//蜂国郡主
//                    ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_"+3,R.drawable.class));
//                }
//                ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_"+3,R.drawable.class));
                if (!TextUtils.isEmpty(data.data.medalOneIcon))
                ImageLoader.loadImg(ivActivityTag,data.data.medalOneIcon);
                if (!TextUtils.isEmpty(data.data.medalOneHeadFrame))
                ImageLoader.loadImg(ivHeadBoard,data.data.medalOneHeadFrame);
//                else if (userMedalBean.medalOneId == 14){//蜂国精灵  /
//                    ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_"+4,R.drawable.class));
//                }
            }

            @Override
            public void _onError(String msg) {

            }
        });

    }

    public RequestBody createRequestBody(Map map) {
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }

}
