package com.fengwo.module_login.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.ReportLabelDto;
import com.fengwo.module_login.mvp.presenter.ReportPresenter;
import com.fengwo.module_login.mvp.ui.adapter.ComplaintAdapter;
import com.fengwo.module_login.mvp.ui.adapter.ReportLabelAdapter;
import com.fengwo.module_login.mvp.ui.iview.IReportView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
@Route(path = ArouterApi.REPORT_ACTIVITY)
public class ReportActivity extends BaseMvpActivity<IReportView, ReportPresenter> implements IReportView {
    private static final int IMG_REQUESTCODE = 10001;
    private static final int MAX = 3;
    @BindView(R2.id.edit_content)
    EditText editContent;
    @BindView(R2.id.tv_text_size)
    TextView tv_text_size;
    @BindView(R2.id.rv_label)
    RecyclerView rv_label;
    @BindView(R2.id.rv_pic)
    RecyclerView rv_pic;

    private List<String> picList = new ArrayList<>();
    private ComplaintAdapter picAdapter;
    private List<ReportLabelDto> labelList = new ArrayList<>();
    private ReportLabelAdapter labelAdapter;

    private List<String> uploadImaUrl = new ArrayList<>();

    private String beReportUserId;//被投诉用户id；

    private BlockingQueue queue = new ArrayBlockingQueue(3);

    @Override
    public ReportPresenter initPresenter() {
        return new ReportPresenter();
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("举报")
                .build();
        beReportUserId = getIntent().getStringExtra("id");
        initRvPic();
        initRvLabel();
        setEditListener();
        p.getLabel();
    }

    private void setEditListener() {
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_text_size.setText(s.length() + "/140");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRvLabel() {
        rv_label.setLayoutManager(new GridLayoutManager(this, 3));
        labelAdapter = new ReportLabelAdapter(labelList);
        rv_label.setAdapter(labelAdapter);
        labelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                labelAdapter.setCheck(position);
//                labelAdapter.getData().get(position).setCheck(!labelAdapter.getData().get(position).isCheck());
//                labelAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRvPic() {
        rv_pic.setLayoutManager(new GridLayoutManager(this, 3));
        picList.add("1");
        picAdapter = new ComplaintAdapter(picList);
        rv_pic.setAdapter(picAdapter);
        picAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                picList.remove(position);
                uploadImaUrl.remove(position - 1);
                picAdapter.notifyDataSetChanged();
            }
        });
        picAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    if (uploadImaUrl.size() == MAX) {
                        toastTip("最大上传" + MAX + "张");
                        return;
                    }
                    MImagePicker.openImagePicker(ReportActivity.this, MImagePicker.TYPE_IMG, IMG_REQUESTCODE);
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_report;
    }


    @OnClick(R2.id.tv_commit)
    public void onViewClicked(View view) {
        commit();
    }

    private void commit() {
        String content = editContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            toastTip("请输入举报内容");
            return;
        }
        if (TextUtils.isEmpty(getLabelId())) {
            toastTip("请选择标签");
            return;
        }
        if (TextUtils.isEmpty(getImageUrl())) {
            toastTip("请上传图片");
            return;
        }
        Map params = new HashMap();
        params.put("content", content);
        params.put("image", getImageUrl());
        params.put("reportId", getLabelId());
        params.put("targetId", beReportUserId);
        p.reportCommit(params);
    }

    private String getLabelId() {
        String labelId = "";
        try {
            labelId = labelAdapter.getCheckItem().getId() + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return labelId;
//        StringBuilder stringBuilder = new StringBuilder();
//        for (ReportLabelDto reportLabelDto : labelAdapter.getData()) {
//            if (reportLabelDto.isCheck())
//                stringBuilder.append(reportLabelDto.getId() + ";");
//        }
//        return stringBuilder.toString();
    }

    private String getImageUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String url : uploadImaUrl) {
            stringBuilder.append(url + ";");
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMG_REQUESTCODE) {
//                showLoadingDialog();

                uploadImgList(MImagePicker.getImagePath(this, data));
            }
        }
    }

    private void upLoad(String url) {
        UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_IMAGE, new File(url), new UploadHelper.OnUploadListener() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onLoading(long cur, long total) {

            }

            @Override
            public void onSuccess(String url) {
                hideLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        picList.add(url);
                        picAdapter.notifyDataSetChanged();
                    }
                });
                uploadImaUrl.add(url);
                L.e("okh:" + url);
                if (queue.size() != 0) {
                    try {
                        upLoad((String) queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError() {
                hideLoadingDialog();
                toastTip("图片上传失败，请重新上传");
            }
        });
    }

    private void uploadImgList(String imagePathList) {
        queue.add(imagePathList);
        try {
            upLoad((String) queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnLabel(BaseListDto<ReportLabelDto> listDto) {
        labelList.addAll(listDto.records);
        labelAdapter.notifyDataSetChanged();
    }

    @Override
    public void returnCommit(HttpResult httpResult) {
        toastTip(httpResult.description);
        if (httpResult.isSuccess()) {
            finish();
        }
    }
}
