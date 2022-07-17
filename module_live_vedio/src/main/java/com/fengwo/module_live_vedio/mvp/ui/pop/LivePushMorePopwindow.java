package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.LivePushMoreDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivePushMoreAdapter;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeAnimateEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class LivePushMorePopwindow extends BasePopupWindow {

    private RecyclerView recyclerView;
    private Context context;

    private LivePushMoreAdapter livePushMoreAdapter;

    private boolean isAnimate = true;
    List<LivePushMoreDto> list;

    public LivePushMorePopwindow(Context context, List<LivePushMoreDto> list) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.BOTTOM);
        this.list = list;
        recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(layoutManager);
        initData();
    }

    private void initData() {
        livePushMoreAdapter = new LivePushMoreAdapter(R.layout.live_item_more_pop, list);
        recyclerView.setAdapter(livePushMoreAdapter);
        livePushMoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LivePushMoreDto livePushMoreDto = livePushMoreAdapter.getData().get(position);
                if (livePushMoreDto == LivePushMoreDto.动画) {
                    isAnimate = !isAnimate;
                    livePushMoreDto.setOpen(!isAnimate);
                    livePushMoreDto.setTitle(isAnimate ? "关闭动画" : "开启动画");
                    RxBus.get().post(new ChangeAnimateEvent(isAnimate));
                    livePushMoreAdapter.notifyDataSetChanged();
                    return;
                }

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(livePushMoreDto, position);
                    livePushMoreAdapter.notifyDataSetChanged();
                    //        dismiss();
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
        return createPopupById(R.layout.live_pop_push_more);
    }

    public void setSelete(boolean b) {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i)==LivePushMoreDto.音效){
                list.get(i).setOpen(b);
                break;
            }
        }
        livePushMoreAdapter.notifyDataSetChanged();

    }

    public interface OnItemClickListener {
        void onItemClick(LivePushMoreDto livePushMoreDto, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
