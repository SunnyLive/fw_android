package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.MakeExpansionDto;
import com.fengwo.module_live_vedio.mvp.dto.PendantListDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivePushMoreAdapters;
import com.fengwo.module_live_vedio.mvp.ui.adapter.PendantListMoreAdapters;
import com.fengwo.module_live_vedio.mvp.ui.adapter.PendantListMoreAdapterser;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @anchor Administrator
 * @date 2020/9/2
 */
public class PendantListPopwindow extends BasePopupWindow {

    private RecyclerView tv_recyclerview;
    private RecyclerView pic_recyclerview;
    private Context context;

    private PendantListMoreAdapters livePushMoreAdapter;
    private PendantListMoreAdapterser pendantListMoreAdapterser;
    PendantListDto listDtos;
    private boolean isAnimate = true;


    public PendantListPopwindow(Context context, PendantListDto listDtos) {
        super(context);
        this.context = context;

        setPopupGravity(Gravity.BOTTOM);
        this.listDtos = listDtos;
        tv_recyclerview = findViewById(R.id.tv_recyclerview);
        tv_recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        pic_recyclerview = findViewById(R.id.pic_recyclerview);
        pic_recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        initData();
    }

    private void initData() {

        livePushMoreAdapter = new PendantListMoreAdapters(R.layout.live_item_pendant_pops, listDtos.getTextStickers());
        tv_recyclerview.setAdapter(livePushMoreAdapter);
        livePushMoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                onItemClickListener.onItemClick(adapter, view, livePushMoreAdapter.getItem(position).getStickerUrl(),livePushMoreAdapter.getItem(position).getId(),true,livePushMoreAdapter.getItem(position).getTextLength(),livePushMoreAdapter.getItem(position).getTextColor());
                dismiss();
            }

        });


        pendantListMoreAdapterser = new PendantListMoreAdapterser(R.layout.live_item_pendant_pops, listDtos.getGraphicStickers());
        pic_recyclerview.setAdapter(pendantListMoreAdapterser);
        pendantListMoreAdapterser.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                onItemClickListener.onItemClick(adapter, view, pendantListMoreAdapterser.getItem(position).getStickerUrl(),pendantListMoreAdapterser.getItem(position).getId(),false,pendantListMoreAdapterser.getItem(position).getTextLength(),pendantListMoreAdapterser.getItem(position).getTextColor());
                dismiss();
            }

        });
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_push_mores);
    }

    public interface OnItemClickListener {
        void onItemClick(BaseQuickAdapter adapter, View view, String pendantListDto,int id,boolean type,int length,String textcolor);
    }

    private PendantListPopwindow.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(PendantListPopwindow.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
