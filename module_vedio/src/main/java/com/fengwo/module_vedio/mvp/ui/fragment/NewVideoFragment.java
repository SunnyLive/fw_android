//package com.fengwo.module_vedio.mvp.ui.fragment;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.android.arouter.facade.annotation.Autowired;
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.fengwo.module_comment.ArouterApi;
//import com.fengwo.module_comment.base.BaseMvpFragment;
//import com.fengwo.module_comment.base.BasePresenter;
//import com.fengwo.module_comment.base.BrowserActivity;
//import com.fengwo.module_comment.base.HttpResult;
//import com.fengwo.module_comment.iservice.UserProviderService;
//import com.fengwo.module_comment.utils.L;
//import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
//import com.fengwo.module_vedio.R;
//import com.fengwo.module_vedio.R2;
//import com.fengwo.module_vedio.mvp.dto.NewVideoDto;
//import com.fengwo.module_vedio.mvp.ui.adapter.NewVideoAdapter;
//import com.fengwo.module_vedio.mvp.ui.pop.SharePop;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
///**
// * @author GuiLianL
// * @intro
// * @date 2019/11/2
// */
//@Route(path = ArouterApi.VEDIO_FRAGMENT_HOME)
//public class NewVideoFragment extends BaseMvpFragment {
//
//    @Autowired
//    UserProviderService userProviderService;
//
//    @BindView(R2.id.smartrefreshlayout)
//    SmartRefreshLayout smartRefreshLayout;
//    @BindView(R2.id.recycleview)
//    RecyclerView recyclerView;
//
//    protected int page = 1;
//    private boolean isRefresh = true;
//    private Gson gson = new Gson();
//
//    private List<NewVideoDto> mListData = new ArrayList<>();
//    private NewVideoAdapter mAdapter;
//
//
//    @Override
//    protected BasePresenter initPresenter() {
//        return null;
//    }
//
//    @Override
//    public void initView(View v) {
//        super.initView(v);
//        SmartRefreshLayoutUtils.setClassicsColor(getActivity(), smartRefreshLayout, R.color.white, R.color.text_66);
//    }
//
//    @Override
//    protected int setContentView() {
//        return R.layout.activity_baselist;
//    }
//
//    @Override
//    public void initUI(Bundle savedInstanceState) {
////        setTitleBackground(Color.WHITE);
//        new ToolBarBuilder().setTitle("玩转蜂窝").setTitleColor(R.color.text_33).build();
//        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
//            isRefresh = true;
//            page = 1;
//            getData();
//        });
//        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
//            isRefresh = false;
//            page++;
//            getData();
//        });
//        initRv();
//        getData();
//    }
//
//    private void getData() {
//        String url = "http://fwtv.gttead.cn/api/NationBroker/brokerStrategy?page=" + page;
//        OkHttpClient okHttpClient = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(url)
//                .get()
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                L.e("lgl====", "response:" + result);
//                HttpResult<List<NewVideoDto>> httpResult = gson.fromJson(result, new TypeToken<HttpResult<List<NewVideoDto>>>() {
//                }.getType());
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (null == httpResult) {
//                            return;
//                        }
//                        smartRefreshLayout.closeHeaderOrFooter();
//                        if (null != httpResult.data) {
//                            smartRefreshLayout.setEnableLoadMore(true);
//                        } else {
//                            smartRefreshLayout.setEnableLoadMore(false);
//                        }
//                        if (page == 1) {
//                            mAdapter.setNewData(httpResult.data);
//                        } else {
//                            mAdapter.addData(httpResult.data);
//                        }
//                    }
//                });
//
//            }
//        });
//    }
//
//    @SuppressLint("ResourceAsColor")
//    private void initRv() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mAdapter = new NewVideoAdapter(mListData);
//        recyclerView.setAdapter(mAdapter);
//        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//        ColorDrawable colorDrawable = new ColorDrawable(Color.GRAY);
//        decoration.setDrawable(colorDrawable);
//        recyclerView.addItemDecoration(decoration);
//        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                NewVideoDto item = mAdapter.getData().get(position);
//                String url = "http://app.zhibo.gttead.cn/strategy.html?token=" + userProviderService.getToken() + "&id=" + mAdapter.getData().get(position).getId();
//                new SharePop(getActivity(), item.getPost_title(), url).showPopupWindow();
//            }
//        });
//        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(getActivity(), BrowserActivity.class);
//                intent.putExtra("title", mAdapter.getData().get(position).getPost_title());
//                intent.putExtra("url", "http://app.zhibo.gttead.cn/strategy.html?token=" + userProviderService.getToken() + "&id=" + mAdapter.getData().get(position).getId());
//                getActivity().startActivity(intent);
//            }
//        });
//        View v = LayoutInflater.from(getActivity()).inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
//        mAdapter.setEmptyView(v);
//    }
//}
