package com.fengwo.module_login.mvp.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.utils.RetrofitUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.StringFormatUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.ProfitDetailDto;
import com.fengwo.module_login.utils.UserManager;

import java.util.Date;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import io.reactivex.Flowable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/18
 */
public class ProfitDetailActivity extends BaseListActivity<ProfitDetailDto> {

    @BindView(R2.id.head_view)
    FrameLayout head_view;

    private final static String PAGE_SIZE = ",20";

    private View headView;
    TimePickerView timePickerView;
    TextView tvStartTime;
    TextView tvEndTime;
    private String start = TimeUtils.dateToString(TimeUtils.getMonthFirstDay(), TimeUtils.YYYY_MM_DD), end = TimeUtils.getCurrentDateInString();

    @Override
    protected void initView() {
        super.initView();
        SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitleColor(R.color.text_33)
                .setTitle("收益明细")
                .build();
        initHeadView();
        tvStartTime.setText(TimeUtils.dateToString(TimeUtils.getMonthFirstDay(), TimeUtils.YYYY_MM_DD));
        tvEndTime.setText(TimeUtils.getCurrentDateInString());
    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.profit_detail_head_view, null);
        TextView tvHoneyValue = headView.findViewById(R.id.tv_honey_value);
        tvHoneyValue.setText(StringFormatUtils.formatDouble(UserManager.getInstance().getUser().profit));
        tvStartTime = headView.findViewById(R.id.item_tv_starttime);
        tvEndTime = headView.findViewById(R.id.item_tv_endtime);
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(tvStartTime);
            }
        });
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(tvEndTime);
            }
        });
        head_view.addView(headView);
    }

    private void timePicker(TextView textView) {
        timePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String str = TimeUtils.dateToString(date, TimeUtils.YYYY_MM_DD);
                textView.setText(str);
                start = tvStartTime.getText().toString();
                end = tvEndTime.getText().toString();
                if (1 != TimeUtils.timeCompare(start, end, TimeUtils.YYYY_MM_DD)) {
                    netObservable();
                } else {
                    toastTip("结束时间必须大于等于开始时间");
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(false) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();
        timePickerView.show();
    }

    private void netObservable() {
        p.getListData(1);
    }

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getProfitDetail(p, start, end);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_profit_detail;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, ProfitDetailDto item, int position) {
        helper.setText(R.id.tv_name, item.getOtherNickname() + "  (" + item.getOriginType() + ")");
        helper.setText(R.id.tv_date, TimeUtils.dealDateFormatToRecord(item.getUpdateTime()));
        if (item.getType() >= 200) { //减少
            helper.setText(R.id.tv_value, "-" + item.getMoney());
        } else {
            helper.setText(R.id.tv_value, "+" + item.getMoney());
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_profit_detail;
    }
}
