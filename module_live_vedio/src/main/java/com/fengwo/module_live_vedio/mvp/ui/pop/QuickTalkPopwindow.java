package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.QuickTalkDto;
import com.fengwo.module_live_vedio.utils.MaxHeightLayoutmanager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;
import razerdp.basepopup.BasePopupWindow;

public class QuickTalkPopwindow extends BasePopupWindow {

    private final String PAGE_SIZE = ",10";

    private RecyclerView rv;
    private final SmartRefreshLayout refreshLayout;
    private Context mContext;
    private QuickTalkAdapter quickTalkAdapter;
    private int page = 1;

    public QuickTalkPopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        mContext = context;
        rv = findViewById(R.id.rv);
        refreshLayout = findViewById(R.id.smartrefreshlayout);
        getQuickTalk();

        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getQuickTalk();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page += page;
            getQuickTalk();
        });
        MaxHeightLayoutmanager layoutManager = new MaxHeightLayoutmanager(mContext,
                DensityUtils.dp2px(getContext(), 300));
        quickTalkAdapter = new QuickTalkAdapter(null);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(quickTalkAdapter);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_quicktalk);
    }


    private void getQuickTalk() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .getCommentWord(page + PAGE_SIZE)
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<QuickTalkDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<QuickTalkDto>> data) {
                        if (refreshLayout != null)
                            refreshLayout.closeHeaderOrFooter();
                        if (data.isSuccess()) {
                            if (page == 1) quickTalkAdapter.setNewData(data.data.records);
                            else quickTalkAdapter.addData(data.data.records);
                        } else {
                            ToastUtils.showShort(getContext(), data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                });
    }

    public interface OnQuickTalkClickListener {
        void onClick(String content);
    }

    private static OnQuickTalkClickListener listener;

    public void setOnQuickTalkClickListener(OnQuickTalkClickListener l) {
        listener = l;
    }


    private class QuickTalkAdapter extends BaseQuickAdapter<QuickTalkDto, BaseViewHolder> {

        public QuickTalkAdapter(@Nullable List<QuickTalkDto> data) {
            super(R.layout.live_item_quicktalk, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, QuickTalkDto item) {
            helper.setText(R.id.tv_content, item.getContent());
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != listener)
                        listener.onClick(item.getContent());
                    QuickTalkPopwindow.this.dismiss();

                }
            });
        }
    }
}
