package com.fengwo.module_comment.pop;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.R;

import java.util.ArrayList;
import java.util.Arrays;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

/**
 * @Author BLCS
 * @Time 2020/6/19 11:33
 */
public class WishTypewindow extends BasePopupWindow {


    private final WishTypeAdapter wishTypeAdapter;

    public WishTypewindow(Context context,ArrayList<String> strings) {
        super(context);
        ButterKnife.bind(this, getContentView());
        ARouter.getInstance().inject(this);
        setPopupGravity(Gravity.BOTTOM);
        RecyclerView rvPop = findViewById(R.id.rv_pop);
        rvPop.setLayoutManager(new LinearLayoutManager(getContext()));
        wishTypeAdapter = new WishTypeAdapter();
        rvPop.setAdapter(wishTypeAdapter);
        wishTypeAdapter.setNewData(strings);
        wishTypeAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            String title = (String) baseQuickAdapter.getData().get(i);
            if (onWishClickListener!=null) onWishClickListener.click(title,i);
            dismiss();
        });
    }



    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_wish_type);
        return  v;
    }

    public interface OnWishClickListener{
        void click(String titles,int pos);
    }
    public OnWishClickListener onWishClickListener;
    public void addOnClickListener(OnWishClickListener listener){
        onWishClickListener = listener;
    }
}
