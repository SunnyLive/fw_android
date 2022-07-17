package com.fengwo.module_chat.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_chat.mvp.model.bean.SearchCardBean;
import com.fengwo.module_chat.mvp.presenter.SocialSearchPresenter;
import com.fengwo.module_chat.mvp.ui.adapter.ChatSearchResultAdapter;
import com.fengwo.module_chat.mvp.ui.adapter.SearchHotAdapter;
import com.fengwo.module_chat.mvp.ui.contract.SocialSearchView;
import com.fengwo.module_chat.widgets.TagTextView;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.widget.MyFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社交圈-搜索
 *
 * @author chenshanghui
 * @intro
 * @date 2019/9/6
 */
public class SocialSearchActivity extends BaseMvpActivity<SocialSearchView, SocialSearchPresenter>
        implements SocialSearchView {

    public static final String SP_SEARCH_HISTORY_KEY = "sp_search_history_key";
    public static final int MAX_HISTORY_NUM = 20;
    private SearchHotAdapter searchHotAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, SocialSearchActivity.class);
        context.startActivity(intent);
    }

    @BindView(R2.id.et_search)
    EditText etSearch;
    @BindView(R2.id.fl_history)
    MyFlowLayout flHistory;
    @BindView(R2.id.fl_hot_search)
    RecyclerView flHotSearch;
    @BindView(R2.id.rv_result)
    RecyclerView rvResult;
    @BindView(R2.id.groupSearch)
    Group searchGroup;
    @BindView(R2.id.tv_search_history)
    TextView tvSearchHistory;
    @BindView(R2.id.iv_clear_history)
    ImageView ivSearchClean;
    @BindView(R2.id.iv_social_search_bg)
    ImageView ivSocialSearchBg;

    private String pageParams = ",20";
    private int page = 1;
    private ChatSearchResultAdapter mAdapter;
    private ArrayList<CircleListBean> mData = new ArrayList<>();

    @Override
    protected void initView() {
        if (DarkUtil.isDarkTheme(this)) {
            ivSocialSearchBg.setVisibility(View.GONE);
        }
        initRecyclerView();
        addSearchHistory();
        etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchGroup.setVisibility(View.GONE);
                rvResult.setVisibility(View.VISIBLE);
                goSearch();
                KeyBoardUtils.closeKeybord(etSearch, this);
                return true;
            }
            return false;
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    searchGroup.setVisibility(View.VISIBLE);
                    rvResult.setVisibility(View.GONE);
                }
            }
        });
        getSearchHot();
    }

    private void getSearchHot() {
        p.getSearchHot();
    }

    private void initRecyclerView() {
        mAdapter = new ChatSearchResultAdapter(mData);
        View v = LayoutInflater.from(this).inflate(R.layout.item_base_empty_view, null, false);
        mAdapter.setEmptyView(v);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<CircleListBean> data = mAdapter.getData();
            ChatCardActivityNew.start(this, new ArrayList<CircleListBean>(data), mAdapter.getData().get(position).id, "0", 0, 0, 0, 0);
        });
        rvResult.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvResult.setAdapter(mAdapter);

        searchHotAdapter = new SearchHotAdapter(null);
        searchHotAdapter.setOnItemClickListener((adapter, view, position) -> {
            String searchKey = searchHotAdapter.getData().get(position);
            etSearch.setText(searchKey);
            etSearch.setSelection(searchKey.length());
            insertSearch(searchKey);
            KeyBoardUtils.closeKeybord(etSearch, SocialSearchActivity.this);
            p.getSearchCard(page + pageParams, searchKey);
        });
        flHotSearch.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        flHotSearch.setAdapter(searchHotAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_social_search;
    }

    @OnClick({R2.id.tv_cancel, R2.id.iv_clear_history, R2.id.iv_search})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            finish();
        } else if (id == R.id.iv_clear_history) {
            SPUtils1.remove(this, SP_SEARCH_HISTORY_KEY);
            addSearchHistory();
        } else if (id == R.id.iv_search) {
            goSearch();
        }
    }

    private void goSearch() {
        String content = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            toastTip("请输入关键词");
            return;
        }
        insertSearch(content);
        p.getSearchCard(page + pageParams, content);
    }

    private void addSearchHistory() {
        flHistory.removeAllViews();
        String searHistory = (String) SPUtils1.get(this, SP_SEARCH_HISTORY_KEY, "");
        flHistory.setMaxLines(4);
        tvSearchHistory.setVisibility(TextUtils.isEmpty(searHistory) ? View.GONE : View.VISIBLE);
        ivSearchClean.setVisibility(TextUtils.isEmpty(searHistory) ? View.GONE : View.VISIBLE);
        flHistory.setVisibility(TextUtils.isEmpty(searHistory) ? View.GONE : View.VISIBLE);
        String[] history = searHistory.split("&");
        int dp6 = (int) getResources().getDimension(R.dimen.dp_6);
        int dp10 = (int) getResources().getDimension(R.dimen.dp_10);
        StringBuilder history20 = new StringBuilder();
        int times = 0;
        for (String s : history) {
            if (TextUtils.isEmpty(s)) {
                continue;
            }
            if (times > MAX_HISTORY_NUM) {
                break;
            }
            TagTextView tvLabel = new TagTextView(this);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMargins(dp10, dp10, 0, 0);
            tvLabel.setTextColor(ContextCompat.getColor(this, R.color.search_history));
            tvLabel.setFullColor(R.color.gray_eaeaea);
            tvLabel.setBackGroudStyle(Paint.Style.STROKE);
            tvLabel.setPadding(dp10, dp6, dp10, dp6);
            tvLabel.setText(s);
            tvLabel.setOnClickListener(v -> {
                etSearch.setText(s);
                etSearch.setSelection(s.length());
                KeyBoardUtils.closeKeybord(etSearch, SocialSearchActivity.this);
                p.getSearchCard(page + pageParams, s);
            });
            flHistory.addView(tvLabel, llp);
            history20.append(s).append("&");
            times++;
        }
        if (times > MAX_HISTORY_NUM) {
            SPUtils1.put(this, SP_SEARCH_HISTORY_KEY, history20.toString());
        }
    }

    private void insertSearch(String input) {
//        tvSearchEmpty.setVisibility(View.GONE);
        flHistory.setVisibility(View.VISIBLE);
        String str = (String) SPUtils1.get(this, SP_SEARCH_HISTORY_KEY, "");
        if (TextUtils.isEmpty(str)) {
            str = input;
        } else if (str.contains(input)) {
            return;
        } else {
            str = input + "&" + str;
        }
        SPUtils1.put(this, SP_SEARCH_HISTORY_KEY, str);
        addSearchHistory();
    }


    @Override
    public SocialSearchPresenter initPresenter() {
        return new SocialSearchPresenter();
    }

    @Override
    public void returnSearchHot(List<String> list) {
        searchHotAdapter.setNewData(list);
    }

    @Override
    public void returnSearchCard(SearchCardBean list) {
        searchGroup.setVisibility(View.GONE);
        rvResult.setVisibility(View.VISIBLE);
        if (page == 1) {
            mAdapter.setNewData(list.getRecords());
        } else {
            mAdapter.addData(list.getRecords());
        }
    }
}