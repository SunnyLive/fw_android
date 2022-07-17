package com.fengwo.module_live_vedio.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.Image;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.widget.CenteredImageSpan;
import com.fengwo.module_live_vedio.R;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.AndroidDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.model.android.ViewCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.util.DanmakuUtils;

public class DanmakuConfigUtil {

    public static DanmakuContext getDanmakuContext() {

        //设置显示最大行数
        Map<Integer, Integer> maxLines = new HashMap<>();
        maxLines.put(BaseDanmaku.TYPE_SCROLL_RL, 5);

        //设置是否显示重叠
        Map<Integer, Boolean> overMap = new HashMap<>();
        overMap.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overMap.put(BaseDanmaku.TYPE_FIX_TOP, true);

        //实例化弹幕上下文
        DanmakuContext mDmkContext = DanmakuContext.create();
        mDmkContext
                .setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDuplicateMergingEnabled(false)  //不可重复合并
                .setScrollSpeedFactor(1.2f)   //设置滚动速度因子
                .setScaleTextSize(1.2f)    //弹幕字体缩放
                .setMaximumLines(maxLines)   //设置最大滚动行
                .preventOverlapping(overMap).setDanmakuMargin(40);
        return mDmkContext;
    }


    /**
     * [生成默认解析]
     *
     * @type {[type]}
     */
    public static BaseDanmakuParser getDefaultDanmakuParser() {
        return new BaseDanmakuParser() {
            @Override
            protected IDanmakus parse() {
                return new Danmakus();
            }
        };
    }

