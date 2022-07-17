package com.fengwo.module_flirt.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.CerTagBean;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author BLCS
 * @Time 2020/8/13 16:02
 */
public class FilterTableAdapter extends BaseQuickAdapter<CerTagBean, BaseViewHolder> {

    private  List<CerTagBean.ChildrenBean> checkeds;

    public FilterTableAdapter() {
        super(R.layout.adapter_filter_table);
        checkeds = new ArrayList<>();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, CerTagBean cerTagBean) {
        holder.setText(R.id.tv_table_title, cerTagBean.getTagName());

        RecyclerView rvTable = holder.getView(R.id.rv_table);
        rvTable.setLayoutManager(new GridLayoutManager(mContext, 3));
        ChildrenTableAdapter childrenTableAdapter = new ChildrenTableAdapter();
        rvTable.setAdapter(childrenTableAdapter);
        childrenTableAdapter.setNewData(cerTagBean.getChildren());

        childrenTableAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            CerTagBean.ChildrenBean bean = (CerTagBean.ChildrenBean) baseQuickAdapter.getData().get(i);
            boolean currentCheck = !bean.isChecked();//判断是否选中
            if (checkeds.size()>0 && !checkeds.get(0).getParentId().equals(bean.getParentId())) {
                ToastUtils.showShort(mContext, "只能选中同一个区域的标签");
                return;
            }
            bean.setChecked(currentCheck);
            TextView tvTable = view.findViewById(R.id.tv_childre_table);
            tvTable.setSelected(currentCheck);
            tvTable.setTextColor(currentCheck ? ContextCompat.getColor(mContext, R.color.text_white) : ContextCompat.getColor(mContext, R.color.color_9a9a));
            if (currentCheck) {//选中
                checkeds.add(bean);
            } else {
                if (checkeds.contains(bean)){
                    checkeds.remove(bean);
                }
            }
        });
    }

    public String getTabNames() {
        StringBuffer stringBuffer = new StringBuffer();
        for (CerTagBean.ChildrenBean bean: checkeds){
             stringBuffer.append(bean.getTagNameX()).append(",");
        }
        String oldString = stringBuffer.toString();
        if (TextUtils.isEmpty(oldString)) return null;
        String newString = oldString.substring(0,oldString.length() - 1);
        return newString;
    }

    public String getParentId() {
           return checkeds.size()>0?checkeds.get(0).getParentId():"";
    }

    public void clearCache() {
        checkeds.clear();
    }
}
