package com.fengwo.module_comment.widget.status;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.fengwo.module_comment.R;

import androidx.annotation.NonNull;


/**
 * 状态页工具类
 */
public class LayoutViewHelper extends ReplaceLayoutHelper {

    private View mErrorView;
    private View mEmptyView;
    private View mProgressView;
    private View mSearchEmptyView;


    public LayoutViewHelper(@NonNull View contentLayout) {
        super(contentLayout);
        initProgressView();
    }

    private void initProgressView() {
        mProgressView = LayoutInflater.from(contentLayout.getContext()).inflate(R.layout.layout_loading, null);
    }


    public void showLoading() {
        showStatusLayout(mProgressView);
    }
//
//
//    public void setEmptyView(int resId, String txt) {
//        if (mEmptyView == null) {
//            mEmptyView = LayoutInflater.from(contentLayout.getContext()).inflate(R.layout.layout_empty_page, null);
//        }
//        ImageView iv = mEmptyView.findViewById(R.id.iv_img);
//        TextView tv = mEmptyView.findViewById(R.id.tv_txt);
//        iv.setImageResource(resId);
//        tv.setText(txt);
//    }
//
//

    /**
     * 数据空页面
     */
    public void showEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(contentLayout.getContext()).inflate(R.layout.layout_search_empty, null);
        }
        showStatusLayout(mEmptyView);
    }

    /**
     * 数据空页面
     */
    public void showListEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(contentLayout.getContext()).inflate(R.layout.item_base_empty_view, null);
        }
        showStatusLayout(mEmptyView);
    }


    /**
     * 搜索空界面
     */
    public void showSearchEmptyView(String text) {
        if (mSearchEmptyView == null) {
            mSearchEmptyView = LayoutInflater.from(contentLayout.getContext()).inflate(R.layout.layout_search_empty, null);
            TextView tvKeyWord = mSearchEmptyView.findViewById(R.id.tv_keyword);
            tvKeyWord.setText("搜索关于“"+ text +"”的更多内容");
        }
        showStatusLayout(mSearchEmptyView);
    }
//
//
//    /**
//     * 网络错误
//     */
//    public void showErrorView(View.OnClickListener... listeners) {
//        if (mErrorView == null) {
//            mErrorView = LayoutInflater.from(contentLayout.getContext()).inflate(R.layout.layout_empty_page, null);
//        }
//        showStatusLayout(mErrorView);
//    }


    public void showCustomView(View customView) {
        showStatusLayout(customView);
    }

}

