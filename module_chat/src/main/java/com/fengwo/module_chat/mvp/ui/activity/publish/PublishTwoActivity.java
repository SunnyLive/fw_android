package com.fengwo.module_chat.mvp.ui.activity.publish;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.fengwo.module_comment.widget.MyFlowLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import butterknife.BindView;


/**
 * 发布文章的第二步
 */
public class PublishTwoActivity extends BaseMvpActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, PublishTwoActivity.class);
        context.startActivity(intent);
    }


    @BindView(R2.id.titleBar)
    AppTitleBar titleBar;
    @BindView(R2.id.tv_date)
    TextView tvDate;
    @BindView(R2.id.imageView)
    ImageView imageView;
    @BindView(R2.id.btn_date)
    ConstraintLayout btnDate;
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.imageView2)
    ImageView imageView2;
    @BindView(R2.id.btn_location)
    ConstraintLayout btnLocation;
    @BindView(R2.id.tv_tag)
    TextView tvTag;
    @BindView(R2.id.imageView3)
    ImageView imageView3;
    @BindView(R2.id.btn_tag)
    ConstraintLayout btnTag;
    @BindView(R2.id.fl_tag)
    MyFlowLayout flTag;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        ArrayList<String> list = new ArrayList<>();
        list.add("蛋炒饭");
        list.add("煮冬瓜");
        list.add("爆炒西蓝花");
        addLabel(flTag, list);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_publish_two;
    }

    private void addLabel(MyFlowLayout linearLayout, List<String> list) {
        int dp7 = (int) getResources().getDimension(R.dimen.dp_7);
        int dp15 = (int) getResources().getDimension(R.dimen.dp_15);
        int dp8 = (int) getResources().getDimension(R.dimen.dp_8);
        int dp9 = (int) getResources().getDimension(R.dimen.dp_9);
        for (String s : list) {
            TextView tvLabel = new TextView(this);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMargins(dp15, dp9, 0, 0);
            //代码设置背景selector
            StateListDrawable bg = new StateListDrawable();
            bg.addState(new int[]{android.R.attr.state_selected}, getResources().getDrawable(R.drawable.rect_purple_round5));
            bg.addState(new int[]{}, getResources().getDrawable(R.drawable.rect_round5_stroke1_gray));
            tvLabel.setBackground(bg);
            //代码设置字体颜色selector
            int[][] states = new int[2][];
            states[0] = new int[]{android.R.attr.state_selected};
            states[1] = new int[]{};
            int[] colors = new int[]{ContextCompat.getColor(this, R.color.white),ContextCompat.getColor(this, R.color.gray_666666)};    //0xffaabbcc跟states[0]对应，0xff000000跟states[1]对应，以此类推
            ColorStateList csl = new ColorStateList(states, colors);
            tvLabel.setTextColor(csl);
            tvLabel.setPadding(dp8, dp7, dp8, dp7);
            tvLabel.setText(s);
            tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.getResources().getDimension(R.dimen.sp_16));
            linearLayout.addView(tvLabel, llp);
            tvLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                }
            });
        }
    }


}
