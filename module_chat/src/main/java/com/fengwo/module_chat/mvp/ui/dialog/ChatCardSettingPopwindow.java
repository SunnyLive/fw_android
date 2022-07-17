package com.fengwo.module_chat.mvp.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;
import com.fengwo.module_comment.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import razerdp.basepopup.BasePopupWindow;

/**
 * 置顶/权限/删除
 */
public class ChatCardSettingPopwindow extends BasePopupWindow {

    @BindView(R2.id.tvTop)
    TextView tvTop;
    @BindView(R2.id.tvPower)
    TextView tvPower;
    @BindView(R2.id.tvDelete)
    TextView tvDelete;
    @BindView(R2.id.cardTopRoot)
    RelativeLayout tvTopRoot;
    @BindView(R2.id.cardPowerRoot)
    RelativeLayout tvPowerRoot;
    private Unbinder bind;

    private ChatCardBean chatCardBean;

    public ChatCardSettingPopwindow(Context context, ChatCardBean chatCardBean) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        this.chatCardBean = chatCardBean;
        initUI();
    }

    private void initUI() {
        //0审核中，1成功，2私密，3封禁，4草稿，5拒审
        if (chatCardBean.cardStatus == 0) {
            tvTop.setTextColor(getContext().getResources().getColor(R.color.text_99));
            tvPower.setTextColor(getContext().getResources().getColor(R.color.text_99));
            tvTopRoot.setBackground(getContext().getResources().getDrawable(R.drawable.bg_card_top_unenable));
            tvPowerRoot.setBackground(getContext().getResources().getDrawable(R.drawable.bg_card_top_unenable));
        } else {
            //私密不能置顶
            if (chatCardBean.getPowerStatus() == 1) {
                tvTop.setTextColor(getContext().getResources().getColor(R.color.text_99));
                tvTopRoot.setBackground(getContext().getResources().getDrawable(R.drawable.bg_card_top_unenable));
            } else {
                tvTop.setTextColor(getContext().getResources().getColor(R.color.text_33));
                tvTopRoot.setBackground(getContext().getResources().getDrawable(R.drawable.bg_card_top));
            }
            tvPower.setTextColor(getContext().getResources().getColor(R.color.text_33));
            tvPowerRoot.setBackground(getContext().getResources().getDrawable(R.drawable.bg_card_power));
        }

        if (TextUtils.isEmpty(chatCardBean.getTopTime())) {
            tvTop.setText("置顶");
        } else {
            tvTop.setText("取消置顶");
        }
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_chat_card_setting);
        bind = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) bind.unbind();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 200);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 200);
    }

    @OnClick({R2.id.topRoot, R2.id.powerRoot, R2.id.deleteRoot, R2.id.cancel})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.topRoot) {
            if (chatCardBean.cardStatus == 0) {
                ToastUtils.showShort(getContext(), "审核中的动态无法置顶");
                return;
            }
            if (chatCardBean.getPowerStatus() == 1) {
                ToastUtils.showShort(getContext(), "私密的动态无法置顶");
                return;
            }
            if (listener != null)
                listener.cardTop();
        } else if (id == R.id.powerRoot) {
            if (chatCardBean.cardStatus == 0) {
                ToastUtils.showShort(getContext(), "审核中的动态无法修改权限");
                return;
            }
            if (listener != null)
                listener.cardPower();
        } else if (id == R.id.deleteRoot) {
            if (listener != null)
                listener.cardDelete();
        } else if (id == R.id.cancel) {
            dismiss();
        }
    }

    public interface OnSelectListener {
        void cardTop();

        void cardPower();

        void cardDelete();
    }

    public OnSelectListener listener;

    public void addOnClickListener(OnSelectListener listener) {
        this.listener = listener;
    }
}
