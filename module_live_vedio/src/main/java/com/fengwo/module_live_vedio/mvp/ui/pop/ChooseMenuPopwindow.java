package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.ZhuboMenuDto;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class ChooseMenuPopwindow extends BasePopupWindow {

    private View btnClose;
    private TagFlowLayout flowLayout;
    private TagAdapter<ZhuboMenuDto> tagAdapter;
    private List<ZhuboMenuDto> datas;

    public ChooseMenuPopwindow(Context context, List<ZhuboMenuDto> datas, OnConfirmCallback callback) {
        super(context);
        this.datas = datas;
        setPopupGravity(Gravity.BOTTOM);
        btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> {
            if (callback != null) {
                StringBuilder menuName = new StringBuilder();
                List<ZhuboMenuDto> selectDatas = getSelected();
                if (selectDatas.size() == 0) {
                    return;
                }
                for (ZhuboMenuDto menuDto : selectDatas) {
                    menuName.append(menuDto.name).append(" ");
                }
                menuName.deleteCharAt(menuName.length() - 1);
                callback.confirm("" + menuName);
            }
        });
        flowLayout = findViewById(R.id.tabflowlayout);
        flowLayout.setMaxSelectCount(2);
        tagAdapter = new TagAdapter<ZhuboMenuDto>(datas) {
            @Override
            public View getView(FlowLayout parent, int position, ZhuboMenuDto o) {
                GradientTextView v = (GradientTextView) LayoutInflater.from(context).inflate(R.layout.live_item_zhubomenu, null);
                v.setText(o.name + "");
                v.setTextColor(Color.parseColor("#999999"));
                return v;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                GradientTextView text = (GradientTextView) view;
                int start = context.getResources().getColor(R.color.homt_tab_selsct_all);
                int end = context.getResources().getColor(R.color.homt_tab_selsct_all);
                text.setColors(start, end);
                text.setTextColor(context.getResources().getColor(R.color.text_white_arr));
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                GradientTextView text = (GradientTextView) view;
                int start = Color.parseColor("#dddddd");
                int end = Color.parseColor("#dddddd");
                text.setColors(start, end);
                text.setTextColor(Color.parseColor("#999999"));
            }
        };
        flowLayout.setAdapter(tagAdapter);
    }

    public List<ZhuboMenuDto> getSelected() {
        List<ZhuboMenuDto> result = new ArrayList<>();
        Iterator<Integer> it = flowLayout.getSelectedList().iterator();
        while (it.hasNext()) {
            result.add(datas.get(it.next()));
        }
        return result;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_zhubomenu);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    public interface OnConfirmCallback {
        void confirm(String tag);
    }
}
