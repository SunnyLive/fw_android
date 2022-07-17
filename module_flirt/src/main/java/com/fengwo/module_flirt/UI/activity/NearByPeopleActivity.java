package com.fengwo.module_flirt.UI.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_flirt.Interfaces.INearbyView;
import com.fengwo.module_flirt.P.NearbyPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.adapter.NearbyAdapter;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.widget.FilterFlirtPopwindow;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @Author BLCS
 * @Time 2020/8/11 16:29
 */
public class NearByPeopleActivity extends BaseMvpActivity<INearbyView, NearbyPresenter> implements INearbyView, OnRefreshListener, OnLoadMoreListener {
    @BindView(R2.id.rv_include)
    RecyclerView mRv;
    @BindView(R2.id.sr_refresh)
    SmartRefreshLayout mRefresh;
    private int page = 1;
    public String value1Age = "";
    public String value2Age = "";
    public int sexA = 0;
    private NearbyAdapter nearbyAdapter;
    public static void start(Activity activity){
        activity.startActivity(new Intent(activity,NearByPeopleActivity.class));
    }
    @Override
    public NearbyPresenter initPresenter() {
        return new NearbyPresenter();
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_arrow_black_left)
                .setTitle("附近的人")
                .setTitleColor(R.color.text_33)
                .setRightText("筛选", v ->  selectedRange())
                .setRightTextColor(R.color.purple_9966FF)
                .build();
        mRefresh.setOnRefreshListener(this);
        mRefresh.setOnLoadMoreListener(this);
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        nearbyAdapter = new NearbyAdapter();
        mRv.setAdapter(nearbyAdapter);
        View view = getLayoutInflater().inflate(R.layout.item_base_empty_view, null);
        nearbyAdapter.setEmptyView(view);
        nearbyAdapter.setOnItemClickListener((baseQuickAdapter, view1, pos) -> {
            if (isFastClick()) return;
            ArrayList<CityHost> data = (ArrayList<CityHost>) baseQuickAdapter.getData();
            int bstatus = data.get(pos).getBstatus();
            if (bstatus==0){
                MineDetailActivity.startActivityWithUserId(NearByPeopleActivity.this, data.get(pos).getId());
            }else{
                FlirtCardDetailsActivity.start(NearByPeopleActivity.this,data.get(pos).getAnchorId());
            }
        });
        p.getPeopleNearby(MapLocationUtil.getInstance().getLongitude(),MapLocationUtil.getInstance().getLatitude(),value2Age, value1Age, page,String.valueOf(sexA));
    }

    private void selectedRange() {
        FilterFlirtPopwindow filterFlirtPopwindow = new FilterFlirtPopwindow(this, TextUtils.isEmpty(value1Age)?0: Integer.parseInt(value1Age),TextUtils.isEmpty(value2Age)?0: Integer.parseInt(value2Age) , sexA);
        filterFlirtPopwindow.addOnClickListener((valueA, valueB, sex) -> {
            value1Age = String.valueOf(valueA);
            value2Age =  String.valueOf(valueB);
            sexA = sex;
            page =1;
            p.getPeopleNearby(MapLocationUtil.getInstance().getLongitude(), MapLocationUtil.getInstance().getLatitude(), value2Age, value1Age, page, String.valueOf(sex));
        });
        filterFlirtPopwindow.showPopupWindow();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_nearby_people;
    }


    @Override
    public void setNearByData(ArrayList<CityHost> records) {
        if (page==1){
            nearbyAdapter.setNewData(records);
            mRefresh.finishRefresh();
        }else{
            if (records.size()>0){
                nearbyAdapter.addData(records);
            }else{
                --page;
            }
            mRefresh.finishLoadMore();
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        p.getPeopleNearby(MapLocationUtil.getInstance().getLongitude(),MapLocationUtil.getInstance().getLatitude(),value2Age,value1Age,page,String.valueOf(sexA));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        p.getPeopleNearby(MapLocationUtil.getInstance().getLongitude(),MapLocationUtil.getInstance().getLatitude(),value2Age,value1Age,++page,String.valueOf(sexA));
    }
}
