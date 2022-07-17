package com.fengwo.module_chat.mvp.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_comment.widget.GridItemDecoration;
import com.fengwo.module_chat.utils.chat_new.MaxHeightGridLayoutManager;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.widget.AppTitleBar;

import java.util.ArrayList;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author Zachary
 * @date 2019/12/24
 */
public class SelectCardCategoryPopupWindow extends BasePopupWindow {

    private BaseQuickAdapter<RecommendCircleBean, BaseViewHolder> categoryAdapter;
    private OnItemSelectListener listener;

    private int page = 1;
    private final String PAGE_SIZE = ",20";

    public SelectCardCategoryPopupWindow(Context context) {
        super(context);
        init();
        getData();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_select_category);
    }

    private void init() {
        AppTitleBar titleBar = findViewById(R.id.titleBar);
        RecyclerView selectRv = findViewById(R.id.selectRv);
        setShowAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.chat_down_in));
        setDismissAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.chat_rise_out));

        titleBar.setBackClickListener(v -> dismiss());

        categoryAdapter = new BaseQuickAdapter<RecommendCircleBean, BaseViewHolder>(R.layout.chat_item_card_select) {

            private int selectPosition = -1;

            @Override
            protected void convert(@NonNull BaseViewHolder helper, RecommendCircleBean item) {
                ImageView imageView = helper.setText(R.id.tvTitle, item.name).getView(R.id.civ_card);
                ImageLoader.loadImg(imageView, item.thumb);
                helper.itemView.setSelected(selectPosition == helper.getLayoutPosition());
            }
        };
        categoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            RecommendCircleBean model = categoryAdapter.getData().get(position);
            if (listener != null) listener.itemClick(model);
        });
        categoryAdapter.setEnableLoadMore(true);
        categoryAdapter.setPreLoadNumber(1);
        categoryAdapter.setOnLoadMoreListener(() -> {
            page += 1;
            getData();
        }, selectRv);
        MaxHeightGridLayoutManager gridLayoutManager = new MaxHeightGridLayoutManager(getContext(), 3, (int) (ScreenUtils.getScreenHeight(getContext()) * 0.66));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position >= categoryAdapter.getData().size()) return 3;
                else return 1;
            }
        });
        selectRv.setAdapter(categoryAdapter);
        selectRv.setLayoutManager(gridLayoutManager);
        selectRv.addItemDecoration(new GridItemDecoration(getContext(), DensityUtils.dp2px(getContext(), 10)));
    }

    public void setOnItemSelectListener(OnItemSelectListener l) {
        this.listener = l;
    }

    private void getData() {
        new RetrofitUtils().createApi(ChatService.class).getCircleList(page + PAGE_SIZE)
                .compose(RxUtils.applySchedulers2()).compose(RxUtils.handleResult2())
                .subscribe(new LoadingObserver<BaseListDto<RecommendCircleBean>>() {
                    @Override
                    public void _onNext(BaseListDto<RecommendCircleBean> data) {
                        ArrayList<RecommendCircleBean> records = data.records;
                        if (records != null) {
                            if (page == 1) {
                                categoryAdapter.loadMoreComplete();
                                categoryAdapter.setNewData(records);
                            } else if (records.size() < 20) {
                                categoryAdapter.loadMoreEnd();
                                categoryAdapter.addData(records);
                            } else {
                                categoryAdapter.loadMoreComplete();
                                categoryAdapter.addData(records);
                            }
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }

    public interface OnItemSelectListener {
        void itemClick(RecommendCircleBean model);
    }
}