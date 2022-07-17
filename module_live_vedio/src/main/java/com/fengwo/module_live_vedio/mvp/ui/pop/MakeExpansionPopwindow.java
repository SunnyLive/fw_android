package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.LivePushMoreDto;
import com.fengwo.module_live_vedio.mvp.dto.MakeExpansionDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivePushMoreAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivePushMoreAdapters;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeAnimateEvent;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @anchor Administrator
 * @date 2020/9/2
 */
public class MakeExpansionPopwindow extends BasePopupWindow
{

        private RecyclerView recyclerView;
        private Context context;
        private List<MakeExpansionDto> img = new ArrayList();//{R.drawable.ic_beauty, R.drawable.ic_share,R.drawable.live_ic_close_animate};
        private LivePushMoreAdapters livePushMoreAdapter;
        private List<MakeExpansionDto> listData;

        private boolean isAnimate = true;
        private boolean isMirror = false;

    public MakeExpansionPopwindow(Context context,boolean isMirror,int le) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.BOTTOM);
        this.isMirror = isMirror;
        recyclerView = findViewById(R.id.recyclerview);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.leftMargin = le-((int)getContext().getResources().getDimension(R.dimen.dp_25));
        recyclerView.setLayoutParams(layoutParams);
        RecyclerView.LayoutManager      layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        img.add(new MakeExpansionDto(R.drawable.pic_my,"美颜"));
        img.add(new MakeExpansionDto(R.drawable.pic_ss,"手势魔法"));
        img.add(new MakeExpansionDto(R.drawable.pic_dj,"道具"));
        img.add(new MakeExpansionDto(R.drawable.pic_gj,"挂件"));
//        img.add(R.drawable.ic_effect);
//        img.add(R.drawable.ic_gesture);
//        img.add(R.drawable.ic_share);
//        img.add(isMirror?R.drawable.ic_mirror_close:R.drawable.ic_mirror_open);
//        img.add(R.drawable.live_ic_close_animate);
        initData();
    }

        private void initData() {
        listData = new ArrayList<>();
        for (int i = 0; i < img.size(); i++) {
            listData.add(img.get(i));
        }
        livePushMoreAdapter = new LivePushMoreAdapters(R.layout.live_item_more_pops, listData);
        recyclerView.setAdapter(livePushMoreAdapter);
        livePushMoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //todo 临时做的判断
//                if (position == img.size() - 1) {
//                    isAnimate = !isAnimate;
//
//                    RxBus.get().post(new ChangeAnimateEvent(isAnimate));
//                    livePushMoreAdapter.notifyDataSetChanged();
//                    return;
//                }
                if (onItemClickListener != null) {
                    if (position == 4){
                        isMirror = !isMirror;

                        livePushMoreAdapter.notifyDataSetChanged();
                    }
                    onItemClickListener.onItemClick(adapter, view, position);
                    dismiss();
                }
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
        return createPopupById(R.layout.live_pop_push_info);
    }

        public interface OnItemClickListener {
            void onItemClick(BaseQuickAdapter adapter, View view, int position);
        }

        private MakeExpansionPopwindow.OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(MakeExpansionPopwindow.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
