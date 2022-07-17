package com.fengwo.module_chat.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.ui.adapter.MuchAdapter;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

/**
 * @Author BLCS
 * @Time 2020/6/19 11:33
 */
public class ChatSingleMuchPopwindow extends BasePopupWindow {

    @BindView(R2.id.rv_pop)
    RecyclerView rvPop;
    @BindView(R2.id.tv_cancel)
    TextView tvCancel;
    @Autowired
    UserProviderService service;

    private final MuchAdapter muchAdapter;

    public ChatSingleMuchPopwindow(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        ARouter.getInstance().inject(this);
        setPopupGravity(Gravity.BOTTOM);
        rvPop.setLayoutManager(new LinearLayoutManager(getContext()));
        muchAdapter = new MuchAdapter();
        rvPop.setAdapter(muchAdapter);


        muchAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            String title = (String) baseQuickAdapter.getData().get(i);
            switch (title){
                case "TA的主页":
                    if (onMuchClickListener!=null) onMuchClickListener.clickUserDetail();
                    dismiss();
                    break;
                case "清空聊天记录":
                    if (onMuchClickListener!=null) onMuchClickListener.clickClearRecords();
                    dismiss();
                    break;
                case "举报":
                    if (onMuchClickListener!=null) onMuchClickListener.clickReport();
                    dismiss();
                    break;
                case "拉黑":
                case "已拉黑":
                    if (onMuchClickListener!=null) onMuchClickListener.clickBlack();
                    dismiss();
                    break;
            }
        });
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_chat_single_much);
        return  v;
    }

    @OnClick(R2.id.tv_cancel)
    public void onClick(View view){
        if (view.getId()==R.id.tv_cancel){
            dismiss();
        }
    }
    private String[] titles;
    public void setIsBlack(int isBlack) {
        titles = new String[]{"TA的主页", "清空聊天记录", "举报",isBlack==1 ?"已拉黑":"拉黑"};
        muchAdapter.setNewData(Arrays.asList(titles));
    }

    public interface OnMuchClickListener{
        void clickUserDetail();
        void clickClearRecords();
        void clickReport();
        void clickBlack();
    }
    public OnMuchClickListener onMuchClickListener;
    public void addOnClickListener(OnMuchClickListener listener){
        onMuchClickListener = listener;
    }
}
