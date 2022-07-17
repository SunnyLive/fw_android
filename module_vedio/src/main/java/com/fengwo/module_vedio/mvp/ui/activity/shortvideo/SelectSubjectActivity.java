package com.fengwo.module_vedio.mvp.ui.activity.shortvideo;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.mvp.dto.AlbumDto;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Flowable;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/2
 */
public class SelectSubjectActivity extends BaseListActivity<AlbumDto> {

    private final static String PAGE_SIZE = ",10";

    @Override
    public Flowable setNetObservable() {
        String pageParams = page+PAGE_SIZE;
        return new RetrofitUtils().createApi(VedioApiService.class).getMyAlbumList(pageParams);
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        p.getListData(page);
    }

    @Override
    protected void initView() {
        super.initView();
//        setTitleBackground(Color.WHITE);
        SmartRefreshLayoutUtils.setTransparentBlackText(this,smartRefreshLayout);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("所属专题")
                .setTitleColor(R.color.text_33)
                .setRight1Img(R.drawable.ic_add_black, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(BuildSubjectActivity.class);
                    }
                })
                .build();
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_select_subject;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, AlbumDto item, int position) {
        helper.setText(R.id.tv_name,item.getName());
        helper.setText(R.id.tv_series,item.getNum()+"集");
        ImageLoader.loadImg(helper.getView(R.id.iv_cover),item.getCover());

        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title",adapter.getData().get(position).getName());
                intent.putExtra("albumId",adapter.getData().get(position).getId());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_baselist;
    }
}
