package com.fengwo.module_chat.mvp.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_chat.BuildConfig;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.base.RefreshAfterClearEvent;
import com.fengwo.module_chat.base.RefreshMessageListEvent;
import com.fengwo.module_chat.base.SystemClearRefreshEvent;
import com.fengwo.module_chat.entity.ChatListItemEntity;
import com.fengwo.module_chat.mvp.presenter.MessageListPresenter;
import com.fengwo.module_chat.mvp.ui.activity.ChatGroupInfoActivity;
import com.fengwo.module_chat.mvp.ui.adapter.MessageListAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IMessageListView;
import com.fengwo.module_chat.mvp.ui.event.ChangeAvatarEvent;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.GreetMessageEvent;
import com.fengwo.module_comment.event.NoDistrubingEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.GsonUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_websocket.EventConstant;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 消息
 * 1. i撩 2.0.1新增 发布动态被限制通知
 */
public class MessageHomeFragment extends BaseMvpFragment<IMessageListView, MessageListPresenter> implements IMessageListView, SwipeMenuCreator, OnItemMenuClickListener {

    @Autowired
    UserProviderService service;

    @BindView(R2.id.rv_message_list)
    SwipeRecyclerView recyclerView;

    private CompositeDisposable allRxBus;
    private MessageListAdapter memberAdapter;
    private String uid;

    @Override
    public void setMessageList(List<ChatListItemEntity> data) {
        if (data != null) {
            Collections.sort(data);
            memberAdapter.setNewData(data);
        }
    }

    @Override
    public void refreshMessageList() {
        onResume();
    }

