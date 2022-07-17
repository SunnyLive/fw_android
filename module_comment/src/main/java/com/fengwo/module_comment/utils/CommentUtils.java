package com.fengwo.module_comment.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;

import com.fengwo.module_comment.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 创建人： min
 * 创建时间： 2017/6/20.
 */

public class CommentUtils {

    public static boolean isOpenFlirt = true;//是否开启I撩模块

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isListEmpty(List l) {
        if (l == null || l.size() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean isPhone(String mobile) {
        if (!TextUtils.isEmpty(mobile) && mobile.length() == 11) {
            return true;
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 拼接字符串  用","隔开
     */
    public static StringBuilder appendString(StringBuilder builder, String str) {
        if (TextUtils.isEmpty(builder.toString())) {
            builder.append(str);
        } else {
            if (builder.toString().contains(str)) {
                int i = builder.toString().indexOf(str);
                if (i > 0) {
                    builder.toString().replace("," + str, "");
                } else {
                    builder.delete(0, builder.length());
                }
            } else {
                builder.append("," + str);
            }
        }
        return builder;
    }

    /**
     * 设置性别与年龄
     *
     * @param context
     * @param anchorId
     * @param sex
     * @param age
     * @param view
     */
    public static void setSexAndAge(Context context, int anchorId, int sex, int age, TextView view) {
        Drawable drawableBoy = context.getResources().getDrawable(
                R.drawable.ic_boy);
        Drawable drawableGirl = context.getResources().getDrawable(
                R.drawable.ic_girl);
        // sex   2女   1 男   0 不显示
        //如果sex=0  age = 0 就不显示性别年龄
        if ((sex == 3 || sex == 0) && age <= 0) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
        //隐藏性别  展示年龄
        view.setBackgroundResource(sex == 1 ? R.drawable.shape_corner_boy : R.drawable.shape_corner_girl);
        view.setText(age > 0 ? age + "" : "");
        view.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null);
        if (sex != 0) {
            //展示性别
            view.setCompoundDrawablesWithIntrinsicBounds(sex == 1 ? drawableBoy : drawableGirl,
                    null, null, null);
        }
    }


    /**
     * 设置性别与年龄  不改背景
     *
     * @param context
     * @param anchorId
     * @param sex
     * @param age
     * @param view
     */
    public static void setSexAndAgeInd(Context context, int anchorId, int sex, int age, TextView view) {
        Drawable drawableboy = context.getResources().getDrawable(
                R.drawable.ic_boy);
        Drawable drawableGirl = context.getResources().getDrawable(
                R.drawable.ic_girl);
        view.setVisibility(View.VISIBLE);
        if (sex == 2 && age == 0) {
            view.setVisibility(View.GONE);
            return;
        }
        if (anchorId > 0) {//是主播 有默认性别 女 年龄20
            view.setText(age > 0 ? age + "" : "20");
            //    view.setBackgroundResource(sex == 1 ? R.drawable.shape_corner_boy : R.drawable.shape_corner_girl);
            view.setCompoundDrawablesWithIntrinsicBounds(sex == 1 ? drawableboy : drawableGirl,
                    null, null, null);
        } else {//用户  没有默认值
            view.setText(age > 0 ? age + "" : "");
//            view.setPadding((int) context.getResources().getDimension(R.dimen.dp_3), (int) context.getResources().getDimension(R.dimen.dp_2), age > 0 ? (int) context.getResources().getDimension(R.dimen.dp_3) : 0, (int) context.getResources().getDimension(R.dimen.dp_2));
            //   view.setBackgroundResource(sex == 1 ? R.drawable.shape_corner_boy : R.drawable.shape_corner_girl);
            if (sex == 0) {
                view.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null, null);
                view.setVisibility(age > 0 ? View.VISIBLE : View.GONE);
            } else if (sex == 1) {
                view.setCompoundDrawablesWithIntrinsicBounds(drawableboy,
                        null, null, null);
            } else {
                view.setCompoundDrawablesWithIntrinsicBounds(drawableGirl,
                        null, null, null);
            }
        }
    }


    /**
     * 设置性别与年龄
     *
     * @param context
     * @param anchorId
     * @param sex
     * @param age
     * @param view
     */
    public static void setSexAndAge2(Context context, int anchorId, int sex, int age, TextView view) {
        Drawable drawableboy = context.getResources().getDrawable(
                R.drawable.ic_boy_blue);
        Drawable drawableGirl = context.getResources().getDrawable(
                R.drawable.ic_girl_red);
        view.setVisibility(View.VISIBLE);
        if (sex == 0 && age == 0) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setBackgroundResource(R.drawable.shape_white_coner);
        if (anchorId > 0) {//是主播 有默认性别 女 年龄20
            view.setText(age > 0 ? age + "" : "20");
            if (sex == 1) {
                view.setTextColor(Color.parseColor("#63A5FF"));
            } else {
                view.setTextColor(Color.parseColor("#FE3C9C"));
            }
            view.setCompoundDrawablesWithIntrinsicBounds(sex == 1 ? drawableboy : drawableGirl,
                    null, null, null);
        } else {//用户  没有默认值
            view.setText(age > 0 ? age + "" : "");
//            view.setPadding((int) context.getResources().getDimension(R.dimen.dp_3), (int) context.getResources().getDimension(R.dimen.dp_2), age > 0 ? (int) context.getResources().getDimension(R.dimen.dp_3) : 0, (int) context.getResources().getDimension(R.dimen.dp_2));
            if (sex == 0) {
                view.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null, null);
                view.setVisibility(age > 0 ? View.VISIBLE : View.GONE);
            } else if (sex == 1) {
                view.setTextColor(Color.parseColor("#63A5FF"));
                view.setCompoundDrawablesWithIntrinsicBounds(drawableboy,
                        null, null, null);
            } else {
                view.setTextColor(Color.parseColor("#FE3C9C"));
                view.setCompoundDrawablesWithIntrinsicBounds(drawableGirl,
                        null, null, null);
            }
        }
    }


    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }


    public final static DecimalFormat df = new DecimalFormat("###.##");

    public static String formatDiamond(int diamond) {
        if (diamond < 10000) return String.valueOf(diamond);
        try {
            BigDecimal divide = new BigDecimal(diamond)
                    .divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP);
            return df.format(divide) + "W";
        } catch (Exception e) {
            return String.valueOf(diamond);
        }
    }

    private static final int MIN_CLICK_DELAY_TIME = 400;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

}
