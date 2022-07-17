package com.fengwo.module_flirt.UI.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.event.FindDetailChangedEvent;
import com.fengwo.module_comment.event.RefreshEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.ShareHelper;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_flirt.Interfaces.IFlirtTalentView;
import com.fengwo.module_flirt.P.FlirtTalentPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.DetailCardActivity;
import com.fengwo.module_flirt.UI.activity.FindDetailActivity;
import com.fengwo.module_flirt.UI.activity.FlirtCardDetailsActivity;
import com.fengwo.module_flirt.adapter.FindListAdapter;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.CoverDto;
import com.fengwo.module_flirt.bean.FindDetailBean;
import com.fengwo.module_flirt.bean.FindListDto;
import com.fengwo.module_flirt.dialog.FindMuchPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.activity.LivingRoomActivity;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShareCommonPopwindow;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.fengwo.module_login.mvp.ui.activity.MineCardDetailActivity;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 在线 列表
 */
public class FindListFragment extends BaseMvpFragment<IFlirtTalentView, FlirtTalentPresenter> implements IFlirtTalentView,
        OnRefreshLoadMoreListener {

    private final int REQUEST_DETAIL = 100;

    @BindView(R2.id.sr_refresh)
    SmartRefreshLayout srRefresh;
    @BindView(R2.id.rv_include)
    RecyclerView rvInclude;
    private FindListAdapter findListAdapter;
    private FindListDto findListDto;
    private FindMuchPopwindow muchPopwindow;

    private int page = 1;
    private int toDetailPosition;

    @Autowired
    UserProviderService userProviderService;

    private Map<Integer, FindDetailBean> mapDetail = new HashMap<>();
    private String city = "全部";
    private String maxAge = "100";
    private String minAge = "0";
    private String sex = "0";

    @Override
    protected FlirtTalentPresenter initPresenter() {
        return new FlirtTalentPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.include_recyclerview;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        SmartRefreshLayoutUtils.setTransparentBlackText(getContext(), srRefresh);
        /*发现列表适配器*/
        findListAdapter = new FindListAdapter(getContext());
        rvInclude.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInclude.setAdapter(findListAdapter);
        /*发现列表点击*/
        findListAdapter.setOnItemClickListener((adapter, view, pos) -> {
            if (isFastClick()) return;
            toDetailPosition = pos;
            if (findListAdapter.getItem(pos).getUserId() == userProviderService.getUserInfo().getId()) {
                ARouter.getInstance().build(ArouterApi.MINE_DETAIL_CARD_ACTION)
                        .withInt(MineCardDetailActivity.CHAR_CARD_ID, findListAdapter.getItem(pos).getId())
                        .navigation();
            } else {
                findListDto = (FindListDto) adapter.getData().get(pos);
                ArrayList<FindListDto> data = (ArrayList<FindListDto>) adapter.getData();
//                int status = data.get(pos).getWenboLiveStatus();
//                if (status==0){
//                    ChatCardActivityNew.start(getContext(),null, String.valueOf(data.get(pos).getId()), String.valueOf(data.get(pos).getCircleId()),0,0,0,0);
//                }else{
//                    FlirtCardDetailsActivity.start(getContext(),data.get(pos).getId());
//                }
                Intent intent = new Intent(getActivity(), FindDetailActivity.class);
                intent.putExtra("id", data.get(pos).getId());
                startActivityForResult(intent, REQUEST_DETAIL);
            }
        });
        findListAdapter.setOnItemChildClickListener((baseQuickAdapter, view12, pos) -> {
            int id = view12.getId();
            findListDto = (FindListDto) baseQuickAdapter.getData().get(pos);
            if (id == R.id.iv_find_much) {//菜单
                if (muchPopwindow == null) {
                    muchPopwindow = new FindMuchPopwindow(getActivity());
                    muchPopwindow.addOnClickListener(new FindMuchPopwindow.OnMuchClickListener() {
                        @Override
                        public void attention(int uid, int pos) {
                            addtention(uid, pos);
                        }
                    });
                }
                L.e("====1 " + findListDto.getIsAttention());
                L.e("====2 " + findListDto.getUserId());
                muchPopwindow.setAttention(findListDto.getIsAttention(), findListDto.getUserId(), pos);
                muchPopwindow.showPopupWindow();
            } else if (id == R.id.iv_find_like || id == R.id.tv_find_like) {//点赞
                p.cardLike(findListDto.getId(), pos);
            } else if (id == R.id.tv_find_shares) {//分享
                List<CoverDto> cover = findListDto.getCover();
                showShareDialog(findListDto.getId(), cover != null && cover.size() > 0 ? cover.get(0).imageUrl : "", findListDto.getExcerpt());
            } else if (id == R.id.tv_find_comments) {//评论
                if (isFastClick()) return;
                ArrayList<FindListDto> data = (ArrayList<FindListDto>) findListAdapter.getData();
                Intent intent = new Intent(getActivity(), FindDetailActivity.class);
                intent.putExtra("id", data.get(pos).getId());
                startActivity(intent);
            } else if (id == R.id.civ_head || id == R.id.tv_flirt || id == R.id.tv_living) {
                if (findListDto.getWenboLiveStatus() == 1) {//下单页面
                    FlirtCardDetailsActivity.start(getContext(), findListDto.getUserId());
                } else if (findListDto.getLiveStatus() == 2) {//直播间
                    ArrayList<ZhuboDto> list = new ArrayList<>();
                    ZhuboDto zhuboDto = new ZhuboDto();
                    zhuboDto.channelId = findListDto.getUserId();
                    zhuboDto.headImg = findListDto.getHeadImg();
                    list.add(zhuboDto);
                    if (FloatingView.getInstance().isShow()) {
                        showExitDialog(list);
                    } else {
                        IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                   //     LivingRoomActivity.start(getContext(), list, 0, true);
                    }
                } else {//用户详情
                    MineDetailActivity.startActivityWithUserId(getContext(), findListDto.getUserId());
                }
            }
        });

        //预览图片和视频
        findListAdapter.setOnImageItemClickListener(new FindListAdapter.OnImageItemClickListener() {
            @Override
            public void onImageItemClick(FindListDto item, int imagePosition) {
                FindDetailBean detail = mapDetail.get(item.getId());
                //先从缓存中取详情
                if (detail != null) {
                    preview(detail, imagePosition);
                } else {
                    p.getDetailData(item.getId(), imagePosition);
                }

            }
        });
        findListAdapter.bindToRecyclerView(rvInclude);

        srRefresh.setOnRefreshLoadMoreListener(this);
        getListData();
        observeFindDetailChange();
    }
    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog(ArrayList<ZhuboDto>  list) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                   //     LivingRoomActivity.start(getActivity(), map, 0, true);
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
                .show(getActivity().getSupportFragmentManager(), "");
    }
    /**
     * 预览视频或者图片
     */
    private void preview(FindDetailBean detail, int position) {
        DetailCardActivity.start(getActivity(), (ArrayList) detail.getCover(), position);
    }

    /**
     * 插入自己发布的动态
     *
     * @param findListDto
     */
    public void insertSelfFind(FindListDto findListDto) {
        rvInclude.scrollToPosition(0);
        findListAdapter.addData(0, findListDto);
    }

    @SuppressLint("CheckResult")
    private void observeFindDetailChange() {
        RxBus.get().toObservable(FindDetailChangedEvent.class).compose(bindToLifecycle()).subscribe(findDetailChangedEvent -> {
            findListDto.setLikes(findDetailChangedEvent.getLikeCount());
            findListDto.setIsLike(findDetailChangedEvent.getIsLike());
            findListDto.setComments(findDetailChangedEvent.getCommentsCount());
            findListAdapter.setLike(toDetailPosition);
            findListAdapter.setComments(toDetailPosition);
        });
    }

    protected void showShareDialog(int id, String imgUrl, String content) {
        ShareCommonPopwindow shareCommonPopwindow = new ShareCommonPopwindow(getContext());
        shareCommonPopwindow.addClickListener(new ShareCommonPopwindow.OnShareClickListener() {
            @Override
            public void onWx() {
                p.getShareInfo(id, 0, imgUrl, content);
            }

            @Override
            public void onWxCircle() {
                p.getShareInfo(id, 1, imgUrl, content);
            }
        });
        shareCommonPopwindow.showPopupWindow();
    }

    public void addtention(int uid, int pos) {
        AttentionUtils.addAttention(uid, new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                findListDto.setIsAttention(1);
                findListAdapter.getData().set(pos, findListDto);
                L.e("===关注成功");
            }

            @Override
            public void _onError(String msg) {
                if (TextUtils.isEmpty(msg)) return;
                ToastUtils.showShort(getContext(), msg);
            }
        });
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        rvInclude.scrollToPosition(0);
    }

    /**
     * 传入筛选条件，同时刷新列表
     */
    public void filter(String city, String maxAge, String minAge, String sex) {
        page = 1;
        this.city = city;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.sex = sex;
        getListData();
    }

    private void getListData() {
        p.getFindList(page, city, maxAge, minAge, sex);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getListData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getListData();
    }

    @Override
    public void setOnLineList(ArrayList<CityHost> records) {

    }

    @Override
    public void getRequestFindListSuccess(BaseListDto<FindListDto> data) {
        srRefresh.closeHeaderOrFooter();
        if (page == 1) {
            findListAdapter.setNewData(data.records);
        } else {
            findListAdapter.addData(data.records);
        }
        page++;
    }

    @Override
    public void cardLikeSuccess(int id, int position) {
        FindListDto bean = findListAdapter.getItem(position);
        assert bean != null;
        if (bean.getIsLike() == 1) {
            bean.setIsLike(0);
            bean.setLikes(bean.getLikes() - 1);
        } else {
            bean.setIsLike(1);
            bean.setLikes(bean.getLikes() + 1);
        }
        findListAdapter.getData().set(position, bean);
        findListAdapter.setLike(position);
    }

    @Override
    public void getShareUrlSuccess(String url, int type, String imgUrl, String content) {
        /*获取分享成功*/
        ShareHelper.get().shareDynamic(getActivity(), url, "蜂窝互娱", imgUrl, content, type);
    }


    @Override
    public void onReceiveFlirtData(boolean isRefresh, List<CityHost> onlineFlirtData) {

    }

    @Override
    public void setMyOrderList(List<MyOrderDto> data) {

    }

    /**
     * @param id       条目id
     * @param detail   详情
     * @param position 点击的图片的position
     */
    @Override
    public void onGetFindDetail(int id, FindDetailBean detail, int position) {
        mapDetail.put(id, detail);
        preview(detail, position);
    }
}
