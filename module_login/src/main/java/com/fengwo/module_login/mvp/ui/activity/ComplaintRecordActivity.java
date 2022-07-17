package com.fengwo.module_login.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.ComplaintReplyService;
import com.fengwo.module_comment.utils.EPSoftKeyBoardListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.emojiTextview.GifTextView;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.ComplaintRecordDto;
import com.google.gson.Gson;
import com.umeng.socialize.editorpage.IEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/7
 */
public class ComplaintRecordActivity extends BaseComplaintListActivty<ComplaintRecordDto> {

    private int id;

    @BindView(R2.id.et_input)
    EditText etInput;
    @BindView(R2.id.btn_send)
    TextView btnSend;

    @Autowired
    ComplaintReplyService service;

    private View layoutInput;
    private Handler mHandler;
    private int type = 0;//最后一条的回复状态

    @Override
    protected void initView() {
        id = getIntent().getIntExtra("id",0);
        super.initView();
//        setTitleBackground(Color.WHITE);
        SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("处理记录")
                .setTitleColor(R.color.text_33)
                .build();

        mHandler = new Handler(Looper.getMainLooper());
        layoutInput = findViewById(R.id.layout_reply_input);
        setSoftListener();
    }

    private void setSoftListener() {
        EPSoftKeyBoardListener.setListener(this, new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) layoutInput.getLayoutParams();
                params.bottomMargin = height;
                layoutInput.setVisibility(View.VISIBLE);
                layoutInput.setLayoutParams(params);
            }

            @Override
            public void keyBoardHide(int height) {
                ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) layoutInput.getLayoutParams();
                layoutInput.setVisibility(View.GONE);
                params.bottomMargin = 0;
            }
        });
    }
    @Override
    public Flowable setNetObservable() {
        String pageParams = page+ PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getComPlaintRecord(id,pageParams);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_complaint_record;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, ComplaintRecordDto item, int position) {
        GifTextView tvContent = helper.getView(R.id.tv_content);
        GifTextView tvReplyContent = helper.getView(R.id.tv_reply_content);
        helper.setText(R.id.tv_nick,item.getNickname());
        tvContent.setSpanText(mHandler,item.getContent(),false);
        helper.setText(R.id.tv_time, item.getCreateTime());
        if (item.getComplaintReplyId()>0){
            helper.getView(R.id.ll_reply).setVisibility(View.VISIBLE);
            tvReplyContent.setSpanText(mHandler,item.getReplyContent(),false);
            helper.setText(R.id.tv_reply_nick,"回复   "+item.getReplyNickname());
        }else {
            helper.getView(R.id.ll_reply).setVisibility(View.GONE);
        }
        ImageLoader.loadImgFitCenter(helper.getView(R.id.iv_head), item.getHeadImg());

        if (position == adapter.getItemCount()-1&& item.getType() == 1){ //是平台回复且是最后一条的时候显示回复按钮
            type = 1;
            helper.getView(R.id.tv_reply).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_reply).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSendMsgEdit("");
                }
            });
        }else {
            helper.getView(R.id.tv_reply).setVisibility(View.GONE);
        }
    }

    @Override
    protected List<ComplaintRecordDto> transFrom(List<ComplaintRecordDto> datas) {
        for (ComplaintRecordDto dto : datas){
            if (dto.getComplaintReplyId()>0){
                for (ComplaintRecordDto sdto : datas){
                    if (dto.getComplaintReplyId() == sdto.getId()){
                        dto.setReplyContent(sdto.getContent());
                        dto.setReplyNickname(sdto.getNickname());
                        break;
                    }
                }

            }
        }
        return datas;
    }

    @Override
    protected int getContentView() {
        return R.layout.login_base_list;
    }

    private void showSendMsgEdit(String msg) {
        layoutInput.setVisibility(View.VISIBLE);
        etInput.setText(msg);
        etInput.requestFocus();
        etInput.setSelection(msg.length());
        KeyBoardUtils.openKeybord(etInput, this);
    }
    public RequestBody createRequestBody(Map map) {
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }

    @OnClick({R2.id.btn_send})
    public void onViewClicked(View v) {
        if (v.getId() == R.id.btn_send) {
            String content = etInput.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return;
            }
            Map map = new HashMap();
            map.put("complaintId",id);
            map.put("content",content);
            service.complaintReply(createRequestBody(map), new LoadingObserver<HttpResult>() {
                @Override
                public void _onNext(HttpResult data) {
                    if (data.isSuccess()){
                        KeyBoardUtils.closeKeybord(etInput, ComplaintRecordActivity.this);
                        etInput.setText("");
//                        adapter.getData().get(adapter.getItemCount()-1).setType(0);
//                        adapter.notifyItemChanged(adapter.getItemCount()-1);
                        toastTip("反馈成功，请查看处理记录");
                        type = 0;
                        p.getListData(1);
                    }else {
                        toastTip(data.description);
                    }
                }

                @Override
                public void _onError(String msg) {
                    toastTip(msg);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("type",type);
        setResult(RESULT_OK,intent);
        finish();
    }
}
