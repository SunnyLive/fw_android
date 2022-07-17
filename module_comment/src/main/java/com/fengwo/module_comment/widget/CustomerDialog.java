package com.fengwo.module_comment.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.CountDownTimerUtil;


/**
 * Created by Administrator on 2018/8/3.
 */

public class CustomerDialog extends Dialog {

    private LinearLayout dialog_ll;
    private TextView tvTitle;
    public TextView tvMsg;
    private TextView btnPositive;
    private TextView btnNegative;
    private ImageView btnCloseWin;
    private EditText edtInputMsg;
    private String mTitle;
    private String mMsg;
    private String inputMsg;
    private String positiveText;
    private String negativeText;
    private long millisecond = -1;
    private onPositiveInterface defaltPositiveListener = new onPositiveInterface() {
        @Override
        public void onPositive() {
            cancel();
        }
    };
    private onNegetiveInterface defaltNegetiveListener = new onNegetiveInterface() {
        @Override
        public void onNegetive() {
            cancel();
        }
    };
    private onPositiveInterface onPositiveListener = defaltPositiveListener;
    private onNegetiveInterface onNegativeListener = defaltNegetiveListener;
    private boolean isInput = false;//是否输入类型
    private boolean isNegativeBtnShow = true;//是否显示取消按钮
    private boolean isOutSideCancel = true;

    private Context context;
    private View mView;
    private Display display;
    private View viewLine;

