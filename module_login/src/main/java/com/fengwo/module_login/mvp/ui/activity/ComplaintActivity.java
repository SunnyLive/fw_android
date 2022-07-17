package com.fengwo.module_login.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.presenter.ComplaintPresenter;
import com.fengwo.module_login.mvp.ui.adapter.ComplaintAdapter;
import com.fengwo.module_login.mvp.ui.iview.IComplaintView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
public class ComplaintActivity extends BaseMvpActivity<IComplaintView, ComplaintPresenter> implements IComplaintView {
    private static final int IMG_REQUESTCODE = 10001;
    private static final int MAX = 3;
    public static final String BECOMPLAINTID = "be_complaint_id";
    public static final String COMPLAINTTYPE = "complaint_type";
    @BindView(R2.id.edit_content)
    EditText editContent;
    @BindView(R2.id.edit_phone)
    EditText editPhone;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;

    private List<String> mListData = new ArrayList<>();
    private ComplaintAdapter mAdapter;

    private List<String> uploadImaUrl = new ArrayList<>();

    private int beComplaintUserId;//被投诉用户id；
    private int complaintType;//举报类型 （投诉类型：1用户，2其他）

    private BlockingQueue queue = new ArrayBlockingQueue(3);

    @Override
    public ComplaintPresenter initPresenter() {
        return new ComplaintPresenter();
    }

    public static void start(Context context,int userId, int type){
        Intent intent = new Intent(context,ComplaintActivity.class);
        intent.putExtra(BECOMPLAINTID,userId);
        intent.putExtra(COMPLAINTTYPE,type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("投诉/建议")
                .build();
        beComplaintUserId = getIntent().getIntExtra(BECOMPLAINTID, -1);
        complaintType = getIntent().getIntExtra(COMPLAINTTYPE, -1);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        mListData.add("1");
        mAdapter = new ComplaintAdapter(mListData);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mListData.remove(position);
                if(uploadImaUrl.size()>1){
                    uploadImaUrl.remove(position - 1);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    if (uploadImaUrl.size() == MAX) {
                        toastTip("最大上传" + MAX + "张");
                        return;
                    }
                    MImagePicker.openImagePicker(ComplaintActivity.this, MImagePicker.TYPE_IMG, IMG_REQUESTCODE);
                }
            }
        });
        initScrollHandler();
    }

    /**
     * 解决NestedScrollView 与 EditText 滑动冲突问题
     */
    private void initScrollHandler() {
        editContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //canScrollVertically()方法为判断指定方向上是否可以滚动,参数为正数或负数,负数检查向上是否可以滚动,正数为检查向下是否可以滚动
                if (editContent.canScrollVertically(1) || editContent.canScrollVertically(-1)) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);//requestDisallowInterceptTouchEvent();要求父类布局不在拦截触摸事件
                    if (event.getAction() == MotionEvent.ACTION_UP) { //判断是否松开
                        v.getParent().requestDisallowInterceptTouchEvent(false); //requestDisallowInterceptTouchEvent();让父类布局继续拦截触摸事件
                    }
                }
                return false;
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_complaint;
    }


    @OnClick({R2.id.tv_commit, R2.id.tv_my_complaint})
    public void onViewClicked(View view) {
        if (isFastClick()) return;
        int id = view.getId();
        if (id == R.id.tv_commit) {
            commit();
        } else if (id == R.id.tv_my_complaint) {
            startActivity(ComplaintsMyActivity.class);
        }
    }

    private void commit() {
        String content = editContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            toastTip("请输入投诉内容");
            return;
        }
        String phone = editPhone.getText().toString().trim();
//        if (TextUtils.isEmpty(phone)) {
//            toastTip("请输入联系方式");
//            return;
//        }
        Map params = new HashMap();
        params.put("content", content);
        params.put("image", getImageUrl());
        params.put("complaint", beComplaintUserId + "");
        params.put("complaintType", complaintType + "");
        params.put("type", 0);
        params.put("contactInfo", phone);
        p.complaintCommit(params);
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
                String url = MImagePicker.getImagePath(this, data);
                if (!TextUtils.isEmpty(url)) {
                    mListData.add(url);
                    mAdapter.notifyDataSetChanged();
                    uploadImg(url);
                }
            }
        }
    }

    private void uploadImg(String imagePathList) {
        queue.add(imagePathList);
        try {
            upLoad((String) queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void upLoad(String url) {
        UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_IMAGE, new File(url), new UploadHelper.OnUploadListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onLoading(long cur, long total) {

            }

            @Override
            public void onSuccess(String url) {
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
                toastTip("图片上传失败，请重新上传");
            }
        });
    }

    @Override
    public void returnCommit(HttpResult httpResult) {
        toastTip(httpResult.description);
        if (httpResult.isSuccess()) {
            startActivity(ComplaintsMyActivity.class);
            finish();
        }
    }
}
