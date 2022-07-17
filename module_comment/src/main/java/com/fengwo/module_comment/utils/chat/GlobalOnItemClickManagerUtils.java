package com.fengwo.module_comment.utils.chat;

import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.EditText;

import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.utils.EmotionGridViewAdapter;
import com.fengwo.module_comment.utils.L;

/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/11/15.
 * 描    述：点击表情的全局监听管理类
 * 修订历史：
 * ================================================
 */
public class GlobalOnItemClickManagerUtils {

    private static GlobalOnItemClickManagerUtils instance;
    private EditText mEditText;//输入框
    private int maxlength;

    public static GlobalOnItemClickManagerUtils getInstance() {
        if (instance == null) {
            synchronized (GlobalOnItemClickManagerUtils.class) {
                if (instance == null) {
                    instance = new GlobalOnItemClickManagerUtils();
                }
            }
        }
        return instance;
    }

    public void attachToEditText(EditText editText,int maxlength) {
        mEditText = editText;
        this.maxlength = maxlength;
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return (parent, view, position, id) -> {
            Object itemAdapter = parent.getAdapter();

            if (itemAdapter instanceof EmotionGridViewAdapter) {
                // 点击的是表情
                EmotionGridViewAdapter emotionGvAdapter = (EmotionGridViewAdapter) itemAdapter;

                if (position == emotionGvAdapter.getCount() - 1) {
                    // 如果点击了最后一个回退按钮,则调用删除键事件
                    mEditText.dispatchKeyEvent(new KeyEvent(
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                } else {
                    // 如果点击了表情,则添加到输入框中
                    String emotionName = emotionGvAdapter.getItem(position);

                    // 获取当前光标位置,在指定位置上添加表情图片文本
                    int curPosition = mEditText.getSelectionStart();
                    StringBuilder sb = new StringBuilder(mEditText.getText().toString());
                    sb.insert(curPosition, emotionName);
                    int length = mEditText.getFilters().length;
                    int length1 = mEditText.getFilters().clone().length;
                    if (curPosition + emotionName.length()<=maxlength){
                        // 特殊文字处理,将表情等转换一下
                        mEditText.setText(EmotionUtils.getEmotionContent(BaseApplication.mApp, mEditText, sb.toString()));
                        // 将光标设置到新增完表情的右侧
                        mEditText.setSelection(curPosition + emotionName.length());
                    }
                }

            }
        };
    }

}
