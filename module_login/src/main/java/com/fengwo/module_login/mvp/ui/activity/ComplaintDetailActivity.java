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
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.ComplaintReplyService;
import com.fengwo.module_comment.utils.EPSoftKeyBoardListener;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.emojiTextview.GifTextView;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.ComplaintsDto;
import com.fengwo.module_login.mvp.ui.adapter.ComplaintDetailAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/7
 */
public class ComplaintDetailActivity extends BaseMvpActivity {

    private static final int START_INTENT = 1232;

    @BindView(R2.id.tv_status)
    TextView tvStatus;
    @BindView(R2.id.tv_deal_content)
    GifTextView tvDealContent;
    @BindView(R2.id.tv_reply)
    TextView tvReply;
    @BindView(R2.id.tv_complaint_content)
    GifTextView tvComplaintContent;
    @BindView(R2.id.rv_image)
    RecyclerView rvImage;
    @BindView(R2.id.tv_complaint_time)
    TextView tvComplaintTime;
    @BindView(R2.id.et_input)
    EditText etInput;
    @BindView(R2.id.btn_send)
    TextView btnSend;

    @Autowired
    ComplaintReplyService service;

    private View layoutInput;
    private ComplaintsDto complaintsDto;
    private List<String> mListData = new ArrayList<>();
    private ComplaintDetailAdapter mAdapter;

    private Handler mHandler;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("投诉详情")
                .build();
        mHandler = new Handler(Looper.getMainLooper());
        complaintsDto = (ComplaintsDto) getIntent().getSerializableExtra("detail");
        layoutInput = findViewById(R.id.layout_reply_input);
        setSoftListener();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvImage.setLayoutManager(linearLayoutManager);
        setData();

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

    private void setData() {
        if (complaintsDto.getStatus() == 0) {
            tvStatus.setText("处理中");
            tvStatus.setTextColor(getResources().getColor(R.color.red_F46060));
            tvDealContent.setText("您的回复信息已提交，客服将在1-3个工作日内处理。");
            tvReply.setVisibility(View.GONE);
        } else {
            tvStatus.setText("已处理");
            tvStatus.setTextColor(getResources().getColor(R.color.text_33));
            tvDealContent.setSpanText(mHandler,complaintsDto.getReplyContent(),false);
            tvReply.setVisibility(View.VISIBLE);
        }
        tvComplaintContent.setSpanText(mHandler,complaintsDto.getContent(),false);
        tvComplaintTime.setText("投诉时间：" + TimeUtils.dealInstanFormat(complaintsDto.getCreateTime(), TimeUtils.DEFAULT_DATE_STRING_SLASH));

        getImageList();
        mAdapter = new ComplaintDetailAdapter(mListData);
        rvImage.setAdapter(mAdapter);
    }

    private void getImageList() {
        mListData.clear();
        String[] split = complaintsDto.getImage().split(",");
        for (String image : split) {
            mListData.add(image);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_complaint_detail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            int type = data.getIntExtra("type",0);
            complaintsDto.setStatus(type);
            setData();
        }
    }

    @OnClick({R2.id.menu_record,R2.id.tv_reply,R2.id.btn_send})
    public void onViewClicked(View v) {
        if (v.getId() == R.id.menu_record) {
            if (complaintsDto.getStatus() == 0) {
                ToastUtils.showShort(ComplaintDetailActivity.this,"投诉正在处理中");
                return;
            }
            Intent intent = new Intent(this, ComplaintRecordActivity.class);
            intent.putExtra("id", complaintsDto.getId());
            startActivityForResult(intent,START_INTENT);
        }else if (v.getId() == R.id.tv_reply){
            showSendMsgEdit("");
        }else if (v.getId() == R.id.btn_send){
            String content = etInput.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return;
            }
            Map map = new HashMap();
            map.put("complaintId",complaintsDto.getId());
            map.put("content",content);
            service.complaintReply(createRequestBody(map), new LoadingObserver<HttpResult>() {
                @Override
                public void _onNext(HttpResult data) {
                    if (data.isSuccess()){
                        KeyBoardUtils.closeKeybord(etInput, ComplaintDetailActivity.this);
                        etInput.setText("");
                        toastTip("反馈成功，请查看处理记录");
                        complaintsDto.setStatus(0);
                        setData();
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
}
