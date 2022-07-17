package com.fengwo.module_login.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.LiveLengthDto;
import com.fengwo.module_login.mvp.presenter.LiveTimePresenter;
import com.fengwo.module_login.mvp.ui.adapter.LivingTimeAdapter;
import com.fengwo.module_login.mvp.ui.iview.ILiveTimeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

public class LivingTimeActivity extends BaseMvpActivity<ILiveTimeView, LiveTimePresenter> implements ILiveTimeView {

    @BindView(R2.id.item_tv_starttime)
    TextView itemTvStarttime;
    @BindView(R2.id.item_tv_endtime)
    TextView itemTvEndtime;
    @BindView(R2.id.item_tv_alltime)
    TextView itemTvAlltime;
    @BindView(R2.id.item_tv_devices)
    TextView itemTvDevices;
    private LivingTimeAdapter livingTimeAdapter;
    @BindView(R2.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_starttime)
    TextView tvStartTime;
    @BindView(R2.id.tv_endtime)
    TextView tvEndTime;
    @BindView(R2.id.tv_live_length)
    TextView tvLiveLength;
    @BindView(R2.id.tv_reset)
    TextView tv_reset;
    @BindView(R2.id.tv_sure)
    TextView tv_sure;
    @BindView(R2.id.tvContentStr)
    TextView tvContentStr;
    //    private LiveLengthDto mLiveLengthDto;
    private List<LiveLengthDto.RecordsBean> mListData = new ArrayList<>();

    TimePickerView timePickerView;
    private String pageParam = ",20";
    private int page = 1;
    private int type = 1;// 1:直播时长，2：视频时长
    private String start = TimeUtils.getCurrentYear() + "/" + TimeUtils.getCurrentMonth() + "/01";
    private String end = TimeUtils.getCurrentYear() + "/" + TimeUtils.getCurrentMonth() + "/" + TimeUtils.getCurrentDay();

    public static void getInstance(Context context, int type) {
        Intent intent = new Intent(context, LivingTimeActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public LiveTimePresenter initPresenter() {
        return new LiveTimePresenter();
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type", 1);
        new ToolBarBuilder().setTitle(type == 1 ? "直播时长" : "i撩记录").setTitleColor(Color.parseColor("#ffffff"))
                .setBackIcon(R.drawable.icon_back).showBack(true).build();
//        setTitleBackground(getResources().getColor(R.color.white));
        tvContentStr.setText(type == 1 ? "累计直播时长" : "累计视频时长");
        if (type == 2) {
            itemTvStarttime.setText("视频对象");
            itemTvEndtime.setText("开始时间");
            itemTvAlltime.setText("结束时间");
            itemTvDevices.setText("视频时长");
        }
        livingTimeAdapter = new LivingTimeAdapter(this, R.layout.login_item_livingtime_adapter, mListData, type);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(livingTimeAdapter);
        livingTimeAdapter.setOnLoadMoreListener(() -> {
            page++;
            getData();
        }, recyclerView);
        View v = LayoutInflater.from(this).inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
        livingTimeAdapter.setEmptyView(v);

        tvStartTime.setText(start);
        tvEndTime.setText(end);
        tv_reset.setSelected(true);

        getData();
    }


    private void getData() {
        String instantStart = TimeUtils.stringToInstant(start + " 00:00:00");
        String instantEnd = TimeUtils.stringToInstant(end + " 23:59:59");
        Map map = new HashMap();
        map.put("startTime", instantStart);
        map.put("endTime", instantEnd);
        if (type == 1) {
            p.getLiveTime(page + pageParam, map);
        } else {
            p.getVideoTime(instantStart, instantEnd, page + pageParam);
            p.getVideoLength();
        }
    }

    @OnClick({R2.id.tv_starttime, R2.id.tv_endtime, R2.id.tv_reset, R2.id.tv_sure})
    void onClick(View v) {
        if (v.getId() == R.id.tv_starttime) {
            timePicker(tvStartTime);
        } else if (v.getId() == R.id.tv_endtime) {
            timePicker(tvEndTime);
        } else if (v.getId() == R.id.tv_reset) {
            tv_reset.setSelected(true);
            tv_sure.setSelected(false);
            resetData();
        } else if (v.getId() == R.id.tv_sure) {
            tv_reset.setSelected(false);
            tv_sure.setSelected(true);
            start = tvStartTime.getText().toString();
            end = tvEndTime.getText().toString();
            if (1 != TimeUtils.timeCompare(start, end, TimeUtils.YYYY_MM_DD_SLASH)) {
                sure();
            } else {
                toastTip("结束时间必须大于开始时间");
            }
        }
    }

    private void resetData() {
        page = 1;
        start = TimeUtils.getCurrentYear() + "/" + TimeUtils.getCurrentMonth() + "/01";
        end = TimeUtils.getCurrentYear() + "/" + TimeUtils.getCurrentMonth() + "/" + TimeUtils.getCurrentDay();

        tvStartTime.setText(start);
        tvEndTime.setText(end);
        getData();
    }

    private void sure() {
        page = 1;
        getData();
    }

    private void timePicker(TextView textView) {
        timePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String str = TimeUtils.dateToString(date, TimeUtils.YYYY_MM_DD_SLASH);
                textView.setText(str);
                tv_reset.setSelected(false);
                tv_sure.setSelected(true);
                start = tvStartTime.getText().toString();
                end = tvEndTime.getText().toString();
                if (1 != TimeUtils.timeCompare(start, end, TimeUtils.YYYY_MM_DD_SLASH)) {
                    sure();
                } else {
                    toastTip("结束时间必须大于开始时间");
                }

            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(false) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();
        timePickerView.show();
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_livingtime;
    }

    @Override
    public void returnLiveTimeDate(LiveLengthDto liveLengthDto) {
        if (type == 1) {
            tvLiveLength.setText(liveLengthDto.getChannelTimes());
        }
        if (liveLengthDto.getRecords() == null || liveLengthDto.getRecords().size() == 0) {
            livingTimeAdapter.loadMoreEnd();
        } else {
            livingTimeAdapter.loadMoreComplete();
        }
        if (page == 1) {
            mListData.clear();
            livingTimeAdapter.setNewData(liveLengthDto.getRecords());
        } else {
            livingTimeAdapter.addData(liveLengthDto.getRecords());
        }
    }

    @Override
    public void returnAllTime(HttpResult httpResult) {
        if (httpResult.isSuccess()) {
            tvLiveLength.setText((String) httpResult.data);
        }
    }

}
