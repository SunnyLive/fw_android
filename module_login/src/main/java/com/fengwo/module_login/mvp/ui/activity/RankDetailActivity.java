package com.fengwo.module_login.mvp.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.RankLevelDto;
import com.fengwo.module_login.mvp.presenter.RankDetailPresenter;
import com.fengwo.module_login.mvp.ui.iview.IRankDerailView;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_login.widget.DividerDecorator;
import com.fengwo.module_login.widget.HuafenProgressBar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class RankDetailActivity extends BaseMvpActivity<IRankDerailView, RankDetailPresenter>
        implements IRankDerailView {

    @BindView(R2.id.iv_header)
    ImageView ivHeader;
    @BindView(R2.id.tv_experience)
    TextView tvExperience;
    @BindView(R2.id.progress)
    HuafenProgressBar progressBar;
    @BindView(R2.id.rv_right)
    RecyclerView rvRight;
    @BindView(R2.id.rv_level_function)
    RecyclerView rvFunction;
    @BindView(R2.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R2.id.title_tv)
    TextView tvTitle;
    @BindView(R2.id.iv_level)
    ImageView iv_level;
    @BindView(R2.id.iv_background)
    ImageView ivBackGround;

    @Override
    public RankDetailPresenter initPresenter() {
        return new RankDetailPresenter();
    }

    @Override
    protected void initView() {
        if (DarkUtil.isDarkTheme(this)){
            ivBackGround.setVisibility(View.GONE);
        }
        setWhiteTitle("主播等级");
        UserInfo userInfo = UserManager.getInstance().getUser();
        progressBar.setProgressWithAnim(0.45F, false);
        ImageLoader.loadImg(ivHeader, userInfo.headImg);
        int resId = getResources().getIdentifier("leve_"+userInfo.myLiveLevel, "drawable" ,getPackageName());
        iv_level.setImageResource(resId);
   //     tvExperience.setText(String.format("距离LV%d 还差%d经验", userInfo.myLiveLevel + 1, userInfo.getForbiddenWords()));
        initprivilege();
        initFunction();

        p.getRankLevelData();
    }

    @Override
    protected int getContentView() {
        return R.layout.login_rank_detaik;
    }

    @OnClick(R2.id.btn_back)
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) {
            finish();
        }
    }

    private void initprivilege() {
        BaseQuickAdapter privilegeAdapter = new BaseQuickAdapter(R.layout.login_item_huafen_privilege) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, Object item) {
                if (helper.getLayoutPosition()==0) {
                    helper.setText(R.id.tv_name, "等级标识");
                    helper.setText(R.id.tv_level, "LV.1开启");
                    helper.getView(R.id.iv).setBackgroundResource(R.drawable.ic_host_level_power_1);
                }else if (helper.getLayoutPosition() == 1){
                    helper.setText(R.id.tv_name, "踢人特权");
                    helper.setText(R.id.tv_level, "LV.5开启");
                    helper.getView(R.id.iv).setBackgroundResource(R.drawable.ic_host_level_power_2);
                }else {
                    helper.setText(R.id.tv_name, "超级火箭");
                    helper.setText(R.id.tv_level, "LV.50开启");
                    helper.getView(R.id.iv).setBackgroundResource(R.drawable.ic_host_level_power_3);
                }
            }
        };
        rvRight.setLayoutManager(new GridLayoutManager(this, 3));
        rvRight.setAdapter(privilegeAdapter);
        privilegeAdapter.setNewData(Arrays.asList(1, 1, 1));
    }

    private void initFunction() {
        BaseQuickAdapter functionAdapter = new BaseQuickAdapter(R.layout.item_rank_detail) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, Object item) {
                if (helper.getLayoutPosition() == 0) {
                    helper.setText(R.id.tv_func, "升级方式")
                            .setText(R.id.tv_action, "行为")
                            .setText(R.id.tv_experience, "获得经验值")
                            .setText(R.id.tv_max, "每日上限值");
                } else {
                    helper.setText(R.id.tv_func, "直播")
                            .setText(R.id.tv_action, "每日开播 10分钟")
                            .setText(R.id.tv_experience, "10")
                            .setText(R.id.tv_max, "400");
                }
            }
        };
        DividerDecorator itemDecoration = new DividerDecorator(this, DividerDecorator.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_orange_1dp));
        rvFunction.addItemDecoration(itemDecoration);
        rvFunction.setLayoutManager(new LinearLayoutManager(this));
        rvFunction.setAdapter(functionAdapter);
        functionAdapter.setNewData(Arrays.asList(1, 1));
    }

    @Override
    public void setRankLevelData(RankLevelDto data) {
        ImageLoader.loadImg(ivHeader, data.getHeadImg());
        int resId = ImageLoader.getChannelLevel(data.getLevel());
        iv_level.setImageResource(resId);
        tvExperience.setText(String.format("距离LV%d 还差%d经验",
                (data.getLevel() + 1), (data.getHighest() - data.getExperience())));
        float percent = new BigDecimal(data.getExperience() - data.getLowest())
                .divide(new BigDecimal(data.getHighest() - data.getLowest()), 2, RoundingMode.HALF_UP)
                .floatValue();
        progressBar.setProgressWithAnim(percent, false);
    }
}