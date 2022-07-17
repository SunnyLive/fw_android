package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.AnchorWishBean;
import com.fengwo.module_comment.bean.WishRepayBean;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.pop.WishTypewindow;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.LoadingDialog;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.utils.WishCacheMr;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.KeyboardUtils;

/**
 * 添加心愿
 *
 * @Author BLCS
 * @Time 2020/6/8 17:01
 */
public class AddAnchorWishPop extends BasePopupWindow implements View.OnClickListener {

    private SelectGiftPopWindow selectGiftPopWindow;
    private String giftName;
    private final EditText etInputNum;
    private final TextView tvSelect;
    private final TextView tvWishSelect;

    private int giftId;
    private String giftImg;
    private final TextView ivCreatWish;
    int rootViewVisibleHeight = 0;
    private final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private final View decorView;
    private int wishType;
    private int repay;
    private final WishCacheMr wishCacheMr;
    private LoadingDialog loadingDialog;
    private boolean cleanCache = true;//判断是否清除暂存数据
    private boolean isOpen;

    public AddAnchorWishPop(Context context) {
        super(context);
        ImageView ivBack = findViewById(R.id.iv_wish_back);
        ImageView ivWishGo = findViewById(R.id.iv_wish_go);
        ivCreatWish = findViewById(R.id.iv_creat_wish);
        ivCreatWish.setSelected(false);
        ConstraintLayout cl_add_wish = findViewById(R.id.cl_add_wish);
        tvSelect = findViewById(R.id.tv_wish_select);
        etInputNum = findViewById(R.id.et_wish_input);
        tvWishSelect = findViewById(R.id.tv_wish_type_select);
        ivBack.setOnClickListener(this);
        ivWishGo.setOnClickListener(this);
        ivCreatWish.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
        tvWishSelect.setOnClickListener(this);
        etInputNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("0")){
                    ToastUtils.showShort(getContext(),"请输入1-9999");
                    etInputNum.setText("");
                }
                ivCreatWish.setSelected(s.length() > 0 && !tvSelect.getText().toString().startsWith("请选择礼物") && !tvWishSelect.getText().toString().startsWith("请选择报答方式"));
            }
        });
        tvWishSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ivCreatWish.setSelected(!TextUtils.isEmpty(etInputNum.getText().toString()) && !tvSelect.getText().toString().startsWith("请选择礼物") && !tvWishSelect.getText().toString().startsWith("请选择报答方式"));
            }
        });
        tvSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ivCreatWish.setSelected(!TextUtils.isEmpty(etInputNum.getText().toString()) && !tvSelect.getText().toString().startsWith("请选择礼物") && !tvWishSelect.getText().toString().startsWith("请选择报答方式"));
            }
        });

        decorView = getContext().getWindow().getDecorView();
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.height();
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) cl_add_wish.getLayoutParams();
                    params.bottomMargin = rootViewVisibleHeight - visibleHeight;
                    cl_add_wish.setLayoutParams(params);
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                if (visibleHeight - rootViewVisibleHeight > 200) {
                    ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) cl_add_wish.getLayoutParams();
                    params.bottomMargin = 0;
                    cl_add_wish.setLayoutParams(params);
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        wishCacheMr = WishCacheMr.getInstance();
        setAdjustInputMethod(true);
        cleanCache =true;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_add_anchor_wish);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    AnchorWishPop anchorWishPop;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_wish_back) {
            if (anchorWishPop == null) {
                anchorWishPop = new AnchorWishPop(getContext(),true);
            }
            anchorWishPop.setType(1);
            anchorWishPop.showPopupWindow();
            cleanCache = false;
            dismiss();
        } else if (v.getId() == R.id.iv_creat_wish) {//确定
            cleanCache = false;
            if (!ivCreatWish.isSelected()) return;
            if (anchorWishPop == null) {
             //   ToastUtils.showShort(getContext(),"请完善信息");
                anchorWishPop = new AnchorWishPop(getContext(),true);
            }
            KeyBoardUtils.closeKeybord(etInputNum, getContext());
            String gifNum = etInputNum.getText().toString();
            String tvWishWay = tvWishSelect.getText().toString();
            dismiss();
            cacheWishData(gifNum, tvWishWay);
            anchorWishPop.setType(1);
            anchorWishPop.showPopupWindow();
            tvSelect.setText("请选择礼物");
            tvWishSelect.setText("请选择报答方式");
            etInputNum.setText("");
        } else if (v.getId() == R.id.tv_wish_select || v.getId() == R.id.iv_wish_go) {//选择礼物
            cleanCache = false;
            if (selectGiftPopWindow == null) {
                getGift();
            } else {
                dismiss();
                selectGiftPopWindow.setWishType(wishType);
                selectGiftPopWindow.showPopupWindow();
            }
        } else if (v.getId() == R.id.tv_wish_type_select) {
            cleanCache = true;
            //获取报答方式
            KeyboardUtils.close(etInputNum);
            getWishRepay();
        }
    }

    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext());
            loadingDialog.setProgressPercent("");
        }
        loadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.setProgressPercent("");
            loadingDialog.hide();
        }
    }

    private WishTypewindow wishTypewindow;

    /**
     * 获取报答方式
     */
    private void getWishRepay() {
        isOpen = true;
        if (wishTypewindow != null) {
            if (wishTypewindow.isShowing()) {
                return;
            } else {
                wishTypewindow.showPopupWindow();
            }
        } else {
            Flowable<HttpResult<WishRepayBean>> wishRepay = new RetrofitUtils().createApi(LiveApiService.class).getWishRepay();
            wishRepay.compose(RxUtils.applySchedulers2()).subscribeWith(new LoadingObserver<HttpResult<WishRepayBean>>() {
                @Override
                public void _onNext(HttpResult<WishRepayBean> data) {
                    if (!isOpen) return;
                    if (data.isSuccess()) {
                        ArrayList<String> strings = new ArrayList<>();
                        strings.add(data.data.get_$1());
                        strings.add(data.data.get_$2());
                        strings.add(data.data.get_$3());
                        if(wishTypewindow==null){
                            wishTypewindow = new WishTypewindow(getContext(), strings);
                            wishTypewindow.addOnClickListener((titles, pos) -> {
                                tvWishSelect.setText(titles);
                                repay = pos;
                            });
                        }
                        if (!wishTypewindow.isShowing()) {
                            wishTypewindow.showPopupWindow();
                        }
                    }
                }

                @Override
                public void _onError(String msg) {
                    ToastUtils.showShort(getContext(), msg);
                }
            });
        }
    }

    /**
     * 缓存数据
     *
     * @param gifNum
     * @param tvWishWay
     */
    private void cacheWishData(String gifNum, String tvWishWay) {
        List<AnchorWishBean> wish = wishCacheMr.getWish();
        AnchorWishBean anchorWishBean = new AnchorWishBean();
        anchorWishBean.giftId = giftId;
        anchorWishBean.giftName = giftName;
        anchorWishBean.icon = giftImg;
        anchorWishBean.repayName = tvWishWay;
        anchorWishBean.repay = repay + 1;
        L.e("=======wishType"+wishType);
        anchorWishBean.wishType = wishType + 1;
        anchorWishBean.wishQuantity = Integer.parseInt(gifNum);
        wish.add(anchorWishBean);
        wishCacheMr.cacheWish(wish);
    }

    @Override
    public void dismiss(boolean animateDismiss) {
        super.dismiss(animateDismiss);
        KeyBoardUtils.closeKeybord(etInputNum, getContext());
    }

    /**
     * 获取礼物列表 接口
     */
    public void getGift() {
        isOpen =true;
        new RetrofitUtils().createApi(LiveApiService.class).getGifts()
                .compose(RxUtils.applySchedulers2())
                .subscribeWith(new LoadingObserver<HttpResult<List<GiftDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<GiftDto>> data) {
                        if (!isOpen) return;
                        int type = -1;
                        List<List<GiftDto>> allGifts = new ArrayList<>();
                        List<GiftDto> list = null;
                        for (int i = 0; i < data.data.size(); i++) {
                            GiftDto gift = data.data.get(i);
                            if (type != gift.giftType) {
                                if (null != list) {
                                    allGifts.add(list);
                                }
                                list = new ArrayList<>();
                                type = gift.giftType;
                            }
                            list.add(gift);
                        }
                        if (list != null) {
                            allGifts.add(list);
                        }
                        if (null == selectGiftPopWindow) {//设置礼物弹窗数据
                            dismiss();
                            selectGiftPopWindow = new SelectGiftPopWindow(getContext(), allGifts);
                            selectGiftPopWindow.setWishType(wishType);
                            selectGiftPopWindow.showPopupWindow();
                        }else{
                            selectGiftPopWindow.setWishType(wishType);
                            selectGiftPopWindow.showPopupWindow();
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        L.e(" ====== "+msg);
                        hideLoadingDialog();
                        ToastUtils.showShort(getContext(), msg);
                    }
                });
    }

    public OnWishClickListener onWishClickListener;

    public void setGifName(String giftName, int giftId, String giftImg) {
        this.giftId = giftId;
        this.giftImg = giftImg;
        tvSelect.setText(TextUtils.isEmpty(giftName) ? "请选择礼物" : giftName);
        this.giftName = giftName;
        if(tvWishSelect==null&&etInputNum==null) return;
        tvWishSelect.setText("请选择报答方式");
        etInputNum.setText("");
    }

    /**
     * 设置心愿类型
     *
     * @param wishType 0=今日 1=本周 2=本月
     */
    public void setWishType(int wishType) {
        L.e("=======wishType"+wishType);
        this.wishType = wishType;
    }

    public interface OnWishClickListener {
        void close();
    }

    public void addClickListener(OnWishClickListener listener) {
        onWishClickListener = listener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        L.e("======"+cleanCache);
        if (cleanCache){
            wishCacheMr.getWish().clear();
        }
        isOpen = false;
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();
        Rect r = new Rect();
        decorView.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.height();
        rootViewVisibleHeight = visibleHeight;
    }

}
