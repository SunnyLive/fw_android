package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

/**
 * @Author BLCS
 * @Time 2020/6/19 11:33
 */
public class FindMuchPopwindow extends BasePopupWindow {

    private int pos;
    @BindView(R2.id.tv_cancel)
    TextView tvCancel;
    @BindView(R2.id.tv_find_report)
    TextView tvReport;
    @BindView(R2.id.tv_find_attention)
    TextView tvAttention;
    @BindView(R2.id.view_lin)
    View view_lin;

    @Autowired
    UserProviderService service;
    private int uid;

    public FindMuchPopwindow(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        ARouter.getInstance().inject(this);
        setPopupGravity(Gravity.BOTTOM);

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
        View v = createPopupById(R.layout.pop_find_much);
        return v;
    }

    @OnClick({R2.id.tv_cancel, R2.id.tv_find_attention, R2.id.tv_find_report})
    public void onClick(View view) {
        if (view.getId() == R.id.tv_cancel) {
            dismiss();
        } else if (view.getId() == R.id.tv_find_attention) {
            if (onMuchClickListener!=null) onMuchClickListener.attention(uid,pos);
            dismiss();
        } else if (view.getId() == R.id.tv_find_report) {
            ArouteUtils.toPathWithId(ArouterApi.REPORT_ACTIVITY, String.valueOf(uid));
            dismiss();
        }
    }

    public void setAttention(int attention,int uid, int pos) {
        this.uid = uid;
        this.pos = pos;
        tvAttention.setVisibility(attention == 1 ? View.GONE : View.VISIBLE);

        view_lin.setVisibility(attention == 1 ? View.GONE : View.VISIBLE);
    }

    public interface OnMuchClickListener {
        void attention(int uid,int pos);
    }

    public OnMuchClickListener onMuchClickListener;

    public void addOnClickListener(OnMuchClickListener listener) {
        onMuchClickListener = listener;
    }
}
