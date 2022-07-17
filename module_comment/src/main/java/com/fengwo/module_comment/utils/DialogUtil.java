package com.fengwo.module_comment.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.fengwo.module_comment.R;

public class DialogUtil {


    /**
     * @param activity                    Context
     * @param title                       提示标题
     * @param msg                         提示内容
     * @param positiveText                确认
     * @param negativeText                取消
     * @param cancelableTouchOut          点击外部是否隐藏提示框
     * @param alertDialogBtnClickListener 点击监听
     */
    public static void showAlertDialog(Activity activity, String title, String msg,
                                       String positiveText, String negativeText, boolean
                                               cancelableTouchOut, final AlertDialogBtnClickListener
                                               alertDialogBtnClickListener) {
        if (activity == null || !ActivitysManager.getInstance().currentActivity().getClass().equals(activity.getClass())) {
            return;
        }
        try {
            View view = LayoutInflater.from(activity).inflate(R.layout.base_dialog, null);
            TextView mTitle = view.findViewById(R.id.tv_title);
            TextView mMessage = view.findViewById(R.id.tv_message);
            TextView positiveButton = view.findViewById(R.id.btn_positive);
            TextView negativeButton = view.findViewById(R.id.btn_negative);
            View line = view.findViewById(R.id.view_line);
            mTitle.setText(title);
            mMessage.setText(msg);
            positiveButton.setText(positiveText);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setView(view);
            builder.setCancelable(false);   //返回键dismiss
            //创建对话框
            AlertDialog dialog = builder.create();
            positiveButton.setOnClickListener(v -> {
                dialog.dismiss();
                if (null == alertDialogBtnClickListener) return;
                alertDialogBtnClickListener.clickPositive();

            });
            if (TextUtils.isEmpty(negativeText)) {
                negativeButton.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            } else {
                negativeButton.setText(negativeText);
                negativeButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    if (null == alertDialogBtnClickListener) return;
                    alertDialogBtnClickListener.clickNegative();

                });
            }
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去掉圆角背景背后的棱角
            dialog.setCanceledOnTouchOutside(cancelableTouchOut);   //失去焦点dismiss
            dialog.show();
        } catch (Exception e) {
            L.e("===Exception");
        }
    }

    /**
     * @param activity                    Context
     * @param title                       提示标题
     * @param msg                         提示内容
     * @param positiveText                确认
     * @param negativeText                取消
     * @param cancelableTouchOut          点击外部是否隐藏提示框
     * @param alertDialogBtnClickListener 点击监听
     */
    public static AlertDialog getAlertDialog(Activity activity, String title, String msg,
                                             String positiveText, String negativeText, boolean
                                                     cancelableTouchOut, final AlertDialogBtnClickListener
                                                     alertDialogBtnClickListener) {
        if (!activity.getWindow().isActive()) return null;
        View view = LayoutInflater.from(activity).inflate(R.layout.base_dialog, null);
        TextView mTitle = view.findViewById(R.id.tv_title);
        TextView mMessage = view.findViewById(R.id.tv_message);
        TextView positiveButton = view.findViewById(R.id.btn_positive);
        TextView negativeButton = view.findViewById(R.id.btn_negative);
        View line = view.findViewById(R.id.view_line);
        mTitle.setText(title);
        mMessage.setText(msg);
        positiveButton.setText(positiveText);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(false);   //返回键dismiss
        //创建对话框
        AlertDialog dialog = builder.create();
        positiveButton.setOnClickListener(v -> {
            dialog.dismiss();
            if (null == alertDialogBtnClickListener) return;
            alertDialogBtnClickListener.clickPositive();

        });
        if (TextUtils.isEmpty(negativeText)) {
            negativeButton.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            negativeButton.setText(negativeText);
            negativeButton.setOnClickListener(v -> {
                dialog.dismiss();
                if (null == alertDialogBtnClickListener) return;
                alertDialogBtnClickListener.clickNegative();

            });
        }
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去掉圆角背景背后的棱角
        dialog.setCanceledOnTouchOutside(cancelableTouchOut);   //失去焦点dismiss
        return dialog;
    }

    public static void showAlertDialog(Activity activity, String title, String msg,
                                       String positiveText, final AlertDialogBtnClickListener
                                               alertDialogBtnClickListener) {

        showAlertDialog(activity, title, msg, positiveText, "", false, alertDialogBtnClickListener);
    }

    public static AlertDialog showNoMoneyDialog(Activity activity, AlertDialogBtnClickListener listener) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_nomoney, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);   //返回键dismiss
        AlertDialog dialog = builder.create();
        view.findViewById(R.id.btn_cancle).setOnClickListener(view1 -> {
            dialog.dismiss();
            listener.clickNegative();
        });
        view.findViewById(R.id.btn_submit).setOnClickListener(view12 -> {
            dialog.dismiss();
            listener.clickPositive();
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去掉圆角背景背后的棱角
        dialog.setCanceledOnTouchOutside(false);   //失去焦点dismiss
        dialog.show();
        return dialog;
    }

    /**
     * @param activity                    Context
     * @param title                       提示标题
     * @param msg                         提示内容
     * @param positiveText                确认
     * @param negativeText                取消
     * @param cancelableTouchOut          点击外部是否隐藏提示框
     * @param alertDialogBtnClickListener 点击监听
     */
    public static AlertDialog getWenboAlertDialog(Activity activity, String title, String msg,
                                                  String positiveText, String negativeText, boolean
                                                          cancelableTouchOut, final AlertDialogBtnClickListener
                                                          alertDialogBtnClickListener) {
        View view = LayoutInflater.from(activity).inflate(R.layout.base_wenbo_dialog, null);
        TextView mTitle = view.findViewById(R.id.tv_title);
        TextView mMessage = view.findViewById(R.id.tv_message);
        TextView positiveButton = view.findViewById(R.id.btn_positive);
        TextView negativeButton = view.findViewById(R.id.btn_negative);
        View line = view.findViewById(R.id.view_line);
        mTitle.setText(title);
        mMessage.setText(msg);
        positiveButton.setText(positiveText);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(false);   //返回键dismiss
        //创建对话框
        AlertDialog dialog = builder.create();
        positiveButton.setOnClickListener(v -> {
            alertDialogBtnClickListener.clickPositive();
            dialog.dismiss();
        });
        if (TextUtils.isEmpty(negativeText)) {
            negativeButton.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            negativeButton.setText(negativeText);
            negativeButton.setOnClickListener(v -> {
                alertDialogBtnClickListener.clickNegative();
                dialog.dismiss();
            });
        }
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去掉圆角背景背后的棱角
        dialog.setCanceledOnTouchOutside(cancelableTouchOut);   //失去焦点dismiss
        return dialog;
    }

    public interface AlertDialogBtnClickListener {
        void clickPositive();

        void clickNegative();
    }
}