    /**
     * 获取一条弹幕
     *
     * @param mDmkContext
     * @param time
     * @param content
     * @return
     */
    public static void getOneDanmu(Context context, DanmakuContext mDmkContext, long time, String content, String color, String nickname, String headerUrl, String nickColor, String msgColor, OnImgDanmuCreatedListener l) {
        mDmkContext.setCacheStuffer(new BackgroundCacheStuffer(context, -1, "#60000000"), null);
        //创建一条从右侧开始滚动的弹幕
        final BaseDanmaku danmaku = mDmkContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL, mDmkContext);
        if (danmaku == null) {
            return;
        }
        danmaku.padding = 5;
        danmaku.priority = 0;
        danmaku.isLive = true;
        danmaku.textShadowColor = 0;
        danmaku.setTime(time + 1000);
        danmaku.textSize = sp2px(context, 12f);
//        danmaku.textColor = Color.parseColor(color);
        final int imgWidth = DensityUtils.dp2px(context, 25);
        if (TextUtils.isEmpty(headerUrl)) {
            String text = "bitmap";
            SpannableStringBuilder spannable = new SpannableStringBuilder(text);
            Drawable drawable = context.getResources().getDrawable(R.drawable.default_head);
            drawable.setBounds(0, 0, imgWidth, imgWidth);
            CenteredImageSpan span = new CenteredImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
            ForegroundColorSpan nickSpan = new ForegroundColorSpan(Color.parseColor(nickColor));
            ForegroundColorSpan contentSpan = new ForegroundColorSpan(Color.parseColor(msgColor));
            spannable.append(nickname);
            spannable.append(":");
            spannable.append(content);
            int imgStart = 0;
            int nickStart = text.length();
            int contentStart = text.length() + nickname.length() + 1;
            spannable.setSpan(span, imgStart, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(nickSpan, nickStart, nickStart + nickname.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(contentSpan, contentStart, contentStart + content.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            danmaku.text = spannable;
            l.onSuccess(danmaku);
        } else {
            ImageLoader.downloadImg(context, headerUrl, imgWidth, imgWidth, new ImageLoader.DownloadBitmapSuccessListener() {
                @Override
                public void success(Bitmap bp) {
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bp);
                    drawable.setCircular(true);
                    drawable.setAntiAlias(true);
                    drawable.setCornerRadius(Math.max(bp.getWidth(), bp.getHeight()));
                    drawable.setBounds(0, 0, imgWidth, imgWidth);
                    String text = "bitmap";
                    SpannableStringBuilder spannable = new SpannableStringBuilder(text);
                    CenteredImageSpan span = new CenteredImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
                    ForegroundColorSpan nickSpan = new ForegroundColorSpan(Color.parseColor(nickColor));
                    ForegroundColorSpan contentSpan = new ForegroundColorSpan(Color.parseColor(msgColor));
                    spannable.append(nickname);
                    spannable.append(":");
                    spannable.append(content);
                    int imgStart = 0;
                    int nickStart = text.length();
                    int contentStart = text.length() + nickname.length() + 1;
                    spannable.setSpan(span, imgStart, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(nickSpan, nickStart, nickStart + nickname.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(contentSpan, contentStart, contentStart + content.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    danmaku.text = spannable;
                    l.onSuccess(danmaku);
                }
            });
        }
//        danmaku.borderColor = Color.GREEN;
    }

    public static void getOneDanmu2(Context context, DanmakuContext mDmkContext, long time, String content, String color,int userLevel, String nickname, String headerUrl, String nickColor, String msgColor, OnImgDanmuCreatedListener l) {
        mDmkContext.setCacheStuffer(new NormalDanmu(context), null);
        //创建一条从右侧开始滚动的弹幕
        final BaseDanmaku danmaku = mDmkContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL, mDmkContext);
        if (danmaku == null) {
            return;
        }
        danmaku.padding = 0;
        danmaku.priority = 0;
        danmaku.isLive = true;
        danmaku.textShadowColor = 0;
        danmaku.setTime(time + 1000);
        danmaku.textSize = sp2px(context, 11f);
        danmaku.setTag(0, headerUrl);
        danmaku.setTag(1, nickname);
        danmaku.setTag(2, nickColor);
        danmaku.setTag(3, msgColor);
        danmaku.setTag(4, color);
        danmaku.setTag(6, userLevel);
        danmaku.text = content;
        final int imgWidth = DensityUtils.dp2px(context, 25);
        if (TextUtils.isEmpty(headerUrl)) {
            Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_head);
            danmaku.setTag(5, b);
            l.onSuccess(danmaku);
        } else {
            ImageLoader.downloadImg(context, headerUrl, imgWidth, imgWidth, new ImageLoader.DownloadBitmapSuccessListener() {
                @Override
                public void success(Bitmap bp) {
                    danmaku.setTag(5, bp);
                    l.onSuccess(danmaku);
                }
            });
        }

    }

    public interface OnImgDanmuCreatedListener {
        void onSuccess(BaseDanmaku danmaku);
    }


    /**
     * 获取一条弹幕
     *
     * @param mDmkContext
     * @param time
     * @param content
     * @return`
     */
    public static BaseDanmaku getGiftDanmu(Context context, DanmakuContext mDmkContext, long time, String content) {
        mDmkContext.setCacheStuffer(new BackgroundGiftCacheStuffer(context), null);
        //创建一条从右侧开始滚动的弹幕
        BaseDanmaku danmaku = mDmkContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_FIX_TOP, mDmkContext);
        if (danmaku == null) {
            return null;
        }
        danmaku.text = content;
        danmaku.duration = new Duration(10000);
        danmaku.priority = 1;
        danmaku.isLive = true;
        danmaku.padding = 20;
        danmaku.setTime(time + 1000);
        danmaku.textSize = sp2px(context, 13f);
        danmaku.textColor = Color.WHITE;
        return danmaku;
    }

    /**
     * 获取一条弹幕
     *
     * @param mDmkContext
     * @param time
     * @param content
     * @return
     */
    public static BaseDanmaku getGuardDanmu(Context context, DanmakuContext mDmkContext, long time, String content) {
        mDmkContext.setCacheStuffer(new BackgroundGiftCacheStuffer(context), null);
        //创建一条从右侧开始滚动的弹幕
        BaseDanmaku danmaku = mDmkContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_FIX_TOP, mDmkContext);
        if (danmaku == null) {
            return null;
        }
        danmaku.text = content;
        danmaku.duration = new Duration(5000);
        danmaku.priority = 1;
        danmaku.isLive = true;
        danmaku.padding = 20;
        danmaku.setTime(time + 1000);
        danmaku.textSize = sp2px(context, 17f);
        danmaku.textColor = Color.WHITE;
        return danmaku;
    }


