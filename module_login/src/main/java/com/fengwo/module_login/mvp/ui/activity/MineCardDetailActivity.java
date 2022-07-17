/*
 *
 *  个人中心 -> 动态列表 -> 动态详情页面
 *
 *
 */
package com.fengwo.module_login.mvp.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.FindDetailChangedEvent;
import com.fengwo.module_comment.event.RefreshCardList;
import com.fengwo.module_comment.utils.EPSoftKeyBoardListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.ShareHelper;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.DetailCardPopView;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShareCommonPopwindow;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.MCD_CommentDto;
import com.fengwo.module_login.mvp.dto.MineCardDetailDto;
import com.fengwo.module_login.mvp.dto.ShareUrlDto;
import com.fengwo.module_login.mvp.presenter.MineCardDetailPresenter;
import com.fengwo.module_login.mvp.ui.adapter.MCD_CommentAdapter;
import com.fengwo.module_login.mvp.ui.iview.IMineCardDetailView;
import com.fengwo.module_login.mvp.ui.pop.AuthorityPopWindow;
import com.fengwo.module_login.mvp.ui.pop.CardDetailView;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@Route(path = ArouterApi.MINE_DETAIL_CARD_ACTION)
public class MineCardDetailActivity extends BaseMvpActivity<IMineCardDetailView, MineCardDetailPresenter> implements IMineCardDetailView {

    public static String CHAR_CARD_ID = "id";

    @BindView(R2.id.status_bar_view)
    View statusBarView;
    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;  //数据列表
    @BindView(R2.id.iv_head)
    CircleImageView ivHead;   //头像
    @BindView(R2.id.tv_nick)
    TextView tvNick;
    @BindView(R2.id.tv_find_like)
    TextView tvFindLike;    //点赞
    @BindView(R2.id.tv_find_comments)
    TextView tvComments;    //评论数
    @BindView(R2.id.tv_find_shares)
    TextView tvShares;    //分享数
    @BindView(R2.id.tv_address)
    TextView tvAddress;
    @BindView(R2.id.et_input)
    EditText etInput;
    @BindView(R2.id.ll_bottom)
    View llBottom;
    @BindView(R2.id.fl_bottom)
    FrameLayout flBottom;
    @BindView(R2.id.view_bottom_input)
    LinearLayout view_bottom_input;

    @BindView(R2.id.tv_hint_label)
    TextView tvHintLabel;
    @BindView(R2.id.tv_hint_content)
    TextView tvHintContent;
    @BindView(R2.id.ll_content_violation)
    View mLlViolation;

    private TextView tvCommentHint;
    private DetailCardPopView detailViewPop;
    private TextView tvContent;
    private TextView tvTime;

    private View viewBg;

    private CardDetailView mMineMoreView;


    private MCD_CommentAdapter mMcd_commentAdapter;
    private ShareCommonPopwindow mShareCommonWindow;//分享弹框

    private int mCardId;
    private int mPageIndex = 1;
    private int mShareType = 0; //分享类型
    private String mShareHeadImg = "";
    private String mShareContent = "";

    @Override
    public MineCardDetailPresenter initPresenter() {
        return new MineCardDetailPresenter();
    }


    @Override
    protected void initView() {
        ((View) statusBarView.getParent()).setEnabled(false);
        ImmersionBar.setStatusBarView(this, statusBarView);
        mCardId = getIntent().getIntExtra(CHAR_CARD_ID, 0);
        p.getCardDetail(mCardId);
        p.getCommentList(mCardId, mPageIndex);
        mMcd_commentAdapter = new MCD_CommentAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mMcd_commentAdapter.setEnableLoadMore(true);
        mMcd_commentAdapter.setOnLoadMoreListener(() -> {
            //加载评论数据
            mPageIndex += 1;
            p.getCommentList(mCardId, mPageIndex);
        }, mRvContent);
        mMcd_commentAdapter.disableLoadMoreIfNotFullPage();
        initHeadView();

        mRvContent.setAdapter(mMcd_commentAdapter);
        mMineMoreView = new CardDetailView(this);
        mMcd_commentAdapter.setOnItemChildClickListener((adapter, v, position) -> {
            int id = v.getId();
            if (id == R.id.iv_find_like) {
                p.requestLikeComment(position, mMcd_commentAdapter.getData().get(position).commentId);
            }
        });
        mMineMoreView.setOnMoreItemClickListener(new CardDetailView.OnItemClickListener() {
            @Override
            public void modify() {
                ARouter.getInstance().build(ArouterApi.CHAT_POST_TREND)
                        .withInt("id", mCardId)
                        .navigation(MineCardDetailActivity.this, 1000);
                onBackPressed();
            }

            @Override
            public void delete() {
                requestCardDel();
            }

            @Override
            public void authority() {
                AuthorityPopWindow aw = new AuthorityPopWindow(MineCardDetailActivity.this);
                aw.setOnItemChooseListener(state -> p.setCardAuthority(mCardId, state));
                aw.showPopupWindow();
            }

            //状态范围 0 去掉置顶 1 置顶
            @Override
            public void stick() {
                p.setCardStick(mCardId, 1);
            }

            @Override
            public void unStick() {
                p.setCardStick(mCardId, 0);
            }
        });
        EPSoftKeyBoardListener.setListener(this, new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) flBottom.getLayoutParams();
                params.bottomMargin = height;
                flBottom.setLayoutParams(params);
            }

