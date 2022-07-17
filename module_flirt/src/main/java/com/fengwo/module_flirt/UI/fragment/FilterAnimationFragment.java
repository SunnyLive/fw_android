package com.fengwo.module_flirt.UI.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.DrawableUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.SvgaUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.multiImageview.MultiImageView;
import com.fengwo.module_flirt.Interfaces.IFlirtAnimationView;
import com.fengwo.module_flirt.Interfaces.OnCarrouselItemClickListener;
import com.fengwo.module_flirt.Interfaces.SelectListenet;
import com.fengwo.module_flirt.P.FlirtAnimationPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.FlirtCardDetailsActivity;
import com.fengwo.module_flirt.UI.certification.FlirtCertificationActivity;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.LabelTalentDto;
import com.fengwo.module_flirt.bean.ZipLabelDto;
import com.fengwo.module_flirt.bean.ZipLabelParentDto;
import com.fengwo.module_flirt.dialog.CertificationDialog;
import com.fengwo.module_flirt.utlis.BraetheInterpolator;
import com.fengwo.module_flirt.utlis.CommonUtils;
import com.fengwo.module_flirt.widget.MerryRoundView;
import com.fengwo.module_flirt.widget.OptionPopwindow;
import com.fengwo.module_login.mvp.ui.activity.RealNameActivity;
import com.fengwo.module_login.utils.UserManager;
import com.gyf.immersionbar.ImmersionBar;
import com.opensource.svgaplayer.SVGAImageView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhy.view.flowlayout.TagFlowLayout.dip2px;

/**
 * @anchor Administrator
 * @date 2020/9/22
 */
public class FilterAnimationFragment extends BaseMvpFragment<IFlirtAnimationView, FlirtAnimationPresenter> implements IFlirtAnimationView, View.OnClickListener {

    @BindView(R2.id.svga_star)
    SVGAImageView svga_star;
    @BindView(R2.id.svga_nebulae)
    SVGAImageView svga_nebulae;

    @BindView(R2.id.tv_become)
    TextView tvBecome;

    @BindView(R2.id.tv_select)
    TextView tvSelect;

    @BindView(R2.id.carrLayout)
    MerryRoundView carrLayout;

    /*底部操作按钮部分*/
    @BindView(R2.id.cl_button)
    View clButton;

    //按钮
    @BindView(R2.id.tv_btn)
    TextView tvBtn;
    //星空标签部分
    @BindView(R2.id.cl_tag)
    View clTag;
    @BindView(R2.id.im_go)
    ImageView imGo;

    /*蜂窝标签*/
    @BindView(R2.id.tv_select_1)
    TextView tvSelect1;
    @BindView(R2.id.tv_select_2)
    TextView tvSelect2;
    @BindView(R2.id.tv_select_3)
    TextView tvSelect3;

    /*根据条件推荐的主播*/
    @BindView(R2.id.cl_recommend_talent)
    View clRecommendTalent;
    /*转场动画*/
//    @BindView(R2.id.svga_transition)
//    SVGAImageView svgaTransition;
    /*选中的tag*/
    @BindView(R2.id.ll_selected_tag)
    View llSelectedTag;
    @BindView(R2.id.fl_mim_1)
    View flMim1;
    @BindView(R2.id.fl_mim_2)
    View flMim2;
    @BindView(R2.id.fl_mim_3)
    View flMim3;
    @BindView(R2.id.fl_mim_4)
    View flMim4;
    @BindView(R2.id.fl_mim_5)
    View flMim5;
    @BindView(R2.id.fl_mim_6)
    View flMim6;
    @BindView(R2.id.fl_mim_7)
    View flMim7;
    @BindView(R2.id.mim_1)
    MultiImageView mim1;
    @BindView(R2.id.mim_2)
    MultiImageView mim2;
    @BindView(R2.id.mim_3)
    MultiImageView mim3;
    @BindView(R2.id.mim_4)
    MultiImageView mim4;
    @BindView(R2.id.mim_5)
    MultiImageView mim5;
    @BindView(R2.id.mim_6)
    MultiImageView mim6;
    @BindView(R2.id.mim_7)
    MultiImageView mim7;

    //page1选中的tag
    @BindView(R2.id.tv_selected_tag1)
    TextView tvSelectedTag1;
    @BindView(R2.id.tv_selected_tag2)
    TextView tvSelectedTag2;
    @BindView(R2.id.tv_selected_tag3)
    TextView tvSelectedTag3;


    private String select1 = "", select2 = "", select3 = "";
    private int width;
    private String sex = "3";
    private String city = "";
    String maxAge = "100";
    String minAge = "0";

