package com.fengwo.module_flirt.UI.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.Interfaces.IFlirtLabelView;
import com.fengwo.module_flirt.P.FlirtLabelPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.FlirtCardDetailsActivity;
import com.fengwo.module_flirt.bean.LabelTalentDto;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 标签组合后的界面
 */
public class FlirtLabelFragment extends BaseMvpFragment<IFlirtLabelView, FlirtLabelPresenter> implements IFlirtLabelView {
    @BindView(R2.id.rv_talent)
    RecyclerView rvTalent;
    private FlirtLabelAdapter mAdapter;
    private int page =1;
    @Override
    protected FlirtLabelPresenter initPresenter() {
        return new FlirtLabelPresenter();
    }
    @Override
    protected int setContentView() {
        return R.layout.fragment_flirt_label;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        rvTalent.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FlirtLabelAdapter();
        rvTalent.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LabelTalentDto labelTalent = (LabelTalentDto) adapter.getData().get(position);
                FlirtCardDetailsActivity.start(getContext(),labelTalent.getAnchorId());
            }
        });
        p.getLabelTalent("","","",page,"");
    }

    class FlirtLabelAdapter extends BaseQuickAdapter<LabelTalentDto, BaseViewHolder> {
        public FlirtLabelAdapter() {
            super(R.layout.adapter_flirt_label);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, LabelTalentDto item) {
            helper.setText(R.id.tv_name,item.getNickname());
            ImageView view = helper.getView(R.id.iv_avatar);
            ImageLoader.loadCircleImg(view,item.getHeadImg());
        }
    }

    @OnClick({R2.id.btn_flirt, R2.id.btn_re_selection})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_flirt) {/*todo 达人页面*/

        } else if (id == R.id.btn_re_selection) {/*todo 重新撩一波*/

        }
    }

    @Override
    public void setLabelTalent(BaseListDto<LabelTalentDto> data) {
        mAdapter.setNewData(data.records);
    }
}
