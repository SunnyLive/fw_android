package com.fengwo.module_comment.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Log;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SvgaUtils {
    private Context context;
    private ArrayList<String> stringList;
    private SVGAImageView svgaImage;
    private SVGAParser parser;

    public SvgaUtils(Context context, SVGAImageView svgaImage) {
        this.context = context;
        this.svgaImage = svgaImage;
        svgaImage.setLoops(1);
    }
    public SvgaUtils(Context context, SVGAImageView svgaImage,int loop) {
        this.context = context;
        this.svgaImage = svgaImage;
    //    svgaImage.setLoops(loop);
    }
    public void repeat() {
   //     svgaImage.setLoops(0);
    }

    /**
     * 初始化数据
     */
    public void initAnimator() {
        initAnimator(null);
    }

    SVGACallback callback;

    public void initAnimator(SVGACallback callback) {
        this.callback = callback;
        parser = new SVGAParser(context);
        stringList = new ArrayList<>();
        //监听大动画的控件周期

        svgaImage.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
                if (callback != null) {
                    callback.onPause();
                }
                Log.e("setCallback", "onPause");
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
                //当动画结束，如果数组容器大于0，则移除容器第一位的数据，轮询播放动画。
                if (stringList != null && stringList.size() > 0) {
                    stringList.remove(0);
                    //如果移除之后的容器大于0，则开始展示新一个的大动画
                    if (stringList != null && stringList.size() > 0) {
                        try {
                            parseSVGA();//解析加载动画
                        } catch (Exception e) {

                        }
                    } else {
                        stopSVGA();
                    }
                } else {
                    stopSVGA();
                }
            }

            @Override
            public void onRepeat() {
                if (callback != null) {
                    callback.onRepeat();
                }
                //    Log.e("setCallback", "onRepeat=" + stringList.size());
                stopSVGA();
            }

            @Override
            public void onStep(int i, double v) {
                if (callback != null) {
                    callback.onStep(i, v);
                }
            }
        });
    }

    private SVGAParser.ParseCompletion mParseCompletionCallback;

    public void getFile(String fileName, String url) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/fwhynew/" + fileName);

        if (file.exists()) {
            sendPlay(file);

            //     startAnimatorUrl(url);
        } else {
            List<String> source = new ArrayList<>();
            source.add(url);
            DownloadHelper.download(source, new DownloadHelper.DownloadListener() {
                @Override
                public void completed() {
                    if (file.exists())
                        sendPlay(file);
                }

                @Override
                public void failed() {
                    if (null != callback){
                        if (isApkInDebug(context)) {
                            ToastUtils.showShort(context, "文件下载失败"+url);
                        }
                        callback.onFinished();
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        }
    }

    private void sendPlay(File file) {
        if (mParseCompletionCallback == null) {
            mParseCompletionCallback = new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {

                    playSVGA(svgaVideoEntity);
                }

                @Override
                public void onError() {
                    if (null != callback) {
                        if (isApkInDebug(context)) {
                            ToastUtils.showShort(context, "播放失败，可能是手机本地已有相同名字的文件");
                        }
                        callback.onFinished();
                    }

                }
            };
        }
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            parser.decodeFromInputStream(bis, file.getAbsolutePath(), mParseCompletionCallback, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void playSVGA(SVGAVideoEntity svgaVideoEntity) {
        if (svgaImage != null) {
            svgaImage.setVideoItem(svgaVideoEntity);
            svgaImage.startAnimation();
        }

    }

    /**
     * 显示动画
     */
    public void startAnimator(String svgaName) {
        stringList.add(stringList.size(), svgaName + ".svga");
        //如果礼物容器列表的数量是1，则解析动画，如果数量不是1，则此处不解析动画，在上一个礼物解析完成之后加载再动画
        if (stringList.size() == 1) {
            parseSVGA();
        }
    }

    /**
     * 显示动画
     */
    public void startAnimatorUrl(String svgaName) {
        stringList.add(stringList.size(), svgaName);
        //如果礼物容器列表的数量是1，则解析动画，如果数量不是1，则此处不解析动画，在上一个礼物解析完成之后加载再动画
        if (stringList.size() == 1) {
            parseSVGA();
        }
    }

    /**
     * 停止动画
     */
    public void stopSVGA() {
        if (svgaImage.isAnimating() && stringList.size() == 0) {
            svgaImage.stopAnimation();
        }
    }
    public void clearList(){
        stringList.clear();
    }

    /**
     * 解析加载动画
     */
    private void parseSVGA() {
        if (stringList.size() > 0) {
            try {

                parser.parse(stringList.get(0), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                        //解析动画成功，到这里才真正的显示动画
                        SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                        svgaImage.setImageDrawable(drawable);
                        svgaImage.startAnimation();
                    }

                    @Override
                    public void onError() {
                        //如果动画数组列表大于0,移除第一位的动画,继续循环解析
                        if (stringList.size() > 0) {
                            stringList.remove(0);
                            parseSVGA();
                        } else {
                            stopSVGA();
                        }
                    }
                });
            } catch (Exception e) {
            }
        } else {
            stopSVGA();
        }
    }

}
