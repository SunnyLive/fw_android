package com.fengwo.module_websocket;

/**
 * @Author BLCS
 * @Time 2020/8/5 17:57
 */
public class Url {
    /*线上*/
    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String BASE_IM_URL_WENBO = BuildConfig.BASE_IM_URL_WENBO;
    public static final String BASE_IM_URL = BuildConfig.BASE_IM_URL;
    public static final String BASE_PAY_URL = BuildConfig.BASE_PAY_URL;

    /*测试*/
    public static  String TEST_BASE_URL = BuildConfig.BASE_URL;
    public static  String TEST_BASE_IM_URL_WENBO = BuildConfig.BASE_IM_URL_WENBO;
    public static  String TEST_BASE_IM_URL = BuildConfig.BASE_IM_URL;
    public static  String TEST_BASE_PAY_URL = BuildConfig.BASE_PAY_URL;

    public static void url(int pos){
        switch (pos){
            case 0: /*线上*/
                TEST_BASE_URL = BuildConfig.BASE_URL;
                TEST_BASE_IM_URL_WENBO = BuildConfig.BASE_IM_URL_WENBO;
                TEST_BASE_IM_URL = BuildConfig.BASE_IM_URL;
                TEST_BASE_PAY_URL = BuildConfig.BASE_PAY_URL;
                break;
            case 1: /*开发*/
                TEST_BASE_URL ="https://apiszdev.fengwohuyu.com/";
                TEST_BASE_IM_URL_WENBO ="wss://wswbszdev.fengwohuyu.com:90/im";
                TEST_BASE_IM_URL = "wss://wsszdev.fengwohuyu.com:88/hkim";
                TEST_BASE_PAY_URL = "https://appszdev.fengwohuyu.com/";
                break;
            case 2: /*测试1*/
                TEST_BASE_URL = "https://apitest.fengwohuyu.com/";
                TEST_BASE_IM_URL_WENBO ="wss://wswbtest.fengwohuyu.com/im";
                TEST_BASE_IM_URL = "wss://wstest.fengwohuyu.com/hkim";
                TEST_BASE_PAY_URL = "https://apptest.fengwohuyu.com/";
                break;
            case 3: /*测试2*/
                TEST_BASE_URL = "https://apisit01.fengwohuyu.com/";
                TEST_BASE_IM_URL_WENBO ="wss://wswbsit01.fengwohuyu.com/im";
                TEST_BASE_IM_URL = "wss://wssit01.fengwohuyu.com/hkim";
                TEST_BASE_PAY_URL = "https://appsit01.fengwohuyu.com/";
                break;
            case 4: /*测试3*/
                TEST_BASE_URL = "https://apisit02.fengwohuyu.com/";
                TEST_BASE_IM_URL_WENBO ="wss://wswbsit02.fengwohuyu.com/im";
                TEST_BASE_IM_URL = "wss://wssit02.fengwohuyu.com/hkim";
                TEST_BASE_PAY_URL = "https://appsit02.fengwohuyu.com/";
                break;
            default:
        }
    }
}