    @Override
    protected MessageListPresenter initPresenter() {
        return new MessageListPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.message_home_fragment;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        uid = service.getUserInfo().fwId;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        memberAdapter = new MessageListAdapter();
        memberAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ChatListItemEntity item = (ChatListItemEntity) adapter.getData().get(position);
            if (item.getTalkUserId() == EventConstant.recent_visitor_event ||
                    item.getTalkUserId() == EventConstant.interact_event ||
                    item.getTalkUserId() == EventConstant.appoint_event ||
                    item.getTalkUserId() == EventConstant.system_event ||
                    item.getTalkUserId() == EventConstant.greet_event||
                    item.getTalkUserId() == EventConstant.official_news
            ) {
                switch (item.getTalkUserId()) {
                    case EventConstant.appoint_event:
                        ArouteUtils.toPathWithId(ArouterApi.MESSAGE_DATINGASSISTANTS);
                        break;
                    case EventConstant.recent_visitor_event:
                        ArouteUtils.toPathWithId(ArouterApi.RECENT_VISITOR);
                        break;
                    case EventConstant.interact_event:
                        ArouteUtils.toPathWithId(ArouterApi.INTERACT_HOME);
                        break;
                    case EventConstant.system_event:
                        ArouteUtils.toPathWithId(ArouterApi.SYSTEM_MESSAGE_HOME);
                        break;
                    case EventConstant.greet_event:
                        ArouteUtils.toPathWithId(ArouterApi.GREET_MESSAGE_LIST);
                        break;
                    case EventConstant.official_news:
                        ArouteUtils.toPathWithId(ArouterApi.OFFICIAL_MESSAGE_LIST);
                        break;
                }
            } else {
                if (TextUtils.isEmpty(item.getGroupId())) {
                    ArouteUtils.toChatSingleActivity(String.valueOf(item.getBelongUserId()),
                            String.valueOf(item.getTalkUserId()), item.getUserName(), item.getUserAvatar());
                } else {
                    ArouteUtils.toChatGroupActivity(String.valueOf(item.getBelongUserId()),
                            String.valueOf(item.getTalkUserId()), item.getGroupName(), item.getGroupAvatar());
                }
            }
        });


        //侧滑
        recyclerView.setSwipeMenuCreator(this);
        recyclerView.setOnItemMenuClickListener(this);
        recyclerView.setAdapter(memberAdapter);
        initDistrub();
        rxBus();
    }

    private void rxBus() {
        allRxBus = new CompositeDisposable();
        allRxBus.add( //收到通知请求消息列表
                RxBus.get().toObservable(RefreshMessageListEvent.class)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .compose(bindToLifecycle()).subscribe(o -> p.getMessageList(uid),
                        Throwable::printStackTrace));

        allRxBus.add( //更新 头像
                RxBus.get().toObservable(ChangeAvatarEvent.class).compose(bindToLifecycle())
                        .subscribe(event -> {
                            for (ChatListItemEntity item : memberAdapter.getData()) {
                                if (item.getTalkUserId() == Integer.parseInt(event.userId)) {
                                    item.setUserAvatar(event.avatar);
                                    memberAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }, Throwable::printStackTrace));
        allRxBus.add(  //消息免打扰
                RxBus.get().toObservable(NoDistrubingEvent.class)
                        .compose(bindToLifecycle()).subscribe(o -> {
                            initDistrub();
                        },
                        Throwable::printStackTrace));

        allRxBus.add(  //消息列表被清空
                RxBus.get().toObservable(RefreshAfterClearEvent.class)
                        .compose(bindToLifecycle()).subscribe(refreshAfterClearEvent -> {
                            //todo 目前只有私聊 和约会助手 被清空的情况
                            if (refreshAfterClearEvent.talkId.equals(EventConstant.appoint_event + "")) {
                                //造一条默认的约会助手消息
                                p.buildAppointMessage(uid);
                                return;
                            }
                            //将数据库的ChatListItem的message置空
                            for (int i = 0; i < memberAdapter.getData().size(); i++) {
                                if (memberAdapter.getData().get(i).getTalkUserId() == Integer.parseInt(refreshAfterClearEvent.talkId)) {
                                    p.setChatListItemEmpty(uid, Integer.parseInt(refreshAfterClearEvent.talkId));
                                    break;
                                }
                            }
                        },
                        Throwable::printStackTrace));

        allRxBus.add(
                RxBus.get().toObservable(SystemClearRefreshEvent.class)
                        .compose(bindToLifecycle()).subscribe(o -> {
                            //系统消息清空
                            p.deleteListItem(uid, EventConstant.system_event + "");
                            //打招呼消息清空
                            p.deleteListItem(uid, EventConstant.greet_event + "");
                            //官方消息清空
                            p.deleteListItem(uid, EventConstant.official_news + "");
                        },
                        Throwable::printStackTrace));

        allRxBus.add(  //打招呼
                RxBus.get().toObservable(GreetMessageEvent.class)
                        .compose(bindToLifecycle()).subscribe(o -> {
                            p.buildLocalInsertOrReplaceList(o);
                        },
                        Throwable::printStackTrace));
    }

    /**
     * 免打扰
     */
    private void initDistrub() {
        String groundIds = (String) SPUtils1.get(Objects.requireNonNull(getActivity()), ChatGroupInfoActivity.SP_NO_DISTURB, "");
        List<String> groupLists = GsonUtils.json2ObjList(groundIds, String.class);
        memberAdapter.setNoDistrubData(groupLists);
    }

    @Override
    public void onResume() {
        super.onResume();
        p.getMessageList(uid);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (allRxBus != null && allRxBus.size() > 0) {
            allRxBus.clear();
        }
    }

    @OnClick({R2.id.iv_clear_message, R2.id.fl_contact_home, R2.id.iv_service})
    public void onViewClicked(View view) {
        if (isFastClick()) return;
        int id = view.getId();
        if (id == R.id.iv_clear_message) {
            CommonDialog.getInstance("提示", "本操作将清除当前未读消息提示,确认进行此操作吗", "取消", "确定", false)
                    .addOnDialogListener(new CommonDialog.OnDialogListener() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void sure() {
                            p.clearAllUnReadList(uid);
                        }
                    }).show(getChildFragmentManager(), "清空未读消息");
        } else if (id == R.id.fl_contact_home) {
            ArouteUtils.toPathWithId(ArouterApi.CONTACT_HOME);
        } else if (id == R.id.iv_service) {
            StringBuilder url = new StringBuilder();
            //这个是测试环境
            if (BuildConfig.DEBUG) {
                url.append("http://h5test.fengwohuyu.com/user/");
                //这个是dev
                //url.append("https://h5szdev.fengwohuyu.com/user/");
            } else {
                url.append("http://h5customer.fwhuyu.com/user/");
            }
            url.append(service.getToken());
            url.append("/route/customer");
            BrowserActivity.start(getContext(), "", url.toString());
        }
    }

    @Override
    public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {
        int direction = menuBridge.getDirection();
        if (direction != SwipeRecyclerView.RIGHT_DIRECTION) return;
        int menuPosition = menuBridge.getPosition();
        if (menuPosition == 0) {
            p.topMessage(uid, Objects.requireNonNull(memberAdapter.getItem(adapterPosition)).getTalkUserId() + "");
        }
        if (menuPosition == 1) {
            CommonDialog.getInstance("", "刪除该聊天", "取消", "确定", false)
                    .addOnDialogListener(new CommonDialog.OnDialogListener() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void sure() {
                            p.deleteListItem(uid, Objects.requireNonNull(memberAdapter.getItem(adapterPosition)).getTalkUserId() + "");
                        }
                    }).show(getChildFragmentManager(), "确定该聊天");
        }

        menuBridge.closeMenu();
    }

    @Override
    public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
        ChatListItemEntity item = memberAdapter.getItem(position);
        assert item != null;
        if (item.getTalkUserId() == EventConstant.appoint_event ||
                item.getTalkUserId() == EventConstant.interact_event ||
                item.getTalkUserId() == EventConstant.recent_visitor_event ||
                item.getTalkUserId() == EventConstant.system_event ||
                item.getTalkUserId() == EventConstant.greet_event) {
            addMenuItem(item.getTopTime() > 0 ? "取消置顶" : "置顶", R.color.text_66, rightMenu);
        } else {
            addMenuItem(item.getTopTime() > 0 ? "取消置顶" : "置顶", R.color.text_66, rightMenu);
            addMenuItem("删除", R.color.red_ff3333, rightMenu);
        }
    }

    private void addMenuItem(String text, int bgColor, SwipeMenu rightMenu) {
        int width = DensityUtils.dp2px(Objects.requireNonNull(getActivity()), 60);
        SwipeMenuItem item = new SwipeMenuItem(getActivity())
                .setBackgroundColor(getResources().getColor(bgColor))
                .setWidth(width)
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                .setText(text)
                .setTextSize(13)
                .setTextColor(Color.WHITE);
        rightMenu.addMenuItem(item);
    }
}
