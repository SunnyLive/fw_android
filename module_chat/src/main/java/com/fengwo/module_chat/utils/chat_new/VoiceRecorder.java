package com.fengwo.module_chat.utils.chat_new;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.L;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ZhouZhBao on 2018/12/14
 */
public class VoiceRecorder {

    @Autowired
    UserProviderService service;

    private static final String TAG = "VoiceRecorder";
    MediaRecorder recorder;

    static final String PREFIX = "voice";
    static final String EXTENSION = ".mp3";

    private boolean isRecording = false;
    private long startTime;
    private String mVoiceFilePath = null;
    private String mVoiceFileName = null;
    private File file;

    private Handler mHandler;

    public VoiceRecorder(Handler handler) {
        ARouter.getInstance().inject(this);
        this.mHandler = handler;
    }

    public static final String STORAGE_VOICE = "voice";
    public static final int RECORDER_ERROR = 400; // 录制结束  文件不存在 & 文件录制为空 & ...
    public static final int RECORDER_SUCCESS = 200; //录制成功

    /**
     * start recording to the file
     */
    @SuppressLint("CheckResult")
    public String startRecording(Context appContext) {
        file = null;
        try {
            // need to create recorder every time, otherwise, will got exception
            // from setOutputFile when try to reuse
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//            recorder.setAudioChannels(1); // MONO
//            recorder.setAudioSamplingRate(8000); // 8000Hz
//            recorder.setAudioEncodingBitRate(64); // seems if change this to
            // 128, still got same file
            // getFriendCount.
            // one easy way is to use temp file
            // file = File.createTempFile(PREFIX + userId, EXTENSION,
            // User.getVoicePath());
            mVoiceFileName = getVoiceFileName(System.currentTimeMillis() + "");
            mVoiceFilePath = FileUtils.createChatDirByName(service.getUserInfo().fwId + File.separator + STORAGE_VOICE) + File.separator + mVoiceFileName;
            file = new File(mVoiceFilePath);
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            isRecording = true;
            recorder.start();
        } catch (IOException e) {
        } catch (IllegalStateException e) {
            L.e(TAG, "prepare() failed");
        }
        Observable.just("")
                .observeOn(Schedulers.io())
                .subscribe(data->{
                    while (isRecording) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                        mHandler.sendMessage(msg);
                        try {
                            Thread.sleep(500);
                        }catch (InterruptedException e){
                            e.fillInStackTrace();
                        }

                    }
                });
        startTime = new Date().getTime();
        Log.d(TAG, "start voice recording to file:" + file.getAbsolutePath());
        return file == null ? null : file.getAbsolutePath();
    }

    /**
     * stop the recoding
     *
     * @return seconds of the voice recorded
     */

    public void discardRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                if (file != null && file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            } catch (IllegalStateException e) {
            } catch (RuntimeException e) {
            }
            isRecording = false;
        }
    }

    public VoiceRecorderBean stopRecoding() throws Exception{
        VoiceRecorderBean voiceRecorderBean = new VoiceRecorderBean();
        if (recorder != null) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;

            if (file.exists()) {
                Log.d(TAG, "原始文件名 ============》" + mVoiceFileName);
                String fileMd5 = FileUtils.getFileMD5(file);
                Log.d(TAG, "MD5 ============》" + fileMd5);
                File fileAfterRename = new File(FileUtils
                        .createChatDirByName(service.getUserInfo().fwId + File.separator + STORAGE_VOICE) + File.separator + getVoiceFileName(fileMd5));
                Log.d(TAG, "MD5name ============》" + fileAfterRename);
                file.renameTo(fileAfterRename);
                file = fileAfterRename;
            }

            if (file == null || !file.exists() || !file.isFile()) {
                voiceRecorderBean.setRecord_state(RECORDER_ERROR);
                return voiceRecorderBean;
            }
            if (file.length() == 0) {
                file.delete();
                voiceRecorderBean.setRecord_state(RECORDER_ERROR);
                return voiceRecorderBean;
            }
            int seconds = (int) (new Date().getTime() - startTime) / 1000;
            voiceRecorderBean.setRecord_state(RECORDER_SUCCESS);
            voiceRecorderBean.setRecord_path(file.getPath());
            voiceRecorderBean.setRecord_time(seconds);
            return voiceRecorderBean;
        }
        return null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (recorder != null) {
            recorder.release();
        }
    }

    private String getVoiceFileName(String voiceFileName) {
        return voiceFileName + EXTENSION;
    }

    public boolean isRecording() {
        return isRecording;
    }


    public String getmVoiceFilePath() {
        return mVoiceFilePath;
    }

    public String getmVoiceFileName() {
        return mVoiceFileName;
    }

    public class VoiceRecorderBean {
        public int getRecord_state() {
            return record_state;
        }

        public void setRecord_state(int record_state) {
            this.record_state = record_state;
        }

        public String getRecord_path() {
            return record_path;
        }

        public void setRecord_path(String record_path) {
            this.record_path = record_path;
        }

        public int getRecord_time() {
            return record_time;
        }

        public void setRecord_time(int record_time) {
            this.record_time = record_time;
        }

        int record_state;/*录制返回状态*/
        String record_path;/*文件路径*/
        int record_time;/*文件时长*/
    }
}
