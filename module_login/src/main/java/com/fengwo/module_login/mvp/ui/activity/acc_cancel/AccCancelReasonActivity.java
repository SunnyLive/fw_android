package com.fengwo.module_login.mvp.ui.activity.acc_cancel;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.utils.UserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/30
 */
public class AccCancelReasonActivity extends BaseMvpActivity {
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;

    private List<ReasonBean> data = new ArrayList<>();
    private List<String> strings =
            Arrays.asList("不好玩","未达成我的预期","个人原因","用户质量原因","产品性能问题","重新注册/换绑手机","其他");

    private String selectText = "";

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().showBack(true)
                .setTitle("注销账号")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .build();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerview.setLayoutManager(layoutManager);

        for (String s:strings){
                data.add(new ReasonBean(s, false));
        }
        ReasonAdapter reasonAdapter = new ReasonAdapter(data);
        recyclerview.setAdapter(reasonAdapter);
        reasonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (ReasonBean bean:data){
                    bean.isSelected = false;
                }
                data.get(position).isSelected= true;
                reasonAdapter.notifyDataSetChanged();
                selectText = data.get(position).name;
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_acc_cancel_reason;
    }


    @OnClick(R2.id.tv_sure)
    public void onViewClicked() {
        if (TextUtils.isEmpty(selectText)){
            toastTip("请选择原因");
            return;
        }
        if(null!=UserManager.getInstance()&&null!=UserManager.getInstance().getUser()&&null!=UserManager.getInstance().getUser().mobile){
            AccCancelConfirmActivity.start(this,selectText, UserManager.getInstance().getUser().mobile,true);
        }

    }

    public static class ReasonBean{
        public String name;
        public boolean isSelected;

        public ReasonBean(String name, boolean isSelected) {
            this.name = name;
            this.isSelected = isSelected;
        }
    }

    public static class ReasonAdapter extends BaseQuickAdapter<ReasonBean, BaseViewHolder>{

        public ReasonAdapter(@Nullable List<ReasonBean> data) {
            super(R.layout.item_acc_cancel_reason,data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ReasonBean item) {
            helper.setText(R.id.tv_name,item.name);
            TextView tvName = helper.getView(R.id.tv_name);
            if (item.isSelected){
                tvName.setTextColor(mContext.getResources().getColor(R.color.text_white));
                tvName.setSelected(true);
            }else {
                tvName.setTextColor(mContext.getResources().getColor(R.color.text_99));
                tvName.setSelected(false);
            }
        }
    }
}
