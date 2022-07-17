package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.adapter.FilterTableAdapter;
import com.fengwo.module_flirt.bean.CerTagBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

/**
 * 筛选标签
 * @Author BLCS
 * @Time 2020/8/13 15:04
 */
public class FilterTablePopwindow extends BasePopupWindow {
    @BindView(R2.id.tv_table_cancel)
    TextView tvCancel;
    @BindView(R2.id.tv_table_ok)
    TextView tvOk;
    @BindView(R2.id.rv_label)
    RecyclerView rvLabel;
    private FilterTableAdapter tableAdapter;

    public FilterTablePopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        initUI();
    }

    private void initUI() {
        rvLabel.setLayoutManager(new LinearLayoutManager(getContext()));
        tableAdapter = new FilterTableAdapter();
        rvLabel.setAdapter(tableAdapter);
    }

    @OnClick({R2.id.tv_table_cancel, R2.id.tv_table_ok})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_table_cancel) {
            dismiss();
        } else if (id == R.id.tv_table_ok) {
            if (listener != null && !TextUtils.isEmpty(tableAdapter.getTabNames()))
                listener.sure(tableAdapter.getTabNames(),tableAdapter.getParentId());
            dismiss();
        }
    }

    public OnClickSureListener listener;

    public interface OnClickSureListener {
        void sure(String tabNames,String parentId);
    }

    public void addOnClickListener(OnClickSureListener onClickSureListener) {
        listener = onClickSureListener;
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_filter_table);
        ButterKnife.bind(this, v);
        return v;
    }

    public void setLabel(List<CerTagBean> data) {
        tableAdapter.setNewData(data);
        tableAdapter.clearCache();
    }
}
