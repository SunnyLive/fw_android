package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.fengwo.module_comment.base.RxHttpUtil;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

public class HeightWeightWindow extends BasePopupWindow {

    private Unbinder bind;
    private CompositeDisposable compositeDisposable;
    @BindView(R2.id.wv_weight)
    WheelView mWvWeight;
    @BindView(R2.id.wv_height)
    WheelView mWvHeight;
    Context mContext;
    ArrayList<String> height;
    ArrayList<String> weight;
    public HeightWeightWindow(Context context) {
        super(context);
        mContext = context;
        setPopupGravity(Gravity.BOTTOM);
        compositeDisposable = new CompositeDisposable();
        initUI();

    }

    private void initUI() {
        height = new ArrayList<>();
        for (int i = 10; i <= 220; i++) {
            height.add(String.format("%dcm", i));
        }
        mWvHeight.setCyclic(true);
        mWvHeight.setAdapter(new ArrayWheelAdapter(height));
        mWvHeight.setCurrentItem(height.size()/2);

        weight = new ArrayList<>();
        for (int i = 10; i <= 150; i++) {
            weight.add(String.format("%dkg", i));
        }
        mWvWeight.setCyclic(true);
        mWvWeight.setAdapter(new ArrayWheelAdapter(weight));
        mWvWeight.setCurrentItem(weight.size()/2);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_height_weight);
        bind = ButterKnife.bind(this, v);
        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) bind.unbind();
        if (compositeDisposable == null) return;
        compositeDisposable.isDisposed();
        compositeDisposable.clear();
        RxHttpUtil.clearHttpRequest();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation();
    }


    @OnClick({R2.id.tv_cancel, R2.id.tv_confirm})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        } else if (id == R.id.tv_confirm) {
            if (onClickListener!=null){
                int h = Integer.parseInt(height.get(mWvHeight.getCurrentItem()).replace("cm",""));
                int w =  Integer.parseInt(weight.get(mWvWeight.getCurrentItem()).replace("kg",""));
                onClickListener.onChoose(h,w);
            }
            dismiss();
        }
    }

    private OnClickListener onClickListener;

    public void setOnClick(OnClickListener l){
        this.onClickListener = l;
    }
    public interface OnClickListener {
        void onChoose(int height, int weight);
    }
}
