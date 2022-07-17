package com.fengwo.module_comment.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.widget.CustomerDialog;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CommonUtil {

    /**
     * textView 添加中划线
     */
    public static void lineAction(TextView textView) {
        textView.getPaint().setAntiAlias(true);//抗锯齿
        // textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        // textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);// 设置中划线并加清晰
    }

    /**
     * textView 添加下划线
     */
    public static void lineUnder(TextView textView) {
        textView.getPaint().setAntiAlias(true);//抗锯齿
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * json字符串转Map
     */
    public static Map<String, Object> jsonToMap(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static void setViewPagerScroll(ViewPager viewPager, Interpolator interpolator, int duration) {
//        try {
//            Field mScroller = null;
//            mScroller = ViewPager.class.getDeclaredField("mScroller");
//            mScroller.setAccessible(true);
//            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), interpolator);
//            scroller.setmDuration(duration);
//            mScroller.set(viewPager, scroller);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 显示/关闭软键盘
     */
    public static void showOrHideKeyboard(Activity activity, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (isShow) {
            if (activity.getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(activity.getCurrentFocus(), 0);
            }
        } else {
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 复制文字
     *
     * @param context
     * @param text
     */
    public static void copy(Context context, String text, boolean toast) {
        if (TextUtils.isEmpty(text)) return;
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("text", text));
        if (toast) {
            ToastUtils.showShort(context, "复制成功");
        }
    }

    /**
     * 更新系统图库
     *
     * @param imgPath
     */
    public static void sendImageChangeBroadcast(Context context, String imgPath) {
        if (TextUtils.isEmpty(imgPath)) return;
        File file = new File(imgPath);
        if (file.exists() && file.isFile()) {
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
        }
    }

    /**
     * 获取手机相册路径
     *
     * @return
     */
    public static String getCameraPath() {
        String cameraPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/DCIM/Camera/";
        File file = new File(cameraPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return cameraPath;
    }

    public static void hideBottomUIMenu(Activity context) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = context.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View decorView = context.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View
                    .SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 打招呼判断逻辑
     * @param context
     * @param userInfo
     * @param myUserInfo
     * @return
     */
    public static boolean call(Context context,UserInfo userInfo,UserInfo myUserInfo){
        if (userInfo == null) return true;
        if (userInfo.getId() == myUserInfo.id) {
            ToastUtils.showShort(context,"不能和自己打招呼哦~");
            return true;
        }

        if (myUserInfo.myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NO
                || myUserInfo.myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NULL) {
            CustomerDialog mcd = new CustomerDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMsg("您未进行实名认证，\n认证后可使用打招呼")
                    .setPositiveButton("去认证", () -> {
                        ArouteUtils.toRealIdCard(myUserInfo.myIdCardWithdraw);
                    }).create();
            mcd.show();
            return true;
        }else if (myUserInfo.myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_ING){
            ToastUtils.showShort(context,"实名认证中,请您耐心等待", Gravity.CENTER);
            return true;
        }
        if (userInfo.isAttention == 2) {//相互关注
            ArouteUtils.toChatSingleActivity(myUserInfo.id + "",
                    userInfo.id + "", userInfo.nickname, userInfo.headImg);
            return true;
        }
        return false;
    }
}
