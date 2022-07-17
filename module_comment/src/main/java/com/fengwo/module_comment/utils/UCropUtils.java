package com.fengwo.module_comment.utils;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.fengwo.module_comment.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class UCropUtils {
    public static void startCrop(AppCompatActivity activity, Uri originUri) {
        File file = new File(activity.getFilesDir(), System.currentTimeMillis() + ".jpg");
        Uri outputUri = Uri.fromFile(file);
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.white));
        UCrop.of(originUri, outputUri).withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .withOptions(options).start(activity);
    }
}