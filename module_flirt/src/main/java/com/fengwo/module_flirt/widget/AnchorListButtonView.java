package com.fengwo.module_flirt.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import de.hdodenhof.circleimageview.CircleImageView;

import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.dialog.MyOrderPopwindow;

import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/5/22 17:00
 */
public class AnchorListButtonView extends FrameLayout implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private CircleImageView cHeader;
    private String roomId;
    private int screenHeight;
    private int viewHeight;
    private boolean isMove;


    public AnchorListButtonView(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
        initUI();
    }

    public AnchorListButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MyOrderPopwindow myOrderPopwindow;

    @SuppressLint("ClickableViewAccessibility")
    public void initUI() {
        screenHeight = ScreenUtils.getScreenHeight(getContext());
        viewHeight = (int) getResources().getDimension(R.dimen.dp_40);
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.include_button, this, true);
        View llButon = findViewById(R.id.ll_button_open);
        llButon.setOnClickListener(this);
        cHeader = findViewById(R.id.civ_header);
        llButon.setOnTouchListener((v, event) -> {
            int y = (int) event.getRawY(); //触摸点相对于屏幕的纵坐标
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP: //当手势类型为移动时
                    if (!isMove) {
                        myOrderPopwindow = new MyOrderPopwindow(getContext(), fragmentManager, roomId);
                        myOrderPopwindow.addFinishListener(() -> {
                            setVisibility(GONE);
                        });
                        myOrderPopwindow.showPopupWindow();
                        if (clickViewListener != null) clickViewListener.clickView();
                    }
                    isMove = false;
                    break;
                case MotionEvent.ACTION_MOVE: //当手势类型为移动时
                    int deltaY = y - mLastY;//两次移动的y的距离差
                    if (Math.abs(deltaY) > 10) {
                        isMove = true;
                    }
                    //重新设置此view相对父容器的偏移量
                    int translationY = (int) getTranslationY() + deltaY;
                    float top = y - event.getY() + deltaY; //上边距
                    if (top > viewHeight * 2 && top < screenHeight - viewHeight * 2) {
                        setTranslationY(translationY);
                    }
                    break;
                default:
                    break;
            }
            //记录上一次移动的坐标
            mLastY = y;
            return false;
        });
    }

    public void setHeader(String imgUrl) {
        ImageLoader.loadCircleImg(cHeader, imgUrl);
    }

    public void setOrderList(List<MyOrderDto> data, String roomId) {
        this.roomId = roomId;
        if (data == null) {
            setVisibility(GONE);
            return;
        }
        if (myOrderPopwindow != null) {
            myOrderPopwindow.setNewData(data);
        }
    }

    public void addClickViewListener(ClickViewListener listener) {
        clickViewListener = listener;
    }

    public ClickViewListener clickViewListener;

    @Override
    public void onClick(View v) {
        //该方法的实现在 onTouch 中  不能删除 否则 onTouch 无效
    }

    public interface ClickViewListener {
        void clickView();
    }

    int mLastY = 0;

}
