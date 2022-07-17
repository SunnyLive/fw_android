package com.fengwo.module_flirt.dialog;


import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.FlowLayoutManager;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.GridItemDecoration;
import com.fengwo.module_flirt.adapter.ReceiveCommentGiftAdapter;
import com.fengwo.module_flirt.adapter.ReceiveCommentLabelAdapter;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.CommentDetailsDTO;
import com.fengwo.module_flirt.widget.CustomRatingBar;
import com.fengwo.module_websocket.bean.ReceiveCommentMsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

public class ReceiveCommentPopWindow extends BasePopupWindow implements View.OnClickListener {

    @BindView(R2.id.tv_video_time)
    TextView tvVideoTime;
    @BindView(R2.id.iv_head)
    CircleImageView ivHead;
    @BindView(R2.id.tv_nick)
    TextView tvNick;
    @BindView(R2.id.rb_grade)
    CustomRatingBar ratingBar;
    @BindView(R2.id.iv_t_head)
    CircleImageView ivThead;
    @BindView(R2.id.tv_t_nick)
    TextView tvTnick;
    @BindView(R2.id.im_clone)
    ImageView im_clone;

    @BindView(R2.id.rv_gift)
    RecyclerView rvGift;


    private ReceiveCommentLabelAdapter mLabelAdapter;    // 评价
    private final Context mContext;
    private ReceiveCommentMsg receiveCommentMsg;
    private ReceiveCommentGiftAdapter mGiftAdapter;     // 礼物
    private CompositeDisposable mCompositeDisposable;
    private int mCommentId;

    public ReceiveCommentPopWindow (Context context, ReceiveCommentMsg receiveCommentMsg) {
        super(context);
        mContext = context;
        setPopupGravity(Gravity.BOTTOM);
        this.receiveCommentMsg = receiveCommentMsg;
        setBackPressEnable(false);
        setOutSideDismiss(false);
        initUI();
    }

    public ReceiveCommentPopWindow (Context context, int commentId) {
        super(context);
        mContext = context;
        setPopupGravity(Gravity.BOTTOM);
        setBackPressEnable(false);
        setOutSideDismiss(false);
        mCommentId = commentId;
        getCommentDetails();
    }

    private void initUI() {
        if (receiveCommentMsg == null) return;
        findViewById(R.id.im_clone).setOnClickListener(this);

        ImageLoader.loadImg(ivHead,receiveCommentMsg.getAnchor().getHeadImg());
        tvNick.setText(receiveCommentMsg.getAnchor().getNickname());

        ImageLoader.loadImg(ivThead,receiveCommentMsg.getUser().getHeadImg());
        tvTnick.setText(receiveCommentMsg.getUser().getNickname());

        tvVideoTime.setText("缘分时长："+ TimeUtils.long2String(receiveCommentMsg.getDuration()));
        ratingBar.showImage(receiveCommentMsg.getStartLevel());

        RecyclerView rvTab = findViewById(R.id.rv_tab);
        rvTab.addItemDecoration(new GridItemDecoration(mContext, R.color.white, R.dimen.dp_5, R.dimen.dp_21));
        rvTab.setLayoutManager(new FlowLayoutManager());
        mLabelAdapter = new ReceiveCommentLabelAdapter();
        rvTab.setAdapter(mLabelAdapter);
        showTagList(receiveCommentMsg.getEvTypes());

        rvGift = findViewById(R.id.rv_gift);
        rvGift.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        rvGift.addItemDecoration(new GridItemDecoration(mContext, R.color.white, R.dimen.dp_10, R.dimen.dp_10));
        mGiftAdapter = new ReceiveCommentGiftAdapter();
        rvGift.setAdapter(mGiftAdapter);
        showGiftList(receiveCommentMsg.getGiftList());
    }

