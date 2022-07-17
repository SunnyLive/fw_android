package com.fengwo.module_chat.mvp.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.CommentModel;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.utils.chat.CommonFragmentPageAdapter;
import com.fengwo.module_chat.mvp.ui.event.CommentRefreshEvent;
import com.fengwo.module_comment.utils.chat.ChatEmotionFragment;
import com.fengwo.module_comment.utils.chat.GlobalOnItemClickManagerUtils;
import com.fengwo.module_comment.utils.chat.FaceConversionUtil;
import com.fengwo.module_comment.utils.chat.NoScrollViewPager;
import com.fengwo.module_comment.base.Constant;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SPUtils1;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CommentInputDialog extends DialogFragment {

    private NoScrollViewPager mVpBottomMore;

    private ArrayList<Fragment> mFragments;

    private EditText etComment;
    private View mEmotionLayout;
    private ImageView ivStatus;
    private ImageView ivSend;
    private View emptyView;

    private int type = 0;
    private CommentModel model;
    private String cardId;
    private int parentIndex;
    private int position;
    private int parentId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pop_comment_edit, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mVpBottomMore = view.findViewById(R.id.vp_bottom_more);
        etComment = view.findViewById(R.id.tvComment);
        ivStatus = view.findViewById(R.id.ivStatus);
        ivSend = view.findViewById(R.id.ivSend);
        mEmotionLayout = view.findViewById(R.id.emotion_layout);
        emptyView = view.findViewById(R.id.viewEmpty);
        initBottomMore();
        initListener();
        KeyBoardUtils.openKeybord(etComment, getContext());
        if (type == 2) {
            String str = String.format("回复 @%s:", FaceConversionUtil.getSmiledText(getContext(), model.nickname));
            etComment.setHint(str);
        } else {
            etComment.setHint("留下你的精彩评论吧");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attr = window.getAttributes();
        attr.gravity = Gravity.BOTTOM;
        attr.width = WindowManager.LayoutParams.MATCH_PARENT;
        attr.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attr);
    }

    public void setData(int parentIndex, int position, int type, CommentModel commentModel, String cardId, int parentId) {
        this.parentIndex = parentIndex;
        this.position = position;
        this.cardId = cardId;
        this.type = type;
        this.model = commentModel;
        this.parentId = parentId;
    }


    private void initListener() {
        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance();
        globalOnItemClickListener.attachToEditText(etComment, 400);
        ivStatus.setOnClickListener(v -> {
            if (ivStatus.isSelected()) {
                // 弹出输入法
                lockChatListHeight();
                hideEmotionLayout(true);
                unlockChatListHeightDelayed();
                ivStatus.setSelected(false);
            } else {
                // 弹出表情框
                if (isSoftInputShown()) {
                    lockChatListHeight();
                    showEmotionLayout();
                    unlockChatListHeightDelayed();
                } else {
                    showEmotionLayout();
                }
                ivStatus.setSelected(true);
            }
        });
        emptyView.setOnClickListener(v -> {
//            if (isSoftInputShown()) {
//                KeyBoardUtils.closeKeybord(etComment, getContext());
//            }
            KeyBoardUtils.closeKeybord(etComment, getContext());
            dismiss();
        });
        etComment.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP && mEmotionLayout.isShown()) {
                lockChatListHeight();
                hideEmotionLayout(true);
                etComment.postDelayed(this::unlockChatListHeightDelayed, 200L);
            } else {
                mEmotionLayout.getLayoutParams().height = 1;
                mEmotionLayout.setVisibility(View.INVISIBLE);
            }
            return false;
        });
        ivSend.setOnClickListener(v -> {
            // 发送消息
            String content = etComment.getText().toString();
            etComment.setText("");
            sendComment(content);
        });
    }

    private void initBottomMore() {
        mFragments = new ArrayList<>();
        ChatEmotionFragment chatEmotionFragment = new ChatEmotionFragment();
        mFragments.add(chatEmotionFragment);
        CommonFragmentPageAdapter mAdapter = new CommonFragmentPageAdapter(getChildFragmentManager(), mFragments);
        mVpBottomMore.setOffscreenPageLimit(1);//添加缓冲页数，避免被销毁
        mVpBottomMore.setAdapter(mAdapter);
        mVpBottomMore.setCurrentItem(0);
    }

    protected void hideEmotionLayout(boolean showSoftInput) {
        if (mEmotionLayout.isShown()) {
            mEmotionLayout.getLayoutParams().height = 1;
            mEmotionLayout.setVisibility(View.INVISIBLE);
            if (showSoftInput) KeyBoardUtils.openKeybord(etComment, getContext());
        }
    }

    protected boolean isSoftInputShown() {
        return KeyBoardUtils.getSupportSoftInputHeight(getActivity()) > 0;
    }

    protected void showEmotionLayout() {
        int softInputHeight = (int) SPUtils1.get(getContext(), Constant.PRE_KEYBOARD_HEIGHT,
                DensityUtils.dp2px(getContext(), 280));
//        int softInputHeight = KeyBoardUtils.getSupportSoftInputHeight(getActivity());
//        if (softInputHeight <= 0) {
//            softInputHeight = (int) SPUtils1.get(getContext(), Constant.PRE_KEYBOARD_HEIGHT,
//                    DensityUtils.dp2px(getContext(), 280));
//        }
        KeyBoardUtils.closeKeybord(etComment, getContext());
        mEmotionLayout.getLayoutParams().height = softInputHeight;
        mEmotionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        KeyBoardUtils.closeKeybord(etComment, getContext());
        GlobalOnItemClickManagerUtils.getInstance().attachToEditText(null, 400);
        super.onDismiss(dialog);
    }


    protected void lockChatListHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) emptyView.getLayoutParams();
        params.height = emptyView.getHeight();
        params.weight = 0.0F;
    }

    protected void unlockChatListHeightDelayed() {
        emptyView.postDelayed(() -> {
            ((LinearLayout.LayoutParams) emptyView.getLayoutParams()).weight = 1.0F;
            ((LinearLayout.LayoutParams) emptyView.getLayoutParams()).height = 0;
        }, 200L);
    }

    private void sendComment(String content) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cardId", Integer.parseInt(cardId));
        map.put("content", content);
        map.put("parentId", type == 1 ? 0 : parentId);
        map.put("secondType", type == 1 ? 0 : (parentId == model.commentId ? 1 : 2));
        map.put("secondUserId", type == 1 ? 0 : (parentId == model.commentId ? 0 : model.userId));
        map.put("type", type);
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        new RetrofitUtils().createApi(ChatService.class).comment(requestBody).compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<CommentModel>>() {
                    @Override
                    public void _onNext(HttpResult<CommentModel> data) {
                        if (data.isSuccess()) {
                            CommentRefreshEvent event = new CommentRefreshEvent(parentIndex, position, data.data);
                            event.cardId = cardId;
                            RxBus.get().post(event);
//                            if (isSoftInputShown())
                            KeyBoardUtils.closeKeybord(etComment, getContext());
                            dismiss();
                        } else
                            ToastUtils.showShort(getContext(), data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }
}