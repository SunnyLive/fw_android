package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent;
import com.fengwo.module_live_vedio.mvp.dto.NewTouTiaoListDto;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.NewToutiaoListAdapter;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class ToutiaoListPopwindow extends BasePopupWindow {

    @Autowired
    UserProviderService userService;

    private View btnCancle;
    private RecyclerView rv;
    NewToutiaoListAdapter mAdapter;
    private LiveApiService service;
    private int channelId;
    private Context mContext;
    private NewTouTiaoListDto touTiaoDto;

    private ImageView ivWenhao;

    private boolean isFirstClickItem = true;


    public ToutiaoListPopwindow(Context context, int channelId) {
        super(context);
        ARouter.getInstance().inject(this);
        setPopupGravity(Gravity.CENTER);
        mContext = context;
        this.channelId = channelId;
        rv = findViewById(R.id.rv);
        btnCancle = findViewById(R.id.btn_toutiao_close);
        ivWenhao = findViewById(R.id.iv_wenhao);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ivWenhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int[] location1 = new int[2];
                    view.getLocationOnScreen(location1);
                    int scr = ScreenUtils.getScreenWidth(mContext);
                    L.e("--------------", location1[0] + "--------------" + scr);
                    listener.onRuleShow(location1[0], location1[1] + view.getHeight());
                }
            }
        });
        rv.setLayoutManager(new GridLayoutManager(context, 3));
        findViewById(R.id.btn_toutiao_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (touTiaoDto == null) return;
                if (touTiaoDto.getGiftPackagePrice() <= 0) return;
                if (touTiaoDto.getGiftPackagePrice() > userService.getUserInfo().balance) {
                    dismiss();
                    DialogUtil.showNoMoneyDialog(getContext(),
                            new DialogUtil.AlertDialogBtnClickListener() {
                                @Override
                                public void clickPositive() {
                                    dismiss();
                                    RxBus.get().post(new ShowRechargePopEvent());
                                }

                                @Override
                                public void clickNegative() {
                                }
                            });
                } else {
                    sendToutiaoGift(touTiaoDto.getId());
                }
            }
        });
        service = new RetrofitUtils().createApi(LiveApiService.class);
        getToutiaoList();
    }

    private void sendToutiaoGift(int id) {
        service.sendToutiaoGift(new HttpUtils.ParamsBuilder()
                .put("id", id + "")
                .put("isAddition", "0")
                .put("targetId", channelId + "")
                .build())
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<PopoDto>>() {
                    @Override
                    public void _onNext(HttpResult<PopoDto> data) {
                        if (data.isSuccess()) {
                            userService.getUserInfo().setExperience(data.data.getExperience().toString());
                            userService.setUsetInfo(userService.getUserInfo());
                            RxBus.get().post(new PaySuccessEvent(data.data.getExperience().toString()));
                            dismiss();
                        } else {
                            ToastUtils.showShort(mContext, data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(mContext, msg);
                    }
                });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_toutiaolist);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    private void getToutiaoList() {
        service.getToutiaoListNew()
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<List<NewTouTiaoListDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<NewTouTiaoListDto>> data) {
                        if (data.isSuccess()) {
                            touTiaoDto = data.data.get(0);
                            mAdapter = new NewToutiaoListAdapter(data.data);
                            rv.setAdapter(mAdapter);
                            mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                                touTiaoDto = data.data.get(position);
                                int left = view.getLeft();
                                if (listener != null && position != mAdapter.getCheckPosition() || isFirstClickItem) {
                                    isFirstClickItem = false;
                                    List<String> rules = new ArrayList<>();
                                    int[] location = new int[2];
                                    view.getLocationOnScreen(location);
                                    listener.onShow(data.data.get(position).getContain(), data.data.get(position).getGiftPackageName(), location[0], location[1] + view.getHeight());
                                    //     listener.onShow(data.data.get(position).getContain(), data.data.get(position).getGiftPackageName(), view.getLeft(), location[1] + view.getHeight());
                                    //       listener.onShow(data.data.get(position).getContain(), data.data.get(position).getGiftPackageName(), location[0]-30, location[1] + view.getHeight());
                                }
                                mAdapter.setCheckPosition(position);
                            });
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    static OnShowRuleListener listener;

    public void SetOnShowRuleListener(OnShowRuleListener l) {
        listener = l;
    }

    public interface OnShowRuleListener {
        void onShow(List<String> rules, String giftPackageName, int left, int bottom);

        void onRuleShow(int left, int bottom);
    }
}