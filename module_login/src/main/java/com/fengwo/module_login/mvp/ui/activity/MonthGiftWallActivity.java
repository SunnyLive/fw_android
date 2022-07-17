/*
 * 大兄弟你好 欢迎来到前辈留下的代码
 * 这里是没有注释的哦，我来把它加上吧
 * 这里呢，原本是主播本月的直播礼物流水记录
 * 现如今把它改为达人或直播的礼物流水记录页面
 * 如果是达人就展示达人的礼物
 * 如果是直播就展示直播的礼物
 * 两个都是的话就以切换的方式展示出来
 *
 * 好好干 祝你好运  我就是前面那个给你留注释的活雷锋
 *
 * */
package com.fengwo.module_login.mvp.ui.activity;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.GiftWallDto;
import com.fengwo.module_login.mvp.presenter.GiftWallPresenter;
import com.fengwo.module_login.mvp.ui.adapter.GiftWallAdapter;
import com.fengwo.module_login.mvp.ui.iview.IGiftWallView;
import com.fengwo.module_login.utils.UserManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MonthGiftWallActivity extends BaseMvpActivity<IGiftWallView, GiftWallPresenter> implements IGiftWallView {

    protected int page = 1;
    protected final String PAGE_SIZE = ",40";
    @BindView(R2.id.tv_receive_num)
    TextView mTvReceiveNum;
    @BindView(R2.id.rv_gift)
    RecyclerView mRvGift;
    @BindView(R2.id.refresh)
    SmartRefreshLayout mRefresh;
    private GiftWallAdapter mAdapter;
    View mView;

    private TextView mTvEmpty;

    @BindView(R2.id.rg_nav)
    RadioGroup mRgNavChoose; //达人 主播 礼物切换

    @BindView(R2.id.iv_nav_back)
    View viewBack;

    @Override
    public GiftWallPresenter initPresenter() {
        return new GiftWallPresenter();
    }

    @Override
    protected void initView() {
        viewBack.setOnClickListener(v -> {
            onBackPressed();
        });
        SmartRefreshLayoutUtils.setWhiteBlackText(this, mRefresh);
        mView = LayoutInflater.from(this).inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
        mTvEmpty = mView.findViewById(R.id.tv_empty);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRvGift.setLayoutManager(layoutManager);
        mRefresh.setOnLoadMoreListener(refreshLayout -> {
            page++;
            getChooseLoadData();

        });
        mRefresh.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getChooseLoadData();
        });
        mAdapter = new GiftWallAdapter(null);
        mRvGift.setAdapter(mAdapter);

        mRgNavChoose.setOnCheckedChangeListener((group, checkedId) -> {
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            getChooseLoadData();
        });


        //判断是主播还是达人
        //如果是主播就展示主播的礼物
        //如果是达人就展示达人的礼物
        //既是主播和达人就同时展示礼物墙

        //是达人还是主播
        if  (UserManager.getInstance().getUser().myIsCardStatus == 1
                && UserManager.getInstance().getUser().wenboAnchorStatus == 1) {
            mRgNavChoose.getChildAt(0).performClick();
        }
        //如果是达人
        else if (UserManager.getInstance().getUser().wenboAnchorStatus == 1) {
            mRgNavChoose.getChildAt(1).performClick();
            mRgNavChoose.getChildAt(0).setVisibility(View.GONE);
        }
        //如果是主播
        else if(UserManager.getInstance().getUser().myIsCardStatus == 1) {
            mRgNavChoose.getChildAt(0).performClick();
            mRgNavChoose.getChildAt(1).setVisibility(View.GONE);
        }
        getGift();

    }



    private void getChooseLoadData(){
        int checkedId = mRgNavChoose.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_anchor_gift_title) {
            getGift();
            mTvEmpty.setText("暂无直播礼物");
        } else if (checkedId == R.id.rb_expert_gift_title) {
            getExpertGift();
            mTvEmpty.setText("暂无达人礼物");
        }
    }



    private void getGift() {
        int userId = getIntent().getIntExtra("userId", 0);
        p.getGiftWall(page + PAGE_SIZE, userId);
    }

    private void getExpertGift(){
        int userId = getIntent().getIntExtra("userId", 0);
        p.getExpertGiftWall(page, userId);
    }


    @Override
    protected int getContentView() {
        return R.layout.login_activity_month_gift_wall;
    }


    @Override
    public void getGiftWall(GiftWallDto data) {
        mTvReceiveNum.setText(DataFormatUtils.formatNumberGift(data.getTotalMoney()));
        mRefresh.finishRefresh();
        if (page == 1) {
            if (data.getPageList().getRecords() == null || data.getPageList().getRecords().size() <= 0) {
                mAdapter.setEmptyView(mView);
            } else {
                mAdapter.setNewData(data.getPageList().getRecords());
            }
        } else {
            mAdapter.addData(data.getPageList().getRecords());
            mRefresh.finishLoadMore();
        }
    }
}
