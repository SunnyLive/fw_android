package com.fengwo.module_flirt.utlis;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_flirt.R;
import com.fengwo.module_login.mvp.ui.activity.ChongzhiActivity;
import com.fengwo.module_login.mvp.ui.activity.RealNameActivity;
import com.fengwo.module_login.utils.UserManager;

import androidx.fragment.app.FragmentManager;

/**
 * @Author BLCS
 * @Time 2020/5/23 10:59
 */
public class CommonUtils {
    /**
     * 金额不足提示
     */
    public static boolean showTipMoney(Context context, double allPrice, FragmentManager fm) {
        if (allPrice > UserManager.getInstance().getUser().getBalance()) {
            CommonDialog.getInstance("", "您的账户余额不足，请先充值", "取消", "立即充值", true)
                    .addOnDialogListener(new CommonDialog.OnDialogListener() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void sure() {
                            Intent intent = new Intent(context, ChongzhiActivity.class);
                            context.startActivity(intent);
                        }
                    }).show(fm, "AppointmentPopwindow");
            return true;
        }
        return false;
    }

    /**
     * 点击成为达人
     * i撩 开播 判断逻辑
     *
     * @param context
     * @param type    目前 type == 1 表示是主播申请 2表示是达人申请
     */
    public static void playing(Context context, int type) {
        chooseRealName(context, type == Common.SKIP_ANCHOR,false);
    }

    public static void playing(Context context, int type, boolean isSetting) {
        chooseRealName(context, type == Common.SKIP_ANCHOR, isSetting);
    }

    private static void chooseRealName(Context context, boolean isAnchor, boolean isSetting) {
        UserInfo userInfo = UserManager.getInstance().getUser();
        //第一步 判断用户资料是否完善
        //判断用户是否实名认证
        //0 是未认证 1是已认证 2认证失败
        String content = isAnchor ? "您未进行实名认证，\n认证后可申请成为主播" : "您未进行实名认证，\n认证后可申请成为达人";
        if (userInfo.myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_YES) {
            //判断是否做过达人认证 或者主播认证
            //如果是从主播认证入口进入，就进行判断该用户是否是主播
            if (isAnchor) {
                if (userInfo.myIsCardStatus == UserInfo.IDCARD_STATUS_YES
                        || userInfo.myIsCardStatus == UserInfo.IDCARD_STATUS_NULL
                        || userInfo.myIsCardStatus == UserInfo.IDCARD_STATUS_NO) {
                    if (userInfo.myIsCardStatus != UserInfo.IDCARD_STATUS_YES) {
                        // 未认证，认证失败
                        ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                                .withInt("status", UserManager.getInstance().getUser().myIdCardWithdraw)
                                .withInt("type", Common.SKIP_ANCHOR)
                                .navigation();
                    } else {
                        if (isSetting) {
                            CustomerDialog mcd = new CustomerDialog.Builder(context)
                                    .setTitle("温馨提示")
                                    .setMsg("您未进行主播认证")
                                    .setPositiveButton("主播认证", () -> {
                                        ARouter.getInstance().build(ArouterApi.REAL_ANCHOR_ACTION)
                                                .withInt("anchor_status", userInfo.myIsCardStatus)
                                                .withInt("type", Common.SKIP_ANCHOR)
                                                .navigation();
                                    }).create();
                            mcd.show();
                        }else {
                            //一键开播检测人脸认证
                            if (!userInfo.isFace()) {
                                showFaceCheckDialog(context, userInfo);
                            } else {
                                ARouter.getInstance().build(ArouterApi.START_LIVE_ACTION).navigation();
                            }

                        }

                    }
                    //实名认证中的提示
                } else if (userInfo.myIsCardStatus == UserInfo.IDCARD_STATUS_ING) {
                    ToastUtils.showShort(context, "正在认证中，请您耐心等待", Gravity.CENTER);
                }
            } else {
                //如果是达人   就进入达人直播页面
                //达人申请状态：0申请中 1申请通过 2申请未通过 3未申请
                //这里的逻辑是达人已经实名成功  已经申请了达人  但是正在申请中
                if (UserManager.getInstance().getUser().wenboAnchorStatus == UserInfo.WENBO_STATUS_ING){
                    ToastUtils.showShort(context, "正在申请中，请您耐心等待", Gravity.CENTER);
                }
                //这里的逻辑是  是i撩进入  并且达人申请通过
                else if (UserManager.getInstance().getUser().wenboAnchorStatus == UserInfo.WENBO_STATUS_YES) {
                    //这个字段是为了区分是从i撩进入 还是从个人资料页面进入的
                    if (!userInfo.isFace()) {
                        showFaceCheckDialog(context, userInfo);
                    } else {
                        if (isSetting)
                            ARouter.getInstance().build(ArouterApi.FLIRT_CERTIFICATION).navigation();
                        else
                            ARouter.getInstance().build(ArouterApi.SELECT_TAG).navigation();
                    }
                }
                //这里的逻辑是  从i撩进入  达人申请了  但是没有申请通过
                else if (UserManager.getInstance().getUser().wenboAnchorStatus == UserInfo.WENBO_STATUS_NO){
                    CustomerDialog mcd = new CustomerDialog.Builder(context)
                            .setTitle("温馨提示")
                            .setMsg("您未进行达人认证")
                            .setPositiveButton("达人认证", () -> {
                                ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                                        .withInt("status", UserInfo.IDCARD_STATUS_YES)
                                        .withInt("type", Common.SKIP_EXPERT)
                                        .withBoolean("isReview",true)
                                        .navigation();
                            }).create();
                    mcd.show();

                }
                //没有申请过达人  就提示用户已经实名成功  去设置同城资料
                else if (UserManager.getInstance().getUser().wenboAnchorStatus == UserInfo.WENBO_STATUS_NULL){
                    CustomerDialog mcd = new CustomerDialog.Builder(context)
                            .setTitle("温馨提示")
                            .setMsg("您未进行达人认证")
                            .setPositiveButton("达人认证", () -> {
                                ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                                        .withInt("status", UserInfo.IDCARD_STATUS_YES)
                                        .withInt("type", Common.SKIP_EXPERT)
                                        .navigation();
                            }).create();
                    mcd.show();
                }
            }
        }
        //实名认证中 提示用户
        else if (userInfo.myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_ING){
            ToastUtils.showShort(context,"实名认证中,请您耐心等待", Gravity.CENTER);
        }
        //没有实名认证 或者实名认证失败  就再次进行实名认证操作
        else if (userInfo.myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NO ||
                userInfo.myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NULL){
            CustomerDialog mcd = new CustomerDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMsg(content)
                    .setPositiveButton("去认证", () -> {
                        ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                                .withInt("status", UserManager.getInstance().getUser().myIdCardWithdraw)
                                .withInt("type", isAnchor ? Common.SKIP_ANCHOR : Common.SKIP_EXPERT)
                                .navigation();
                    }).create();
            mcd.show();
        }
    }


    /**
     * i撩运营弹框
     *
     * @param context
     */
    public static void showOperation(Context context) {
        String value = context.getString(R.string.expert_operating_tip);
        final CustomerDialog.Builder builder = new CustomerDialog.Builder(context);
        builder.setTitle("如何成为达人");
        builder.setMsg(value);
        CustomerDialog dialog = builder.create();
        dialog.show();
        dialog.hideCancle();
        dialog.showCloseButton();
        dialog.tvMsg.setGravity(Gravity.START);
    }


    /**
     * 开播判断逻辑
     */
    public static void showIsRealNameDialog(Context context) {

        //todo  等ui，目前没有ui，不知道做什么鬼样子
        String msg = "";

        switch (UserManager.getInstance().getUser().myIsCardStatus) {
            case UserInfo.IDCARD_STATUS_NULL:
                msg = "该账户未进行实名认证";
                break;
            case UserInfo.IDCARD_STATUS_ING:
                msg = "该账户实名认证中";
                break;
            case UserInfo.IDCARD_STATUS_NO:
                msg = "该账户实名认证不通过";
                break;
        }
        CustomerDialog dialog = new CustomerDialog.Builder(context).setTitle("提示").setMsg(msg)
                .setNegativeButton("取消", () -> {

                })
                .setPositiveButton("确定", () -> {
                    if (UserManager.getInstance().getUser().myIsCardStatus == UserInfo.IDCARD_STATUS_NULL)
                        RealNameActivity.start(context, UserManager.getInstance().getUser().myIsCardStatus);
                }).create();
        dialog.show();
        if (UserManager.getInstance().getUser().myIsCardStatus != UserInfo.IDCARD_STATUS_NULL) {
            dialog.hideCancle();
        }
    }

    private static void showFaceCheckDialog(Context context, UserInfo userInfo) {
        CustomerDialog dialog = new CustomerDialog.Builder(context).setTitle("温馨提示")
                .setMsg("该账户未进行人脸认证")
                .setPositiveButton("去认证", () -> {
                    ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                            .withInt("type", Common.SKIP_USER)
                            .withBoolean("isReview",userInfo.myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NULL)
                            .withInt("status", userInfo.myIdCardWithdraw)
                            .navigation();
                        }
                ).create();
        dialog.show();
    }
}
