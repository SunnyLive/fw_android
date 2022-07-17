package com.fengwo.module_flirt.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.TopicTagBean;

import java.util.function.Consumer;

/**
 * @Author BLCS
 * @Time 2020/5/20 16:44
 */
public class LabelAdapter extends BaseQuickAdapter<CerTagBean, BaseViewHolder> {
    private int checkPosition = 0;

    public LabelAdapter() {
        super(R.layout.adapter_label);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder mHolder, CerTagBean label) {
        mHolder.setText(R.id.tv_label, label.getTagName());
        TextView tv = mHolder.getView(R.id.tv_label);
        tv.setSelected(mHolder.getAdapterPosition() == checkPosition);
    }


    public void setCheck(int position) {
        checkPosition = position;
        notifyDataSetChanged();
    }

    public void setCheck(String parentId) {
        for (int i=0;i<getData().size();i++) {
            if (getData().get(i).getId()==Integer.parseInt(parentId)){
                setCheck(i);
            }
        }
    }
}
