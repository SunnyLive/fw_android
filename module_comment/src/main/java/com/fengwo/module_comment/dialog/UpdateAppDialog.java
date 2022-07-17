package com.fengwo.module_comment.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.bean.VensionDto;
import com.fengwo.module_comment.utils.UpdateUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import razerdp.blur.thread.ThreadPoolManager;

/**
 * 版本升级弹窗
 *
 * @Author gukaihong
 * @Time 2020/12/10
 */
public class UpdateAppDialog {
    private static boolean isFirst = true;
    private File file = null;

    /**
     * 只弹一次
     */
    public void showDialogOnlyOne(Activity context, VensionDto version) {
        if (isFirst) {
            showDialog(context, version);
        }
    }

    public UpdateAppDialog showDialog(Activity context, VensionDto version) {
        if (Integer.parseInt(version.getAppVersion()) <= UpdateUtil.getVersionCode(context)) {
            return this;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.main_dialog_vension, null);
        TextView tvDes = view.findViewById(R.id.tv_des);
        TextView tvUpdate = view.findViewById(R.id.tv_update);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        if (version.getForceUpdate() == 1) {//TODO 后台暂时没做 如果做了请把这个判断删掉 还有点击事件
            tvCancel.setVisibility(View.GONE);
        }

        tvUpdate.setOnClickListener(l -> {
            //if (version.isForce()) {
            // if(BuildConfig.DEBUG){
            if (!TextUtils.isEmpty(version.getAndroidUrl())) {
                loadNewVersionProgress(context, context, version.getAndroidUrl());
            } else {
                String packName = BaseApplication.mApp.getPackageName();
                UpdateUtil.toMarket(context, packName, null);
            }
//            }else{

//            }
            // } else {
//            String packName = BaseApplication.mApp.getPackageName();
//            UpdateUtil.toMarket(context, packName, null);
            //   }

        });
        tvDes.setText(version.getUpdateInfo());
        tvDes.setMovementMethod(ScrollingMovementMethod.getInstance());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(version.getForceUpdate() != 1);   //返回键dismiss
        AlertDialog dialog = builder.create();
        tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(version.getForceUpdate() != 1);
        dialog.show();
        isFirst = false;
        return this;
    }

    /**
     * 重新安装
     */
    public void reInstallApk(Activity activity) {
        if (file != null) {
            installApk(activity, file);
        }
    }

    private void loadNewVersionProgress(final Activity activity, final Context context, final String url) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在更新apk");
        pd.show();
        //启动子线程下载任务
        ThreadPoolManager.execute(() -> {
            try {
                file = getFileFromServer(url, pd);
                Thread.sleep(3000);
                boolean installAllowed;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    installAllowed = context.getPackageManager().canRequestPackageInstalls();//26 判断是否有未知应用权限
                    if (installAllowed) {//有权限
                        installApk(context, file);
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 1);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + context.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivityForResult(intent, 8);
                        //打开权限回调 直接  installApk(file);
                    }
                } else {
                    installApk(context, file);
                }
                pd.dismiss(); //结束掉进度条对话框
            } catch (Exception e) {
                //下载apk失败
                //          showToast("请链接wifi在进行下载");
                pd.dismiss();
                e.printStackTrace();
            }
        });
    }

    /**
     * 从服务器获取apk文件的代码
     * 传入网址uri，进度条对象即可获得一个File文件
     * （要在子线程中执行哦）
     */
    private File getFileFromServer(String uri, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            long time = System.currentTimeMillis();//当前时间的毫秒数
            File file = new File(Environment.getExternalStorageDirectory() + "/Download", time + "111.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    //覆盖安装
    private void installApk(Context context, File file) {
        if (file == null) {
            return;
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 24) { //适配安卓7.0
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkFileUri = FileProvider.getUriForFile(context.getApplicationContext(),
                    "fengwoImg", file);
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);//这里是8.0的打开文件需要FileProvider权限 具代码在最下方给出
            intent.setAction("android.intent.action.VIEW");
            //      intent.setDataAndType(apkFileUri, HomeActivity.this.getContentResolver().getType(apkFileUri));
            intent.setDataAndType(apkFileUri, "application/vnd.android.package-archive");

        } else {
            //执行动作
            intent.setAction(Intent.ACTION_VIEW);
            //执行的数据类型
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


}
