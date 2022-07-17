package com.fengwo.module_live_vedio.mvp.ui.df;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.RoomMemberChangeMsg;
import com.fengwo.module_live_vedio.mvp.presenter.LivingRoomPresenter;

import java.util.ArrayList;
import java.util.List;

public class PKGroupMatchingDialog extends DialogFragment {
    private int MAX_NUM = 3;

    private TextView tvCountdown;
    private ImageView ivHead;
    private BaseQuickAdapter otherAdapter;
    private BaseQuickAdapter weAdapter;
    private GridLayoutManager selfLayoutManager;
    private GridLayoutManager enemyLayoutManager;
    private List<RoomMemberChangeMsg> mList = new ArrayList<>();

    private int rvWidth;
    private LivingRoomPresenter presenter;
    private String headUrl;
    private CountDownTimer matchStartCDT;

    //    @SuppressLint("ValidFragment")
//    public PKGroupMatchingDialog(List<RoomMemberChangeMsg> list, int MAX_NUM, LivingRoomPresenter presenter) {
//        this.mList = list;
//        this.MAX_NUM = MAX_NUM;
//        this.presenter = presenter;
//        for (int i = list.size();i<MAX_NUM;i++){
//            mList.add(new RoomMemberChangeMsg());
//        }
//    }

    public static DialogFragment getInstance(LivingRoomPresenter presenter){
        PKGroupMatchingDialog pkGroupMatchingDialog = new PKGroupMatchingDialog();
        pkGroupMatchingDialog.presenter = presenter;
        return pkGroupMatchingDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.dialog_pk_group_matching, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.ivClose).setOnClickListener(v -> {
            cancelPop();
        });
        tvCountdown = view.findViewById(R.id.tvCountdown);
        ivHead = view.findViewById(R.id.iv_header);
        if (headUrl != null)
            ImageLoader.loadImg(ivHead, headUrl);
//        RecyclerView selfRv = view.findViewById(R.id.rvSelfGroup);
//        TextView tvGradient = view.findViewById(R.id.tvGradient);
//        RecyclerView enemyRv = view.findViewById(R.id.rvEnemyGroup);

//        // 设置RecyclerView
//        selfLayoutManager = new GridLayoutManager(getContext(), MAX_NUM);
//        enemyLayoutManager = new GridLayoutManager(getContext(), MAX_NUM);
//        GridItemDecoration decoration = new GridItemDecoration(DensityUtils.dp2px(getContext(), 7.5f));
//        otherAdapter = new BaseQuickAdapter(R.layout.item_group_matching) {
//            @Override
//            protected void convert(@NonNull BaseViewHolder helper, Object item) {
//                CircleImageView image = helper.getView(R.id.ivContent);
//                image.setBorderColor(Color.parseColor("#D225E1"));
//            }
//        };
//        weAdapter = new BaseQuickAdapter<RoomMemberChangeMsg,BaseViewHolder>(R.layout.item_group_matching,mList) {
//            @Override
//            protected void convert(@NonNull BaseViewHolder helper, RoomMemberChangeMsg item) {
//                ImageLoader.loadImg(helper.getView(R.id.ivContent),item.headImg);
//            }
//        };
//        selfRv.setLayoutManager(selfLayoutManager);
//        selfRv.setAdapter(weAdapter);
//        selfRv.addItemDecoration(decoration);
//        enemyRv.setLayoutManager(enemyLayoutManager);
//        enemyRv.setAdapter(otherAdapter);
//        enemyRv.addItemDecoration(decoration);
//        // 设置数据
//        otherAdapter.setNewData(Arrays.asList(0, 1, 2));
    }

    private void startMatchCountDown(int second) {
        cancelCountDown();
        matchStartCDT = new CountDownTimer(second * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvCountdown != null)
                    tvCountdown.setText((int) (millisUntilFinished / 1000) + "S");
            }

            @Override
            public void onFinish() {
                if (isVisible()) cancelPop();
            }
        }.start();
    }

    private void cancelPop() {
        presenter.cancelGroupPk();
    }

    public void cancelCountDown() {
        if (matchStartCDT != null) matchStartCDT.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attr = window.getAttributes();
//        attr.width = Math.max(DensityUtils.dp2px(getContext(), 100), rvWidth)
//                + DensityUtils.dp2px(getContext(), 36);
        attr.width = WindowManager.LayoutParams.WRAP_CONTENT;
        attr.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attr.gravity = Gravity.BOTTOM | Gravity.END;
        window.setAttributes(attr);
    }

    public void showDF(FragmentManager fragmentManager, String tag, String headUrl) {
        this.headUrl = headUrl;
        show(fragmentManager, tag);
        fragmentManager.executePendingTransactions();
        startMatchCountDown(180);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        cancelCountDown();
    }
}