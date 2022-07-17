package com.fengwo.module_chat.mvp.ui.activity.chat_new;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.EnterGroupModel;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.utils.ChatSingleMuchPopwindow;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseEachAttention;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.IsBlackDto;
import com.fengwo.module_comment.iservice.GetUserInfoByIdService;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.FastClickListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.fengwo.module_comment.widget.CustomerDialog;

import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

@Route(path = ArouterApi.CHAT_SINGLE)
public class ChatSingleActivity extends BaseChatActivity {

    @BindView(R2.id.titleBar)
    AppTitleBar titleBar;
    @BindView(R2.id.rl_no_real_idcard)
    RelativeLayout rlNoRealIdcard;
    @BindView(R2.id.rl_greet)
    RelativeLayout rlGreet;
    @BindView(R2.id.tvGreet)
    TextView tvGreet;
    @BindView(R2.id.iv_fee_gif)
    ImageView iv_fee_gif;
    @BindView(R2.id.tv_to_renzhen)
    TextView tvToRenzhen;
    @BindView(R2.id.tv_tips)
    TextView tvTips;
    @BindView(R2.id.fl_lay_input)
    FrameLayout flLayInput;
    @BindView(R2.id.fl_lay_input_root)
    FrameLayout flLayInputRoot;
    private CompositeDisposable compositeDisposable;
    private String name;
    private int isBlack;
    @Autowired
    UserProviderService userProviderService;
    //private UserInfo userInfo;

    @Autowired
    GetUserInfoByIdService getUserInfoByIdService;

    @Override
    public String setFromId() {
        return getIntent().getStringExtra("fromUid");
    }

    @Override
    public String setToId() {
        return getIntent().getStringExtra("toUid");
    }

    @Override
    public void sendText(CharSequence text) {
        p.sendTxtMessage(text.toString());
    }

    @Override
    public void sendImage(String url, int width, int height) {
        p.sendImageMessage(url, width, height);
    }

    @Override
    public void sendAudio(String file, int duration) {
        p.sendAudio(file, duration);
    }

    @Override
    public void sendRandomContent(String title) {
        p.sendTxtMessage(title);
    }

    private ChatSingleMuchPopwindow muchPopwindow;

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        super.initView();
        name = getIntent().getStringExtra("name");
        setTalkName(name);
        compositeDisposable = new CompositeDisposable();
        setTalkAvatar(getIntent().getStringExtra("headerImg"));
        titleBar.setTitle(getIntent().getStringExtra("name"));

        //1.0.22 添加更多
        titleBar.setMoreIcon(R.drawable.ic_chatsingle_much);
        titleBar.setMoreClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIsBlack();

            }
        });
