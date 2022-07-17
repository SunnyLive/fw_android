package com.fengwo.module_flirt.UI.certification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_flirt.Interfaces.IPreStartWbView;
import com.fengwo.module_flirt.P.PreWbPresent;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.HostChatRoomActivity;
import com.fengwo.module_flirt.bean.ReconWbBean;
import com.fengwo.module_flirt.bean.StartWBBean;
import com.fengwo.module_flirt.bean.TopicTagBean;
import com.fengwo.module_live_vedio.mvp.ui.activity.zhubo.LivePushActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.zhubo.StartLiveActivity;
import com.fengwo.module_login.utils.UserManager;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/10
 */
@Route(path = ArouterApi.SELECT_TAG)
public class PreStartWbActivity extends BaseMvpActivity<IPreStartWbView, PreWbPresent> implements IPreStartWbView {
    @BindView(R2.id.fl_tag)
    TagFlowLayout flTag;
    @BindView(R2.id.tv_select_tag_save)
    GradientTextView tvSelectTagSave;
    @BindView(R2.id.iv_close)
    ImageView ivClose;
    @BindView(R2.id.tv_room_title)
    EditText tvRoomTitle;

    private TagAdapter<TopicTagBean> tagAdapter;
    private List<TopicTagBean> datas = new ArrayList<>();
    private List<String> bgColors = Arrays.asList("#ECF5FF","#F7F3FF","#FFF4F5");
    private List<String> textColors = Arrays.asList("#73ABFF","#A78CFF","#FF9F8C");

    private String selecText;

    @Override
    public PreWbPresent initPresenter() {
        return new PreWbPresent();
    }

    @Override
    protected void initView() {
        tvRoomTitle.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else if (source.length()+dest.length()>10){
                    toastTip("超过最大字数");
                    return source.subSequence(0, 10 - dest.length());
                }else {
                    return null;
                }
            }
        }});
        p.getTopicTag();
        p.reconnec();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_select_tag;
    }

    @OnClick({R2.id.iv_close, R2.id.tv_select_tag_save})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.iv_close){
            finish();
        }else if (view.getId() == R.id.tv_select_tag_save){
            String roomTitle = tvRoomTitle.getText().toString().trim();
            if (TextUtils.isEmpty(roomTitle)){
                toastTip("交友话题不能为空");
                return;
            }
            if (FloatingView.getInstance().isShow()) {
                showExitDialog(roomTitle);
            } else {
                p.startFlirt(roomTitle);
            }
        }
    }

    private void setTagAdapter(){
        tagAdapter = new TagAdapter<TopicTagBean>(datas) {
            @Override
            public View getView(FlowLayout parent, int position, TopicTagBean o) {
                int n  = position%textColors.size();
                GradientTextView v = (GradientTextView) LayoutInflater.from(PreStartWbActivity.this).inflate(R.layout.item_flow_text, null);
                v.setColors(Color.parseColor(bgColors.get(n)),Color.parseColor(bgColors.get(n)));
                v.setText(o.topicName);
                v.setTextColor(Color.parseColor(textColors.get(n)));
                return v;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                GradientTextView text = (GradientTextView) view;
                int start = getResources().getColor(R.color.colorAccent);
                int end = getResources().getColor(R.color.colorPrimary);
                text.setColors(end,end);
                text.setTextColor(getResources().getColor(R.color.white));
                selecText = text.getText().toString();
                tvRoomTitle.setText(selecText);
                if (selecText.length()<=10)
                    tvRoomTitle.setSelection(selecText.length());
//                Intent intent = new Intent();
//                intent.putExtra("title",text.getText().toString());
//                intent.putExtra("menuId",datas.get(position).getId());
//                setResult(RESULT_OK,intent);
//                finish();
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                int n  = position%textColors.size();
                GradientTextView text = (GradientTextView) view;
                text.setColors(Color.parseColor(bgColors.get(n)),Color.parseColor(bgColors.get(n)));
                text.setTextColor(Color.parseColor(textColors.get(n)));
                selecText = "";
                tvRoomTitle.setText(selecText);
                tvRoomTitle.setSelection(selecText.length());
            }
        };
        flTag.setAdapter(tagAdapter);
        flTag.setMaxSelectCount(1);
    }

    @Override
    public void startWB(StartWBBean startWBBean) {
        HostChatRoomActivity.start(PreStartWbActivity.this, startWBBean);
        finish();
    }

    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog(String roomTitle) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onCancel() {
                  //      p.startFlirt(roomTitle);
                    }
                })
                .setGear(floatingView.getGear())
                .setNickname(floatingView.getNickname())
                .setExpireTime(floatingView.getExpireTime())
                .setHeadImg(floatingView.getHeadImg())
                .setRoomId(floatingView.getRoomId())
                .setTip("退出达人房间，印象值将归零\n是否要退出")
                .show(getSupportFragmentManager(), "");
    }

    @Override
    public void setTagList(BaseListDto<TopicTagBean> list) {
        datas = list.records;
        setTagAdapter();
    }

    @Override
    public void reconnec(ReconWbBean reconWbBean) {
        if (reconWbBean.isLive) {
            new CustomerDialog.Builder(this).setTitle("提示")
                    .setMsg("由于您上次异常断开直播，是否需要继续上次直播")
                    .setPositiveButton("需要", new CustomerDialog.onPositiveInterface() {
                        @Override
                        public void onPositive() {
                            HostChatRoomActivity.start(PreStartWbActivity.this, reconWbBean.startLiveVO);
                            finish();
                        }
                    }).setNegativeButton("不需要", new CustomerDialog.onNegetiveInterface() {
                @Override
                public void onNegetive() {
                    p.rejectReconnect();
                }
            }).setOutSideCancel(false)
                    .create().show();
        }
    }

}
