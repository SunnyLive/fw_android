package com.fengwo.module_flirt.UI.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_chat.mvp.model.bean.CommentModel;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.event.FindDetailChangedEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_flirt.adapter.CommentAdapter2;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.EPSoftKeyBoardListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.ShareHelper;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.Interfaces.IFindDetailView;
import com.fengwo.module_flirt.P.FindDetailPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.bean.FindDetailBean;
import com.fengwo.module_flirt.dialog.FindMuchPopwindow;
import com.fengwo.module_comment.widget.DetailCardPopView;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShareCommonPopwindow;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/9/23
 */
@Route(path = ArouterApi.FLIRT_FIND_DETAIL_ACTION)
public class FindDetailActivity extends BaseMvpActivity<IFindDetailView, FindDetailPresenter> implements IFindDetailView {
    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.iv_head)
    CircleImageView ivHead;
    @BindView(R2.id.tv_nick)
    TextView tvNick;
    @BindView(R2.id.tv_address)
    TextView tvAddress;
    @BindView(R2.id.tv_attention)
    TextView tvAttention;
    @BindView(R2.id.iv_more)
    ImageView ivMore;
    TextView tvContent;
    TextView tvTime;
    View viewBg;
    @BindView(R2.id.rv_content)
    RecyclerView recyclerview;
    @BindView(R2.id.tv_find_shares)
    TextView tvFindShares;
    @BindView(R2.id.tv_find_like)
    TextView tvFindLike;
    @BindView(R2.id.tv_find_comments)
    TextView tvFindComments;
    @BindView(R2.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R2.id.status_bar_view)
    View statusBarView;
    DetailCardPopView detailViewPop;
    @BindView(R2.id.view_bottom_input)
    LinearLayout view_bottom_input;
    @BindView(R2.id.fl_bottom)
    FrameLayout flBottom;
    @BindView(R2.id.et_input)
    EditText etInput;
    @BindView(R2.id.tv_discuss)
    TextView tv_discuss;

    @Autowired
    UserProviderService userProviderService;

    private int page = 1;
    private final String PAGE_SIZE = ",20";
    private CommentAdapter2 commentAdapter;
    private String totalCount;
    private String id;
    private int CardId;
    private FindMuchPopwindow muchPopwindow;
    private FindDetailBean findDetailBean;

    private FindDetailChangedEvent findDetailChangedEvent;

    @Override
    public FindDetailPresenter initPresenter() {
        return new FindDetailPresenter();
    }


    @Override
    protected void initView() {
        ImmersionBar.setStatusBarView(this, statusBarView);
        CardId = getIntent().getIntExtra("id", 0);
        p.getDetailData(CardId);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter2();
        commentAdapter.setEnableLoadMore(true);
        commentAdapter.setOnLoadMoreListener(() -> {
            page += 1;
            p.getComments(CardId + "", page + PAGE_SIZE);
        }, recyclerview);
        commentAdapter.disableLoadMoreIfNotFullPage();
        commentAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            int id = view1.getId();
            if (id == R.id.iv_find_like) {
                p.likeComment(position, -1, commentAdapter.getData().get(position).commentId);
            }
        });
        initHeadView();
