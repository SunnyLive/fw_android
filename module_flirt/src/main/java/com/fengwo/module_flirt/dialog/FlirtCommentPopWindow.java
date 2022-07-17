package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.adapter.FlirtCommentPopAdapter;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.FlirtCommentBean;
import com.fengwo.module_login.utils.UserManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;


public class FlirtCommentPopWindow extends BasePopupWindow {

    public static final int REQUEST_DATA_SIZE = 20;

    private RecyclerView rvComment;
    private FlirtCommentBean mFlirtCommentBean;
    private CompositeDisposable mCompositeDisposable;
    private SmartRefreshLayout smartRefreshLayout;
    private int current = 1;

    private FlirtCommentPopAdapter mAdapter;
    private FlirtApiService service;
    private RetrofitUtils retrofitUtils;
    private View emptyView;


    public FlirtCommentPopWindow(Context context,IAddListListener listener) {
        super(context);
        this.listener = listener;
        initData();
        initView();
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setPopupGravity(Gravity.BOTTOM);
        setShowAnimation(AnimationUtils.loadAnimation(getContext(), com.fengwo.module_chat.R.anim.pickerview_slide_in_bottom));
        setDismissAnimation(AnimationUtils.loadAnimation(getContext(), com.fengwo.module_chat.R.anim.pickerview_slide_out_bottom));
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_flirt_comment);
    }

    private void initData() {
        mCompositeDisposable = new CompositeDisposable();
        mFlirtCommentBean = new FlirtCommentBean();
        mFlirtCommentBean.setRecords(new ArrayList<>());
    }

    private void initView() {
        findViewById(R.id.iv_close).setOnClickListener((v)-> dismiss());
        emptyView = findViewById(R.id.empty_view);
        smartRefreshLayout = findViewById(R.id.smartrefreshlayout);
        SmartRefreshLayoutUtils.setTransparentBgWithWhileText(getContext(), smartRefreshLayout);
        smartRefreshLayout.setEnableOverScrollDrag(true);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getCommentList(current++);
            }
        });
        rvComment = findViewById(R.id.rv_comment);
        rvComment.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAdapter = new FlirtCommentPopAdapter(R.layout.item_flirt_comment,
                mFlirtCommentBean.getRecords());
        rvComment.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(((adapter, view, position) -> {
            listener.clickBank(mAdapter.getItem(position));
//            dismiss();
//            FlirtCommentBean.RecordsDTO record = mAdapter.getItem(position);
//            ReceiveCommentPopWindow rcPopWindow = new ReceiveCommentPopWindow(getContext(), record.getId());
//            rcPopWindow.showPopupWindow();
        }));
        getCommentList(current++);
    }

    /**
     *
     * @param current
     */
    private void getCommentList(int current) {
        if (retrofitUtils == null) {
            retrofitUtils = new RetrofitUtils();
        }
        if (service == null) {
            service = retrofitUtils.createWenboApi(FlirtApiService.class);
        }
        WenboParamsBuilder builder = new WenboParamsBuilder();
        builder.put("anchorId", UserManager.getInstance().getUser().getId() + "")
                .put("current", current + "")
                .put("size", REQUEST_DATA_SIZE + "");
//                .put("startLevel", startLevel + "")
//                .put("evTypes", "")
//                .put("livingRoomUserId", livingRoomUserId + "")
//                .put("startTime", "")
//                .put("endTime", "");

        mCompositeDisposable
                .add(service.getComments(builder.build())
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<FlirtCommentBean>>() {

                    @Override
                    public void _onNext(HttpResult<FlirtCommentBean> data) {
                        if (data.isSuccess()) {
                            mFlirtCommentBean.setCurrent(data.data.getCurrent());
                            mFlirtCommentBean.setPages(data.data.getPages());
                            mFlirtCommentBean.setSize(data.data.getSize());
                            mFlirtCommentBean.setTotal(data.data.getTotal());
                            mFlirtCommentBean.getRecords().addAll(data.data.getRecords());
                            mAdapter.notifyDataSetChanged();

                            if (mAdapter.getData().size() == data.data.getTotal()) {
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            }

                            if (mAdapter.getData().size() == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                                rvComment.setVisibility(View.GONE);
                            } else {
                                emptyView.setVisibility(View.GONE);
                                rvComment.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                }));
    }

    private IAddListListener listener;//声明成员变量

    public interface IAddListListener {//创建抽象类
        void clickBank( FlirtCommentBean.RecordsDTO data);//添加抽象方法，可任意添加多个可带参数如void test(String cibtext);往下加就行
    }
}
