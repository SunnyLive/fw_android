package com.fengwo.module_comment.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class MImagePicker {
    public final static int TYPE_IMG = 10001;
    public final static int TYPE_VEDIO = 10002;
    public final static int TYPE_ALL = 10003;

//    public static void openImagePicker(Activity activity, int requestCode) {
//        openImagePicker(activity, TYPE_ALL, 1, requestCode);
//    }

    public static void openImagePicker(Activity activity, int type, int requestCode) {
//        openImagePicker(activity, type, 1, requestCode);
        startImagepicker(activity, type, requestCode);
    }

//    public static void openImagePicker(Fragment fragment, int requestCode) {
//        openImagePicker(fragment, TYPE_ALL, 1, requestCode);
//    }

//    public static void openImagePicker(Activity activity, int type, int max, int requestCode) {
//        Set<MimeType> t = MimeType.ofImage();
//        switch (type) {
//            case TYPE_IMG:
//                t = MimeType.ofImage();
//                break;
//            case TYPE_VEDIO:
//                t = MimeType.ofVideo();
//                break;
//            case TYPE_ALL:
//                t = MimeType.ofAll();
//                break;
//        }
//        Matisse.from(activity)
//                .choose(t)
//                .countable(true)
//                .maxSelectable(max)
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .theme(R.style.Matisse_Zhihu)
//                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
//                        new CaptureStrategy(true, "fengwoImg", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fengwoImg"))
//                .imageEngine(new MyGlideEngine())
//                .thumbnailScale(0.85f)
//                .forResult(requestCode);
//    }

    private static void startImagepicker(Context context, int type, int requestCode) {
        Intent intent = new Intent();
        switch (type) {
            case TYPE_IMG:
                intent.setType("image/*");
                break;
            case TYPE_VEDIO:
                intent.setType("video/*");
                break;
            case TYPE_ALL:
                intent.setType("video/*;image/*");
                break;
        }
        /* action的值可以使用Intent.ACTION_GET_CONTENT这个Action也可以使用Intent.ACTION_PICK区别在于前一个需要二次选择系统的程序后者直接到系统视频 */
        intent.setAction(Intent.ACTION_PICK);
        /* 取得相片后返回本画面 */
        if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
    }

//    public static void openImagePicker(Fragment fragment, int type, int max, int requestCode) {
//        Set<MimeType> t = MimeType.ofImage();
//        switch (type) {
//            case TYPE_IMG:
//                t = MimeType.ofImage();
//                break;
//            case TYPE_VEDIO:
//                t = MimeType.ofVideo();
//                break;
//            case TYPE_ALL:
//                t = MimeType.ofAll();
//                break;
//        }
//        Matisse.from(fragment)
//                .choose(t, false)
//                .countable(true)
//                .maxSelectable(max)
//                .capture(true)  //是否可以拍照
//                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
//                        new CaptureStrategy(true, "fengwoImg", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fengwoImg"))
//
////                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
////                .gridExpectedSize(fragment.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .thumbnailScale(0.85f)
//                .imageEngine(new MyGlideEngine())
//                .forResult(requestCode);
//    }


//    static public List<Uri> getImageUriList(Intent data) {
//        return Matisse.obtainResult(data);
//    }

    static public String getImagePath(Context context, Intent data) {
        String url = getPhotoFromPhotoAlbum.getRealPathFromUri(context, data.getData());
        return url;
    }


//    static public List<String> getImagePathList(Intent data) {
//        return Matisse.obtainPathResult(data);
//    }

    static public void toCrop(Activity mContext, Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        mContext.startActivityForResult(intent, requestCode);
    }


}
