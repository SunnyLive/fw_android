package com.fengwo.module_vedio.mvp.ui.activity.shortvideo;

import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_vedio.R2;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.liteav.demo.play.v3.SuperPlayerVideoId;
import com.tencent.liteav.demo.player.server.VideoDataMgr;
import com.tencent.liteav.demo.player.superplayer.VideoModel;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/31
 */
public abstract class BaseSuperPlayerActivity<V extends MvpView, P extends BasePresenter<V>> extends BaseMvpActivity<V, P> implements SuperPlayerView.OnSuperPlayerViewCallback {

    private static final String TAG = "BaseSuperPlayerActivity";

    @BindView(R2.id.superVodPlayerView)
    SuperPlayerView mSuperPlayerView;
    @BindView(R2.id.layout_player)
    RelativeLayout layoutPlayer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        mSuperPlayerView.setPlayerViewCallback(this);
        changeAppBrightness(getSystemBrightness());
    }

    protected void playVideo(String url) {
        VideoModel model = new VideoModel();
        model.videoURL = url != null ? url : "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4";
        model.appid = Constants.TX_APP_ID;
        playVideoModel(model);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mSuperPlayerView.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAY) {
            Log.i(TAG, "onResume state :" + mSuperPlayerView.getPlayState());
            mSuperPlayerView.onResume();
            if (mSuperPlayerView.getPlayMode() == SuperPlayerConst.PLAYMODE_FLOAT) {
                mSuperPlayerView.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause state :" + mSuperPlayerView.getPlayState());
        if (mSuperPlayerView.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
            mSuperPlayerView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuperPlayerView.release();
        if (mSuperPlayerView.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
            mSuperPlayerView.resetPlayer();
        }
        VideoDataMgr.getInstance().setGetVideoInfoListListener(null);
    }

    private void playVideoModel(VideoModel videoModel) {
        final SuperPlayerModel superPlayerModelV3 = new SuperPlayerModel();
        superPlayerModelV3.appId = videoModel.appid;

        if (!TextUtils.isEmpty(videoModel.videoURL)) {
            superPlayerModelV3.title = videoModel.title;
            superPlayerModelV3.url = videoModel.videoURL;
            superPlayerModelV3.qualityName = "原画";

            superPlayerModelV3.multiURLs = new ArrayList<>();
            if (videoModel.multiVideoURLs != null) {
                for (VideoModel.VideoPlayerURL modelURL : videoModel.multiVideoURLs) {
                    superPlayerModelV3.multiURLs.add(new SuperPlayerModel.SuperPlayerURL(modelURL.url, modelURL.title));
                }
            }
        } else if (!TextUtils.isEmpty(videoModel.fileid)) {
            superPlayerModelV3.videoId = new SuperPlayerVideoId();
            superPlayerModelV3.videoId.fileId = videoModel.fileid;
        }
        mSuperPlayerView.playWithModel(superPlayerModelV3);
    }

    @Override
    public void onStartFullScreenPlay() {
    }

    @Override
    public void onStopFullScreenPlay() {

    }

    @Override
    public void onClickFloatCloseBtn() {
        // 点击悬浮窗关闭按钮，那么结束整个播放
        mSuperPlayerView.resetPlayer();
        finish();
    }

    @Override
    public void onClickSmallReturnBtn() {
        // 点击小窗模式下返回按钮，开始悬浮播放
//        showFloatWindow();
        mSuperPlayerView.resetPlayer();
        finish();
    }

    @Override
    public void onStartFloatWindowPlay() {
        // 开始悬浮播放后，直接返回到桌面，进行悬浮播放
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);
    }

    /**
     * 悬浮窗播放
     */
    protected void showFloatWindow() {
        if (mSuperPlayerView.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAY) {
            mSuperPlayerView.requestPlayMode(SuperPlayerConst.PLAYMODE_FLOAT);
        } else {
            mSuperPlayerView.resetPlayer();
            finish();
        }
    }

    @Override
    protected boolean getImmersionBar() {
        return false;
    }

    private int getSystemBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    public void changeAppBrightness(int brightness) {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }

    @Override
    public void onBackPressed() {
        if (mSuperPlayerView.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN){
            mSuperPlayerView.mVodController.onBackPress(SuperPlayerConst.PLAYMODE_FULLSCREEN);
        }else {
            super.onBackPressed();
        }
    }
}
