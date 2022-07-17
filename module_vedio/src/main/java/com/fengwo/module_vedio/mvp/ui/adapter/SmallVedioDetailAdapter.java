package com.fengwo.module_vedio.mvp.ui.adapter;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.eventbus.SmallVedioSeekBarTrackingTouchEvent;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SmallVedioDetailAdapter extends BaseQuickAdapter<VideoHomeShortModel, BaseVH> {
    public final static String TAG = "SmallVedioDetailAdapter";
    private boolean isFirst = true;
    private int startPosition;
    private SparseBooleanArray isAddViews;
    private int userId;

    public SmallVedioDetailAdapter(@Nullable List<VideoHomeShortModel> data, int startPosition, int userId) {
        super(R.layout.vedio_item_smallvedio_detail, data);
        this.startPosition = startPosition;
        isAddViews = new SparseBooleanArray();
        this.userId = userId;
    }

    public void hideConver(int position) {
        ImageView iv = (ImageView) getViewByPosition(position, R.id.iv_fengmian);
        if (null != iv)
            iv.setVisibility(View.GONE);
    }

    public void showConver(int position) {
        ImageView iv = (ImageView) getViewByPosition(position, R.id.iv_fengmian);
        if (null != iv)
            iv.setVisibility(View.VISIBLE);
    }

    public TXCloudVideoView getVideoView(int position) {
        return (TXCloudVideoView) getViewByPosition(position, R.id.detail_player);
    }

    public void seekTo(int progress, int position) {
        SeekBar seekBar = (SeekBar) getViewByPosition(position, R.id.seekbar);
        if (null != seekBar)
            seekBar.setProgress(progress);
    }

    /**
     * 添加浏览数
     *
     * @param id
     */
    private void addViews(int id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id + "");
        new RetrofitUtils().createApi(VedioApiService.class)
                .addSmallViews(HttpUtils.createRequestBody(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            L.e("video id:" + id + "------addviews success------");
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        L.e("video id:" + id + "------addviews error-------" + msg);
                    }
                });
    }

    @Override
    protected void convert(@NonNull BaseVH helper, VideoHomeShortModel item) {
        SeekBar seekBar = helper.getView(R.id.seekbar);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SmallVedioSeekBarTrackingTouchEvent event = new SmallVedioSeekBarTrackingTouchEvent(seekBar.getProgress());
                RxBus.get().post(event);
            }
        });
        ImageLoader.loadImg(helper.getView(R.id.iv_fengmian), item.cover);
        helper.addOnClickListener(R.id.btn_header)
                .addOnClickListener(R.id.btn_like)
                .addOnClickListener(R.id.btn_comment)
                .addOnClickListener(R.id.btn_share)
                .addOnClickListener(R.id.btn_more)
                .addOnClickListener(R.id.btn_attention);
        ImageView iv = helper.getView(R.id.iv_like);
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.userUrl);
        helper.setText(R.id.tv_like, item.likes + "");
        helper.setText(R.id.tv_comment, item.comments + "");
        helper.setText(R.id.tv_share, item.shares + "");
        helper.setText(R.id.tv_authorname, "@" + item.userName);
        helper.setText(R.id.tv_des, item.description);
        if (item.isLike) {
            iv.setImageResource(R.drawable.vedio_icon_smalldetail_like);
        } else
            iv.setImageResource(R.drawable.vedio_icon_smalldetail_unlike);

        if (item.userId == userId) {
            helper.setVisible(R.id.btn_more, true);
            helper.setVisible(R.id.btn_attention, false);
        } else {
            helper.setVisible(R.id.btn_more, false);
            helper.getView(R.id.btn_attention).setVisibility(item.isAttention == 1 ? View.GONE : View.VISIBLE);
        }
    }


    public void isShowPlayerIcon(int position, boolean isShow) {
        ImageView iv = (ImageView) getViewByPosition(position, R.id.iv_player);
        if (null == iv) return;
        iv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void isShowProgress(int position, boolean isShow) {
        ProgressBar pb = (ProgressBar) getViewByPosition(position, R.id.progress);
        if (null == pb) return;
        pb.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void changeLike(int position, boolean like) {
        ImageView iv = (ImageView) getViewByPosition(position, R.id.iv_like);
        TextView tv = (TextView) getViewByPosition(position, R.id.tv_like);
        if (null == iv || null == tv) return;
        if (like) {
            iv.setImageResource(R.drawable.vedio_icon_smalldetail_like);
            tv.setText(Integer.parseInt(tv.getText().toString().trim()) + 1 + "");
        } else {
            iv.setImageResource(R.drawable.vedio_icon_smalldetail_unlike);
            tv.setText(Integer.parseInt(tv.getText().toString().trim()) - 1 + "");
        }
    }

    public void setcommentNum(int position, int num) {
        TextView tvComment = (TextView) getViewByPosition(position, R.id.tv_comment);
        tvComment.setText(num + "");
    }

    public void setAttention(int position, int isAttention) {
        ImageView iv = (ImageView) getViewByPosition(position, R.id.btn_attention);
        getData().get(position).isAttention = isAttention;
        if (null != iv) {
            if (isAttention > 0) {
                iv.setSelected(true);
                iv.setVisibility(View.GONE);
            } else {
                iv.setSelected(false);
                iv.setVisibility(getData().get(position).userId == userId ? View.GONE : View.VISIBLE);
            }
        }
    }

    public void setAttentionSelected(int position) {
        ImageView iv = (ImageView) getViewByPosition(position, R.id.btn_attention);
        if (null != iv)
            iv.setSelected(true);

    }
}