    private CustomerDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_customer);
        tvTitle = (TextView) findViewById(R.id.dialog_customer_title);
        tvMsg = (TextView) findViewById(R.id.dialog_customer_content);
        edtInputMsg = (EditText) findViewById(R.id.dialog_customer_input_content);
        btnPositive = (TextView) findViewById(R.id.dialog_customer_confirm);
        btnNegative = (TextView) findViewById(R.id.dialog_customer_cancel);
        dialog_ll = (LinearLayout) findViewById(R.id.dialog_ll);
        btnCloseWin = (ImageView) findViewById(R.id.iv_dialog_close);
        viewLine = findViewById(R.id.view_line);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        dialog_ll.setLayoutParams(new LinearLayout.LayoutParams((int) (display.getWidth() * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void hideCancle() {
        btnNegative.setVisibility(View.GONE);
        viewLine.setVisibility(View.GONE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        lp.setMargins(0,0,0,0);
    }


    public void showCloseButton(){
        btnCloseWin.setVisibility(View.VISIBLE);
    }


    /**
     * 显示dialog
     */
    @Override
    public void show() {
        super.show();
        show(this);
    }

    private void show(final CustomerDialog mDialog) {
        if (TextUtils.isEmpty(mDialog.mTitle)) {
            mDialog.tvTitle.setVisibility(View.GONE);
        } else {
            mDialog.tvTitle.setText(mDialog.mTitle);
        }
        if (isInput) {
            mDialog.edtInputMsg.setVisibility(View.VISIBLE);
            mDialog.tvMsg.setVisibility(View.GONE);
        } else {
            mDialog.edtInputMsg.setVisibility(View.GONE);
            mDialog.tvMsg.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mDialog.mMsg)) {
                mDialog.tvMsg.setText(mDialog.mMsg);
            }
        }
        mDialog.btnCloseWin.setOnClickListener(view -> {
            cancel();
        });

        if (!mDialog.isNegativeBtnShow) {
            mDialog.btnNegative.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mDialog.btnPositive
                    .getLayoutParams();
            layoutParams.setMargins(150, layoutParams.topMargin, 150, layoutParams.bottomMargin);
            mDialog.btnPositive.setLayoutParams(layoutParams);
        } else {
            mDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.onNegativeListener.onNegetive();
                    cancel();
                }
            });
            if (!TextUtils.isEmpty(mDialog.negativeText)) {
                mDialog.btnNegative.setText(mDialog.negativeText);
            }
        }
        if (isInput && inputPositiveListener != null) {
            mDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputMsg = edtInputMsg.getText().toString();
                    mDialog.inputPositiveListener.onInputPositive(inputMsg);
                    cancel();
                }
            });
        } else {
            mDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.onPositiveListener.onPositive();
                    cancel();
                }
            });
        }
        if (!TextUtils.isEmpty(mDialog.positiveText)) {
            mDialog.btnPositive.setText(mDialog.positiveText);
        }
        if (mDialog.millisecond > 0) {
            tvMsg.setTextColor(context.getResources().getColor(R.color.red_ff6666));
            CountDownTimerUtil.getInstance().startCountdown(tvMsg, mDialog.millisecond / 1000);
        }
        if (!mDialog.isOutSideCancel) {
            mDialog.setCanceledOnTouchOutside(false);
        }
        //解决dilaog中EditText无法弹出输入的问题
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    public interface InputPositiveInterface {
        void onInputPositive(String inputMsg);
    }

    public interface onPositiveInterface {
        void onPositive();
    }

    public interface onNegetiveInterface {
        void onNegetive();
    }

    private InputPositiveInterface inputPositiveListener;//输入类型确定回调输入数据接口

    public static class Builder {

        private CustomerDialog mDialog;

        public Builder(Context context) {
            mDialog = new CustomerDialog(context);
        }

        public Builder setTitle(String title) {
            mDialog.mTitle = title;
            return this;
        }

        public Builder setMsg(String content) {
            mDialog.mMsg = content;
            return this;
        }

        public Builder inputContent(String inputContent) {
            mDialog.inputMsg = inputContent;
            return this;
        }

        /**
         * 设置确认按钮的回调
         *
         * @param onClickListener
         */
        public Builder setPositiveButton(onPositiveInterface onClickListener) {
            mDialog.onPositiveListener = onClickListener;
            return this;
        }

        /**
         * 设置确认按钮的回调
         *
         * @param btnText,onClickListener
         */
        public Builder setPositiveButton(String btnText, onPositiveInterface onClickListener) {
            mDialog.positiveText = btnText;
            mDialog.onPositiveListener = onClickListener;
            return this;
        }

        /**
         * 设置取消按钮的回掉
         *
         * @param onClickListener
         */
        public Builder setNegativeButton(onNegetiveInterface onClickListener) {
            mDialog.onNegativeListener = onClickListener;
            return this;
        }

        /**
         * 设置取消按钮的回调
         *
         * @param btnText,onClickListener
         */
        public Builder setNegativeButton(String btnText, onNegetiveInterface onClickListener) {
            mDialog.negativeText = btnText;
            mDialog.onNegativeListener = onClickListener;
            return this;

        }

        /**
         * 设置对话框被cancel对应的回调接口，cancel()方法被调用时才会回调该接口
         *
         * @param onCancelListener
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mDialog.setOnCancelListener(onCancelListener);
            return this;
        }

        /**
         * 设置对话框消失对应的回调接口，一切对话框消失都会回调该接口
         *
         * @param onDismissListener
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mDialog.setOnDismissListener(onDismissListener);
            return this;
        }

        /**
         * 设置是否显示取消按钮，默认显示
         *
         * @param isNegativeBtnShow
         */
        public Builder setNegativeBtnShow(boolean isNegativeBtnShow) {
            mDialog.isNegativeBtnShow = isNegativeBtnShow;
            return this;
        }

        /**
         * 设置是否显示取消按钮，默认显示
         *
         * @param isOutSideCancel
         */
        public Builder setOutSideCancel(boolean isOutSideCancel) {
            mDialog.isOutSideCancel = isOutSideCancel;
            return this;
        }

        /**
         * 是否是输入类型的dialog
         *
         * @param b
         * @return
         */
        public Builder setInput(boolean b) {
            mDialog.isInput = b;
            return this;
        }

        public Builder setInputPositiveListener(InputPositiveInterface inputPositiveListener) {
            mDialog.inputPositiveListener = inputPositiveListener;
            return this;
        }

        public Builder setCountDownTimer(long mills) {
            mDialog.millisecond = mills;
            return this;
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        public CustomerDialog create() {
            return mDialog;
        }
    }


}
