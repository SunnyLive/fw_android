package com.fengwo.module_chat.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.CardMemberBean;
import com.fengwo.module_chat.mvp.model.bean.CardMemberModel;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;
import com.fengwo.module_chat.mvp.model.bean.CommentModel;
import com.fengwo.module_chat.mvp.presenter.ChatCardPresenter;
import com.fengwo.module_chat.mvp.ui.activity.publish.PostCardActivity;
import com.fengwo.module_chat.mvp.ui.adapter.ChatCardAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IChatCardView;
import com.fengwo.module_chat.mvp.ui.dialog.ChatCardPowerPopwindow;
import com.fengwo.module_chat.mvp.ui.dialog.ChatCardSettingPopwindow;
import com.fengwo.module_chat.mvp.ui.dialog.CommentDialog;
import com.fengwo.module_chat.mvp.ui.dialog.CommentInputDialog;
import com.fengwo.module_chat.mvp.ui.dialog.RePushPopWindow;
import com.fengwo.module_chat.mvp.ui.event.CommentRefreshEvent;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.AttentionRefreshEvent;
import com.fengwo.module_comment.event.RefreshCardList;
import com.fengwo.module_comment.event.RefreshCardNum;
import com.fengwo.module_comment.event.RefreshEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.SuperPlayerGlobalConfig;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = ArouterApi.CARD_HOME)
public class ChatCardActivityNew extends BaseMvpActivity<IChatCardView, ChatCardPresenter>
        implements IChatCardView, ITXVodPlayListener {

    private ChatCardAdapter cardAdapter;
    private BaseQuickAdapter<CardMemberBean, BaseViewHolder> memberAdapter;
    private ArrayList<CircleListBean> cardlists;
    private boolean isFirst = true;
    private int preIndex = 0;
    private CountBackUtils attentionCb;

    public static void start(Context context, ArrayList<CircleListBean> data, String cardId, String circleId, int showSame, int uid, int mylike, int tab) {
        Intent intent = new Intent(context, ChatCardActivityNew.class);
        intent.putExtra("data", data);
        intent.putExtra("cardId", cardId);
        intent.putExtra("circleId", circleId);
        intent.putExtra("showSame", showSame);
        intent.putExtra("uid", uid);
        intent.putExtra("myLike", mylike);
        intent.putExtra("tab", tab);
        context.startActivity(intent);
    }

    @Autowired
    UserProviderService service;
    @BindView(R2.id.title)
    AppTitleBar titleBar;
    @BindView(R2.id.recyclerview)
    RecyclerView rvCard;
    @BindView(R2.id.rvMember)
    RecyclerView rvMember;
    @BindView(R2.id.tv_circle_num)
    TextView tvCircleCount;
    private CommentDialog commentDialog;

    private final String PAGE_SIZE = ",20";
    private boolean hasMore = true;

    private String circleId;
    private String cardId;
    private int uid;
    private int tab;
    private int myLike = 0;//是否是点赞列表
    private int pos;
    private int page = 1;
    private int currentP = 0;

    private ArrayList<ChatCardBean> cards = new ArrayList<>();

    private TXVodPlayer mVodPlayer;
    private TXVodPlayConfig mVodPlayConfig;
    private SafeHandle mHandle;

    private void initVodPlayer() {
        if (mVodPlayer != null)
            return;
        mVodPlayer = new TXVodPlayer(this);
        SuperPlayerGlobalConfig config = SuperPlayerGlobalConfig.getInstance();
        mVodPlayConfig = new TXVodPlayConfig();
        mVodPlayConfig.setCacheFolderPath(Environment.getExternalStorageDirectory().getPath() + "/.nomedia");
        mVodPlayConfig.setMaxCacheItems(config.maxCacheItem);
        mVodPlayer.setConfig(mVodPlayConfig);
        mVodPlayer.setLoop(true);
        mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mVodPlayer.setVodListener(this);
        mVodPlayer.enableHardwareDecode(config.enableHWAcceleration);
    }

    private void playVideo(ChatCardBean bean) {
        TXCloudVideoView videoView = cardAdapter.getVideo(preIndex);
        videoView.updateVideoViewSize(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
        mVodPlayer.setPlayerView(videoView);
        mVodPlayer.setAutoPlay(true);
        mVodPlayer.setVodListener(this);
        mVodPlayer.startPlay(bean.imgContent.get(0).imageUrl);
    }

    @Override
    public ChatCardPresenter initPresenter() {
        return new ChatCardPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        int showSame = getParameter();
        titleBar.setMoreVisible(showSame == 0);
        titleBar.setMoreClickListener(v -> {
            ChatCardSameActivity.start(this, cardAdapter.getItem(preIndex).id,  cardAdapter.getItem(preIndex).circleId);
        });

        initViewPager();
        initMember();
        initVodPlayer();
        attentionCb = new CountBackUtils();
        p.getCardList(cardId, circleId, 0, uid, tab, myLike);
        mHandle = new SafeHandle(this) {
            @Override
            protected void mHandleMessage(Message msg) {
                if (isFirst) {
                    isFirst = false;
                    p.getMemberList(preIndex, cardAdapter.getItem(preIndex).circleId);
                    if (cardAdapter.getItem(preIndex).isVedio()) {
                        cardAdapter.showVedio(preIndex);
                        playVideo(cardAdapter.getItem(preIndex));
                    }
                }
            }
        };

        RxBus.get().toObservable(AttentionRefreshEvent.class)
                .compose(bindToLifecycle()).subscribe(event -> {
                    refreshAttention(String.valueOf(event.refreshUid), String.valueOf(event.isAttention));
                }
        );

        RxBus.get().toObservable(CommentRefreshEvent.class)
                .compose(bindToLifecycle())
                .subscribe(commentRefreshEvent -> refreshCommentNumber(commentRefreshEvent.cardId));

        RxBus.get().toObservable(RefreshEvent.class)
                .compose(bindToLifecycle())
                .subscribe(refreshEvent -> onRefresh());
    }

    /**
     * Gets the parameters passed by other pages
     */
    private int getParameter() {
        Intent intent = getIntent();
        //卡片的位置
        cardId = intent.getStringExtra("cardId");
        circleId = intent.getStringExtra("circleId");
        //用于判断是否隐藏 Same
        int showSame = intent.getIntExtra("showSame", 0);
        cardlists = (ArrayList<CircleListBean>) intent.getSerializableExtra("data");
        uid = intent.getIntExtra("uid", 0);
        myLike = intent.getIntExtra("myLike", 0);
        tab = intent.getIntExtra("tab", 0);

        return showSame;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_chat_card_new;
    }

    @OnClick(R2.id.tv_enter_circle)
    public void onClick(View view) {
        try {
            ChatCardBean card = cardAdapter.getData().get(preIndex);
            // 进入群聊
            ArouteUtils.toChatGroupActivity(String.valueOf(service.getUserInfo().id), card.groupId, card.circleName, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void stopPlay() {
        if (mVodPlayer != null) {
            mVodPlayer.setVodListener(null);
            mVodPlayer.stopPlay(false);
            TXCloudVideoView videoView = cardAdapter.getVideo(preIndex);
            if (null != videoView)
                videoView.onDestroy();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mVodPlayer && mVodPlayer.isPlaying()) {
            mVodPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mVodPlayer && null != cardAdapter.getItem(preIndex) && cardAdapter.getItem(preIndex).isVedio()) {
            mVodPlayer.resume();
        }
    }

    // 初始化卡片Fragment
    private void initViewPager() {
        cardAdapter = new ChatCardAdapter(cards, service.getUserInfo().id);
        rvCard.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(rvCard);

        rvCard.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int first = linearLayoutManager.findFirstVisibleItemPosition();
                    if (preIndex == first) return;
                    cardAdapter.notifyDataSetChanged();
                    stopPlay();
                    if (cardAdapter.getItem(preIndex) == null) return;
                    String preCir = cardAdapter.getItem(preIndex).circleId;
                    preIndex = first;
                    circleId = preCir;
//                    if (firstVisibleItem != preIndex && lastVisibleItem != preIndex) {
//                        preIndex = firstVisibleItem;
                    ChatCardBean cardBean = cardAdapter.getData().get(preIndex);
                    if (cardBean.type.equals("2")) {//视频
                        playVideo(cardBean);
                    }
                    titleBar.setTitle(cardBean.circleName);
                    if (!preCir.equals(cardAdapter.getItem(preIndex).circleId) && p != null)
                        p.getMemberList(preIndex, cardAdapter.getItem(preIndex).circleId);
                    // 加载更多数据
                    if (hasMore && preIndex == cards.size() - 2) {
                        p.getCardList(cardAdapter.getData().get(cardAdapter.getItemCount() - 1).id, circleId, 1, uid,tab, myLike);
                    }
                    if (hasMore && preIndex == 1) {
                        p.getCardList(cardAdapter.getData().get(0).id, circleId, -1, uid,tab, myLike);
                    }
//                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        cardAdapter.bindToRecyclerView(rvCard);
        cardAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.iv_add_attention) {
                    p.attentionUser(cardAdapter.getItem(position).userId, position);
                } else if (id == R.id.view_like) {
                    p.cardLike(cardAdapter.getItem(position).id, position);
                } else if (id == R.id.iv_chat_comment) {
                    if (!isFastClick()) {
                        ChatCardBean card = cardAdapter.getItem(position);
                        commentDialog = new CommentDialog(ChatCardActivityNew.this, card.id);
                        commentDialog.setOnCommentListener(new CommentDialog.OnCommentListener() {
                            @Override
                            public void comment(int parentIndex, int position, int type, CommentModel commentModel, String cardId, int parentId) {
                                CommentInputDialog commentEditDialog = new CommentInputDialog();
                                commentEditDialog.setData(parentIndex, position, type, commentModel, cardId, parentId);
                                commentEditDialog.show(getSupportFragmentManager(), "commentEdit");
                            }
                        });
                        commentDialog.showPopupWindow();
                    }
                } else if (id == R.id.iv_header) {
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, Integer.parseInt(Objects.requireNonNull(cardAdapter.getItem(position)).userId));
                } else if (id == R.id.tvCardSetting) {
                    ChatCardBean cardBean = cardAdapter.getItem(position);
                    assert cardBean != null;
                    if (cardBean.cardStatus == 4 || cardBean.cardStatus == 5) {
                        showCardRePushDialog(cardBean, position);
                    } else {
                        showCardSettingDialog(cardBean, position);
                    }
                } else if (id == R.id.tvShare) {
                    toastTip("分享");
                }
            }
        });
        rvCard.setAdapter(cardAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onRefresh();
    }

    /**
     * 草稿/拒审 重新发布
     *
     * @param cardBean
     * @param position
     */
    private void showCardRePushDialog(ChatCardBean cardBean, int position) {
        RePushPopWindow rePushPopWindow = new RePushPopWindow(ChatCardActivityNew.this, cardBean);
        rePushPopWindow.addOnClickListener(new RePushPopWindow.OnSelectListener() {
            @Override
            public void cardRePush() {
//                postCard();
                Intent intent = new Intent(ChatCardActivityNew.this, PostCardActivity.class);
                intent.putExtra("id",Integer.parseInt(cardId));
                startActivityForResult(intent,222);
                currentP = position;
//                finish();
            }

            @Override
            public void cardDelete() {
                CommonDialog.getInstance("", "确定删除此动态吗?", "取消", "确定", false)
                        .addOnDialogListener(new CommonDialog.OnDialogListener() {
                            @Override
                            public void cancel() {

                            }

                            @Override
                            public void sure() {
                                p.cardDelete(cardBean.id, position);
                                rePushPopWindow.dismiss();
                            }
                        }).show(getSupportFragmentManager(), "删除动态");
            }
        });

        rePushPopWindow.showPopupWindow();
    }

    //发布
    private void postCard() {
       p.doPost(cardId);
    }


    /**
     * 置顶/权限/删除
     *
     * @param cardBean
     * @param position
     */
    private void showCardSettingDialog(ChatCardBean cardBean, int position) {
        ChatCardSettingPopwindow chatCardSettingPopwindow = new ChatCardSettingPopwindow(ChatCardActivityNew.this, cardBean);
        chatCardSettingPopwindow.addOnClickListener(new ChatCardSettingPopwindow.OnSelectListener() {
            @Override
            public void cardTop() {
                p.cardTop(cardBean.id,
                        TextUtils.isEmpty(cardBean.getTopTime())
                        , position);
                chatCardSettingPopwindow.dismiss();
            }

            @Override
            public void cardPower() {
                ChatCardPowerPopwindow chatCardPowerPopwindow = new ChatCardPowerPopwindow(ChatCardActivityNew.this);
                chatCardPowerPopwindow.addOnClickListener(status -> {
                    p.cardPower(cardBean.id,
                            status,
                            position);
                    chatCardPowerPopwindow.dismiss();
                });
                chatCardPowerPopwindow.showPopupWindow();
                chatCardSettingPopwindow.dismiss();
            }

            @Override
            public void cardDelete() {
                CommonDialog.getInstance("", "确定删除此动态吗?", "取消", "确定", false)
                        .addOnDialogListener(new CommonDialog.OnDialogListener() {
                            @Override
                            public void cancel() {

                            }

                            @Override
                            public void sure() {
                                p.cardDelete(cardBean.id, position);
                                chatCardSettingPopwindow.dismiss();
                            }
                        }).show(getSupportFragmentManager(), "删除动态");
            }
        });
        chatCardSettingPopwindow.showPopupWindow();
    }


    private void initMember() {
        rvMember.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        memberAdapter = new BaseQuickAdapter<CardMemberBean, BaseViewHolder>(R.layout.chat_item_card_member) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, CardMemberBean item) {
                if (helper.getAdapterPosition() == 0) {
                    RecyclerView.MarginLayoutParams lp =
                            (RecyclerView.MarginLayoutParams) helper.itemView.getLayoutParams();
                    lp.leftMargin = 0;
                    helper.itemView.setLayoutParams(lp);
                }
                ImageView imageView = helper.getView(R.id.ivMember);
                if (helper.getAdapterPosition() >= 3) {
                    imageView.setImageResource(R.drawable.ic_card_home_more);
                } else ImageLoader.loadImg(imageView, item.headImg);
            }
        };
        rvMember.setAdapter(memberAdapter);
    }

    public void refreshMemberUI(CardMemberModel data) {
        if (data == null) return;
        if (Integer.parseInt(data.usersNum) > 3) {
            List<CardMemberBean> beans = data.members.subList(0, 3);
            beans.add(new CardMemberBean());
            memberAdapter.setNewData(beans);
        } else {
            memberAdapter.setNewData(data.members);
        }
        tvCircleCount.setText(String.format("%s人", data.usersNum));
    }

    private static final String TAG = "ChatCardActivity";


    @Override
    public void setMemberList(int position, CardMemberModel data) {
        memberAdapter.setNewData(data.members);
        refreshMemberUI(data);
    }

    @Override
    public void setCardList(ArrayList<ChatCardBean> records) {
        if (CommentUtils.isListEmpty(records)) return;
        titleBar.setTitle(records.get(0).circleName);
        cardAdapter.addData(records);
        p.getCardList(records.get(0).id, circleId, -1, uid,tab, myLike);
        mHandle.sendEmptyMessageDelayed(1, 200);
    }

    @Override
    public void addLeftCardList(ArrayList<ChatCardBean> records) {
        if (CommentUtils.isListEmpty(records)) return;
        cardAdapter.addData(0, records);
        preIndex = records.size();

    }


    public void onRefresh(){
//        p.getCardList(cardId, circleId, 0, uid, tab, myLike);
        if (cardAdapter!=null) {
            cardAdapter.getData().get(currentP).cardStatus = 0;
        }

    }
    @Override
    public void addRightCardList(ArrayList<ChatCardBean> records) {
        if (CommentUtils.isListEmpty(records)) return;
        cardAdapter.addData(records);
    }

    @Override
    public void attentionSuccess(String id, int position) {
        refreshAttention(id, "1");

        cardAdapter.setAttentionOk(position);
        attentionCb.countBack(1, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {

            }

            @Override
            public void finish() {
                cardAdapter.setAttention(true, preIndex);
            }
        });
    }

    public void refreshAttention(String id, String isAttention) {
        List<ChatCardBean> data = cardAdapter.getData();
        for (ChatCardBean bean : data) {
            if (bean.userId.equals(id)) {
                bean.isAttention = isAttention;
            }
        }
        cardAdapter.setNewData(data);
    }

    public void refreshCommentNumber(String id) {
        List<ChatCardBean> data = cardAdapter.getData();
        for (ChatCardBean bean : data) {
            if (bean.id.equals(id)) {
                bean.comments = String.valueOf(Integer.parseInt(bean.comments) + 1);
            }
        }
        cardAdapter.setNewData(data);
    }

    @Override
    public void cardLikeSuccess(String id, int position) {
        ChatCardBean bean = cardAdapter.getItem(position);
        if (bean.isLike.equals("1")) {
            bean.isLike = "0";
            bean.likes = Integer.parseInt(bean.likes) - 1 + "";
            cardAdapter.getData().set(position, bean);
            cardAdapter.setLike(false, preIndex);
        } else {
            bean.isLike = "1";
            bean.likes = Integer.parseInt(bean.likes) + 1 + "";
            cardAdapter.getData().set(position, bean);
            cardAdapter.setLike(true, preIndex);
        }


    }

    @Override
    public void cardTopSuccess(String id, int position) {
        RxBus.get().post(new RefreshCardList());
        toastTip("设置成功");

        cardAdapter.getData().get(position).setTopTime(TextUtils.isEmpty(cardAdapter.getData().get(position).getTopTime())
                ? new Date().toString() : null);
    }

    @Override
    public void cardPowerSuccess(String id, int position, int powerStatus) {
        RxBus.get().post(new RefreshCardList());
        toastTip("权限设置成功");

        cardAdapter.getData().get(position).setPowerStatus(powerStatus);
    }

    @Override
    public void cardDeleteSuccess(String id, int position) {
        RxBus.get().post(new RefreshCardNum());
        RxBus.get().post(new RefreshCardList());
        toastTip("删除成功");

        finish();
    }

    @Override
    public void postCardSuccess(boolean isDraft) {
        RxBus.get().post(new RefreshCardList());
        toastTip("发布成功!");
        finish();
    }

    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
        if (event == TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED) { //视频播放开始
            cardAdapter.hideDefaultImg(preIndex);
        } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
        } else if (event == TXLiveConstants.PLAY_ERR_HLS_KEY
//                || event == TXLiveConstants.PLAY_ERR_VOD_LOAD_LICENSE_FAIL
//                || event == TXLiveConstants.PLAY_ERR_VOD_UNSUPPORT_DRM
                || event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {// 播放点播文件失败
            Toast.makeText(this, bundle.getString(TXLiveConstants.EVT_DESCRIPTION) + ",尝试其他链接播放", Toast.LENGTH_SHORT).show();
        }

        if (event < 0
//                && event != TXLiveConstants.PLAY_ERR_VOD_LOAD_LICENSE_FAIL
                && event != TXLiveConstants.PLAY_ERR_HLS_KEY
//                && event != TXLiveConstants.PLAY_ERR_VOD_UNSUPPORT_DRM
                && event != TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
            mVodPlayer.stopPlay(true);
            Toast.makeText(this, bundle.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

    }
}