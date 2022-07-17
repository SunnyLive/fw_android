package com.fengwo.module_main.mvp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_chat.mvp.ui.activity.publish.PostCardActivity;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_flirt.UI.certification.FlirtCertificationActivity;
import com.fengwo.module_flirt.UI.certification.PreStartWbActivity;
import com.fengwo.module_flirt.dialog.CertificationDialog;
import com.fengwo.module_live_vedio.mvp.ui.activity.zhubo.StartLiveActivity;
import com.fengwo.module_login.mvp.ui.activity.RealNameActivity;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_main.R;
import com.umeng.socialize.view.BaseDialog;

import razerdp.basepopup.BasePopupWindow;

public class MainMenuPopup extends BasePopupWindow {

    private Context context;

    public MainMenuPopup(Context context) {
        super(context);
        this.context = context;
        setBackground(R.color.alpha_65_black);
        findViewById(R.id.btn_main_menu_social).setOnClickListener(v -> {
            // 新建社交
            Intent intent = new Intent(context, PostCardActivity.class);
            context.startActivity(intent);
            dismiss();
        });
        findViewById(R.id.btn_main_menu_live).setOnClickListener(v -> {
            // 开启直播
            if (UserManager.getInstance().getUser().isReanName()) {
                Intent intent = new Intent(context, StartLiveActivity.class);
                context.startActivity(intent);
            } else {
                showIsRealNameDialog();
            }
            dismiss();
        });
        View wenboBtn = findViewById(R.id.btn_main_menu_short);
        wenboBtn.setVisibility(UserManager.getInstance().getUser().isWenboRole() ? View.VISIBLE : View.GONE);
        wenboBtn.setOnClickListener(v -> {
            // 开启文播
            if (!UserManager.getInstance().getUser().isReanName()) {
                showIsRealNameDialog();
            } else if (!UserManager.getInstance().getUser().isWenboPush()&&!UserManager.getInstance().getWenboCer()) {
                showIsFlirtDialog();
            } else {
                Intent intent = new Intent(getContext(), PreStartWbActivity.class);
                getContext().startActivity(intent);
            }
            dismiss();
        });
        findViewById(R.id.btn_main_menu_small).setOnClickListener(v -> {
//            Intent intent = new Intent(getContext(), TCVideoRecordActivity.class);
            Intent intent = new Intent(getContext(), FlirtCertificationActivity.class);
            getContext().startActivity(intent);
            dismiss();
        });
        findViewById(R.id.root).setOnClickListener(v -> dismiss());
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.main_pop_main_menu);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    private void showIsRealNameDialog() {
        String msg = "";
        switch (UserManager.getInstance().getUser().getMyIsCardStatus()) {
            case UserInfo.IDCARD_STATUS_NULL:
                msg = "该账户未进行主播认证";
                break;
            case UserInfo.IDCARD_STATUS_ING:
                msg = "该账户主播认证中";
                break;
            case UserInfo.IDCARD_STATUS_NO:
                msg = "该账户主播认证不通过";
                break;
        }
        CustomerDialog dialog = new CustomerDialog.Builder(context).setTitle("提示").setMsg(msg)
                .setNegativeButton("取消", () -> {

                })
                .setPositiveButton("去主播认证", () -> {
                    if (UserManager.getInstance().getUser().getMyIsCardStatus() == UserInfo.IDCARD_STATUS_NULL)
                        RealNameActivity.start(context, UserManager.getInstance().getUser().getMyIsCardStatus());
                }).create();
        dialog.show();
        if (UserManager.getInstance().getUser().getMyIsCardStatus() != UserInfo.IDCARD_STATUS_NULL) {
            dialog.hideCancle();
        }
//        CertificationDialog certificationDialog = new CertificationDialog(context, msg, "去实名认证",
//                new CertificationDialog.onPositiveListener() {
//                    @Override
//                    public void onPositive() {
//                        if (UserManager.getInstance().getUser().myIsCardStatus == UserInfo.IDCARD_STATUS_NULL)
//                        RealNameActivity.start(context, UserManager.getInstance().getUser().myIsCardStatus);
//                    }
//                });
//        certificationDialog.show();
    }

    private void showIsFlirtDialog() {
        CertificationDialog certificationDialog = new CertificationDialog(context, "个人资料不完善，请先完善信息", "完善资料",
                new CertificationDialog.onPositiveListener() {
                    @Override
                    public void onPositive() {
                        Intent intent = new Intent(context, FlirtCertificationActivity.class);
                        context.startActivity(intent);
                    }
                });
        certificationDialog.show();
    }
}
