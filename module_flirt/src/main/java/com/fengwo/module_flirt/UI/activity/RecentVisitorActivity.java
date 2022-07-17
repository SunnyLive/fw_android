package com.fengwo.module_flirt.UI.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.VisitorRecordBean;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DateUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.widget.RoundImageView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_live_vedio.mvp.ui.activity.LivingRoomActivity;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.fengwo.module_websocket.EventConstant;

import java.util.ArrayList;

import io.reactivex.Flowable;

/**
 * 最近访客
 */
@Route(path = ArouterApi.RECENT_VISITOR)
public class RecentVisitorActivity extends BaseListActivity<VisitorRecordBean> {

    @Autowired
    UserProviderService userProviderService;

    @Override
    protected void initView() {
        super.initView();

        //清空已读消息
        ChatGreenDaoHelper daoHelper = ChatGreenDaoHelper.getInstance();
        daoHelper.cleanUnreadCount(userProviderService.getUserInfo().id + "", EventConstant.recent_visitor_event + "");

        SmartRefreshLayoutUtils.setWhiteBlackText(this, findViewById(R.id.smartrefreshlayout));
        new ToolBarBuilder().setTitle("最近访客").setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .showBack(true).build();
        setTitleBackground(getResources().getColor(R.color.white));
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(ChatService.class).getVisitorRecordList(p);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_recent_visitor_list;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, VisitorRecordBean item, int position) {
        RoundImageView avator = helper.getView(R.id.civ_head);
        ImageLoader.loadRouteImg(avator, item.headImg, 8);
        helper.setText(R.id.tv_name, item.nickname);

        //时间
        helper.setText(R.id.tv_visitor_create_time, item.lastTime);
        //访问数
        helper.setText(R.id.tv_visitor_num, item.vistorNum + "次");

        //年龄
        TextView tvAge = helper.getView(R.id.tv_age);
        tvAge.setVisibility(View.VISIBLE);
        tvAge.setText(TextUtils.isEmpty(item.age) ? "" : item.age);
        if (TextUtils.isEmpty(item.age))
            tvAge.setCompoundDrawablePadding(0);
        else
            tvAge.setCompoundDrawablePadding(4);

        //性别icon
        if (item.sex == 2) {
            tvAge.setEnabled(true);
        } else if (item.sex == 1) {
            tvAge.setEnabled(false);
        } else {
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            if (!TextUtils.isEmpty(item.age)) {
                tvAge.setBackgroundResource(R.drawable.shape_corner_boy);
            } else
                //无性别 无年龄
                tvAge.setVisibility(View.GONE);
        }

        helper.getView(R.id.civ_head_root).setOnClickListener(view -> {
            MineDetailActivity.startActivityWithUserId(RecentVisitorActivity.this, adapter.getData().get(position).userId);
        });

        ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_baselist;
    }
}
