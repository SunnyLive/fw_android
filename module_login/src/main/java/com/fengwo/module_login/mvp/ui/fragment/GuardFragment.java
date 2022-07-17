package com.fengwo.module_login.mvp.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseFragment;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.GuardDto;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/22
 */
public class GuardFragment extends BaseListFragment<GuardDto> {


    public static GuardFragment newInstance(int type) {
        Bundle args = new Bundle();
        GuardFragment fragment = new GuardFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(View v) {
        super.initView(v);
        SmartRefreshLayoutUtils.setClassicsColor(getActivity(), smartRefreshLayout, R.color.normal_bg, R.color.text_33);
    }

    @Override
    public Flowable setNetObservable() {
        int type = getArguments().getInt("type");
        if (type == 1) {
            return new RetrofitUtils().createApi(LoginApiService.class).getMyGuardList(page + PAGE_SIZE);
        } else {
            return new RetrofitUtils().createApi(LoginApiService.class).getGuardMeList(page + PAGE_SIZE);
        }
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_guard_list;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, GuardDto item, int position) {
        helper.setText(R.id.tv_nick, item.getGuardUserNickname());
        long time = TimeUtils.dealDateFormatTolong(item.getGuardDeadline());
        helper.setText(R.id.tv_duration, "剩" + TimeUtils.culSurplusDay(time) + "天");
        ImageLoader.loadImgFitCenter(helper.getView(R.id.iv_guard), item.getLevelIcon());
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.getGuardUserHeadImg());
    }

    @Override
    public String setEmptyContent() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_baselist_notitle;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

    }
}
