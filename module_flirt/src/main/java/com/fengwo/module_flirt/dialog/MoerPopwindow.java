package com.fengwo.module_flirt.dialog;

import android.content.Context;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.fengwo.module_flirt.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @anchor Administrator
 * @date 2020/9/2
 */
public class MoerPopwindow extends BasePopupWindow {


    private  LinearLayout ll_view,ll_share,ll_screen;
    public Context context;


    public MoerPopwindow(Context context) {
        super(context);

        setBackground(R.color.transparent);
        setPopupGravity(Gravity.BOTTOM);


        ll_view = findViewById(R.id.ll_view);
        ll_share = findViewById(R.id.ll_shares);
        ll_screen = findViewById(R.id.ll_screen);
        OnDismissListener mDismissListener = new OnDismissListener() {
            @Override
            public void onDismiss() {
                if(onItemClickListener!=null)
                    onItemClickListener.isDismiss();
            }
        };
        setOnDismissListener(mDismissListener);
        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onItemClickListener){
                    onItemClickListener.onItemClick(0);
                }
            }
        });
        ll_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onItemClickListener){
                    onItemClickListener.onItemClick(1);
                }
            }
        });
        ll_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dismiss();
                return false;
            }
        });
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ll_view.getLayoutParams();
//        layoutParams.leftMargin = le-((int)getContext().getResources().getDimension(R.dimen.dp_25));
//        ll_view.setLayoutParams(layoutParams);

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
        return createPopupById(R.layout.live_pop_moer);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void isDismiss();
    }

    private MoerPopwindow.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MoerPopwindow.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
