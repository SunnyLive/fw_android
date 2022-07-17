package com.fengwo.module_chat.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.GroupInfoModel;
import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_chat.mvp.presenter.GroupInfoPresenter;
import com.fengwo.module_chat.mvp.ui.adapter.ChatInfoPeopleAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IChatInfoView;
import com.fengwo.module_chat.mvp.ui.event.GroupMemberChangeEvent;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.event.NoDistrubingEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.GsonUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.chat.FaceConversionUtil;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_comment.widget.GridItemDecoration;
import com.fengwo.module_comment.widget.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群管理页面
 */
public class ChatGroupInfoActivity extends BaseMvpActivity<IChatInfoView, GroupInfoPresenter> implements IChatInfoView {

    public final int REQUEST_EDIT_NOTICE = 101;
    public static final String SP_NO_DISTURB = "SP_NO_DISTURB";

    @BindView(R2.id.rv_people)
    RecyclerView rvPeople;
    @BindView(R2.id.view_all)
    View allMemberView;
    @BindView(R2.id.btn_view_all)
    TextView btnViewAll;
    @BindView(R2.id.switch_disturb)
    Switch disturbSwitch;
    @BindView(R2.id.menuitem_name)
    MenuItem menuitemName;
    @BindView(R2.id.menuitem_lab)
    MenuItem menuitemLab;
    @BindView(R2.id.menuitem_notice)
    MenuItem menuitemNotice;
    @BindView(R2.id.tv_notice)
    TextView tvNotice;
    @BindView(R2.id.btn_submit)
    GradientTextView btnSubmit;

    @Autowired
    UserProviderService service;

    private String groupId;
    private GroupInfoModel groupModel;
    private boolean isGroupManager = true;

    public static void start(Activity context, String groupId, int requestCode) {
        Intent intent = new Intent(context, ChatGroupInfoActivity.class);
        intent.putExtra("groupId", groupId);
        context.startActivityForResult(intent, requestCode);
    }

    private ChatInfoPeopleAdapter chatInfoPeopleAdapter;

    @Override
    public GroupInfoPresenter initPresenter() {
        return new GroupInfoPresenter();
    }

