package com.fengwo.module_chat.mvp.ui.activity.social.userpage;

import android.os.Bundle;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.presenter.ChatPresenter;
import com.fengwo.module_chat.mvp.ui.contract.IChatView;
import com.fengwo.module_chat.mvp.ui.adapter.SmallVideoAdapter;
import com.fengwo.module_comment.base.BaseMvpFragment;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 社交个人- 小视频
 *
 * @author chenshanghui
 * @intro
 * @date 2019/9/23
 */
public class SmallVideoFragment extends BaseMvpFragment<IChatView, ChatPresenter> implements IChatView {

    public static SmallVideoFragment newInstance(){
        return new SmallVideoFragment();
    }


    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    public ChatPresenter initPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.chat_fragment_recyclerview;
    }

    private SmallVideoAdapter smallVideoAdapter;


    private ArrayList<String> arrayList = new ArrayList<>();


    @Override
    public void initUI(Bundle savedInstanceState) {
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(),3));
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        smallVideoAdapter = new SmallVideoAdapter(getContext(),arrayList);
        recyclerview.setAdapter(smallVideoAdapter);
    }
}
