package com.fengwo.module_comment.manage;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.L;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.AuthUIControlClickListener;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.PreLoginResultListener;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;

/**
 * @Des 一键登录
 * @Author BLCS
 * @Time 2020/3/9 12:22
 * https://help.aliyun.com/document_detail/144231.html?spm=a2c4g.11186623.6.557.3e142dd4obdAsa#h2-5-16-16
 */
public class AutoLoginManage {
    private PhoneNumberAuthHelper mAlicomAuthHelper;
    private String KEY_AUTO_LOGIN = "2jyViD9Ky42krbWDOtLsMtkB+42io1lBFJsxFjHGlqoZkmHr0LjFWrPMhAq0vziiUj8qKd8q8Q93cAdkcI2E3stN75Di1m2Skl+w5RI6kBAjgDJhIqOpe76HwcU2wTqavj/M7MaEhoPVSIpy+/mfQHfNdwW/SLPIJJWq+3I7L9jNkyeYp0baZRSClS0pENI1dHt2ihNzyU79Bx82N5t7w52mYxr3QFv/IWp05CKcyuG1sz6fKZGYIqbDifeUJ1A76O7tvNwLrQXxz6XZOaqmRpsIVRKAKLd7iQ1ZgFgurhU=";
    private static final int TIME_OUT = 5000;
    private boolean checkRet;
    private static AutoLoginManage autoLoginManage;
    private Context context;
    private boolean isClick = false; //过滤初始化失败监听： 初始化的时候 失败会调用 到失败监听

    public AutoLoginManage(Context context) {
        //拿app的上下文 防止内存泄漏
        this.context = context.getApplicationContext();
    }

    public static AutoLoginManage getInstance(Context context) {
        if (autoLoginManage == null) {
            synchronized (AutoLoginManage.class) {
                if (autoLoginManage == null) {
                    autoLoginManage = new AutoLoginManage(context);
                }
            }
        }
        return autoLoginManage;
    }

