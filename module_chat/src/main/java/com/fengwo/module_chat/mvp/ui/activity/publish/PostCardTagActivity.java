package com.fengwo.module_chat.mvp.ui.activity.publish;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.ui.adapter.CardTagSelectAdapter;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_chat.mvp.presenter.CardTagPresenter;
import com.fengwo.module_chat.mvp.ui.adapter.CardTagAdapter;
import com.fengwo.module_chat.mvp.ui.contract.ICardTagView;
import com.fengwo.module_comment.widget.GridItemDecoration;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PostCardTagActivity extends BaseMvpActivity<ICardTagView, CardTagPresenter> implements ICardTagView {
    @BindView(R2.id.tv_selected_tag_count)
    TextView tvSelectedCount;
    @BindView(R2.id.rv_selected_tag)
    RecyclerView selectedRecyclerView;
    @BindView(R2.id.rv_tag_all)
    RecyclerView tagRecyclerView;
    @BindView(R2.id.tv_confirm)
    TextView tvConfirm;

    private int circleId;
    private CardTagAdapter tagAdapter;
    private CardTagSelectAdapter selectedAdapter;
    private final int MAX_COUNT = 3;
    private String selectedTagIds;

    public static void start(Activity activity, int circleId, String tagIds, int requestCode) {
        Intent intent = new Intent(activity, PostCardTagActivity.class);
        intent.putExtra("circleId", circleId);
        intent.putExtra("tagIds", tagIds);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public CardTagPresenter initPresenter() {
        return new CardTagPresenter();
    }

    @Override
    protected void initView() {
        initSelectedRecyclerView();
        initTagRecyclerView();
        circleId = getIntent().getIntExtra("circleId", -1);
        selectedTagIds = getIntent().getStringExtra("tagIds");
        p.getTagList(circleId);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_card_tag;
    }

    private static final String TAG = "PostCardTagActivity";
    @Override
    public void setAllTag(List<CardTagModel> data) {
        L.e(TAG, "setAllTag: "+data.size() );
        if (data != null && data.size() > 0) {
            if (!TextUtils.isEmpty(selectedTagIds)) {
                String[] split = selectedTagIds.split(" ");
                if (split.length > 0) {
                    ArrayList<CardTagModel> selected = new ArrayList<>();
                    for (String tagId : split) {
                        for (CardTagModel model : data) {
                            if (TextUtils.equals(tagId, String.valueOf(model.id))) {
                                model.selected = true;
                                selected.add(model);
                                break;
                            }
                        }
                    }
                    selectedAdapter.setNewData(selected);
                    tvSelectedCount.setText(String.format("(%d/%d)", selectedAdapter.getData().size(), MAX_COUNT));
                } else selectedAdapter.setNewData(null);
            }
            tagAdapter.setNewData(data);
        }
    }

    @OnClick(R2.id.tv_confirm)
    public void onViewClick(View view) {
        if (selectedAdapter.getData().size() <= 0) {
            toastTip("请先选择标签，再提交");
            return;
        }
        // 将选择的标签填入回调
        Intent intent = new Intent();
        intent.putExtra("data", (Serializable) selectedAdapter.getData());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initTagRecyclerView() {
        tagAdapter = new CardTagAdapter();
        tagAdapter.setOnItemClickListener((adapter, view, position) -> {
            CardTagModel model = tagAdapter.getData().get(position);
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
                    CardTagModel selectedModel = selectedAdapter.getData().get(i);
                    if (selectedModel.id == model.id) {
                        currentPosition = i;
                        break;
                    }
                }
                if (currentPosition >= 0) selectedAdapter.remove(currentPosition);
            }
            // 个数的UI显示
            tvSelectedCount.setText(String.format("(%d/%d)", selectedAdapter.getData().size(), MAX_COUNT));
        });
        GridItemDecoration itemDecoration = new GridItemDecoration(this, DensityUtils.dp2px(this, 12));
        tagRecyclerView.setLayoutManager(new GridLayoutManager(this, MAX_COUNT));
        tagRecyclerView.setAdapter(tagAdapter);
        tagRecyclerView.addItemDecoration(itemDecoration);
        tagRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initSelectedRecyclerView() {
        selectedAdapter = new CardTagSelectAdapter();
        selectedAdapter.setOnItemClickListener((adapter, view, position) -> {
            // 选中Adapter的UI显示
            CardTagModel model = selectedAdapter.getData().get(position);
            selectedAdapter.remove(position);
            // 全部Tag的Adapter的UI显示
            int originPosition = model.originPosition;
            tagAdapter.getData().get(originPosition).selected = false;
            tagAdapter.notifyDataSetChanged();
            // 个数的UI显示
            tvSelectedCount.setText(String.format("(%d/%d)", selectedAdapter.getData().size(), MAX_COUNT));
        });
        GridItemDecoration itemDecoration = new GridItemDecoration(this, DensityUtils.dp2px(this, 12));
        selectedRecyclerView.setLayoutManager(new GridLayoutManager(this, MAX_COUNT));
        selectedRecyclerView.setAdapter(selectedAdapter);
        selectedRecyclerView.addItemDecoration(itemDecoration);
        selectedRecyclerView.setNestedScrollingEnabled(false);
    }
}