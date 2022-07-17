package com.fengwo.module_comment.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.fengwo.module_comment.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ImageLoader {

    private final static String WATERMARK = "?x-oss-process=style/watermark";

    private static ImageLoader instance;

    public static void init(Context c) {
        instance = new ImageLoader(c);
    }

    private Context mContext;

    public ImageLoader(Context c) {
        mContext = c;
    }

    public static int getUserLevel(int level) {
        if (level < 1) {
            level = 1;
        }
        if (level > 47) {
            level = 47;
        }
        return getResId("login_ic_v" + level, R.drawable.class);
    }

    public static int getChannelLevel(int level) {
        return getResId("login_ic_type3_v" + level, R.drawable.class);
    }

    public static int getVipLevel(int level) {
        return getResId("vip" + (level > 0 ? level : 1), R.drawable.class);
    }


    /**
     * 主播大的等级
     */
    public static int getAnchorLevel(int level) {
        if (level <= 5) {
            return R.drawable.ic_live_level1_5;
        } else if (level <= 10) {
            return R.drawable.ic_live_level6_10;
        } else if (level <= 15) {
            return R.drawable.ic_live_level11_15;
        } else if (level <= 20) {
            return R.drawable.ic_live_level16_20;
        } else if (level <= 25) {
            return R.drawable.ic_live_level21_25;
        } else if (level <= 30) {
            return R.drawable.ic_live_level26_30;
        } else if (level <= 35) {
            return R.drawable.ic_live_level31_35;
        } else if (level <= 40) {
            return R.drawable.ic_live_level36_40;
        } else if (level <= 45) {
            return R.drawable.ic_live_level41_45;
        } else {
            return R.drawable.ic_live_level46_50;
        }
    }


    /**
     * 获取直播间用户的等级
     *
     * @param level
     * @return
     */
    public static String getLiveUserLevel(int level) {
        if (level <= 5) {
            return "ic_user_level_1_5";
        } else if (level <= 10) {
            return "ic_user_level_6_10";
        } else if (level <= 13) {
            return "ic_user_level_11_13";
        } else if (level <= 16) {
            return "ic_user_level_14_16";
        } else if (level <= 19) {
            return "ic_user_level_17_19";
        } else if (level <= 22) {
            return "ic_user_level_20_22";
        } else if (level <= 25) {
            return "ic_user_level_23_25";
        } else {
            return "ic_user_level_" + level;
        }
    }


    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void loadBitMapImg(ImageView i, Bitmap url) {
        Glide.with(i.getContext())
                .load(url)
                .into(i);
    }

    public static void loadBitMapImg(ImageView i, int url) {
        Glide.with(i.getContext())
                .load(url)
                .into(i);
    }

    public static void loadImg(ImageView i, String url, int widht, int height) {
        //设置图片圆角角度
        instance.loadRouteImage(i, url, widht, height);
    }

    public static void loadSplash(ImageView i, String url) {
        instance.loadSplashImg(i, url);
    }

    public static void loadImageNoDefault(ImageView i, String url) {
        instance.loadImageNoDefaults(i, url);
    }
    public static void loadImg(ImageView i, String url) {
        instance.lodaImage(i, url, true);
    }

    public static void loadImgGaussian(ImageView i, String url) {
        instance.lodaImagegGaussian(i, url, true);
    }

    public static void loadImgs(ImageView i, String url) {
        instance.lodaImages(i, url, true);
    }

    public static void loadUrl(View i, String url) {
        instance.lodaImage(i, url, true);
    }


    public static void loadImgWithWatermark(ImageView i, String url) {
        L.e("watermark", url);
        instance.lodaImage(i, url, true);
    }

    public static void loadImgNoCache(ImageView i, String url) {
        instance.lodaImage(i, url, false);
    }

    public static void loadImg(ImageView i, String url, int defaultRes) {
        instance.lodaImage(i, url, defaultRes);
    }

    public static void loadImgBlur(ImageView i, String url, int defaultRes) {
        instance.lodaImageBlur(i, url, defaultRes);
    }

    public static void loadHightImage(ImageView i, String url, int defaultRes) {
        instance.loadHighImage(i, url, defaultRes);
    }

    public static void loadGif(ImageView i, int res) {
        instance.loadGif1(i, res);
    }

    public static void loadGif1OneTime(ImageView i, int res) {
        instance.loadGifOneTime(i, res);
    }

    public static void loadImg(ImageView i, int drawableRes) {
        instance.lodaImage(i, drawableRes);
    }

    public static void loadRouteImg(ImageView i, String url) {
        //设置图片圆角角度
        instance.loadRouteImage(i, url);
    }

    public static void loadRouteImg(ImageView i, String url, int size) {
        //设置图片圆角角度
        loadRouteImg(i, url, size, false);
    }

    public static void loadRouteImg(ImageView i, String url, int size, boolean isCenterCrop) {
        //设置图片圆角角度
        instance.loadRouteImage(i, url, size, isCenterCrop);
    }

    public static void downloadImg(Context context, String url, int width, int height, DownloadBitmapSuccessListener l) {
        instance.downloadImage(context, url, width, height, l);
    }

    public static void loadRouteImg(ImageView i, String url, int widht, int height) {
        //设置图片圆角角度
        instance.loadRouteImage(i, url, widht, height);
    }

    public static void loadCircleImg(ImageView i, String url) {
        instance.loadCircleImage(i, url);
    }

    public static void loadCircleImg(ImageView i, int url) {
        instance.loadCircleImage(i, url);
    }

    private void loadRouteImage(ImageView iv, String url) {
        if (null == iv) return;
        RoundedCorners roundedCorners = new RoundedCorners(15);
        RequestOptions options = RequestOptions
                .noTransformation()
                .error(R.drawable.timg)
                .placeholder(R.drawable.timg)
                .transform(roundedCorners)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(mContext)
                .load(url)
                .apply(options)
                .into(iv);


    }

    @SuppressLint("CheckResult")
    private void loadRouteImage(ImageView iv, String url, int size, boolean isCenterCrop) {
        if (null == iv) return;
        RoundedCorners roundedCorners = new RoundedCorners(size);
        RequestOptions options = RequestOptions
                .noTransformation()
                .error(R.drawable.live_home_default)
                .placeholder(R.drawable.live_home_default)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if(isCenterCrop){
            options.transform(new CenterCrop(), roundedCorners);
        }else{
            options.transform(roundedCorners);
        }
        Glide.with(mContext)
                .load(url)
                .apply(options)
                .into(iv);

    }


    public static void loadRoute4Img(ImageView i, String url, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        //设置图片圆角角度
        instance.loadRoute4Image(i, url, topLeft, topRight, bottomRight, bottomLeft);
    }

    private void loadRoute4Image(ImageView iv, String url, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        if (null == iv) return;
        GranularRoundedCorners granularRoundedCorners = new GranularRoundedCorners(topLeft, topRight, bottomRight, bottomLeft);
        RequestOptions options = RequestOptions
                .noTransformation()
                .fitCenter()
                .transform(granularRoundedCorners)
                .error(R.drawable.icon_impression_default)
                .placeholder(R.drawable.icon_impression_default)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(mContext)
                .load(url)
                .apply(options)
                .into(iv);
    }


    private void downloadImage(Context context, String url, int width, int height, DownloadBitmapSuccessListener listener) {
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.timg)
                .error(R.drawable.timg)
                .transform(new FitCenter())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .override(width, height)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).asBitmap().load(url).apply(requestOptions)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        listener.success(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }


    private void loadRouteImage(ImageView iv, String url, int width, int height) {
        if (null == iv) return;
        RoundedCorners roundedCorners = new RoundedCorners(15);
        RequestOptions options = RequestOptions
                .noTransformation()
                .error(R.drawable.timg)
                .placeholder(R.drawable.timg)
                .transform(roundedCorners)
                .dontAnimate()
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(url)
                .apply(options)
                .into(iv);

    }

    public static void loadImgReSize(ImageView iv, String url, int width, int height) {
        if (null == iv) return;
        RequestOptions options = RequestOptions
                .noTransformation()
                .error(R.drawable.timg)
                .placeholder(R.drawable.timg)
                .dontAnimate()
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(iv)
                .load(url)
                .apply(options)
                .into(iv);

    }

    private void lodaImages(ImageView i, String url, boolean cache) {
        if (null == i) return;

        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.live_home_default)
                .error(R.drawable.live_home_default)
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .fitCenter()
                .dontAnimate();
        if (cache) {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        } else {
            requestOptions.skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE); // 不使用磁盘缓存
        }
        Glide.with(i.getContext())
                .load(url)
                .apply(requestOptions)
                .into(i);
    }

    private void lodaImagegGaussian(ImageView i, String url, boolean cache) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.live_home_default)
                .error(R.drawable.live_home_default)
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .fitCenter()
                .dontAnimate();
        if (cache) {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        } else {
            requestOptions.skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE); // 不使用磁盘缓存
        }
        //刚开始做模糊 又不需要做 谁tm在改就去弄死他
        Glide.with(i.getContext()).load(url).apply(RequestOptions.bitmapTransform(new BlurTransformation(1, 1))).into(i);
