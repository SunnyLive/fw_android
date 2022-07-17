/*
 *  为后辈们做点贡献
 *
 *  这里原本是没有注释的
 *
 *  这个页面应该是点击用户资料  进入我的工会的页面
 *
 *  那么这个页面的作用就是展示用户所有的公会
 *  申请的和没有申请的都在这个里面
 *
 *  不用感谢我，请叫我活雷锋 2020-10-27
 *
 * */

package com.fengwo.module_login.mvp.ui.activity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.UnionInfo;
import com.fengwo.module_login.mvp.presenter.MyUnionPresenter;
import com.fengwo.module_login.mvp.ui.iview.IMyUnionView;
import com.fengwo.module_login.utils.UserManager;

import androidx.core.content.ContextCompat;
import butterknife.BindView;

public class MyGonghuiActivity extends BaseMvpActivity<IMyUnionView, MyUnionPresenter> implements IMyUnionView {


    @BindView(R2.id.cl_union_anchor)
    View mViewUnionAnchor;           //主播公会 parent
    @BindView(R2.id.iv_union_anchor_heard)
    ImageView mIvAnchorUnion;        //主播公会头像
    @BindView(R2.id.tv_union_anchor_name)
    TextView mTvAnchorUnionName;
    @BindView(R2.id.tv_union_anchor_id)
    TextView mTvAnchorUnionId;
    @BindView(R2.id.tv_union_anchor_desc)
    TextView mTvAnchorUnionDesc;
    @BindView(R2.id.btn_union_anchor)
    Button mBtnAnchorUnion;
    @BindView(R2.id.tv_union_anchor_error)
    TextView mTvUnionAnchorError;
    @BindView(R2.id.tv_union_anchor_label)
    TextView mTvUnionAnchorLabel;

    @BindView(R2.id.cl_union_iliao)
    View mViewUnionIliao;            //i撩公会 parent
    @BindView(R2.id.iv_union_iliao_heard)
    ImageView mIvIliaoUnion;         //i撩公会头像
    @BindView(R2.id.tv_union_iliao_name)
    TextView mTvIliaoUnionName;
    @BindView(R2.id.tv_union_iliao_id)
    TextView mTvIliaoUnionId;
    @BindView(R2.id.tv_union_iliao_desc)
    TextView mTvIliaoUnionDesc;
    @BindView(R2.id.btn_union_iliao)
    Button mBtnIliaoUnion;
    @BindView(R2.id.tv_union_iliao_error)
    TextView mTvUnionIliaoError;
    @BindView(R2.id.tv_union_expert_label)
    TextView mTvUnionExpertLabel;

    @Override
    public MyUnionPresenter initPresenter() {
        return new MyUnionPresenter();
    }

    @Override
    protected void initView() {
        setWhiteTitle("我的公会");
        mBtnAnchorUnion.setOnClickListener(v -> {
            ARouter.getInstance().build(ArouterApi.SEARCH_UNION_ACTION)
                    .withInt("unionType",GonghuiActivity.ANCHOR_UNION)
                    .navigation();
        });

        mBtnIliaoUnion.setOnClickListener(v -> {
            ARouter.getInstance().build(ArouterApi.SEARCH_UNION_ACTION)
                    .withInt("unionType",GonghuiActivity.EXPERT_UNION)
                    .navigation();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        p.getUnionInfo();
        if (UserManager.getInstance().getUser().myIsCardStatus == 1) {
            mViewUnionAnchor.setVisibility(View.VISIBLE);
        }
        if (UserManager.getInstance().getUser().wenboAnchorStatus  == 1){
            mViewUnionIliao.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_mygonghui;
    }

    @Override
    public void onUnionInfoSuccess(UnionInfo u) {
        //申请状态 0：拒绝 1：申请中 2：通过
        String unionId = "公会ID： ";
        final Drawable[] DRAWABLE_PARENT = {
                ContextCompat.getDrawable(this, R.drawable.icon_union_item_bg),
                ContextCompat.getDrawable(this, R.drawable.icon_union_anchor),
                ContextCompat.getDrawable(this, R.drawable.icon_union_iliao)};

        mViewUnionAnchor.setBackground(DRAWABLE_PARENT[1]);
        mViewUnionIliao.setBackground(DRAWABLE_PARENT[2]);

        if (u.familyVO != null) {
            mViewUnionAnchor.setBackground(DRAWABLE_PARENT[0]);
            mTvAnchorUnionName.setText(u.familyVO.familyName);
            mTvAnchorUnionId.setText(unionId + u.familyVO.familyCode);
            mTvAnchorUnionDesc.setText(u.familyVO.familyIntroduction);
            ImageLoader.loadImg(mIvAnchorUnion, u.familyVO.familyLogo);
            mTvUnionAnchorError.setText("");
            switch (u.familyVO.applyStatus) {
                case 0:
                    mTvUnionAnchorError.setText("申请公会失败");
                    break;
                case 1:
                    mBtnAnchorUnion.setText("申请中");
                    mBtnAnchorUnion.setEnabled(false);
                    break;
                case 2:
                    mTvUnionAnchorLabel.setVisibility(View.VISIBLE);
                    mBtnAnchorUnion.setVisibility(View.GONE);
                    break;
            }
        }

        if (u.wenFamilyVO != null){
            mViewUnionIliao.setBackground(DRAWABLE_PARENT[0]);
            mTvIliaoUnionName.setText(u.wenFamilyVO.familyName);
            mTvIliaoUnionId.setText(unionId + u.wenFamilyVO.familyCode);
            mTvIliaoUnionDesc.setText(u.wenFamilyVO.familyIntroduction);
            ImageLoader.loadImg(mIvIliaoUnion, u.wenFamilyVO.familyLogo);
            mTvUnionIliaoError.setText("");
            switch (u.wenFamilyVO.applyStatus) {
                case 0:
                    mTvUnionIliaoError.setText("申请公会失败");
                    break;
                case 1:
                    mBtnIliaoUnion.setText("申请中");
                    mBtnIliaoUnion.setEnabled(false);
                    break;
                case 2:
                    mBtnIliaoUnion.setVisibility(View.GONE);
                    mTvUnionExpertLabel.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
