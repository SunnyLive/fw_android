package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.iservice.AttentionService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.SPUtils1;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.widget.ClearEditText;
import com.fengwo.module_comment.widget.MyFlowLayout;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.SearchDto;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.SearchResultAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = ArouterApi.SEARCH_LIVE_ACTION)
public class SearchActivity extends BaseMvpActivity implements View.OnFocusChangeListener {

    public static final String SP_KEY_SEARCH_HISTORY = "searchhistory";
    public static final String SEARCH_KEY = "searchkey";

    String[] hisSearch = {};
    @BindView(R2.id.btn_search)
    TextView btnSearch;
    @BindView(R2.id.btn_clear_history)
    ImageView btnClearHistory;
    @BindView(R2.id.flowlayout)
    MyFlowLayout flowlayout;
    @BindView(R2.id.iv_arr)
    ImageView arrIv;
    @BindView(R2.id.rv_hotest)
    RecyclerView rvHotest;
    @BindView(R2.id.tv_search_empty)
    TextView tvSearchEmpty;
    @BindView(R2.id.btn_all_history)
    FrameLayout btnAllHistory;
    @BindView(R2.id.ll_search_history)
    LinearLayout llSearchHistory;
    @BindView(R2.id.et_search)
    ClearEditText et_search;
    @BindView(R2.id.rv_search)
    RecyclerView rvSearch;

    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.ll_search)
    LinearLayout llSearch;

    @Autowired
    AttentionService attentionService;
    SearchResultAdapter searchResultAdapter;

    private boolean isShowAllHistory = false;

    private LiveApiService service;

    private int page = 1;
    private boolean isLoadmore;

    @Autowired(name = "activeChoose")
    boolean mActiveChoose;
    @Override
    protected void initView() {
//      ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).reset().init();
        SmartRefreshLayoutUtils.setClassicsColor(this, smartRefreshLayout, android.R.color.transparent, R.color.text_66);
        initSearchHistory();
        service = new RetrofitUtils().createApi(LiveApiService.class);
        et_search.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String input = et_search.getText().toString().trim();
                if (TextUtils.isEmpty(input)) {
                    toastTip("搜索不能为空");
                } else {
                    insertSearch(input);
                    search(input, 1);
                    KeyBoardUtils.closeKeybord(et_search, SearchActivity.this);
                }
            }
            return false;
        });
        et_search.setOnFocusChangeListener(this);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadmore = true;
                int p = 1 + page;
                search(et_search.getText().toString().trim(), p);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                search(et_search.getText().toString().trim(), 1);
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = et_search.getText().toString().trim();
                if (TextUtils.isEmpty(input)) {
                    smartRefreshLayout.setVisibility(View.GONE);
                    llSearch.setVisibility(View.VISIBLE);
                } else {
                    search(et_search.getText().toString().trim(), 1);
                }
            }
        });
    }

    private void initSearchHistory() {
        String searHistory = (String) SPUtils1.get(this, SP_KEY_SEARCH_HISTORY, "");
        if (TextUtils.isEmpty(searHistory)) {
            tvSearchEmpty.setVisibility(View.VISIBLE);
            llSearchHistory.setVisibility(View.GONE);
        } else {
            String[] history = searHistory.split("&");
            tvSearchEmpty.setVisibility(View.GONE);
            llSearchHistory.setVisibility(View.VISIBLE);
            flowlayout.setMaxLines(2);
            for (int i = 0; i < history.length; i++) {
                if (TextUtils.isEmpty(history[i])) {
                    continue;
                }
                flowlayout.addView(initTab(history[i]));
            }
        }

    }

    public View initTab(String history) {
        View v = LayoutInflater.from(this).inflate(R.layout.live_item_searchhistory, null);
        TextView tv = v.findViewById(R.id.tv_history);
        tv.setText(history);
        v.setOnClickListener(v1 -> {
            try {
                String searchContent = tv.getText().toString();

                et_search.setText(searchContent);
                et_search.setSelection(searchContent.length());
                search(tv.getText().toString(), 1);
                insertSearch(tv.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return v;
    }


    @Override
    protected int getContentView() {
        return R.layout.live_activity_search;
    }


    @SuppressLint("InvalidR2Usage")
    @OnClick({R2.id.btn_clear_history, R2.id.flowlayout, R2.id.btn_all_history, R2.id.btn_search})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_clear_history) {
            SPUtils1.remove(this, SP_KEY_SEARCH_HISTORY);
            initSearchHistory();
        } else if (id == R.id.flowlayout) {
        } else if (id == R.id.btn_all_history) {
            if (isShowAllHistory) {
                flowlayout.setMaxLines(2);
            } else {
                flowlayout.setMaxLines(-1);
            }
            rotateArr();
            isShowAllHistory = !isShowAllHistory;
        } else if (id == R.id.btn_search) {
//            if (smartRefreshLayout.getVisibility() == View.VISIBLE) {
//                smartRefreshLayout.setVisibility(View.GONE);
//                llSearch.setVisibility(View.VISIBLE);
//            } else {
                KeyBoardUtils.closeKeybord(et_search, this);
                onBackPressed();
//            }
        }
    }

    private void insertSearch(String input) {
        tvSearchEmpty.setVisibility(View.GONE);
        llSearchHistory.setVisibility(View.VISIBLE);
        flowlayout.removeAllViews();
        String str = (String) SPUtils1.get(this, SP_KEY_SEARCH_HISTORY, "");
        String[] historys = str.split("&");
        List<String> historyList = new ArrayList<>();
        historyList.add(input);
        for (int i = 0; i < historys.length; i++) {
            if (input.equals(historys[i]))
                continue;
            if (!TextUtils.isEmpty(historys[i])) {
                historyList.add(historys[i]);

            }
        }
        for (int i = 0; i < historyList.size(); i++) {
            String mStr = historyList.get(i);
            flowlayout.addView(initTab(mStr));
            if (i == 0) {
                str = mStr;
            } else {
                str += "&" + mStr;
            }
        }
        SPUtils1.put(this, SP_KEY_SEARCH_HISTORY, str);

    }

    public void rotateArr() {
        ObjectAnimator rotation;
        if (isShowAllHistory) {
            rotation = ObjectAnimator.ofFloat(arrIv, "rotation", 180, 360);
        } else
            rotation = ObjectAnimator.ofFloat(arrIv, "rotation", 0, 180);
        rotation.setDuration(300);
        rotation.start();
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    private void search(String str, int page) {
        service.searchChannel(str, page + "," + PAGE_SIZE)
                .compose(handleResult())
                .subscribe(new LoadingObserver<BaseListDto<SearchDto>>() {
                    @Override
                    public void _onNext(BaseListDto<SearchDto> data) {
                        SearchActivity.this.page = page;
                        llSearch.setVisibility(View.GONE);
                        smartRefreshLayout.setVisibility(View.VISIBLE);
                        if (null == searchResultAdapter) {
                            searchResultAdapter = new SearchResultAdapter(data.records);
                            rvSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                            View empty = View.inflate(SearchActivity.this, R.layout.item_base_empty_view, null);
                            rvSearch.setAdapter(searchResultAdapter);
                            searchResultAdapter.setEmptyView(empty);
                            searchResultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    et_search.clearFocus();
                                    SearchDto searchDto = searchResultAdapter.getData().get(position);
                                    String userRole = searchResultAdapter.getData().get(position).getUserRole();
                                    String[] roleList = userRole.split(",");
                                    boolean isExport = Arrays.asList(roleList).contains("ROLE_ANCHOR_WENBO");
                                    boolean isAnchor = Arrays.asList(roleList).contains("ROLE_ANCHOR");
                                    //这个逻辑呢是用来在万圣节活动上使用的 获取主播的信息
                                    if (isAnchor && mActiveChoose) {
                                        Intent i = new Intent();
                                        i.putExtra("anchor", new String[]{String.valueOf(searchDto.getId()),
                                                searchDto.getNickname(),searchDto.getHeadImg()});
                                        setResult(RESULT_OK,i);
                                        finish();
                                        return;
                                    }
                                    if (isExport && isAnchor) {//如果是i撩主播也是秀场主播
                                        if (searchResultAdapter.getData().get(position).getChannelStatus() == 2) {
                                            ArrayList<ZhuboDto> list = new ArrayList<>();
                                            ZhuboDto zhuboDto = new ZhuboDto();
                                            zhuboDto.channelId = searchResultAdapter.getData().get(position).getId();
                                            list.add(zhuboDto);
                                            if (FloatingView.getInstance().isShow()) {
                                                showExitDialog(list);
                                            } else {
                                                IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                                             //   ArouteUtils.toLive(list);
                                            }
                                        } else if (searchResultAdapter.getData().get(position).getLiveStatus() == 1&& CommentUtils.isOpenFlirt) {
                                            ArrayList<ZhuboDto> list = new ArrayList<>();
                                            ZhuboDto zhuboDto = new ZhuboDto();
                                            zhuboDto.channelId = searchResultAdapter.getData().get(position).getId();
                                            list.add(zhuboDto);
                                            ArouteUtils.toFlirtCardDetailsActivity(searchResultAdapter.getData().get(position).getId());
                                        } else {
                                            int id = searchResultAdapter.getItem(position).getId();
                                            ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, id);
                                        }
                                    }else if (isExport && CommentUtils.isOpenFlirt){//是I撩主播
                                        ArrayList<ZhuboDto> list = new ArrayList<>();
                                        ZhuboDto zhuboDto = new ZhuboDto();
                                        zhuboDto.channelId = searchResultAdapter.getData().get(position).getId();
                                        list.add(zhuboDto);
                                        ArouteUtils.toFlirtCardDetailsActivity(searchResultAdapter.getData().get(position).getId());
                                    }else if (isAnchor && searchResultAdapter.getData().get(position).getChannelStatus() == 2){//是秀场主播
                                        ArrayList<ZhuboDto> list = new ArrayList<>();
                                        ZhuboDto zhuboDto = new ZhuboDto();
                                        zhuboDto.channelId = searchDto.getId();
                                        zhuboDto.headImg = searchDto.getHeadImg();
                                        list.add(zhuboDto);
                                        if (FloatingView.getInstance().isShow()) {
                                            showExitDialog(list);
                                        } else {
                                            IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                                         //   ArouteUtils.toLive(list);
                                        }
                                    }else{
                                        int id = searchResultAdapter.getItem(position).getId();
                                        ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, id);
                                    }
                                }
                            });
                            searchResultAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    if (searchResultAdapter.getData().get(position).getAttention() == 1) {
//                                        toastTip("您已关注");
                                        attentionService.delAttention(searchResultAdapter.getData().get(position).getId(), new LoadingObserver<HttpResult>() {
                                            @Override
                                            public void _onNext(HttpResult data) {
//                                                toastTip(data.description);
                                                if (data.isSuccess()) {
                                                    searchResultAdapter.getData().get(position).setAttention(0);
                                                    searchResultAdapter.notifyItemChanged(position);
                                                }
                                            }

                                            @Override
                                            public void _onError(String msg) {
                                                toastTip(msg);
                                            }
                                        });
                                        return;
                                    }
                                    attentionService.addAttention(searchResultAdapter.getData().get(position).getId(), new LoadingObserver<HttpResult>() {
                                        @Override
                                        public void _onNext(HttpResult data) {
//                                            toastTip(data.description);
                                            if (data.isSuccess()) {
                                                searchResultAdapter.getData().get(position).setAttention(1);
                                                searchResultAdapter.notifyItemChanged(position);
                                            }
                                        }

                                        @Override
                                        public void _onError(String msg) {
                                            toastTip(msg);
                                        }
                                    });
                                }
                            });
                        } else {
                            if (isLoadmore) {
                                isLoadmore = false;
                                searchResultAdapter.addData(data.records);
                            } else {
                                searchResultAdapter.setNewData(data.records);
                            }
                        }

                        smartRefreshLayout.closeHeaderOrFooter();
                        if (null != data.records) {
                            if (data.records.size() < 20) {
                                smartRefreshLayout.setNoMoreData(true);
                            } else {
                                smartRefreshLayout.resetNoMoreData();
                            }
                        }


                    }

                    @Override
                    public void _onError(String msg) {
                        toastTip(msg);
                    }
                });
    }
    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog(ArrayList<ZhuboDto> list) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                    //    ArouteUtils.toLive(list);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .setGear(floatingView.getGear())
                .setNickname(floatingView.getNickname())
                .setExpireTime(floatingView.getExpireTime())
                .setHeadImg(floatingView.getHeadImg())
                .setRoomId(floatingView.getRoomId())
                .setTip("退出达人房间，印象值将归零\n是否要退出")
                .show(getSupportFragmentManager(), "");
    }
    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            insertSearch(et_search.getText().toString().trim());
        }
    }
}
