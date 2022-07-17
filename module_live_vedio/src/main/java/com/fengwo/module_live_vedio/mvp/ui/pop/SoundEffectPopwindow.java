package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.LivePushMoreDto;
import com.fengwo.module_live_vedio.mvp.dto.SoundDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivePushMoreAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LiveSoundMoreAdapter;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeAnimateEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class SoundEffectPopwindow extends BasePopupWindow {

    private RecyclerView recyclerView;
    private Context context;
    private List<SoundDto> img = new ArrayList();//{R.drawable.ic_beauty, R.drawable.ic_share,R.drawable.live_ic_close_animate};
    private LiveSoundMoreAdapter livePushMoreAdapter;
private TextView tv_context;
    private boolean isAnimate = true;
    private boolean isMirror = false;

    public SoundEffectPopwindow(Context context) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.BOTTOM);
        tv_context = findViewById(R.id.tv_context);
        tv_context.setText("音效调节");
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lm);
        img.add(new SoundDto(false,"0"));
        img.add(new SoundDto(false,"KTV"));
        img.add(new SoundDto(false,"小房间"));
        img.add(new SoundDto(false,"大会堂"));
        img.add(new SoundDto(false,"低沉"));
        img.add(new SoundDto(false,"洪亮"));
        img.add(new SoundDto(false,"磁性"));
        initData();
    }

    private void initData() {
        livePushMoreAdapter = new LiveSoundMoreAdapter(R.layout.live_item_sound, img);
        recyclerView.setAdapter(livePushMoreAdapter);
        livePushMoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < img.size(); i++) {
                    if(position==i){
                        img.get(i).setOpen(true);
                    }else {
                        img.get(i).setOpen(false);
                    }
                }
                livePushMoreAdapter.notifyDataSetChanged();
                String livePushMoreDto = livePushMoreAdapter.getData().get(position).getTitle();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(livePushMoreDto);
                }

//                switch (livePushMoreDto){
//                    case "0":
//                        break;
//                    case "KTV":
//                        break;
//                }




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
        void onItemClick(String livePushMoreDto);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
