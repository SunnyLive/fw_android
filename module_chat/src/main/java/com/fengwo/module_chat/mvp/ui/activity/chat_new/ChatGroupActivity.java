package com.fengwo.module_chat.mvp.ui.activity.chat_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.base.RefreshGroupSettingEvent;
import com.fengwo.module_chat.mvp.model.bean.BubbleBean;
import com.fengwo.module_chat.mvp.model.bean.EnterGroupModel;
import com.fengwo.module_chat.mvp.ui.activity.BubbleRecommendActivity;
import com.fengwo.module_chat.mvp.ui.activity.ChatGroupInfoActivity;
import com.fengwo.module_chat.mvp.ui.adapter.ChatGroupResAdapter;
import com.fengwo.module_chat.mvp.ui.event.ChatGroupResRefreshEvent;
import com.fengwo.module_chat.mvp.ui.event.GroupForbiddenEvent;
import com.fengwo.module_chat.mvp.ui.event.GroupShowBubbleEvent;
import com.fengwo.module_comment.widget.GridItemDecoration;
import com.fengwo.module_chat.widgets.QiqiuView;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.widget.AppTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

@Route(path = ArouterApi.CHAT_GROUP)
public class ChatGroupActivity extends BaseChatActivity {

    private static final int REQUEST_GROUP_MANAGER = 1001;

    @BindView(R2.id.titleBar)
    AppTitleBar titleBar;

    @BindView(R2.id.rv_res)
    RecyclerView rvRes;
    @BindView(R2.id.status_expand)
    ImageView ivResStatus;
    @BindView(R2.id.qiqiu)
    QiqiuView qiqiuView;

    @Autowired
    UserProviderService userProviderService;

    LinearLayout resView;
    private ChatGroupResAdapter resAdapter;
    private String groupId;
    private String groupName;
    private EnterGroupModel groupInfo;

    @Override
    public String setFromId() {
        return getIntent().getStringExtra("fromUid");
    }

    @Override
    public String setToId() {
        return getIntent().getStringExtra("toUid");
    }

    @Override
    public void sendText(CharSequence text) {
        p.sendTxtMessage(text.toString());
    }

    @Override
    public void sendImage(String url, int width, int height) {
        p.sendImageMessage(url, width, height);
    }

