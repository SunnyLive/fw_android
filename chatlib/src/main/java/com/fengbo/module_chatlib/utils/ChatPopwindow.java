package com.fengbo.module_chatlib.utils;

import android.content.Context;
import android.renderscript.Sampler;
import android.view.View;

import com.fengbo.module_chatlib.R;

import razerdp.basepopup.BasePopupWindow;

public class ChatPopwindow extends BasePopupWindow implements View.OnClickListener {
    View v;
    OnItemClickListener l;

    private View btnReback, btnDel;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.l = l;
    }

    public ChatPopwindow(Context context) {
        super(context);
        setBackgroundColor(android.R.color.transparent);
        btnReback = findViewById(R.id.btn_reback);
        btnDel = findViewById(R.id.btn_del);
        btnReback.setOnClickListener(this);
        btnDel.setOnClickListener(this);
    }

    public int getWidth() {
        return v.getWidth();
    }

    @Override
    public View onCreateContentView() {
        v = createPopupById(R.layout.item_msg_pop);
        return v;
    }

    @Override
    public void onClick(View view) {
        if (null == this.l) {
            return;
        }
        int id = view.getId();
        if (id == R.id.btn_reback) {
            l.onReback();
        } else if (id == R.id.btn_del) {
            l.onDel();
        }
        dismiss();
    }

    public interface OnItemClickListener {
        void onReback();

        void onDel();

        void onCheckAble();
    }
}
