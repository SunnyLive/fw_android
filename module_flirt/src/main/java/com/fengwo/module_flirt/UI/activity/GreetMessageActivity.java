package com.fengwo.module_flirt.UI.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.base.GreetClearRefreshEvent;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.GreetMessageBean;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.FastClickListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_websocket.EventConstant;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

@Route(path = ArouterApi.GREET_MESSAGE_LIST)
public class GreetMessageActivity extends BaseListActivity<GreetMessageBean> {

    @Autowired
    UserProviderService userProviderService;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void initView() {
        super.initView();
        compositeDisposable = new CompositeDisposable();

        //清空已读消息
        ChatGreenDaoHelper daoHelper = ChatGreenDaoHelper.getInstance();
        daoHelper.cleanUnreadCount(userProviderService.getUserInfo().id + "", EventConstant.greet_event + "");

//        SmartRefreshLayoutUtils.setWhiteBlackText(this, findViewById(R.id.smartrefreshlayout));
//        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        new ToolBarBuilder().setTitle("收到的招呼")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .showBack(true)
                .setRight1Img(R.drawable.ic_clear_message, view -> {
                    CommonDialog.getInstance("提示", "确认清空收到的招呼吗", "取消", "确定", false)
                            .addOnDialogListener(new CommonDialog.OnDialogListener() {
                                @Override
                                public void cancel() {

                                }

                                @Override
                                public void sure() {
                                    compositeDisposable.add(new RetrofitUtils().createApi(ChatService.class)
                                            .clearGreetMessageList()
                                            .compose(RxUtils.applySchedulers2())
                                            .subscribeWith(new LoadingObserver<HttpResult>() {
                                                @Override
                                                public void _onNext(HttpResult data) {
                                                    RxBus.get().post(new GreetClearRefreshEvent());
                                                    finish();
                                                }

                                                @Override
                                                public void _onError(String msg) {
                                                    toastTip(msg);
                                                }
                                            }));
                                }
                            }).show(getSupportFragmentManager(), "清空收到的招呼");
                })
                .build();
        setTitleBackground(getResources().getColor(R.color.white));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable == null) return;
        compositeDisposable.isDisposed();
        compositeDisposable.clear();
    }

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(ChatService.class).getGreetMessageList(p);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_greet_message_list;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, GreetMessageBean item, int position) {
        CircleImageView avator = helper.getView(R.id.ivHeader);
        ConstraintLayout root= helper.getView(R.id.root);
        ImageLoader.loadRouteImg(avator, item.headImg, 8);
        helper.setText(R.id.tvName, item.nickname);
        helper.setText(R.id.tvDate, item.time);
        helper.setText(R.id.tvContent, item.content);

        //年龄
        TextView tvAge = helper.getView(R.id.tv_age);
        tvAge.setVisibility(View.VISIBLE);
        tvAge.setTextColor(Color.WHITE);
        tvAge.setText(TextUtils.isEmpty(item.age) ? "" : item.age);
        if (TextUtils.isEmpty(item.age))
            tvAge.setCompoundDrawablePadding(0);
        else
            tvAge.setCompoundDrawablePadding(4);

        //性别icon
        if (item.sex == 2) {
            tvAge.setBackgroundResource(R.drawable.shape_corner_girl);
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_girl, 0, 0, 0);
        } else if (item.sex == 1) {
            tvAge.setBackgroundResource(R.drawable.shape_corner_boy);
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_boy, 0, 0, 0);
        } else {
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            if (!TextUtils.isEmpty(item.age)) {
                tvAge.setBackgroundResource(R.drawable.shape_corner_boy);
            } else
                //无性别 无年龄
                tvAge.setVisibility(View.GONE);
        }

        root.setOnClickListener(new FastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                ArouteUtils.toGreetChatSingleActivity(userProviderService.getUserInfo().fwId, String.valueOf(item.userId),
                        item.nickname, item.content, item.headImg);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_baselist_white;
    }
}