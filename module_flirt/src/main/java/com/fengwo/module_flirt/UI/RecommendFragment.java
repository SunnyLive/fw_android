package com.fengwo.module_flirt.UI;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_flirt.Interfaces.IVideoDatingView;
import com.fengwo.module_flirt.Interfaces.RecommendListener;
import com.fengwo.module_flirt.P.VideoDatingPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.FlirtCardDetailsActivity;
import com.fengwo.module_flirt.adapter.VideoDatingAdapter;
import com.fengwo.module_flirt.bean.CityHost;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 推荐交友 列表
 *
 * @Author BLCS
 * @Time 2020/3/26 17:33
 */
public class RecommendFragment extends BaseMvpFragment<IVideoDatingView, VideoDatingPresenter> implements BaseQuickAdapter.OnItemClickListener, IVideoDatingView, RecommendListener {
    @BindView(R2.id.rv_include)
    RecyclerView mRv;
    @BindView(R2.id.sr_refresh)
    SmartRefreshLayout mRefresh;
    private VideoDatingAdapter mAdapter;
    private String location = "全部";
    private int page;
    private static final String KEY_POS = "KEY";
    private int status = 0; //1 在线交友   0 推荐
    private String tabelName;
    @Override
    protected VideoDatingPresenter initPresenter() {
        return new VideoDatingPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.include_recyclerview;
    }

    public static RecommendFragment getInstance(int pos) {
        RecommendFragment RecommendFragment = new RecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POS, pos);
        RecommendFragment.setArguments(bundle);
        return RecommendFragment;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        int anInt = getArguments().getInt(KEY_POS, 0);
        mRefresh.setEnableRefresh(false);
        mRefresh.setEnableLoadMore(false);
        initRv();
        if (p==null) initPresenter();
        if (p!=null) p.getVideoDating(location, 1, anInt, "");
    }

    private void initRv() {
        mRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new VideoDatingAdapter();
        mRv.setAdapter(mAdapter);
        View view = getLayoutInflater().inflate(R.layout.item_base_empty_view, null);
        mAdapter.setEmptyView(view);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int pos) {
        if (isFastClick()) return;
        FlirtCardDetailsActivity.start(getContext(), (ArrayList<CityHost>) baseQuickAdapter.getData(), pos);
    }

    public RecommendListener getListener() {
        return this;
    }

    @Override
    public void onRefrsh() {
        page = 1;
        L.e("location" + location);
        L.e("status " + status);
        if (!TextUtils.isEmpty(tabelName)&&tabelName.equals("全部")){
            tabelName = "";
        }
        if (p==null) initPresenter();
        if (p!=null) p.getVideoDating(location, page, status, tabelName);
    }

    @Override
    public void onLoadMore() {
        if (!TextUtils.isEmpty(tabelName)&&tabelName.equals("全部")){
            tabelName = "";
        }
        if (p==null) initPresenter();
        if (p!=null)
            p.getVideoDating(location, ++page, status, tabelName);
    }

    @Override
    public void getVideoDating(String location, int page, int status, String tabelName) {
        //去掉位置最后一个字 防止传厦门市 服务端匹配不到 厦门 这样的位置
        if (location.length() > 1) {

            this.status = status;
            this.tabelName = tabelName;
            this.location = location;
            this.page = page;
            String realtabelName = tabelName.equals("全部") ? "" : tabelName;
            if (p==null) initPresenter();
            if (p!=null)
            p.getVideoDating(location, page, status, realtabelName);
        }
    }

    @Override
    public void setData(ArrayList<CityHost> records) {
        if (page == 1) {
            mAdapter.getData().clear();
            mAdapter.setNewData(records);
        } else {
            mAdapter.addData(records);
        }
        if (!TextUtils.isEmpty(tabelName)){
            mAdapter.setTableName(tabelName);
        }
    }
}
