package com.fengwo.module_flirt.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;
import com.fengwo.module_chat.mvp.ui.activity.ChatCardActivityNew;
import com.fengwo.module_chat.mvp.ui.activity.chat_new.ChatSingleActivity;
import com.fengwo.module_chat.utils.ChatSingleMuchPopwindow;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.BitmapUtil;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ShareHelper;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_flirt.Interfaces.IFindView;
import com.fengwo.module_flirt.Interfaces.INearbyView;
import com.fengwo.module_flirt.Interfaces.NearByListener;
import com.fengwo.module_flirt.P.FindPresenter;
import com.fengwo.module_flirt.P.NearbyPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.FindDetailActivity;
import com.fengwo.module_flirt.UI.activity.FlirtCardDetailsActivity;
import com.fengwo.module_flirt.UI.activity.NearByPeopleActivity;
import com.fengwo.module_flirt.adapter.FindHeaderViewAdapter;
import com.fengwo.module_flirt.adapter.FindListAdapter;
import com.fengwo.module_flirt.adapter.NearbyAdapter;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.CoverDto;
import com.fengwo.module_flirt.bean.FindHeaderDto;
import com.fengwo.module_flirt.bean.FindListDto;
import com.fengwo.module_flirt.dialog.FindMuchPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShareCommonPopwindow;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 发现 列表
 * @Author BLCS
 * @Time 2020/3/26 17:34
 */
public class NearbyFragment extends BaseMvpFragment<IFindView, FindPresenter> implements NearByListener,IFindView {
    @BindView(R2.id.rv_include)
    RecyclerView mRv;
    @BindView(R2.id.ll_empty)
    LinearLayout llEmpty;

    private String max;
    private String min;
    private int page;
    private String sex;
    private String city = "全部";
    private FindHeaderViewAdapter headerAdapter;
    private FindListAdapter findListAdapter;
    private FindMuchPopwindow muchPopwindow;
    private FindListDto findListDto;

    @Override
    protected FindPresenter initPresenter() {
        return new FindPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_nearby;
    }
    @Override
    public void initUI(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        findListAdapter = new FindListAdapter(getActivity());
        mRv.setAdapter(findListAdapter);
        addHeaderView();
        findListAdapter.setOnItemClickListener((baseQuickAdapter, view1, pos) -> {
            if (isFastClick()) return;
            ArrayList<FindListDto> data = (ArrayList<FindListDto>) baseQuickAdapter.getData();
            int status = data.get(pos).getWenboLiveStatus();
            if (status==0){
                ChatCardActivityNew.start(getContext(),null, String.valueOf(data.get(pos).getId()), String.valueOf(data.get(pos).getCircleId()),0,0,0,0);
//                MineDetailActivity.startActivityWithUserId(getContext(), data.get(pos).getId());
            }else{
                FlirtCardDetailsActivity.start(getContext(),data.get(pos).getId());
            }

        });

        findListAdapter.setOnItemChildClickListener((baseQuickAdapter, view12, pos) -> {
            int id = view12.getId();
            findListDto = (FindListDto) baseQuickAdapter.getData().get(pos);
            if (id == R.id.iv_find_much) {
                    if (muchPopwindow==null){
                        muchPopwindow = new FindMuchPopwindow(getActivity());
                        muchPopwindow.addOnClickListener(new FindMuchPopwindow.OnMuchClickListener() {
                            @Override
                            public void attention(int uid,int pos) {
                                addtention(uid,pos);
                            }
                        });
                    }
                L.e("====1 "+ findListDto.getIsAttention());
                L.e("====2 "+ findListDto.getUserId());
                muchPopwindow.setAttention(findListDto.getIsAttention(), findListDto.getUserId(),pos);
                muchPopwindow.showPopupWindow();
            } else if (id == R.id.iv_find_like) {
                p.cardLike(findListDto.getId(),pos);
            } else if (id == R.id.tv_find_shares) {
                toastTip("分享");
                List<CoverDto> cover = findListDto.getCover();
                showShareDialog(findListDto.getId(),cover!=null &&cover.size()>0?cover.get(0).imageUrl:"",findListDto.getExcerpt());
            }
        });
        findListAdapter.bindToRecyclerView(mRv);
    }

    protected void showShareDialog(int id,String imgUrl,String content) {
        ShareCommonPopwindow shareCommonPopwindow = new ShareCommonPopwindow(getContext());
        shareCommonPopwindow.addClickListener(new ShareCommonPopwindow.OnShareClickListener() {
            @Override
            public void onWx() {
                p.getShareInfo(id,0,imgUrl,content);
            }

            @Override
            public void onWxCircle() {
                p.getShareInfo(id,1,imgUrl,content);
            }
        });
        shareCommonPopwindow.showPopupWindow();
    }
    private void addHeaderView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.header_find, null);
        RecyclerView rvHeader = inflate.findViewById(R.id.rv_find_header);
        TextView tvGo = inflate.findViewById(R.id.tv_find_go);
        rvHeader.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        headerAdapter = new FindHeaderViewAdapter();
        rvHeader.setAdapter(headerAdapter);
        tvGo.setOnClickListener(v -> {//进入附近的人 列表
            NearByPeopleActivity.start(getActivity());
        });
        findListAdapter.addHeaderView(inflate);
    }

    public void addtention(int uid,int pos){
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
                ToastUtils.showShort(getContext(),msg);
            }
        });
    }
    public NearByListener getListener() {
        return this;
    }

    @Override
    public void onRefrsh() {
        page = 1;
        if (p==null) initPresenter();
        if (p!=null) {
            p.getHeaderInfo();
            p.getFindList(page, city);
        }
    }

    @Override
    public void onLoadMore() {
        if (p==null) initPresenter();
        if (p!=null)
            p.getFindList(++page,city);
    }

    @Override
    public void getPeopleNearby(String longitude, String latitude,String max, String min, int page, String sex, String city) {
        this.max = max;
        this.min = min;
        this.page = page;
        this.sex = sex;
        this.city = city;
        if (p==null) initPresenter();
        if (p!=null){
            p.getHeaderInfo();
            p.getFindList(page,city);
        }
    }

    @Override
    public void getRequesHeadersSuccess(List<FindHeaderDto> data) {
        headerAdapter.setNewData(data);
    }

    @Override
    public void getRequestFindListSuccess(BaseListDto<FindListDto> data) {
        if(page==1){
            findListAdapter.setNewData(data.records);
            if (data.records.size()>0){
                llEmpty.setVisibility(View.GONE);
            }else{
                llEmpty.setVisibility(View.VISIBLE);
            }
        }else{
            if(data.records.size() > 0){
                findListAdapter.addData(data.records);
                llEmpty.setVisibility(View.GONE);
            }else{
                --page;
            }
        }
    }

    @Override
    public void cardLikeSuccess(int id, int position) {
        FindListDto bean = findListAdapter.getItem(position);
        if (bean.getIsLike()==1) {
            bean.setIsLike(0);
            bean.setLikes( bean.getLikes()- 1);
            findListAdapter.getData().set(position, bean);
            findListAdapter.setLike(false, position);
        } else {
            bean.setIsLike(1);
            bean.setLikes( bean.getLikes()+ 1);
            findListAdapter.getData().set(position, bean);
            findListAdapter.setLike(true, position);
        }
    }

    @Override
    public void getShareUrlSuccess(String url,int type,String imgUrl,String content) {
        ShareHelper.get().shareDynamic(getActivity(),url,"蜂窝互娱",imgUrl,content,type);
    }
}
