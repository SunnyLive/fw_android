package com.fengwo.module_chat.mvp.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LongClickDialog {

    private static final int DEFAULT_ITEM_HEIGHT = 50;

    public static void showLongClickDialogByPosition(Context ctx, View parent, CharSequence titleHint, String[] items, int gravity, int px, int py,
                                                     final OnItemClickListener itemClickListener) {
        int width = DensityUtils.dp2px(ctx, 75);

        View view = LayoutInflater.from(ctx).inflate(R.layout.view_longclick_delete, null);
        LinearLayout rootLayout = view.findViewById(R.id.longclick_delete_layout);
        RecyclerView listView = view.findViewById(R.id.list);
        TextView title = view.findViewById(R.id.longclick_delete_title);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
        rootLayout.setLayoutParams(lp);

//        lp = (LinearLayout.LayoutParams) listView.getLayoutParams();
//        lp.height = getShowDialogHeight(items.length);
        if (!TextUtils.isEmpty(titleHint)) {
            title.setText(titleHint);
        } else {
            view.findViewById(R.id.layout_dialog_title).setVisibility(View.GONE);
            view.setBackground(null);
        }

        BaseQuickAdapter adapter = new BaseQuickAdapter(R.layout.longclick_delete_item_adapter) {

            @Override
            protected void convert(@NonNull BaseViewHolder helper, Object item) {
                TextView title = helper.getView(android.R.id.title);
                title.setText((String) item);
                title.setGravity(Gravity.CENTER);
            }
        };
        listView.setLayoutManager(new LinearLayoutManager(ctx));
        listView.setAdapter(adapter);
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, items);
        adapter.setNewData(list);

        final PopupWindow popupWindow = new PopupWindow(view, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimationLongClickDelete);
        popupWindow.setFocusable(true);

        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(items[position]);
            }
            popupWindow.dismiss();
        });
        view.setOnClickListener(v -> popupWindow.dismiss());
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        L.e("==== "+py);
        popupWindow.showAtLocation(parent, gravity, px, py);
    }

    public static int getShowDialogHeight(int length) {
        return DensityUtils.dp2px(BaseApplication.mApp, length * DEFAULT_ITEM_HEIGHT + DensityUtils.dp2px(BaseApplication.mApp, 1));
    }

    public interface OnItemClickListener {
        void onItemClick(String content);
    }
}