    public static void getEnterDanmu(Context context, DanmakuContext mDmkContext, long time, String nickname, int level, String guardUrl, String vipLevel, OnImgDanmuCreatedListener l) {
        mDmkContext.setCacheStuffer(new BackgroundCacheStuffer(context, R.drawable.live_bg_enter, ""), new BaseCacheStuffer.Proxy() {
            @Override
            public void prepareDrawing(BaseDanmaku danmaku, boolean fromWorkerThread) {

            }

            @Override
            public void releaseResource(BaseDanmaku danmaku) {

            }
        });
        BaseDanmaku danmaku = mDmkContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null) {
            return;
        }
        danmaku.padding = 5;
        danmaku.priority = 0;
        danmaku.isLive = true;
        danmaku.setTime(time + 1000);
        danmaku.textSize = sp2px(context, 13f);
        danmaku.textColor = Color.RED;
        danmaku.borderColor = Color.TRANSPARENT;
        StringBuilder builder = new StringBuilder();
        builder.append("1");
        if (!TextUtils.isEmpty(vipLevel) && Integer.parseInt(vipLevel) > 0) {
            builder.append(" 2 ");
        }
        if (!TextUtils.isEmpty(guardUrl)) {
            builder.append(" 3 ");
            builder.append(nickname + "进入直播间了！");
            ImageLoader.downloadImg(context, guardUrl, 50, 50, new ImageLoader.DownloadBitmapSuccessListener() {
                @Override
                public void success(Bitmap bp) {
                    CenteredImageSpan shouhuSpan = new CenteredImageSpan(context, bp);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(builder.toString());
                    int imgRes = ImageLoader.getUserLevel(level);
                    Drawable d = context.getResources().getDrawable(imgRes);
                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                    CenteredImageSpan span = new CenteredImageSpan(context, imgRes);
                    spannableStringBuilder.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (!TextUtils.isEmpty(vipLevel) && Integer.parseInt(vipLevel) > 0) {
                        int vipImg = ImageLoader.getVipLevel(Integer.parseInt(vipLevel));
                        Drawable vipDrawable = context.getResources().getDrawable(vipImg);
                        vipDrawable.setBounds(0, 0, vipDrawable.getIntrinsicWidth(), vipDrawable.getIntrinsicHeight());
                        CenteredImageSpan vipSpan = new CenteredImageSpan(context, vipImg);
                        int vipStart = builder.toString().indexOf("2");
                        spannableStringBuilder.setSpan(vipSpan, vipStart, vipStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    int guardStart = builder.toString().indexOf("3");
                    spannableStringBuilder.setSpan(shouhuSpan, guardStart, guardStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    danmaku.text = spannableStringBuilder;
                    l.onSuccess(danmaku);
                }
            });
        } else {
            builder.append(nickname + "进入直播间了！");
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(builder.toString());
            int imgRes = ImageLoader.getUserLevel(level);
            Drawable d = context.getResources().getDrawable(imgRes);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            CenteredImageSpan span = new CenteredImageSpan(context, imgRes);
            spannableStringBuilder.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (!TextUtils.isEmpty(vipLevel) && Integer.parseInt(vipLevel) > 0) {
                int vipImg = ImageLoader.getVipLevel(Integer.parseInt(vipLevel));
                Drawable vipDrawable = context.getResources().getDrawable(vipImg);
                vipDrawable.setBounds(0, 0, vipDrawable.getIntrinsicWidth(), vipDrawable.getIntrinsicHeight());
                CenteredImageSpan vipSpan = new CenteredImageSpan(context, vipImg);
                int vipStart = builder.toString().indexOf("2");
                spannableStringBuilder.setSpan(vipSpan, vipStart, vipStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            danmaku.text = spannableStringBuilder;
            l.onSuccess(danmaku);
        }

    }

    /**
     * sp转px的方法。
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private static class BackgroundCacheStuffer extends SpannedCacheStuffer {

        private Context mContext;
        private int res;
        private String color;

        public BackgroundCacheStuffer(Context context, int drawable, String color) {
            this.res = drawable;
            mContext = context;
            this.color = color;
        }

        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
//            if (res > 0) {
//                danmaku.padding = 10;
//            } else {
//            }
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawText(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, TextPaint paint, boolean fromWorkerThread) {
            if (danmaku.obj == null) {
                super.drawText(danmaku, lineText, canvas, left, top, paint, fromWorkerThread);
                return;
            }
            SoftReference<StaticLayout> reference = (SoftReference<StaticLayout>) danmaku.obj;
            StaticLayout staticLayout = reference.get();
            boolean requestRemeasure = 0 != (danmaku.requestFlags & BaseDanmaku.FLAG_REQUEST_REMEASURE);
            boolean requestInvalidate = 0 != (danmaku.requestFlags & BaseDanmaku.FLAG_REQUEST_INVALIDATE);

            if (requestInvalidate || staticLayout == null) {
                if (requestInvalidate) {
                    danmaku.requestFlags &= ~BaseDanmaku.FLAG_REQUEST_INVALIDATE;
                }
                CharSequence text = danmaku.text;
                if (text != null) {
                    if (requestRemeasure) {
                        staticLayout = new StaticLayout(text, paint, (int) Math.ceil(StaticLayout.getDesiredWidth(danmaku.text, paint)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                        danmaku.paintWidth = staticLayout.getWidth();
                        danmaku.paintHeight = staticLayout.getHeight();
                        danmaku.requestFlags &= ~BaseDanmaku.FLAG_REQUEST_REMEASURE;
                    } else {
                        staticLayout = new StaticLayout(text, paint, (int) danmaku.paintWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                    }
                    danmaku.obj = new SoftReference<>(staticLayout);
                } else {
                    return;
                }
            }
            boolean needRestore = false;
            if (left != 0 && top != 0) {
                canvas.save();
                if (res > 0) {
                    canvas.translate(left, top + paint.ascent());
                } else {
                    canvas.translate(left, top + paint.ascent() + -20);//因加载网络图片 需要偏移
                }
                needRestore = true;
            }
            staticLayout.draw(canvas);
            if (needRestore) {
                canvas.restore();
            }
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {

            if (res > 0) {
//                Drawable b = ((NinePatchDrawable) mContext.getResources().getDrawable(R.drawable.enterview_item31));
                Rect rf = new Rect();
                rf.left = (int) (left + 2);
                rf.top = (int) (top + 2);
                rf.right = (int) (left + danmaku.paintWidth - 2);
                rf.bottom = (int) (top + danmaku.paintHeight - 2);
                Bitmap bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(res)).getBitmap();
                canvas.drawBitmap(bitmap, rf, rf, paint);
            } else {
                RectF rf = new RectF();
                rf.left = (int) (left);
                rf.top = (int) (top);
                rf.right = (int) (left + danmaku.paintWidth - 2);
                rf.bottom = (int) (top + danmaku.paintHeight - 2);
                paint.setColor(Color.parseColor(color));
                canvas.drawRoundRect(rf, 100, 100, paint);
            }
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }

    /**
     * 绘制礼物背景(自定义弹幕样式)
     */
    private static class BackgroundGiftCacheStuffer extends SpannedCacheStuffer {

        private Context mContext;

        public BackgroundGiftCacheStuffer(Context context) {
            mContext = context;
        }

        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            RectF rf = new RectF();
            rf.left = (int) (left);
            rf.top = (int) (top);
            rf.right = (int) (left + danmaku.paintWidth);
            rf.bottom = (int) (top + danmaku.paintHeight);
            LinearGradient linearGradient = new LinearGradient(
                    0, 0, danmaku.paintWidth, danmaku.paintHeight
                    , new int[]{Color.parseColor("#FFCE3D"), Color.parseColor("#FF3EB0")}
                    , new float[]{0, 1f}, Shader.TileMode.REPEAT);
            paint.setShader(linearGradient);
            canvas.drawRoundRect(rf, 100, 100, paint);
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }


    private static class NormalDanmu extends ViewCacheStuffer<DanmuVh> {
        Context mContext;

        public NormalDanmu(Context context) {
            mContext = context;
        }

        @Override
        public DanmuVh onCreateViewHolder(int viewType) {
            return new DanmuVh(View.inflate(mContext, R.layout.item_danmu, null));
        }

        @Override
        public void onBindViewHolder(int viewType, DanmuVh viewHolder, BaseDanmaku danmaku, AndroidDisplayer.DisplayerConfig displayerConfig, TextPaint paint) {
            if (paint != null)
                viewHolder.strTv.getPaint().set(paint);
            viewHolder.strTv.setText(danmaku.text);
            viewHolder.headerIv.setImageBitmap((Bitmap) danmaku.getTag(5));
            viewHolder.strTv.setTextColor(Color.parseColor(danmaku.getTag(3) + ""));
            viewHolder.strTv.setText(danmaku.text);
            viewHolder.nameTv.setText(danmaku.getTag(1) + "");
            viewHolder.nameTv.setTextColor(Color.parseColor(danmaku.getTag(2) + ""));
            viewHolder.ivLevel.setImageResource(ImageLoader.getResId("login_ic_v" +  danmaku.getTag(6), R.drawable.class));
//            viewHolder.itemView.setBackgroundColor(Color.parseColor(danmaku.getTag(4) + ""));


//            danmaku.setTag(0, headerUrl);
//            danmaku.setTag(1, nickname);
//            danmaku.setTag(2, nickColor);
//            danmaku.setTag(3, msgColor);
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            danmaku.setTag(null);
        }
    }

    private static class DanmuVh extends ViewCacheStuffer.ViewHolder {
        private final ImageView headerIv;
        private final TextView nameTv;
        private final TextView strTv;
        private final ImageView ivLevel;
        private final View itemView;

        public DanmuVh(View itemView) {
            super(itemView);
            headerIv = itemView.findViewById(R.id.iv_header);
            nameTv = itemView.findViewById(R.id.tv_name);
            strTv = itemView.findViewById(R.id.tv_str);
            ivLevel = itemView.findViewById(R.id.iv_level);
            this.itemView = itemView;
        }
    }
}