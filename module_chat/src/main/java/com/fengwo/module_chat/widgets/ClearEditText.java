package com.fengwo.module_chat.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengwo.module_chat.R;

import kotlin.jvm.internal.PropertyReference0Impl;

/**
 *
 * @author chenshanghui
 * @intro
 * @date 2019/9/6
 */
public class ClearEditText extends FrameLayout implements TextWatcher, View.OnClickListener {

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private ImageView ivClear;
    private EditText etInput;

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search_bar, this);
        ivClear = findViewById(R.id.iv_clear);
        etInput = findViewById(R.id.et_input);
        ivClear.setVisibility(View.GONE);
        ivClear.setOnClickListener(this);
        etInput.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (TextUtils.isEmpty(charSequence)) {
            ivClear.setVisibility(View.GONE);
        } else {
            ivClear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        etInput.setText("");
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener){
        etInput.setOnEditorActionListener(listener);
    }

    public void setImeOption(int imeOption){
        etInput.setImeOptions(imeOption);
    }

}
