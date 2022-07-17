package com.fengwo.module_login.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.utils.AppUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class AboutUsActivity extends BaseMvpActivity {

    @BindView(R2.id.tv_version)
    TextView tvVersion;
    @BindView(R2.id.tv_protocol)
    TextView tvProtocol;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
//        setTitleBackground(getResources().getColor(R.color.white));
        new ToolBarBuilder().setBackIcon(R.drawable.ic_back_black).setTitleColor(R.color.text_33)
                .setTitle("关于我们").showBack(true).build();

        tvProtocol.setText(Html.fromHtml(getResources().getString(R.string.about_us_rule)));
        tvVersion.setText(AppUtils.getVersionName(this));
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_about_us;
    }

    @OnClick({R2.id.tv_protocol, R2.id.btn_back, R2.id.tv_phone})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_protocol) {
            BrowserActivity.startRuleActivity(this);
        } else if (id == R.id.btn_back) {
            finish();
        } else if (id == R.id.tv_phone) {
            new RxPermissions(this).request(Manifest.permission.CALL_PHONE)
                    .subscribe(new Consumer<Boolean>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                Uri data = Uri.parse("tel:" + "4000051118");
                                intent.setData(data);
                                startActivity(intent);
                            }
                        }
                    });

        }
    }
}
