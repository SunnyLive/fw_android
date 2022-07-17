package com.fengwo.module_flirt.UI.certification;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;


import com.fengwo.module_comment.widget.GridItemDecoration;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.Interfaces.ICerTagView;
import com.fengwo.module_flirt.P.CerTagPresenter;
import com.fengwo.module_flirt.adapter.CerTagAdapter;
import com.fengwo.module_flirt.bean.CerTagBean;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class FlirtCerTagActivity extends BaseMvpActivity<ICerTagView, CerTagPresenter> implements ICerTagView {
    @BindView(R2.id.tv_selected_tag_count)
    TextView tvSelectedCount;
    @BindView(R2.id.rv_selected_tag)
    RecyclerView selectedRecyclerView;
    @BindView(R2.id.rv_tag_all)
    RecyclerView tagRecyclerView;
    @BindView(R2.id.tv_confirm)
    TextView tvConfirm;

    private int circleId;
    private CerTagAdapter tagAdapter;
    private CerTagAdapter selectedAdapter;
    private final int MAX_COUNT = 4;
    private String selectedTagIds;
    private int page = 1;
    private int pageSize = 100;

    public static void start(Activity activity, int circleId, String tagIds, int requestCode) {
        Intent intent = new Intent(activity, FlirtCerTagActivity.class);
        intent.putExtra("circleId", circleId);
        intent.putExtra("tagIds", tagIds);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public CerTagPresenter initPresenter() {
        return new CerTagPresenter();
    }

    @Override
    protected void initView() {
        initSelectedRecyclerView();
        initTagRecyclerView();
//        circleId = getIntent().getIntExtra("circleId", -1);
        selectedTagIds = getIntent().getStringExtra("tagIds");
        p.getTagList(page,pageSize);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_card_tag;
    }

    private static final String TAG = "PostCardTagActivity";
    @Override
    public void setAllTag(BaseListDto<CerTagBean> data) {
        L.e(TAG, "setAllTag: "+data.records.size() );
        if (data != null && data.records.size() > 0) {
            if (!TextUtils.isEmpty(selectedTagIds)) {
                String[] split = selectedTagIds.split(" ");
                if (split.length > 0) {
                    ArrayList<CerTagBean> selected = new ArrayList<>();
                    for (String tagId : split) {
                        for (CerTagBean model : data.records) {
                            if (TextUtils.equals(tagId, String.valueOf(model.getId()))) {
                                model.selected = true;
                                selected.add(model);
                                break;
                            }
                        }
                    }
                    selectedAdapter.setNewData(selected);
                    tvSelectedCount.setText("（"+String.format("%d/%d", selectedAdapter.getData().size(), MAX_COUNT)+"）");
                } else selectedAdapter.setNewData(null);
            }
            tagAdapter.setNewData(data.records);
        }
    }

    @OnClick(R2.id.tv_confirm)
    public void onViewClick(View view) {
        if (selectedAdapter.getData().size() <= 0) {
            toastTip("请选择标签");
            return;
        }
        // 将选择的标签填入回调
        Intent intent = new Intent();
        intent.putExtra("data", (Serializable) selectedAdapter.getData());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initTagRecyclerView() {
        tagAdapter = new CerTagAdapter();
        tagAdapter.setOnItemClickListener((adapter, view, position) -> {
            CerTagBean model = tagAdapter.getData().get(position);
            if (selectedAdapter.getData().size() >= MAX_COUNT && !model.selected) {
                ToastUtils.showShort(this, "标签个数已满");
                return;
            }
            // 设置全部Tag的Adapter的Ui显示
            model.originPosition = position;
            model.selected = !model.selected;
            tagAdapter.notifyDataSetChanged();
            // 选中Adapter的UI绘制
            if (model.selected) {
                selectedAdapter.addData(model);
            } else {
                int currentPosition = -1;
                for (int i = 0; i < selectedAdapter.getData().size(); i++) {
                    CerTagBean selectedModel = selectedAdapter.getData().get(i);
                    if (selectedModel.getId() == model.getId()) {
                        currentPosition = i;
                        break;
                    }
                }
                if (currentPosition >= 0) selectedAdapter.remove(currentPosition);
            }
            // 个数的UI显示
            tvSelectedCount.setText("（"+String.format("%d/%d", selectedAdapter.getData().size(), MAX_COUNT)+"）");
            tvConfirm.setText("完成（"+String.format("%d/%d", selectedAdapter.getData().size(), MAX_COUNT)+"）");
        });
        GridItemDecoration itemDecoration = new GridItemDecoration(this, DensityUtils.dp2px(this, 12));
        tagRecyclerView.setLayoutManager(new GridLayoutManager(this, MAX_COUNT));
        tagRecyclerView.setAdapter(tagAdapter);
        tagRecyclerView.addItemDecoration(itemDecoration);
        tagRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initSelectedRecyclerView() {
        selectedAdapter = new CerTagAdapter();
        selectedAdapter.setOnItemClickListener((adapter, view, position) -> {
            // 选中Adapter的UI显示
            CerTagBean model = selectedAdapter.getData().get(position);
            selectedAdapter.remove(position);
            // 全部Tag的Adapter的UI显示
            int originPosition = model.originPosition;
            tagAdapter.getData().get(originPosition).selected = false;
            tagAdapter.notifyDataSetChanged();
            // 个数的UI显示
            tvSelectedCount.setText(String.format("%d/%d", selectedAdapter.getData().size(), MAX_COUNT));
            tvConfirm.setText("完成"+String.format("%d/%d", selectedAdapter.getData().size(), MAX_COUNT));
        });
        GridItemDecoration itemDecoration = new GridItemDecoration(this, DensityUtils.dp2px(this, 12));
        selectedRecyclerView.setLayoutManager(new GridLayoutManager(this, MAX_COUNT));
        selectedRecyclerView.setAdapter(selectedAdapter);
        selectedRecyclerView.addItemDecoration(itemDecoration);
        selectedRecyclerView.setNestedScrollingEnabled(false);
    }
}