//        Glide.with(i.getContext())
//                .load(url)
//                .thumbnail(0.1f)
//                .apply(requestOptions)
//                .into(i);
    }

    private void loadImageNoDefaults(ImageView i, String url) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(i.getContext())
                .load(url)
                .apply(requestOptions)
                .into(i);
    }

    private void lodaImage(ImageView i, String url, boolean cache) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.live_home_default)
                .error(R.drawable.live_home_default)
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .fitCenter()
                .dontAnimate();
        if (cache) {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        } else {
            requestOptions.skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE); // 不使用磁盘缓存
        }
        Glide.with(i.getContext())
                .load(url)
                .apply(requestOptions)
                .into(i);
    }

    public static void loadOrigImg(ImageView i, String url){
        RequestOptions requestOptions = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .fitCenter()
                .dontAnimate();
        Glide.with(i.getContext())
                .load(url)
                .apply(requestOptions)
                .into(i);
    }

    public static void loadOrigImg(ImageView i, String url, @DrawableRes int defId){
        RequestOptions requestOptions = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .error(defId)
                .placeholder(defId)
                .priority(Priority.LOW)
                .fitCenter()
                .dontAnimate();
        Glide.with(i.getContext())
                .load(url)
                .apply(requestOptions)
                .into(i);
    }

    @SuppressLint("CheckResult")
    private void lodaImage(View i, String url, boolean cache) {
        if (null == i) return;
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.live_home_default)
                .error(R.drawable.live_home_default)
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .fitCenter()
                .dontAnimate();
        if (cache) {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        } else {
            requestOptions.skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE); // 不使用磁盘缓存
        }
        Glide.with(i.getContext())
                .load(url)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        i.post(() -> {
                            i.setBackground(resource);

                        });
                        return true;
                    }
                })
                .into(ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenHeight(mContext));
    }
    private void loadSplashImg(ImageView i, String url) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(i.getContext())
                .load(url)
                .apply(requestOptions)
                .into(i);
    }


    private void loadGif1(ImageView i, int url) {
        if (null == i) {
            return;
        }
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .asGif()
                .load(url)
                .apply(requestOptions)
                .into(i);
    }

    private void loadGifOneTime(ImageView i, int url) {
        if (null == i) {
            return;
        }
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .asGif()
                .load(url)
                .apply(requestOptions)
                .listener(new RequestListener<GifDrawable>() {//添加监听，设置播放次数
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource instanceof GifDrawable) {
                            resource.setLoopCount(1);//只播放一次
                        }
                        return false;
                    }
                })
                .into(i);
    }

    private void lodaImage(ImageView i, String url, int defaultRes) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(defaultRes)
                .error(defaultRes)
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .into(i);
    }

    private void lodaImage(ImageView i, int drawableRes) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(drawableRes)
                .apply(requestOptions)
                .into(i);
    }

    private void lodaImageBlur(ImageView i, String url, int defaultRes) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(defaultRes)
                .error(defaultRes)
                .transform(new MultiTransformation<>(new CenterCrop(), new BlurTransformation(15, 3)))
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .into(i);
    }

    private void loadHighImage(ImageView i, String url, int defaultRes) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(defaultRes)
                .error(defaultRes)
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .into(i);
    }

    private void lodaImage(ImageView i, String url, int defaultRes, int width, int height) {
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(defaultRes)
                .error(defaultRes)
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .into(i);
    }


    private void loadCircleImage(ImageView i, String url) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .transform(new CircleCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .into(new DrawableImageViewTarget(i));
    }


    public static void loadCircleWithBorder(ImageView i, String url, float borderWidth, int borderColor) {
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.timg)
                .error(R.drawable.timg)
                .transform(new GlideCircleBorderTransform(borderWidth, borderColor))
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(i.getContext())
                .load(url)
                .apply(requestOptions)
                .into(new DrawableImageViewTarget(i));

    }

    private void loadRoundImg(ImageView i, String url, int round) {
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.timg)
                .error(R.drawable.timg)
                .transform(new CircleCrop(), new RoundedCorners(DensityUtils.px2dp(i.getContext(), round)))
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(i.getContext())
                .load(url)
                .apply(requestOptions)
                .into(new DrawableImageViewTarget(i));
    }

    public static void clearCache(WeakReference<Context> c) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(c.get().getApplicationContext()).clearDiskCache();
            }
        }).start();
        Glide.get(c.get().getApplicationContext()).clearMemory();
    }

    public  static  void clearGlide(Context context){
        Glide.get(context).clearMemory();
    }
    public static void loadLocalImg(File file, ImageView imageView) {
        if (!checkContext(imageView)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.timg)
                .error(R.drawable.timg)
                .transform(new CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(imageView.getContext()).load(file).apply(requestOptions).into(imageView);
    }

    public static void loadImgFitCenter(ImageView imageView, String url) {
        if (!checkContext(imageView)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.timg)
                .error(R.drawable.timg)
                .transform(new FitCenter())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(imageView.getContext()).load(url).apply(requestOptions).into(imageView);
    }

    /**
     * @param iv
     * @return true 表示正常
     */
    private static boolean checkContext(ImageView iv) {
        Context context = iv.getContext();
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Glide 的图形变换可参考Glide 示例源码
     * {@link com.bumptech.glide.load.resource.bitmap.CenterCrop}
     * {@link com.bumptech.glide.load.resource.bitmap.BitmapTransformation}
     */

    public static class GlideCircleBorderTransform extends BitmapTransformation {
        private final String ID = getClass().getName();
        private Paint mBorderPaint;
        private float borderWidth;
        private int borderColor;


        public GlideCircleBorderTransform(float borderWidth, int borderColor) {
            this.borderWidth = borderWidth;
            this.borderColor = borderColor;
            mBorderPaint = new Paint();
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStrokeWidth(borderWidth);
            mBorderPaint.setDither(true);

        }


        private Bitmap circleCrop(BitmapPool bitmapPool, Bitmap source) {

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap square = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = bitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            //画图
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            //设置 Shader
            paint.setShader(new BitmapShader(square, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float radius = size / 2f;
            //绘制一个圆
            canvas.drawCircle(radius, radius, radius, paint);


            /************************描边*********************/
            //注意：避免出现描边被屏幕边缘裁掉
            float borderRadius = radius - (borderWidth / 2);
            //画边框
            canvas.drawCircle(radius, radius, borderRadius, mBorderPaint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(ID.getBytes(CHARSET));
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof GlideCircleBorderTransform;
        }

        @Override
        public int hashCode() {
            return ID.hashCode();
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }
    }

    public interface DownloadBitmapSuccessListener {
        void success(Bitmap bp);
    }

    public interface SaveImageSuccessListener {
        void success(String url);
        void failed();
    }

    public static Bitmap getBitmap(Context mContext, String url) {
        try {
            return Glide.with(mContext)
                    .asBitmap()
                    .load(url)
                    .submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getDrawable(Context mContext, String url) {
        try {
            return Glide.with(mContext)
                    .asDrawable()
                    .load(url)
                    .submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setBackground(Context mContext, String url, View view){
        Glide.with(mContext)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(resource);
                        view.setBackground(drawable);
                    }

                });
    }
    private void loadCircleImage(ImageView i, int ids) {
        if (null == i) return;
        if (!checkContext(i)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.timg)
                .error(R.drawable.timg)
                .transform(new CircleCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(ids)
                .apply(requestOptions)
                .into(new DrawableImageViewTarget(i));
    }

    public static void saveImage(Context context, String url, int width, int height, SaveImageSuccessListener listener) {
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.timg)
                .error(R.drawable.timg)
                .transform(new FitCenter())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .override(width, height)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).asBitmap().load(url).apply(requestOptions)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        FileOutputStream out = null;
                        try { // 获取SDCard指定目录下
                            String sdCardDir = Environment.getExternalStorageDirectory() + "/fwtv/chat/card/";
                            File dirFile = new File(sdCardDir);  //目录转化成文件夹
                            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                                dirFile.mkdirs();
                            }                          //文件夹有啦，就可以保存图片啦
                            String bitmapName = System.currentTimeMillis() + ".jpeg";
                            File file = new File(sdCardDir, bitmapName);// 在SDcard的目录下创建图片文,以当前时间为其命名
                            out = new FileOutputStream(file);
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            //            System.out.println("_________保存到____sd______指定目录文件夹下____________________");
                            Log.e("saveBitMap", "saveBitmap: 图片保存到" + sdCardDir + bitmapName);
                            out.flush();
                            out.close();
                            listener.success(file.getAbsolutePath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            listener.failed();
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.failed();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * 加载视频第一帧
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadVideoFirstFrame(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(0)
                                .centerCrop()
                )
                .load(url)
                .into(imageView);
    }

}
