package com.fengwo.module_live_vedio.mvp.ui.df;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.PkResultDto;
import com.fengwo.module_live_vedio.mvp.dto.TeamPkItemResultDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.PkGroupResultAdapter;

import java.util.Arrays;

public class PKGroupResultDialog extends DialogFragment {

    private View background;
    private RecyclerView recyclerView;
    BaseQuickAdapter adapter;
    PkResultDto pkResultDto;

    public static DialogFragment getInstance(PkResultDto pkResultDto){
        PKGroupResultDialog pkGroupResultDialog = new PKGroupResultDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("pkResultDto",pkResultDto);
        pkGroupResultDialog.setArguments(bundle);
        return pkGroupResultDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(true);
        return inflater.inflate(R.layout.dialog_pk_group_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pkResultDto = (PkResultDto) getArguments().getSerializable("pkResultDto");
        background = view.findViewById(R.id.view_back);
        recyclerView = view.findViewById(R.id.rv_group);
        ImageView imageView = view.findViewById(R.id.iv_result_status);
        if (pkResultDto.getTeamPkResultDto().getTeamIsWin()>0){
            imageView.setImageResource(R.drawable.ic_group_success);
        }else if (pkResultDto.getTeamPkResultDto().getTeamIsWin()<0){
            imageView.setImageResource(R.drawable.ic_group_failure);
        }else {
            imageView.setImageResource(R.drawable.ic_group_draw);
        }
        TextView tvWe = view.findViewById(R.id.tv_team_score);
        tvWe.setText("我方收礼"+pkResultDto.getTeamPkResultDto().getTeamPoint());
        TextView tvOther = view.findViewById(R.id.tv_other_team_score);
        tvOther.setText("对方收礼"+pkResultDto.getTeamPkResultDto().getOtherTeamPoint());
        view.findViewById(R.id.iv_close).setOnClickListener(v -> dismiss());

        float marginTop = (ScreenUtils.getScreenWidth(getContext()) -
                DensityUtils.dp2px(getContext(), 96F)) * 0.55F * 0.6F;

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) background.getLayoutParams();
        lp.topMargin = (int) marginTop;
        background.setLayoutParams(lp);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PkGroupResultAdapter(pkResultDto.getTeamPkResultDto().getList());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attr = window.getAttributes();
        attr.width = ScreenUtils.getScreenWidth(getContext()) - DensityUtils.dp2px(getContext(), 48F);
        attr.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attr);
    }
    public void showDF(FragmentManager fragmentManager, String tag){
        show(fragmentManager,tag);
        fragmentManager.executePendingTransactions();
    }
}
