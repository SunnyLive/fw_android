package com.fengwo.module_flirt.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.adapter.LabelAdapter;
import com.fengwo.module_flirt.bean.CerTagBean;

import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/3/26 15:41
 */
public class FlirtIndicatorView extends LinearLayout implements View.OnClickListener {
    private Context context;
    private TextView tvVideo, tvNear, tvAppointment, tvLocation,tv_flirt_select;
    private LinearLayout llSearch;
    private View line;
    private int startPos;
    private ViewPager vpFlirt;
    private RecyclerView rvLabel;
    private LabelAdapter labelAdapter;
    private LinearLayout llRv;

    public FlirtIndicatorView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FlirtIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public FlirtIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        LayoutInflater.from(context).inflate(R.layout.layout_indicator, this, true);
        tvVideo = findViewById(R.id.tv_video);
        tvNear = findViewById(R.id.tv_near);
        line = findViewById(R.id.view_line);
        llSearch = findViewById(R.id.ll_search);
        tvLocation = findViewById(R.id.tv_location);
        tv_flirt_select = findViewById(R.id.tv_flirt_select);
        llRv = findViewById(R.id.ll_rv);
        tvAppointment = findViewById(R.id.tv_appointment);
//        llSearchNearby = findViewById(R.id.ll_search_nearby);

        tvVideo.setOnClickListener(this);
        tvNear.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        tv_flirt_select.setOnClickListener(this);
        tvAppointment.setOnClickListener(this);
//        llSearchNearby.setOnClickListener(this);

        initRv();

        checkText(0);
    }

    private void initRv() {
        rvLabel = findViewById(R.id.rv_label);
        labelAdapter = new LabelAdapter();
        rvLabel.setAdapter(labelAdapter);
        rvLabel.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        labelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                labelAdapter.setCheck(i);
                if (onClickListener != null) onClickListener.clickLabel(baseQuickAdapter, view, i);
            }
        });

    }

    /**
     * 设置标签数据
     *
     * @param datas
     */
    public void setLabelData(List<CerTagBean> datas) {
        labelAdapter.setNewData(datas);
    }

    public void setLabelIndex(int index) {
        labelAdapter.setCheck(index);
    }

    /**
     * 选中item UI 更新
     *
     * @param pos
     */
    public void checkText(int pos) {
        tvVideo.setSelected(pos == 0 ? true : false);
        tvAppointment.setSelected(pos == 1 ? true : false);
        tvNear.setSelected(pos == 2 ? true : false);
        tvVideo.setTextColor(pos == 0 ? ContextCompat.getColor(getContext(), R.color.purple_9966ff) : ContextCompat.getColor(getContext(), R.color.text_33));
        tvAppointment.setTextColor(pos == 1 ? ContextCompat.getColor(getContext(), R.color.purple_9966ff) : ContextCompat.getColor(getContext(), R.color.text_33));
        tvNear.setTextColor(pos == 2 ? ContextCompat.getColor(getContext(), R.color.purple_9966ff) : ContextCompat.getColor(getContext(), R.color.text_33));

    }

    /**
     * 设置位置
     */
    public void setLocation(String location) {
        tvLocation.setText(location);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_video) {//点击视频
            checkText(0);
            if (vpFlirt != null) vpFlirt.setCurrentItem(0);
        } else if (id == R.id.tv_near) {//点击附近
            checkText(2);
            if (vpFlirt != null) vpFlirt.setCurrentItem(2);
        } else if (id == R.id.tv_appointment) {//点击约聊
            checkText(1);
            if (vpFlirt != null) vpFlirt.setCurrentItem(1);
        } else if (id == R.id.ll_search) {//点击选择地址
            if (onClickListener != null) onClickListener.selectAddress();
        }else if (id == R.id.tv_flirt_select) {//点击搜索附近的人
            if (onClickListener != null) onClickListener.clickSelect();
        }
    }

    /**
     * 绑定viewpager
     *
     * @param vpFlirt
     */
    public void bindViewpage(ViewPager vpFlirt) {
        this.vpFlirt = vpFlirt;
        LayoutParams layoutParams = (LayoutParams) line.getLayoutParams();
        startPos = layoutParams.leftMargin;
        vpFlirt.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    layoutParams.leftMargin = (int) (startPos + DensityUtils.dp2px(getContext(), 80) * positionOffset);
                } else if (position == 1) {
                    layoutParams.leftMargin = (int) (startPos + DensityUtils.dp2px(getContext(), 80) + DensityUtils.dp2px(getContext(), 80) * positionOffset);
                }
                line.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                checkText(position);
                if (position == 2) {//附近的人
//                    llSearchNearby.setVisibility(VISIBLE);
                    llSearch.setVisibility(VISIBLE);
                    llRv.setVisibility(GONE);
                    if (onClickListener != null) onClickListener.clickFind();
                } else if (position == 0) {//视频交友
//                    llSearchNearby.setVisibility(GONE);
                    llSearch.setVisibility(GONE);
                    llRv.setVisibility(VISIBLE);
                    if (onClickListener != null) onClickListener.clickVideoSelect();
                } else if (position == 1) {//约聊
//                    llSearchNearby.setVisibility(GONE);
                    llSearch.setVisibility(GONE);
                    llRv.setVisibility(VISIBLE);
                    if (onClickListener != null) onClickListener.clickAppointment();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public OnClickListener onClickListener;

    public void addClickListener(OnClickListener clickListener) {
        onClickListener = clickListener;
    }

    public void setCheckStatus(String parentId) {
        labelAdapter.setCheck(parentId);
    }

    public interface OnClickListener {
        //点击视频
        void clickVideoSelect();

        //点击视频
        void clickAppointment();

        //点击附近
        void clickFind();

        //点击选择地址
        void selectAddress();

        //点击标签
        void clickLabel(BaseQuickAdapter baseQuickAdapter, View view, int i);

        void clickSelect();
    }
}
