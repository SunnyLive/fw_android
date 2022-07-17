package com.fengwo.module_login.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.OkhttpUtil;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_login.R;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.Url;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

public class SplashActivity extends Activity implements ITXVodPlayListener {
    CountBackUtils utils;

    ImageView ivSplash;

    TextView tvJump;

    TXCloudVideoView cardTxvideoView;

    private TXVodPlayer mVodPlayer;
    private TXVodPlayConfig mVodPlayConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (UserManager.getInstance().getUser() != null) {
            boolean isNightMode = (boolean) SPUtils1.get(this, "IS_MODE_NIGHT" + UserManager.getInstance().getUser().id, false);
            DarkUtil.setDarkTheme(isNightMode ? MODE_NIGHT_YES : MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_splash);
        ivSplash = findViewById(R.id.iv_splash);
        tvJump = findViewById(R.id.tv_jump);
        cardTxvideoView = findViewById(R.id.card_txvideo_view);
        utils = new CountBackUtils();
        UserManager.init(this);
        initPlay();
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMain();
            }
        });

//        Intent i = new Intent(this, MainService.class);
//        startService(i);
    }



    /**
     * 初始化播放器
     */
    public void initPlay() {
        if (mVodPlayer != null)
            return;
        mVodPlayer = new TXVodPlayer(this);
//        SuperPlayerGlobalConfig config = SuperPlayerGlobalConfig.getInstance();
        mVodPlayConfig = new TXVodPlayConfig();
        mVodPlayConfig.setCacheFolderPath(Environment.getExternalStorageDirectory().getPath() + "/.nomedia");
//        mVodPlayConfig.setMaxCacheItems(config.maxCacheItem);
        mVodPlayer.setConfig(mVodPlayConfig);
        mVodPlayer.setLoop(false);
        mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mVodPlayer.setVodListener(this);

//        mVodPlayer.enableHardwareDecode(config.enableHWAcceleration);
    }
    /**
     * 播放视频
     */
    public void startPlay(String url, TXCloudVideoView txCloudVideoView) {
        if (mVodPlayer == null) {
            initPlay();
        }
//        txCloudVideoView.updateVideoViewSize(ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenHeight(mContext));
        mVodPlayer.setPlayerView(txCloudVideoView);
        mVodPlayer.setAutoPlay(true);
        mVodPlayer.setVodListener(this);
        mVodPlayer.startPlay(url);
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if (mVodPlayer != null) {
            mVodPlayer.setVodListener(null);
            mVodPlayer.stopPlay(false);
            mVodPlayer = null;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getServiceKey("start_pages");
    }



    private void gotoMain() {
        utils.destory();

        if (null != UserManager.getInstance().getUser()) {
            ARouter.getInstance().build(ArouterApi.MAIN_MAIN_ACTIVITY).navigation();
        } else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
        stopPlay();
    }


    private void getServiceKey(String key) {
//        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url.TEST_BASE_URL + "api/base/" + key)
//                .url(BuildConfig.DEBUG?Url.TEST_BASE_URL: Url.BASE_URL + "api/base/" + key)
                .get()
                .build();
        Call call = OkhttpUtil.getInstance().okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                KLog.e("splash", e.getMessage());
                gotoMain();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                KLog.e("splash", result);
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    JSONObject splashInfo = resultJSON.getJSONObject("data");
//                    JSONObject splashData = new JSONObject(splashInfo);
                    String pic = splashInfo.getString("img");
                    String time = splashInfo.getString("time");
                    String jumpurl = splashInfo.getString("url");
                    String title = splashInfo.getString("title");
                    int openType = splashInfo.getInt("openType");
                    String video = "";
                    if (splashInfo.has("video")){
                        video = splashInfo.getString("video");
                    }
                    String finalVideo = video;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(finalVideo)){
                                ivSplash.setVisibility(View.GONE);
                                startPlay(finalVideo,cardTxvideoView);
                                cardTxvideoView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!TextUtils.isEmpty(jumpurl)) {
                                            if (openType == 1) {//链接打开方式，1 app内，2 外部浏览器
                                                BrowserActivity.start(SplashActivity.this, title, jumpurl);
                                            } else {
                                                Intent intent = new Intent();
                                                intent.setAction("android.intent.action.VIEW");
                                                Uri content_url = Uri.parse(jumpurl);
                                                intent.setData(content_url);
                                                startActivity(intent);
                                            }
                                            utils.destory();
                                        }
                                    }
                                });
                            }else {
                                cardTxvideoView.setVisibility(View.GONE);
                                ImageLoader.loadSplash(ivSplash, pic);
                                ivSplash.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!TextUtils.isEmpty(jumpurl)) {
                                            if (openType == 1) {//链接打开方式，1 app内，2 外部浏览器
                                                BrowserActivity.start(SplashActivity.this, title, jumpurl);
                                            } else {
                                                Intent intent = new Intent();
                                                intent.setAction("android.intent.action.VIEW");
                                                Uri content_url = Uri.parse(jumpurl);
                                                intent.setData(content_url);
                                                startActivity(intent);
                                            }
                                            utils.destory();
                                        }
                                    }
                                });
                            }
                        }
                    });
                    utils.countBack(Integer.parseInt(time) + 1, new CountBackUtils.Callback() {
                        @Override
                        public void countBacking(long time) {
                            if (tvJump.getVisibility() != View.VISIBLE) {
                                tvJump.setVisibility(View.VISIBLE);

                            }
                            tvJump.setText("跳过 " + (time));
                        }

                        @Override
                        public void finish() {
                            gotoMain();
                        }
                    });
                } catch (Exception e) {
                    gotoMain();
                    e.printStackTrace();
                }

            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        utils.destory();
        if (mVodPlayer != null) {
            mVodPlayer.setVodListener(null);
            mVodPlayer.stopPlay(false);
            mVodPlayer = null;
        }
    }

    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
        if (event == TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED) { //视频播放开始
        } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
        } else if (event == TXLiveConstants.PLAY_ERR_HLS_KEY
                || event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {// 播放点播文件失败
        }
        if (event < 0
//                && event != TXLiveConstants.PLAY_ERR_VOD_LOAD_LICENSE_FAIL
                && event != TXLiveConstants.PLAY_ERR_HLS_KEY
//                && event != TXLiveConstants.PLAY_ERR_VOD_UNSUPPORT_DRM
                && event != TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
            mVodPlayer.stopPlay(true);
        }
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

    }


}
