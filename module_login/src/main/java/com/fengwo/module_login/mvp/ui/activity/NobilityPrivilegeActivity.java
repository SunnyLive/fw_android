package com.fengwo.module_login.mvp.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.widget.ViewPagerIndicator;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_live_vedio.mvp.dto.RefreshBackpack;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.NobilityDTO;
import com.fengwo.module_login.mvp.dto.NobilityTypeDTO;
import com.fengwo.module_login.mvp.dto.PrivilegeDto;
import com.fengwo.module_login.mvp.presenter.NobilityPrivilegePresenter;
import com.fengwo.module_login.mvp.ui.adapter.NobilityAdapter;
import com.fengwo.module_login.mvp.ui.adapter.NobilityTypeAdapter;
import com.fengwo.module_login.mvp.ui.adapter.PrivilegeAdapter;
import com.fengwo.module_login.mvp.ui.iview.INobilityPrivilegeView;
import com.fengwo.module_login.mvp.ui.pop.NobilityDialog;
import com.fengwo.module_login.widget.TestPagerTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
@Route(path = ArouterApi.VIP_ACTION)
public class NobilityPrivilegeActivity extends BaseMvpActivity<INobilityPrivilegeView, NobilityPrivilegePresenter>
        implements INobilityPrivilegeView, NobilityTypeAdapter.OnItemClickListener {

    @Autowired
    UserProviderService userProviderService;

    @BindView(R2.id.privilegeViewPager)
    ViewPager nobilityViewPager;
    @BindView(R2.id.indicator)
    ViewPagerIndicator indicator;
    @BindView(R2.id.rvType)
    RecyclerView rvType;
    @BindView(R2.id.rvPrivilege)
    RecyclerView rvPrivilege;
    @BindView(R2.id.tvAccountBalance)
    TextView tvAccountBalance;
    @BindView(R2.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R2.id.tv_sy)
    TextView tv_sy;
    private NobilityTypeAdapter nobilityAdapter;
    private NobilityAdapter typeAdapter;

    private List<NobilityDTO> nobilityList;
    private PrivilegeAdapter privilegeAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_nobility_privilege;
    }

    @Override
    public NobilityPrivilegePresenter initPresenter() {
        return new NobilityPrivilegePresenter();
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().setBackIcon(R.drawable.ic_back_black)
                .setTitleColor(R.color.text_33)
                .setTitle("贵族权限").showBack(true).build();
//        setTitleBackground(getResources().getColor(R.color.white));

        initNobilityViewPager();
        initTypeRecyclerView();

        p.getNobilityTypeList();
        tvAccountBalance.setText(String.format("我的花钻 %d", userProviderService.getUserInfo().balance));
    }

    /**
     * 返回贵族个数、数据
     */
    @Override
    public void setPrivilegeTypeList(List<NobilityTypeDTO> data) {
        nobilityAdapter.list = data;
        nobilityViewPager.setAdapter(nobilityAdapter);
        indicator.setViewPager(nobilityViewPager, false);
        p.getNobilityPrivilege();
    }

    /**
     * 返回所有贵族套餐内容
     */
    @Override
    public void setPrivilegeList(List<NobilityDTO> nobilityList) {
        this.nobilityList = nobilityList;
        if (nobilityAdapter.list != null && nobilityAdapter.list.size() > 1) {
            nobilityViewPager.setCurrentItem(1);
            setPrivilegeData(nobilityAdapter.list.get(nobilityViewPager.getCurrentItem()));
        }
    }

    @Override
    public void buyNobilitySuccess(HttpResult<PopoDto> data) {
        toastTip("购买成功");
        userProviderService.getUserInfo().setExperience(data.data.getExperience().toString());
        userProviderService.setUsetInfo(userProviderService.getUserInfo());
        //告诉直播室 这边购买成功啦 可以刷新数据啦
        RxBus.get().post(new RefreshBackpack());
        p.updateWalletInfo();

    }

    @Override
    public void buyNobilityFail(String msg) {
        toastTip(msg);
    }

    @Override
    public void updateWalletInfo(Long amount) {
        userProviderService.getUserInfo().balance = amount;
        tvAccountBalance.setText(String.format("我的花钻 %d", amount));
        finish();

    }

    /**
     * 切换贵族
     */
    @Override
    public void NobilityClick(NobilityTypeDTO data, int position) {
        if (nobilityViewPager.getCurrentItem() == position) return;
        nobilityViewPager.setCurrentItem(position);
        setPrivilegeData(data);
        if (!typeAdapter.getData().isEmpty())
            initPrivilegeRecyclerView(typeAdapter.getData().get(0).privilegeVOList);
    }

    @OnClick(R2.id.tvSubmit)
    public void onViewClick(View view) {
        if (typeAdapter != null && typeAdapter.getItemCount() > 0) {
            NobilityDTO dto = typeAdapter.getData().get(typeAdapter.getSelectPosition());
            if (userProviderService.getUserInfo().balance >= dto.itemPrice) {
                showTypeDialog(NobilityDialog.TYPE_PAY_CONFIRM);
            } else showTypeDialog(NobilityDialog.TYPE_PAY_FAILURE);
        }
    }

    private void initNobilityViewPager() {
        nobilityAdapter = new NobilityTypeAdapter(this);
        nobilityViewPager.setPageMargin(DensityUtils.dp2px(this, 10));
        nobilityViewPager.setOffscreenPageLimit(2);
        int padding = (ScreenUtils.getScreenWidth(this)) / 3;
        ViewGroup.LayoutParams lp = nobilityViewPager.getLayoutParams();
        lp.height = padding;
        nobilityViewPager.setLayoutParams(lp);
        nobilityViewPager.setPadding(padding + DensityUtils.dp2px(this, 10),
                0, padding + DensityUtils.dp2px(this, 10), 0);
        nobilityViewPager.setPageTransformer(false, new TestPagerTransformer(0.66F));
        nobilityViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setPrivilegeData(nobilityAdapter.list.get(position));
                if (!typeAdapter.getData().isEmpty())
                    initPrivilegeRecyclerView(typeAdapter.getData().get(0).privilegeVOList);
            }
        });
    }

    private void initTypeRecyclerView() {
        rvType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        typeAdapter = new NobilityAdapter();
        typeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            typeAdapter.setSelectItem(position);
            if (userProviderService.getUserInfo().myVipLevel > typeAdapter.getData().get(position).level) {
                tvSubmit.setEnabled(false);
                tvSubmit.setText("无需购买");
                tv_sy.setVisibility(View.GONE);
            } else if (userProviderService.getUserInfo().myVipLevel == typeAdapter.getData().get(position).level) {
                tvSubmit.setEnabled(true);
                tvSubmit.setText("续费");
                tv_sy.setVisibility(View.VISIBLE);
                tv_sy.setText("剩余" + userProviderService.getUserInfo().vipRemainDays + "天");

            } else {
                tvSubmit.setEnabled(true);
                tvSubmit.setText("立即开通");
                tv_sy.setVisibility(View.GONE);
            }
            if (!typeAdapter.getData().isEmpty())
                initPrivilegeRecyclerView(typeAdapter.getData().get(position).privilegeVOList);
        });
        rvType.setAdapter(typeAdapter);

        rvPrivilege.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void initPrivilegeRecyclerView(List<PrivilegeDto> list) {
        if (privilegeAdapter == null) {
            privilegeAdapter = new PrivilegeAdapter(list);
            rvPrivilege.setAdapter(privilegeAdapter);
        } else privilegeAdapter.setNewData(list);
    }

    /**
     * 设置贵族所属的套餐
     */
    private void setPrivilegeData(NobilityTypeDTO currentItem) {
        if (nobilityList == null) return;
        ArrayList<NobilityDTO> items = new ArrayList<>();
        for (NobilityDTO item : nobilityList) {
            if (item.level == currentItem.level) items.add(item);
        }
        typeAdapter.setNewData(items);
        typeAdapter.setSelectItem(0);
        UserInfo userInfo =  userProviderService.getUserInfo();
        KLog.e("tag",userInfo.myVipLevel+"");
        if (userProviderService.getUserInfo().myVipLevel > currentItem.level) {
            tvSubmit.setEnabled(false);
            tvSubmit.setText("无需购买");
            tv_sy.setVisibility(View.GONE);
        } else if (userProviderService.getUserInfo().myVipLevel == currentItem.level) {
            tvSubmit.setEnabled(true);
            tvSubmit.setText("续费");
            tv_sy.setVisibility(View.VISIBLE);
            tv_sy.setText("剩余" + userProviderService.getUserInfo().vipRemainDays + "天");
        } else {
            tvSubmit.setEnabled(true);
            tv_sy.setVisibility(View.GONE);
            tvSubmit.setText("立即开通");
        }
    }

    private void showTypeDialog(int type) {
        NobilityDialog dialog = new NobilityDialog();
        dialog.setListener(new NobilityDialog.OnPositiveItemClickListener() {
            @Override
            public void onItemConfirm() {
                NobilityDTO dto = typeAdapter.getData().get(typeAdapter.getSelectPosition());
                p.buyNobility(dto.id);
            }

            @Override
            public void onItemPay() {
                startActivity(ChongzhiActivity.class);
            }
        });
        dialog.setType(type);
        dialog.show(getSupportFragmentManager(), NobilityDialog.class.getSimpleName());
    }
}