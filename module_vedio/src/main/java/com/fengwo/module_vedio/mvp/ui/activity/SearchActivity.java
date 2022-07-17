package com.fengwo.module_vedio.mvp.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.GsonUtils;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.widget.MyFlowLayout;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.mvp.dto.VideoSearchDto;
import com.fengwo.module_vedio.mvp.presenter.SearchPresenter;
import com.fengwo.module_vedio.mvp.ui.adapter.VideoSearchAdapter;
import com.fengwo.module_vedio.mvp.ui.adapter.VideoSearchResultAdapter;
import com.fengwo.module_vedio.mvp.ui.iview.ISearchResultView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseMvpActivity<ISearchResultView, SearchPresenter> implements ISearchResultView {

    public static final String SP_KEY_SEARCH_HISTORY = "Searchhistory";
    public static final String SEARCH_KEY = "searchkey";
    @BindView(R2.id.ll_search_history)
    LinearLayout llSearchHistory;
    @BindView(R2.id.fl_search_history)
    MyFlowLayout flSearchhistory;
    @BindView(R2.id.rv_search_guest)
    RecyclerView rvSearchGuest;
    @BindView(R2.id.rv_video_search_result)
    RecyclerView rvVideoSearchResult;
    @BindView(R2.id.et_search)
    EditText etSearch;
    @BindView(R2.id.iv_search_clean)
    ImageView ivSearchClean;
    @BindView(R2.id.ll_video_search_init)
    LinearLayout llVideoSearchInit;

    private VideoSearchAdapter videoSearchAdapter;//热搜
    private VideoSearchResultAdapter videoSearchResultAdapter;//显示搜索结果
    private List<String> saveSearchHistory = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.vedio_activity_search;
    }
    @Override
    public SearchPresenter initPresenter() {
        return new SearchPresenter();
    }
    @Override
    protected void initView() {
        initSearchHistory();
        //init RecyclerView
        initRv();
        //search listenter
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivSearchClean.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                llVideoSearchInit.setVisibility(TextUtils.isEmpty(s)?View.VISIBLE:View.GONE);
                rvVideoSearchResult.setVisibility(TextUtils.isEmpty(s)?View.GONE:View.VISIBLE);
                if (TextUtils.isEmpty(s)) videoSearchResultAdapter.setCustomData(null,null,null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //click Search listenter
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//软盘搜索键
                    clickKeyboardSearch(etSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        p.getSearchGuessData();
    }

    private void initRv() {
        //init Search hot RecyclerView
        rvSearchGuest.setLayoutManager(new GridLayoutManager(this, 2));
        videoSearchAdapter = new VideoSearchAdapter();
        rvSearchGuest.setAdapter(videoSearchAdapter);
        videoSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String content = (String) adapter.getData().get(position);
                etSearch.setText(content);
                clickKeyboardSearch(content);
            }
        });
        //init Search result RecyclerView
        rvVideoSearchResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        videoSearchResultAdapter = new VideoSearchResultAdapter();
        rvVideoSearchResult.setAdapter(videoSearchResultAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        String searchContent = etSearch.getText().toString().trim();
        //解决 影视点赞返回再进去没刷新问题
        if (!TextUtils.isEmpty(searchContent)){
            clickKeyboardSearch(searchContent);
        }
    }

    public void clickKeyboardSearch(String content) {
        if (TextUtils.isEmpty(content)){
            toastTip(getResources().getString(R.string.input_search_content));
            return;
        }
        //save search Content by search history
        String oldData = (String) SPUtils1.get(SearchActivity.this, SP_KEY_SEARCH_HISTORY, "");
        if (TextUtils.isEmpty(oldData)) {
            saveSearchHistory.add(content);
            SPUtils1.put(SearchActivity.this, SP_KEY_SEARCH_HISTORY, GsonUtils.objList2Json(saveSearchHistory));
        } else {
            List<String> list = GsonUtils.json2ObjList(oldData, String.class);
            if (oldData.contains(content)) {
                list.remove(content);
                list.add(0,content);
            }else{
                list.add(0,content);
            }
            SPUtils1.put(SearchActivity.this, SP_KEY_SEARCH_HISTORY,GsonUtils.objList2Json(list));
        }
        //refresh search history
        initSearchHistory();
        // search
        p.search(content,1);
    }

    /**
     * show/hide layout
     */
    private void initSearchHistory() {
        flSearchhistory.removeAllViews();
        String searHistory = (String) SPUtils1.get(this, SP_KEY_SEARCH_HISTORY, "");
        if (TextUtils.isEmpty(searHistory)) {
            llSearchHistory.setVisibility(View.GONE);
            flSearchhistory.setVisibility(View.GONE);
        } else {
            llSearchHistory.setVisibility(View.VISIBLE);
            flSearchhistory.setVisibility(View.VISIBLE);
            List<String> list =  GsonUtils.json2ObjList(searHistory,String.class);
            flSearchhistory.setMaxLines(2);
            for (String content:list) {
                flSearchhistory.addView(initTab(content));
            }
        }
    }

    /**
     * history tab
     */
    public View initTab(String history) {
        View v = LayoutInflater.from(this).inflate(R.layout.vedio_item_searchhistory, null);
        AutoUtils.auto(v);
        TextView tv = v.findViewById(R.id.tv_history);
        tv.setText(history);
        tv.setOnClickListener(v1 -> {
            clickKeyboardSearch(history);
            etSearch.setText(history);
        });
        return v;
    }

    @Override
    public void showGuessData(List<String> data) {
        videoSearchAdapter.setNewData(data);
    }

    @Override
    public void showSearchResult(VideoSearchDto data,String content) {
        View empty = LayoutInflater.from(this).inflate(R.layout.item_base_empty_view, null);
        videoSearchResultAdapter.setCustomData(data,content,empty);
        rvVideoSearchResult.setVisibility(View.VISIBLE);
        llVideoSearchInit.setVisibility(View.GONE);
    }

    @OnClick({R2.id.btn_search, R2.id.iv_search_clean, R2.id.btn_clear_history})
    public void onViewClicked(View view) {
        if (isFastClick()) return;
        int id = view.getId();
        if (id == R.id.btn_search) {
            finish();
        } else if (id == R.id.iv_search_clean) {
            etSearch.setText("");
        } else if (id == R.id.btn_clear_history) {
            SPUtils1.put(SearchActivity.this, SP_KEY_SEARCH_HISTORY,"");
            flSearchhistory.removeAllViews();
            initSearchHistory();
        }
    }
}