    private List<ZipLabelDto> zipLabelChildList = new ArrayList<>(12);
    private SvgaUtils svgaUtils;
    private SvgaUtils svgaUtils2;

    private Handler mHandler = new Handler();
    private Runnable runPage1 = new Runnable() {
        @Override
        public void run() {
            showPage(1);
        }
    };

    /*page1上面显示隐藏的view*/
    private View[] page1Views;
    /*page2上面显示隐藏的view*/
    private View[] page2Views;

    /*当前page*/
    private int page = -1;
    private ArrayList<ValueAnimator> animList;

    private Random mRandom = new Random();

    private List<MultiImageView> multiImageViews;
    private List<View> animViews;

    private List<LabelTalentDto> labelTalents;

    private SelectListenet listener;
    ZipLabelParentDto data;
    private boolean closed;
    private ValueAnimator honeycombAnim;

    @Override
    protected FlirtAnimationPresenter initPresenter() {
        return new FlirtAnimationPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_filter_animation;
    }

    @Override
    public void initView(View v) {
        super.initView(v);
        page1Views = new View[]{tvSelect, carrLayout, clTag, clButton,tvBecome};
        page2Views = new View[]{clRecommendTalent, llSelectedTag, clButton};
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarDarkIcon(true, 0.2f)
                .navigationBarColor(com.fengwo.module_comment.R.color.white)
                .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                .init();
        tvSelect1.setTypeface(BaseApplication.mApp.textFace);
        tvSelect2.setTypeface(BaseApplication.mApp.textFace);
        tvSelect3.setTypeface(BaseApplication.mApp.textFace);
        tvSelectedTag1.setTypeface(BaseApplication.mApp.textFace);
        tvSelectedTag2.setTypeface(BaseApplication.mApp.textFace);
        tvSelectedTag3.setTypeface(BaseApplication.mApp.textFace);

        animViews = new ArrayList<>(7);
        animViews.add(flMim1);
        animViews.add(flMim2);
        animViews.add(flMim3);
        animViews.add(flMim4);
        animViews.add(flMim5);
        animViews.add(flMim6);
        animViews.add(flMim7);
        multiImageViews = new ArrayList<>(7);
        multiImageViews.add(mim1);
        multiImageViews.add(mim2);
        multiImageViews.add(mim3);
        multiImageViews.add(mim4);
        multiImageViews.add(mim5);
        multiImageViews.add(mim6);
        multiImageViews.add(mim7);
        startSvgaBg();
        showPage(0);
    }


    @Override
    public boolean getUserVisibleHint() {
        //运营信息弹框
        showOperation();
        return super.getUserVisibleHint();
    }

