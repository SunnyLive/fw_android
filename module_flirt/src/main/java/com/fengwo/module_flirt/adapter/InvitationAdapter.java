package com.fengwo.module_flirt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.utils.TimeUtils;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.UI.DrawTextView;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.IliaoBean;
import com.fengwo.module_flirt.bean.PeriodPrice;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

public class InvitationAdapter extends BaseQuickAdapter<IliaoBean.RecordsBean, BaseViewHolder> {
Context context;
    public InvitationAdapter(Context context) {

        super(R.layout.adapter_invitation);
        this.context =context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, IliaoBean.RecordsBean item) {
        ImageView im_pic = holder.getView(R.id.cim_pic);
        ImageLoader.loadImg(im_pic, item.getHeadImg());
        holder.setText(R.id.tv_name,item.getNickname());
        holder.setText(R.id.tv_context,item.getSignature());
        TextView tv_yq = holder.getView(R.id.tv_yq);
        boolean sexIsUnknown = item.getSex() == 0 || item.getSex() == 3;
        holder.setGone(R.id.im_zx,item.getOnlineStatus()==1);
        if (sexIsUnknown && item.getAge() == 0) {
            holder.setGone(R.id.tv_card_age_and_sex, false);
        } else {
            holder.setGone(R.id.tv_card_age_and_sex, true);
            if (item.getAge() <= 0) {
                holder.setText(R.id.tv_card_age_and_sex, null);
            } else {
                holder.setText(R.id.tv_card_age_and_sex, item.getAge() + "岁");
            }

            DrawTextView drawTextView = holder.getView(R.id.tv_card_age_and_sex);
            if (item.getSex() == 0 || item.getSex() == 3) {
                drawTextView.setStartDrawAble(R.drawable.ic_gender, R.dimen.dp_0, R.dimen.dp_0);
            } else {
                drawTextView.setStartDrawAble(R.drawable.ic_gender, R.dimen.dp_10, R.dimen.dp_10);
            }
            holder.getView(R.id.tv_card_age_and_sex).setSelected(item.getSex() != 1);
        }
        if(item.isIsInvited()){
            tv_yq.setTextColor(Color.parseColor("#999999"));
            tv_yq.setBackgroundResource(R.drawable.bg_wyq);
            tv_yq.setText("已邀请");
        }else {
            tv_yq.setBackgroundResource(R.drawable.bg_yq);
            tv_yq.setTextColor(Color.parseColor("#ffffff"));
            tv_yq.setText("邀请");
            tv_yq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestBody build = new WenboParamsBuilder()
                            .put("userId", String.valueOf(item.getId()))
                            .build();
                    new RetrofitUtils().createWenboApi(FlirtApiService.class)
                            .getYq(build)
                            .compose(RxUtils.applySchedulers2())
                            .subscribe(new LoadingObserver<HttpResult>() {
                                @Override
                                public void _onNext(HttpResult data) {
                                    if (data.isSuccess()) {
                                        tv_yq.setTextColor(Color.parseColor("#999999"));
                                        tv_yq.setBackgroundResource(R.drawable.bg_wyq);
                                        tv_yq.setText("已邀请");

                                    } else {
                                        ToastUtils.showShort(context, data.description);
                                    }
                                }

                                @Override
                                public void _onError(String msg) {
                                    ToastUtils.showShort(context, msg);
                                }
                            });
                }
            });
        }

    }


}
