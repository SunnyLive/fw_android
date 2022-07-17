package com.fengbo.module_chatlib.utils;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.fengbo.module_chatlib.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * *****************************************
 *
 * @author 飞剑
 * @文件名称 : FaceConversionUtil.java
 * @创建时间 : 2013-1-27 下午02:34:09
 * @文件描述 : 表情转换工具
 * *****************************************
 */
public class FaceConversionUtil {

    public static final String DELETE_KEY = "em_delete_delete_expression";
    static String[] emoticon_array_txt = {"[招呼]", "[害羞]", "[调皮]", "[委屈]", "[流汗]", "[敲你]", "[微笑]", "[坏笑]", "[抠鼻]", "[发火]", "[疑问]", "[惊讶]", "[低级]", "[耍酷]"
            , "[尴尬]", "[流泪]", "[流汗]", "[晕倒]", "[偷笑]", "[鼓掌]", "[鼻滴]", "[好色]", "[傻笑]", "[亲亲]"};
    static int[] emoji = {
            R.drawable.e00,
            R.drawable.e01,
            R.drawable.e02,
            R.drawable.e03,
            R.drawable.e04,
            R.drawable.e05,
            R.drawable.e06,
            R.drawable.e07,
            R.drawable.e08,
            R.drawable.e09,
            R.drawable.e10,
            R.drawable.e11,
            R.drawable.e12,
            R.drawable.e13,
            R.drawable.e14,
            R.drawable.e15,
            R.drawable.e16,
            R.drawable.e17,
            R.drawable.e18,
            R.drawable.e19,
            R.drawable.e20,
            R.drawable.e21,
            R.drawable.e22,
            R.drawable.e23,
    };



    private static final Factory spannableFactory = Factory
            .getInstance();

    private static final Map<Pattern, Object> emoticons = new HashMap<Pattern, Object>();


    static {
        for (int i = 0; i < emoji.length; i++) {
            addPattern(emoticon_array_txt[i],emoji[i]);
        }
//        EaseEmojiconInfoProvider emojiconInfoProvider = EaseUI.getInstance().getEmojiconInfoProvider();
//        if(emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null){
//            for(Map.Entry<String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()){
//                addPattern(entry.getKey(), entry.getValue());
//            }
//        }

    }

    /**
     * add text and icon to the map
     * @param emojiText-- text of emoji
     * @param icon -- resource id or local path
     */
    public static void addPattern(String emojiText, Object icon){
        emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon);
    }


    /**
     * replace existing spannable with smiles
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Map.Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Object value = entry.getValue();
                    if(value instanceof String && !((String) value).startsWith("http")){
                        File file = new File((String) value);
                        if(!file.exists() || file.isDirectory()){
                            return false;
                        }
                        spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.setSpan(new ChatImageSpan(context, (Integer) value),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key){
        boolean b = false;
        for (Map.Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static int getSmilesSize(){
        return emoticons.size();
    }


}