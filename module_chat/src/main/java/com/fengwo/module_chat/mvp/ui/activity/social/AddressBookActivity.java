package com.fengwo.module_chat.mvp.ui.activity.social;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.ContactBean;
import com.fengwo.module_chat.mvp.presenter.AddressBookPresenter;
import com.fengwo.module_chat.mvp.ui.contract.IAddressBookView;
import com.fengwo.module_chat.mvp.ui.adapter.ChatAddressBookAdapter;
import com.fengwo.module_chat.widgets.indexbar.FWDividerItemDecoration;
import com.fengwo.module_chat.widgets.indexbar.IndexBar;
import com.fengwo.module_chat.widgets.indexbar.suspension.SuspensionDecoration;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.widget.AppTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 通讯录
 *
 * @author chenshanghui
 * @date 2019/9/16
 */
public class AddressBookActivity extends BaseMvpActivity<IAddressBookView, AddressBookPresenter>
        implements IAddressBookView {

    @BindView(R2.id.iv_search)
    ImageView ivSearch;
    @BindView(R2.id.titleBar)
    AppTitleBar titleBar;
    @BindView(R2.id.rv_contact)
    RecyclerView rvContact;
    @BindView(R2.id.indexBar)
    IndexBar indexBar;
    @BindView(R2.id.tv_selected)
    TextView tvSelected;

    private ChatAddressBookAdapter mAdapter;
    private SuspensionDecoration suspensionDecoration;
    private ArrayList<ContactBean> mData = new ArrayList<>();

    @Override
    protected void initView() {
        titleBar.setBackgroundResource(R.color.white);
        titleBar.setMoreClickListener(v -> showMorePop());

        initRecyclerView();
        initHeaderData();
        initData();
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_adress_book;
    }

    @Override
    public AddressBookPresenter initPresenter() {
        return new AddressBookPresenter();
    }

    @Override
    public void setContactListData(List<ContactBean> list) {
        if (list != null && list.size() > 0) {
            indexBar.setmSourceDatas(list).invalidate();
            suspensionDecoration.setmDatas(list);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new ChatAddressBookAdapter(mData);
        suspensionDecoration = new SuspensionDecoration(this, mData);
        FWDividerItemDecoration dividerItemDecoration = new FWDividerItemDecoration(this,
                FWDividerItemDecoration.VERTICAL_LIST);

        rvContact.setLayoutManager(layoutManager);
        rvContact.addItemDecoration(suspensionDecoration);
        rvContact.addItemDecoration(dividerItemDecoration);
        rvContact.setAdapter(mAdapter);
        indexBar.setmPressedShowTextView(tvSelected)
                .setNeedRealIndex(false)
                .setmLayoutManager(layoutManager);
    }

    // 初始化头部
    private void initHeaderData() {
        View contactHeader = LayoutInflater.from(this).inflate(R.layout.chat_header_address_book, rvContact, false);
        ImageView ivAvatar = contactHeader.findViewById(R.id.iv_avatar);
        TextView tvName = contactHeader.findViewById(R.id.tv_name);
        tvName.setText("手机联系人");
        int padding = DensityUtils.dp2px(this, 10);
        ivAvatar.setPadding(padding, padding, padding, padding);
        ivAvatar.setImageResource(R.drawable.chat_ic_contact_book);
        ivAvatar.setBackground(getResources().getDrawable(R.drawable.bg_chat_contact));
        contactHeader.setOnClickListener(v -> {
            // TODO: 2019/11/20 添加手机通讯录
        });
        mAdapter.addHeaderView(contactHeader);

        View groupHeader = LayoutInflater.from(this).inflate(R.layout.chat_header_address_book, rvContact, false);
        ImageView avatar = groupHeader.findViewById(R.id.iv_avatar);
        TextView name = groupHeader.findViewById(R.id.tv_name);
        name.setText("我的群聊");
        avatar.setPadding(padding, padding, padding, padding);
        avatar.setImageResource(R.drawable.chat_ic_group);
        avatar.setBackground(getResources().getDrawable(R.drawable.bg_chat_group));
        groupHeader.setOnClickListener(v -> {
            // TODO: 2019/11/20 跳转群聊列表页
        });
        mAdapter.addHeaderView(groupHeader);
    }

    // 获取通讯录数据
    private void initData() {
        p.getContactList();
    }


    private void showMorePop() {
        // TODO: 2019/11/21 弹出popupWindow
    }
}