package com.fengwo.module_comment.base.live;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.fengwo.module_comment.utils.L;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;


/**
 * 播放器管理类
 */
public class PlayerManager {

    private static PlayerManager instance;
    private final Context mContext;
    private TXLivePlayer mLivePlayer = null;  // 播放器控制器
    private ITXLivePlayListener mainListener;  // 播放器状态监听
    private boolean mHWDecode = false;
    private int mCurrentRenderMode;
    private int mCurrentRenderRotation;

    protected int mActivityType = 2;
    protected int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    public static final int ACTIVITY_TYPE_PUBLISH = 1;
    public static final int ACTIVITY_TYPE_LIVE_PLAY = 2;
    public static final int ACTIVITY_TYPE_VOD_PLAY = 3;
    public static final int ACTIVITY_TYPE_LINK_MIC = 4;
    public static final int ACTIVITY_TYPE_REALTIME_PLAY = 5;

    private PlayerManager(Context context) {
        mContext = context.getApplicationContext();
        init();
    }

    public static PlayerManager getInstance(Context context) {
        synchronized (PlayerManager.class) {
            if (instance == null) {
                synchronized (PlayerManager.class) {
                    instance = new PlayerManager(context);
                }
            }
        }
        return instance;
    }

    private void init() {
        mCurrentRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
        mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
        mainListener = new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int event, Bundle param) {
                String playEventLog = "score event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
//                    onPlayBegin();
//                    L.d("AutoMonitor", "PlayFirstRender,cost=" + (System.currentTimeMillis() - mStartPlayTS));
                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    L.d("txplayevent", "PLAY_ERR_NET_DISCONNECT");
//                    onPlayEnd();
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                    L.d("txplayevent", "PLAY_EVT_PLAY_LOADING");
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                    L.d("txplayevent", "PLAY_EVT_RCV_FIRST_I_FRAME");
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                    L.d("txplayevent", "PLAY_EVT_CHANGE_RESOLUTION");
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_ROTATION) {
                    L.d("txplayevent", "PLAY_EVT_CHANGE_ROTATION");
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        };
    }

    /**
     * 播放直播
     * @param url 播放地址
     * @param listener 播放器回调(PlayerEvent, NetStatus)
     * @return 返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
     */
    public boolean startPlay(String url, TXCloudVideoView videoView, ITXLivePlayListener listener) {
        if (mLivePlayer != null && mLivePlayer.isPlaying()) {
            stopPlay();
        }

        if (!checkPlayUrl(url)) {
            return false;
        }

        mLivePlayer = new TXLivePlayer(mContext);
        mLivePlayer.setPlayListener(listener == null ? mainListener : listener);
        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
        // (1) 只有 4.3 以上android系统才支持
        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
        mLivePlayer.enableHardwareDecode(mHWDecode);
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
        mLivePlayer.setRenderMode(mCurrentRenderMode);
        //设置播放器缓存策略
        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
        mLivePlayer.setPlayerView(videoView);
        int result = mLivePlayer.startPlay(url, mPlayType); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
        if (result != 0) {
            return false;
        }
        return true;
    }

    /**
     * 检测播放地址是否正确
     * @param playUrl
     * @return
     */
    protected boolean checkPlayUrl(final String playUrl) {
        if (TextUtils.isEmpty(playUrl) || (!playUrl.startsWith("http://") && !playUrl.startsWith("https://") && !playUrl.startsWith("rtmp://") && !playUrl.startsWith("/"))) {
//            Toast.makeText(getApplicationContext(), "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
            return false;
        }

        switch (mActivityType) {
            case ACTIVITY_TYPE_LIVE_PLAY: {
                if (playUrl.startsWith("rtmp://")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
                } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".flv")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
                } else {
//                    Toast.makeText(getApplicationContext(), "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            break;
            case ACTIVITY_TYPE_REALTIME_PLAY:
                mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC;
                break;
            default:
//                Toast.makeText(getApplicationContext(), "播放地址不合法，目前仅支持rtmp,flv播放方式!",Toast.LENGTH_SHORT).show();
                return false;
        }
        return true;
    }

    protected void setRenderMode(int mode) {
        mCurrentRenderMode = mode;
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if (mLivePlayer != null) {
            mLivePlayer.stopRecord();
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(true);
            mLivePlayer = null;
        }
    }

    /**
     * 恢复播放
     */
    public void onResume() {
        if (null != mLivePlayer)
            mLivePlayer.resume();
    }

    /**
     * 暂停播放
     */
    public void onPause() {
        if (null != mLivePlayer)
            mLivePlayer.pause();
    }

    public void onDestroy() {
        stopPlay();
        mainListener = null;
        instance = null;
    }


}
