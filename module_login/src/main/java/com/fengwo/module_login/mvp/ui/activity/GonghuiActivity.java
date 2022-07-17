/*
 *  为后辈们做点贡献
 *
 *  这里原本是没有注释的
 *
 *  这个页面应该是搜索工会的页面
 *
 *  当主播没有工会时  会进入这个页面
 *
 *  进行搜索工会  展示工会  并进行申请工会的操作
 *
 *  不用感谢我，请叫我活雷锋 2020-10-27
 *
 * */

package com.fengwo.module_login.mvp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.GonghuiListDto;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@Route(path = ArouterApi.SEARCH_UNION_ACTION)
public class GonghuiActivity extends BaseListActivity<GonghuiListDto> {

    @BindView(R2.id.et_gonghui)
    EditText etGonghui;

    @BindView(R2.id.iv_search_union)
    View mSearch;

    @BindView(R2.id.cb_search)
    CheckBox mCheckBoxSearch;

    private String searchStr;

    private int mUnionType = ANCHOR_UNION;

    public static final int ANCHOR_UNION = 0x00001;
    public static final int EXPERT_UNION = 0x00002;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // UnionType 1 是主播搜索  2 是达人搜索
        mUnionType = getIntent().getIntExtra("unionType", 1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setWhiteTitle("公会列表");
        SmartRefreshLayoutUtils.setWhiteBlackText(this, smartRefreshLayout);
        mSearch.setOnClickListener(v -> {
            String searchStr = etGonghui.getText().toString().trim();
            if (TextUtils.isEmpty(searchStr)) {
                toastTip("请输入工会名称搜索！");
            } else {
                GonghuiActivity.this.searchStr = searchStr;
                p.getListData(1);
            }
        });

        mCheckBoxSearch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonView.setText("取消");
                String searchStr = etGonghui.getText().toString().trim();
                if (TextUtils.isEmpty(searchStr)) {
                    toastTip("请输入工会名称搜索！");
                } else {
                    GonghuiActivity.this.searchStr = searchStr;
                    p.getListData(1);
                }
            }else {
                buttonView.setText("搜索");
                etGonghui.setText("");
                adapter.getData().clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_gonghuilist;
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.login_item_gonghuilist;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, GonghuiListDto item, int position) {
        helper.setText(R.id.tv_union_id, item.getFamilyCode());
        TextView unionName = helper.getView(R.id.tv_union_name);
        unionName.setText(item.getFamilyName());
        unionName.setSingleLine(true);//设置单行显示
        unionName.setHorizontallyScrolling(true);//横向滚动
        unionName.setMarqueeRepeatLimit(-1);
        unionName.setSelected(true);//开始滚

        helper.getView(R.id.btn_submit).setOnClickListener(view -> {
            switch (mUnionType) {
                case ANCHOR_UNION:
                    applyGonghui(item.getId() + "");
                    break;
                case EXPERT_UNION:
                    applyExpertUnion(item.getId() + "");
                    break;
            }
        });

    }


    @Override
    public Flowable setNetObservable() {
        switch (mUnionType) {
            case ANCHOR_UNION:
                return new RetrofitUtils().createApi(LoginApiService.class)
                        .getGonghui(page + PAGE_SIZE, "[[\"eq\",\"familyCode\",\"" + searchStr + "\"],[\"eq\",\"familyType\",1]]");
            case EXPERT_UNION:
                return searchExpertUnion(searchStr);
        }
        return null;
    }


    /**
     * 搜索达人公会
     *
     * @param unionCode 公会id
     * @return Flowable
     */
    private Flowable searchExpertUnion(String unionCode) {
        String params = "{\"params\": {\"current\": 1,\"keywords\": \"" + unionCode + "\", \"size\": 20  }}";
        RequestBody requestBody = RequestBody.create(params, MediaType.parse("application/json"));
        return new RetrofitUtils().createWenboApi(LoginApiService.class).searchExpertUnion(requestBody);
    }

    private void applyGonghui(String id) {
        new RetrofitUtils().createApi(LoginApiService.class).applyGonghui(id)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>(this) {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            toastTip("申请成功");
                            GonghuiActivity.this.finish();
                        }else {
                            toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }


    /**
     * 申请i撩公会
     *
     * @param id 公会id
     */
    private void applyExpertUnion(String id) {
        String params = "{\"params\": {\"familyId\": " + id + "}}";
        RequestBody requestBody = RequestBody.create(params, MediaType.parse("application/json"));
        new RetrofitUtils().createWenboApi(LoginApiService.class)
                .applyExpertUnion(requestBody)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            toastTip("申请成功");
                            GonghuiActivity.this.finish();
                        }else {
                            toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }


}
