package com.fengwo.module_chat.mvp.ui.activity.message;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.presenter.MessagesPresenter;
import com.fengwo.module_chat.mvp.ui.activity.social.AddressBookActivity;
import com.fengwo.module_chat.mvp.ui.adapter.ChatMessagesAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IMessageActivityView;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

/**
 * 消息列表
 *
 * @author chenshanghui
 * @date 2019/9/16
 */
public class MessagesActivity extends BaseMvpActivity<IMessageActivityView, MessagesPresenter> implements IMessageActivityView {

    @BindView(R2.id.titleBar)
    AppTitleBar titleBar;
    @BindView(R2.id.rvMsg)
    RecyclerView rvMsg;
    @BindView(R2.id.smartFreshLayout)
    SmartRefreshLayout smartFreshLayout;

    public static void start(Context context) {
        Intent intent = new Intent(context, MessagesActivity.class);
        context.startActivity(intent);
    }


    private ChatMessagesAdapter mAdapter;

    private ArrayList<String> mData = new ArrayList<>();

    @Override
    public MessagesPresenter initPresenter() {
        return new MessagesPresenter();
    }

    @Override
    protected void initView() {
        WhoBody whoBody1 = new WhoBody();
        whoBody1.setUserIds(1);
        whoBody1.setUserName("漂亮尼姑");
        whoBody1.setUserDesc("用户介绍的内容.....");
//        whoBody1.setUserPic(R.drawable.e18950107292);
        whoBody1.setHeadImg("18950107292.png");
        whoBody1.setHeadImgIp("118.31.68.230");
        titleBar.setBackgroundResource(R.drawable.rect_gradient_purple);
        titleBar.setMoreClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddressBookActivity.class);
            }
        });
        rvMsg.setLayoutManager(new LinearLayoutManager(this));
        p.getMessageList(1);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_messages;
    }

    @Override
    public void setMessageList(List<MessagesPresenter.Friend> d) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = new ChatMessagesAdapter(d);
                rvMsg.setAdapter(mAdapter);
            }
        });

    }


    public class WhoBody {

        int userIds;
        String userName;
        String userDesc;
        int userPic;
        String headImg;
        String headImgIp;

        public String getHeadImgIp() {
            return headImgIp;
        }

        public void setHeadImgIp(String headImgIp) {
            this.headImgIp = headImgIp;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getUserIds() {
            return userIds;
        }

        public void setUserIds(int userIds) {
            this.userIds = userIds;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserDesc() {
            return userDesc;
        }

        public void setUserDesc(String userDesc) {
            this.userDesc = userDesc;
        }

        public int getUserPic() {
            return userPic;
        }

        public void setUserPic(int userPic) {
            this.userPic = userPic;
        }
    }
}
