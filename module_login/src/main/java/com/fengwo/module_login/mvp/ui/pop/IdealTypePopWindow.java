package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.donkingliang.labels.LabelsView;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.RxHttpUtil;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.TagsDto;
import com.fengwo.module_login.utils.UserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

import static com.fengwo.module_comment.base.RxHttpUtil.handleResult;

public class IdealTypePopWindow extends BasePopupWindow {

    private Unbinder bind;
    private CompositeDisposable compositeDisposable;
    @BindView(R2.id.labels)
    LabelsView mLabels;
    Context mContext;
    private int select_num = 0;
    private int can_select_count = 3;
    private HashMap<String, TagsDto> mLabelBeans;
    private List<TagsDto> mLabelList;
    private String mIdealType;

    public IdealTypePopWindow(Context context, String idealType) {
        super(context);
        mIdealType = idealType;
        mContext = context;
        setPopupGravity(Gravity.BOTTOM);
        compositeDisposable = new CompositeDisposable();
        initUI();

    }

    private void initUI() {
        UserInfo userInfo = UserManager.getInstance().getUser();
        mLabelBeans = new HashMap<>();
        mLabelList = new ArrayList<>();
        compositeDisposable.add(new RetrofitUtils().createApi(LoginApiService.class)
                .getTags("IDEAL_TYPE")
                .compose(handleResult())
                .subscribeWith((new LoadingObserver<TagsDto>() {

                    @Override
                    public void _onNext(TagsDto data) {
                        if (!TextUtils.isEmpty(data.getTagSet())) {
                            mLabelList.clear();
                            String[] tags = data.getTagSet().split(",");
                            for (int i = 0; i < tags.length; i++) {
                                TagsDto tagsDto = new TagsDto();
                                tagsDto.setTagSet(tags[i]);
                                mLabelList.add(tagsDto);
                            }


                            mLabels.setLabels(mLabelList, new LabelsView.LabelTextProvider<TagsDto>() {
                                @Override
                                public CharSequence getLabelText(TextView label, int position, TagsDto data) {
                                    if (!TextUtils.isEmpty(mIdealType)) {
                                        String[] chooseTags = mIdealType.split(",");
                                        if(mIdealType.contains(data.getTagSet())){
                                            select_num++;
                                            data.setmIsSelect(!data.ismIsSelect());
                                            mLabelBeans.put(data.getTagSet(), data);
                                            label.setBackgroundResource(R.drawable.bg_ideal_type_selector);
                                            label.setTextColor(ContextCompat.getColor(mContext, R.color.black_1A1A1A));
                                        }
                                    }
                                    return data.getTagSet();
                                }
                            });
                        }

                    }

                    @Override
                    public void _onError(String msg) {

                    }
                })));
        mLabels.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                if (mLabelList == null || mLabelList.size() <= 0)
                    return;
                TagsDto labelBean = mLabelList.get(position);
                if (select_num < can_select_count) {
                    labelBean.setmIsSelect(!labelBean.ismIsSelect());
                } else {
                    if (!labelBean.ismIsSelect()) {
                        ToastUtils.showShort(mContext, "最多可以选择3个标签");
                        return;
                    } else {
                        labelBean.setmIsSelect(!labelBean.ismIsSelect());
                    }
                }
                if (labelBean.ismIsSelect()) {
                    select_num++;
                    label.setBackgroundResource(R.drawable.bg_ideal_type_selector);
                    label.setTextColor(ContextCompat.getColor(mContext, R.color.alpha_lxx));
                } else {
                    if (select_num > 0) select_num--;
                    label.setBackgroundResource(R.drawable.bg_ideal_type);
                    label.setTextColor(ContextCompat.getColor(mContext, R.color.text_99));
                }
                if (mLabelBeans.get(labelBean.getTagSet()) != null) {
                    mLabelBeans.remove(labelBean.getTagSet());
                } else {
                    mLabelBeans.put(labelBean.getTagSet(), labelBean);
                }
            }
        });


    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_ideal_type);
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
            if (onClickListener != null) {
                onClickListener.onChoose(mLabelBeans);
            }
            dismiss();
        }
    }

    private OnClickListener onClickListener;

    public void setOnClick(OnClickListener l) {
        this.onClickListener = l;
    }

    public interface OnClickListener {
        void onChoose(HashMap<String, TagsDto> map);
    }
}
