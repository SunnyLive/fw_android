package com.fengwo.module_flirt.UI.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.BuildConfig;
import com.fengwo.module_chat.base.GreetClearRefreshEvent;
import com.fengwo.module_chat.base.OffcialBean;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.GreetMessageBean;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.FastClickListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.IliaoBean;
import com.fengwo.module_websocket.EventConstant;
import com.umeng.commonsdk.debug.I;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.RequestBody;

import static com.fengwo.module_comment.base.RxHttpUtil.addNet;
import static com.fengwo.module_comment.utils.HttpUtils.createRequestBody;

@Route(path = ArouterApi.OFFICIAL_MESSAGE_LIST)
public class OfficialMessageActivity extends BaseListActivity<OffcialBean> {

    @Autowired
    UserProviderService userProviderService;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void initView() {
        super.initView();
        compositeDisposable = new CompositeDisposable();

        //清空已读消息
        ChatGreenDaoHelper daoHelper = ChatGreenDaoHelper.getInstance();
        daoHelper.cleanUnreadCount(userProviderService.getUserInfo().id + "", EventConstant.official_news + "");

//        SmartRefreshLayoutUtils.setWhiteBlackText(this, findViewById(R.id.smartrefreshlayout));
//        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        new ToolBarBuilder().setTitle("蜂窝官方")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .setRight1Img(R.drawable.icon_service, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder url = new StringBuilder();
                        //这个是测试环境
                        if (BuildConfig.DEBUG) {
                            url.append("http://h5test.fengwohuyu.com/user/");
                            //这个是dev
                            //url.append("https://h5szdev.fengwohuyu.com/user/");
                        } else {
                            url.append("http://h5customer.fwhuyu.com/user/");
                        }
                        url.append(userProviderService.getToken());
                        url.append("/route/customer");
                        BrowserActivity.start(OfficialMessageActivity.this, "", url.toString());
                    }
                })
                .showBack(true)
                .setRight2Img(R.drawable.ic_clear_message, view -> {
                    CommonDialog.getInstance("提示", "确定清空当前所有消息？", "取消", "确定", false)
                            .addOnDialogListener(new CommonDialog.OnDialogListener() {
                                @Override
                                public void cancel() {

                                }

                                @Override
                                public void sure() {
                                    new RetrofitUtils().createApi(ChatService.class)
                                            .clearMarkMessageList()
                                            .compose(RxUtils.applySchedulers())
                                            .subscribe(new LoadingObserver<HttpResult>() {
                                                @Override
                                                public void _onNext(HttpResult data) {
                                                    if (data.isSuccess()) {
                                                        addNet(daoHelper.deleteListItem(userProviderService.getUserInfo().id + "", EventConstant.official_news+"").subscribe(aBoolean -> {
                                                            if (aBoolean)
                                                                RxBus.get().post(new GreetClearRefreshEvent());
                                                            finish();
                                                        }));

                                                    }
                                                }

                                                @Override
                                                public void _onError(String msg) {
                                                    toastTip(msg);
                                                }
                                            });


                                }
                            }).show(getSupportFragmentManager(), "tips_clean");
                })
                .build();
        setTitleBackground(getResources().getColor(R.color.white));


        new RetrofitUtils().createApi(ChatService.class)
                .officialMarkPost()
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {


                    }

                    @Override
                    public void _onError(String msg) {
                        toastTip(msg);
                    }
                });


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
        RequestBody build = new WenboParamsBuilder()
                .put("current", page + "")
                .build();
        return new RetrofitUtils().createWenboApi(ChatService.class).officialNewPost(build);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_offcial_message_list;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, OffcialBean item, int position) {
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tv_context, item.getSubTitle());
        //  helper.setText(R.id.tv_time, item.getSendTime());
        helper.setText(R.id.tv_time, TimeUtils.formatDateByMessage(item.getSendTimestamp()));
        if (TextUtils.isEmpty(item.getContent())) {
            helper.setGone(R.id.ll_lin, false);
            helper.setGone(R.id.ll_info, false);
        } else {
            helper.setGone(R.id.ll_lin, true);
            helper.setGone(R.id.ll_info, true);
        }
        LinearLayout ll_view = helper.getView(R.id.ll_view);
        ll_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(item.getContent())) {
                    HtmlActivity.Companion.forward(OfficialMessageActivity.this, item.getContent(), item.getTitle());
                }
            }
        });


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_baselist;
    }
}