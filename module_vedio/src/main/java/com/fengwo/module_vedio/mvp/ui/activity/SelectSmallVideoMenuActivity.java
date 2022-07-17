package com.fengwo.module_vedio.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.mvp.dto.SmallVideoMenuDto;
import com.fengwo.module_vedio.mvp.presenter.SmallVideoMenuPresenter;
import com.fengwo.module_vedio.mvp.ui.iview.ISmallVideoMenuView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/9
 */
public class SelectSmallVideoMenuActivity extends BaseMvpActivity<ISmallVideoMenuView, SmallVideoMenuPresenter> implements ISmallVideoMenuView {

    @BindView(R2.id.flow_layout)
    TagFlowLayout flowLayout;
    private TagAdapter<SmallVideoMenuDto> tagAdapter;
    private List<SmallVideoMenuDto> datas;

    @Override
    public SmallVideoMenuPresenter initPresenter() {
        return new SmallVideoMenuPresenter();
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("选择分类")
                .build();
        p.getSmallVideoMenu();
        flowLayout.setMaxSelectCount(1);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_select_small_video_menu;
    }

    @Override
    public void setSmallVideoMenu(BaseListDto<SmallVideoMenuDto> listDto) {
            datas = listDto.records;
            setTagAdapter();
    }

    private void setTagAdapter(){
        tagAdapter = new TagAdapter<SmallVideoMenuDto>(datas) {
            @Override
            public View getView(FlowLayout parent, int position, SmallVideoMenuDto o) {
                TextView v = (TextView) LayoutInflater.from(SelectSmallVideoMenuActivity.this).inflate(R.layout.item_small_video_flow_text, null);
                v.setText(o.getName() + "");
                v.setTextColor(getResources().getColor(R.color.text_33));
                return v;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                TextView text = (TextView) view;
//                int start = context.getResources().getColor(R.color.purple_8893FF);
//                int end = context.getResources().getColor(R.color.purple_9B70FC);
//                text.setColors(start, end);
//                text.setTextColor(context.getResources().getColor(R.color.white));
                Intent intent = new Intent();
                intent.putExtra("title",text.getText().toString());
                intent.putExtra("menuId",datas.get(position).getId());
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
//                GradientTextView text = (GradientTextView) view;
//                int start = context.getResources().getColor(R.color.gray_eaeaea);
//                int end = context.getResources().getColor(R.color.gray_eaeaea);
//                text.setColors(start, end);
//                text.setTextColor(context.getResources().getColor(R.color.text_33));
            }
        };
        flowLayout.setAdapter(tagAdapter);
    }
}