//        checkRealName();
        ImageLoader.loadGif1OneTime(iv_fee_gif,R.drawable.ic_logo_chat);
        iv_fee_gif.setOnClickListener(v -> {
            p.sendRandomContent();
            ImageLoader.loadGif1OneTime(iv_fee_gif,R.drawable.ic_logo_chat);
        });

        if(null!=getIntent().getStringExtra("msg")){
            p.sendTxtMessage(getIntent().getStringExtra("msg"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkRealName();
    }

    private void checkRealName() {
        //实名认证失败 或者 没有实名认证时
        if (userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NULL
                || userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NO
                || userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_ING) {
            rlNoRealIdcard.setVisibility(View.VISIBLE);
            rlGreet.setVisibility(View.GONE);
            tvToRenzhen.setOnClickListener(v -> {
                if (userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_ING) {
                    ToastUtils.showShort(v.getContext(), "实名认证中,请您耐心等待", Gravity.CENTER);
                    return;
                }
                ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                        .withInt("type", Common.SKIP_USER)
                        .withInt("status", userProviderService.getUserInfo().myIdCardWithdraw)
                        .navigation();
            });
        }else if (userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_YES){
            rlNoRealIdcard.setVisibility(View.GONE);
            getUserInfo();
        }
    }

    private void getUserInfo() {
        getUserInfoByIdService.getEachAttention(setToId(), new LoadingObserver<HttpResult<BaseEachAttention>>(this) {
            @Override
            public void _onNext(HttpResult<BaseEachAttention> data) {
//                userInfo = data.data;
//                userInfo.isAttention = 0;
                p.switchStatus = data.data.switchStatus;
                switch (data.data.state) {
                    case 2://互相关注 只有互相关注了才能正常聊天
                        rlGreet.setVisibility(View.GONE);
                        flLayInput.setVisibility(View.GONE);
                        flLayInputRoot.setVisibility(View.VISIBLE);
                        break;
                    case 1://已关注
                        flLayInputRoot.setVisibility(View.GONE);
                        flLayInput.setVisibility(View.GONE);
                        rlGreet.setVisibility(View.VISIBLE);
                        tvGreet.setOnClickListener(new FastClickListener() {
                            @Override
                            public void onNoFastClick(View v) {
                            }
                        });
                        //   p.hasItemInSession(setFromId(), setToId());
                        break;
                    default://未关注
                        if (data.data.switchStatus == 0) {
                            flLayInputRoot.setVisibility(View.GONE);
                            flLayInput.setVisibility(View.GONE);
                            rlGreet.setVisibility(View.VISIBLE);
                            tvGreet.setEnabled(false);
                            tvTips.setText("此功能升级中");
                            tvGreet.setBackgroundResource(R.drawable.radius_gray_bg);
                        } else {
                            rlGreet.setVisibility(View.GONE);
                            flLayInput.setVisibility(View.VISIBLE);
                            flLayInputRoot.setVisibility(View.VISIBLE);
                            p.hasItemInSession(setFromId(), setToId());
                        }
                        break;
                }
            }

            @Override
            public void _onError(String msg) {

            }
        });
    }

    private void showMorePop() {
        if (muchPopwindow == null) {
            muchPopwindow = new ChatSingleMuchPopwindow(ChatSingleActivity.this);
            muchPopwindow.addOnClickListener(new ChatSingleMuchPopwindow.OnMuchClickListener() {
                @Override
                public void clickUserDetail() {
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, Integer.parseInt(setToId()));
                }

                @Override
                public void clickClearRecords() {
                    if (mVoicePlayerWrapper != null)
                        mVoicePlayerWrapper.release();
                    p.deleteChatRecords();
                }

                @Override
                public void clickReport() {
                    ArouteUtils.toPathWithId(ArouterApi.REPORT_ACTIVITY, setToId());
                }

                @Override
                public void clickBlack() {
                    if (isBlack == 1) {
                        deleteBlack();
                    } else {
                        new CustomerDialog.Builder(ChatSingleActivity.this)
                                .setMsg("确认把用户" + name + "拉黑吗")
                                .setPositiveButton(() -> addBlackList()).create().show();
                    }
                }
            });
        }
        muchPopwindow.setIsBlack(isBlack);
        muchPopwindow.showPopupWindow();

        KeyBoardUtils.closeKeybord(mEditText, ChatSingleActivity.this);
    }

    public void deleteBlack() {
        compositeDisposable.add( //判断是否已拉黑
                new RetrofitUtils().createApi(ChatService.class)
                        .deleteBlack(Integer.parseInt(setToId()))
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (data.isSuccess()) {
                                    toastTip("移除成功");
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                toastTip(msg);
                            }
                        })
        );

    }

    /**
     * 判断是否是黑名单
     */
    public void getIsBlack() {
        compositeDisposable.add( //判断是否已拉黑
                new RetrofitUtils().createApi(ChatService.class)
                        .judgeBlack(setToId())
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<IsBlackDto>>() {
                            @Override
                            public void _onNext(HttpResult<IsBlackDto> data) {
                                if (data.isSuccess()) {
                                    IsBlackDto bean = data.data;
                                    L.e("====== " + bean.isBlack);
                                    isBlack = bean.isBlack;
                                    showMorePop();
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                toastTip(msg);
                            }
                        }));
    }

    private void addBlackList() {
        compositeDisposable.add(
                new RetrofitUtils().createApi(ChatService.class)
                        .AddBlackList(Integer.parseInt(setToId()))
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                ToastUtils.showShort(ChatSingleActivity.this, data.description);
                            }

                            @Override
                            public void _onError(String msg) {
                                ToastUtils.showShort(ChatSingleActivity.this, msg);
                            }
                        }));
    }

    @Override
    protected void initData() {
        p.isGroup = false;
        super.initData();
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_single;
    }

    @Override
    public void enterGroupSuccess(EnterGroupModel data) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }

    @Override
    public void enterGroupFail() {

    }

    @Override
    public void hasItemInSession(boolean hasItemInSession) {
        if (!hasItemInSession) {
            //打招呼
            if (getIntent().getStringExtra("greet") != null) {
                p.sendGreetTxtMessage(name, getIntent().getStringExtra("greet"), getIntent().getStringExtra("headerImg"));
            }
        } else {
//            List<ChatMsgEntity> collect = StreamSupport.stream(mChatAdapter.getData()).filter(e -> e.getMsgType() == ChatMsgEntity.MsgType.systemTxtAttention).collect(Collectors.toList());
//            if (collect == null || collect.size() <= 0)
            //不存入数据库，存入数据库会照成历史遗留问题。修改为实时显示 2021/01/05
            p.buildAttentionTxtMessage();
        }
    }

    @Override
    public void onAttentionSuccess() {
        flLayInput.setVisibility(View.GONE);
        List<ChatMsgEntity> collect = StreamSupport.stream(mChatAdapter.getData()).filter(e -> e.getMsgType() == ChatMsgEntity.MsgType.systemTxtAttention).collect(Collectors.toList());
        if (collect != null)
            p.deleteSystemItem(collect.get(0));
    }
}