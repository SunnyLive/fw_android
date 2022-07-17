package com.fengwo.module_login.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.RefreshCardList;
import com.fengwo.module_comment.event.RefreshCardNum;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.chat.ChatTimeUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.fengwo.module_login.utils.UserManager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class OldMineCardFragment extends BaseListFragment<CircleListBean> {
    private final static String UID = "uid";
    private final static String ZAN = "iszan";

    public static OldMineCardFragment newInstance(int uid, boolean isZan) {

        Bundle args = new Bundle();
        args.putInt(UID, uid);
        args.putBoolean(ZAN, isZan);
        OldMineCardFragment fragment = new OldMineCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setData(List<CircleListBean> datas, int page) {
        super.setData(datas, page);
//        RxBus.get().post(new RefreshNum(datas.size()));
    }

    private int uid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        uid = getArguments().getInt(UID, -1);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getCardByUser(p, uid + "");
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.chat_item_user_trends;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, CircleListBean item, int position) {
        Group group = helper.getView(R.id.groupChat);
//        ImageView civHeader = helper.getView(R.id.civChat);
        ImageView ivPoster = helper.getView(R.id.ivChatHeader);
        TextView tvTitle = helper.getView(R.id.tvChat);
        View location = helper.getView(R.id.view_location);
        TextView tvLocation = helper.getView(R.id.tvLocation);
        ImageView ivIsLike = helper.getView(R.id.ivChatAgree);
        ivIsLike.setImageResource(item.isLike == 0 ? R.drawable.chat_ic_home_agree_normal : R.drawable.chat_ic_home_agree_selected);
        helper.setVisible(R.id.iv_mine_card_secret,item.powerStatus == 1);
        if (item.isAd == 1) {
            ViewGroup.LayoutParams lp = ivPoster.getLayoutParams();
            lp.height = DensityUtils.dp2px(getActivity(), 129);
            ImageLoader.loadImg(ivPoster, item.adImage);
            ivPoster.setLayoutParams(lp);
        } else {
            ViewGroup.LayoutParams lp = ivPoster.getLayoutParams();
            lp.height = DensityUtils.dp2px(getActivity(), 206);
            ivPoster.setLayoutParams(lp);
            ImageLoader.loadImg(ivPoster, item.cover);
        }
        if (!TextUtils.isEmpty(item.topTime)|| item.cardStatus == 0|| item.cardStatus == 4|| item.cardStatus == 5){
            helper.setVisible(R.id.ll_card_status,true);
        }else {
            helper.setVisible(R.id.ll_card_status,false);
        }
        if (!TextUtils.isEmpty(item.topTime)) {
            helper.setImageResource(R.id.iv_mine_card_status, R.drawable.ic_set_top);
            helper.setText(R.id.tv_card_status, "置顶");
//        } else if (item.powerStatus == 1) {
//            helper.setImageResource(R.id.iv_mine_card_status, R.drawable.ic_private);
//            helper.setText(R.id.tv_card_status, "私密");
        } else {
            switch (item.cardStatus) {//0审核中，1成功，2私密，3封禁，4草稿，5拒审
                case 0: //审核中
                    helper.setImageResource(R.id.iv_mine_card_status, R.drawable.ic_white_user);
                    helper.setText(R.id.tv_card_status, "审核中");
                    break;
                case 4:
                case 5:
                    helper.setImageResource(R.id.iv_mine_card_status, R.drawable.ic_caogao);
                    helper.setText(R.id.tv_card_status, "草稿");
                    break;
                case 2:
                    helper.setVisible(R.id.iv_mine_card_secret,true);
                    break;
            }
        }
        location.setVisibility(TextUtils.isEmpty(item.position) ? View.INVISIBLE : View.VISIBLE);
        tvLocation.setText(item.position);
        tvTitle.setText(item.excerpt);
        tvTitle.setVisibility(TextUtils.isEmpty(item.excerpt) ? View.GONE : View.VISIBLE);
        group.setVisibility(TextUtils.isEmpty(item.nickname) ? View.GONE : View.VISIBLE);
        helper.setText(R.id.tvChatNum, String.valueOf(item.likes));
        helper.setText(R.id.tv_comment_num,item.comments+"");
        helper.setText(R.id.tv_time, ChatTimeUtils.getFlirtCardTime(ChatTimeUtils.getTime(item.createTime)));
//        ImageLoader.loadImg(civHeader, item.headImg);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArouteUtils.toCardHome(getData(), item.id, "0", 1, uid, 0,0);
            }
        });
        helper.itemView.setOnLongClickListener(v -> {
            assert getFragmentManager() != null;
            if (item.userId.equals(""+UserManager.getInstance().getUser().id)) {
                CommonDialog.getInstance("", "确定删除动态吗?", "取消", "确定", false)
                        .addOnDialogListener(new CommonDialog.OnDialogListener() {
                            @Override
                            public void cancel() {

                            }

                            @SuppressLint("CheckResult")
                            @Override
                            public void sure() {
                                Map map = new HashMap();
                                map.put("cardId", item.id);
                                String sBody = new Gson().toJson(map);
                                RequestBody body = RequestBody.create(sBody, MediaType.parse("application/json"));
                                new RetrofitUtils().createApi(LoginApiService.class)
                                        .deleteMineCard(body)
                                        .compose(io_main())
                                        .subscribeWith(new LoadingObserver<HttpResult>() {
                                            @Override
                                            public void _onNext(HttpResult data) {
                                                if (data.isSuccess()) {
                                                    onRefresh();
                                                    RxBus.get().post(new RefreshCardNum());
                                                }
                                            }

                                            @Override
                                            public void _onError(String msg) {

                                            }
                                        });
                            }
                        }).show(getFragmentManager(), "删除动态");
            }
            return true;
        });
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        ((MineDetailActivity)getActivity()).reFreshData();
    }

    @Override
    public String setEmptyContent() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_baselist_notitle;
    }

    @SuppressLint("CheckResult")
    @Override
    public void initUI(Bundle savedInstanceState) {
        SmartRefreshLayoutUtils.setWhiteBlackText(getActivity(), smartRefreshLayout);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        RxBus.get().toObservable(RefreshCardList.class).compose(bindToLifecycle())
                .subscribe(refreshCardList -> {
                    if (smartRefreshLayout != null) {
                        recyclerView.scrollToPosition(0);
                        smartRefreshLayout.autoRefresh();
                    }
                });
    }
}