    @Override
    public void sendAudio(String path, int duration) {
        p.sendAudio(path, duration);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        super.initView();
        resView = findViewById(R.id.view_res);
        resAdapter = new ChatGroupResAdapter();
        resAdapter.setOnItemClickListener((adapter, view, position) -> {
            EnterGroupModel.GroupInfoModel model = resAdapter.getData().get(position);
            String realUrl = String.format("%s&token=%s", model.merchantUrl, userProviderService.getToken());
//            WebActivity.startWithTitleUrl(this, model.merchantName, realUrl);
            BrowserActivity.start(this, model.merchantName, realUrl);
//            WebViewActivity.start(this, model.merchantName, realUrl);
        });
        rvRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvRes.addItemDecoration(new GridItemDecoration(this, 10));
        rvRes.setAdapter(resAdapter);

        qiqiuView.setOnItemClickListener((bubbleId, name) -> {
            BubbleRecommendActivity.start(this, bubbleId, name);
        });
        RxBus.get().toObservable(GroupShowBubbleEvent.class).compose(bindToLifecycle()).subscribe(new Consumer<GroupShowBubbleEvent>() {
            @Override
            public void accept(GroupShowBubbleEvent groupShowBubbleEvent) throws Exception {
                BubbleBean bubbleBean = new BubbleBean(groupShowBubbleEvent.id, groupShowBubbleEvent.bubbleName);
                qiqiuView.addQipao(bubbleBean);
            }
        });
        RxBus.get().toObservable(RefreshGroupSettingEvent.class).compose(bindToLifecycle()).subscribe(new Consumer<RefreshGroupSettingEvent>() {
            @Override
            public void accept(RefreshGroupSettingEvent refreshGroupSettingEvent) throws Exception {
                //隐藏群设置
                if (titleBar==null) return;
                titleBar.setMoreVisible(false);
            }
        });
        RxBus.get().toObservable(ChatGroupResRefreshEvent.class).compose(bindToLifecycle()).subscribe(new Consumer<ChatGroupResRefreshEvent>() {
            @Override
            public void accept(ChatGroupResRefreshEvent chatGroupResRefreshEvent) throws Exception {
                ArrayList<EnterGroupModel.GroupInfoModel> models = new ArrayList<>();
                EnterGroupModel.GroupInfoModel model = new EnterGroupModel.GroupInfoModel();
                model.cover = chatGroupResRefreshEvent.imgUrl;
                model.id = chatGroupResRefreshEvent.id;
                model.merchantName = chatGroupResRefreshEvent.name;
                model.merchantUrl = chatGroupResRefreshEvent.linkUrl;
                models.add(model);
                setResourceList(models);
            }
        });
        RxBus.get().toObservable(GroupForbiddenEvent.class).compose(bindToLifecycle()).subscribe(new Consumer<GroupForbiddenEvent>() {
            @Override
            public void accept(GroupForbiddenEvent groupForbiddenEvent) throws Exception {
                if (TextUtils.equals(groupId, groupForbiddenEvent.groupId)) { // 群对应
                    if (groupInfo.isGroupMaster != 1) { // 是否群主
                        if (groupForbiddenEvent.forbidden) { // 被禁言
                            mInput.setIsBan(true);
//                            DialogUtil.showAlertDialog(ChatGroupActivity.this, "群禁言",
//                                    "群主开启群禁言，您已无法发言", "确定",
//                                    new DialogUtil.AlertDialogBtnClickListener() {
//                                        @Override
//                                        public void clickPositive() {
//                                        }
//
//                                        @Override
//                                        public void clickNegative() {
//                                        }
//                                    });
                        } else {
                            mInput.setIsBan(false);
//                            DialogUtil.showAlertDialog(ChatGroupActivity.this, "群禁言取消",
//                                    "群主解除群禁言，您已可以发言", "确定",
//                                    new DialogUtil.AlertDialogBtnClickListener() {
//                                        @Override
//                                        public void clickPositive() {
//                                        }
//
//                                        @Override
//                                        public void clickNegative() {
//                                        }
//                                    });
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        p.isGroup = true;
        super.initData();
        groupId = getIntent().getStringExtra("toUid");
        groupName = getIntent().getStringExtra("name");
        String avatar = getIntent().getStringExtra("avatar");
        if (!TextUtils.isEmpty(avatar)){
            setTalkAvatar(avatar);
        }
        setTalkName(groupName);
        titleBar.setTitle(groupName);
        titleBar.setMoreClickListener(v -> ChatGroupInfoActivity.start(this, setToId(), REQUEST_GROUP_MANAGER));
        p.enterGroup(groupId);
    }

    @OnClick(R2.id.status_expand)
    public void onViewClick(View view) {
        super.onViewClick(view);
        int id = view.getId();
        if (id == R.id.status_expand) {
            if (ivResStatus.isSelected()) {
                rvRes.setVisibility(View.GONE);
                ivResStatus.setSelected(false);
            } else {
                rvRes.setVisibility(View.VISIBLE);
                ivResStatus.setSelected(true);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resAdapter.setNewData(null);
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_GROUP_MANAGER && data != null) {
            boolean isQuit = data.getBooleanExtra("quit", false);
            if (isQuit) finish();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_group;
    }

    @Override
    public void enterGroupSuccess(EnterGroupModel data) {
        this.groupInfo = data;
        setTalkAvatar(data.groupHeadImg);
        setTalkName(data.groupName);
        titleBar.setTitle(data.groupName);
        if (data.forbidden == 1 || data.isGroupMaster == 1) {
            mInput.setIsBan(false);
        } else mInput.setIsBan(true);
        setResourceList(data.resourceInfo);
    }

    @Override
    public void enterGroupFail() {
        if (titleBar==null) return;
        titleBar.setMoreVisible(false);
    }

    @Override
    public void hasItemInSession(boolean hasItemInSession) {

    }

    @Override
    public void onAttentionSuccess() {

    }


    private void setResourceList(List<EnterGroupModel.GroupInfoModel> resourceInfo) {
        if (resourceInfo != null && resourceInfo.size() > 0) {
            resView.setVisibility(View.VISIBLE);
            rvRes.setVisibility(View.VISIBLE);
            ivResStatus.setSelected(true);
            //处理推荐店铺显示问题
            if (resAdapter.getData().size()>2){
                if (resAdapter.getData().size()>3) resAdapter.getData().subList(0,3);
                if (!resAdapter.getData().contains(resourceInfo.get(0))){
                    resAdapter.getData().add(0,resourceInfo.get(0));
                    resAdapter.setNewData(resAdapter.getData().subList(0,3));
                }
            } else if (resAdapter.getData().size()>1){
                resAdapter.addData(0,resourceInfo.get(0));
            }else{
                resAdapter.setNewData(resourceInfo.size()>3?resourceInfo.subList(0,3):resourceInfo);
            }
            scrollToLastDelayed();
        } else {
            resView.setVisibility(View.GONE);
        }
    }
}