    private void getCommentDetails() {
        mCompositeDisposable = new CompositeDisposable();
        RetrofitUtils retrofitUtils = new RetrofitUtils();
        FlirtApiService service = retrofitUtils.createWenboApi(FlirtApiService.class);
        WenboParamsBuilder builder = new WenboParamsBuilder();
        builder.put("id", mCommentId + "");

        mCompositeDisposable.add(service.getCommentDetails(builder.build())
                        .compose(RxUtils.applySchedulers())
                        .subscribeWith(new LoadingObserver<HttpResult<CommentDetailsDTO>>() {

                            @Override
                            public void _onNext(HttpResult<CommentDetailsDTO> data) {
                                if (data.isSuccess()) {
                                    initUI2(data.data);
                                } else {
                                    ToastUtils.showShort(getContext(), data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                ToastUtils.showShort(getContext(), msg);
                            }
                        }));
    }


    private void initUI2(CommentDetailsDTO commentDetailsDTO) {
        if (commentDetailsDTO.getId() == null) return;
        findViewById(R.id.im_clone).setOnClickListener(this);
        ImageLoader.loadImg(ivHead,commentDetailsDTO.getAnchorHeadImg());
        tvNick.setText(commentDetailsDTO.getAnchorNickname());

        ImageLoader.loadImg(ivThead,commentDetailsDTO.getUserHeadImg());
        tvTnick.setText(commentDetailsDTO.getUserNickname());

        tvVideoTime.setText("缘分时长："+ TimeUtils.long2String(commentDetailsDTO.getDuration()));
        ratingBar.showImage(commentDetailsDTO.getStartLevel());

        RecyclerView rvTab = findViewById(R.id.rv_tab);
        rvTab.addItemDecoration(new GridItemDecoration(mContext, R.color.white, R.dimen.dp_5, R.dimen.dp_21));
        rvTab.setLayoutManager(new FlowLayoutManager());
        mLabelAdapter = new ReceiveCommentLabelAdapter();
        rvTab.setAdapter(mLabelAdapter);
        showTagList(commentDetailsDTO.getEvTypes());

        rvGift = findViewById(R.id.rv_gift);
        rvGift.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        rvGift.addItemDecoration(new GridItemDecoration(mContext, R.color.white, R.dimen.dp_10, R.dimen.dp_10));
        mGiftAdapter = new ReceiveCommentGiftAdapter();
        rvGift.setAdapter(mGiftAdapter);
        showGiftList2(commentDetailsDTO.getGiftList());
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popup_window_receive_comment);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.im_clone){
            dismiss();
        }
    }

    private void showTagList(String tagLabels) {
        if (TextUtils.isEmpty(tagLabels)) return;
        String[] tagArr = tagLabels.split(",");
        mLabelAdapter.setNewData(Arrays.asList(tagArr));
    }

    private void showGiftList(List<ReceiveCommentMsg.GiftListDTO> giftList) {
        if (giftList == null || giftList.size() == 0) {
            rvGift.setVisibility(View.GONE);
        } else {
            rvGift.setVisibility(View.VISIBLE);
            List<ReceiveCommentGiftAdapter.GiftListDTO> adapterList = new ArrayList<>();
            for (ReceiveCommentMsg.GiftListDTO gift : giftList) {
                ReceiveCommentGiftAdapter.GiftListDTO dto = new ReceiveCommentGiftAdapter.GiftListDTO();
                dto.setGiftName(gift.getGiftName());
                dto.setGiftNum(gift.getGiftNum());
                dto.setGiftSmallImgPath(gift.getGiftSmallImgPath());
                adapterList.add(dto);
            }
            mGiftAdapter.setNewData(adapterList);
        }
    }

    private void showGiftList2(List<CommentDetailsDTO.GiftListDTO> giftList) {
        if (giftList == null || giftList.size() == 0) {
            rvGift.setVisibility(View.GONE);
        } else {
            rvGift.setVisibility(View.VISIBLE);
            List<ReceiveCommentGiftAdapter.GiftListDTO> adapterList = new ArrayList<>();
            for (CommentDetailsDTO.GiftListDTO gift : giftList) {
                ReceiveCommentGiftAdapter.GiftListDTO dto = new ReceiveCommentGiftAdapter.GiftListDTO();
                dto.setGiftName(gift.getGiftName());
                dto.setGiftNum(gift.getGiftCount());
                dto.setGiftSmallImgPath(gift.getSmallImgPath());
                adapterList.add(dto);
            }
            mGiftAdapter.setNewData(adapterList);
        }
    }

}
