package com.fengwo.module_flirt.UI.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.fragment.CommentListFragment;
import com.fengwo.module_flirt.UI.fragment.PraiseListFragment;
import com.fengwo.module_websocket.EventConstant;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.LayoutBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 互动通知
 */
@Route(path = ArouterApi.INTERACT_HOME)
public class InteractActivity extends BaseMvpActivity {

    private String[] titles = {"\u3000赞\u3000", "评论"};

    @BindView(R2.id.tabLayout)
    ScrollIndicatorView tabLayout;
    @BindView(R2.id.vp)
    ViewPager vp;

    @Autowired
    UserProviderService userProviderService;

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        //清空已读消息
        ChatGreenDaoHelper daoHelper = ChatGreenDaoHelper.getInstance();
        daoHelper.cleanUnreadCount(userProviderService.getUserInfo().id + "", EventConstant.interact_event + "");

        setTitleBackground(getResources().getColor(R.color.white));
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("互动通知")
                .setTitleColor(R.color.text_33)
                .build();

        fragmentList.add(new PraiseListFragment());
        fragmentList.add(new CommentListFragment());
        setScrollIndicator();
    }

    private void setScrollIndicator() {
        vp.setOffscreenPageLimit(fragmentList.size());
        int selectColor = Color.WHITE;
        int unSelectColor = getResources().getColor(R.color.text_99);
        tabLayout.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(15, 13));
        tabLayout.setSplitAuto(false);
        tabLayout.setScrollBar(new LayoutBar(getApplicationContext(), R.layout.layout_contact_home_slidebar, ScrollBar.Gravity.CENTENT_BACKGROUND));
        IndicatorViewPager pager = new IndicatorViewPager(tabLayout, vp);
        pager.setClickIndicatorAnim(false);
        pager.setAdapter(new IndicatorViewPager.IndicatorFragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public View getViewForTab(int position, View convertView, ViewGroup container) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(InteractActivity.this).inflate(R.layout.chat_item_home_tab, container, false);
                }
                TextView textView = (TextView) convertView;
                textView.setText(titles[position]);
                int width = getTextWidth(textView);
                int padding = DensityUtils.dp2px(InteractActivity.this, 25);
                //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
                textView.setWidth((int) (width * 1.2f) + padding);
                return convertView;
            }

            @Override
            public Fragment getFragmentForPage(int position) {
                return fragmentList.get(position);
            }

            private int getTextWidth(TextView textView) {
                if (textView == null) {
                    return 0;
                }
                Rect bounds = new Rect();
                String text = textView.getText().toString();
                Paint paint = textView.getPaint();
                paint.getTextBounds(text, 0, text.length(), bounds);
                return bounds.left + bounds.width();
            }
        });
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_contact_home;
    }
}