            @Override
            public void keyBoardHide(int height) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) flBottom.getLayoutParams();
                params.bottomMargin = 0;
                flBottom.setLayoutParams(params);
                llBottom.setVisibility(View.VISIBLE);
                view_bottom_input.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 设置 RecyclerView 头部的view
     */
    private void initHeadView() {
        View headView = LayoutInflater.from(this).inflate(R.layout.layout_head_card_detail, null);
        mMcd_commentAdapter.addHeaderView(headView);
        tvCommentHint = headView.findViewById(R.id.tv_comment_hint);
        detailViewPop = headView.findViewById(R.id.detail_view_pop);
        tvContent = headView.findViewById(R.id.tv_content);
        tvTime = headView.findViewById(R.id.tv_time);
        viewBg = headView.findViewById(R.id.view_bg);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_mine_card_detail;
    }


    @Override
    public void onResume() {
        super.onResume();
        detailViewPop.refreshView();
    }

    @Override
    public void onPause() {
        super.onPause();
        detailViewPop.stopPlay();
    }

    @Override
    public void onBackPressed() {
        String charLikes = tvFindLike.getTag() == null ? null : tvFindLike.getTag().toString();
        String charComments = tvComments.getTag() == null ? null : tvComments.getTag().toString();
        String charShares = tvShares.getTag() == null ? null : tvShares.getTag().toString();
        int likes = Integer.parseInt(TextUtils.isEmpty(charLikes) ? "0" : charLikes);//点赞
        int comments = Integer.parseInt(TextUtils.isEmpty(charComments) ? "0" : charComments);//评论数
        int shares = Integer.parseInt(TextUtils.isEmpty(charShares) ? "0" : charShares);    //分享数
        int isLike = tvFindLike.isSelected() ? 1 : 0;
        RxBus.get().post(new FindDetailChangedEvent(likes, shares, comments, isLike));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        detailViewPop.destroy();
        super.onDestroy();
    }

    /**
     * 当前视图的点击事件
     * <p>
     * R2.id.iv_back 返回
     * R2.id.iv_more 更多操作
     *
     * @param v v
     */
    @OnClick({
            R2.id.iv_back, //返回
            R2.id.iv_more, //更多
            R2.id.tv_discuss, //评论一下
            R2.id.btn_send,  //发送评论
            R2.id.tv_find_like, //点赞
            R2.id.tv_find_shares, //分享
            R2.id.tv_find_comments, //评论
            R2.id.iv_head     //头像
    })
    public void onViewClick(View v) {
        // 点击返回
        if (v.getId() == R.id.iv_back) {
            onBackPressed();
        }
        // 更多操作
        else if (v.getId() == R.id.iv_more) {
            mMineMoreView.showPopupWindow();
        }
        //评论
        else if (v.getId() == R.id.tv_discuss || v.getId() == R.id.tv_find_comments) {
            showSendMsgEdit("");
        }
        //发送评论
        else if (v.getId() == R.id.btn_send) {
            String content = etInput.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return;
            }
            p.sendComment(mCardId, content);
            etInput.setText("");
        }
        //点赞
        else if (v.getId() == R.id.tv_find_like) {
            int like = 0;
            if (!TextUtils.isEmpty(tvFindLike.getTag().toString())) {
                like = Integer.parseInt(tvFindLike.getTag().toString());
            }
            tvFindLike.setSelected(!tvFindLike.isSelected());
            if (tvFindLike.isSelected()) {
                ++like;
            } else {
                like = like != 0 ? like - 1 : 0;
            }
            setTextValue(tvFindLike, like,"点赞");
            p.requestCardLike(mCardId);
        }
        //分享
        else if (v.getId() == R.id.tv_find_shares) {
            showShareDialog(mCardId);
        }
    }

    @Override
    public void resultDataSuccess(MineCardDetailDto d) {
        ((View) statusBarView.getParent()).setEnabled(true);
        ImageLoader.loadCircleImg(ivHead, mShareHeadImg = d.getHeadImg());
        tvNick.setText(d.getNickname());
        tvFindLike.setSelected(d.getIsLike());
        tvAddress.setText(d.getPosition());
        setTextValue(tvShares, d.getShares(),"分享");
        setTextValue(tvComments, d.getComments(),"评论");
        setTextValue(tvFindLike, d.getLikes(),"点赞");
        if (TextUtils.isEmpty(d.getPosition())) {
            tvAddress.setVisibility(View.INVISIBLE);
        } else {
            tvAddress.setVisibility(View.GONE);
            tvAddress.setText(d.getPosition());
        }
        tvContent.setText(mShareContent = d.getExcerpt());
        tvTime.setText(d.getTime());
        if (d.getCover() == null || d.getCover().size() == 0) {
            detailViewPop.setVisibility(View.GONE);
        } else {
            detailViewPop.setBanner((ArrayList) d.getCover(), 0);
            detailViewPop.addClickPlayListener((videoTime, current) -> {
                ARouter.getInstance().build(ArouterApi.FLIRT_DETAIL_CARD_ACTION)
                        .withSerializable("bannerList", (Serializable) d.getCover())
                        .withInt("position", 0)
                        .navigation();
            });
        }
        /*
         * 动态模块：
         * 1成功           ： 置顶（取消置顶）  权限  删除  取消
         * 4草稿，5拒审     ： 编辑  删除  取消
         * 0审核中          ： 删除  取消
         * 3封禁           ： 删除  取消
         * -----------------------------------------------
         */
        mMineMoreView.setStick(TextUtils.isEmpty(d.getTopTime()));
        mMineMoreView.chooseItem(d.getCardStatus());
        tvHintContent.setText(d.getRemark());
        mLlViolation.setVisibility(View.VISIBLE);
        switch (d.getCardStatus()) {
            case 0:
                tvHintLabel.setText("审核中,仅自己可见");
                tvHintContent.setText("蜂窝君正在努力审核中,请稍等…");
                tvHintLabel.setTextColor(ContextCompat.getColor(this, R.color.blue_63A5FF));
                break;
            case 1:
                mLlViolation.setVisibility(View.GONE);
                llBottom.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvHintLabel.setText("内容涉及违规已封禁");
                tvHintLabel.setTextColor(ContextCompat.getColor(this, R.color.red_ff5a6d));
                break;
            case 4:
            case 5:
                tvHintLabel.setText("草稿");
                tvHintLabel.setTextColor(ContextCompat.getColor(this, R.color.gray_DDDDDD));
                break;

        }
    }

    @Override
    public void resultDataFail(String f) {
        ((View) statusBarView.getParent()).setEnabled(false);
        ToastUtils.show(getApplicationContext(),f,2000);
        onBackPressed();
    }

    @Override
    public void resultCommentsSuccess(BaseListDto<MCD_CommentDto> d) {
        //tvFindComments.setText(totalCount + "");
        if (mPageIndex == 1) {
            mMcd_commentAdapter.setNewData(d.records);
        } else if (d.records == null || d.records.size() < 20) {
            if (d.records != null) mMcd_commentAdapter.addData(d.records);
            mMcd_commentAdapter.loadMoreEnd();
        } else {
            mMcd_commentAdapter.addData(d.records);
            mMcd_commentAdapter.loadMoreComplete();
        }
        if (mMcd_commentAdapter.getData().isEmpty()) {
            tvCommentHint.setVisibility(View.VISIBLE);
        }else {
            tvCommentHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void resultCommentsFail(String f) {

    }

    @Override
    public void sendCommentResultSuccess(MCD_CommentDto d) {
        KeyBoardUtils.closeKeybord(etInput, this);
        int comments = 0;
        if (!TextUtils.isEmpty(tvComments.getText())) {
            comments = Integer.parseInt(tvComments.getText().toString());
        }
        ++comments;
        setTextValue(tvComments, comments,"评论");
        mMcd_commentAdapter.addData(0, d);
        mMcd_commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendCommentResultFail(String f) {

    }

    @Override
    public void resultStickSuccess(boolean isStick) {
        ToastUtils.show(this, mMineMoreView.isStick() ? "置顶成功" : "取消置顶成功", 1000);
        mMineMoreView.setStick(!mMineMoreView.isStick());
        RxBus.get().post(new RefreshCardList());
    }

    @Override
    public void resultStickFail(String f) {

    }

    @Override
    public void resultAuthoritySuccess(boolean isAuthor) {
        ToastUtils.show(this, "权限设置成功", 1000);
    }

    @Override
    public void resultAuthorityFail(String f) {

    }


    @Override
    public void resultShareSuccess(ShareUrlDto i) {
        int shares = 0;
        if (!TextUtils.isEmpty(tvShares.getText())) {
            shares = Integer.parseInt(tvShares.getText().toString());
        }
        ++shares;
        setTextValue(tvShares, shares,"分享");
        ShareHelper.get().shareDynamic(this,
                i.getUrl(),
                "蜂窝互娱",
                mShareHeadImg,
                mShareContent,
                mShareType);
    }

    @Override
    public void resultShareFail(String f) {

    }

    @Override
    public void resultCardLikeSuccess() {

    }

    @Override
    public void resultCardLikeFail(String f) {

    }

    @Override
    public void requestError(String e) {

    }

    @Override
    public void resultCommentLike(int position) {
        MCD_CommentDto MD = mMcd_commentAdapter.getData().get(position);
        int isLike = MD.isLike;
        MD.isLike = isLike == 1 ? 0 : 1;
        if (isLike == 1) {
            MD.likes -= 1;
        } else MD.likes += 1;
        mMcd_commentAdapter.setData(position, MD);
    }

    /**
     * 点击更多选择删除的操作
     */
    private void requestCardDel() {
        CommonDialog.getInstance("", "确定删除动态吗?", "取消", "确定", false)
                .addOnDialogListener(new CommonDialog.OnDialogListener() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void sure() {
                        Map map = new HashMap();
                        map.put("cardId", mCardId);
                        String sBody = new Gson().toJson(map);
                        RequestBody body = RequestBody.create(sBody, MediaType.parse("application/json"));
                        netManager.add(new RetrofitUtils().createApi(LoginApiService.class)
                                .deleteMineCard(body)
                                .compose(io_main())
                                .subscribeWith(new LoadingObserver<HttpResult>() {
                                    @Override
                                    public void _onNext(HttpResult data) {
                                        if (data.isSuccess()) {
                                            onBackPressed();
                                        } else {
                                            ToastUtils.show(getApplication(), data.description, 1000);
                                        }
                                    }

                                    @Override
                                    public void _onError(String msg) {

                                    }
                                }));
                    }
                }).show(getSupportFragmentManager(), "删除动态");
    }


    /**
     * 打开评论框
     *
     * @param msg 数据
     */
    private void showSendMsgEdit(String msg) {
        llBottom.setVisibility(View.GONE);
        view_bottom_input.setVisibility(View.VISIBLE);
        etInput.setText(msg);
        etInput.requestFocus();
        etInput.setSelection(msg.length());
        KeyBoardUtils.openKeybord(etInput, this);
    }


    /**
     * 微信分享
     *
     * @param id 动态id
     */
    private void showShareDialog(int id) {
        mShareCommonWindow = new ShareCommonPopwindow(this);
        mShareCommonWindow.addClickListener(new ShareCommonPopwindow.OnShareClickListener() {
            @Override
            public void onWx() {
                mShareType = 0;
                p.requestCardShare(id);
            }

            @Override
            public void onWxCircle() {
                mShareType = 1;
                p.requestCardShare(id);
            }
        });
        mShareCommonWindow.showPopupWindow();
    }

    private void setTextValue(TextView tv, int value,String defaultValue) {
        tv.setText(value > 99 ? "99+" : value == 0 ? defaultValue : value + "");
        tv.setTag(value);
    }

}
