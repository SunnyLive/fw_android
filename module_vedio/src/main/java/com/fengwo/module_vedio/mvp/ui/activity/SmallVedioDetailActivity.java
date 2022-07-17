package com.fengwo.module_vedio.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.IsAttentionDto;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.iservice.AttentionService;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.eventbus.SmallVedioCommentRefreshEvent;
import com.fengwo.module_vedio.eventbus.SmallVedioSeekBarTrackingTouchEvent;
import com.fengwo.module_vedio.mvp.dto.VideoSearchDto;
import com.fengwo.module_vedio.mvp.presenter.SmallVedioDetailPresenter;
import com.fengwo.module_vedio.mvp.ui.adapter.SmallVedioDetailAdapter;
import com.fengwo.module_vedio.mvp.ui.dialog.CommentInputPopupWindow;
import com.fengwo.module_vedio.mvp.ui.dialog.CommentPopupWindow;
import com.fengwo.module_vedio.mvp.ui.dialog.SharePopupWindow;
import com.fengwo.module_vedio.mvp.ui.fragment.shortvideo.ShortVideoCommentInputDialog;
import com.fengwo.module_vedio.mvp.ui.iview.ISmallVedioDetailView;
import com.fengwo.module_vedio.mvp.ui.pop.DeleteVideoPop;
import com.tencent.liteav.demo.play.SuperPlayerGlobalConfig;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

@Route(path = ArouterApi.SMALLVEDIODETAILACTIVITY)
public class SmallVedioDetailActivity extends BaseMvpActivity<ISmallVedioDetailView, SmallVedioDetailPresenter> implements BaseQuickAdapter.OnItemChildClickListener, ISmallVedioDetailView, ITXVodPlayListener {
    @BindView(R2.id.rv_smallvedil_detail)
    RecyclerView rvSmallvedilDetail;
    @Autowired
    AttentionService attentionService;
    private SmallVedioDetailAdapter smallVedioDetailAdapter;
    private CommentPopupWindow commentPopupWindow;
    private CommentInputPopupWindow commentInputPopupWindow;
    private SharePopupWindow sharePopupWindow;
    private int preIndex = 0;
    CompositeDisposable compositeDisposable;
    private ArrayList<VideoHomeShortModel> list;
    private boolean isPause = false;

    @Override
    public SmallVedioDetailPresenter initPresenter() {
        return new SmallVedioDetailPresenter();
    }

    private CountBackUtils attentionCb;
    private int position;
    int lastVisibleItem;

    int firstVisibleItem;

    private int nowCommentPosition;

    private int page;

    private boolean doSomething = false;//防止滑动时一直做操作
    private int menuId;
    private String searchKey;
    private RetrofitUtils retrofitUtils;

    @Autowired
    UserProviderService userProviderService;

    public static void startActivity(Context c, int postioin, ArrayList<VideoHomeShortModel> l) {
        Bundle b = new Bundle();
        b.putSerializable("list", l);
        b.putInt("position", postioin);
        Intent i = new Intent(c, SmallVedioDetailActivity.class);
        i.putExtras(b);
        c.startActivity(i);
    }

