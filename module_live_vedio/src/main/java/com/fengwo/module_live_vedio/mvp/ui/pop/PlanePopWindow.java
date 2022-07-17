package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.RxHttpUtil;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.PlaneAllDto;
import com.fengwo.module_live_vedio.mvp.dto.PlaneGiftDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.GiftAdapter;
import com.google.gson.Gson;
import com.umeng.commonsdk.debug.E;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/8/10
 */
public class PlanePopWindow extends BasePopupWindow {

    @BindView(R2.id.tv_pop_title)
    TextView tvPopTitle;
    @BindView(R2.id.tv_title_rec)
    TextView tvTitleRec;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.rv_content)
    RelativeLayout rvContent;
    @BindView(R2.id.recycleview_plane)
    RecyclerView recycleviewPlane;
    @BindView(R2.id.iv_complete)
    ImageView ivComplete;
    @BindView(R2.id.tv_bottom_tips)
    TextView tvBottomTips;

    private CompositeDisposable disposable;
    private PlaneAdapter planeAdapter;
    private GiftAdapter giftAdapter;
    private int channelId;
    private boolean isPush;
    private Context context;
    @Autowired
    UserProviderService userProviderService;

    public PlanePopWindow(Context context,int channelId) {
        super(context);
        ARouter.getInstance().inject(this);
        this.context = context;
        setPopupGravity(Gravity.BOTTOM);
        disposable =new CompositeDisposable();
        this.channelId = channelId;
        isPush = channelId == userProviderService.getUserInfo().id;
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        GridLayoutManager planeLayoutManager = new GridLayoutManager(context,3);
        recycleviewPlane.setLayoutManager(planeLayoutManager);
        planeAdapter = new PlaneAdapter(null,isPush);
        giftAdapter = new GiftAdapter(null,isPush);
        recycleviewPlane.setAdapter(planeAdapter);
        recyclerview.setAdapter(giftAdapter);
        planeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (PlaneAllDto allDto : planeAdapter.getData()){
                    allDto.setChenck(false);
                }
                planeAdapter.getData().get(position).setChenck(true);
                planeAdapter.notifyDataSetChanged();
                getGiftList(planeAdapter.getData().get(position).getId());
            }
        });
        giftAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (onSendGiftListener!=null){
                    onSendGiftListener.onSendGift(giftAdapter.getData().get(position).getGiftId());
                    dismiss();
                }
            }
        });

        if (isPush){
            tvBottomTips.setText("当主播完成对应的舱位收礼任务时，将获得对应舱位的缘分值奖励；");
        }else {
            tvBottomTips.setText("当主播完成舱位收礼任务时，当前主播直播间将于5分钟后启航告白航班，届时将开启航班舱门随机降落礼盒，点击礼盒赢大礼~");
        }

        getPlaneBoxList();
    }

    private void getPlaneBoxList() {
        Flowable<HttpResult<List<PlaneAllDto>>> flowable;
        if (isPush){
            flowable = new RetrofitUtils().createApi(LiveApiService.class).getPlaneBoxListAnchor(channelId);
        }else {
            flowable = new RetrofitUtils().createApi(LiveApiService.class).getPlaneBoxListUser(channelId,userProviderService.getUserInfo().id);
        }
         disposable.add(flowable
                 .compose(RxUtils.applySchedulers())
                 .subscribeWith(new LoadingObserver<HttpResult<List<PlaneAllDto>>>() {
                     @Override
                     public void _onNext(HttpResult<List<PlaneAllDto>> data) {
                         if (data.isSuccess()) {
                             if (!isPush){
                                 for (int i = 0;i<data.data.size();i++){
                                     if (i==0){
                                         data.data.get(i).setChenck(true);
                                     }
                                     data.data.get(i).setPlaneOpenSecond(data.data.get(i).getPlaneOpenSecond()-30);
                                 }
                             }
                             planeAdapter.setNewData(data.data);
                             getGiftList(data.data.get(0).getId());
                         }else {
                             ToastUtils.showShort(context,data.description);
                         }
                     }

                     @Override
                     public void _onError(String msg) {
                         ToastUtils.showShort(context,msg);
                     }
                 }));
    }

    private void getGiftList(int boxId) {
        Map map = new HashMap();
        map.put("boxId",boxId);
        map.put("channelId",channelId);
        disposable.add(new RetrofitUtils().createApi(LiveApiService.class).getBoxGiftInfo(boxId,channelId)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<PlaneGiftDto>>() {
                    @Override
                    public void _onNext(HttpResult<PlaneGiftDto> data) {
                        if (data.isSuccess()) {
                            giftAdapter.setNewData(data.data.getGiftList());

                            if (isPush) {
                                ivComplete.setVisibility(data.data.getConditionQuantity() <= data.data.getFactQuantity() ? View.VISIBLE : View.GONE);
                                StringBuilder sb= new StringBuilder();
                                sb.append(data.data.getBoxName()+"收礼任务（"+data.data.getFactQuantity()+"/"+data.data.getConditionQuantity()+")");
                                int startS = sb.indexOf(String.valueOf(data.data.getFactQuantity()));
                                SpannableString spannableString = new SpannableString(sb);
                                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF92F8"));
                                spannableString.setSpan(colorSpan,startS,startS+(String.valueOf(data.data.getFactQuantity())).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                tvTitleRec.setText(spannableString);
                            }else {
                                tvTitleRec.setText(data.data.getBoxName()+"起航条件收礼任务（"+data.data.getFactQuantity()+"/"+data.data.getConditionQuantity()+")");
                            }
                        }else {
                            ToastUtils.showShort(context,data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context,msg);
                    }
                }));
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_plane);
        ButterKnife.bind(this, v);
        return v;
    }

    public static class PlaneAdapter extends BaseQuickAdapter<PlaneAllDto, BaseViewHolder> {

        private boolean isPush;
        private SparseArray<CountDownTimer> countDownArray;
        private int checkPosition = 0;

        public PlaneAdapter(@Nullable List<PlaneAllDto> data,boolean isPush) {
            super(R.layout.item_pop_plane_plane, data);
            this.isPush = isPush;
            countDownArray = new SparseArray<>();
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, PlaneAllDto item) {
            helper.setBackgroundRes(R.id.fl_bg_check, item.isChenck() ? R.drawable.bg_activitygift_checked : R.color.transparent);
            helper.setText(R.id.tv_plane_name,item.getName());
            helper.setText(R.id.tv_integral,"缘分值：+"+item.getScore());
            if (helper.getLayoutPosition()==0) {
                ImageLoader.loadImg(helper.getView(R.id.iv_plane), R.drawable.ic_plane);
            }else if (helper.getLayoutPosition() ==1){
                ImageLoader.loadImg(helper.getView(R.id.iv_plane), R.drawable.ic_plane_1);
            }else {
                ImageLoader.loadImg(helper.getView(R.id.iv_plane), R.drawable.ic_plane_2);
            }
            helper.setVisible(R.id.tv_integral,isPush);
            if (item.isAnchorIsReach()&&isPush) {
                helper.setTextColor(R.id.tv_plane_name, Color.parseColor("#FF92F7"));
                helper.setTextColor(R.id.tv_integral, Color.parseColor("#FF92F7"));
            }else {
                helper.setTextColor(R.id.tv_plane_name, Color.parseColor("#FFFFFF"));
                helper.setTextColor(R.id.tv_integral, Color.parseColor("#B2B8FF"));
            }
            if (item.getPlaneOpenSecond()>0) {
                helper.setVisible(R.id.tv_countdown,true);
                CountDownTimer timer = countDownArray.get(helper.getLayoutPosition());
                if (timer == null) {
                    timer = new CountDownTimer(item.getPlaneOpenSecond() * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            helper.setText(R.id.tv_countdown, TimeUtils.calY((int) (millisUntilFinished / 1000)));
                        }

                        @Override
                        public void onFinish() {
                            helper.setVisible(R.id.tv_countdown,false);
                        }
                    };
                    countDownArray.put(helper.getLayoutPosition(),timer);
                    timer.start();
                }
            }else {
                helper.setVisible(R.id.tv_countdown,false);
            }
        }
        public void  onDestroy(){
            try {
                if (countDownArray.size()>0){
                    for (int i= 0;i<countDownArray.size();i++){
                        countDownArray.get(i).cancel();
                    }
                    countDownArray.clear();
                }
            }catch (Exception e){

            }
        }
    }
    public static class GiftAdapter extends BaseQuickAdapter<PlaneGiftDto.GiftListBean, BaseViewHolder> {

        private boolean isPush;

        public GiftAdapter(@Nullable List<PlaneGiftDto.GiftListBean> data,boolean isPush) {
            super(R.layout.item_pop_plane_gift, data);
            this.isPush = isPush;
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, PlaneGiftDto.GiftListBean item) {
            if (isPush){
                helper.setText(R.id.tv_gift_name,item.getGiftName());
            }else {
                helper.setText(R.id.tv_gift_name,item.getGiftName()+"*"+item.getGiftQuantity());
            }
            ImageLoader.loadImg(helper.getView(R.id.iv_gift),item.getGiftIcon());
            TextView view = helper.getView(R.id.tv_send_now);
            view.setText(isPush?"x"+item.getGiftQuantity():"立即赠送");
            view.setBackgroundResource(isPush?0:R.drawable.btn_item_plane);

            if (!isPush){
                helper.addOnClickListener(R.id.tv_send_now);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable!=null){
            disposable.clear();
        }
        if (planeAdapter!=null){
            planeAdapter.onDestroy();
        }
    }

    public RequestBody createRequestBody(Map map) {
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }

    public interface OnSendGiftListener{
        void onSendGift(int giftId);
    }
    private OnSendGiftListener onSendGiftListener;

    public void setOnSendGiftListener(OnSendGiftListener onSendGiftListener) {
        this.onSendGiftListener = onSendGiftListener;
    }
}
