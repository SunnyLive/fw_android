package com.fengwo.module_comment.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.bean.ReceiveSocketBean;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.utils.ActivitysManager;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_websocket.FWWebSocketWenBo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.CompositeDisposable;


/**
 * 创建人： min
 * 创建时间： 2017/7/11.
 */

public abstract class BaseMvpActivity<V extends MvpView, P extends BasePresenter<V>> extends BaseRxActivity {

    protected final static int PAGE_SIZE = 20;
    private boolean isFirst = true;
    private boolean isLogout;
    protected CompositeDisposable netManager;
    protected P p;
    TextView textView;
    private CommonDialog commonDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);//禁止截屏
        super.onCreate(savedInstanceState);
        p = initPresenter();
        netManager = new CompositeDisposable();
        if (null != p)
            p.attachView((V) this);
        initView();
        //initDialog();
    }

    protected P getP() {
        if (null == p) {
            p = initPresenter();
        }
        return p;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wenBoOnSocketConnectListener != null) {
            FWWebSocketWenBo.getInstance().removeListener(wenBoOnSocketConnectListener);
        }
        if (null != p) {
            p.detachView();
            p = null;
        }
        if (netManager != null) {
            netManager.clear();
        }
    }

    public abstract P initPresenter();

    private FWWebSocketWenBo.OnSocketConnectListener wenBoOnSocketConnectListener;

    public void initDialog() {

        if (FWWebSocketWenBo.getInstance() == null || !CommentUtils.isOpenFlirt) return;
        if (wenBoOnSocketConnectListener == null) {
            wenBoOnSocketConnectListener = new FWWebSocketWenBo.OnSocketConnectListener() {
                @Override
                public void onMessage(String playLoad) {
                    try {

                        JSONObject jsonObject = new JSONObject(playLoad);
                        if (jsonObject.getString("busiEvent").equals("notice")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            long noticeTime = jsonObject.getLong("timestamp");
                            String action = data.getString("action");
                            /*预约时间到 赴约弹窗*/
                            ReceiveSocketBean bean = new Gson().fromJson(data.toString(), ReceiveSocketBean.class);
                            long currentTime = System.currentTimeMillis();//当前时间
                            if (action.equals("preNotice")) {//预约提前通知主播
                                long outTime = noticeTime + 11 * 60 * 1000; // 小于这个时间 为主播可收到通知时间
                                if (outTime > currentTime) {//通知
//                                String time = TimeUtils.format2Hours((outTime - currentTime)/1000);//剩余时间
//                                String content = TextUtils.isEmpty(time) ? "1分钟内，你有一个“私撩预约”订单" : time + "后，你有一个“私撩预约”订单";
                                    //          dialogNotice(bean.getContent().getValue(), action, "同城私撩预约", "马上开启“同城私撩”", "");
                                }
                            } else if (action.equals("appointment")) {//预约时间到 通知用户进入直播间
                                long outTime = noticeTime + 3 * 60 * 1000;//该时间内 用户可以收到弹窗
                                if (outTime > currentTime) {
                                    dialogNotice(bean.getContent().getValue(), action, "私撩预约", "马上赴约", bean.getUser().getUserId());
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onReconnect() {

                }

                @Override
                public void onFaildMsg(String msgid) {
                    KLog.e("tag", "到这里了？？");
                }
            };
        }
        //预约 弹窗通知
        FWWebSocketWenBo.getInstance().addOnConnectListener(wenBoOnSocketConnectListener);
    }

    public void dialogNotice(String content, String action, String title, String sureContent, String userId) {
        if (ActivitysManager.getInstance().currentActivity() != this) return;
        if (commonDialog != null && commonDialog.isVisible()) {
            commonDialog.dismiss();
        }
        commonDialog = CommonDialog.getInstance(content, sureContent, title)
                .addOnDialogListener(new CommonDialog.OnDialogListener() {
                    @Override
                    public void cancel() {
                    }

                    @Override
                    public void sure() {//马上赴约
                        if (action.equals("preNotice")) {//预约 提前 通知主播  10
                            ARouter.getInstance().build(ArouterApi.SELECT_TAG).navigation();
                        } else if (action.equals("appointment")) {//预约时间到 通知用户进入直播间
                            ArouteUtils.toFlirtCardDetailsActivity(Integer.parseInt(userId));
                        }
                    }
                });
        commonDialog.show(getSupportFragmentManager(), "ChatRoomActivity");
    }

    Dialog logout;

    @Override
    public void tokenIInvalid() {
        if (mCurrentLifeEvent == Lifecycle.Event.ON_ANY || mCurrentLifeEvent == Lifecycle.Event.ON_STOP || mCurrentLifeEvent == Lifecycle.Event.ON_DESTROY)
            return;
//        if (!this.hasWindowFocus()) return;
//        if (isLogout) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SPUtils1.remove(BaseMvpActivity.this, "userinfo");
                if (null == logout) {
                    logout = DialogUtil.getAlertDialog(BaseMvpActivity.this, "重新登录", "登录过期，请重新登录！", "确定", "", false, new DialogUtil.AlertDialogBtnClickListener() {
                        @Override
                        public void clickPositive() {
                            ActivitysManager.getInstance().popAll();
                            ArouteUtils.toPathWithId(ArouterApi.LOGIN_ACTIVITY);
                        }

                        @Override
                        public void clickNegative() {

                        }
                    });
                }
                try {
                    if (!logout.isShowing() && !BaseMvpActivity.this.isFinishing() && !BaseMvpActivity.this.isDestroyed())
                        logout.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
