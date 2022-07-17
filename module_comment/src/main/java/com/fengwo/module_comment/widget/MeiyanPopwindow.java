package com.fengwo.module_comment.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.base.BeautyDto;
import com.fengwo.module_comment.utils.camera.Config;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class MeiyanPopwindow extends BasePopupWindow implements SeekBar.OnSeekBarChangeListener {
    private String[] title = {"美颜", "美型", "滤镜", "贴纸"};
    private SeekBar sbFennen, sbHongrun, sbBaixi, sbMeifu, sbDuibidu, sbEye, sbFace, sbFilter;
    private TextView tvFennen, tvHongrun, tvBaixi, tvMeifu, tvDuibidu, tvEye, tvFace, tvFilter;
    private OnChangeListener listener;
    private List<ItemData> mListData;
    private List<ItemSticker> mStickerListData;
    private Context mContext;

    private TabLayout topTablayout;

    private ConstraintLayout cl_meiyan, clmeixing, cl_filter,tiezhi_cl;
    private RecyclerView filterRv,rv_tiezhi;

    public void setOnchangeListener(OnChangeListener l) {
        listener = l;
    }

    public MeiyanPopwindow(Context context) {
        super(context);
        mContext = context;
        sbFennen = findViewById(R.id.sb_fennen);
        tvFennen = findViewById(R.id.tv_fennen);
        sbHongrun = findViewById(R.id.sb_hongrun);
        tvHongrun = findViewById(R.id.tv_hongrun);
        sbBaixi = findViewById(R.id.sb_baixi);
        tvBaixi = findViewById(R.id.tv_baixi);
        sbMeifu = findViewById(R.id.sb_meifu);
        tvMeifu = findViewById(R.id.tv_meifu);
        sbEye = findViewById(R.id.sb_eye);
        tvEye = findViewById(R.id.tv_eye);
        sbFace = findViewById(R.id.sb_face);
        tvFace = findViewById(R.id.tv_face);
        topTablayout = findViewById(R.id.top_tb);
        cl_meiyan = findViewById(R.id.cl_meiyan);
        clmeixing = findViewById(R.id.cl_meixing);
        sbFilter = findViewById(R.id.sb_filter);
        rv_tiezhi = findViewById(R.id.rv_tiezhi);
        tiezhi_cl = findViewById(R.id.tiezhi_cl);
        tvFilter = findViewById(R.id.tv_lvjing_value);
        cl_filter = findViewById(R.id.filter_cl);

        filterRv = findViewById(R.id.rv_lvjing);
//        sbDuibidu = findViewById(R.id.sb_duibidu);
//        tvDuibidu = findViewById(R.id.tv_duibidu);
        sbFennen.setOnSeekBarChangeListener(this);
        sbHongrun.setOnSeekBarChangeListener(this);
        sbBaixi.setOnSeekBarChangeListener(this);
        sbMeifu.setOnSeekBarChangeListener(this);
        sbEye.setOnSeekBarChangeListener(this);
        sbFace.setOnSeekBarChangeListener(this);
        sbFilter.setOnSeekBarChangeListener(this);
        initTab();
        ConstructList();
        ConstructStickerList();
//        sbDuibidu.setOnSeekBarChangeListener(this);
        setBackgroundColor(android.R.color.transparent);
    }

    private void initTab() {
        for (int i = 0; i < title.length; i++) {
            TabLayout.Tab t = topTablayout.newTab();
            t.setText(title[i]);
            topTablayout.addTab(t);
        }
        topTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int posiiton = tab.getPosition();
                switch (posiiton) {
                    case 0:
                        cl_meiyan.setVisibility(View.VISIBLE);
                        clmeixing.setVisibility(View.GONE);
                        cl_filter.setVisibility(View.GONE);
                        tiezhi_cl.setVisibility(View.GONE);
                        break;
                    case 1:
                        cl_meiyan.setVisibility(View.GONE);
                        clmeixing.setVisibility(View.VISIBLE);
                        cl_filter.setVisibility(View.GONE);
                        tiezhi_cl.setVisibility(View.GONE);
                        break;
                    case 2:
                        cl_meiyan.setVisibility(View.GONE);
                        clmeixing.setVisibility(View.GONE);
                        cl_filter.setVisibility(View.VISIBLE);
                        tiezhi_cl.setVisibility(View.GONE);
                        break;
                    case 3:
                        cl_meiyan.setVisibility(View.GONE);
                        clmeixing.setVisibility(View.GONE);
                        cl_filter.setVisibility(View.GONE);
                        tiezhi_cl.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void ConstructList() {
        mListData = new ArrayList<>();
        for (int i = 0; i < Config.mFilterName.length; ++i) {
            ItemData itemData = new ItemData();
            itemData.filterName = Config.mFilterName[i];
            itemData.filterType = Config.mFilterType[i];
            itemData.resId = Config.mFilterImage[i];
            mListData.add(itemData);
        }
        filterRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        filterRv.setAdapter(new FilterAdapter(mListData));
    }
    private void ConstructStickerList() {
        mStickerListData = new ArrayList<>();
        for (int i = 0; i < Config.mStickerImg.length; ++i) {
            ItemSticker itemSticker = new ItemSticker();
            itemSticker.stickerImg = Config.mStickerImg[i];
            itemSticker.stickerType = Config.mStickerType[i];
            mStickerListData.add(itemSticker);
        }
        rv_tiezhi.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rv_tiezhi.setAdapter(new StickerAdapter(mStickerListData));
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_meiyan);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (listener != null) {
            float value = 1f * i / 100;
            int id = seekBar.getId();
            if (id == R.id.sb_fennen) {
                tvFennen.setText(value + "");
                listener.onFennenChange(value);
            } else if (id == R.id.sb_hongrun) {
                tvHongrun.setText(value + "");
                listener.onHongrunChange(value);
            } else if (id == R.id.sb_baixi) {
                tvBaixi.setText(value + "");
                listener.onBaixiChange(value);
            } else if (id == R.id.sb_meifu) {
                tvMeifu.setText(i + "");
                listener.onMeifuChange(i);
            } else if (id == R.id.sb_eye) {
                tvEye.setText(i + "");
                listener.OnEyeChange(i);
            } else if (id == R.id.sb_face) {
                tvFace.setText(i + "");
                listener.onFaceChange(i);
            } else if (id == R.id.sb_filter) {
                tvFilter.setText(i + "");
                listener.onFilterStrengthChange(i);
            }
        }
    }
    public void setBeautyProgress(BeautyDto beautyDto){
        tvFennen.setText(beautyDto.mPinkValue + "");
        tvHongrun.setText(beautyDto.mReddenValue + "");
        tvBaixi.setText(beautyDto.mWhitenValue + "");
        tvMeifu.setText(beautyDto.mSoftenValue + "");
        tvEye.setText(beautyDto.mEyeValue + "");
        tvFace.setText(beautyDto.mFaceValue + "");
        tvFilter.setText(beautyDto.mFilterValue + "");
        sbFennen.setProgress((int) (beautyDto.mPinkValue*100));
        sbHongrun.setProgress((int) (beautyDto.mReddenValue*100));
        sbBaixi.setProgress((int) (beautyDto.mWhitenValue*100));
        sbMeifu.setProgress(beautyDto.mSoftenValue);
        sbEye.setProgress(beautyDto.mEyeValue);
        sbFace.setProgress(beautyDto.mFaceValue);
        sbFilter.setProgress(beautyDto.mFilterValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public class ItemData {
        String filterName;
        String filterType;
        int resId;
    }
    public class ItemSticker {
        int stickerImg;
        String stickerType;
    }


    public interface OnChangeListener {
        void onFennenChange(float f);

        void onHongrunChange(float f);

        void onBaixiChange(float f);

        void onMeifuChange(int i);

        void OnEyeChange(int i);

        void onFaceChange(int i);

        void onFilterChange(String name);

        void onFilterStrengthChange(int value);

        void onStickerChange(String type);
    }

    public class FilterAdapter extends BaseQuickAdapter<ItemData, BaseViewHolder> {

        public FilterAdapter(@Nullable List<ItemData> data) {
            super(R.layout.item_filter, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ItemData item) {
            helper.setText(R.id.tv_title, item.filterName);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != listener) {
                        listener.onFilterChange(item.filterType);
                    }
                }
            });
            if (item.resId!=1)
            helper.setImageResource(R.id.iv_filter,item.resId);

        }
    }
    public class StickerAdapter extends BaseQuickAdapter<ItemSticker,BaseViewHolder> {

        public StickerAdapter(@Nullable List<ItemSticker> data) {
            super(R.layout.item_sticker,data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ItemSticker item) {
            helper.getView(R.id.iv_sticker).setBackgroundResource(item.stickerImg);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != listener) {
                        listener.onStickerChange(item.stickerType);
                    }
                }
            });
        }
    }
}
