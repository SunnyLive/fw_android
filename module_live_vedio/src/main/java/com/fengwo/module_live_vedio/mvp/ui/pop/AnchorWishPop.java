package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.AnchorWishBean;
import com.fengwo.module_comment.bean.AnchorWishCreatBean;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.ui.event.RefrshWishEvent;
import com.fengwo.module_live_vedio.utils.WishCacheMr;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

/**
 * 主播心愿单
 *
 * @Author BLCS
 * @Time 2020/6/8 17:01
 */
public class AnchorWishPop extends BasePopupWindow implements View.OnClickListener {

    private final CompositeDisposable compositeDisposable;
    @Autowired
    UserProviderService userProviderService;
    @BindView(R2.id.iv_wish_close)
    ImageView ivClosePop;
    /*添加心愿*/
    @BindViews({R2.id.ll_add_wish_today, R2.id.ll_add_wish_week, R2.id.ll_add_wish_month})
    List<LinearLayout> llAddWishs;
    /*生成心愿*/
    @BindViews({R2.id.ll_create_wish_today, R2.id.ll_create_wish_week, R2.id.ll_create_wish_month})
    List<ConstraintLayout> llCreateWishs;

    /*按钮 生成心愿*/
    @BindView(R2.id.iv_creat_wish)
    TextView ivCreatWish;

    @BindView(R2.id.cl_bg)
    ConstraintLayout clBg;

    /*生成心愿列表*/
    @BindViews({R2.id.iv_close_wish_today, R2.id.iv_close_wish_week, R2.id.iv_close_wish_month})
    List<ImageView> ivCloses;
    @BindViews({R2.id.tv_des_type_today, R2.id.tv_des_type_week, R2.id.tv_des_type_month})
    List<TextView> tvDesTypes;

    /*展示心愿列表*/
    @BindViews({R2.id.tv_des_wish_today, R2.id.tv_des_wish_week, R2.id.tv_des_wish_month})
    List<TextView> tvDesWishs;
    @BindViews({R2.id.ll_wish_rank_list, R2.id.ll_wish_rank_list_week, R2.id.ll_wish_rank_list_month})
    List<LinearLayout> llWishRankLists;
    @BindViews({R2.id.iv_wish_complete_today, R2.id.iv_wish_complete_week, R2.id.iv_wish_complete_month})
    List<ImageView> ivWishCompletes;

    @BindViews({R2.id.tv_ydc, R2.id.tv_ydc2, R2.id.tv_ydc3})
    List<ImageView> ivWishYdc;


    @BindViews({R2.id.civ_ranklist1_1, R2.id.civ_ranklist1_2, R2.id.civ_ranklist1_3})
    List<CircleImageView> civRanklists1;
    @BindViews({R2.id.civ_ranklist2_1, R2.id.civ_ranklist2_2, R2.id.civ_ranklist2_3})
    List<CircleImageView> civRanklists2;
    @BindViews({R2.id.civ_ranklist3_1, R2.id.civ_ranklist3_2, R2.id.civ_ranklist3_3})
    List<CircleImageView> civRanklists3;

    @BindViews({R2.id.iv_wish_help_today, R2.id.iv_wish_help_week, R2.id.iv_wish_help_month})
    List<ImageView> ivWishgHelpToday;

    /*共同部分*/
    @BindViews({R2.id.iv_wish_present, R2.id.iv_wish_present_week, R2.id.iv_wish_present_month,})
    List<ImageView> ivWishPresent;
    @BindViews({R2.id.tv_wish_present_name, R2.id.tv_wish_present_name_week, R2.id.tv_wish_present_name_month,})
    List<TextView> tvWishName;

    @BindViews({R2.id.tv_wish_value, R2.id.tv_wish_value_week, R2.id.tv_wish_value_month,})
    List<TextView> tvWishValue;
    @BindViews({R2.id.pb_wish_num, R2.id.pb_wish_num_week, R2.id.pb_wish_num_month,})
    List<ProgressBar> pbWishNum;


