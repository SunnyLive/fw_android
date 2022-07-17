package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.BuyGuardSuccessEvent;
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent;
import com.fengwo.module_live_vedio.mvp.dto.BuyShouhuDto;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.BuyShouhuListAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.PrivilegeAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

public class BuyShouhuPopwindow extends BasePopupWindow {
    private final CompositeDisposable mCompositeDisposable;
    @BindView(R2.id.ll_xiaoshouhu)
    LinearLayout llXiaoshouhu;
    @BindView(R2.id.ll_dashouhu)
    LinearLayout llDashouhu;
    @BindView(R2.id.rv)
    RecyclerView rv;
    @BindView(R2.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_total)
    TextView tvTotal;
    @BindView(R2.id.tv_money)
    TextView tvMoney;
    @BindView(R2.id.tv_moneys)
    TextView tvMoneys;
    @BindView(R2.id.tv_cz)
    TextView tv_cz;

    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R2.id.rv_tequan)
    RecyclerView rvTequan;
    @BindView(R2.id.btn_submit)
    TextView btnSubmit;
    @BindView(R2.id.tv_tile_name)
    TextView tv_tile_name;


    @BindView(R2.id.iv_user_level_badge)
    ImageView iv_user_level_badge;

    @BindView(R2.id.iv_anchor_level_badge)
    ImageView iv_anchor_level_badge;
    @BindView(R2.id.iv_vip_level_badge)
    ImageView iv_vip_level_badge;



    private Context mContext;
    @Autowired
    UserProviderService userProviderService;

    private BuyShouhuListAdapter adapter;
    private PrivilegeAdapter privilegeAdapter;

    private int channelId;
    List<List<BuyShouhuDto.GuardListBean>> guardList;
    private CompositeDisposable disposables;

    public BuyShouhuPopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        disposables = new CompositeDisposable();
        ARouter.getInstance().inject(this);
        mContext = context;
        mCompositeDisposable =new CompositeDisposable();
        ButterKnife.bind(this, getContentView());
        llXiaoshouhu.setBackgroundResource(R.drawable.live_bg_buy_shouhu_selected);

        getShouhu();
        tv_cz.setOnClickListener(v -> {
            RxBus.get().post(new ShowRechargePopEvent());
            dismiss();
        });


    }

    public void setZhuboInfo(int id,UserInfo data) {
        channelId = id;

            if(data.liveLevel>0){
                iv_user_level_badge.setVisibility(View.VISIBLE);
                int levelRes = ImageLoader.getResId("login_ic_type3_v" + data.liveLevel,
                        R.drawable.class);
                iv_user_level_badge.setImageResource(levelRes);
            }
            tvName.setText(data.nickname + "的守护军团");
            tv_tile_name.setText(data.nickname);
            ImageLoader.loadImg(ivHeader, data.headImg);
            //   tvTotal.setText(data.data.total + "");






    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_buyshouhu);
    }

    @OnClick({R2.id.ll_xiaoshouhu, R2.id.ll_dashouhu, R2.id.btn_submit})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.ll_xiaoshouhu) {
            llXiaoshouhu.setBackgroundResource(R.drawable.live_bg_buy_shouhu_selected);
            llDashouhu.setBackgroundResource(R.drawable.live_bg_buy_shouhu_normal);
            adapter.setNewData(guardList.get(0));
        } else if (id == R.id.ll_dashouhu) {
            llDashouhu.setBackgroundResource(R.drawable.live_bg_buy_shouhu_selected);
            llXiaoshouhu.setBackgroundResource(R.drawable.live_bg_buy_shouhu_normal);
            adapter.setNewData(guardList.get(1));
        } else if (id == R.id.btn_submit) {
            btnSubmit.setEnabled(false);
            buyShouhu();
        }
    }

    private void buyShouhu() {
        if (null == adapter || null == adapter.getSelected()) return;
        BuyShouhuDto.GuardListBean dto = adapter.getSelected();
        disposables.add(new RetrofitUtils().createApi(LiveApiService.class)
                .buyGuard(dto.getIdX(), channelId)
                .compose(RxUtils.applySchedulers2())
                .subscribeWith(new LoadingObserver<HttpResult<PopoDto>>() {
                    @Override
                    public void _onNext(HttpResult<PopoDto> data) {
                        if (data.isSuccess()) {
                            userProviderService.getUserInfo().setExperience(data.data.getExperience().toString());
                            userProviderService.setUsetInfo(userProviderService.getUserInfo());
                            BuyShouhuDto.LevelListBean bean = levelList.get(tabLayout.getSelectedTabPosition());
                            RxBus.get().post(new BuyGuardSuccessEvent(bean.getLevelIcon(), bean.getLevelName(),data.data.getExperience().toString()));
                            dismiss();
                        } else {
                            ToastUtils.showShort(mContext, data.description);
                        }
                        btnSubmit.setEnabled(true);
                    }

                    @Override
                    public void _onError(String msg) {
                        btnSubmit.setEnabled(true);
                    }
                }));

    }

    List<BuyShouhuDto.LevelListBean> levelList;

    private void getShouhu() {
        disposables.add(new RetrofitUtils().createApi(LiveApiService.class)
                .getShouhuList()
                .compose(RxUtils.applySchedulers2())
                .subscribeWith(new LoadingObserver<HttpResult<BuyShouhuDto>>() {
                    @Override
                    public void _onNext(HttpResult<BuyShouhuDto> data) {
                        if (!data.isSuccess()) {
                            return;
                        }
                        guardList = new ArrayList<>();
                        levelList = data.data.getLevelList();
                        initLevelTab(levelList);
                        for (int i = 0; i < levelList.size(); i++) {
                            List<BuyShouhuDto.GuardListBean> temp = new ArrayList<>();
                            for (int j = 0; j < data.data.getGuardList().size(); j++) {
                                BuyShouhuDto.GuardListBean b = data.data.getGuardList().get(j);
                                if (b.getLevelX() == levelList.get(i).getLevelX()) {
                                    temp.add(b);
                                }
                            }
                            guardList.add(temp);
                        }
                        setRight(guardList.get(0).get(0).getPrivilegeVOList());
                        rv.setLayoutManager(new GridLayoutManager(mContext, 3));
                        adapter = new BuyShouhuListAdapter(guardList.get(0));
                        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                BuyShouhuPopwindow.this.adapter.check(position);
                                setRight(BuyShouhuPopwindow.this.adapter.getPrivilege());
                            }
                        });
                        rv.setAdapter(adapter);
//                        setRight(litterList.get(0));
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));
    }


    private void initLevelTab(List<BuyShouhuDto.LevelListBean> levelList) {
        for (int i = 0; i < levelList.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = View.inflate(mContext, R.layout.live_item_shouhulevel, null);
            ImageView im_yz = view.findViewById(R.id.im_yz);
            if (i == 0) {
                im_yz.setVisibility(View.VISIBLE);
            }else {
                im_yz.setVisibility(View.GONE);
            }
            TextView tv = view.findViewById(R.id.tv_name);
            ImageView iv = view.findViewById(R.id.iv);
            tv.setText(levelList.get(i).getLevelName());
            ImageLoader.loadImgFitCenter(iv, levelList.get(i).getLevelIcon());
            tab.setCustomView(view);
            tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.im_yz).setVisibility(View.VISIBLE);
             //   tab.getCustomView().setBackgroundResource(R.drawable.live_bg_buy_shouhu_selected);
                int position = tab.getPosition();
                if (!CommentUtils.isListEmpty(guardList.get(position))) {
                    adapter.check(0);
                    adapter.setNewData(guardList.get(position));
                    setRight(adapter.getPrivilege());
                } else {
                    adapter.setNewData(null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.im_yz).setVisibility(View.GONE);
                tab.getCustomView().setBackgroundResource(0);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setRight(List<BuyShouhuDto.GuardListBean.PrivilegeVOListBean> dto) {
        if (privilegeAdapter == null) {
            privilegeAdapter = new PrivilegeAdapter(dto);
            rvTequan.setLayoutManager(new GridLayoutManager(mContext, 4));
            rvTequan.setAdapter(privilegeAdapter);
        } else {
            privilegeAdapter.setNewData(dto);
        }
    }

    @Override
    public void dismiss() {
        if (null != disposables && disposables.size() > 0) {
            disposables.clear();
        }
        super.dismiss();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    public void setBalance(long huazuan) {
        tvMoney.setText("我的花钻：" + huazuan);
        tvMoneys.setText( String.valueOf(huazuan));
    }
}
