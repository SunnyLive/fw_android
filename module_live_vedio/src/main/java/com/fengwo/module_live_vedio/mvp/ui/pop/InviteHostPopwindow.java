package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseNiceDialog;
import com.fengwo.module_comment.base.BasePopWindow;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.AttentionHostDto;
import com.fengwo.module_live_vedio.mvp.presenter.LivingRoomPresenter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.InviteHostAdapter;
import com.fengwo.module_live_vedio.mvp.ui.df.BaseDialogFragment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.umeng.socialize.view.BaseDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.PopupWindowCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/15
 */
public class InviteHostPopwindow extends PopupWindow {

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private InviteHostAdapter mAdapter;
    private List<AttentionHostDto> mListData = new ArrayList<>();

    private ImageView ivClose;
    private EditText editSearch;
    private LinearLayout llTips;

    public LivingRoomPresenter presenter;
    public boolean isSingle;
    public Context context;
    public String roomId;

    public InviteHostPopwindow(Context context, LivingRoomPresenter presenter, boolean isSingle, String roomId) {
        super(context);
        if (context instanceof FragmentActivity){
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            fragmentActivity.getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY){
                        if (isShowing()) dismiss();
                    }
                }
            });
        }
        this.context = context;
        this.presenter = presenter;
        this.isSingle = isSingle;
        this.roomId = roomId;
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(LayoutInflater.from(context).inflate(R.layout.live_pop_invite_host,null,false));
        setOutsideTouchable(true);
        setAnimationStyle(R.style.livePopWindowStyle);
        initView();
    }

    private void getData() {
        new RetrofitUtils().createApi(LiveApiService.class).getAttentionHost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult<List<AttentionHostDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<AttentionHostDto>> data) {
                        if (data.isSuccess()) {
                            if (data.data != null && data.data.size() > 0) {
                                mAdapter.setNewData(data.data);
                            } else {
                                llTips.setVisibility(View.VISIBLE);
                            }
                        }
                        smartRefreshLayout.closeHeaderOrFooter();
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    protected void initView() {
        smartRefreshLayout = getContentView().findViewById(R.id.smartrefreshlayout);
        recyclerView = getContentView().findViewById(R.id.rv_invite_host_pop);
        ivClose = getContentView().findViewById(R.id.iv_close);
        editSearch = getContentView().findViewById(R.id.edit_search);
        llTips = getContentView().findViewById(R.id.ll_tips);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            getData();
        });
        mAdapter = new InviteHostAdapter(mListData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (isSingle) {
                    presenter.inviteFriendSinglePk(mAdapter.getData().get(position).getUserId());
                    dismiss();
                } else {
                    List<Integer> list = new ArrayList();
                    list.add(mAdapter.getData().get(position).getUserId());
                    presenter.inveteFriendTeamPk(list, roomId);
                }

            }
        });

        getData();
    }

    public void setIsSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public void show(FragmentActivity activity, String roomId) {
        this.roomId = roomId;
        showAtLocation(activity.findViewById(android.R.id.content), Gravity.BOTTOM,0,0);
    }

    public void show(FragmentActivity activity) {
        showAtLocation(activity.findViewById(android.R.id.content), Gravity.BOTTOM,0,0);
    }
}
