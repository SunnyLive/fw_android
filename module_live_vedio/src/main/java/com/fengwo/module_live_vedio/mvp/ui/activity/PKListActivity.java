package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.PKGroupListDTO;
import com.fengwo.module_live_vedio.mvp.dto.PKPlayerInfoDTO;
import com.fengwo.module_live_vedio.mvp.dto.PKSingleListDTO;

import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

public class PKListActivity extends BaseListActivity {

    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_GROUP = 1;
    public static final int TYPE_UNION = 2;

    public static void startForSingle(Context context) {
        Intent intent = new Intent(context, PKListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", TYPE_SINGLE);
        context.startActivity(intent);
    }

    public static void startForGroup(Context context) {
        Intent intent = new Intent(context, PKListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", TYPE_GROUP);
        context.startActivity(intent);
    }

    public static void startForUnion(Context context) {
        Intent intent = new Intent(context, PKListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", TYPE_UNION);
        context.startActivity(intent);
    }

    private int type;

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type", TYPE_SINGLE);
        new ToolBarBuilder().showBack(true).setTitle("PK列表").setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black).build();
//        setTitleBackground(getResources().getColor(R.color.white));
        super.initView();
    }

    @Override
    public Flowable setNetObservable() {
        LiveApiService api = new RetrofitUtils().createApi(LiveApiService.class);
        switch (type) {
            case TYPE_SINGLE:
                //真实接口
//                return api.getSinglePKList(page + PAGE_SIZE);
                // 模拟数据
                return Flowable.create((FlowableOnSubscribe<HttpResult>) emitter -> {
                    BaseListDto<PKSingleListDTO> list = new BaseListDto<>();
                    list.records = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        PKSingleListDTO dto = new PKSingleListDTO();
                        dto.firstMember = new PKPlayerInfoDTO("http://pic1.win4000.com/pic/8/70/a4a088abd3_250_350.jpg", "鲜艳的空中");
                        dto.twoMember = new PKPlayerInfoDTO("http://pic1.win4000.com/pic/9/1d/0a5ee1002e_250_350.jpg", "专业的年代");
                        list.records.add(dto);
                    }
                    HttpResult<BaseListDto> result = new HttpResult<>();
                    result.status = HttpResult.SUCCESS;
                    result.data = list;
                    emitter.onNext(result);
                }, BackpressureStrategy.ERROR);
            case TYPE_GROUP:
                return api.getGroupPKList(page + PAGE_SIZE);
            case TYPE_UNION:
                return null;
            default:
                return null;
        }
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_pk_list;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, Object item, int position) {
        ImageView ivSelf = helper.getView(R.id.ivTeammateBack);
        ImageView ivEnemy = helper.getView(R.id.ivEnemyBack);
        ImageView ivTeammateFirst = helper.getView(R.id.ivTeammateFirst);
        ImageView ivTeammateSecond = helper.getView(R.id.ivTeammateSecond);
        ImageView ivEnemyFirst = helper.getView(R.id.ivEnemyFirst);
        ImageView ivEnemySecond = helper.getView(R.id.ivEnemySecond);
        switch (type) {
            case TYPE_SINGLE:
                PKSingleListDTO single = (PKSingleListDTO) item;
                if (single.firstMember != null) {
                    ImageLoader.loadImg(ivSelf, single.firstMember.headImg);
                    helper.setText(R.id.tvSelfName, single.firstMember.nickname);
                }
                if (single.twoMember != null) {
                    ImageLoader.loadImg(ivEnemy, single.twoMember.headImg);
                    helper.setText(R.id.tvEnemyName, single.twoMember.nickname);
                }
                ivTeammateFirst.setVisibility(View.INVISIBLE);
                ivTeammateSecond.setVisibility(View.INVISIBLE);
                ivEnemyFirst.setVisibility(View.INVISIBLE);
                ivEnemySecond.setVisibility(View.INVISIBLE);
                break;
            case TYPE_GROUP:
                PKGroupListDTO group = (PKGroupListDTO) item;
                if (group.firstMember != null) {
                    ImageLoader.loadImg(ivSelf, group.firstMember.get(0).headImg);
                    helper.setText(R.id.tvSelfName, group.firstMember.get(0).nickname);
                }
                if (group.twoMember != null) {
                    ImageLoader.loadImg(ivEnemy, group.twoMember.get(0).headImg);
                    helper.setText(R.id.tvEnemyName, group.twoMember.get(0).nickname);
                }
                if (group.firstMember != null && group.firstMember.size() > 1)
                    ImageLoader.loadImg(ivTeammateFirst, group.firstMember.get(1).headImg);
                if (group.firstMember != null && group.firstMember.size() > 2)
                    ImageLoader.loadImg(ivTeammateSecond, group.firstMember.get(2).headImg);
                if (group.twoMember != null && group.twoMember.size() > 1)
                    ImageLoader.loadImg(ivEnemyFirst, group.twoMember.get(1).headImg);
                if (group.twoMember != null && group.twoMember.size() > 2)
                    ImageLoader.loadImg(ivEnemySecond, group.twoMember.get(2).headImg);

                ivTeammateFirst.setVisibility(View.VISIBLE);
                ivTeammateSecond.setVisibility(View.VISIBLE);
                ivEnemyFirst.setVisibility(View.VISIBLE);
                ivEnemySecond.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pk_list;
    }
}