package com.fengwo.module_login.mvp.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.UserDetailDto;
import com.fengwo.module_login.mvp.presenter.UserDetailPresenter;
import com.fengwo.module_login.mvp.ui.iview.IUserDetailView;

import butterknife.BindView;
import butterknife.OnClick;

public class UserDetailActivity extends BaseMvpActivity<IUserDetailView, UserDetailPresenter> implements IUserDetailView {
    @BindView(R2.id.iv_user_desc)
    ImageView ivBack;
    @BindView(R2.id.iv_header)
    ImageView ivHeader;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_desc)
    TextView tvDesc;
    @BindView(R2.id.iv_sex)
    ImageView ivSex;
    @BindView(R2.id.tv_age)
    TextView tvAge;
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.tv_relative)
    TextView tvRelative;

    @Override
    public UserDetailPresenter initPresenter() {
        return new UserDetailPresenter();
    }

    @Override
    protected void initView() {
        int userId = getIntent().getIntExtra("id", -1);
        if (userId < 0) {
            ToastUtils.showShort(this, "用户id错误");
            finish();
            return;
        }
        p.getUserDetail(String.valueOf(userId));
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_user_detail;
    }

    @OnClick({R2.id.iv_back, R2.id.iv_add_user, R2.id.tv_relative})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.iv_add_user) {
        } else if (id == R.id.tv_relative) {

        }
    }

    @Override
    public void getUserDetail(UserDetailDto data) {
        ImageLoader.loadImg(ivHeader, data.headImg);
        tvName.setText(data.nickname);
        tvLocation.setText(data.location);
        tvDesc.setText(data.signature);
        tvAge.setText("未知");
        ivSex.setSelected(data.sex != 2);
        tvRelative.setSelected(data.isAttention == 1);
        tvRelative.setText(data.isAttention == 1 ? "已关注" : "关注");
    }
}
