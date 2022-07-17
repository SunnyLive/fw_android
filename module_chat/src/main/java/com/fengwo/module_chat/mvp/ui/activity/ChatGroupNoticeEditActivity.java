package com.fengwo.module_chat.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.presenter.EditNoticePresenter;
import com.fengwo.module_chat.mvp.ui.contract.IEditNoticeView;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.fengwo.module_comment.widget.ClearEditText;

import butterknife.BindView;

public class ChatGroupNoticeEditActivity extends BaseMvpActivity<IEditNoticeView, EditNoticePresenter>
        implements IEditNoticeView {

    @BindView(R2.id.title)
    AppTitleBar titleBar;
    @BindView(R2.id.cet_group)
    ClearEditText editText;
    @BindView(R2.id.tv_count)
    TextView tvCount;

    private int groupId;

    public static void start(Activity context, int groupId,String content, int requestCode) {
        Intent intent = new Intent(context, ChatGroupNoticeEditActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupContent", content);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public EditNoticePresenter initPresenter() {
        return new EditNoticePresenter();
    }

    @Override
    protected void initView() {
        groupId = getIntent().getIntExtra("groupId", -1);
        String groupContent = getIntent().getStringExtra("groupContent");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) tvCount.setText("0/120");
                else tvCount.setText(String.format("%d/120", s.length()));
            }
        });
        if (!TextUtils.isEmpty(groupContent)){
            editText.setText(groupContent);
            editText.setSelection(groupContent.length());
        }
        titleBar.setMoreClickListener(v -> {
            if (TextUtils.isEmpty(editText.getText())) {
                toastTip("请填写群公告");
                return;
            }
            p.editNotice(groupId, editText.getText().toString());
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_group_notice;
    }

    @Override
    public void editNoticeSuccess(String content) {
        toastTip("公告修改成功");
        Intent intent = getIntent().putExtra("data", content);
        setResult(RESULT_OK, intent);
        finish();
    }
}
