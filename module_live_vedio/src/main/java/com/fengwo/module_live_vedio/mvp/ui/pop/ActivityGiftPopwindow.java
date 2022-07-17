package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.ZqDto;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

public class ActivityGiftPopwindow extends BasePopupWindow {

    @BindView(R2.id.rv)
    RecyclerView rv;
    @BindView(R2.id.btn_buy)
    TextView btnBuy;
    @BindView(R2.id.btn_no_show)
    TextView btnNoShow;
    @BindView(R2.id.im_pic)
    ImageView im_pic;

    @BindView(R2.id.im_gift)
    ImageView im_gift;
    @BindView(R2.id.im_giftmeal)
    ImageView im_giftmeal;

    @BindView(R2.id.rl_viewint)
    RelativeLayout rl_viewint;

    private boolean isType = false;


    private ActivityGiftAdapter activityGiftAdapter;//礼物
    private CompositeDisposable disposable;
    private Context mContext;


    public ActivityGiftPopwindow(Context context) {
        super(context);
        mContext = context;
        disposable = new CompositeDisposable();
        this.setOutSideTouchable(false);
        ButterKnife.bind(this, getContentView());
        setPopupGravity(Gravity.CENTER);
        btnNoShow.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        rv.setLayoutManager(new GridLayoutManager(mContext, 3));
        activityGiftAdapter = new ActivityGiftAdapter();
        rv.setAdapter(activityGiftAdapter);
        rl_viewint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        im_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isType){
                    activityGiftAdapter.getposition(0);
                    isType = false;
                    im_gift.setImageResource(R.drawable.pic_activity_l);
                    im_giftmeal.setImageResource(R.drawable.pic_activity_rs);
                    disposable.add(new RetrofitUtils().createApi(LiveApiService.class).getLiveFrstvalInfo()
                            .compose(RxUtils.applySchedulers())
                            .subscribeWith(new LoadingObserver<HttpResult<ZqDto>>() {
                                @Override
                                public void _onNext(HttpResult<ZqDto> data) {
                                    if (null == data.data) {
                                        dismiss();
                                        return;
                                    }
                                    ImageLoader.loadImg(im_pic, data.data.getActivityCenterImg());
                                    activityGiftAdapter.setNewData(data.data.activityGifts);
                                }

                                @Override
                                public void _onError(String msg) {

                                }
                            }));
                }
            }
        });
        im_giftmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isType){
                    activityGiftAdapter.getposition(0);
                    isType = true;
                    im_gift.setImageResource(R.drawable.pic_activity_ls);
                    im_giftmeal.setImageResource(R.drawable.pic_activity_r);
                    disposable.add(new RetrofitUtils().createApi(LiveApiService.class).getLiveDiscountInfo()
                            .compose(RxUtils.applySchedulers())
                            .subscribeWith(new LoadingObserver<HttpResult<ZqDto>>() {
                                @Override
                                public void _onNext(HttpResult<ZqDto> data) {
                                    if (null == data.data) {
                                        dismiss();
                                        return;
                                    }
                                    ImageLoader.loadImg(im_pic, data.data.getActivityCenterImg());
                                    activityGiftAdapter.setNewData(data.data.activityGifts);
                                }

                                @Override
                                public void _onError(String msg) {

                                }
                            }));
                }
            }
        });
        disposable.add(new RetrofitUtils().createApi(LiveApiService.class).getLiveFrstvalInfo()
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<ZqDto>>() {
                    @Override
                    public void _onNext(HttpResult<ZqDto> data) {
                        if (null == data.data) {
                            dismiss();
                            return;
                        }
                        ImageLoader.loadImg(im_pic, data.data.getActivityCenterImg());
                        activityGiftAdapter.setNewData(data.data.activityGifts);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void destory() {
        if (null != activityGiftAdapter) {
            activityGiftAdapter = null;
        }
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        mContext = null;
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_gift_activity);
        return v;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultAlphaAnimation(false);
    }

    @OnClick({R2.id.btn_buy, R2.id.btn_no_show, R2.id.iv_close, R2.id.im_zs})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_buy || id == R.id.im_zs) {
            if (onActivityGiftSendListener != null) {
                if(activityGiftAdapter.getCheckGift().giftDiscountPriceTotal==0){
                    onActivityGiftSendListener.onSend(activityGiftAdapter.getCheckGift());
                }else {
                    onActivityGiftSendListener.onActivitySend(activityGiftAdapter.getCheckGift(),activityGiftAdapter.getCheckGift().giftQuantity);
                }

            }
            dismiss();
        } else if (id == R.id.btn_no_show) {
            SPUtils1.put(mContext, "noshowtoday", System.currentTimeMillis());
            dismiss();
        } else if (id == R.id.iv_close) {
            dismiss();
        }

    }

    OnActivityGiftSendListener onActivityGiftSendListener;

    public OnActivityGiftSendListener getOnActivityGiftSendListener() {
        return onActivityGiftSendListener;
    }

    public void setOnActivityGiftSendListener(OnActivityGiftSendListener onActivityGiftSendListener) {
        this.onActivityGiftSendListener = onActivityGiftSendListener;
    }

    public interface OnActivityGiftSendListener {
        void onSend(GiftDto dto);
        void onActivitySend(GiftDto dto,int num);
    }

    public static class ActivityGiftAdapter extends BaseQuickAdapter<GiftDto, BaseViewHolder> {

        private int checkPosition = 0;

        public ActivityGiftAdapter() {
            super(R.layout.item_activity_gift);
        }

        public GiftDto getCheckGift() {
            return getItem(checkPosition);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder holder, GiftDto item) {

            ((TextView) holder.getView(R.id.tv_ori_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.setText(R.id.tv_gift_name, item.giftName);
            TextView tv_gift_names = holder.getView(R.id.tv_gift_names);
            TextView tvOldoney = holder.getView(R.id.tv_old_money);
            if(item.giftDiscountPriceTotal == 0) {
                holder.setText(R.id.tv_now_price, (int) item.giftPrice + "");
                tv_gift_names.setVisibility(View.GONE);
                tvOldoney.setVisibility(View.INVISIBLE);

            } else {
                holder.setText(R.id.tv_now_price, (int) item.giftDiscountPriceTotal + "");
                tvOldoney.setVisibility(View.VISIBLE);
                tvOldoney.setText("原价"+(int) item.giftPrice * item.giftQuantity + "");
                tvOldoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_gift_names.setVisibility(View.VISIBLE);
                tv_gift_names.setText("X"+item.giftQuantity);
            }
          //  holder.setText(R.id.tv_ori_price, (int) item.giftOriginalPrice + "");
//            TextView tvOldoney = holder.getView(R.id.tv_old_money);
//            if(item.giftDiscountPriceTotal==0){
//                tvOldoney.setVisibility(View.GONE);
//            }else {
//                tvOldoney.setVisibility(View.VISIBLE);
//                tvOldoney.setText((int) item.giftPrice * item.giftQuantity + "");
//                tvOldoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            }


            ImageLoader.loadImg(holder.getView(R.id.iv_gift), item.giftIcon);
//            holder.setVisible(R.id.iv_add_queen,holder.getLayoutPosition()==0);

            if (checkPosition == holder.getLayoutPosition()) {
                ImageView iv = holder.getView(R.id.iv_gift);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setRepeatMode(Animation.REVERSE);
                scaleAnimation.setRepeatCount(Animation.INFINITE);
                scaleAnimation.setDuration(400);
                iv.startAnimation(scaleAnimation);
            }

        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            holder.setBackgroundRes(R.id.root, checkPosition == position ? R.drawable.bg_activityg_gift_select : R.color.transparent);
            holder.getView(R.id.root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPosition = position;
                    notifyDataSetChanged();
                }
            });
        }

        public void getposition(int i) {
            checkPosition = i;
        }
    }
}
