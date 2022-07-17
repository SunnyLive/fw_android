package com.fengwo.module_flirt.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_chat.utils.chat_new.FileUtils;
import com.fengwo.module_chat.utils.chat_new.VoicePlayHelper;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.bean.CheckAnchorStatus;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.pop.MessageGreetPopwindow;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_flirt.Interfaces.IFlirtCardDetailsView;
import com.fengwo.module_flirt.P.FlirtCardDetailsPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.adapter.FlirtCardDetailsAdapter;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.ShareInfoBean;
import com.fengwo.module_flirt.widget.AnchorListButtonView;
import com.fengwo.module_live_vedio.mvp.ui.df.ShareCodeDialog;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.fengwo.module_login.utils.UserManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

;
;

/**
 * 视频交友详情
 *
 * @Author BLCS
 * @Time 2020/3/27 16:45
 */
@Route(path = ArouterApi.FLIRT_CARD_DETAILS_ACTIVITY)
public class FlirtCardDetailsActivity extends BaseMvpActivity<IFlirtCardDetailsView, FlirtCardDetailsPresenter> implements IFlirtCardDetailsView {

    private static SHARE_MEDIA SHARE_TYPE = SHARE_MEDIA.WEIXIN;
    @Autowired
    UserProviderService userProviderService;
    @BindView(R2.id.rv_include)
    RecyclerView mRv;
    @BindView(R2.id.sr_refresh)
    SmartRefreshLayout mRefresh;
    @BindView(R2.id.cl_card_detail)
    ConstraintLayout cl_card_detail;
    private FlirtCardDetailsAdapter mAdapter;
    private ArrayList<CityHost> cityHosts = new ArrayList<>();
    private int pos;
    private long startTimes;
    private long endTimes;
    private String headImg;
    VoicePlayHelper voicePlayHelper;
    private int anchorId;
    private AnchorListButtonView anchorListButtonView;
    private int status;
    private String tableName;

    private boolean canScroll = true;
    private CheckAnchorStatus data;
    private ArrayList<CityHost> datas;
    private MessageGreetPopwindow greetPopwindow;
    private boolean is_shuaxin = true;


    //传入的筛选条件
    private String city;
    private String maxAge;
    private String minAge;
    private String sex;

    //页码，这个值可能从外部传入不同于1的值
    private int page = 1;
    private Disposable mDisposable;

    @Override
    public FlirtCardDetailsPresenter initPresenter() {
        return new FlirtCardDetailsPresenter();
    }

    /**
     * 从在线的TA进入
     *
     * @param context
     * @param data
     * @param pos
     * @param city
     * @param maxAge
     * @param minAge
     * @param sex
     * @param page
     */
    public static void start(Context context, ArrayList<CityHost> data, int pos, String city, String maxAge, String minAge, String sex, int page) {
        Intent intent = new Intent(context, FlirtCardDetailsActivity.class);
        intent.putExtra("CityHost", data);
        intent.putExtra("POS", pos);
        intent.putExtra("CITY", city);
        intent.putExtra("maxAge", maxAge);
        intent.putExtra("minAge", minAge);
        intent.putExtra("sex", sex);
        intent.putExtra("page", page);
        context.startActivity(intent);
    }

    /**
     * 从在线的TA进入
     *
     * @param context
     * @param city
     * @param maxAge
     * @param minAge
     * @param sex
     */
    public static void start(Context context, int anchorId, String city, String maxAge, String minAge, String sex) {
        Intent intent = new Intent(context, FlirtCardDetailsActivity.class);
        intent.putExtra("anchorId", anchorId);
        intent.putExtra("CITY", city);
        intent.putExtra("maxAge", maxAge);
        intent.putExtra("minAge", minAge);
        intent.putExtra("sex", sex);
        context.startActivity(intent);
    }


    /**
     * 适用于 列表传入 用于上下滑
     */
    public static void start(Context context, ArrayList<CityHost> data, int pos) {
        Intent intent = new Intent(context, FlirtCardDetailsActivity.class);
        intent.putExtra("CityHost", data);
        intent.putExtra("POS", pos);
        context.startActivity(intent);
    }

