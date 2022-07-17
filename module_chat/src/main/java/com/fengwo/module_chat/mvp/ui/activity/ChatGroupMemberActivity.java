package com.fengwo.module_chat.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_chat.mvp.presenter.ChatGroupMemberPresenter;
import com.fengwo.module_chat.mvp.ui.adapter.ChatAllPeopleAdapter;
import com.fengwo.module_chat.mvp.ui.adapter.ChatAllPeopleChooseAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IChatGroupMemberView;
import com.fengwo.module_chat.mvp.ui.dialog.CreateGroupPopupWindow;
import com.fengwo.module_chat.mvp.ui.event.GroupMemberChangeEvent;
import com.fengwo.module_chat.widgets.MaxWidthLayoutManager;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.widget.AppTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群成员管理页面
 */
public class ChatGroupMemberActivity extends BaseMvpActivity<IChatGroupMemberView, ChatGroupMemberPresenter>
        implements IChatGroupMemberView {

    public static final int TYPE_CREATE_GROUP = 11;
    public static final int TYPE_LIST = 12;
    public static final int TYPE_REMOVE = 13;
    private CreateGroupPopupWindow createGroupPopupWindow;

    public static void startListMode(Activity activity, String groupId, boolean isGroupManager) {
        Intent intent = new Intent(activity, ChatGroupMemberActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("type", TYPE_LIST);
        intent.putExtra("isGroupManager", isGroupManager);
        activity.startActivity(intent);
    }

    public static void startRemoveMode(Activity activity, String groupId, boolean isGroupManager) {
        Intent intent = new Intent(activity, ChatGroupMemberActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("type", TYPE_REMOVE);
        intent.putExtra("isGroupManager", isGroupManager);
        activity.startActivity(intent);
    }

    @BindView(R2.id.rv_choose)
    RecyclerView rvChoose;
    @BindView(R2.id.rv_people)
    RecyclerView rvPeople;
    @BindView(R2.id.et_search)
    EditText etSearch;
    @BindView(R2.id.ll_bottom)
    TextView tvSubmit;
    @BindView(R2.id.title)
    AppTitleBar titleBar;

    @Autowired
    UserProviderService userService;

    private ChatAllPeopleChooseAdapter chooseAdapter;
    private ChatAllPeopleAdapter allPeopleAdapter;

    private final String PAGE_SIZE = ",20";
    private String groupId;
    private int type = TYPE_LIST;

    private boolean isSearchMode = false;
    private List<GroupMemberModel> memberList = new ArrayList<>();
    private int originPage = 1;
    private int searchPage = 1;

    @Override
    public ChatGroupMemberPresenter initPresenter() {
        return new ChatGroupMemberPresenter();
    }

    @Override
    protected void initView() {
        groupId = getIntent().getStringExtra("groupId");
        type = getIntent().getIntExtra("type", TYPE_LIST);

        if (type == TYPE_REMOVE) {
            titleBar.setTitle("删除圈成员");
            tvSubmit.setText("确认删除");
        } else {
            titleBar.setTitle("圈成员");
            tvSubmit.setText("建立私群");
        }

        initMemberPeopleRv();
        initChooseRv();
        initSearch();
        loadMemberList(originPage, "");
    }

    @OnClick(R2.id.ll_bottom)
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_bottom) {
            if (type == TYPE_REMOVE) {
                // 删除群成员
                if (CommentUtils.isListEmpty(chooseAdapter.getData())) {
                    toastTip("请选择要删除的成员！");
                    return;
                }
                DialogUtil.showAlertDialog(this, "删除群成员", "确认删除已选群成员？",
                        "确认", "取消", false, new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                StringBuilder builder = new StringBuilder();
                                for (GroupMemberModel memberModel : chooseAdapter.getData()) {
                                    builder.append(memberModel.userId).append(",");
                                }
                                p.deleteGroupMember(groupId, builder.substring(0, builder.length() - 1));
                            }

                            @Override
                            public void clickNegative() {
                            }
                        });
            } else {
                if (chooseAdapter.getData().size() < 2) {
                    toastTip("至少3人才能建立私群");
                    return;
                }
                // 创建私群
                createGroupPopupWindow = new CreateGroupPopupWindow(this, groupName -> {
                    if(isFastClick()) return;
                    L.e( "onViewClick: ", "===");
                    if (TextUtils.isEmpty(groupName)) {
                        toastTip("请输入您的群名");
                    } else {
                        StringBuilder builder = new StringBuilder();
                        for (GroupMemberModel memberModel : chooseAdapter.getData()) {
                            builder.append(memberModel.userId).append(",");
                        }
                        KeyBoardUtils.closeKeybord(etSearch, this);
                        p.createGroup(groupName.toString(), Integer.parseInt(groupId), builder.substring(0, builder.length() - 1));
                    }
                });
                createGroupPopupWindow.setPopupGravity(Gravity.BOTTOM);
                createGroupPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                createGroupPopupWindow.showPopupWindow();
            }
        }
    }

    // 初始化搜索框
    private void initSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    // 搜索关键字用户
                    isSearchMode = true;
                    searchPage = 1;
                    loadMemberList(searchPage, s.toString());
                } else {
                    allPeopleAdapter.setNewData(memberList);
                    isSearchMode = false;
                }
            }
        });
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH && event != null && event.getAction() == KeyEvent.ACTION_UP) {
                // 搜索关键字
                isSearchMode = true;
                searchPage = 1;
                loadMemberList(searchPage, etSearch.getText().toString());
                KeyBoardUtils.closeKeybord(etSearch, this);
                return true;
            }
            return false;
        });
        etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) KeyBoardUtils.closeKeybord(etSearch, this);
        });
    }

    // 初始化已选择成员列表
    private void initChooseRv() {
        chooseAdapter = new ChatAllPeopleChooseAdapter();
        chooseAdapter.setOnItemClickListener((adapter, view, position) -> {
            GroupMemberModel item = chooseAdapter.getItem(position);
            chooseAdapter.remove(position);
            allPeopleAdapter.setCheck(item);
            if (!isSearchMode) {
                for (GroupMemberModel member : memberList) {
                    if (member.userId == item.userId) {
                        member.isSelected = false;
                        break;
                    }
                }
            }
        });
        rvChoose.setAdapter(chooseAdapter);
        rvChoose.setLayoutManager(new MaxWidthLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvChoose.setOnTouchListener((v, event) -> {
            if (!rvChoose.hasFocus()) rvChoose.requestFocus();
            return false;
        });
    }

    // 初始化成员列表
    private void initMemberPeopleRv() {
        allPeopleAdapter = new ChatAllPeopleAdapter();
        allPeopleAdapter.setOnItemClickListener((adapter, view, position) -> {
            GroupMemberModel item = allPeopleAdapter.getData().get(position);
            if (item.userId == userService.getUserInfo().id) return;
            chooseAdapter.checkIfHasDelOrAdd(item, rvChoose);
            if (isSearchMode) {
                for (GroupMemberModel member : memberList) {
                    if (member.userId == item.userId) {
                        member.isSelected = !member.isSelected;
                        break;
                    }
                }
                isSearchMode = false;
                etSearch.setText("");
                allPeopleAdapter.setNewData(memberList);
            } else allPeopleAdapter.setCheck(position);
        });
        allPeopleAdapter.setEnableLoadMore(true);
        allPeopleAdapter.setPreLoadNumber(2);
        allPeopleAdapter.setOnLoadMoreListener(() -> {
            if (isSearchMode) { // 搜索模式，进行搜索
                searchPage += 1;
                loadMemberList(searchPage, etSearch.getText().toString());
            } else { // 正常模式，数据加载
                originPage += 1;
                loadMemberList(originPage, etSearch.getText().toString());
            }
        }, rvPeople);
        rvPeople.setLayoutManager(new LinearLayoutManager(this));
        rvPeople.setAdapter(allPeopleAdapter);
        rvPeople.setOnTouchListener((v, event) -> {
            if (!rvPeople.hasFocus()) rvPeople.requestFocus();
            return false;
        });
    }

    public void loadMemberList(int page, String searchKey) {
        showLoadingDialog();
        p.getMemberList(groupId, searchKey, page + PAGE_SIZE);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_allpeople;
    }

    @Override
    public void setGroupMember(List<GroupMemberModel> records) {
        if (isSearchMode) {
            compareDataAndSelect(records, memberList);
            if (searchPage == 1) {
                allPeopleAdapter.setNewData(records);
            } else {
                allPeopleAdapter.addData(records);
            }
        } else {
            if (originPage == 1) {
                if (memberList.size() == 0) {
                    allPeopleAdapter.setNewData(records);
                } else {
                    allPeopleAdapter.setNewData(memberList);
                }
            } else {
                allPeopleAdapter.addData(records);
            }
            memberList.addAll(records);
        }
        if (records == null || records.size() < 20) {
            allPeopleAdapter.loadMoreEnd();
        } else {
            allPeopleAdapter.loadMoreComplete();
        }
    }

    @Override
    public void createGroupSuccess(String groupId, String groupName) {
        if (createGroupPopupWindow != null && createGroupPopupWindow.isShowing()) {
            createGroupPopupWindow.dismiss();
        }
        ArouteUtils.toChatGroupActivity(String.valueOf(userService.getUserInfo().id), groupId, groupName,"");
        finish();
    }

    @Override
    public void deleteGroupMemberSuccess() {
        finish();
        RxBus.get().post(new GroupMemberChangeEvent(Integer.parseInt(groupId)));
    }

    public void compareDataAndSelect(List<GroupMemberModel> searchList, List<GroupMemberModel> memberList) {
        if (searchList != null && searchList.size() > 0) {
            for (GroupMemberModel searchItem : searchList) {
                for (GroupMemberModel member : memberList) {
                    if (searchItem.userId == member.userId) {
                        searchItem.isSelected = member.isSelected;
                        break;
                    }
                }
            }
        }
    }
}