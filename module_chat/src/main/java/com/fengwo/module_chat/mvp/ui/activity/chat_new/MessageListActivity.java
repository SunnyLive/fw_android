package com.fengwo.module_chat.mvp.ui.activity.chat_new;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.base.RefreshMessageListEvent;
import com.fengwo.module_chat.entity.ChatListItemEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.presenter.MessageListPresenter;
import com.fengwo.module_chat.mvp.ui.activity.ChatGroupInfoActivity;
import com.fengwo.module_chat.mvp.ui.adapter.MessageListAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IMessageListView;
import com.fengwo.module_chat.mvp.ui.dialog.TranspondDialog;
import com.fengwo.module_chat.mvp.ui.event.ChangeAvatarEvent;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.event.NoDistrubingEvent;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.GsonUtils;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.widget.AppTitleBar;
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
import io.reactivex.disposables.CompositeDisposable;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

@Route(path = ArouterApi.MESSAGE_LIST)
public class MessageListActivity extends BaseMvpActivity<IMessageListView, MessageListPresenter>
        implements IMessageListView, SwipeMenuCreator, OnItemMenuClickListener {
    public static int TYPE_LIST = 0;
    public static int TYPE_TRANSPOND = 1;
    @BindView(R2.id.recyclerview)
    SwipeRecyclerView recyclerView;
    @BindView(R2.id.title)
    AppTitleBar titleBar;
    @BindView(R2.id.ll_search)
    LinearLayout llSearch;
    @BindView(R2.id.btn_search)
    TextView btnSearch;
    @BindView(R2.id.et_search)
    EditText etSearch;

    private String uid;
    private int type = TYPE_LIST;
    private CompositeDisposable allRxBus;

    //    int unReadCount = 0;
    public static void start(Context context, String uid) {
        Intent intent = new Intent(context, MessageListActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    private MessageListAdapter memberAdapter;

    @Override
    public MessageListPresenter initPresenter() {
        return new MessageListPresenter();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        uid = getIntent().getStringExtra("uid");
        type = getIntent().getIntExtra("type", TYPE_LIST);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter = new MessageListAdapter();
        if (type == TYPE_LIST) titleBar.setTitle("我的消息");
        else titleBar.setTitle("请选择转发对象");

        if (type != TYPE_TRANSPOND) {
            recyclerView.setSwipeMenuCreator(this);
            recyclerView.setOnItemMenuClickListener(this);
            memberAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                ChatListItemEntity item = (ChatListItemEntity) adapter.getData().get(position);
                if (item.getTalkUserId() == EventConstant.recent_visitor_event ||
                        item.getTalkUserId() == EventConstant.interact_event ||
                        item.getTalkUserId() == EventConstant.appoint_event ||
                        item.getTalkUserId() == EventConstant.system_event ||
                        item.getTalkUserId() == EventConstant.greet_event) {
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
        } else {
            titleBar.setMoreIcon(R.drawable.ic_seach_social);
            titleBar.setMoreClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llSearch.setVisibility(View.VISIBLE);
                    titleBar.setBackVisible(false);
                    titleBar.setMoreVisible(false);
                    titleBar.setTitle("");
                }
            });
            btnSearch.setOnClickListener(v -> {
                llSearch.setVisibility(View.GONE);
                p.getMessageList(uid);
                titleBar.setBackVisible(true);
                titleBar.setMoreVisible(true);
                titleBar.setTitle("请选择转发对象");
                KeyBoardUtils.closeKeybord(etSearch,this);
            });
            //点击搜索
            etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        p.getSearchMessageList(uid,etSearch.getText().toString());
                        KeyBoardUtils.closeKeybord(etSearch,MessageListActivity.this);
                        return true;
                    }
                    return false;
                }
            });

            memberAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                ChatMsgEntity data = (ChatMsgEntity) getIntent().getSerializableExtra("data");
                ChatListItemEntity entity1 = (ChatListItemEntity) adapter.getData().get(position);
                //弹窗
                TranspondDialog transpondDialog = new TranspondDialog(this);
                transpondDialog.setContent(entity1.getUserAvatar(),entity1.getUserName(),data.getText());
                transpondDialog.addOnClickListener(new TranspondDialog.OnClickListener() {
                    @Override
                    public void clickCancel() {
                        finish();
                    }
                    @Override
                    public void clickOk() {
                        ChatListItemEntity entity = memberAdapter.getData().get(position);
                        Intent intent = new Intent();
                        intent.putExtra("data", entity);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                transpondDialog.showPopupWindow();
            });
        }

        recyclerView.setAdapter(memberAdapter);
        initDistrub();
        rxBus();
    }

    public void rxBus() {
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
    }

    /**
     * 免打扰
     */
    private void initDistrub() {
        String groundIds = (String) SPUtils1.get(this, ChatGroupInfoActivity.SP_NO_DISTURB, "");
        List<String> groupLists = GsonUtils.json2ObjList(groundIds, String.class);
        memberAdapter.setNoDistrubData(groupLists);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_message;
    }

    @Override
    public void onResume() {
        super.onResume();
        p.getMessageList(uid);
    }

    @Override
    public void setMessageList(List<ChatListItemEntity> data) {
        if (data != null) {
            Collections.sort(data);
            if (type == TYPE_TRANSPOND) {
                data = StreamSupport.stream(data)
                        .filter(e -> e.getTalkUserId() != EventConstant.appoint_event)
                        .filter(e -> e.getTalkUserId() != EventConstant.recent_visitor_event)
                        .filter(e -> e.getTalkUserId() != EventConstant.interact_event)
                        .filter(e -> e.getTalkUserId() != EventConstant.system_event)
                        .filter(e -> e.getTalkUserId() != EventConstant.greet_event)
                        .collect(Collectors.toList());
            }
            memberAdapter.setNewData(data);

        }
    }

    @Override
    public void refreshMessageList() {
        onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (allRxBus != null && allRxBus.size() > 0) {
            allRxBus.clear();
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
            p.deleteListItem(uid, Objects.requireNonNull(memberAdapter.getItem(adapterPosition)).getTalkUserId() + "");
        }

        menuBridge.closeMenu();
    }

    @Override
    public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
        ChatListItemEntity item = memberAdapter.getItem(position);
        assert item != null;
        addMenuItem(item.getTopTime() > 0 ? "取消置顶" : "置顶", R.color.text_66, rightMenu);
        addMenuItem("删除", R.color.red_ff3333, rightMenu);
    }

    private void addMenuItem(String text, int bgColor, SwipeMenu rightMenu) {
        int width = DensityUtils.dp2px(Objects.requireNonNull(this), 60);
        SwipeMenuItem item = new SwipeMenuItem(this)
                .setBackgroundColor(getResources().getColor(bgColor))
                .setWidth(width)
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                .setText(text)
                .setTextSize(13)
                .setTextColor(Color.WHITE);
        rightMenu.addMenuItem(item);
    }
}