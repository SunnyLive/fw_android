package com.fengwo.module_login.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.HuafenLevelDto;
import com.fengwo.module_login.mvp.dto.PrivilegeDto;
import com.fengwo.module_login.mvp.dto.TaskDto;
import com.fengwo.module_login.mvp.presenter.HuafenPresenter;
import com.fengwo.module_login.mvp.ui.adapter.HuafenTaskAdapter;
import com.fengwo.module_login.mvp.ui.adapter.PrivilegeAdapter;
import com.fengwo.module_login.mvp.ui.iview.IHuafenView;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_login.widget.HuafenProgressBar;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HuafenActivity extends BaseMvpActivity<IHuafenView, HuafenPresenter> implements IHuafenView {
    @BindView(R2.id.progress)
    HuafenProgressBar progress;
    @BindView(R2.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R2.id.tv_next_level)
    TextView tvNextLevel;
    @BindView(R2.id.rv_task)
    RecyclerView rvTask;
    @BindView(R2.id.rv_privilege)
    RecyclerView rvPrivilege;
    @BindView(R2.id.iv_level)
    ImageView ivLevel;
    @BindView(R2.id.ll_huafen_bg)
    LinearLayout llHuafenBg;

    @Override
    public HuafenPresenter initPresenter() {
        return new HuafenPresenter();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void initView() {
        if (DarkUtil.isDarkTheme(this)){
            llHuafenBg.setBackground(null);
        }
        setWhiteTitle("花粉");
        p.getUserHuafenLevel();
        p.getUserTask();
        p.getUserPrivilege();
        ImageLoader.loadImg(ivHeader, UserManager.getInstance().getUser().headImg);
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_huafen;
    }

    @Override
    public void setLevle(HuafenLevelDto data) {
        long allLevel = data.levelHighest - data.levelLowest;
        ivLevel.setImageResource(ImageLoader.getUserLevel(data.levelId));

        if (data.myExperience>data.levelHighest){
            tvNextLevel.setText("已满级");
            progress.setProgressWithAnim(allLevel*1f/allLevel);
        }else {
            tvNextLevel.setText("距离升级：" + (data.levelHighest - data.myExperience));
            progress.setProgressWithAnim((data.myExperience - data.levelLowest) * 1f / allLevel);
        }

    }

    @Override
    public void setTaskList(List<TaskDto> data) {
        rvTask.setLayoutManager(new GridLayoutManager(this, 2));
        rvTask.setAdapter(new HuafenTaskAdapter(data));
    }

    @Override
    public void setPrivelege(List<PrivilegeDto> data) {
        rvPrivilege.setLayoutManager(new GridLayoutManager(this, 3));
        rvPrivilege.setAdapter(new PrivilegeAdapter(data));

    }
}
