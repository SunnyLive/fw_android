package com.fengwo.module_flirt.UI.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.SystemMessageBean;
import com.fengwo.module_chat.mvp.ui.activity.ChatCardActivityNew;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_chat.base.SystemClearRefreshEvent;
import com.fengwo.module_login.mvp.ui.activity.MineCardDetailActivity;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.EventConstant;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 系统消息列表
 */
@Route(path = ArouterApi.SYSTEM_MESSAGE_HOME)
public class SystemMessageActivity extends BaseListActivity<SystemMessageBean> {

    @Autowired
    UserProviderService userProviderService;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void initView() {
        super.initView();
        compositeDisposable = new CompositeDisposable();

        //清空已读消息
        ChatGreenDaoHelper daoHelper = ChatGreenDaoHelper.getInstance();
        daoHelper.cleanUnreadCount(userProviderService.getUserInfo().id + "", EventConstant.system_event + "");

//        SmartRefreshLayoutUtils.setWhiteBlackText(this, findViewById(R.id.smartrefreshlayout));
//        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        new ToolBarBuilder().setTitle("发现消息")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .showBack(true)
                .setRight1Img(R.drawable.ic_clear_message, view -> {
                    CommonDialog.getInstance("提示", "确认清空系统消息吗", "取消", "确定", false)
                            .addOnDialogListener(new CommonDialog.OnDialogListener() {
                                @Override
                                public void cancel() {

                                }

                                @Override
                                public void sure() {
                                    compositeDisposable.add(new RetrofitUtils().createApi(ChatService.class)
                                            .clearSystemMessageList()
                                            .compose(RxUtils.applySchedulers2())
                                            .subscribeWith(new LoadingObserver<HttpResult>() {
                                                @Override
                                                public void _onNext(HttpResult data) {
                                                    RxBus.get().post(new SystemClearRefreshEvent());
                                                    finish();
//                                                    if (smartRefreshLayout != null) {
//                                                        recyclerView.scrollToPosition(0);
//                                                        smartRefreshLayout.autoRefresh();
//                                                    }
                                                }

                                                @Override
                                                public void _onError(String msg) {
                                                    toastTip(msg);
                                                }
                                            }));
                                }
                            }).show(getSupportFragmentManager(), "清空系统消息");
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
        return new RetrofitUtils().createApi(ChatService.class).getSystemMessageList(p);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_system_message_list;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, SystemMessageBean item, int position) {
        String content = item.content + "";
        TextView view = helper.getView(R.id.tv_content);
        view.setText(content);

        switch (item.type){
            case "0"://
                helper.setText(R.id.tv_reason, "涉及违规，请及时处理" );

                break;
            case "3"://
                helper.setText(R.id.tv_reason, "内容审核不通过已存为草稿" );
                break;
            case "4"://
                helper.setText(R.id.tv_reason, "涉及违规，内容已被封禁，请注意文明用户" );
                break;
        }


        LinearLayout ll_view = helper.getView(R.id.ll_view);
        helper.setText(R.id.tv_time, item.time);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.type){
                    case "3"://
                    case "0"://自己发的
//                        ARouter.getInstance().build(ArouterApi.CHAT_POST_TREND)
//                                .withInt("id",Integer.parseInt(item.targetId))
//                                .navigation(SystemMessageActivity.this,1000);
                        ARouter.getInstance().build(ArouterApi.MINE_DETAIL_CARD_ACTION)
                                .withInt(MineCardDetailActivity.CHAR_CARD_ID,Integer.parseInt(item.targetId))
                                .navigation();
                        break;
                    case "4":
                        Intent intent = new Intent(SystemMessageActivity.this, FindDetailActivity.class);
                        intent.putExtra("id", Integer.parseInt(item.targetId));
                        startActivity(intent);
                        break;
                }
            }
        });
        ll_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.type){
                    case "3"://
                    case "0"://自己发的
//                        ARouter.getInstance().build(ArouterApi.CHAT_POST_TREND)
//                                .withInt("id",Integer.parseInt(item.targetId))
//                                .navigation(SystemMessageActivity.this,1000);
                        ARouter.getInstance().build(ArouterApi.MINE_DETAIL_CARD_ACTION)
                                .withInt(MineCardDetailActivity.CHAR_CARD_ID,Integer.parseInt(item.targetId))
                                .navigation();
                        break;
                    case "4":
                        Intent intent = new Intent(SystemMessageActivity.this, FindDetailActivity.class);
                        intent.putExtra("id", Integer.parseInt(item.targetId));
                        startActivity(intent);
                        break;
                }
            }
        });

        SpannableStringBuilder style = new SpannableStringBuilder();
        //设置文字
        style.append(view.getText().toString());
//        style.setSpan(new ClickableSpan() {
//                          @Override
//                          public void onClick(@NonNull View widget) {
//
//
////                              KLog.e("tag","id="+item.id);
////                              //todo 根据item的类型做不同操作
////                              ChatCardActivityNew.start(SystemMessageActivity.this, null, item.targetId, "", 1, UserManager.getInstance().getUser().getId(), 0, 0);
//                          }
//
//                          @Override
//                          public void updateDrawState(@NonNull TextPaint ds) {
//                              ds.setUnderlineText(false);
//                          }
//                      },
//                content.length() - 6,
//                content.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#9966FF"));
        style.setSpan(foregroundColorSpan, 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //配置给TextView
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(style);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_baselist;
    }
}
