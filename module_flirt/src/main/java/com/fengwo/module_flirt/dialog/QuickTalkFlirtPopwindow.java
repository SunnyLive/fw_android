package com.fengwo.module_flirt.dialog;

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
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.CommentWordDto;
import com.fengwo.module_live_vedio.utils.MaxHeightLayoutmanager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

public class QuickTalkFlirtPopwindow extends BasePopupWindow {


    private RecyclerView rv;
    private final SmartRefreshLayout refreshLayout;
    private Context mContext;
    private QuickTalkAdapter quickTalkAdapter;
    private int page = 0;

    public QuickTalkFlirtPopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        mContext = context;
        rv = findViewById(R.id.rv);
        refreshLayout = findViewById(R.id.smartrefreshlayout);
        getQuickTalk(page);

        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getQuickTalk(page);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            getQuickTalk(++page);
        });
        MaxHeightLayoutmanager layoutManager = new MaxHeightLayoutmanager(mContext,
                DensityUtils.dp2px(getContext(), 300));
        quickTalkAdapter = new QuickTalkAdapter(null);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(quickTalkAdapter);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_quicktalk_flirt);
    }


    private void getQuickTalk(int current) {
        RequestBody build = new WenboParamsBuilder()
                .put("current", String.valueOf(current))
                .put("size", "20")
                .put("userPort","1")
                .build();
        new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .getCommentWord(build)
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<CommentWordDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<CommentWordDto>> data) {
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


    private class QuickTalkAdapter extends BaseQuickAdapter<CommentWordDto, BaseViewHolder> {

        public QuickTalkAdapter(@Nullable List<CommentWordDto> data) {
            super(R.layout.live_item_quicktalk_flirt, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, CommentWordDto item) {
            helper.setText(R.id.tv_content, item.getTitle());
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != listener)
                        listener.onClick(item.getTitle());
                    QuickTalkFlirtPopwindow.this.dismiss();

                }
            });
        }
    }
}