    /**
     * 初始化
     *
     * @param listener
     */
    public AutoLoginManage init(OnTokenResultListener listener) {
        // 1.init get token callback Listener
        TokenResultListener mTokenListener = new TokenResultListener() {
            @Override
            public void onTokenSuccess(final String ret) {
                try {
                    TokenRet tokenRet = JSON.parseObject(ret, TokenRet.class);
                    if (tokenRet != null && !("600001").equals(tokenRet.getCode())) {
                        if (listener != null && mAlicomAuthHelper != null) {
                            listener.getTokenSuccess(tokenRet.getToken());
                            mAlicomAuthHelper.quitLoginPage();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isClick = false;
                }
            }

            @Override
            public void onTokenFailed(final String ret) {
                L.e("======ret "+ret);
                try {
                    TokenRet tokenRet = JSON.parseObject(ret, TokenRet.class);
                    if (listener != null && mAlicomAuthHelper != null && isClick) {
                        listener.getTokenFailed(tokenRet.getMsg());
                        mAlicomAuthHelper.hideLoginLoading();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isClick = false;
                    if (null != listener)
                        listener.hideLoading();
                }
            }
        };

        // 2. 获取认证实例
        mAlicomAuthHelper = PhoneNumberAuthHelper.getInstance(context, mTokenListener);
        //设置SDK秘钥
        mAlicomAuthHelper.setAuthSDKInfo(KEY_AUTO_LOGIN);
        //检查终端是否支持号码认证
        checkRet = mAlicomAuthHelper.checkEnvAvailable();
        mAlicomAuthHelper.setAuthListener(mTokenListener);

        if (!checkRet) {
//            Toast.makeText(context,"当前网络不支持，请检测蜂窝网络后重试",Toast.LENGTH_SHORT).show();
        }

        //设置日志输出是否开启
        mAlicomAuthHelper.setLoggerEnable(BuildConfig.DEBUG);

        /**
         * 控件点击事件回调
         */
        mAlicomAuthHelper.setUIClickListener(new AuthUIControlClickListener() {
            @Override
            public void onClick(String code, Context context, JSONObject jsonObj) {
                L.e("======code "+code);
                L.e("======jsonObj "+jsonObj);
                if (TextUtils.isEmpty(code)) return;
                if (onUIClickListener == null) return;
                switch (code) {
                    case "700000"://点击返回，⽤户取消免密登录
                        onUIClickListener.onClickBack(jsonObj);
                        break;
                    case "700001"://点击切换按钮，⽤户取消免密登录
                        onUIClickListener.onClickToggle(jsonObj);
                        break;
                    case "700002"://点击登录按钮事件
                        onUIClickListener.onClickLogin(jsonObj);
                        break;
                    case "700003"://点击check box事件
                        onUIClickListener.onClickCheck(jsonObj);
                        break;
                    case "700004"://点击协议富文本文字事件
                        onUIClickListener.onClickText(jsonObj);
                        break;
                }
            }
        });

        /**
         * 一键登录预取号
         */
        mAlicomAuthHelper.accelerateLoginPage(TIME_OUT, new PreLoginResultListener() {
            @Override
            public void onTokenSuccess(final String vendor) {
                if (onAccelerateListener == null) return;
                onAccelerateListener.onTokenSuccess(vendor);
            }

            @Override
            public void onTokenFailed(final String vendor, final String ret) {
                if (onAccelerateListener == null) return;
                onAccelerateListener.onTokenFailed(vendor, ret);
            }
        });
        configLoginTokenLand();
        return this;
    }

    /**
     * 一键登录接口
     */
    public void login() {
        if (mAlicomAuthHelper == null) return;
        isClick = true;
        mAlicomAuthHelper.getLoginToken(context, TIME_OUT);
    }

    /**
     * 退出登录页面
     */
    public void quitLoginPage() {
        if (mAlicomAuthHelper == null) return;
        mAlicomAuthHelper.quitLoginPage();
   //     mAlicomAuthHelper.clearPreInfo();
    }

    /**
     * 退出登录授权页时，授权页的loading消失由APP控制
     */
    public void hide() {
        if (mAlicomAuthHelper == null) return;
        mAlicomAuthHelper.hideLoginLoading();
    }

    private void configLoginTokenLand() {
        mAlicomAuthHelper.setAuthUIConfig(new AuthUIConfig.Builder()
                .setAppPrivacyOne("用户协议,隐私条款", "https://app.fwhuyu.com/agreement")
                .setPrivacyBefore("注册/登录表示同意")
                .setCheckboxHidden(false)
                .setAppPrivacyColor(Color.WHITE, Color.WHITE)
                //.setStatusBarColor(Color.TRANSPARENT)
                .setStatusBarColor(Color.parseColor("#FFAA4A"))
                //.setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                .setLogBtnBackgroundPath("icon_login_bt_bg")
                .setLogBtnHeight(40)
                .setLogBtnWidth(240)
                .setLogBtnTextSize(18)
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                .setSloganText("我的网红生活")
                .setSloganTextSize(24)
                .setPrivacyState(true)//默认勾选
                .setUncheckedImgPath("pic_login_wx")
                .setCheckedImgPath("pic_login_yx")
                .setSloganTextColor(context.getResources().getColor(R.color.text_white))
                .setSwitchAccTextColor(context.getResources().getColor(R.color.text_white))
                .setSwitchAccTextSize(12)
                .setNumberColor(context.getResources().getColor(R.color.text_white))
                .setNumberSize(18)
                .setSwitchAccText("切换其他手机号")
                .setPageBackgroundPath("icon_login_bg_new")
                .setLogoImgPath("icon_login_logo")
                .setLogoHeight(100)
                .setLogoWidth(100)
                .setLogoOffsetY(88)//logo的偏移量
                .setSloganOffsetY(198)//slogan图标的偏移量
                .setNumFieldOffsetY(320) // 手机号码偏移量
                .setLogBtnOffsetY(366)//登录按钮偏移位置
                .setSwitchOffsetY(436)//切换其他手机号偏移位置
                .setNavColor(Color.TRANSPARENT)
                .setNavText("")
                .setNavReturnImgPath("ic_back_white")
                .setWebViewStatusBarColor(Color.TRANSPARENT)
                .setWebNavColor(Color.TRANSPARENT)
                .setWebNavTextColor(context.getResources().getColor(R.color.black_333333))
                .create());

    }


    /**
     * 获取 token 监听
     */
    public OnTokenResultListener onTokenResultListener;

    public interface OnTokenResultListener {
        void getTokenSuccess(String token);

        void getTokenFailed(String msg);

        void hideLoading();
    }

    /**
     * 预取号监听
     */
    public OnAccelerateListener onAccelerateListener;

    public interface OnAccelerateListener {
        void onTokenSuccess(String msg);

        void onTokenFailed(String msg, String ret);
    }

    public AutoLoginManage addAccelerateListener(OnAccelerateListener listener) {
        this.onAccelerateListener = listener;
        return this;
    }

    /**
     * 控件点击事件回调
     */
    public OnUIClickListener onUIClickListener;

    public interface OnUIClickListener {
        void onClickBack(JSONObject jsonObj);

        void onClickToggle(JSONObject jsonObj);

        void onClickLogin(JSONObject jsonObj);

        void onClickCheck(JSONObject jsonObj);

        void onClickText(JSONObject jsonObj);
    }

    public AutoLoginManage addUIClickListener(OnUIClickImplements listener) {
        this.onUIClickListener = listener;
        return this;
    }

    public static abstract class OnUIClickImplements implements OnUIClickListener {

        @Override
        public void onClickBack(JSONObject jsonObj) {
            /**
             * 点击返回，⽤户取消免密登录
             */
        }

        @Override
        public void onClickLogin(JSONObject jsonObj) {
            /**
             * 点击登录按钮事件
             */
        }

        @Override
        public void onClickCheck(JSONObject jsonObj) {
            /**
             * 点击check box事件
             */
        }

        @Override
        public void onClickText(JSONObject jsonObj) {
            /**
             * 点击协议富文本文字事件
             */
        }
    }

}
