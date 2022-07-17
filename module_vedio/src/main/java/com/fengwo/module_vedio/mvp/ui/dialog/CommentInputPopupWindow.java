package com.fengwo.module_vedio.mvp.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Toast;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.eventbus.ShowCommentInputEvent;
import com.fengwo.module_vedio.api.VedioApiService;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import razerdp.basepopup.BasePopupWindow;

public class CommentInputPopupWindow extends BasePopupWindow {

    private Context mContext;
    private EditText et;
    private View btnSend;
    ShowCommentInputEvent event;

    public CommentInputPopupWindow(Context context) {
        super(context);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setPopupGravity(Gravity.BOTTOM);
        mContext = context;
        et = findViewById(R.id.et);
        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(v -> {
            String content = et.getText().toString().trim();
            int type = event.commentId > 0 ? 1 : 0;
            addComment(event.videoId, content, type, event.commentId);
        });
    }

    /**
     * @param vedioId
     * @param content
     * @param type    0-1级评论，1-2级评论
     */
    private void addComment(int vedioId, String content, int type, int parentId) {
        Map<String, String> params = new HashMap<>();
        params.put("videoId", vedioId + "");
        params.put("content", content);
        params.put("type", type + "");
        if (parentId > 0) {
            params.put("commentId", parentId + "");
        }
        new RetrofitUtils().createApi(VedioApiService.class)
                .addSmallComment(HttpUtils.createRequestBody(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            Toast.makeText(mContext, "评论成功~~~~~", Toast.LENGTH_SHORT).show();
                            et.setText("");
                            dismiss();
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();
        et.requestFocus();
        KeyBoardUtils.openKeybord(et, mContext);
    }

    @Override
    public void dismiss() {
        KeyBoardUtils.closeKeybord(et, mContext);
        super.dismiss();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.vedio_popwindow_inputcomment);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    public void setReplyInfo(ShowCommentInputEvent showCommentInputEvent) {
        event = showCommentInputEvent;
        if (!TextUtils.isEmpty(showCommentInputEvent.userName))
            et.setHint("@" + showCommentInputEvent.userName);

    }
}