    @BindView(R2.id.im_top)
    ImageView im_top;
    AddAnchorWishPop addAnchorWishPop;
    private int type;
    private List<AnchorWishBean> wishInfo;
    private final ArrayList<List<CircleImageView>> rankLists;
    private final int dp1;
    private final WishCacheMr wishCacheMr;
    private boolean cleanCache = true;
    private final FragmentActivity activity;
    private boolean isPush = false;//是不是主播端 默认false

    public AnchorWishPop(Context context, boolean ispush) {
        super(context);
        this.isPush = ispush;
        ButterKnife.bind(this, getContentView());
        compositeDisposable = new CompositeDisposable();
        setPopupGravity(Gravity.BOTTOM);
        ArouteUtils.inject(this);
        setAdjustInputMethod(true);
        rankLists = new ArrayList<>();
        rankLists.add(civRanklists1);
        rankLists.add(civRanklists2);
        rankLists.add(civRanklists3);
        dp1 = DensityUtils.px2dp(getContext(), getContext().getResources().getDimension(R.dimen.dp_1));
        wishCacheMr = WishCacheMr.getInstance();
        cleanCache = true;
        activity = (FragmentActivity) getContext();
        if (ispush) {
            im_top.setBackgroundResource(R.drawable.pic_xy_top);
        } else {
            im_top.setBackgroundResource(R.drawable.pic_xy_tops);
        }

    }

