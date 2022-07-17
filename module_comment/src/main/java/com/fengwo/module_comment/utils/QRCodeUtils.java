package com.fengwo.module_comment.utils;

import android.graphics.Bitmap;


import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class QRCodeUtils {

    public interface OnGenerQRCodeListener {
        void onSuccess(Bitmap bitmap);
    }

    public interface OnDecodeSuccess {
        void onSuccess(String result);
    }

    public static void generQRCode(String content, int size, int color, OnGenerQRCodeListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(content, size, color);
                    Observable.just(bitmap)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(b -> {
                                listener.onSuccess(b);
                            });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void generQRCode(String content, int size, OnGenerQRCodeListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(content, size);
                Observable.just(bitmap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(b -> {
                            listener.onSuccess(b);
                        });

            }
        }).start();
    }

    public static void decodeQR(String path, OnDecodeSuccess l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = QRCodeDecoder.syncDecodeQRCode(path);
                l.onSuccess(result);
            }
        }).start();

    }
}