//        commentAdapter.setOnChildItemClickListener(new CommentAdapter.OnChildItemClickListener() {
//            @Override
//            public void commentLike(int parentPosition, int position, CommentModel commentModel) {
//                likeComment(parentPosition, position, commentModel.commentId);
//            }
//
//            @Override
//            public void commentClick(int parentPosition, int position, CommentModel commentModel) {
//                if (listener != null)
//                    listener.comment(parentPosition, position, 2, commentModel, id,
//                            commentAdapter.getData().get(parentPosition).commentId);
//            }
//        });
//        commentAdapter.setOnItemClickListener((adapter, view12, position) -> {// 回复一级评论
//            if (listener != null) {
//                listener.comment(position, -1, 2, commentAdapter.getData().get(position),
//                        id, commentAdapter.getData().get(position).commentId);
//            }
//        });
        recyclerview.setAdapter(commentAdapter);
        recyclerview.setItemAnimator(null);
        page = 1;
        p.getComments(CardId + "", page + PAGE_SIZE);

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
//                view_bottom_notice.setVisibility(View.GONE);
//                rvChat.scrollToPosition((rvChat.getAdapter()).getItemCount() - 1);

            }
        });
    }

    private void initHeadView() {
        View headView = LayoutInflater.from(this).inflate(R.layout.head_find_detail, null);
        commentAdapter.addHeaderView(headView);
        detailViewPop = headView.findViewById(R.id.detail_view_pop);
        tvContent = headView.findViewById(R.id.tv_content);
        tvTime = headView.findViewById(R.id.tv_time);
        viewBg = headView.findViewById(R.id.view_bg);
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
    protected int getContentView() {
        return R.layout.activity_find_detail;
    }


    @Override
    public void setData(FindDetailBean findDetailBean) {
        this.findDetailBean = findDetailBean;

        //创建通知列表刷新的额event
        if (findDetailChangedEvent == null) {
            findDetailChangedEvent = new FindDetailChangedEvent();
        }
        findDetailChangedEvent.setIsLike(findDetailBean.getIsLike());
        findDetailChangedEvent.setLikeCount(findDetailBean.getLikes());
        findDetailChangedEvent.setCommentsCount(findDetailBean.getComments());

        //如果是本人隐藏关注和更多入口
        if (findDetailBean.getUserId() == userProviderService.getUserInfo().getId()) {
            tvAttention.setVisibility(View.GONE);
            ivMore.setVisibility(View.GONE);
        } else {
            tvAttention.setVisibility(View.VISIBLE);
            ivMore.setVisibility(View.VISIBLE);
        }

        ImageLoader.loadCircleImg(ivHead, findDetailBean.getHeadImg());
        tvNick.setText(findDetailBean.getNickname());
        if (TextUtils.isEmpty(findDetailBean.getPosition())) {
            tvAddress.setVisibility(View.INVISIBLE);
        } else {
            tvAddress.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(findDetailBean.getDistance())) {
                double distance = Double.parseDouble(findDetailBean.getDistance());
                tvAddress.setText(String.format("%s %s", findDetailBean.getPosition(), DataFormatUtils.formatNumberKm(distance)));
            } else {
                tvAddress.setText(findDetailBean.getPosition());
            }
        }

        tvContent.setText(findDetailBean.getExcerpt());
        tvTime.setText(findDetailBean.getTime());

        setTextValue(tvFindShares, findDetailBean.getShares(),"分享");
        setTextValue(tvFindLike, findDetailBean.getLikes(),"点赞");
        setTextValue(tvFindComments, findDetailBean.getComments(),"评论");


        if (findDetailBean.getCover() == null || findDetailBean.getCover().size() == 0) {
            detailViewPop.setVisibility(View.GONE);
        } else {
            detailViewPop.setBanner((ArrayList) findDetailBean.getCover(), 0);
            detailViewPop.addClickPlayListener((videoTime, current) ->
                    DetailCardActivity.start(FindDetailActivity.this,
                            (ArrayList) findDetailBean.getCover(), 0)
            );
        }
        tvFindLike.setSelected(findDetailBean.getIsLike() == 1);

        refreshAttention();
    }

    /**
     * 刷新关注状态
     */
    private void refreshAttention() {
        if (findDetailBean.getIsAttention() == 1) {
            tvAttention.setText("已关注");
            tvAttention.setTextColor(getResources().getColor(R.color.text_99));
            tvAttention.setSelected(true);
        } else {
            tvAttention.setText("关注");
            tvAttention.setTextColor(getResources().getColor(R.color.red_FE3ACE));
            tvAttention.setSelected(false);
        }
    }

    @Override
    public void setCommentSuccess(CommentModel commentModel) {
        KeyBoardUtils.closeKeybord(etInput, this);
        totalCount = String.valueOf(Integer.parseInt(totalCount) + 1);
        setTextValue(tvFindComments, Integer.parseInt(totalCount),"评论");
        commentAdapter.addData(0, commentModel);
        recyclerview.scrollToPosition(0);
        if (findDetailChangedEvent != null) {
            findDetailChangedEvent.setCommentsCount(Integer.parseInt(totalCount));
            RxBus.get().post(findDetailChangedEvent);
        }
    }

    @Override
    public void setComments(BaseListDto<CommentModel> data) {
        ArrayList<CommentModel> comments = data.records;
        totalCount = data.total;
        setTextValue(tvFindComments, Integer.parseInt(totalCount),"评论");
        if (page == 1) {
            commentAdapter.setNewData(comments);
        } else if (comments == null || comments.size() < 20) {
            if (comments != null) commentAdapter.addData(comments);
            commentAdapter.loadMoreEnd();
        } else {
            commentAdapter.addData(comments);
            commentAdapter.loadMoreComplete();
        }
    }

    @Override
    public void commentLike(int index) {
        CommentModel commentModel = commentAdapter.getData().get(index);
        int isLike = commentModel.isLike;
        commentModel.isLike = isLike == 1 ? 0 : 1;
        if (isLike == 1) {
            commentModel.likes -= 1;
        } else commentModel.likes += 1;
        commentAdapter.setData(index, commentModel);
    }

    @Override
    public void cardLikeSuccess(int id) {
        /*获取点赞*/
        if (findDetailBean.getIsLike() == 1) {
            findDetailBean.setIsLike(0);
            findDetailBean.setLikes(findDetailBean.getLikes() - 1);
            tvFindLike.setSelected(false);
        } else {
            findDetailBean.setIsLike(1);
            findDetailBean.setLikes(findDetailBean.getLikes() + 1);
            tvFindLike.setSelected(true);
        }
        setTextValue(tvFindLike, findDetailBean.getLikes(),"点赞");
        if (findDetailChangedEvent != null) {
            findDetailChangedEvent.setLikeCount(findDetailBean.getLikes());
            findDetailChangedEvent.setIsLike(findDetailBean.getIsLike());
            RxBus.get().post(findDetailChangedEvent);
        }
    }

    @Override
    public void getShareUrlSuccess(String url, int type, String imgUrl, String content) {
        /*获取分享成功*/
        ShareHelper.get().shareDynamic(this, url, "蜂窝互娱", imgUrl, content, type);
    }


    @OnClick({R2.id.iv_back, R2.id.tv_discuss, R2.id.btn_send, R2.id.iv_more, R2.id.tv_attention,
            R2.id.tv_find_like, R2.id.tv_find_shares, R2.id.tv_find_comments, R2.id.iv_head})
    void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.tv_discuss || v.getId() == R.id.tv_find_comments) {//评论
            showSendMsgEdit("");
        } else if (v.getId() == R.id.btn_send) {
            String content = etInput.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return;
            }
            p.sendComment(CardId, content);
            etInput.setText("");
        } else if (v.getId() == R.id.iv_more) {
            if (muchPopwindow == null) {
                muchPopwindow = new FindMuchPopwindow(this);
                muchPopwindow.addOnClickListener((uid, pos) -> addtention(uid));
            }
            muchPopwindow.setAttention(findDetailBean.getIsAttention(), findDetailBean.getUserId(), 0);
            muchPopwindow.showPopupWindow();
        } else if (v.getId() == R.id.tv_attention) {//关注
            if (findDetailBean != null) {
                addtention(findDetailBean.getUserId());
            }
        } else if (v.getId() == R.id.iv_head) {//头像 跳转到详情
            if (findDetailBean != null) {
                MineDetailActivity.startActivityWithUserId(this, findDetailBean.getUserId());
            }
        } else if (v.getId() == R.id.tv_find_like) {//点赞
            if (findDetailBean != null) {
                p.cardLike(findDetailBean.getId());
            }
        } else if (v.getId() == R.id.tv_find_shares) {//分享
            List<FindDetailBean.CoverBean> cover = findDetailBean.getCover();
            showShareDialog(findDetailBean.getId(), cover != null && cover.size() > 0 ? cover.get(0).getImageUrl() : "", findDetailBean.getExcerpt());
        }
    }

    protected void showShareDialog(int id, String imgUrl, String content) {
        ShareCommonPopwindow shareCommonPopwindow = new ShareCommonPopwindow(this);
        shareCommonPopwindow.addClickListener(new ShareCommonPopwindow.OnShareClickListener() {
            @Override
            public void onWx() {
                p.getShareInfo(id, 0, imgUrl, content);
            }

            @Override
            public void onWxCircle() {
                p.getShareInfo(id, 1, imgUrl, content);
            }
        });
        shareCommonPopwindow.showPopupWindow();
    }


    public void addtention(int uid) {
        if (findDetailBean.getIsAttention() == 1) {
            AttentionUtils.delAttention(uid, new LoadingObserver<HttpResult>() {
                @Override
                public void _onNext(HttpResult data) {
                    findDetailBean.setIsAttention(0);
                    refreshAttention();
                }

                @Override
                public void _onError(String msg) {
                    if (TextUtils.isEmpty(msg)) return;
                    ToastUtils.showShort(FindDetailActivity.this, msg);
                }
            });
        } else {
            AttentionUtils.addAttention(uid, new LoadingObserver<HttpResult>() {
                @Override
                public void _onNext(HttpResult data) {
                    findDetailBean.setIsAttention(1);
                    refreshAttention();
                }

                @Override
                public void _onError(String msg) {
                    if (TextUtils.isEmpty(msg)) return;
                    ToastUtils.showShort(FindDetailActivity.this, msg);
                }
            });
        }
    }

    private void showSendMsgEdit(String msg) {
        llBottom.setVisibility(View.GONE);
        view_bottom_input.setVisibility(View.VISIBLE);
        etInput.setText(msg);
        etInput.requestFocus();
        etInput.setSelection(msg.length());
        KeyBoardUtils.openKeybord(etInput, this);
    }

    @Override
    protected void onDestroy() {
        detailViewPop.destroy();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int[] leftTop = {0, 0};
        //获取输入框当前的location位置
        view_bottom_input.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + view_bottom_input.getHeight();
        int right = left + view_bottom_input.getWidth();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x > left && x < right && y > top && y < bottom) {
            // 点击的是发送按钮，保留点击EditText的事件
            // 必不可少，否则所有的组件都不会有TouchEvent了
            if (getWindow().superDispatchTouchEvent(event)) {
                return true;
            }
            return onTouchEvent(event);
        }
        handlerOtherTypeDispatchTouchEvent(event);
        return false;
    }

    private boolean handlerOtherTypeDispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, event)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(event);
        } else if (action == MotionEvent.ACTION_MOVE) {

        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                v.setFocusable(false);
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }


    private void setTextValue(TextView tv, int value,String defaultValue) {
        tv.setText(value > 99 ? "99+" : value <= 0 ? defaultValue : value + "");
    }
}
