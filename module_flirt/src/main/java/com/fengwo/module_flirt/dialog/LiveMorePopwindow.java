package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_flirt.adapter.LiveMoreAdapter;
import com.fengwo.module_flirt.bean.LiveMoreBean;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.LivePushMoreDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivePushMoreAdapter;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeAnimateEvent;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 */
public class LiveMorePopwindow extends BasePopupWindow {

    private RecyclerView recyclerView;
    private Context context;
    private List<Integer> img = new ArrayList();//{R.drawable.ic_beauty, R.drawable.ic_share,R.drawable.live_ic_close_animate};
    private LiveMoreAdapter livePushMoreAdapter;
    private List<LivePushMoreDto> listData;
    private boolean isMirror = false;
    public LiveMorePopwindow(Context context,boolean isMirror) {
        super(context);
        this.context = context;
        this.isMirror = isMirror;
        setPopupGravity(Gravity.BOTTOM);
        recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(layoutManager);
        img.add(R.drawable.ic_beauty);
        img.add(R.drawable.ic_effect);
        img.add(R.drawable.ic_gesture);
        img.add(isMirror?R.drawable.ic_mirror_close:R.drawable.ic_mirror_open);
        img.add(R.drawable.ic_switch_1);
        initData();
    }

    private void initData() {
        listData = new ArrayList<>();

        livePushMoreAdapter = new LiveMoreAdapter(R.layout.live_item_more_pop, img);
        recyclerView.setAdapter(livePushMoreAdapter);
        livePushMoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 3){
                    isMirror = !isMirror;
                    img.set(3,isMirror?R.drawable.ic_mirror_close:R.drawable.ic_mirror_open);
                    livePushMoreAdapter.notifyDataSetChanged();
                }
                if (onItemClickListener!=null) onItemClickListener.onItemClick(adapter,view,position);
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
        return createPopupById(R.layout.live_pop_push_more);
    }

    public interface OnItemClickListener {
        void onItemClick(BaseQuickAdapter adapter, View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
