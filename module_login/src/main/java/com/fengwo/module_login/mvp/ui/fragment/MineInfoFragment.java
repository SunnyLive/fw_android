package com.fengwo.module_login.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.adapter.GuardAdapter;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.UserMedalBean;
import com.fengwo.module_comment.iservice.GetUserInfoByIdService;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserMedalService;
import com.fengwo.module_comment.utils.CopyUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_live_vedio.eventbus.ShowBuyGuardPopEvent;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.LiveProfitDto;
import com.fengwo.module_live_vedio.mvp.ui.pop.BuyShouhuPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShouhuListPopwindow;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.GiftWallDto;
import com.fengwo.module_login.mvp.presenter.MineInfoPresenter;
import com.fengwo.module_login.mvp.ui.activity.ContributeTodayActivity;
import com.fengwo.module_login.mvp.ui.activity.MonthGiftWallActivity;
import com.fengwo.module_login.mvp.ui.adapter.GiftWallAdapter;
import com.fengwo.module_login.mvp.ui.adapter.ImpressionAdapter;
import com.fengwo.module_login.mvp.ui.adapter.LiveLabelAdapter;
import com.fengwo.module_login.mvp.ui.adapter.MineTodayReceiveAdapter;
import com.fengwo.module_login.mvp.ui.iview.IMineInfoView;
import com.fengwo.module_login.utils.UserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MineInfoFragment extends BaseMvpFragment<IMineInfoView, MineInfoPresenter>
        implements IMineInfoView {
    @Autowired
    UserMedalService userMedalService;
    @BindView(R2.id.tv_id)
    TextView mTvId;   //蜂窝号
    @BindView(R2.id.tv_des)
    TextView mTvSignature;   //签名
    @BindView(R2.id.tv_distance)
    TextView mTvDistance;  //距离
    @BindView(R2.id.iv_user_level)
    ImageView mIvUserLevel;
    @BindView(R2.id.iv_live_level)
    ImageView mIvLiveLevel; //主播等级
    @BindView(R2.id.tv_live_level)
    TextView mTvLiveLevel;
    @BindView(R2.id.tv_constellation)
    TextView mTvConstellation; //星座
    @BindView(R2.id.tv_hw)
    TextView mTvHW;
    @BindView(R2.id.tv_address)
    TextView mTvAddress;    //地址
    @BindView(R2.id.tv_ideal_type)
    TextView mTvIdealType;
    @BindView(R2.id.tv_guard_num)
    TextView mTvGuardNum;   //守护人数
    @BindView(R2.id.rv_guard)
    RecyclerView mRvGuard;   //守护列表
    @BindView(R2.id.rl_live)
    RelativeLayout mRlLive;  //是否主播显示守护相关布局
    @BindView(R2.id.iv_guard)
    ImageView mIvGuard;
    @BindView(R2.id.tv_guard_left_time)
    TextView mTvGuardLeftTime;   //守护剩余时间
    @BindView(R2.id.tv_today_receive_num)
    TextView mTvTodayReceiveNum;
    @BindView(R2.id.tv_today_receive)
    TextView mTvReceiveName;
    @BindView(R2.id.rv_gift)
    RecyclerView mRvGift;    //礼物数据
    @BindView(R2.id.tv_is_online)
    TextView mTvIsOnline;   //是否在线
    @BindView(R2.id.tv_gift_num)
    TextView mTvGiftNum;  //礼物总数
    @BindView(R2.id.iv_sex)
    ImageView ivSex;  //性别
    @BindView(R2.id.rv_label)
    RecyclerView mRvLabel;
    @BindView(R2.id.rv_today_receive)
    RecyclerView mTvTodayReceive;   //今日贡献榜
    @BindView(R2.id.iv_guard_arrow)
    ImageView mIvGuardArrow;

    @BindView(R2.id.tv_impression__num)//今日印象值
            TextView mImpressionNum;
    @BindView(R2.id.rv_impression_img)
    RecyclerView mRvImpressionContent;//头像列

    @Autowired
    GetUserInfoByIdService getImpressionService; //今日印象值接口


    @BindView(R2.id.iv_activity_tag)
    ImageView ivTag;

    protected final static String PAGE_SIZE = ",3";
    protected int page = 1;

    private UserInfo mUserInfo;

    private ShouhuListPopwindow mPopWindow;
    private BuyShouhuPopwindow buyShouhuPopwindow;
    protected CompositeDisposable mCompositeDisposable;

    public static MineInfoFragment newInstance() {
        Bundle args = new Bundle();
        MineInfoFragment fragment = new MineInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected MineInfoPresenter initPresenter() {
        return new MineInfoPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine_info;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mRvGuard.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mTvTodayReceive.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRvImpressionContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
        mRvGift.setLayoutManager(layoutManager);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(RxBus.get().toObservable(ShowBuyGuardPopEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<ShowBuyGuardPopEvent>() {
                    @Override
                    public void accept(ShowBuyGuardPopEvent showBuyGuardPopEvent) throws Exception {
                            if (null != buyShouhuPopwindow&&showBuyGuardPopEvent.isMine) {
                                buyShouhuPopwindow.showPopupWindow();
                            }
                    }
                }));


    }

    public void initUserInfo(UserInfo userInfo) {
        if (userInfo == null)
            return;
        this.mUserInfo = userInfo;
        mTvId.setText(userInfo.fwId);
        mTvSignature.setText(userInfo.signature);
        int levelRes = ImageLoader.getResId("login_ic_v" + userInfo.getLevel(), R.drawable.class);
        mIvUserLevel.setImageResource(levelRes);
        if (userInfo.getLiveLevel() > 0) {
            int level2Res = ImageLoader.getResId("login_ic_type3_v" + userInfo.getLiveLevel(), R.drawable.class);
            mIvLiveLevel.setImageResource(level2Res);
        } else {
            mIvLiveLevel.setVisibility(View.GONE);
        }
        mTvConstellation.setText(userInfo.getConstellation());  //星座
        mTvHW.setText(TextUtils.isEmpty(userInfo.height) ? "" : userInfo.height + "cm" + (TextUtils.isEmpty(userInfo.weight) ? "  " : "  " + userInfo.weight + "kg  "));
        mTvAddress.setText(userInfo.location.replace("/", "."));
        if (!TextUtils.isEmpty(userInfo.idealTypeTag)) {
            mTvIdealType.setText("#" + userInfo.idealTypeTag.replace(",", " #"));  //理想型
        }
        mTvGuardNum.setText(DataFormatUtils.formatNumbers(userInfo.userGuardNum) + "人");
        if (userInfo.getId() != UserManager.getInstance().getUser().getId()) {
            mTvDistance.setText(DataFormatUtils.formatNumberKm(userInfo.distance));
        } else {
            mTvDistance.setText(userInfo.location.replace("/", "."));
        }
        mIvGuardArrow.setVisibility(userInfo.getId() == UserManager.getInstance().getUser().getId() ? View.VISIBLE : View.GONE);
        //判断是否是主播
        if (!TextUtils.isEmpty(userInfo.userRole) && userInfo.userRole.contains("ROLE_ANCHOR")) {
            mRlLive.setVisibility(View.VISIBLE);
            mIvLiveLevel.setVisibility(View.VISIBLE);
            mTvLiveLevel.setVisibility(View.VISIBLE);
            mTvReceiveName.setText("今日花蜜值");
        } else {
            mRlLive.setVisibility(View.GONE);
            mIvLiveLevel.setVisibility(View.GONE);
            mTvLiveLevel.setVisibility(View.GONE);
            mTvReceiveName.setText("今日贡献榜花蜜值");
        }
        List<UserInfo.UserGuardList> userGuardLists;
        if (userInfo.userGuardList != null && userInfo.userGuardList.size() > 3) {
            userGuardLists = userInfo.userGuardList.subList(0, 3);
        } else {
            userGuardLists = userInfo.userGuardList;
        }
        GuardAdapter guardAdapter = new GuardAdapter(userGuardLists);
        mRvGuard.setAdapter(guardAdapter);

        mTvIsOnline.setText(userInfo.onlineStatus == 0 ? "离线" : "在线");
//        ivSex.setVisibility(View.VISIBLE);
//        if (userInfo.sex == 1) {
//            ivSex.setImageResource(R.drawable.ic_boy_sex);
//        } else if (userInfo.sex == 2) {
//            ivSex.setImageResource(R.drawable.ic_girl_sex);
//        } else {
//            ivSex.setVisibility(View.GONE);
//        }
        //是否守护
        if (userInfo.myIsGuard) {
            mIvGuard.setVisibility(View.GONE);
            mTvGuardLeftTime.setVisibility(View.VISIBLE);
            mTvGuardLeftTime.setText("剩余: " + userInfo.myGuardDeadline + "天");
        } else {
            mIvGuard.setVisibility(View.VISIBLE);
            mTvGuardLeftTime.setVisibility(View.GONE);
        }
        if (userInfo.id == UserManager.getInstance().getUser().getId()) {
            mIvGuard.setVisibility(View.GONE);
        }
        mTvTodayReceiveNum.setText((TextUtils.isEmpty(userInfo.todayReceive) ? "0" : DataFormatUtils.formatNumberGift(Integer.parseInt(userInfo.todayReceive))));

        p.getGiftWall(page + ",5", userInfo.getId());
        p.getReceiveList(page + PAGE_SIZE, userInfo.getId());
        //直播标签
        if (!TextUtils.isEmpty(userInfo.liveLabel)) {
            mRvLabel.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            LiveLabelAdapter liveLabelAdapter = new LiveLabelAdapter();
            mRvLabel.setAdapter(liveLabelAdapter);
            String tagName = userInfo.getLiveLabel();
            if (!TextUtils.isEmpty(tagName)) {
                String[] split = tagName.split(",");
                List<String> tables = Arrays.asList(split);
                liveLabelAdapter.setNewData(tables);
            }
        }

        userMedalService.getUserMedal(userInfo.getId(), 0, new LoadingObserver<HttpResult<UserMedalBean>>() {
            @Override
            public void _onNext(HttpResult<UserMedalBean> data) {
//                ivRegimental.setVisibility(userMedalBean.isLeader == 1 ? View.VISIBLE : View.INVISIBLE);
                if (data.data.medalOneId > 0 && !TextUtils.isEmpty(data.data.medalOneIcon))
//                    ivTag.setImageResource(ImageLoader.getResId("ic_medal_" + userMedalBean.medalOneId, R.drawable.class));
                    ImageLoader.loadImg(ivTag, data.data.medalOneIcon);
                if (data.data.medalTwoId > 0 && !TextUtils.isEmpty(data.data.medalTwoIcon))
//                    ivTag2.setImageResource(ImageLoader.getResId("ic_medal_" + userMedalBean.medalTwoId, R.drawable.class));
                    ImageLoader.loadImg(ivTag, data.data.medalTwoIcon);
                if (data.data.medalThreeId > 0)
//                    ivTag3.setImageResource(ImageLoader.getResId("ic_medal_" + userMedalBean.medalThreeId, R.drawable.class));
                    ImageLoader.loadImg(ivTag, data.data.medalThreeIcon);
//                if (userMedalBean.medalOneId > 11 && userMedalBean.medalOneId < 20)
//                    ivHeaderBg.setBackgroundResource(ImageLoader.getResId("bg_mine_head_" + userMedalBean.medalOneId, R.drawable.class));
//                else
//                    ivHeaderBg.setBackgroundResource(ImageLoader.getResId("bg_mine_head_0", R.drawable.class));
//                ImageLoader.loadImg(ivHeaderBg,data.data.medalOneHeadFrame);

            }

            @Override
            public void _onError(String msg) {

            }
        });

        //获取今日印象总值
        getImpressionService.getUserInfoById(String.valueOf(userInfo.id), new LoadingObserver<HttpResult<UserInfo>>() {
            @Override
            public void _onNext(HttpResult<UserInfo> data) {
                if (data.isSuccess()) {
                    mImpressionNum.setText(String.valueOf(data.data.todayTotalTime));
                    mRvImpressionContent.setAdapter(new ImpressionAdapter(data.data.getTodayUserTimes()));
                }
            }

            @Override
            public void _onError(String msg) {

            }
        });


        //用户等级 徽章
        if (userInfo.userMedalsList != null && !userInfo.userMedalsList.isEmpty()) {
            List<UserInfo.UserMedalsList> userMedalsList = userInfo.userMedalsList;
            UserInfo.UserMedalsList uml = userMedalsList.get(userMedalsList.size() - 1);

            if (!TextUtils.isEmpty(uml.medalIcon)) {
                ivTag.setVisibility(View.VISIBLE);
                ImageLoader.loadImg(ivTag, uml.medalIcon);
            }
        }

    }


    @OnClick({R2.id.iv_guard, R2.id.tv_id_copy, R2.id.tv_more, R2.id.rl_today_receive, R2.id.rl_anchor_guard})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (null == mUserInfo) {
            return;
        }
        if (id == R.id.iv_guard) {  //马上守护
            if (null == mPopWindow) {
                mPopWindow = new ShouhuListPopwindow(getActivity(), false, 1);
            }
            p.getUserInfo(false, mUserInfo.getId());

             p.getGuardList(mUserInfo.getId());
        } else if (id == R.id.rl_anchor_guard) {
            if (UserManager.getInstance().getUser().getId() != mUserInfo.getId())
                return;
            if (null == mPopWindow) {
                mPopWindow = new ShouhuListPopwindow(getActivity(), true, 1);
            }
            p.getUserInfo(false, mUserInfo.getId());
                   p.getGuardList(mUserInfo.getId());
        } else if (id == R.id.tv_id_copy) {
            if (CopyUtils.copy2Board(Objects.requireNonNull(getActivity()), mTvId.getText().toString())) {
                toastTip("复制成功");
            } else toastTip("复制失败");
        } else if (id == R.id.tv_more) {    //查看更多
            Intent intent = new Intent(getActivity(), MonthGiftWallActivity.class);
            intent.putExtra("userId", mUserInfo.getId());
            startActivity(intent);
        } else if (id == R.id.rl_today_receive) {
            Intent intent = new Intent(getActivity(), ContributeTodayActivity.class);
            intent.putExtra("userId", mUserInfo.getId());
            startActivity(intent);
        }
    }


    @Override
    public void getGiftWall(GiftWallDto data) {
        GiftWallAdapter adapter = new GiftWallAdapter(data.getPageList().getRecords());
        mRvGift.setAdapter(adapter);
        mTvGiftNum.setText(DataFormatUtils.formatNumberGift(data.getTotalMoney()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable == null) return;
        mCompositeDisposable.isDisposed();
        mCompositeDisposable.clear();
    }

    @Override
    public void setGuardWindow(int total, ArrayList<GuardListDto.Guard> records) {
        if (buyShouhuPopwindow == null)
            buyShouhuPopwindow = new BuyShouhuPopwindow(getActivity());
        buyShouhuPopwindow.setZhuboInfo(mUserInfo.getId(),mUserInfo);
        buyShouhuPopwindow.setBalance(UserManager.getInstance().getUser().getBalance());
        if (null != mPopWindow)
            mPopWindow.setData(total, records);
    }

    @Override
    public void setTodayReceive(int total, List<LiveProfitDto.RecordsBean> records) {
        MineTodayReceiveAdapter adapter = new MineTodayReceiveAdapter(records);
        mTvTodayReceive.setAdapter(adapter);
    }

    @Override
    public void showShouhu(Boolean isHost, int id, UserInfo data) {
        mPopWindow.setData(isHost, id, data);
        mPopWindow.showPopupWindow();
    }
}
