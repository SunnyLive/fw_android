package com.fengwo.module_flirt.UI;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_flirt.Interfaces.IFlirtHomeView;
import com.fengwo.module_flirt.P.FlirtHomePresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.fragment.FlirtLabelFragment;
import com.fengwo.module_flirt.UI.fragment.FlirtTalentFragment;
import com.fengwo.module_flirt.bean.ZipLabelDto;
import com.fengwo.module_flirt.utlis.CommonUtils;
import com.fengwo.module_flirt.widget.FilterFlirtPopwindow;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * i撩 标签 动效页
 */
public class FlirtHomeFragment extends BaseMvpFragment<IFlirtHomeView, FlirtHomePresenter> implements IFlirtHomeView {
    @BindView(R2.id.rv_lable)
    RecyclerView rvLable;
    @BindView(R2.id.fl_content)
    FrameLayout flContent;
    public String value1Age = "";
    public String value2Age = "";
    public int sexA = 0;
    private LableAdapter lableAdapter;
    private FragmentTransaction fragmentTransaction;
    private FilterFlirtPopwindow filterFlirtPopwindow;

    @Override
    protected FlirtHomePresenter initPresenter() {
        return new FlirtHomePresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_flirt_home;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        rvLable.setLayoutManager(new GridLayoutManager(getContext(), 3));
        lableAdapter = new LableAdapter();
        rvLable.setAdapter(lableAdapter);
        /*片段管理*/
        fragmentTransaction = getFragmentManager().beginTransaction();
        /*筛选*/
        filter();
        /*获取数据*/
        p.getData("","","");

    }
    /**
     * 筛选
     */
    private void filter() {
        filterFlirtPopwindow = new FilterFlirtPopwindow(getContext(), TextUtils.isEmpty(value1Age) ? 0 : Integer.parseInt(value1Age), TextUtils.isEmpty(value2Age) ? 0 : Integer.parseInt(value2Age), sexA);
        filterFlirtPopwindow.addOnClickListener((valueA, valueB, sex) -> {
            L.e("valueA = " + valueA);
            L.e("valueB = " + valueB);
            L.e("sex = " + sex);
        });
    }

    @OnClick({R2.id.btn_playing, R2.id.btn_filter, R2.id.btn_flirt, R2.id.btn_home})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_playing) {/*i 撩 开播流程*/
            CommonUtils.playing(getContext(),2);
        } else if (id == R.id.btn_filter) {/*筛选*/
            filterFlirtPopwindow.showPopupWindow();
        } else if (id == R.id.btn_flirt) {/* TODO 撩一波*/
            FlirtLabelFragment flirtLabelFragment = new FlirtLabelFragment();
            fragmentTransaction.replace(R.id.fl_content,flirtLabelFragment).commit();

        } else if (id == R.id.btn_home) {/*TODO 达人列表*/
            FlirtTalentFragment talentFragment = new FlirtTalentFragment();
            fragmentTransaction.replace(R.id.fl_content,talentFragment).commit();
        }
    }

    @Override
    public void getZipLabel(List<ZipLabelDto> data) {
        lableAdapter.setNewData(data);
    }

    /**
     * label adapter
     */
    class LableAdapter extends BaseQuickAdapter<ZipLabelDto, BaseViewHolder> {

        public LableAdapter() {
            super(R.layout.adapter_lable);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ZipLabelDto item) {
            helper.setText(R.id.tv_table_name, TextUtils.isEmpty(item.getTagName())?item.getNickname():item.getTagName());
        }
    }
}