    private void showOperation() {


        //如果是达人  就关闭 运营提示
        if (UserManager.getInstance().getUser().wenboAnchorStatus == 1) {
            tvBecome.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(),
                    R.drawable.selector_is_talent), null, null, null);
        }

        new DrawableUtil(tvBecome, new DrawableUtil.OnDrawableListener() {
            @Override
            public void onLeft(View v, Drawable left) {

            }

            @Override
            public void onRight(View v, Drawable right) {
                CommonUtils.showOperation(getContext());
            }
        });

    }

    /**
     * 背景svga
     */
    private void startSvgaBg() {
        svgaUtils = new SvgaUtils(getContext(), svga_star,0);
        svgaUtils.initAnimator();
        svgaUtils.repeat();
        svgaUtils.startAnimator("iliao_star");

        ViewGroup.LayoutParams lp = svga_nebulae.getLayoutParams();
        lp.height = (int) (ScreenUtils.getScreenWidth(getActivity()) * 1.2);
        lp.width = (int) (ScreenUtils.getScreenWidth(getActivity()) * 1.2);
        svga_nebulae.setLayoutParams(lp);
        svgaUtils2 = new SvgaUtils(getContext(), svga_nebulae,0);
        svgaUtils2.initAnimator();
        svgaUtils2.repeat();
        svgaUtils2.startAnimator("iliao_nebulae");
    }

    /**
     * 切换page
     *
     * @param page
     */
    private void showPage(int page) {
        if (this.page == page) {
            return;
        }
        if (page == 0) {
            clearParams();//清空选项
            pauseAnim();//暂停page1的抖动动画
            if (closed) {//重新获取数据开启星球（第一次进入时再BaseLiaoFragment中有调用resume()）
                resume();
            }
            tvBtn.setBackgroundResource(R.mipmap.pic_flirt_btn);
            showOrHindView(page2Views, false);//因为两个views中都有底部的clButton，所以需要先隐藏再显示
            showOrHindView(page1Views, true);
        } else if (page == 1) {
            p.resetPage();//重置页码
            close();//关闭page0的星球
           // transitionSvg();//转场动画
            getRecommendTalent();//获取推荐主播
            refreshPage1TagView();//更新page1选中的tag
            resumeAnim();//重启page1动画
            tvBtn.setBackgroundResource(R.mipmap.tv_btns);
            showOrHindView(page1Views, false);
        //    svgaTransition.setVisibility(View.VISIBLE);
            tvBtn.setEnabled(true);
            imGo.setEnabled(true);

            showOrHindView(page2Views, true);
            showHoneycombAnim(true);

        }
        this.page = page;
    }

    /**
     * 继续波动动画
     */
    public void resumeAnim() {
        if (animList != null)
            for (int i = 0; i < animList.size(); i++) {
                if (animList.get(i).isPaused()) {
                    animList.get(i).resume();
                }
            }
    }

    /**
     * 暂停波动动画
     */
    public void pauseAnim() {
        if (animList != null)
            for (int i = 0; i < animList.size(); i++) {
                if (animList.get(i).isRunning()) {
                    animList.get(i).pause();
                }
            }
    }

    /**
     * 刷新page1的选中tag
     */
    private void refreshPage1TagView() {
        String str2 = getTagName();
        List<String> list = Arrays.asList(str2.split(","));
        switch (list.size()) {
            case 1:
                tvSelectedTag1.setVisibility(View.GONE);
                tvSelectedTag2.setVisibility(View.VISIBLE);
                tvSelectedTag2.setText(list.get(0));
                tvSelectedTag3.setVisibility(View.GONE);
                break;
            case 2:
                tvSelectedTag1.setVisibility(View.VISIBLE);
                tvSelectedTag1.setText(list.get(0));
                tvSelectedTag2.setVisibility(View.GONE);
                tvSelectedTag3.setVisibility(View.VISIBLE);
                tvSelectedTag3.setText(list.get(1));
                break;
            case 3:
                tvSelectedTag1.setVisibility(View.VISIBLE);
                tvSelectedTag1.setText(list.get(0));
                tvSelectedTag2.setVisibility(View.VISIBLE);
                tvSelectedTag2.setText(list.get(1));
                tvSelectedTag3.setVisibility(View.VISIBLE);
                tvSelectedTag3.setText(list.get(2));
                break;
        }
    }

    /**
     * 获取推荐达人
     */
    private void getRecommendTalent() {
        p.setTagName(getTagName(), maxAge, minAge, String.valueOf(sex), city);
    }

    /**
     * 显示隐藏views
     *
     * @param isShow
     */
    private void showOrHindView(View[] views, boolean isShow) {
        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        }
    }

