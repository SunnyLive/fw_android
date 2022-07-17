package com.fengwo.module_chat.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;
import com.fengwo.module_chat.mvp.model.bean.ChatCardImgBean;
import com.fengwo.module_chat.mvp.presenter.ChatCardChildPresenter;
import com.fengwo.module_chat.mvp.ui.adapter.ChatCardThumbAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IChatCardChildView;
import com.fengwo.module_chat.mvp.ui.dialog.CommentDialog;
import com.fengwo.module_chat.mvp.ui.dialog.CommentInputDialog;
import com.fengwo.module_chat.mvp.ui.event.CommentRefreshEvent;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.event.AttentionRefreshEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

@Deprecated
public class ChatCardFragment extends BaseMvpFragment<IChatCardChildView, ChatCardChildPresenter>
        implements IChatCardChildView, ITXVodPlayListener {

    @BindView(R2.id.view_video)
    View videoView;
    @BindView(R2.id.view_picture)
    View pictureView;
    @BindView(R2.id.rvPicture)
    RecyclerView rvPicture;
    @BindView(R2.id.rvPictureThumb)
    RecyclerView rvPictureThumb;
    @BindView(R2.id.tv_card_content)
    TextView tvCardContent;
    @BindView(R2.id.tv_card_name)
    TextView tvNickName;
    @BindView(R2.id.tv_chat_comment)
    TextView tvCommentNum;
    @BindView(R2.id.tv_chat_like)
    TextView tvLikeNum;
    @BindView(R2.id.iv_header)
    ImageView ivHeader;
    @BindView(R2.id.iv_add_attention)
    ImageView ivAddAttention;
    @BindView(R2.id.view_like)
    ImageView btnLike;
    @BindView(R2.id.cloud_video_view)
    TXCloudVideoView superVideoView;
    @Autowired
    UserProviderService service;
    private BaseQuickAdapter<ChatCardImgBean, BaseViewHolder> pictureAdapter;
    private ChatCardThumbAdapter thumbAdapter;
    private ChatCardBean card;
    private CommentDialog commentDialog;
    private TXVodPlayer mVodPlayer;
    private float lastPlayTime = -1;

    @Override
    protected ChatCardChildPresenter initPresenter() {
        return new ChatCardChildPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.chat_fragment_card;
    }

    @SuppressLint("CheckResult")
    @Override
    public void initUI(Bundle savedInstanceState) {
        initPictureView();
        refreshUI(card);

        RxBus.get().toObservable(CommentRefreshEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<CommentRefreshEvent>() {
                    @Override
                    public void accept(CommentRefreshEvent commentRefreshEvent) throws Exception {
                        if (TextUtils.equals(card.id, commentRefreshEvent.cardId)) {
                            card.comments = String.valueOf(Integer.parseInt(card.comments) + 1);
                            tvCommentNum.setText(card.comments);
                        }
                    }
                });
        RxBus.get().toObservable(AttentionRefreshEvent.class)
                .compose(bindToLifecycle())
                .subscribe(event -> {
                            L.e("========", "event.isAttention " + event.isAttention);
                            ivAddAttention.setSelected(event.isAttention == 1);
                            ivAddAttention.setVisibility(event.isAttention == 1 ? View.GONE : View.VISIBLE);
                        }
                );
    }

    @OnClick({R2.id.iv_chat_comment, R2.id.view_like, R2.id.iv_add_attention, R2.id.iv_header})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.view_like) {
            p.cardLike(card.id);
        } else if (id == R.id.iv_add_attention) {
            if (TextUtils.equals(card.isAttention, "1")) {
                p.unattentionUser(card.userId);
            } else {
                p.attentionUser(card.userId);
            }
        } else if (id == R.id.iv_chat_comment) {
            if (!isFastClick()) {
                commentDialog = new CommentDialog(getContext(), card.id);
                commentDialog.setOnCommentListener((parentIndex, position, type, commentModel, cardId, parentId) -> {
                    CommentInputDialog commentEditDialog = new CommentInputDialog();
                    commentEditDialog.setData(parentIndex, position, type, commentModel, cardId, parentId);
                    commentEditDialog.show(getChildFragmentManager(), "commentEdit");
                });
                commentDialog.showPopupWindow();
            }
        } else if (id == R.id.iv_header) {
            ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, Integer.parseInt(card.userId));
        }
    }


    @Override
    public void cardLikeSuccess(String id) {
        if (card == null) return;
        if (!TextUtils.equals(card.id, id)) return;
        if (TextUtils.equals(card.isLike, "1")) {
            card.isLike = "0";
            int i = Integer.parseInt(card.likes);
            i -= 1;
            card.likes = String.valueOf(i);
            refreshLikeStatus();
        } else {
            card.isLike = "1";
            int i = Integer.parseInt(card.likes);
            i += 1;
            card.likes = String.valueOf(i);
            refreshLikeStatus();
        }
    }

    CountBackUtils countBackUtils;

    @Override
    public void attentionSuccess(String id) {
        if (card == null) return;
        if (!TextUtils.equals(card.userId, id)) return;
        card.isAttention = "1";
        ivAddAttention.setSelected(true);
        service.getUserInfo().isAttention = 1;
        countBackUtils = new CountBackUtils();
        countBackUtils.countBack(1, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {

            }

            @Override
            public void finish() {
                ivAddAttention.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void removeAttentionSuccess(String id) {
        if (card == null) return;
        if (!TextUtils.equals(card.userId, id)) return;
        card.isAttention = "0";
        ivAddAttention.setSelected(false);
        service.getUserInfo().isAttention = 0;
    }

    private void initPictureView() {
        LinearLayoutManager pictureLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvPicture.setLayoutManager(pictureLayoutManager);
        pictureAdapter = new BaseQuickAdapter<ChatCardImgBean, BaseViewHolder>(R.layout.item_chat) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ChatCardImgBean item) {
                ImageView imageView = helper.getView(R.id.root);
                ImageLoader.loadImg(imageView, item.imageUrl);
            }
        };
        rvPicture.setAdapter(pictureAdapter);
        rvPicture.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = pictureLayoutManager.findFirstVisibleItemPosition();
                    if (position >= 0) thumbAdapter.setSelectedPosition(position);
                }
            }
        });
        new PagerSnapHelper().attachToRecyclerView(rvPicture);
        thumbAdapter = new ChatCardThumbAdapter();
        thumbAdapter.setOnItemClickListener((adapter, view, position) -> {
            thumbAdapter.setSelectedPosition(position);
            rvPicture.smoothScrollToPosition(position);
        });
        rvPictureThumb.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvPictureThumb.setAdapter(thumbAdapter);
    }

    private static final String TAG = "ChatCardFragment";

    public void refreshUI(ChatCardBean card) {
        this.card = card;
        if (card == null) return;
        if (pictureView == null) return;
        ImageLoader.loadImg(ivHeader, card.headImg);

        if (card.isAttention.equals("1")) {
            ivAddAttention.setVisibility(View.GONE);
        } else {
            ivAddAttention.setSelected(TextUtils.equals(card.isAttention, "1"));
            ivAddAttention.setVisibility(card.userId.equals(String.valueOf(service.getUserInfo().id)) ? View.GONE : View.VISIBLE);
        }
        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, Integer.parseInt(card.userId));
            }
        });

        tvCardContent.setText(card.excerpt);

        tvNickName.setVisibility(TextUtils.isEmpty(card.nickname) ? View.GONE : View.VISIBLE);
        tvNickName.setText("@" + card.nickname);
        tvCommentNum.setText(card.comments);
        refreshLikeStatus();
        if (TextUtils.equals(card.type, "1")) { // 图片
            pictureView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            pictureAdapter.setNewData(card.imgContent);
            if (card.imgContent != null && card.imgContent.size() > 1) {
                rvPictureThumb.setVisibility(View.VISIBLE);
                thumbAdapter.setNewData(card.imgContent);
            } else rvPictureThumb.setVisibility(View.GONE);
        } else if (TextUtils.equals(card.type, "2") && card.imgContent != null && card.imgContent.size() > 0) { // 视频
            pictureView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            initVodPlayer(card);
        }
    }

    private void initVodPlayer(ChatCardBean bean) {
        mVodPlayer = new TXVodPlayer(getContext());
        TXVodPlayConfig mVodPlayConfig = new TXVodPlayConfig();
        mVodPlayConfig.setCacheFolderPath(Environment.getExternalStorageDirectory().getPath() + "/.nomedia");
        mVodPlayConfig.setMaxCacheItems(5);
        mVodPlayer.setConfig(mVodPlayConfig);
        mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mVodPlayer.setVodListener(this);
        mVodPlayer.enableHardwareDecode(true);
        mVodPlayer.setLoop(true);
        mVodPlayer.setPlayerView(superVideoView);
        mVodPlayer.setAutoPlay(true);
        //---
        mVodPlayer.startPlay(bean.imgContent.get(0).imageUrl);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mVodPlayer != null && card.imgContent != null && card.imgContent.size() > 0) {
            if (isVisibleToUser) {
                mVodPlayer.startPlay(card.imgContent.get(0).imageUrl);
            } else {
                mVodPlayer.stopPlay(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVodPlayer != null && card != null && lastPlayTime > 0) {
            mVodPlayer.setStartTime(lastPlayTime);
            mVodPlayer.startPlay(card.imgContent.get(0).imageUrl);
            lastPlayTime = -1;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVodPlayer != null && mVodPlayer.isPlaying()) {
            lastPlayTime = mVodPlayer.getCurrentPlaybackTime();
            mVodPlayer.stopPlay(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mVodPlayer != null) mVodPlayer.stopPlay(true);
        if (superVideoView != null) superVideoView.removeVideoView();
        if (null != countBackUtils && countBackUtils.isTiming()) {
            countBackUtils.destory();
        }
    }

    private void refreshLikeStatus() {
        btnLike.setSelected(TextUtils.equals(card.isLike, "1"));
        tvLikeNum.setSelected(TextUtils.equals(card.isLike, "1"));
        tvLikeNum.setText(card.likes);
    }

    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int i, Bundle bundle) {
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

    }

}