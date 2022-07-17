/*
 * i????
 *
 * ???????
 *
 * */
package com.fengwo.module_flirt.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.CommentWordDto;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;

public class ShortcutView extends BaseChatRoomDialog {
    @BindView(R2.id.ci_shortcut_header)
    ImageView mIvShortcutHeader;
    @BindView(R2.id.tv_shortcut_nick_name)
    TextView mTvShortcutNickName;
    @BindView(R2.id.tv_shortcut_sign)
    TextView mTvShortcutSign;
    @BindView(R2.id.tv_shortcut_content)
    TextView mTvShortcutContent;
    @BindView(R2.id.rv_shortcut_msg)
    RecyclerView mRvShortcutMsg;


    private ShortcutAdapter mAdapter;

    private int current = 1;//???

    @OnClick({R2.id.bt_send_chat_room, R2.id.iv_shortcut_close})
    public void onViewClick(View v) {
        //?????
        if (v.getId() == R.id.bt_send_chat_room) {
            if (mListener != null) {
                if (!mListener.onChangeChatUser()) {
                    WenboWsChatDataBean.FromUserBean fb = mChatData.data.getFromUser();
                    WenboWsChatDataBean.RoomBean mRoom = mChatData.data.getRoom();
                    if (!mRoom.getRoomId().equals("0")) {
                        quitRoom(mChatData.data.getRoom().getRoomId());
                    }
                    checkAnchorStatus(fb.getUserId(), fb.getHeadImg());
                } else {
                    dismiss();
                }
            } else {
                WenboWsChatDataBean.FromUserBean fb = mChatData.data.getFromUser();
                WenboWsChatDataBean.RoomBean mRoom = mChatData.data.getRoom();
                if (!mRoom.getRoomId().equals("0")) {
                    quitRoom(mChatData.data.getRoom().getRoomId());
                }
                checkAnchorStatus(fb.getUserId(), fb.getHeadImg());
            }
        }
        //????
        else if (v.getId() == R.id.iv_shortcut_close) {
            dismiss();
        }
    }

    public ShortcutView(Context context, SocketRequest<WenboWsChatDataBean> chatData) {
        super(context, chatData);
        this.mChatData = chatData;
        initView();
        requestData();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_shortcut);
    }


    @SuppressLint("SetTextI18n")
    private void initView() {
        setPopupGravity(Gravity.CENTER);
        mAdapter = new ShortcutAdapter();
        mRvShortcutMsg.setAdapter(mAdapter);
        mRvShortcutMsg.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        mRvShortcutMsg.addItemDecoration(new StaggeredDecoration(getContext(), 5));
        //????item??
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (null != mListener) {
                mListener.onItemClick((CommentWordDto) adapter.getData().get(position));
                dismiss();
            }
        });
        setTip();
        ImageLoader.loadImg(mIvShortcutHeader, mChatData.data.getFromUser().getHeadImg());
        mTvShortcutNickName.setText(mChatData.data.getFromUser().getNickname() + "");
        mTvShortcutContent.setText(mChatData.data.getContent().getValue() + "");
    }

    private void setTip(){
        if (null != mChatData.data.getGears()&&mChatData.data.getIsGears()!=null&&mChatData.data.getIsGears().equals("1")) {
            mTvShortcutSign.setVisibility(View.VISIBLE);
        switch (mChatData.data.getGears()) {
            case "0":
                mTvShortcutSign.setBackgroundResource(R.drawable.bg_text_sign2);
                mTvShortcutNickName.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_63A5FF));
                mTvShortcutSign.setText(getContext().getString(R.string.char_open_fate));
                break;
            case "2":
                mTvShortcutNickName.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_4DC6E0));
                mTvShortcutSign.setBackgroundResource(R.drawable.bg_text_sign3);
                mTvShortcutSign.setText(getContext().getString(R.string.char_susan_slade));
                break;
            case "1":
                mTvShortcutNickName.setTextColor(ContextCompat.getColor(getContext(), R.color.color_FFC147));
                mTvShortcutSign.setBackgroundResource(R.drawable.bg_text_sign1);
                mTvShortcutSign.setText(getContext().getString(R.string.char_once_again));
                break;
        }
        }else {
            mTvShortcutSign.setVisibility(View.GONE);
        }
    }

    private static class ShortcutAdapter extends BaseQuickAdapter<CommentWordDto, BaseViewHolder> {

        public ShortcutAdapter() {
            super(R.layout.shortcut_item, null);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, CommentWordDto item) {
            helper.setText(R.id.tv_shortcut_item, item.getTitle());
        }

    }

    private void requestData() {
        RequestBody build = new WenboParamsBuilder()
                .put("current", String.valueOf(current))
                .put("size", "20")
                .put("userPort", "1")
                .build();
        new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .getCommentWord(build)
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<CommentWordDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<CommentWordDto>> data) {
                        if (data.isSuccess()) {
                            if (current == 1) {
                                mAdapter.setNewData(data.data.records);
                            } else {
                                mAdapter.addData(data.data.records);
                            }
                        } else {
                            ToastUtils.showShort(getContext(), data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                });
    }



    private ShortcutItemClickListener mListener;
    public void setOnItemClickListener(ShortcutItemClickListener mListener){
        this.mListener = mListener;
    }

    public interface ShortcutItemClickListener{
        void onItemClick(CommentWordDto data);

        boolean onChangeChatUser();
    }
}
