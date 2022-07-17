package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.FlowLayoutManager;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.adapter.FinshChatLabelAdapter;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.BeforeQuitMsg;
import com.fengwo.module_flirt.bean.CommentTagDto;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

/**
 * 聊天结束弹窗
 */
public class FinishChatPopwindow extends BasePopupWindow implements View.OnClickListener {
    @BindView(R2.id.tv_video_time)
    TextView tvVideoTime;
    @BindView(R2.id.tv_expense)
    TextView tvExpense;
    @BindView(R2.id.iv_head)
    CircleImageView ivHead;
    @BindView(R2.id.tv_nick)
    TextView tvNick;
    @BindView(R2.id.tv_price)
    TextView tvPrice;
    @BindView(R2.id.rb_grade)
    ScaleRatingBar ratingBar;
    @BindView(R2.id.iv_reward)
    ImageView ivRewara;
    @BindView(R2.id.iv_t_head)
    CircleImageView ivThead;
    @BindView(R2.id.tv_t_nick)
    TextView tvTnick;
    @BindView(R2.id.im_clone)
    ImageView im_clone;


    private CompositeDisposable mConpositeDisposable;
    FinshChatLabelAdapter finshChatLabelAdapter;
    @Autowired
    UserProviderService service;
    private String roomId;
    private long anthorId;
    private Context mContext;
    private long livingRoomUserId;

    public FinishChatPopwindow(Context context, long anthorId, String roomId,long livingRoomUserId,UserProviderService service) {
        super(context);
        mContext = context;
        this.service = service;
        this.livingRoomUserId = livingRoomUserId;
        setPopupGravity(Gravity.BOTTOM);
        this.roomId = roomId;
        this.anthorId = anthorId;
        initUI();
        setBackPressEnable(false);
        setOutSideDismiss(false);
    }

    private void initUI() {
        mConpositeDisposable = new CompositeDisposable();
        findViewById(R.id.tv_finsh_chat).setOnClickListener(this);
        findViewById(R.id.im_clone).setOnClickListener(this);

        ivRewara.setOnClickListener(this);
        RecyclerView rvTab = findViewById(R.id.rv_tab);
        rvTab.setLayoutManager(new FlowLayoutManager());
        finshChatLabelAdapter = new FinshChatLabelAdapter();
        rvTab.setAdapter(finshChatLabelAdapter);
        getTagList();
        getBeforQuitMsg();
        ImageLoader.loadImg(ivThead,service.getUserInfo().headImg+"");
        tvTnick.setText(service.getUserInfo().nickname+"");
    }

    @Override
    public boolean isShowing() {
        ImageLoader.loadGif(findViewById(R.id.iv_reward), R.drawable.gif_gift);
        return super.isShowing();

    }
    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_finsh_chat);
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


    public void addOnClickListener(OnSureListener listener) {
        onSureListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_finsh_chat) {
            appraiseHost();
        } else if (v.getId() == R.id.iv_reward) {
            if (null != onSureListener)
                onSureListener.onSendGift();
        }else if(v.getId() == R.id.im_clone){
            onSureListener.onDis();
        }
    }

    public interface OnSureListener {
        void sure();

        void onSendGift();

        void onFail();

        void onDis();
    }

    public OnSureListener onSureListener;

    private void getTagList() {
        WenboParamsBuilder builder = new WenboParamsBuilder();
        builder.put("current", 1 + "")
                .put("size", 1000 + "");
        mConpositeDisposable.add(new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .getCommentTag(builder.build())
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<List<CommentTagDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<CommentTagDto>> data) {
                        if (data.isSuccess()) {
                            finshChatLabelAdapter.setNewData(data.data);
                        } else if (data.isTokenInvalid()&&onSureListener!=null){
                            onSureListener.onFail();
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(mContext,msg);
                    }
                }));
    }

    private void getBeforQuitMsg() {
        WenboParamsBuilder builder = new WenboParamsBuilder();
        builder.put("livingRoomUserId", livingRoomUserId + "");
        mConpositeDisposable.add(new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .getBeforQuitMsg(builder.build())
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<BeforeQuitMsg>>() {
                    @Override
                    public void _onNext(HttpResult<BeforeQuitMsg> data) {
                        if (data.isSuccess()) {
                            ImageLoader.loadImg(ivHead, data.data.headImg);
                            tvNick.setText(data.data.nickname);
                            tvPrice.setText(data.data.price);
                            tvVideoTime.setText("缘分时长："+data.data.time);
                            tvExpense.setText(data.data.conPrice + "");
                        } else if (data.isTokenInvalid()&&onSureListener!=null){
                            onSureListener.onFail();
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    private void appraiseHost() {
//        String sparseArray = getEvTupes();
        WenboParamsBuilder builder = new WenboParamsBuilder();
        int score = ratingBar.getRating() < 0 ? 0 : (int) ratingBar.getRating();
        String evTypes = finshChatLabelAdapter.getValue();
        if (TextUtils.isEmpty(evTypes)) {
            ToastUtils.showShort(mContext, "请选择标签");
            return;
        }
        builder.put("anchorId", anthorId + "")
                .put("startLevel", score + "")
                .put("livingRoomUserId", livingRoomUserId + "")
                .put("evTypes", finshChatLabelAdapter.getValue());
        mConpositeDisposable.add(new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .appraiseHost(builder.build())
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        L.e("========"+data.status);
                        L.e("========"+data.description);
                        if (data.isSuccess() && onSureListener != null) {
                            ToastUtils.showShort(mContext, "评价成功");
                            dismiss();
                            onSureListener.sure();
                        } else if (data.isTokenInvalid()&&onSureListener!=null){
                            onSureListener.onFail();
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        L.e("========"+msg);
                        ToastUtils.showShort(mContext, msg);
                        onSureListener.onFail();
                    }
                }));
    }

}