    /**
     * @param type 0.主播添加心愿 1 主播生成心愿  2.主播查看心愿 3.用户查看主播心愿
     */
    public void setType(int type) {
        cleanCache = true;
        this.type = type;
        switch (type) {
            case 1://生成心愿
                //   clBg.setBackgroundResource(R.drawable.ic_wish_bg_clent);
                List<AnchorWishBean> wishs = wishCacheMr.getWish();
                for (int i = 0; i < llAddWishs.size(); i++) {
                    llAddWishs.get(i).setVisibility(View.VISIBLE);
                    ivCloses.get(i).setVisibility(View.VISIBLE);
                    tvDesTypes.get(i).setVisibility(View.VISIBLE);
                    tvDesWishs.get(i).setVisibility(View.GONE);
                    llWishRankLists.get(i).setVisibility(View.GONE);

                    ivWishCompletes.get(i).setVisibility(View.GONE);
                    ivWishYdc.get(i).setVisibility(View.GONE);
                    ivWishgHelpToday.get(i).setVisibility(View.GONE);
                    llCreateWishs.get(i).setPadding(0, 0, 0, DensityUtils.dp2px(getContext(), getContext().getResources().getDimension(R.dimen.dp_5)));
                }
                for (AnchorWishBean bean : wishs) {
                    L.e("bean.wishType " + bean.wishType);
                    llAddWishs.get(bean.wishType - 1).setVisibility(View.GONE);
                    llCreateWishs.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                    //显示数据
                    ImageView imageView = ivWishPresent.get(bean.wishType - 1);
                    ImageLoader.loadRouteImg(imageView, bean.icon, 8);
                    tvWishName.get(bean.wishType - 1).setText(bean.giftName);
                    tvDesTypes.get(bean.wishType - 1).setText("报答方式：" + bean.repayName);
                    tvWishValue.get(bean.wishType - 1).setText(bean.factQuantity + "/" + bean.wishQuantity);
                    ProgressBar progressBar = pbWishNum.get(bean.wishType - 1);
                    progressBar.setMax(bean.wishQuantity);
                    progressBar.setProgress(0);
                }
                //如果有许下的心愿则显示
                anchorWish(wishCacheMr.getExistWish());
                if (wishCacheMr.getWish().size() > 0) {
                    ivCreatWish.setVisibility(View.VISIBLE);
                }
                break;
            case 2: //主播查看心愿
                //   clBg.setBackgroundResource(R.drawable.ic_wish_bg_clent);
                ivCreatWish.setVisibility(View.GONE);

                for (int i = 0; i < llWishRankLists.size(); i++) {
                    llAddWishs.get(i).setVisibility(View.VISIBLE);
                    //   tvPbNums.get(i).setVisibility(View.VISIBLE);
//                    llWishRankLists.get(i).setVisibility(View.VISIBLE);
//                    tvDesTypes.get(i).setVisibility(View.GONE);
//                    tvDesWishs.get(i).setVisibility(View.VISIBLE);
//                    ivWishCompletes.get(i).setVisibility(View.GONE);
//                    ivWishgHelpToday.get(i).setVisibility(View.GONE);
//                    ivCloses.get(i).setVisibility(View.VISIBLE);
//                    llCreateWishs.get(i).setPadding(0,0,0,0);
                }
                anchorWish(wishInfo);
                break;
            case 3://用户查看主播心愿
                ivCreatWish.setVisibility(View.GONE);
                //  clBg.setBackgroundResource(R.drawable.ic_wish_bg_clent);
                for (int i = 0; i < llWishRankLists.size(); i++) {
                    llWishRankLists.get(i).setVisibility(View.VISIBLE);
                    tvDesTypes.get(i).setVisibility(View.GONE);
                    //   tvPbNums.get(i).setVisibility(View.VISIBLE);
                    tvDesWishs.get(i).setVisibility(View.VISIBLE);
                    ivWishCompletes.get(i).setVisibility(View.GONE);
                    ivWishYdc.get(i).setVisibility(View.GONE);
                    ivWishgHelpToday.get(i).setVisibility(View.VISIBLE);
                    ivCloses.get(i).setVisibility(View.GONE);
                    llCreateWishs.get(i).setPadding(0, 0, 0, 0);
                    llAddWishs.get(i).setVisibility(View.GONE);
                }
                if (wishInfo == null || wishInfo.size() == 0) return;
                for (AnchorWishBean bean : wishInfo) {
                    if (bean.id > 0) {
                        llCreateWishs.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                        //礼物icon
                        ImageLoader.loadRouteImg(ivWishPresent.get(bean.wishType - 1), bean.icon, 8);
                        //礼物昵称
                        tvWishName.get(bean.wishType - 1).setText("贡献榜");
                        //进度
                        tvWishValue.get(bean.wishType - 1).setText(bean.factQuantity + "/" + bean.wishQuantity);
                        ProgressBar progressBar = pbWishNum.get(bean.wishType - 1);
                        progressBar.setMax(bean.wishQuantity);
                        progressBar.setProgress(bean.factQuantity);
                        //报答方式
                        tvDesWishs.get(bean.wishType - 1).setText("报答方式：" + bean.repayName);

                        if (bean.factQuantity > 0) {
                            double pb = bean.factQuantity / (double) bean.wishQuantity;
                            String value = DataFormatUtils.formatDouble(pb * 100);

                            double left = getContext().getResources().getDimension(R.dimen.dp_150) * pb;

                        } else {

                        }
                        //显示排行榜头像
                        List<AnchorWishBean.WishDevoteUserVOListBean> userVOListBean = bean.wishDevoteUserVOList;
                        for (int j = 0; j < userVOListBean.size(); j++) {
                            CircleImageView civRank = rankLists.get(bean.wishType - 1).get(j);
                            String header = userVOListBean.get(j).headImg;
                            ImageLoader.loadCircleWithBorder(civRank, header, dp1, ContextCompat.getColor(getContext(), R.color.purple_B699FF));
                        }

                        if (bean.status == 1) {//进度完成 显示完成图标
                            if(!isPush){
                                ivWishCompletes.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                            }

                            ivWishYdc.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            default://添加心愿
                //     clBg.setBackgroundResource(R.drawable.ic_wish_bg_clent);
                for (int i = 0; i < llAddWishs.size(); i++) {
                    llAddWishs.get(i).setVisibility(View.VISIBLE);
                    llCreateWishs.get(i).setVisibility(View.GONE);
                }
                ivCreatWish.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 主播查看心愿
     */
    private void anchorWish(List<AnchorWishBean> wishInfo) {
        if (wishInfo == null) return;
        for (AnchorWishBean bean : wishInfo) {
            if (bean.id > 0) { //UI
                llWishRankLists.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                tvDesTypes.get(bean.wishType - 1).setVisibility(View.GONE);
                tvDesWishs.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                ivWishCompletes.get(bean.wishType - 1).setVisibility(View.GONE);
                ivWishYdc.get(bean.wishType - 1).setVisibility(View.GONE);
                ivWishgHelpToday.get(bean.wishType - 1).setVisibility(View.GONE);
                ivCloses.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                llCreateWishs.get(bean.wishType - 1).setPadding(0, 0, 0, 0);

                llAddWishs.get(bean.wishType - 1).setVisibility(View.GONE);
                llCreateWishs.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                //礼物icon
                ImageLoader.loadRouteImg(ivWishPresent.get(bean.wishType - 1), bean.icon, 8);
                tvWishName.get(bean.wishType - 1).setText("贡献榜");
                //进度
                tvWishValue.get(bean.wishType - 1).setText(bean.factQuantity + "/" + bean.wishQuantity);

                if (bean.factQuantity > 0) {
                    double pb = bean.factQuantity / (double) bean.wishQuantity;
                    String value = DataFormatUtils.formatDouble(pb * 100);

                    double left = getContext().getResources().getDimension(R.dimen.dp_150) * pb;

                } else {

                }
                ProgressBar progressBar = pbWishNum.get(bean.wishType - 1);
                progressBar.setMax(bean.wishQuantity);
                progressBar.setProgress(bean.factQuantity);
                //报答方式
                tvDesWishs.get(bean.wishType - 1).setText("报答方式：" + bean.repayName);
                //显示排行榜头像
                List<AnchorWishBean.WishDevoteUserVOListBean> userVOListBean = bean.wishDevoteUserVOList;
                for (int j = 0; j < userVOListBean.size(); j++) {
                    CircleImageView civRank = rankLists.get(bean.wishType - 1).get(j);
                    String header = userVOListBean.get(j).headImg;
                    ImageLoader.loadCircleWithBorder(civRank, header, dp1, ContextCompat.getColor(getContext(), R.color.purple_B699FF));
                }
                L.e("=====" + bean.status);
                if (bean.status == 1) {//进度完成 显示完成图标
                    if(!isPush) {
                        ivWishCompletes.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                    }
                    ivWishYdc.get(bean.wishType - 1).setVisibility(View.VISIBLE);
                }
                ivCloses.get(bean.wishType - 1).setVisibility(View.GONE);
            }
        }
    }

    @Override

    public View onCreateContentView() {
        return createPopupById(R.layout.pop_anchor_wish);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    /**
     * 生成心愿
     */
    private void commitWish() {
        ArrayList<AnchorWishCreatBean> anchorWishCreatBeans = new ArrayList<>();
        for (AnchorWishBean bean : wishCacheMr.getWish()) {
            anchorWishCreatBeans.add(new AnchorWishCreatBean(userProviderService.getUserInfo().getId(), bean.giftId, bean.repay, bean.wishQuantity, bean.wishType));
        }
        Map map = new HashMap();
        map.put("createWishDTOList", anchorWishCreatBeans);
        String json = new Gson().toJson(map);
        L.e("====== " + json);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        new RetrofitUtils().createApi(LiveApiService.class).commitWish(requestBody).compose(RxUtils.applySchedulers2()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                ToastUtils.showShort(getContext(), data.description);
                RxBus.get().post(new RefrshWishEvent());
                wishCacheMr.cleanCache();
                dismiss();
            }

            @Override
            public void _onError(String msg) {
                ToastUtils.showShort(getContext(), msg);
            }
        });
    }

    @OnClick({R2.id.iv_wish_close, R2.id.iv_creat_wish, R2.id.iv_close_wish_today, R2.id.iv_close_wish_week, R2.id.iv_close_wish_month,
            R2.id.ll_add_wish_today, R2.id.ll_add_wish_week, R2.id.ll_add_wish_month,
            R2.id.iv_wish_help_today, R2.id.iv_wish_help_week, R2.id.iv_wish_help_month})
    public void onClick(View view) {
        if (CommentUtils.isFastClick()) return;
        cleanCache = true;
        int id = view.getId();
        if (id == R.id.iv_wish_close) {
            dismiss();
        } else if (id == R.id.iv_creat_wish) {
            commitWish();//生成心愿
        } else if (id == R.id.iv_close_wish_today) {
            deleteTip(0);
        } else if (id == R.id.iv_close_wish_week) {
            deleteTip(1);
        } else if (id == R.id.iv_close_wish_month) {
            deleteTip(2);
        } else if (id == R.id.ll_add_wish_today) {
            addWish(0);
        } else if (id == R.id.ll_add_wish_week) {
            addWish(1);
        } else if (id == R.id.ll_add_wish_month) {
            addWish(2);
        } else if (id == R.id.iv_wish_help_today) {
            fillWish(1);
        } else if (id == R.id.iv_wish_help_week) {
            fillWish(2);
        } else if (id == R.id.iv_wish_help_month) {
            fillWish(3);
        }
    }

    private void deleteTip(int pos) {
        CommonDialog.getInstance("温馨提示", "是否删除心愿礼物配置", "取消", "确定", false).addOnDialogListener(new CommonDialog.OnDialogListener() {
            @Override
            public void cancel() {

            }

            @Override
            public void sure() {
                deleteItem(pos);
            }
        }).show(activity.getSupportFragmentManager(), "删除心愿");
    }

    /**
     * 助他达成
     *
     * @param wishType
     */
    private void fillWish(int wishType) {
        dismiss();
        if (wishInfo == null) return;
        for (AnchorWishBean beans : wishInfo) {
            L.e("====" + beans.wishType);
            L.e("====" + beans.toString());
            if (beans.wishType == wishType) {
                if (onWishClickListener != null) onWishClickListener.fillWish(beans.giftId);
            }
        }
    }

    /**
     * 添加心愿
     *
     * @param i
     */
    private void addWish(int i) {
        wishCacheMr.cacheExistWish(wishInfo == null ? wishCacheMr.getExistWish() : wishInfo);

        if (addAnchorWishPop == null) {
            addAnchorWishPop = new AddAnchorWishPop(getContext());
        }
        addAnchorWishPop.setWishType(i);
        addAnchorWishPop.showPopupWindow();
        //添加心愿
        cleanCache = false;
        dismiss();
    }

    private void deleteItem(int wishType) {
        cleanCache = false;
        llCreateWishs.get(wishType).setVisibility(View.GONE);
        llAddWishs.get(wishType).setVisibility(View.VISIBLE);
        wishCacheMr.deleteCache(wishType + 1);
        if (wishCacheMr.getWish().size() == 0) {
            ivCreatWish.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (cleanCache) {
            wishCacheMr.getWish().clear();
        }
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }

    public OnWishClickListener onWishClickListener;

    /**
     * 设置心愿
     *
     * @param wishInfo
     */
    public void setWishData(List<AnchorWishBean> wishInfo) {
        this.wishInfo = wishInfo;
        wishCacheMr.cacheExistWish(wishInfo);
    }

    public interface OnWishClickListener {
        void fillWish(int giftId);
    }

    public void addClickListener(OnWishClickListener listener) {
        onWishClickListener = listener;
    }
}
