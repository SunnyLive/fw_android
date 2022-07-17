package com.fengwo.module_vedio.mvp.ui.activity.shortvideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.api.API;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.eventbus.PlayShortVideoEvent;
import com.fengwo.module_vedio.mvp.ui.adapter.FragmentVpAdapter;
import com.fengwo.module_vedio.mvp.ui.fragment.shortvideo.ShortVideoChildFragment;
import com.fengwo.module_vedio.mvp.ui.fragment.shortvideo.ShortVideoCommentsFragment;
import com.fengwo.module_vedio.mvp.ui.pop.DeleteVideoPop;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.tencent.liteav.demo.play.SuperPlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/25
 */
@Route(path = ArouterApi.SHORT_VIDEO)
public class ShortVideoActivity extends BaseSuperPlayerActivity {

    @BindView(R2.id.superVodPlayerView)
    SuperPlayerView mSuperPlayerView;
    @BindView(R2.id.layout_player)
    RelativeLayout layoutPlayer;
    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    @BindView(R2.id.tv_des_title)
    TextView tvDesTitle;
    @BindView(R2.id.tv_des_des)
    TextView tvDesDes;
    @BindView(R2.id.iv_des_close)
    ImageView ivDesClose;
    @BindView(R2.id.rl_des)
    RelativeLayout rlDes;
    @BindView(R2.id.iv_more)
    ImageView ivMore;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentVpAdapter fragmentVpAdapter;
    private CompositeDisposable compositeDisposable;

    @Autowired
    UserProviderService userProviderService;
    private int movieId;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    public static void startShortVideo(Context context, int albumId, VideoHomeShortModel videoHomeShortModel) {//专辑传albumID，短片传model
        Intent intent = new Intent(context, ShortVideoActivity.class);
        intent.putExtra("albumId", albumId);
        intent.putExtra("model", videoHomeShortModel);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        int albumId = getIntent().getIntExtra("albumId", 0);
        int uid = getIntent().getIntExtra("uid", 0);//个人中心过来才需要传uid
        VideoHomeShortModel model = (VideoHomeShortModel) getIntent().getSerializableExtra("model");
        if (model==null){
            model = new VideoHomeShortModel();
            model.albumId = albumId;
            if (uid>0) model.userId = uid;
        }
        titleList.add("视频");
        titleList.add("评论0");
        fragmentList.add(ShortVideoChildFragment.getInstance(albumId, model));
        fragmentList.add(ShortVideoCommentsFragment.getInstance());
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(fragmentVpAdapter);
        tabLayout.setupWithViewPager(viewPager);

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RxBus.get().toObservable(PlayShortVideoEvent.class)
                .compose(bindToLifecycle())
                .subscribe(shortVideoEvent -> {
                    playVideo(shortVideoEvent.url);
                    setDesContent(shortVideoEvent);
                    movieId = shortVideoEvent.movieId;
                    ((ShortVideoCommentsFragment)fragmentList.get(1)).setMovieId(shortVideoEvent.movieId);
                    if (userProviderService.getUserInfo().getId() == shortVideoEvent.userId){
                        ivMore.setVisibility(View.VISIBLE);
                    }else {
                        ivMore.setVisibility(View.GONE);
                    }
                }));
    }

    private void setDesContent(PlayShortVideoEvent shortVideoEvent) {
        tvDesDes.setText("简介："+shortVideoEvent.movieDes);
        tvDesTitle.setText(shortVideoEvent.movieTitle);
        titleList.clear();
        titleList.add("视频");
        titleList.add("评论"+shortVideoEvent.comments);
        fragmentVpAdapter.notifyDataSetChanged();
    }


    @Override
    protected int getContentView() {
        return R.layout.vedio_activity_short_video;
    }

    @OnClick({R2.id.iv_des_close,R2.id.iv_more})
    void onClick(View v){
        if (v.getId() == R.id.iv_des_close) {
            rlDes.setVisibility(View.GONE);
        }else if (v.getId() == R.id.iv_more){
            DeleteVideoPop deleteVideoPop = new DeleteVideoPop(this, new DeleteVideoPop.onDeleteListener() {
                @Override
                public void onDelete() {
                    deleteVideo();
                }
            });
            deleteVideoPop.showPopupWindow();
        }
    }

    private void deleteVideo(){
        Map map = new HashMap();
        map.put("movieId",movieId);
        map.put("userId",userProviderService.getUserInfo().getId());
        compositeDisposable.add(new RetrofitUtils().createApi(VedioApiService.class)
                .delShortVideo(createRequestBody(map))
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        toastTip(data.description);
                        if (data.isSuccess()){
                            finish();
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        toastTip(msg);
                    }
                }));
    }
    public RequestBody createRequestBody(Map map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }


    public void showDes(){
        rlDes.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable.size()>0&&!compositeDisposable.isDisposed()){
            compositeDisposable.isDisposed();
            compositeDisposable = null;
        }
    }
}

