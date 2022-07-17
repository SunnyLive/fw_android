package com.fengwo.module_flirt.UI.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.utils.ClickUtil;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_flirt.Interfaces.IFlirtTalentView;
import com.fengwo.module_flirt.Interfaces.IVideoDatingView;
import com.fengwo.module_flirt.Interfaces.RecommendListener;
import com.fengwo.module_flirt.P.FlirtTalentPresenter;
import com.fengwo.module_flirt.P.VideoDatingPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.BannerIndicator;
import com.fengwo.module_flirt.UI.DrawTextView;
import com.fengwo.module_flirt.UI.GridItemDecoration;
import com.fengwo.module_flirt.UI.activity.FlirtCardDetailsActivity;
import com.fengwo.module_flirt.UI.activity.RankActivity;
import com.fengwo.module_flirt.adapter.OnLineTalentAdapter;
import com.fengwo.module_flirt.adapter.VideoDatingAdapter;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.FindDetailBean;
import com.fengwo.module_flirt.bean.FindListDto;
import com.fengwo.module_websocket.Url;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 在线 列表
 */
public class OnLineFragment extends BaseMvpFragment<IFlirtTalentView, FlirtTalentPresenter> implements IFlirtTalentView {


    @BindView(R2.id.rv_include)
    RecyclerView rvInclude;

    @BindView(R2.id.sr_refresh)
    SmartRefreshLayout smartRefreshLayout;

    private FlirtOnlineAdapter onLineTalentAdapter;
    private int page = 1;
    private String tagName = "";
    private StaggeredGridLayoutManager manager;

    private List<CityHost.AdvertListBean> mBanners;
    private String city;
    private String maxAge;
    private String minAge;
    private String sex;

    @Override
    protected FlirtTalentPresenter initPresenter() {
        return new FlirtTalentPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.include_recyclerview;
    }

    private List<CityHost> data = new ArrayList<>();

    @Override
    public void initUI(Bundle savedInstanceState) {
        /*在线列表适配器*/
        initRefreshListener();
        SmartRefreshLayoutUtils.setTransparentBlackText(getActivity(), smartRefreshLayout);
        onLineTalentAdapter = new FlirtOnlineAdapter(data);
//        rvInclude.addItemDecoration(new GridItemDecoration(rvInclude.getContext(), android.R.color.transparent,
//                R.dimen.dp_0, R.dimen.dp_15, R.dimen.dp_6, R.dimen.dp_6, R.dimen.dp_15, R.dimen.dp_15));
        rvInclude.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvInclude.setAdapter(onLineTalentAdapter);
        //  p.getOnlineList(page,tagName);
        /*在线列表点击*/
        onLineTalentAdapter.setOnItemClickListener((adapter, view, pos) -> {
            if (ClickUtil.canClick(600)) {
                ArrayList<CityHost> arrayList = new ArrayList<>();
                arrayList.addAll(adapter.getData());
//            if (isFastClick()||pos==0) return;
////            去掉第一行广告
//            adapter.getData().remove(0);
                if (null != arrayList.get(0).getAdvertList() && arrayList.get(0).getAdvertList().size() != 0) {
                    arrayList.remove(0);
                    toFlirtCardDetails(arrayList, pos - 1);
                } else {
                    toFlirtCardDetails(arrayList, pos);
                }
            }

        });
        initBanner();
    }

    private void toFlirtCardDetails(ArrayList<CityHost> cityHosts, int position) {
        KLog.e("tag", "position=" + position);
        FlirtCardDetailsActivity.start(getContext(), cityHosts, position, city, maxAge, minAge, sex, p.getOnlinePage());
    }

    private void initBanner() {
        mBanners = new ArrayList<>();
        mBannerAdapter = new FlirtBannerAdapter(mBanners);
    }