//    /**
//     * 转场动画
//     */
//    private void transitionSvg() {
//        SvgaUtils svgaTransitionUtil = new SvgaUtils(getContext(), svgaTransition);
//        svgaTransitionUtil.initAnimator(new SVGACallback() {
//            @Override
//            public void onPause() {
//
//            }
//
//            @Override
//            public void onFinished() {
//                svgaTransition.setVisibility(View.GONE);
//                showOrHindView(page2Views, true);
//                showHoneycombAnim(true);
//            }
//
//            @Override
//            public void onRepeat() {
//
//            }
//
//            @Override
//            public void onStep(int i, double v) {
//
//            }
//        });
//        svgaTransitionUtil.startAnimator("iliao_transition");
//    }

    /**
     * 蜂窝展开收拢动画
     */
    private void showHoneycombAnim(boolean isInit) {
        //这个便宜大小根据大小MultiImageView的大小决定的
        int xDeviation1 = dip2px(getActivity(), 112 / 2.0F);
        int xDeviation2 = dip2px(getActivity(), (112 + 78) / 2.0F + 10);
        int yDeviation = dip2px(getActivity(), (112 + 78) / 2.0F + 2);
        if (isInit) {
            honeycombAnim = ValueAnimator.ofFloat(0F, 1F).setDuration(800);
        } else {
            honeycombAnim.end();
            honeycombAnim = ValueAnimator.ofFloat(1F, 0F, 1F).setDuration(1600);
        }
        honeycombAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                if (isInit) {
                    for (int i = 0; i < animViews.size(); i++) {
                        changeAnim(animViews.get(i), 0.5F + 0.5F * progress);
                    }

                    //page2的控件慢慢显示出来
                    for (int i = 0; i < page2Views.length; i++) {
                        page2Views[i].setAlpha(0.4F + 0.6F * progress);
                    }
                }

                flMim2.setTranslationX(-xDeviation1 * progress);
                flMim2.setTranslationY(-yDeviation * progress);
                flMim3.setTranslationX(xDeviation1 * progress);
                flMim3.setTranslationY(-yDeviation * progress);
                flMim4.setTranslationX(xDeviation2 * progress);
                flMim5.setTranslationX(xDeviation1 * progress);
                flMim5.setTranslationY(yDeviation * progress);
                flMim6.setTranslationX(-xDeviation1 * progress);
                flMim6.setTranslationY(yDeviation * progress);
                flMim7.setTranslationX(-xDeviation2 * progress);

            }
        });
        if (isInit) {
            honeycombAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    showRepeatBtn();
                }
            });
        }
        honeycombAnim.start();
    }

    /**
     * 根据进度修改view透明度与大小
     *
     * @param view
     * @param progress
     */
    private void changeAnim(View view, float progress) {
        view.setAlpha(0.5F + 0.5F * progress);
        view.setScaleX(0.5F + 0.5F * progress);
        view.setScaleY(0.5F + 0.5F * progress);
    }

    /**
     * 播放头像的波动动画
     */
    private void showRepeatBtn() {
        if (animList == null) {
            animList = new ArrayList<>(7);
        }

        if (animList.size() == 0) {
            for (int i = 0; i < 7; i++) {
                View view = animViews.get(i);
                ValueAnimator anim = ValueAnimator.ofFloat(1, 0.9F).setDuration(5000);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = (float) animation.getAnimatedValue();
                        view.setAlpha(1 - (1 - progress) * 4F);
                        view.setScaleX(progress);
                        view.setScaleY(progress);
                    }
                });
                anim.setRepeatCount(ValueAnimator.INFINITE);
                anim.setInterpolator(new BraetheInterpolator());
                if (i != 0) {
                    anim.setStartDelay(mRandom.nextInt(5000));
                }
                animList.add(anim);
            }
        }

        for (int i = 0; i < 7; i++) {
            animList.get(i).start();
        }
    }
    OptionPopwindow optionPopwindow;
    @OnClick({R2.id.tv_btn, R2.id.tv_select, R2.id.im_go, R2.id.tv_select_1, R2.id.tv_select_2, R2.id.tv_select_3, R2.id.tv_become, R2.id.tv_refresh})
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_btn) {
            if (page == 0) {//page 0->1
                if (TextUtils.isEmpty(select1) && TextUtils.isEmpty(select2) && TextUtils.isEmpty(select3)) {
                    toastTip("请选择标签");
                } else {
                    showPage(1);
                }
            } else {//page 1->0
                showPage(0);
            }
        } else if (view.getId() == R.id.tv_select) {
            if(null==optionPopwindow){
                optionPopwindow = new OptionPopwindow(getActivity(), 0);
            }

            optionPopwindow.addOnClickListener((sex, minAge, maxAge, city) -> {
                FilterAnimationFragment.this.sex = String.valueOf(sex);
                FilterAnimationFragment.this.minAge = String.valueOf(minAge);
                FilterAnimationFragment.this.maxAge = String.valueOf(maxAge);
                FilterAnimationFragment.this.city = city;
                optionPopwindow.dismiss();
            });
            optionPopwindow.showPopupWindow();
        } else if (view.getId() == R.id.im_go) {
            if (null != listener) {
                listener.select(1);
            }
        } else if (view.getId() == R.id.tv_select_1) {
            select1 = "";
            tvSelect1.setBackgroundResource(R.mipmap.pic_select);
            tvSelect1.setText("");
            mHandler.removeCallbacks(runPage1);
        } else if (view.getId() == R.id.tv_select_2) {
            select2 = "";
            tvSelect2.setBackgroundResource(R.mipmap.pic_select);
            tvSelect2.setText("");
            mHandler.removeCallbacks(runPage1);
        } else if (view.getId() == R.id.tv_select_3) {
            select3 = "";
            tvSelect3.setBackgroundResource(R.mipmap.pic_select);
            tvSelect3.setText("");
            mHandler.removeCallbacks(runPage1);
        } else if (view.getId() == R.id.tv_become) {
            /*if (UserManager.getInstance().getUser().isWenboRole()) {//是I撩主播
                //判断资料是否完善
                if (UserManager.getInstance().getUser().isWeboSet || UserManager.getInstance().getWenboCer()) {
                    Intent intent = new Intent(getContext(), PreStartWbActivity.class);
                    getContext().startActivity(intent);
                } else {
                    showIsFlirtDialog();
                }
            } else if (UserManager.getInstance().getUser().isWenboUser()) {//是I撩用户
                showIsRealNameDialog();
            } else {
                toastTip("暂无权限");
            }*/

            CommonUtils.playing(getContext(), 2);

        } else if (view.getId() == R.id.tv_refresh) {//换一批
            if (page == 0) {
                getData();
            } else if (page == 1) {
                getRecommendTalent();
            }
        }
    }

    /**
     * 点击主播头像
     *
     * @param view
     */
    @OnClick({R2.id.fl_mim_1, R2.id.fl_mim_2, R2.id.fl_mim_3, R2.id.fl_mim_4, R2.id.fl_mim_5, R2.id.fl_mim_6, R2.id.fl_mim_7})
    protected void onHeadClick(View view) {
        if (labelTalents == null) {
            return;
        }
        int anchorId = -1;
        if (view.getId() == R.id.fl_mim_1) {
            anchorId = getAnchorId(0);
        } else if (view.getId() == R.id.fl_mim_2) {
            anchorId = getAnchorId(1);
        } else if (view.getId() == R.id.fl_mim_3) {
            anchorId = getAnchorId(2);
        } else if (view.getId() == R.id.fl_mim_4) {
            anchorId = getAnchorId(3);
        } else if (view.getId() == R.id.fl_mim_5) {
            anchorId = getAnchorId(4);
        } else if (view.getId() == R.id.fl_mim_6) {
            anchorId = getAnchorId(5);
        } else if (view.getId() == R.id.fl_mim_7) {
            anchorId = getAnchorId(6);
        }
        if (anchorId > 0) {
            FlirtCardDetailsActivity.start(getContext(), anchorId);//跳转下单页
        }
    }

    /**
     * 获取主播id
     *
     * @param position
     * @return
     */
    private int getAnchorId(int position) {
        if (labelTalents.size() > position) {
            return labelTalents.get(position).getAnchorId();
        } else {
            return -1;
        }
    }

    /**
     * 开播判断逻辑
     */
    private void showIsRealNameDialog() {
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
        CustomerDialog dialog = new CustomerDialog.Builder(getActivity()).setTitle("提示").setMsg(msg)
                .setNegativeButton("取消", () -> {

                })
                .setPositiveButton("确定", () -> {
                    if (UserManager.getInstance().getUser().myIsCardStatus == UserInfo.IDCARD_STATUS_NULL)
                        RealNameActivity.start(getActivity(), UserManager.getInstance().getUser().myIsCardStatus);
                }).create();
        dialog.show();
        if (UserManager.getInstance().getUser().myIsCardStatus != UserInfo.IDCARD_STATUS_NULL) {
            dialog.hideCancle();
        }
    }

    /**
     * 个人资料不完善 弹窗
     */
    private void showIsFlirtDialog() {
        CertificationDialog certificationDialog = new CertificationDialog(getActivity(), "个人资料不完善，请先完善信息", "完善资料",
                new CertificationDialog.onPositiveListener() {
                    @Override
                    public void onPositive() {
                        Intent intent = new Intent(getActivity(), FlirtCertificationActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
        certificationDialog.show();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        ViewGroup.LayoutParams params = carrLayout.getLayoutParams();
        params.width = width;
        params.height = width;
        carrLayout.setLayoutParams(params);

        carrLayout.setOnCarrouselItemClickListener(new OnCarrouselItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ZipLabelDto zipLabelChild = zipLabelChildList.get(position);
                if (zipLabelChild.isAnchor()) {
                    //Context context, int anchorId, String city, String maxAge, String minAge, String sex
                    FlirtCardDetailsActivity.start(getContext(), zipLabelChild.getAnchorId(), city, maxAge, minAge, sex);//跳转下单页
//                    MineDetailActivity.startActivityWithUserId(getContext(), zipLabelChild.getAnchorId()); //跳转主播详情
                } else {
                    String tagName = zipLabelChild.getTagName();
                    if (TextUtils.equals(tagName, select1) || TextUtils.equals(tagName, select2) || TextUtils.equals(tagName, select3)) {
                        if(isAdded()){
                            ToastUtils.showShort(getContext(), R.string.string_selected_tag);
                        }
                        return;
                    }

                    if (TextUtils.isEmpty(select1)) {
                        tvSelect1.setBackgroundResource(R.mipmap.pic_selects_1);
                        tvSelect1.setText(zipLabelChild.getTagName());
                        select1 = zipLabelChild.getTagName();
                    } else if (TextUtils.isEmpty(select2)) {
                        tvSelect2.setBackgroundResource(R.mipmap.pic_selects_2);
                        tvSelect2.setText(zipLabelChild.getTagName());
                        select2 = zipLabelChild.getTagName();
                    } else if (TextUtils.isEmpty(select3)) {
                        tvSelect3.setBackgroundResource(R.mipmap.pic_selects_3);
                        tvSelect3.setText(zipLabelChild.getTagName());
                        select3 = zipLabelChild.getTagName();
                        mHandler.postDelayed(runPage1, 1000);
                        tvBtn.setEnabled(false);
                        imGo.setEnabled(false);
                    }
                }
            }
        });
    }

    /**
     * 获取选中的标签名
     *
     * @return
     */
    private String getTagName() {
        StringBuilder tagSb = new StringBuilder();
        if (!TextUtils.isEmpty(select1)) {
            tagSb.append(",");
            tagSb.append(select1);
        }
        if (!TextUtils.isEmpty(select2)) {
            tagSb.append(",");
            tagSb.append(select2);
        }
        if (!TextUtils.isEmpty(select3)) {
            tagSb.append(",");
            tagSb.append(select3);
        }
        if (tagSb.length() > 0) {
            tagSb.deleteCharAt(0);
        }
        return tagSb.toString();
    }

    public void setIOnclickListener(SelectListenet listener) {
        this.listener = listener;
    }

    @Override
    public void onReceiveLoadMore(List<LabelTalentDto> data) {
        this.labelTalents = data;
        int size = Math.min(7, data.size());
        for (int i = 0; i < size; i++) {
            ImageLoader.loadImg(multiImageViews.get(i), data.get(i).getHeadImg());
            ImageView iv_gif = animViews.get(i).findViewById(R.id.iv_gif);
            if (data.get(i).getBstatus() == 1) {//开播
                iv_gif.setVisibility(View.VISIBLE);
                ImageLoader.loadGif(iv_gif, R.drawable.live_cell_gif);
            } else {
                iv_gif.setVisibility(View.GONE);
            }
            LinearLayout ll_content = animViews.get(i).findViewById(R.id.ll_content);
            ImageView im_sex = animViews.get(i).findViewById(R.id.im_sex);
            TextView tv_age = animViews.get(i).findViewById(R.id.tv_age);

            if (data.get(i).getSex() == 1) {//'性别；0：保密，1：男；2：女',
                ll_content.setBackgroundResource(R.drawable.bg_age_man);
                im_sex.setImageResource(R.mipmap.pic_man);
            } else {
                ll_content.setBackgroundResource(R.drawable.bg_age_nv);
                im_sex.setImageResource(R.mipmap.pic_nv);
            }

            tv_age.setText(String.valueOf(data.get(i).getAge() == 0 ? 20 : data.get(i).getAge()));
        }
        showHoneycombAnim(false);
    }

    int[] headLabels = new int[]{R.drawable.ic_head_label_1, R.drawable.ic_head_label_2, R.drawable.ic_head_label_3};
    int[] starLabels = new int[]{R.drawable.ic_label_1, R.drawable.ic_label_2, R.drawable.ic_label_3,
            R.drawable.ic_label_4, R.drawable.ic_label_5, R.drawable.ic_label_6, R.drawable.ic_label_7,
            R.drawable.ic_label_8, R.drawable.ic_label_9};

    @Override
    public void getZipLabel(ZipLabelParentDto data) {
        this.data = data;
        zipLabelChildList.clear();
        close();
        //主播数量
        int anchorSize = 0;
        //推荐主播数量
        if (data.getLabelTalents() != null) {
            anchorSize = Math.min(data.getLabelTalents().size(), 3);
            for (int i = 0; i < anchorSize; i++) {
                LabelTalentDto labelTalent = data.getLabelTalents().get(i);
                zipLabelChildList.add(new ZipLabelDto(labelTalent.getAge(), labelTalent.getAnchorId(), labelTalent.getHeadImg(), labelTalent.getNickname(), labelTalent.getSex(), labelTalent.getBstatus()));
            }
        }

        int tagSize = data.getCerTags().size();
        if (tagSize > 0) {
            //最多展示12个元素,（前三一定为头像）
            if (data.getCerTags().size() >= 3 - anchorSize) {
                //先用头像不足3个元素
                for (int i = 0; i < 3 - anchorSize; i++) {
                    CerTagBean cerTag = data.getCerTags().get(i);
                    zipLabelChildList.add(new ZipLabelDto(headLabels[i], cerTag.getTagName(), ZipLabelDto.StarGrade.normalHead));
                }
                //如果CerTags个数+主播数量大于等于12，则补满12个元素
                if (data.getCerTags().size() >= 12 - anchorSize) {
                    for (int i = 3 - anchorSize; i < 12 - anchorSize; i++) {
                        CerTagBean cerTag = data.getCerTags().get(i);
                        int position = i - (3 - anchorSize);
                        int grade = position == 0 ? ZipLabelDto.StarGrade.grade1 :
                                position <= 3 ? ZipLabelDto.StarGrade.grade2 : ZipLabelDto.StarGrade.grade3;
                        zipLabelChildList.add(new ZipLabelDto(starLabels[i - (3 - anchorSize)], cerTag.getTagName(), grade));
                    }
                } else { //如果CerTags个数+主播数量小于12，则循环补足12个
                    for (int i = 3; i < 12; i++) {
                        CerTagBean cerTag = data.getCerTags().get(i % tagSize);
                        int position = (i - (3 - anchorSize)) % starLabels.length;
                        int grade = position == 0 ? ZipLabelDto.StarGrade.grade1 :
                                position <= 3 ? ZipLabelDto.StarGrade.grade2 : ZipLabelDto.StarGrade.grade3;
                        zipLabelChildList.add(new ZipLabelDto(starLabels[position % starLabels.length], cerTag.getTagName(), grade));
                    }
                }
            } else {
                //先用默认头像补足3
                for (int i = 0; i < 3 - anchorSize; i++) {
                    CerTagBean cerTag = data.getCerTags().get(i);
                    zipLabelChildList.add(new ZipLabelDto(headLabels[i], cerTag.getTagName(), ZipLabelDto.StarGrade.normalHead));
                }
                //用星球补足12
                for (int i = 3; i < 12; i++) {
                    CerTagBean cerTag = data.getCerTags().get(i % tagSize);
                    int position = (i - (3 - anchorSize)) % starLabels.length;
                    int grade = position == 0 ? ZipLabelDto.StarGrade.grade1 :
                            position <= 3 ? ZipLabelDto.StarGrade.grade2 : ZipLabelDto.StarGrade.grade3;
                    zipLabelChildList.add(new ZipLabelDto(starLabels[position % starLabels.length], cerTag.getTagName(), grade));
                }
            }
        }

        //进行乱序
        Collections.shuffle(zipLabelChildList);

        for (int i = 0; i < zipLabelChildList.size(); i++) {
            carrLayout.addView(newAddView(zipLabelChildList.get(i)));
        }
//        KLog.e("tag", "" + width / 2);
        carrLayout.isture = true;
        carrLayout.setR((width / 2.2F)).setAutoRotation(true);
        carrLayout.setAutoRotationTime(1);
        carrLayout.resume();
        carrLayout.refreshLayout();
    }

    public void close() {
        if (carrLayout != null) {
            carrLayout.close();
            carrLayout.removeAllViews();
        }
        closed = true;
        KLog.d("tag", "close");
    }

    @Override
    public void onPause() {
        super.onPause();
        KLog.e("tag", "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        KLog.v("tagss", "onResume");
        tvBtn.setEnabled(true);
        imGo.setEnabled(true);
    }

    public void resume() {
        KLog.e("tag", "resume");
        clearParams();
        getData();
        tvBtn.setEnabled(true);
        imGo.setEnabled(true);
        if (UserManager.getInstance().getUser().wenboAnchorStatus == 1) {
            tvBecome.setText("我是达人");
            tvBecome.setSelected(true);
        } else {
            tvBecome.setText("成为达人");
            tvBecome.setSelected(false);
        }

    }
    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(false, 0.2f)
                .navigationBarDarkIcon(true, 0.2f)
                .navigationBarColor(com.fengwo.module_comment.R.color.white)
                .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                .init();
    }
    private void getData() {
        p.getData();
    }

    /**
     * 清除选中的tag
     */
    private void clearParams() {
        select1 = "";
        tvSelect1.setBackgroundResource(R.mipmap.pic_select);
        tvSelect1.setText("");
        select2 = "";
        tvSelect2.setBackgroundResource(R.mipmap.pic_select);
        tvSelect2.setText("");
        select3 = "";
        tvSelect3.setBackgroundResource(R.mipmap.pic_select);
        tvSelect3.setText("");
        sex = "3";
        city = "";
        maxAge = "100";
        minAge = "0";
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        KLog.e("tagss", isVisibleToUser + "");
        tvBtn.setEnabled(true);
        imGo.setEnabled(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("tagss", hidden + "");
        if (hidden) {// 界面可见
            //TODO now visible to user

        } else {// 界面不可见 相当于onpause
            //TODO now invisible to user
        }
    }

    private View newAddView(ZipLabelDto zipLabelDto) {
        if (!zipLabelDto.isAnchor()) {
            View label = LayoutInflater.from(getActivity()).inflate(R.layout.view_label, null, false);
            TextView textView = label.findViewById(R.id.tv_title);
            textView.setTag(zipLabelDto);
            //textView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font/iliaostyle.ttf"));
            textView.setTypeface(BaseApplication.mApp.textFace);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            int size;
            //不同等级星球设置不同大小，字体大小
            if (zipLabelDto.getStarGrade() == ZipLabelDto.StarGrade.normalHead) {
                textView.setTextSize(12);
                size = dip2px(getActivity(), 80);
            } else if (zipLabelDto.getStarGrade() == ZipLabelDto.StarGrade.grade1) {
                textView.setTextSize(22);
                size = dip2px(getActivity(), 190);
            } else if (zipLabelDto.getStarGrade() == ZipLabelDto.StarGrade.grade2) {
                textView.setTextSize(17);
                size = dip2px(getActivity(), 160);
            } else {
                textView.setTextSize(12);
                size = dip2px(getActivity(), 120);
            }
            textView.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
            textView.setBackgroundResource(zipLabelDto.getStarBgRes());
            textView.setText(zipLabelDto.getTagName());
            // Log.e("tag", zipLabelDto.getHeadImg() + "" + zipLabelDto.getAge() + "" + zipLabelDto.getNickname() + "" + zipLabelDto.getTagName() + "" + zipLabelDto.getBstatus());
            return label;
        } else {
            View view = LayoutInflater.from(this.getContext()).inflate(R.layout.qp_users_type, null, false);
            view.setTag(zipLabelDto);
            ImageView iv_gif = view.findViewById(R.id.iv_gif);
            ImageLoader.loadGif(iv_gif, R.drawable.live_cell_gif);
            LinearLayout ll_back = view.findViewById(R.id.ll_back);
            ImageView im_sex = view.findViewById(R.id.im_sex);
            ImageView im_pic = view.findViewById(R.id.im_pic);
            TextView tv_age = view.findViewById(R.id.tv_age);
            TextView tv_title = view.findViewById(R.id.tv_title);
            tv_title.setText(zipLabelDto.getNickname());
            ImageLoader.loadImg(im_pic, zipLabelDto.getHeadImg());

            if (zipLabelDto.getSex() == 1) {//'性别；0：保密，1：男；2：女',
                ll_back.setBackgroundResource(R.drawable.bg_age_man);
                im_sex.setImageResource(R.mipmap.pic_man);
            } else {
                ll_back.setBackgroundResource(R.drawable.bg_age_nv);
                im_sex.setImageResource(R.mipmap.pic_nv);
            }
            tv_age.setText(String.valueOf(zipLabelDto.getAge() == 0 ? 20 : zipLabelDto.getAge()));
            return view;
        }
    }

    private void actionOtherVisible(boolean isShow, View triggerView, View animView) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (isShow) {
                animView.setVisibility(View.VISIBLE);
                //    if (mListener != null) mListener .onShowAnimationEnd();
            } else {
                animView.setVisibility(View.GONE);
                //   if (mListener != null) mListener .onHideAnimationEnd();
            }
            return;
        }
        int[] tvLocation = new int[2];
        triggerView.getLocationInWindow(tvLocation);
        int tvX = tvLocation[0] + triggerView.getWidth() / 2;
        int tvY = tvLocation[1] + triggerView.getHeight() / 2;

        int[] avLocation = new int[2];
        animView.getLocationInWindow(avLocation);
        int avX = avLocation[0] + animView.getWidth() / 2;
        int avY = avLocation[1] + animView.getHeight() / 2;
        int rippleW;
        if (tvX < avX) {
            rippleW = animView.getWidth() - tvX;
        } else {
            rippleW = tvX - avLocation[0];
        }
        int rippleH;
        if (tvY < avY) {
            rippleH = animView.getHeight() - tvY;
        } else {
            rippleH = tvY - avLocation[1];
        }
        float maxRadius = (float) Math.sqrt((rippleW * rippleW + rippleH * rippleH));
        float startRadius;
        float endRadius;
        if (isShow) {
            startRadius = 0f;
            endRadius = maxRadius;
        } else {
            startRadius = maxRadius;
            endRadius = 0f;
        }
        Animator anim = ViewAnimationUtils.createCircularReveal(animView, tvX, tvY, startRadius, endRadius);
        animView.setVisibility(View.VISIBLE);
        anim.setDuration(1000);
        anim.setInterpolator(new DecelerateInterpolator());
        //监听动画结束，进行回调
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isShow) {
                    animView.setVisibility(View.VISIBLE);
                    //      if (mListener != null) mListener .onShowAnimationEnd();
                } else {
                    animView.setVisibility(View.GONE);
                    //       if (mListener != null) mListener .onHideAnimationEnd();
                }
            }
        });
        anim.start();

    }
}