    /**
     * 由其他页面进入该界面调用  通过主播ID 获取该界面信息
     *
     * @param context
     * @param anchorId
     */
    public static void start(Context context, int anchorId) {
        Intent intent = new Intent(context, FlirtCardDetailsActivity.class);
        intent.putExtra("anchorId", anchorId);
        context.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*获取点单列表*/
        p.getMyOrder();
        //获取主播状态 点单完成之后 返回 刷新 显示状态
        p.checkAnchorStatusint(reFreshAnchorId, false);
    }

    private int reFreshAnchorId;

    @Override
    protected void initView() {
        //记录浏览时间
        startTimes = System.currentTimeMillis();
        //处理传过来的参数
        getBundle();
        //语音播放器处理
        handleVoice();
        initRv();
        initData();
        initEvent();
        //动态添加浮窗按钮
        anchorListButtonView = new AnchorListButtonView(this, getSupportFragmentManager());
        cl_card_detail.addView(anchorListButtonView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        anchorListButtonView.addClickViewListener(new AnchorListButtonView.ClickViewListener() {
            @Override
            public void clickView() {
                p.getMyOrder();
            }
        });
        SmartRefreshLayoutUtils.setTransparentBlackText(this, mRefresh);
    }

    private void initEvent() {


    }

    /**
     * 参数处理
     */
    private void getBundle() {
        //获取主播ID
        datas = (ArrayList<CityHost>) getIntent().getSerializableExtra("CityHost");
        if (datas != null) {
            cityHosts.clear();
            cityHosts.addAll(datas);
        }
        //接受来自其他页面的数据
        pos = getIntent().getIntExtra("POS", 0);
        pos = Math.max(pos, 0);
        city = getIntent().getStringExtra("CITY");
        anchorId = getIntent().getIntExtra("anchorId", 0);
        maxAge = getIntent().getStringExtra("maxAge");
        minAge = getIntent().getStringExtra("minAge");
        sex = getIntent().getStringExtra("sex");
        page = getIntent().getIntExtra("page", 1);
    }

    /**
     * 语音播放处理
     */
    private void handleVoice() {
        voicePlayHelper = new VoicePlayHelper(this, "");
        voicePlayHelper.setVoicePlayListener(new VoicePlayHelper.VoicePlayListener() {
            @Override
            public void onCompletion() {
                voicePlayHelper.clearPlayingStatus();
                mAdapter.stopVoicePlay(pos);
            }

            @Override
            public void onPlaying() {
                mAdapter.stopMediaPlay(pos);
                mAdapter.startVoicePlay(pos);
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 请求数据
     */
    private void initData() {
        mAdapter.setNewData(cityHosts);
        if (anchorId != 0) {//其他界面跳转该界面
            //获取主播信息
            p.getAnchorInfo(0, anchorId);
            //获取主播状态
//            p.checkAnchorStatus(anchorId, false);
            reFreshAnchorId = anchorId;
            /*非列表进入 不能上下刷新 与 滑动*/
            mRefresh.setEnableRefresh(false);
            mRefresh.setEnableLoadMore(false);
            canScroll = false;
        } else {
            //在线列表 跳转该界面
            preloadingAnchorDetail(true, false);
//            //获取主播状态
//            p.checkAnchorStatus(datas.get(pos).getAnchorId(), false);
            reFreshAnchorId = datas.get(pos).getAnchorId();
            canScroll = true;
            mRefresh.setEnableRefresh(true);
            mRefresh.setEnableLoadMore(false);
        }
        L.e("============mAdapter " + mAdapter.getData().size());
        mRv.scrollToPosition(pos);
    }

    /**
     * 详情列表UI
     */
    private void initRv() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRv.setLayoutManager(linearLayoutManager);
        mAdapter = new FlirtCardDetailsAdapter(voicePlayHelper);
        mRv.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRv);
        new PagerSnapHelper().attachToRecyclerView(mRv);
        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (RecyclerView.SCROLL_STATE_IDLE == newState) {//已经停止
                        int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                        int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        if (firstVisibleItem != pos && lastVisibleItem != pos) {
                            recordTime(pos);
                            //滑动页面 停止播放语音与视频
                            mAdapter.stopMediaPlay(pos);
                            mAdapter.stopVoicePlay(pos);

                            boolean isNext = firstVisibleItem > pos;
                            boolean isInit = Math.abs(firstVisibleItem - pos) > 1;
                            pos = firstVisibleItem;
                            reFreshAnchorId = mAdapter.getData().get(pos).getAnchorId();
                            //预加载
                            preloadingAnchorDetail(isInit, isNext);
                            voicePlayHelper.stopVoice();
                            //获取主播状态
                            p.checkAnchorStatus(reFreshAnchorId, false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getOnlineList();
            }
        });

        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (canScroll) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    int itemTotalCount = layoutManager.getItemCount();
                    View lastChildView = layoutManager.getChildAt(layoutManager.getChildCount() - 1);
                    int lastChildBottom = lastChildView.getBottom();
                    int recyclerBottom = recyclerView.getBottom();
                    if (lastVisibleItem == itemTotalCount - 1 && lastChildBottom == recyclerBottom) {
                        if (is_shuaxin) {
                            is_shuaxin = false;
                            getOnlineList();
                        }
                    }
                }
            }
        });

//        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                p.getVideoDating(city, ++page, status, tableName);
//                mRefresh.finishLoadMore();
//            }
//        });
        /*页面 点击事件处理*/
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (isFastClick()) return;
                int id = view.getId();
                CityHost cityHost = (CityHost) baseQuickAdapter.getData().get(i);
                anchorId = cityHost.getAnchorId();
                headImg = cityHost.getHeadImg();
                if (id == R.id.iv_card_close) {
                    finish();
                } else if (id == R.id.iv_card_setting) {//TODO 分享
                    ShareCodeDialog dialog = new ShareCodeDialog();
                    dialog.setOnItemClickListener(new ShareCodeDialog.OnItemClickListener() {
                        @Override
                        public void WeiXinShare() {

                            SHARE_TYPE = SHARE_MEDIA.WEIXIN;
                            p.getShareInfo(anchorId);
                        }

                        @Override
                        public void WeiXinCircleShare() {
                            SHARE_TYPE = SHARE_MEDIA.WEIXIN_CIRCLE;
                            p.getShareInfo(anchorId);
                        }
                    });
                    getSupportFragmentManager().beginTransaction().add(dialog, "share").commitAllowingStateLoss();
                } else if (id == R.id.ll_card_flirt) {/* 视频交友 */
                    if (UserManager.getInstance().getUser().getId() == anchorId) {
                        toastTip("不能操作自己");
                        return;
                    }
//                    if( !userProviderService.getUserInfo().isReanName()){
//                        AuthenticationDialog authenticationDialog = new AuthenticationDialog(FlirtCardDetailsActivity.this,new AuthenticationDialog.onPositiveListener(){
//
//                            @Override
//                            public void onPositive() {
//                                ArouteUtils.toRealIdCard();
//                            }
//                        });
//                        authenticationDialog.show();
//                        return;
//                    }
                    if (data == null) return;
                    //获取主播状态 点单完成之后 返回 刷新 显示状态
                    p.checkAnchorStatus(reFreshAnchorId, true);

                } else if (id == R.id.iv_card_header) {/*头像*/
                    if (cityHosts.size()==0||pos > cityHosts.size() - 1) {
                        toastTip("加载错误请返回从新尝试");
                        return;
                    }
                    L.e("=====直播状态 " + cityHosts.get(pos).getLiveStatus());
                    if (cityHosts.get(pos).getLiveStatus() == 2) {//如果处于直播状态 这跳转直播间
                        ArrayList<ZhuboDto> list = new ArrayList<>();
                        ZhuboDto zhuboDto = new ZhuboDto();
                        zhuboDto.channelId = cityHosts.get(pos).getAnchorId();
                        zhuboDto.headImg = cityHosts.get(pos).getHeadImg();
                        list.add(zhuboDto);
                        if (FloatingView.getInstance().isShow()) {
                            showExitDialog(list);
                        } else {
                            IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                            //       LivingRoomActivity.start(FlirtCardDetailsActivity.this, list, 0, true);
                        }
                    } else {//跳转详情页
                        MineDetailActivity.startActivityWithUserId(FlirtCardDetailsActivity.this, cityHosts.get(pos).getAnchorId());
                    }
                } else if (id == R.id.tv_card_player_status) {
                    //TODO 直播状态
                } else if (id == R.id.ll_card_voice) {//播放语音
                    CityHost bean = mAdapter.getItem(i);
                    assert bean != null;
                    if ((bean.getAnchorId() + "").equals(voicePlayHelper.getFromId())) {
                        voicePlayHelper.stopVoice();
                        mAdapter.stopVoicePlay(i);
                    } else {
                        if (null == bean.getAudioPath() || bean.getAudioPath().equals("null")) {
                            voicePlayHelper.setFromId(view.getTag() + "");
                            voicePlayHelper.loadDownVoiceAndPlay(view.getTag() + "", FileUtils.getVoiceDir("fw"));
                        } else {
                            voicePlayHelper.setFromId(bean.getAnchorId() + "");
                            voicePlayHelper.loadDownVoiceAndPlay(bean.getAudioPath(), FileUtils.getVoiceDir("fw"));
                        }


                    }
                }
            }
        });
    }

    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog(ArrayList<ZhuboDto> list) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {

                            IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);



                   //     LivingRoomActivity.start(FlirtCardDetailsActivity.this, map, 0, true);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .setGear(floatingView.getGear())
                .setNickname(floatingView.getNickname())
                .setExpireTime(floatingView.getExpireTime())
                .setHeadImg(floatingView.getHeadImg())
                .setRoomId(floatingView.getRoomId())
                .setTip("退出达人房间，印象值将归零\n是否要退出")
                .show(getSupportFragmentManager(), "");
    }

    /**
     * 预加载主播详情
     *
     * @param isInit 是否为初始加载，初始加载时需要上下各预加载一条，不为初始加载时只需要延对应方向加载一条
     * @param isNext 是否为预加载下一条，如果不是则加载上一条
     */
    private void preloadingAnchorDetail(boolean isInit, boolean isNext) {
        if (isInit) {
            getAnchorDetail(pos);
            getAnchorDetail(pos - 1);
            getAnchorDetail(pos + 1);
        } else {
            if (isNext) {
                getAnchorDetail(pos + 1);
            } else {
                getAnchorDetail(pos - 1);
            }
        }
    }

    /**
     * 加载指定position的主播详情
     *
     * @param position
     */
    private void getAnchorDetail(int position) {
        if (position >= 0 && position < mAdapter.getData().size()) {
            p.getAnchorInfo(position, mAdapter.getData().get(position).getAnchorId());
        }
    }

    /**
     * 分页加载数据
     */
    private void getOnlineList() {
        p.getOnlineList(page, city, maxAge, minAge, sex);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.stopMediaPlay(pos);
        mAdapter.stopVoicePlay(pos);
        recordTime(pos);
    }

    /**
     * 添加浏览记录
     *
     * @param pos
     */
    private void recordTime(int pos) {
        if (p != null && mAdapter.getData().size() > 0) {
            if (mAdapter.getData().size() > pos) {
            CityHost cityHost = mAdapter.getData().get(pos);
            endTimes = System.currentTimeMillis();
            long sec = (endTimes - startTimes) / 1000;
            if (sec > 2 && cityHost.getAnchorId() != UserManager.getInstance().getUser().getId()) {
                p.addBrowingRecord(cityHost.getAnchorId(), sec);
            }
        }
        }
        startTimes = System.currentTimeMillis();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_flirt_card;
    }

    @Override
    public void onReceiveFlirtData(boolean isRefresh, List<CityHost> onlineFlirtData) {
        /*刷新 与 上拉 数据更新*/
        if (page == 1) {
            mAdapter.getData().clear();
            //第一条数据可能是广告
            if (null != onlineFlirtData.get(0).getAdvertList() && onlineFlirtData.get(0).getAdvertList().size() != 0) {
                onlineFlirtData.remove(0);
            }
            mAdapter.setNewData(onlineFlirtData);
            pos = 0;
            reFreshAnchorId = onlineFlirtData.get(0).getAnchorId();
            //请求第一条的详情
            p.getAnchorInfo(pos, reFreshAnchorId);

            mRefresh.finishRefresh();
        } else {
            if (onlineFlirtData.size() == 0) {
                toastTip("已滑动到底部");
                return;
            }
            mAdapter.addData(onlineFlirtData);
            preloadingAnchorDetail(false, true);
        }
        is_shuaxin = true;
        page++;
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            voicePlayHelper.stopVoice();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            voicePlayHelper.release();
            mAdapter.stopMediaPlay(pos);
        } catch (Exception e) {

        }
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }


    @Override
    public void checkAnchorStatus(CheckAnchorStatus data, boolean isClick) {
        //检测主播是否处于开播状态
        this.data = data;
        if (isClick) {
            if (data.getBstatus() == 0) {//不在线 打招呼
                if (UserManager.getInstance().getUser().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NO
                        || UserManager.getInstance().getUser().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NULL) {
                    CustomerDialog mcd = new CustomerDialog.Builder(this)
                            .setTitle("温馨提示")
                            .setMsg("您未进行实名认证，\n认证后可使用打招呼")
                            .setPositiveButton("去认证", () -> {
                                ArouteUtils.toRealIdCard(UserManager.getInstance().getUser().myIdCardWithdraw);
                            }).create();
                    mcd.show();
                    return;
                } else if (UserManager.getInstance().getUser().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_ING) {
                    ToastUtils.showShort(this, "实名认证中,请您耐心等待", Gravity.CENTER);
                    return;
                }
                if (greetPopwindow == null) {
                    greetPopwindow = new MessageGreetPopwindow(this);
                    greetPopwindow.setOnSelectListener(new MessageGreetPopwindow.OnSelectListener() {
                        @Override
                        public void onSelect(String content) {
                            p.greetDiscuss(anchorId + "", content);
                        }

                        @Override
                        public void onRequest() {
                            observeMessageTagList();
                        }
                    });
                } else {
                    if (greetPopwindow.isShowing())
                        return;
                }
                greetPopwindow.Request();
                greetPopwindow.showPopupWindow();
            } else {
                if (data.isHasAppointment()) {//判断是否已经预约过
                    if (null != mAdapter.getItem(pos).getRes() && mAdapter.getItem(pos).getRes().size() > 0) {
                        if (mAdapter.getItem(pos).getRes().get(0).getRPath().endsWith("mp4")) {
                            ChatRoomActivity.startWait(FlirtCardDetailsActivity.this, anchorId, "-1", 1);
                        } else {
                            ChatRoomActivity.startWait(FlirtCardDetailsActivity.this, anchorId, mAdapter.getItem(pos).getRes().get(0).getRPath(), 1);
                        }
                    } else {
                        ChatRoomActivity.startWait(FlirtCardDetailsActivity.this, anchorId, "-1", 0);
                    }

                } else {
                    if (null != mAdapter.getItem(pos).getRes() && mAdapter.getItem(pos).getRes().size() > 0) {
                        if (mAdapter.getItem(pos).getRes().get(0).getRPath().endsWith("mp4")) {
                            ChatRoomActivity.startWait(FlirtCardDetailsActivity.this, anchorId, "-1", 1);//sb需求让改的 没图片显示占位图
                        } else {
                            ChatRoomActivity.startWait(FlirtCardDetailsActivity.this, anchorId, mAdapter.getItem(pos).getRes().get(0).getRPath(), 1);
                        }

                    } else {
                        ChatRoomActivity.startWait(FlirtCardDetailsActivity.this, anchorId, "-1", 1);
                    }
                }
                // 如果有小窗显示，关闭小窗
                FloatingView.getInstance().hide();
            }
        } else {
            mAdapter.getItem(pos).setBstatus(data.getBstatus());
            KLog.e("tag", "position=" + pos);
            mAdapter.setStatus(data.getBstatus(), pos);
        }
    }

    private int apage = 0, pages = 1;

    private void observeMessageTagList() {
        if (apage < pages)
            apage++;
        else {
            apage = 1;
        }

        p.greetTipsList(apage);
    }

    @Override
    public void getAnchorInfoSuccess(int position, CityHost cityHost) {
        L.e("=========获取主播信息" + cityHost.getNickname());
        if (canScroll) {
//            if (data != null) {
            mAdapter.getData().set(position, cityHost);
            mAdapter.notifyItemChanged(position);
//            mAdapter.replaceData(pos, cityHost);
//            }
        } else {
            cityHosts.clear();
            cityHosts.add(cityHost);
            mAdapter.setNewData(cityHosts);
        }
    }

    @Override
    public void getShareInfoSuccess(ShareInfoBean data) {
        UMWeb web = new UMWeb(data.getShareUrl());
        web.setTitle(data.getShareTitle());
        web.setDescription(data.getShareContent());
        if (!TextUtils.isEmpty(data.getShareImg()))
            web.setThumb(new UMImage(this, data.getShareImg()));
        else web.setThumb(new UMImage(this, headImg));
        L.e("=====]SHARE_TYPE " + SHARE_TYPE);
        new ShareAction(this)
                .setPlatform(SHARE_TYPE)//传入平台
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        toastTip("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        toastTip("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })//回调监听器
                .share();
    }


    @Override
    public void setMyOrderList(List<MyOrderDto> data) {/*点单列表*/
        anchorListButtonView.setVisibility(data != null && data.size() > 0 ? View.VISIBLE : View.GONE);
        anchorListButtonView.setHeader(data != null && data.size() > 0 ? data.get(0).getHeadImg() : null);
        anchorListButtonView.setOrderList(data, "0");
    }

    @Override
    public void greet(String content) {
        //todo 打招呼成功
        if (greetPopwindow.isShowing())
            greetPopwindow.dismiss();
//        if (userInfo.isAttention == 0) {
//            AttentionUtils.addAttention(userInfo.id, new LoadingObserver<HttpResult>() {
//                @Override
//                public void _onNext(HttpResult data) {
//                    if (data.isSuccess()) {
//                        userInfo.isAttention = 1;
//                        attentionUI();
//                        refreshUserInfo();
//                        RxBus.get().post(new AttentionRefreshEvent(userInfo.id, 1));
//                    }
//                }
//
//                @Override
//                public void _onError(String msg) {
//                    toastTip(msg);
//                }
//            });
//        }
//        UserInfo mine = UserManager.getInstance().getUser();
//        if (userInfo == null || mine == null) return;
//        RxBus.get().post(new GreetMessageEvent(mine.id + "", userInfo.id + "", userInfo.nickname, userInfo.headImg, content, mine.headImg, System.currentTimeMillis() / 1000));
        UserInfo mine = UserManager.getInstance().getUser();
        CityHost cityHost = cityHosts.get(pos);
        ArouteUtils.toGreetChatSingleMsgActivity(mine.getId() + "", cityHost.getAnchorId() + "", cityHost.getNickname(), content, cityHost.getHeadImg());
    }

    @Override
    public void setGreetTips(List<String> dataList, int page) {
        pages = page;
        greetPopwindow.setAdapter(dataList);
    }

    @Override
    public void checkAnchorStatusint(CheckAnchorStatus data, boolean isClick) {
        mAdapter.setStatus(data.getBstatus(), pos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
