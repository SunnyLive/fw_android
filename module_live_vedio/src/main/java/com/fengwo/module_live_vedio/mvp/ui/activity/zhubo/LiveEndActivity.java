package com.fengwo.module_live_vedio.mvp.ui.activity.zhubo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_live_vedio.R;

import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.ChannelShareInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.CloseLiveDto;
import com.fengwo.module_live_vedio.mvp.dto.StartLivePushDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LiveEndGiftAdapter;
import com.fengwo.module_live_vedio.mvp.ui.df.ShareCodeDialog;
import com.fengwo.module_live_vedio.utils.LocalCacheUtils;
import com.fengwo.module_live_vedio.utils.QRCodeEncoder;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import cc.shinichi.library.tool.ui.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.umeng.socialize.bean.PlatformName.WEIXIN_CIRCLE;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/17
 */
public class LiveEndActivity extends BaseMvpActivity {
    @BindView(R2.id.iv_head)
    CircleImageView ivHead;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_time)
    TextView tvTime;
    @BindView(R2.id.tv_look_people)
    TextView tvLookPeople;
    @BindView(R2.id.tv_honey_value)
    TextView tvHoneyValue;
    @BindView(R2.id.tv_new_fans)
    TextView tvNewFans;
    @BindView(R2.id.tv_sure)
    TextView tvSure;
    @BindView(R2.id.tv_honey_text)
    TextView tvHoneyText;
    @BindView(R2.id.tv_new_fans_text)
    TextView tvNewFansText;
    @BindView(R2.id.recyclerViewGift)
    RecyclerView recyclerView;

    @BindView(R2.id.im_close)
    ImageView im_close;

    @BindView(R2.id.tv_null)
    TextView tv_null;
    @BindView(R2.id.rl_view)
    RelativeLayout rl_view;


    private boolean isHost = false;
    private int userId = -1;
    private CloseLiveDto liveDto;
    WindowManager wm;
    private ImageView imEwm,  fxIvHead;;
    JumpInvtationDataBean jumpInvtationDataBean;

    @Override
    protected void initView() {
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        isHost = getIntent().getBooleanExtra("isHost", false);
        userId = getIntent().getIntExtra("userId", 0);
        liveDto = getIntent().getParcelableExtra("data");
        if(null!=getIntent().getParcelableExtra("jumpdata")){
            jumpInvtationDataBean = getIntent().getParcelableExtra("jumpdata");
        }
        setNewView();
        if (liveDto != null) {
            tvName.setText(liveDto.getNickname());
//            TimeUtils.cal()
            tvHoneyValue.setText( DataFormatUtils.formatNumberGift(liveDto.getProfit()) + "");
            tvLookPeople.setText(liveDto.getLookTimes() + "");
//            tvLike.setText(liveDto.getProfit()+"");
            tvNewFans.setText(liveDto.getFansNums() + "");
            tvTime.setText(TimeUtils.int2String(liveDto.getLiveTime()));
        }
        if (isHost) {
            tvHoneyText.setVisibility(View.VISIBLE);
            tvHoneyValue.setVisibility(View.VISIBLE);
            tvNewFansText.setVisibility(View.VISIBLE);
            tvNewFans.setVisibility(View.VISIBLE);
            tvSure.setVisibility(View.VISIBLE);
        } else {
            tvHoneyText.setVisibility(View.GONE);
            tvHoneyValue.setVisibility(View.GONE);
            tvNewFansText.setVisibility(View.GONE);
            tvNewFans.setVisibility(View.GONE);
            tvSure.setVisibility(View.GONE);
        }

        getRecieveGift();
    }

    private void getRecieveGift() {
        if(null==liveDto.getReceiveGite()){
            tv_null.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            LiveEndGiftAdapter liveEndGiftAdapter = new LiveEndGiftAdapter();
            recyclerView.setAdapter(liveEndGiftAdapter);
            liveEndGiftAdapter.setNewData(liveDto.getReceiveGite());
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_live_end;
    }
    private LocalCacheUtils localCacheUtils;
    private Bitmap bitmap;
    private Bitmap btm;
    @OnClick({R2.id.tv_sure,R2.id.im_close})
    void onClick(View view) {
        if (view.getId() == R.id.tv_sure) {
            if(null!=myView){
                ShareCodeDialog dialog = new ShareCodeDialog();
                dialog.setOnItemClickListener(new ShareCodeDialog.OnItemClickListener() {
                    @Override
                    public void WeiXinShare() {
                        try {
                            rl_view.addView(myView);

                            localCacheUtils = new LocalCacheUtils();
                            bitmap = QRCodeEncoder.createCode(liveDto.getShareUrl(),null , 100);
                            imEwm.setImageBitmap(bitmap);
                            imEwm.post(new Runnable() {
                                @Override
                                public void run() {
                                    btm = loadBitmapFromView(myView);
                                    share(btm,SHARE_MEDIA.WEIXIN);
//                            long time = System.currentTimeMillis();
//                            saveBitmap(btm, time + ".JPEG");
                                }
                            });

                        }catch (IllegalStateException e){

                        }

                    }

                    @Override
                    public void WeiXinCircleShare() {
                        try {
                            rl_view.addView(myView);

                            localCacheUtils = new LocalCacheUtils();
                            bitmap = QRCodeEncoder.createCode(liveDto.getShareUrl(),null , 100);
                            imEwm.setImageBitmap(bitmap);
                            imEwm.post(new Runnable() {
                                @Override
                                public void run() {
                                    btm = loadBitmapFromView(myView);
                                    share(btm,SHARE_MEDIA.WEIXIN_CIRCLE);
//                            long time = System.currentTimeMillis();
//                            saveBitmap(btm, time + ".JPEG");
                                }
                            });

                        }catch (IllegalStateException e){

                        }

                    }
                });
                dialog.show(getSupportFragmentManager(), "share");



            }



    //        finish();
        }else if(view.getId() == R.id.im_close){
            onBackPressed();
        }
    }
    View myView;

    private void setNewView(){
         myView = LayoutInflater.from(this).inflate(R.layout.view_endlive_fx, null, false);

        TextView tv_name = myView.findViewById(R.id.tv_name);
        tv_name.setText(liveDto.getNickname());
        TextView tv_id = myView.findViewById(R.id.tv_id);
        tv_id.setText("蜂窝号："+liveDto.getChannelId());
        RecyclerView  recyclerViewGift = myView.findViewById(R.id.recyclerViewGift);
        TextView myName = myView.findViewById(R.id.tv_time);
        TextView tv_look_people = myView.findViewById(R.id.tv_look_people);
        TextView tv_new_fans = myView.findViewById(R.id.tv_new_fans);
        TextView tv_honey_value = myView.findViewById(R.id.tv_honey_value);

       // myName.setText(liveDto.getNickname());
        tv_honey_value.setText( DataFormatUtils.formatNumberGift(liveDto.getProfit()) + "");
        tv_look_people.setText(liveDto.getLookTimes() + "");
        tv_new_fans.setText(liveDto.getFansNums() + "");
        myName.setText(TimeUtils.int2String(liveDto.getLiveTime()));

        fxIvHead = myView.findViewById(R.id.iv_head);

        ImageLoader.loadImgs(fxIvHead,liveDto.getHeadImg()+"");
          imEwm = myView.findViewById(R.id.im_ewm);

        TextView tv_null = myView.findViewById(R.id.tv_null);
        if(null==liveDto.getReceiveGite()){
            tv_null.setVisibility(View.VISIBLE);
        }else {
            recyclerViewGift.setLayoutManager(new GridLayoutManager(this, 3));
            LiveEndGiftAdapter liveEndGiftAdapter = new LiveEndGiftAdapter();
            recyclerViewGift.setAdapter(liveEndGiftAdapter);
            liveEndGiftAdapter.setNewData(liveDto.getReceiveGite());
        }
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_view.removeView(myView);
            }
        });
    }
    //关注
    public void attention(String id) {
        new RetrofitUtils().createApi(LiveApiService.class).addAttention(id)
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(LiveEndActivity.this, data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(LiveEndActivity.this, msg);
                    }
                });

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        if(w==0){
            loadBitmapFromView(v);
            return null;
        }
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(v.getLeft(), v.getTop(), w + v.getLeft(), h + v.getTop());
        v.draw(c);

        return bmp;
    }
    /*
     * 保存文件，文件名为当前日期
     */
    public void saveBitmap(Bitmap bitmap, String bitName) {

        String fileName;
        File file;
        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
            fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/" + bitName;
        }
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
// 插入图库
                //         MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), bitName, null);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));



    }

    public void share(Bitmap data, SHARE_MEDIA shareMedia) {
        if (!CommentUtils.isWeixinAvilible(this)) {
            ToastUtils.showShort(this, "您没有安装微信！！！");
            return;
        }

        UMImage image = new UMImage(this, data);//网络图片
        new ShareAction(this).setPlatform(shareMedia).withText("hello") .setCallback(new UMShareListener(){
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                rl_view.removeView(myView);
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        }).withMedia(image).share();


//        UMWeb web = new UMWeb("http://www.baidu.com");
//        web.setTitle("1");
//        web.setDescription("2");
//
//            web.setThumb(new UMImage(this, "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1583138449941.jpg"));
//
//        new ShareAction(this)
//                .setPlatform(shareMedia)//传入平台
//                .withMedia(web)
//
//                .share();




//        UMImage web = new UMImage(LiveEndActivity.this,data);
//
////        web.setTitle("蜂窝互娱");
////        web.setDescription("蜂窝互娱");
////        web.setThumb(web);
//        new ShareAction(LiveEndActivity.this).setPlatform(shareMedia).withText("hello").withMedia(web).share();
//        new ShareAction(LiveEndActivity.this)
//                .setPlatform(shareMedia)//传入平台
//                .withMedia(web)
//                .share();

    }

    @Override
    public void onBackPressed() {
        if(null!=jumpInvtationDataBean){
            ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTION)
                    .withInt("anchorId", Integer.valueOf(jumpInvtationDataBean.getRoomId()))
                    .withInt("status", 1)
                    .withString("bgUrl", jumpInvtationDataBean.getPic())
                    .navigation();
        }

        super.onBackPressed();
    }
}
