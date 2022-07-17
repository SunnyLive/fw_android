package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.bean.OrderSortBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class OrderSortDialogWindow extends BasePopupWindow {

    @BindView(R2.id.rv)
    RecyclerView recyclerView;

    public OrderSortDialogWindow(Context context, Boolean isOutSide, int orderBy) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        setBackPressEnable(false);
        setOutSideDismiss(isOutSide);

        //排序 0：距离,1：活跃时间,2:添加好友时间,3:首字母 -> 默认为1
        List<OrderSortBean> sortBeans = new ArrayList<>();
        sortBeans.add(new OrderSortBean(0, "按距离", orderBy == 0));
        sortBeans.add(new OrderSortBean(1, "按最近活跃时间", orderBy == 1));
        sortBeans.add(new OrderSortBean(2, "按添加好友时间", orderBy == 2));
        sortBeans.add(new OrderSortBean(3, "按首字母", orderBy == 3));

        recyclerView.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        BaseQuickAdapter<OrderSortBean, BaseViewHolder> adapter = new BaseQuickAdapter<OrderSortBean, BaseViewHolder>(R.layout.item_order_sort_list) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, OrderSortBean item) {
                helper.setText(R.id.item_tv, item.title);
                LinearLayout layout = helper.getView(R.id.item_root);
                if (item.isCheck()) {
                    helper.setTextColor(R.id.item_tv, Color.WHITE);
                    layout.setBackgroundResource(R.drawable.rect_purple_round100);
                    helper.setGone(R.id.item_iv_check, true);
                } else {
                    helper.setTextColor(R.id.item_tv, getContext().getResources().getColor(R.color.text_66));
                    layout.setBackgroundColor(Color.TRANSPARENT);
                    helper.setGone(R.id.item_iv_check, false);
                }
            }
        };

        adapter.setNewData(sortBeans);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (listener != null) {
                listener.onSelected(position);
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);

    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_order_sort_dialog);
        ButterKnife.bind(this, v);
        return v;
    }

    public void addOnClickListener(OnOrderSortClickListener onOrderSortClickListener) {
        listener = onOrderSortClickListener;
    }

    public OnOrderSortClickListener listener;

    public interface OnOrderSortClickListener {
        void onSelected(int orderBy);
    }
}
