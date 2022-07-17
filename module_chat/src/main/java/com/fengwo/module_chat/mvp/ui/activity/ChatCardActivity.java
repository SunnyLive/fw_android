package com.fengwo.module_chat.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.CardMemberBean;
import com.fengwo.module_chat.mvp.model.bean.CardMemberModel;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_chat.mvp.presenter.ChatCardPresenter;
import com.fengwo.module_chat.mvp.ui.contract.IChatCardView;
import com.fengwo.module_chat.mvp.ui.fragment.ChatCardFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.widget.AppTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//@Route(path = ArouterApi.CARD_HOME)
@Deprecated
public class ChatCardActivity extends BaseMvpActivity<IChatCardView, ChatCardPresenter>
        implements IChatCardView {

    private FragmentStatePagerAdapter cardAdapter;
    private BaseQuickAdapter<CardMemberBean, BaseViewHolder> memberAdapter;
    private ArrayList<CircleListBean> cardlists;
    private boolean isFirst = true;

    public static void start(Context context, ArrayList<CircleListBean> data, String cardId, String circleId, int showSame) {
        Intent intent = new Intent(context, ChatCardActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("cardId", cardId);
        intent.putExtra("circleId", circleId);
        intent.putExtra("showSame", showSame);
        context.startActivity(intent);
    }

    @Autowired
    UserProviderService service;
    @BindView(R2.id.title)
    AppTitleBar titleBar;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    @BindView(R2.id.rvMember)
    RecyclerView rvMember;
    @BindView(R2.id.tv_circle_num)
    TextView tvCircleCount;


    private final String PAGE_SIZE = ",20";
    private boolean hasMore = true;

    private String circleId;
    private String cardId;
    private int pos;
    private int page = 1;

    private ArrayList<ChatCardBean> cards = new ArrayList<>();

    @Override
    public ChatCardPresenter initPresenter() {
        return new ChatCardPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        int showSame = getParameter();
        titleBar.setMoreVisible(showSame == 0 ? true : false);
        titleBar.setMoreClickListener(v -> {
            //TODO  进入的id 是动态的  需要修复
            ChatCardSameActivity.start(this, cardlists.get(pos).id, circleId);
        });
        initViewPager();
        initMember();
        p.getCardList(cardId, circleId, 0, 0,0,0);
    }

    /**
     * Gets the parameters passed by other pages
     */
    private int getParameter() {
        Intent intent = getIntent();
        //卡片的位置
        cardId = intent.getStringExtra("cardId");
        circleId = intent.getStringExtra("circleId");
        //用于判断是否隐藏 Same
        int showSame = intent.getIntExtra("showSame", 0);
        cardlists = (ArrayList<CircleListBean>) intent.getSerializableExtra("data");
        return showSame;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_chat_card;
    }

    @OnClick(R2.id.tv_enter_circle)
    public void onClick(View view) {
        if (cards == null || cards.size() <= 0) return;
        int index = viewPager.getCurrentItem();
        ChatCardBean card = cards.get(index);
        // 进入群聊
        ArouteUtils.toChatGroupActivity(String.valueOf(service.getUserInfo().id), card.groupId, card.circleName,"");
    }

    // 初始化卡片Fragment
    private void initViewPager() {
        cardAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {

                ChatCardFragment fragment = new ChatCardFragment();
                fragment.refreshUI(cards.get(position));
                if (position > 0) {
                    int lastPosition = position - 1;
                    if (!TextUtils.equals(cards.get(lastPosition).circleId, cards.get(position).circleId) &&
                            cards.get(position).member == null) {
                        p.getMemberList(position, cards.get(position).circleId);
                    } else {
                        cards.get(position).member = cards.get(lastPosition).member;
                    }
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return cards.size();
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return PagerAdapter.POSITION_NONE;
            }
        };
        viewPager.setAdapter(cardAdapter);
        viewPager.setPageMargin(DensityUtils.dp2px(this, 5));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ChatCardBean cardBean = cards.get(position);
                titleBar.setTitle(cardBean.circleName);
                titleBar.setTitleColor(R.color.text_white);
                refreshMemberUI();
                // 加载更多数据
                if (hasMore && position == cards.size() - 2) {
                    p.getCardList(cards.get(cards.size() - 1).id, circleId, 1, 0,0,0);
                }
                if (hasMore && position == 1) {
                    p.getCardList(cards.get(0).id, circleId, -1, 0,0,0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initMember() {
        rvMember.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        memberAdapter = new BaseQuickAdapter<CardMemberBean, BaseViewHolder>(R.layout.chat_item_card_member) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, CardMemberBean item) {
                if (helper.getAdapterPosition() == 0) {
                    RecyclerView.MarginLayoutParams lp =
                            (RecyclerView.MarginLayoutParams) helper.itemView.getLayoutParams();
                    lp.leftMargin = 0;
                    helper.itemView.setLayoutParams(lp);
                }
                ImageView imageView = helper.getView(R.id.ivMember);
                if (helper.getAdapterPosition() >= 3) {
                    imageView.setImageResource(R.drawable.ic_card_home_more);
                } else ImageLoader.loadImg(imageView, item.headImg);
            }
        };
        rvMember.setAdapter(memberAdapter);
    }

    public void refreshMemberUI() {
        int currentItem = viewPager.getCurrentItem();
        ChatCardBean card = cards.get(currentItem);
        if (card.member == null) return;
        if (Integer.parseInt(card.member.usersNum) > 3) {
            List<CardMemberBean> beans = card.member.members.subList(0, 3);
            beans.add(new CardMemberBean());
            memberAdapter.setNewData(beans);
        } else {
            memberAdapter.setNewData(card.member.members);
        }
        tvCircleCount.setText(String.format("%s人", card.member.usersNum));
    }

    private static final String TAG = "ChatCardActivity";


    @Override
    public void setMemberList(int position, CardMemberModel data) {
        if (position > cards.size() - 1) return;
        cards.get(position).member = data;
        if (position == viewPager.getCurrentItem()) refreshMemberUI();
    }

    @Override
    public void setCardList(ArrayList<ChatCardBean> records) {
        L.e(TAG, "setCardList: " + records.get(0).toString());
        if (records != null && records.size() > 0) {
            titleBar.setTitle(records.get(0).circleName);
            titleBar.setTitleColor(R.color.text_white);
            cards.addAll(records);
            cardAdapter.notifyDataSetChanged();
            circleId = records.get(0).circleId;
            p.getMemberList(Integer.parseInt(cardId), circleId);
        }
        if (Integer.parseInt(cardId) > 0) {
            p.getCardList(cardId, circleId, -1, 0,0,0);
        }
    }

    @Override
    public void addLeftCardList(ArrayList<ChatCardBean> records) {
        if (!CommentUtils.isListEmpty(records)) {
            cards.addAll(0, records);
            cardAdapter.notifyDataSetChanged();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int currentIndex;
                    if (isFirst) {
                        isFirst = false;
                        currentIndex = records.size();
                    } else {
                        currentIndex = records.size() + 1;
                    }
                    viewPager.setCurrentItem(currentIndex);
                }
            });
        }
    }

    @Override
    public void addRightCardList(ArrayList<ChatCardBean> records) {
        if (!CommentUtils.isListEmpty(records)) {
            cards.addAll(records);
            cardAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void attentionSuccess(String id,int position) {

    }

    @Override
    public void cardLikeSuccess(String id, int position) {

    }

    @Override
    public void cardTopSuccess(String id, int position) {

    }

    @Override
    public void cardPowerSuccess(String id, int position, int powerStatus) {

    }

    @Override
    public void cardDeleteSuccess(String id, int position) {

    }

    @Override
    public void postCardSuccess(boolean isDraft) {

    }
}