package com.fengwo.module_comment.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.adapter.MessageTagListAdapter;
import com.fengwo.module_comment.base.RxHttpUtil;
import com.fengwo.module_comment.widget.TagLayoutView;
import com.fengwo.module_comment.widget.multiImageview.MultiImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import razerdp.basepopup.BasePopupWindow;

public class MessageGreetPopwindow extends BasePopupWindow implements View.OnClickListener {

    TextView tvName;

    TagLayoutView tagMessageGreet;

    MultiImageView ivHeader;

    ImageView ivHeaderBg;

    TextView tvAge;

    private Unbinder bind;
    private MessageTagListAdapter messageTagListAdapter;



    public MessageGreetPopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        setOutSideDismiss(true);
        initUI();
        messageTagListAdapter = new MessageTagListAdapter(getContext());
        tagMessageGreet.setAdapter(messageTagListAdapter);
        messageTagListAdapter.setOnTagClickListener(new TagLayoutView.TagViewAdapter.OnTagClickListener() {
            @Override
            public void onClicked(int position) {
                String tag = messageTagListAdapter.getItem(position);
                if (onSelectListener != null)
                    onSelectListener.onSelect(tag);


            }
        });
        if (onSelectListener != null)
            onSelectListener.onRequest();
    }

    private void initUI() {
        tvName =  findViewById(R.id.tvName);
        tagMessageGreet =  findViewById(R.id.tagMessageGreet);
        ivHeader =  findViewById(R.id.iv_header);
        ivHeaderBg =  findViewById(R.id.iv_header_bg);
        ivHeaderBg =  findViewById(R.id.iv_header_bg);
        tvAge =  findViewById(R.id.tv_age);
        TextView  tvGreetUpdate =  findViewById(R.id.tvGreetUpdate);
        ImageView  ivGreetUpdate =  findViewById(R.id.ivGreetUpdate);
        RelativeLayout root =  findViewById(R.id.root);
        ImageView  ivClose =  findViewById(R.id.ivClose);
        ConstraintLayout rootContent =  findViewById(R.id.rootContent);
        tvGreetUpdate.setOnClickListener(this);
        ivGreetUpdate.setOnClickListener(this);
        root.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        rootContent.setOnClickListener(this);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_message_greet);
        bind = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) bind.unbind();
        RxHttpUtil.clearHttpRequest();
    }

    public void setAdapter(List<String> dataList){
        if (messageTagListAdapter!=null)
            messageTagListAdapter.setTagList(dataList);
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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tvGreetUpdate || id == R.id.ivGreetUpdate) {
            if (onSelectListener != null)
                onSelectListener.onRequest();
        } else if (id == R.id.root || id == R.id.ivClose) {
            dismiss();
        }
    }

    public void Request() {
        if (onSelectListener != null)
            onSelectListener.onRequest();
    }

    public interface OnSelectListener {
        void onSelect(String content);
        void onRequest();
    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
    }
}
