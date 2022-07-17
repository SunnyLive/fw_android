package com.fengwo.module_flirt.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.ChatRoomActivity;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_comment.bean.CheckAnchorStatus;
import com.fengwo.module_flirt.bean.GiftDto;
import com.fengwo.module_flirt.manager.HandleMsgManager;
import com.fengwo.module_flirt.utlis.CommonUtils;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.bean.WenboGiftMsg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

public class MyOrderPopwindow extends BasePopupWindow {
    @BindView(R2.id.rv)
    RecyclerView rv;
    @BindView(R2.id.iv_window_close)
    View ivWindowClose;
    @Autowired
    UserProviderService userProviderService;
    public MyOrderAdapter myOrderAdapter;
    private CompositeDisposable compositeDisposable;
    private FragmentManager fragmentManager;
    private String roomId;
    private GiftPopWindow giftPopWindow;
    private boolean isClickALL = true;

    public MyOrderPopwindow(Context context, FragmentManager fragmentManager, String roomId) {
        super(context);
        this.fragmentManager = fragmentManager;
        this.roomId = roomId;
        ArouteUtils.inject(this);
        ButterKnife.bind(this, getContentView());
        setPopupGravity(Gravity.CENTER);
        compositeDisposable = new CompositeDisposable();
        rv.setLayoutManager(new GridLayoutManager(context, 2));
        myOrderAdapter = new MyOrderAdapter();
        ivWindowClose.setOnClickListener(v -> {
            dismiss();
        });
        rv.setAdapter(myOrderAdapter);
        myOrderAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            MyOrderDto bean = (MyOrderDto) baseQuickAdapter.getData().get(i);
            if (view.getId() == R.id.btn_submit) {
                TextView tv = (TextView) view;
                if (tv.getText().equals("进入聊天室")) {
                    if (roomId.equals(bean.getRoomId())) return;
                    if (roomId.equals("0")) {
                        checkAnchorStatus((int) bean.getAnchorId(), bean);
                    } else {
                        quitRoom(roomId);
                        checkAnchorStatus((int) bean.getAnchorId(), bean);
                    }
                } else if (tv.getText().equals("对Ta评价")) {
                    showFinishChatPopwindow(bean, i);
                }
            } else if (view.getId() == R.id.iv_header) {
                if (bean.getStatus() == 0) {//结束
                    MineDetailActivity.startActivityWithUserId(getContext(), (int) bean.getAnchorId());
                    dismiss();
                } else {//未结束 进入聊天室
                    if (roomId.equals(bean.getRoomId())) return;
                    if (roomId.equals("0")) {
                        checkAnchorStatus((int) bean.getAnchorId(), bean);
                    } else {
                        quitRoom(roomId);
                        checkAnchorStatus((int) bean.getAnchorId(), bean);
                    }

                }
            }
            //心动小屋点击删除操作
            else if (view.getId() == R.id.iv_impression_del) {
                long anchorId = bean.getAnchorId();
                CommonDialog.getInstance("提示", "是否关闭该心动小屋", "取消", "确定", false)
                        .addOnDialogListener(new CommonDialog.OnDialogListener() {
                            @Override
                            public void cancel() {
                            }

                            @Override
                            public void sure() {
                                clearData(String.valueOf(anchorId));
                            }
                        }).show(fragmentManager, "clearData");
            }
        });

    }

    /**
     * 退出直播间
     */
    @SuppressLint("CheckResult")
    public void quitRoom(String roomId) {
        RequestBody build = new WenboParamsBuilder()
                .put("roomId", roomId)
                .build();
        new RetrofitUtils().createWenboApi(FlirtApiService.class).quitRoom(build).compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    /**
     * 检测主播直播状态
     */
    public void checkAnchorStatus(int anchorId, MyOrderDto bean) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .build();
        compositeDisposable.add(new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .checkAnchorStatus(build).compose(RxUtils.applySchedulers()).subscribeWith(new LoadingObserver<HttpResult<CheckAnchorStatus>>() {
                    @Override
                    public void _onNext(HttpResult<CheckAnchorStatus> data) {
                        if (data.isSuccess()) {
                            if (getContext().getClass().getName().equals(ChatRoomActivity.class.getName())) {
                                getContext().finish();
                            }
                            if (data.data.getBstatus() == 0) {
                                ToastUtils.showShort(getContext(), "主播已关播");
                            } else {
                                ChatRoomActivity.startWait(getContext(), (int) bean.getAnchorId(), "-1", 0);
                            }
                            dismiss();
                        } else {
                            ToastUtils.showShort(getContext(), data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        ToastUtils.showShort(getContext(), msg);
                        dismiss();
                    }
                }));
    }

    /**
     * 显示评价
     *
     * @param bean
     */
    public void showFinishChatPopwindow(MyOrderDto bean, int position) {
        giftPopWindow = new GiftPopWindow(getContext(), fragmentManager, bean.getAnchorId());
        giftPopWindow.setIsChatRoom(false, false);
        giftPopWindow.setBalance(UserManager.getInstance().getUser().balance);
        giftPopWindow.setAnchorInfo(bean.getHeadImg(), bean.getNickname());
        giftPopWindow.setOnGiftSendListener(new GiftPopWindow.OnGiftSendListener() {
            @Override
            public void onNormalGiftSend(GiftDto gift) {
                if (CommonUtils.showTipMoney(getContext(), gift.price, fragmentManager)) return;
                sendNormalGift(bean.getAnchorId(), gift);
            }

            @Override
            public void onTimeGiftSend(GiftDto gift, boolean isFirstEnter) {
                if (CommonUtils.showTipMoney(getContext(), gift.price, fragmentManager)) return;

            }
        });
        FinishChatPopwindow popwindow = new FinishChatPopwindow(getContext(), bean.getAnchorId(), bean.getRoomId(), bean.getLivingRoomUserId(), userProviderService);
        popwindow.addOnClickListener(new FinishChatPopwindow.OnSureListener() {
            @Override
            public void sure() {
                //   dismiss();
                bean.setEvaluationStatus(1);
                myOrderAdapter.getData().remove(position);
                myOrderAdapter.notifyDataSetChanged();
                popwindow.dismiss();
                if (myOrderAdapter.getData().size() == 1) {
                    dismiss();
                }
            }

            @Override
            public void onSendGift() {
                giftPopWindow.setBalance(UserManager.getInstance().getUser().balance);
                giftPopWindow.setOnlyGift(true);
                giftPopWindow.setIsCommend(true);
                giftPopWindow.showPopupWindow();
            }

            @Override
            public void onFail() {
                dismiss();
            }

            @Override
            public void onDis() {
                popwindow.dismiss();
            }
        });
        popwindow.showPopupWindow();
    }



    @Override
    public void dismiss() {
        super.dismiss();
        compositeDisposable.dispose();
    }

    /**
     * 余额处理
     */
    private void handleBalance() {
        userProviderService.updateWallet(integer -> {
            UserInfo userInfo = userProviderService.getUserInfo();
            userInfo.balance = integer;
            userProviderService.setUsetInfo(userInfo);
        });
    }

    public void sendNormalGift(long anchorId, GiftDto gift) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .put("giftId", gift.id + "")
                .put("quantity", "1").build();
        compositeDisposable.add(new RetrofitUtils().createWenboApi(FlirtApiService.class).sendNormalGift(build)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            showGiftIfsend(gift);
                            RxBus.get().post(new PaySuccessEvent(""));
                            ToastUtils.showShort(getContext(), "赠送成功");
                            handleBalance();
                            if (giftPopWindow != null) giftPopWindow.dismiss();
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                        if (giftPopWindow != null) giftPopWindow.dismiss();
                    }
                }));
    }

    private void showGiftIfsend(GiftDto giftDto) {
        WenboGiftMsg msg = new WenboGiftMsg();
        WenboGiftMsg.Gift gift = new WenboGiftMsg.Gift();
        gift.setBigImgPath(giftDto.bigImgPath);
        gift.setGiftName(giftDto.giftName);
        gift.setGiftPrice(giftDto.price + "");
        gift.setSmallImgPath(giftDto.smallImgPath);
        gift.setCharmValue(giftDto.charmValue);
        msg.setGift(gift);
        WenboGiftMsg.User user = new WenboGiftMsg.User();
        UserInfo myUserInfo = UserManager.getInstance().getUser();
        user.setNickname(myUserInfo.nickname);
        user.setHeadImg(myUserInfo.headImg);
        msg.setUser(user);
//        addGift(msg);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_my_order);
    }

    /**
     * 清除数据
     */
    public void clearData(String ids) {
        L.e("ids " + ids);
        RequestBody build = new WenboParamsBuilder()
                //.put("ids", ids)
                .put("ids", ids)
                .build();
        compositeDisposable.add(new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .clearZhuBoList(build).compose(RxUtils.applySchedulers()).subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            if (myOrderAdapter.getData().size() <= 1) {
                                if (myOrderAdapter.getData().size() == 1) {
                                    myOrderAdapter.remove(0);
                                }
                                if (onFinishListener != null) onFinishListener.dimiss();
                                dismiss();
                            } else {
                                getOrderList();
                            }
                        } else {
                            ToastUtils.showShort(getContext(), data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        ToastUtils.showShort(getContext(), msg);
                        dismiss();
                    }
                }));
    }

    /**
     * 获取订单列表
     */
    private void getOrderList() {
        compositeDisposable.add(new RetrofitUtils().createWenboApi(FlirtApiService.class).getMyOrder().compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<List<MyOrderDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<MyOrderDto>> data) {
                        setNewData(data.data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        ToastUtils.showShort(getContext(), msg);
                    }
                }));
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultAlphaAnimation(false);
    }

    @Override
    public void dismiss(boolean animateDismiss) {
        super.dismiss(animateDismiss);
        //判断是否有数据 没有则隐藏点单浮窗
        if (myOrderAdapter.getData() == null || myOrderAdapter.getData().size() == 0) {
            if (onFinishListener != null) onFinishListener.dimiss();
        }
        myOrderAdapter.cancelAllTimers();
    }

    public void setNewData(List<MyOrderDto> data) {
        if (myOrderAdapter == null) return;
        if (!roomId.equals("0")) {
            ArrayList<MyOrderDto> myOrderDtos = new ArrayList<>(data);
            for (MyOrderDto bean : myOrderDtos) {
                if (bean.getRoomId().equals(roomId)) {
                    data.remove(bean);
                    data.add(0, bean);
                }
            }
        }
        myOrderAdapter.setNewData(data);
    }

    public interface OnFinishListener {
        void dimiss();
    }

    public OnFinishListener onFinishListener;

    public void addFinishListener(OnFinishListener listener) {
        onFinishListener = listener;
    }

    private class MyOrderAdapter extends BaseQuickAdapter<MyOrderDto, BaseViewHolder> {
        private final CountBackUtils countBackUtils;
        private boolean isSelect;
        private final int size;
        private final long inTime;


        public MyOrderAdapter() {
            super(R.layout.adapter_my_order);
//            size = DensityUtil.dip2px(getContext(), getContext().getResources().getDimension(R.dimen.dp_4));
            size = (int) getContext().getResources().getDimension(R.dimen.dp_8);
            countBackUtils = new CountBackUtils();
            String interTime = (String) SPUtils1.get(getContext(), "Flirt_Time", "");
            inTime = TextUtils.isEmpty(interTime) ? 1 : Integer.parseInt(interTime);
//            countBackUtils.countBack(-1, 10, new CountBackUtils.Callback() {
//                @Override
//                public void countBacking(long time) {
//                    notifyDataSetChanged();
//                }
//
//                @Override
//                public void finish() {
//
//                }
//            });/**/
        }

        public void cancelAllTimers() {
            countBackUtils.destory();
        }


        public void setCurrentImpression(String value) {

        }


        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
        @Override
        protected void convert(@NonNull BaseViewHolder helper, MyOrderDto item) {
            TextView tvTime = helper.getView(R.id.tv_time);
            TextView tvTimeEnd = helper.getView(R.id.tv_time_end);
            View tvTimeParent = helper.getView(R.id.ll_impression_parent);
            View root = helper.getView(R.id.root);
            ImageView cHeader = helper.getView(R.id.iv_header);
            TextView tvSubmit = helper.getView(R.id.btn_submit);
            TextView tvName = helper.getView(R.id.tv_name);

            if (item.getExpireTime() > 0) {
                tvTimeParent.setVisibility(View.VISIBLE);
                tvTime.setText("印象：" + (item.getExpireTime() / (1000 * inTime)));//1 = 5秒
            } else {
                item.setStatus(0);
            }

            helper.addOnClickListener(R.id.btn_submit, R.id.iv_header, R.id.iv_impression_del);

            root.setSelected(false);


            tvName.setText(item.getNickname());
            if (!TextUtils.isEmpty(item.getHeadImg())) {
                ImageLoader.loadRoute4Img(cHeader, item.getHeadImg(), size, size, 0, 0);
            }
            if (item.getRoomId().equals(roomId)) {
                root.setSelected(true);
                helper.setGone(R.id.iv_impression_del, false);
                tvName.setTextColor(mContext.getResources().getColor(R.color.text_white));
                HandleMsgManager.getInstance().cleanMes(roomId);
                tvSubmit.setTextColor(ContextCompat.getColor(mContext, R.color.text_white));
                tvSubmit.setSelected(false);
            }
            helper.setGone(R.id.iv_impression_del,true);
            if (item.getStatus() == 0) {//0-已结束 1-未结束
                tvTimeEnd.setVisibility(View.VISIBLE);
                tvTimeParent.setVisibility(View.GONE);
                tvTimeEnd.setText("已结束");
                if (item.getEvaluationStatus() == 0) {// 0-未评价,1-已评价
                    tvSubmit.setText("对Ta评价");
                    tvSubmit.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_63A5FF));
                    tvSubmit.setSelected(false);
                } else {
                    tvSubmit.setText("已评价");
                    tvSubmit.setTextColor(ContextCompat.getColor(getContext(), R.color.text_99));
                    tvSubmit.setSelected(false);
                }
                if (item.getRoomId().equals(roomId)) {
                    tvSubmit.setText("聊天中");
                    tvSubmit.setSelected(false);
                    helper.setGone(R.id.iv_impression_del,false);
                    tvSubmit.setTextColor(ContextCompat.getColor(getContext(), R.color.text_white));
                    root.setSelected(true);
                    HandleMsgManager.getInstance().cleanMes(roomId);
                }
                tvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.text_white));
                helper.setGone(R.id.tv_unread, false);
                HandleMsgManager.getInstance().cleanMes(item.getRoomId());
                helper.setChecked(R.id.iv_order_select, item.isCheck());
                helper.setVisible(R.id.iv_order_select, isSelect);

            } else {
                tvSubmit.setText(item.getRoomId().equals(roomId) ? "聊天中" : "进入聊天室");
                helper.setGone(R.id.iv_impression_del,false);
                tvSubmit.setSelected(!item.getRoomId().equals(roomId));
                tvSubmit.setTextColor(item.getRoomId().equals(roomId)
                        ? ContextCompat.getColor(mContext, R.color.text_white)
                        : ContextCompat.getColor(mContext, R.color.color_FE3C9C)
                );
                //聊天中的时候 显示未读消息数
                int unReadSize = HandleMsgManager.getInstance().getSize(item.getRoomId());
                helper.setGone(R.id.tv_unread, unReadSize > 0);
                helper.setText(R.id.tv_unread, unReadSize > 99 ? "99+" : String.valueOf(unReadSize));
                helper.setVisible(R.id.iv_order_select, false);
            }


            helper.setOnCheckedChangeListener(R.id.iv_order_select, (buttonView, isChecked) -> {
                item.setCheck(isChecked);
                if (onCheckListener != null) onCheckListener.check(isChecked);
            });
            helper.setOnTouchListener(R.id.iv_order_select, (v, event) -> {
                isClickALL = false;
                return false;
            });

        }

        public void setSelect(boolean isSelect) {
            this.isSelect = isSelect;
            notifyDataSetChanged();
        }

        public void selectAll(boolean isSelectAll) {
            for (MyOrderDto item : myOrderAdapter.getData()) {
                if (isSelectAll && item.getStatus() == 0) {
                    item.setCheck(true);
                } else {
                    item.setCheck(false);
                }
            }
            notifyDataSetChanged();
        }

        public void addCheckedListener(OnCheckListener listener) {
            onCheckListener = listener;
        }

        public OnCheckListener onCheckListener;
    }

    public interface OnCheckListener {
        void check(boolean isChecked);
    }
}