    public static void startActivity(Context c, int postioin, ArrayList<VideoHomeShortModel> l, int menuId, String searchKey, int page) {
        Bundle b = new Bundle();
        b.putSerializable("list", l);
        b.putInt("position", postioin);
        b.putInt("menuId", menuId);
        b.putString("search", searchKey);
        b.putInt("page", page);
        Intent i = new Intent(c, SmallVedioDetailActivity.class);
        i.putExtras(b);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        retrofitUtils = new RetrofitUtils();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {// 隐藏标题栏
        setTitleBackground(getResources().getColor(android.R.color.transparent));
        new ToolBarBuilder().showBack(true).build();
        Bundle b = getIntent().getExtras();
        compositeDisposable = new CompositeDisposable();
        list = (ArrayList<VideoHomeShortModel>) b.getSerializable("list");
        L.e("initView: ", "===" + this.list.size());
        preIndex = b.getInt("position", 0);
        menuId = b.getInt("menuId", -1);
        searchKey = b.getString("search");
        page = b.getInt("page");
        attentionCb = new CountBackUtils();
        smallVedioDetailAdapter = new SmallVedioDetailAdapter(this.list, preIndex, userProviderService.getUserInfo().id);
        smallVedioDetailAdapter.bindToRecyclerView(rvSmallvedilDetail);
        rvSmallvedilDetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        smallVedioDetailAdapter.setOnItemChildClickListener(this);
        rvSmallvedilDetail.setAdapter(smallVedioDetailAdapter);
        rvSmallvedilDetail.scrollToPosition(preIndex);
        rvSmallvedilDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    if (firstVisibleItem != preIndex && lastVisibleItem != preIndex) {
                        smallVedioDetailAdapter.showConver(firstVisibleItem);
                        smallVedioDetailAdapter.isShowProgress(firstVisibleItem, true);
                        stopPlay();
                        startPlay(list.get(firstVisibleItem).url, firstVisibleItem);
//                        GSYVideoManager.releaseAllVideos();
//                        View v2 = linearLayoutManager.findViewByPosition(firstVisibleItem);
//                        EmptyGsyVideoPlayer player = v2.findViewById(R.id.detail_player);
//                        player.startPlayLogic();
                        preIndex = firstVisibleItem;
                        doSomething = false;
                        getAttention();
                        if (firstVisibleItem == smallVedioDetailAdapter.getItemCount() - 1) {
                            loadNextPage();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (firstVisibleItem != preIndex) {
                    if (!doSomething) {
                        View v2 = linearLayoutManager.findViewByPosition(firstVisibleItem);
                        v2.findViewById(R.id.iv_fengmian).setVisibility(View.VISIBLE);
                        doSomething = true;
                    }
                }
            }
        });
        new PagerSnapHelper().attachToRecyclerView(rvSmallvedilDetail);
        commentInputPopupWindow = new CommentInputPopupWindow(this);
        sharePopupWindow = new SharePopupWindow(this);

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (SmallVedioDetailActivity.this.list == null) return;
                startPlay(list.get(preIndex).url, preIndex);
            }
        });
        initPlay();
        getAttention();
        compositeDisposable.add(RxBus.get().toObservable(SmallVedioCommentRefreshEvent.class).subscribe(new Consumer<SmallVedioCommentRefreshEvent>() {
            @Override
            public void accept(SmallVedioCommentRefreshEvent smallVedioCommentRefreshEvent) throws Exception {
                SmallVedioDetailActivity.this.list.get(preIndex).comments = smallVedioCommentRefreshEvent.totalComments;
                smallVedioDetailAdapter.setcommentNum(preIndex, smallVedioCommentRefreshEvent.totalComments);
            }
        }));
        RxBus.get().toObservable(SmallVedioSeekBarTrackingTouchEvent.class).subscribe(new Consumer<SmallVedioSeekBarTrackingTouchEvent>() {
            @Override
            public void accept(SmallVedioSeekBarTrackingTouchEvent smallVedioSeekBarTrackingTouchEvent) throws Exception {
                L.e("xxxxxxxxxseekto", smallVedioSeekBarTrackingTouchEvent.nowPro + "");
                mVodPlayer.seek(1f * smallVedioSeekBarTrackingTouchEvent.nowPro / 100 * duration);
            }
        });
    }

    private void loadNextPage() {
        int p = page + 1;
        if (menuId >= 0) {
            if (menuId == 0) {
                netManager.add(retrofitUtils.createApi(VedioApiService.class).getVideoHomeList(0, p + "," + PAGE_SIZE)
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<BaseListDto<VideoHomeShortModel>>() {
                            @Override
                            public void _onNext(BaseListDto<VideoHomeShortModel> data) {
                                smallVedioDetailAdapter.addData(data.records);
                                page = p;
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        }));
            } else {
                netManager.add(retrofitUtils.createApi(VedioApiService.class).getShortVideoList(menuId, 0, p + "," + PAGE_SIZE)
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<BaseListDto<VideoHomeShortModel>>() {
                            @Override
                            public void _onNext(BaseListDto<VideoHomeShortModel> data) {
                                smallVedioDetailAdapter.addData(data.records);
                                page = p;
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        }));
            }
        } else if (!TextUtils.isEmpty(searchKey)) {
            retrofitUtils.createApi(VedioApiService.class).getVideoSearch(searchKey, p + "," + PAGE_SIZE)
                    .compose(io_main())
                    .compose(handleResult())
                    .map(new Function<VideoSearchDto, List<VideoHomeShortModel>>() {
                        @Override
                        public List<VideoHomeShortModel> apply(VideoSearchDto videoSearchDto) throws Exception {
                            return videoSearchDto.getVideoInfoArr().getRecords();
                        }
                    })
                    .subscribe(new LoadingObserver<List<VideoHomeShortModel>>() {
                        @Override
                        public void _onNext(List<VideoHomeShortModel> data) {
                            smallVedioDetailAdapter.addData(data);
                            page = p;
                        }

                        @Override
                        public void _onError(String msg) {
                        }
                    });
        }
    }

    private void getAttention() {
        compositeDisposable.add(attentionService.isAttention(list.get(preIndex).userId, new LoadingObserver<HttpResult<IsAttentionDto>>() {
            @Override
            public void _onNext(HttpResult<IsAttentionDto> data) {
                if (data.isSuccess())
                    smallVedioDetailAdapter.setAttention(preIndex, data.data.isAttention);
            }

            @Override
            public void _onError(String msg) {

            }
        }));
    }


    // 点播播放器
    private TXVodPlayer mVodPlayer;
    private TXVodPlayConfig mVodPlayConfig;

    private void initPlay() {
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

    private void startPlay(String url, int position) {
        TXCloudVideoView videoView = smallVedioDetailAdapter.getVideoView(position);
        if (videoView == null) return;
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVodPlayer.isPlaying()) {
                    isPause = true;
                    mVodPlayer.pause();
                    smallVedioDetailAdapter.isShowPlayerIcon(preIndex, true);
                } else {
                    isPause = false;
                    mVodPlayer.resume();
                    smallVedioDetailAdapter.isShowPlayerIcon(preIndex, false);
                }
            }
        });
        videoView.updateVideoViewSize(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
        mVodPlayer.setPlayerView(videoView);
        mVodPlayer.setAutoPlay(true);
        mVodPlayer.setVodListener(this);
        mVodPlayer.startPlay(url);
        if (p != null) p.addPlayNum(smallVedioDetailAdapter.getData().get(preIndex).id);
    }


    private void stopPlay() {
        if (mVodPlayer != null) {
            mVodPlayer.setVodListener(null);
            mVodPlayer.stopPlay(false);
            TXCloudVideoView videoView = smallVedioDetailAdapter.getVideoView(position);
            if (null != videoView)
                videoView.onDestroy();

        }
    }

    private void pausePlay() {
        if (mVodPlayer.isPlaying()) {
            mVodPlayer.pause();
        }
    }

    private void resumePlay() {
        if (null != mVodPlayer) {
            mVodPlayer.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPause)
            resumePlay();
        getAttention();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
        if (null != attentionCb && attentionCb.isTiming()) {
            attentionCb.destory();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.vedio_activity_small_detail;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        int id = view.getId();
        if (isFastClick())
            return;
        if (id == R.id.btn_header) {
            int uid = list.get(preIndex).userId;
            ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, uid);
//            startActivity(SmallAuthorDetailActivity.class);
        } else if (id == R.id.btn_comment) {
            commentPopupWindow = new CommentPopupWindow(this, String.valueOf(list.get(preIndex).id));
            commentPopupWindow.setOnCommentListener((parentIndex, secondPosition, type, commentModel, videoId, parentId) -> {
                ShortVideoCommentInputDialog commentEditDialog = new ShortVideoCommentInputDialog();
                commentEditDialog.setCommentVideoType(true);
                commentEditDialog.setData(parentIndex, secondPosition, type, commentModel, videoId, parentId);
                commentEditDialog.show(getSupportFragmentManager(), "commentEdit");
            });
            commentPopupWindow.showPopupWindow();
        } else if (id == R.id.btn_like) {
            p.setLike(list.get(position).id, position);
        } else if (id == R.id.btn_share) {
            if (null == sharePopupWindow) {
                sharePopupWindow = new SharePopupWindow(this);
            }
            sharePopupWindow.showPopupWindow();
        } else if (id == R.id.btn_attention) {
            if (list.get(preIndex).isAttention == 0) {
                attentionService.addAttention(list.get(preIndex).userId, new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            VideoHomeShortModel bean = list.get(position);
                            bean.isAttention = 1;
                            smallVedioDetailAdapter.getData().set(position, bean);
                            smallVedioDetailAdapter.setAttentionSelected(position);
                            attentionCb.countBack(1, new CountBackUtils.Callback() {
                                @Override
                                public void countBacking(long time) {

                                }

                                @Override
                                public void finish() {
                                    smallVedioDetailAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
            } else {
                attentionService.delAttention(list.get(preIndex).userId, new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            smallVedioDetailAdapter.setAttention(preIndex, 0);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
            }
        } else if (id == R.id.btn_more) {
            DeleteVideoPop deleteVideoPop = new DeleteVideoPop(this, new DeleteVideoPop.onDeleteListener() {
                @Override
                public void onDelete() {
                    Map map = new HashMap();
                    map.put("videoId", list.get(preIndex).id);
                    p.delSmallVideo(map);
                }
            });
            deleteVideoPop.showPopupWindow();
        }
    }

    @Override
    public void setLike(int position) {
        boolean like = !list.get(position).isLike;
        list.get(position).isLike = like;
        smallVedioDetailAdapter.changeLike(position, like);
    }

    int duration;

    @Override
    public void setDelete(HttpResult httpResult) {
        toastTip(httpResult.description);
        if (httpResult.isSuccess()) {
            finish();
        }
    }

    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
        if (event == TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED) { //视频播放开始
            smallVedioDetailAdapter.isShowPlayerIcon(preIndex, false);
            smallVedioDetailAdapter.isShowProgress(preIndex, false);
        } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
            smallVedioDetailAdapter.hideConver(preIndex);
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            int progress = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
            duration = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION);
            smallVedioDetailAdapter.seekTo((int) ((1f * progress / duration) * 100), preIndex);
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