    private void initRefreshListener() {
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            getOnLineData(true);
        });

        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            getOnLineData(false);
        });
    }

    /**
     * 传入筛选条件，同时刷新列表
     */
    public void filter(String city, String maxAge, String minAge, String sex) {
        this.city = city;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.sex = sex;
        getOnLineData(true);
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        rvInclude.scrollToPosition(0);
    }

    /**
     * 获取在线数据
     */
    private void getOnLineData(boolean isRefresh) {
        p.getOnlineList(isRefresh, city, maxAge, minAge, sex);
    }

    @Override
    public void onResume() {
        super.onResume();
        //页面可见，数据为空，则重新刷新一遍
        if (onLineTalentAdapter != null && onLineTalentAdapter.getData().isEmpty()) {
            getOnLineData(true);
        }

//        if (bannerView != null && !mBanners.isEmpty()) {
//            bannerView.start();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (bannerView != null && !mBanners.isEmpty()) {
//            bannerView.stop();
//        }
    }

    private Banner<CityHost.AdvertListBean, FlirtBannerAdapter> bannerView;
    private FlirtBannerAdapter mBannerAdapter;

    private static class FlirtBannerAdapter extends BannerImageAdapter<CityHost.AdvertListBean> {
        public FlirtBannerAdapter(List<CityHost.AdvertListBean> mData) {
            super(mData);
        }

        @Override
        public void onBindView(BannerImageHolder holder, CityHost.AdvertListBean data, int position, int size) {
            ImageLoader.loadImg(holder.imageView, data.getImage());
        }

    }

    @Override
    public void setOnLineList(ArrayList<CityHost> records) {
        /*请求在线列表数据*/
        L.e("=====" + records.size());
        onLineTalentAdapter.setNewData(records);
    }

    @Override
    public void getRequestFindListSuccess(BaseListDto<FindListDto> data) {

    }

    @Override
    public void cardLikeSuccess(int id, int position) {

    }

    @Override
    public void getShareUrlSuccess(String url, int type, String imgUrl, String content) {

    }

    @Override
    public void onReceiveFlirtData(boolean isRefresh, List<CityHost> onlineFlirtData) {
        if (isRefresh) {
            smartRefreshLayout.finishRefresh();
            if (onlineFlirtData.isEmpty()) {
                return;
            }
            onLineTalentAdapter.setNewData(onlineFlirtData);
            rvInclude.scrollToPosition(0);
        } else {
            smartRefreshLayout.finishLoadMore();
            if (onlineFlirtData.isEmpty()) {
                toastTip(getResources().getString(R.string.no_more_data));
                return;
            }
            onLineTalentAdapter.addData(onlineFlirtData);
        }
    }

    @Override
    public void setMyOrderList(List<MyOrderDto> data) {

    }

    @Override
    public void onGetFindDetail(int id, FindDetailBean detail, int position) {

    }

    private class FlirtOnlineAdapter extends BaseMultiItemQuickAdapter<CityHost, BaseViewHolder> {

        public FlirtOnlineAdapter(List<CityHost> data) {
            super(data);
            addItemType(0, R.layout.item_filter_home_online_header);
            addItemType(1, R.layout.item_filter_home_online);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, CityHost item) {
            if (helper.getItemViewType() == 1) {
                //昵称
                helper.setText(R.id.tvNickName, item.getNickname());
//                ((DrawTextView) helper.getView(R.id.tvNickName)).setStartDrawAble(ImageLoader.getChannelLevel(item.getVipLevel() == 0 ? 1 : item.getVipLevel()), R.dimen.dp_30, R.dimen.dp_9);
                //个性签名
                String signature = item.getRoomTitle();

                if (!TextUtils.isEmpty(signature)) {
                    helper.setText(R.id.tvSignaturea, signature);
                }
                //年龄、性别
                //  ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);
//                if ((item.getSex() == 0 || item.getSex() == 3) && item.getAge() <= 0) {
//                    helper.setVisible(R.id.tvGender, false);
//                } else {
                if ((item.getSex() == 0 || item.getSex() == 3)){
                    helper.setVisible(R.id.tvGender, true);
                }

                if (item.getAge() <= 0) {
                    helper.setText(R.id.tv_age, null);
                } else {
                    helper.setText(R.id.tv_age, String.valueOf(item.getAge()));
                }

//                    DrawTextView DrawTextView = helper.getView(R.id.tvGender);
                ImageView im_sex = helper.getView(R.id.im_sex);
                im_sex.setImageResource(R.drawable.ic_gender);
                LinearLayout tvGender = helper.getView(R.id.tvGender);
                if (item.getSex() == 1) {
                    tvGender.setBackgroundResource(R.drawable.bg_gender);
                } else {
                    tvGender.setBackgroundResource(R.drawable.shape_corner_girl);

                }
                helper.getView(R.id.im_sex).setSelected(item.getSex() != 1);


                if (item.getBstatus() == 1) {
                    helper.setGone(R.id.im_gif, true);
                    ImageLoader.loadGif(helper.getView(R.id.im_gif), R.drawable.live_cell_gif);
                } else {
                    helper.setGone(R.id.im_gif, false);
                }
                if (item.getBstatus() != 1 && item.getSex() == 0&&item.getAge() == 0) {
                    tvGender.setVisibility(View.GONE);
                }else {
                    tvGender.setVisibility(View.VISIBLE);
                }
                helper.setGone(R.id.im_sex, item.getSex() >= 1);
                helper.setGone(R.id.tv_age, item.getAge() >= 1);


                //头像
                ImageLoader.loadRouteImg(helper.getView(R.id.ivGallery), item.getCover(), DensityUtils.dp2px(getActivity(),8), true);
                //标签
                String tagName = item.getTagName();
                LinearLayoutCompat tagContainer = helper.getView(R.id.containerPlayerTag);
                tagContainer.setVisibility(TextUtils.isEmpty(tagName) ? View.GONE : View.VISIBLE);
                tagContainer.removeAllViews();
                if (!TextUtils.isEmpty(tagName)) {
                    addTag2Container(tagName, tagContainer);
                }
            } else {
                setHeaderBannerData(helper, item.getAdvertList());
            }
        }

        /**
         * 填充头部Banner数据
         *
         * @param helper     {@link BaseViewHolder}
         * @param advertList banners数据集
         */
        private void setHeaderBannerData(BaseViewHolder helper, List<CityHost.AdvertListBean> advertList) {
            if (advertList == null || advertList.size() == 0) return;
            //find banner View
            bannerView = helper.getView(R.id.flirtBanner);
            BannerIndicator bannerIndicator = helper.getView(R.id.bannerIndicator);
            if (bannerView.getAdapter() == null) {
                bannerView.setAdapter(mBannerAdapter);
                //设置banner点击事件
                mBannerAdapter.setOnBannerListener(this::handleBannerClickListener);
            }
            ImageView im_pmd = helper.getView(R.id.im_pmd);
            ImageLoader.loadGif(im_pmd, R.drawable.pic_banner_gif);

            //设置自定义指示器
            bannerView.setIndicator(bannerIndicator);
            //通知指示器，数据大小变更
            bannerIndicator.updatePageCount(advertList.size());
            if (!mBanners.isEmpty()) {
                mBanners.clear();
            }
            mBanners.addAll(advertList);
            //更新banner
            mBannerAdapter.notifyDataSetChanged();
        }

        /**
         * 处理banner点击跳转逻辑
         *
         * @param item 点击的item
         */
        private void handleBannerClickListener(Object item, int position) {
            if (bannerView == null || !(item instanceof CityHost.AdvertListBean)) return;
            Log.e("BannerClickListener", "点击banner ->" + ((CityHost.AdvertListBean) item).getAdvertName());

            if ("同城之星".equals(((CityHost.AdvertListBean) item).getAdvertName())) {
                startActivity(new Intent(getContext(), RankActivity.class));
            } else {
                BrowserActivity.start(mContext, "", ((CityHost.AdvertListBean) item).getContentUrl());

            }
        }

        /**
         * 添加标签
         *
         * @param tagName      标签值
         * @param tagContainer 容器
         */
        private void addTag2Container(String tagName, LinearLayoutCompat tagContainer) {
            if (tagContainer.getChildCount() >= 3) return;
            if (tagName.contains(",")) {
                for (String tag : tagName.split(",")) {
                    if (TextUtils.isEmpty(tag)) {
                        continue;
                    }
                    addTag2Container(tag, tagContainer);
                }
                return;
            }
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.tv_add_tag, null, false);
            AppCompatTextView tagView = view.findViewById(R.id.tagView);
            //文本内容
            tagView.setText(tagName);
            tagView.setEllipsize(TextUtils.TruncateAt.END);
            //有多个标签时，设置需要设置标签之间的间距
            if (tagContainer.getChildCount() > 0) {
                LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(getResources().getDimensionPixelOffset(R.dimen.x6), 0, 0, 0);

                tagView.setLayoutParams(layoutParams);
            }
            //添加标签
            tagContainer.addView(tagView);
        }
    }
}