    @Override
    protected void initView() {
        setWhiteTitle("群聊消息");
        new ToolBarBuilder().setRight1Img(R.drawable.chat_ic_friend, view ->
                ChatGroupMemberActivity.startListMode(this, groupId, isGroupManager)).build();

        groupId = getIntent().getStringExtra("groupId");

        chatInfoPeopleAdapter = new ChatInfoPeopleAdapter();
        chatInfoPeopleAdapter.setOnItemClickListener((adapter, view, position) -> {
            int viewType = adapter.getItemViewType(position);
            switch (viewType) {
                case ChatInfoPeopleAdapter.TYPE_ADD:
                    // TODO: 2019/12/17 私群添加群成员(从大群成员里面添加)(暂时没有)
                    break;
                case ChatInfoPeopleAdapter.TYPE_REMOVE:
                    // 私群删除群成员
                    ChatGroupMemberActivity.startRemoveMode(this, groupId, isGroupManager);
                    break;
                case ChatInfoPeopleAdapter.TYPE_CONTENT:
                    // TODO: 2019/12/17 查看群群成员个人主页(先不做)
                    break;
            }
        });
        GridItemDecoration itemDecoration = new GridItemDecoration(this, DensityUtils.dp2px(this, 10));
        rvPeople.setLayoutManager(new GridLayoutManager(this, 5));
        rvPeople.addItemDecoration(itemDecoration);
        rvPeople.setAdapter(chatInfoPeopleAdapter);
        rvPeople.setNestedScrollingEnabled(false);
        // TODO 消息免打扰
        disturbSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                p.groupDisturb(Integer.parseInt(groupId), 0);
            } else {
                p.groupDisturb(Integer.parseInt(groupId), 1);
            }
        });

        RxBus.get().toObservable(GroupMemberChangeEvent.class).subscribe(new io.reactivex.functions.Consumer<GroupMemberChangeEvent>() {
            @Override
            public void accept(GroupMemberChangeEvent groupMemberChangeEvent) throws Exception {
                int changedGroupId = groupMemberChangeEvent.groupId;
                if (changedGroupId == Integer.parseInt(groupId)) {
                    if (p == null) {
                        return;
//                        p = initPresenter();
//                        p.attachView(ChatGroupInfoActivity.this);
                    }
                    p.getGroupMemberList(groupId, "", "1,20");
                }
            }
        });
        showLoadingDialog();
        p.getGroupInfo(groupId);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_chatinfo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT_NOTICE && data != null) {
            String content = data.getStringExtra("data");
            tvNotice.setText(content);
            groupModel.content = content;
        }
    }

    @OnClick({R2.id.menuitem_notice, R2.id.btn_submit, R2.id.view_all, R2.id.menuitem_lab})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.menuitem_notice) {// 修改群公告

            ChatGroupNoticeEditActivity.start(this, Integer.parseInt(groupId), TextUtils.isEmpty(groupModel.content)? null:groupModel.content, REQUEST_EDIT_NOTICE);
        } else if (id == R.id.btn_submit) {
            if (isGroupManager) { // 禁言
                if (groupModel.forbidden != 0) {
                    DialogUtil.showAlertDialog(this, "全体禁言", "是否将群成员全部禁言?",
                            "确定", "取消", false, new DialogUtil.AlertDialogBtnClickListener() {
                                @Override
                                public void clickPositive() {
                                    p.forbiddenGroupTalk(groupId);
                                }

                                @Override
                                public void clickNegative() {
                                }
                            });
                } else p.unforbiddenGroupTalk(groupId);
            } else { // 退出群聊
                DialogUtil.showAlertDialog(this, "退出并删除群聊", "是否确定要退出群聊?",
                        "确定", "取消", false,
                        new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                p.quitGroup(groupId);
                            }

                            @Override
                            public void clickNegative() {
                            }
                        });
            }
        } else if (id == R.id.view_all) {// 查看全部成员
            ChatGroupMemberActivity.startListMode(this, groupId, isGroupManager);
        } else if (id == R.id.menuitem_lab) {
            // TODO: 2019/12/18 修改群标签
        }
    }

    @Override
    public void setGroupInfo(GroupInfoModel data) {
        this.groupModel = data;
        isGroupManager = groupModel.userId == service.getUserInfo().id;
        menuitemName.setRightText(data.groupName);
        if (data.tags != null && data.tags.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < data.tags.size(); i++) {
                if (i < 3) {
                    builder.append(data.tags.get(i)).append(" ");
                } else break;
            }
            menuitemLab.setRightText(builder.substring(0, builder.length() - 1));
        }
        if (TextUtils.isEmpty(data.content)) {
            tvNotice.setText("暂未设置群公告");
        } else tvNotice.setText(FaceConversionUtil.getSmiledText(this, data.content));
        disturbSwitch.setChecked(data.disturb == 0);
        p.getGroupMemberList(groupId, "", "1,20");
        // 判断是否群管理，隐藏相关功能
        if (!isGroupManager) {
            btnSubmit.setText("退出群聊");
            menuitemName.setEnabled(false);
            menuitemNotice.setEnabled(false);
        } else {
            if (data.forbidden == 0) {
                btnSubmit.setText("取消全体禁言");
            } else btnSubmit.setText("全体禁言");
        }
    }

    @Override
    public void forbiddenSuccess() {
        groupModel.forbidden = 0;
        btnSubmit.setText("取消全体禁言");
        toastTip("禁言成功");
    }

    @Override
    public void unforbiddenSuccess() {
        groupModel.forbidden = 1;
        btnSubmit.setText("全体禁言");
        toastTip("已取消禁言");
    }

    @Override
    public void getGroupMember(String memberCount, ArrayList<GroupMemberModel> records) {
        if (records == null) return;
        if (isGroupManager) {
            if (records.size() % 5 == 0) {
                records.get(records.size() - 1).type = ChatInfoPeopleAdapter.TYPE_REMOVE;
            } else {
                GroupMemberModel deleteModel = new GroupMemberModel();
                deleteModel.type = ChatInfoPeopleAdapter.TYPE_REMOVE;
                records.add(deleteModel);
            }
        }
        btnViewAll.setText(String.format("查看全部成员(%s)", memberCount));
        chatInfoPeopleAdapter.setNewData(records);
        hideLoadingDialog();
    }

    @Override
    public void quitGroupSuccess() {
        // 删除聊天记录
        ChatGreenDaoHelper.getInstance().deleteGroup(String.valueOf(service.getUserInfo().id), groupId);
        // 退出群聊成功
        Intent intent = new Intent();
        intent.putExtra("quit", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private static final String TAG = "ChatGroupInfoActivity";
    private  List<String> groupLists;
    @Override
    public void disturbGroupSuccess() {
        String groundIds = (String) SPUtils1.get(this, SP_NO_DISTURB, "");

        if (TextUtils.isEmpty(groundIds)){
            groupLists = new ArrayList<>();
        }else{
            groupLists = GsonUtils.json2ObjList(groundIds, String.class);
        }
        if (disturbSwitch.isChecked()){
            if(!groupLists.contains(groupId)) groupLists.add(groupId);
        }else{
            if(groupLists.contains(groupId)) groupLists.remove(groupId);
        }
        SPUtils1.put(this,SP_NO_DISTURB , GsonUtils.objList2Json(groupLists));
        RxBus.get().post(new NoDistrubingEvent());
    }

    @Override
    public void disturbGroupFailed(String msg, int status) {
        toastTip(msg);
        if (status == 1) {
            disturbSwitch.setChecked(true);
        } else disturbSwitch.setChecked(false);
    }
}