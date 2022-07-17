package com.fengwo.module_flirt.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.UI.activity.ChatRoomActivity;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_comment.bean.CheckAnchorStatus;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

public abstract class BaseChatRoomDialog  extends BasePopupWindow {

    public SocketRequest<WenboWsChatDataBean> mChatData;
    public CompositeDisposable mCompositeDisposable;

    public BaseChatRoomDialog(Context context, SocketRequest<WenboWsChatDataBean> chatData) {
        super(context);
        this.mChatData = chatData;
        this.mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this, getContentView());
    }


    @Override
    public void dismiss() {
        super.dismiss();
        mCompositeDisposable.dispose();
    }

    public BaseChatRoomDialog(Context context, int width, int height) {
        super(context, width, height);
    }

    public BaseChatRoomDialog(Fragment fragment) {
        super(fragment);
    }

    public BaseChatRoomDialog(Fragment fragment, int width, int height) {
        super(fragment, width, height);
    }

    public BaseChatRoomDialog(Dialog dialog) {
        super(dialog);
    }

    public BaseChatRoomDialog(Dialog dialog, int width, int height) {
        super(dialog, width, height);
    }
    /**
     * ?????
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
     *
     * ????????
     *
     * @param anchorId  ??id
     * @param headImg ????
     *
     */
    public void checkAnchorStatus(String anchorId,String headImg) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId)
                .build();
        mCompositeDisposable.add(new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .checkAnchorStatus(build).compose(RxUtils.applySchedulers()).subscribeWith(new LoadingObserver<HttpResult<CheckAnchorStatus>>() {
                    @Override
                    public void _onNext(HttpResult<CheckAnchorStatus> data) {
                        if (data.isSuccess()) {
                            if (getContext().getClass().getName().equals(ChatRoomActivity.class.getName())) {
                                getContext().finish();
                            }
                            if (data.data.getBstatus() == 0) {
                                ToastUtils.showShort(getContext(), "对方已下播");
                            } else {
                                ChatRoomActivity.startWait(getContext(), Integer.parseInt(anchorId), "-1", 0);
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

}
