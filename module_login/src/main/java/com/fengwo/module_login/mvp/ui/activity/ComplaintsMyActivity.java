package com.fengwo.module_login.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.utils.RetrofitUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.emojiTextview.GifTextView;
import com.fengwo.module_login.R;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.ComplaintsDto;

import java.io.Serializable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.Flowable;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/16
 */
public class ComplaintsMyActivity extends BaseListActivity<ComplaintsDto> {

    private final static String PAGE_SIZE = ",10";

    public Handler mHandler;
    @Override
    protected void initView() {
        super.initView();
//        setTitleBackground(Color.WHITE);
        SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("我的投诉")
                .setTitleColor(R.color.text_33)
                .build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (p!=null) {
            p.getListData(1);
        }
    }

    @Override
    public Flowable setNetObservable() {
        String pageParams = page+ PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getMyComplaints(pageParams);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_complaints_list;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, ComplaintsDto item, int position) {
        GifTextView tvContent = helper.getView(R.id.tv_title);
        tvContent.setSpanText(mHandler,item.getContent(),false);
        helper.setText(R.id.tv_time, TimeUtils.dealInstanFormat(item.getCreateTime(),TimeUtils.DEFAULT_DATE_STRING_SLASH));
//        helper.setText(R.id.tv_time, item.getCreateTime()+"");
        TextView tvStatus = helper.getView(R.id.tv_status);
        tvStatus.setText(item.getStatus()==0?"处理中":"已处理");
        tvStatus.setTextColor(item.getStatus()==0?getResources().getColor(R.color.red_F46060):getResources().getColor(R.color.text_33));
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComplaintsMyActivity.this,ComplaintDetailActivity.class);
                intent.putExtra("detail", (Serializable) adapter.getData().get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_attention;
    }
}
