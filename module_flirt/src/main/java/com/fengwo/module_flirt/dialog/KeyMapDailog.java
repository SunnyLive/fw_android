package com.fengwo.module_flirt.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.widgets.chat_new.MsgEditText;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.event.GameEvent;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.EmotionGridViewAdapter;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.utils.chat.ChatEmotionFragment;
import com.fengwo.module_comment.utils.chat.EmotionPagerAdapter;
import com.fengwo.module_comment.utils.chat.EmotionUtils;
import com.fengwo.module_comment.utils.chat.GlobalOnItemClickManagerUtils;
import com.fengwo.module_comment.utils.chat.IndicatorView;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.CommentWordDto;
import com.fengwo.module_live_vedio.mvp.dto.ZhuboMenuDto;
import com.fengwo.module_live_vedio.utils.MaxHeightLayoutmanager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class KeyMapDailog extends DialogFragment {

    //点击发表，内容不为空时的回调
    public SendBackListener sendBackListener;
    private ImageView iv_caiquan, iv_touzi, im_choice, iv_expression;

    ViewPager fragmentChatVp;
    private IndicatorView fragmentChatGroup;
    private EmotionPagerAdapter emotionPagerAdapter;
    ImageView mycheckbox;
    LinearLayout ll_kjhf;
    private RecyclerView rv;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private EditText inputDlg;
    LinearLayout ll_type;
    private QuickTalkAdapter quickTalkAdapter;
    private int page = 0;
    private IndicatorView fragment_chat_group;
    LinearLayout ll_bq;
    private boolean type;

    private View contentview;

    public void setHeigth(int heigth) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_kjhf.getLayoutParams();
        layoutParams.height = heigth - 80;
        ll_kjhf.setLayoutParams(layoutParams);
    }


    public void setType(boolean type) {
        this.type = type;
        if(null!=mycheckbox&&null!=ll_type){
            if (type) {
                mycheckbox.setVisibility(View.GONE);
                ll_type.setVisibility(View.GONE);
            } else {
                ll_type.setVisibility(View.VISIBLE);
                mycheckbox.setVisibility(View.VISIBLE);
            }
        }

    }

    public interface SendBackListener {
        void sendBack(String inputText);

        void setOnDismiss(boolean isforce);

        void setVideoSwitch();
    }


    @SuppressLint("ValidFragment")
    public KeyMapDailog( SendBackListener sendBackListener) {//提示文字
        this.sendBackListener = sendBackListener;

    }

    public EditText getInputDlg() {
        return inputDlg;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        if(null==contentview){
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
         contentview = View.inflate(getActivity(), R.layout.comment_dialog_layout, null);
        initView(contentview);
        dialog.setContentView(contentview);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        final Handler hanler = new Handler();
        dialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                KeyBoardUtils.closeKeybord(inputDlg, getContext());
                sendBackListener.setOnDismiss(true);
                dismiss();
                return false;
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hanler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideSoftkeyboard();
                    }
                }, 200);

            }
        });
        initKjhf();
        }
        return dialog;
    }

    private void initView(View contentview) {
        rv = contentview.findViewById(R.id.rv);
        LinearLayout ll_View = contentview.findViewById(R.id.ll_View);
        ll_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ll_type = contentview.findViewById(R.id.ll_type);
        iv_caiquan = contentview.findViewById(R.id.iv_caiquan);
        mycheckbox = contentview.findViewById(R.id.cb_kjhf);
        if (type) {
            mycheckbox.setVisibility(View.GONE);
            ll_type.setVisibility(View.GONE);
        } else {
            ll_type.setVisibility(View.VISIBLE);
            mycheckbox.setVisibility(View.VISIBLE);
        }
        ll_kjhf = contentview.findViewById(R.id.ll_kjhf);
        fragment_chat_group = contentview.findViewById(R.id.fragment_chat_group);
        iv_touzi = contentview.findViewById(R.id.iv_touzi);
        ll_bq = contentview.findViewById(R.id.ll_bq);
        im_choice = contentview.findViewById(R.id.im_choice);
        iv_expression = contentview.findViewById(R.id.iv_expression);
        fragmentChatVp = contentview.findViewById(R.id.fragment_chat_vp);
        fragmentChatGroup = contentview.findViewById(R.id.fragment_chat_group);
        fragmentChatVp.setVisibility(View.GONE);
        inputDlg = (EditText) contentview.findViewById(R.id.dialog_comment_content);
        mycheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBackListener.setVideoSwitch();
            }
        });
        iv_caiquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.get().post(new GameEvent(GameEvent.TYPE_CAIQUAN));
            }
        });
        iv_touzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.get().post(new GameEvent(GameEvent.TYPE_TOUZI));
            }
        });
        im_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (im_choice.isSelected()) {
                    setViewGsonType(1);
                } else {
                    setViewGsonType(2);
                }
            }
        });
        iv_expression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewGsonType(0);
            }
        });
        final ImageView tv_send = contentview.findViewById(R.id.dialog_comment_send);
        inputDlg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tv_send.setImageResource(R.drawable.pic_ends);
                } else {
                    tv_send.setImageResource(R.drawable.pic_end);
                }

            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputDlg.getText().toString().trim())) {
//
                    Toast.makeText(getActivity(), "输入内容为空", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCanceledOnTouchOutside(false);
                    sendBackListener.sendBack(inputDlg.getText().toString());
                    inputDlg.setText("");
                }
            }
        });
        inputDlg.setFocusable(true);
        inputDlg.setFocusableInTouchMode(true);
        inputDlg.requestFocus();
    }




    public void setViewGsonType(int type) {
        switch (type) {
            case 0://打开表情   键盘和表情的切换
                KeyBoardUtils.closeKeybord(inputDlg, getContext()); //关闭键盘

                initEmotion();//添加表情
                ll_kjhf.setSelected(false);
                ll_kjhf.setVisibility(View.GONE);//隐藏快捷回复
                fragment_chat_group.setVisibility(View.VISIBLE);//显示表情
                fragmentChatVp.setVisibility(View.VISIBLE);
                break;
            case 1://收回表情
                KeyBoardUtils.openKeybord(inputDlg, getContext());//打开键盘
                im_choice.setSelected(false);//设置表情未选
                fragment_chat_group.setVisibility(View.GONE);//隐藏表情
                fragmentChatVp.setVisibility(View.GONE);
                ll_kjhf.setVisibility(View.GONE);//也隐藏快捷回复
                break;
            case 2://打开快捷回复   键盘和快捷回复的切换
                KeyBoardUtils.closeKeybord(inputDlg, getContext()); //关闭键盘
                ll_kjhf.setVisibility(View.VISIBLE);//显示快捷回复
                mycheckbox.setSelected(true);
                fragment_chat_group.setVisibility(View.GONE);//隐藏表情
                fragmentChatVp.setVisibility(View.GONE);
                im_choice.setSelected(true);//设置表情已选
                break;
            case 3://收回快捷回复
                ll_kjhf.setVisibility(View.GONE);//隐藏快捷回复
                im_choice.setSelected(false); //关闭快捷回复 弹出键盘 自然表情变成calse  再点不会无效
                mycheckbox.setSelected(false);
                KeyBoardUtils.openKeybord(inputDlg, getContext());//打开键盘
                fragment_chat_group.setVisibility(View.GONE);//隐藏表情
                fragmentChatVp.setVisibility(View.GONE);
                break;
        }

    }

    private void initKjhf() {

        getQuickTalk(page);


        MaxHeightLayoutmanager layoutManager = new MaxHeightLayoutmanager(getContext(),
                DensityUtils.dp2px(getContext(), 300));
        quickTalkAdapter = new QuickTalkAdapter(null);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(quickTalkAdapter);
    }

    private void getQuickTalk(int current) {
        RequestBody build = new WenboParamsBuilder()
                .put("current", String.valueOf(current))
                .put("size", "20")
                .put("userPort", "1")
                .build();
        new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .getCommentWord(build)
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<CommentWordDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<CommentWordDto>> data) {

                        if (data.isSuccess()) {
//                            tagAdapter = new TagAdapter<CommentWordDto>(data.data.records) {
//                                @Override
//                                public View getView(FlowLayout parent, int position, CommentWordDto o) {
//                                    GradientTextView v = (GradientTextView) LayoutInflater.from(getContext()).inflate(com.fengwo.module_live_vedio.R.layout.live_item_zhubomenu, null);
//                                    v.setText(o.getTitle() + "");
//                                    v.setTextColor(Color.parseColor("#999999"));
//                                    return v;
//                                }
//
//                                @Override
//                                public void onSelected(int position, View view) {
//                                    super.onSelected(position, view);
//                                    GradientTextView text = (GradientTextView) view;
//                                    int start = getContext().getResources().getColor(com.fengwo.module_live_vedio.R.color.homt_tab_selsct_all);
//                                    int end = getContext().getResources().getColor(com.fengwo.module_live_vedio.R.color.homt_tab_selsct_all);
//                                    text.setColors(start, end);
//                                    text.setTextColor(getContext().getResources().getColor(com.fengwo.module_live_vedio.R.color.text_white_arr));
//                                }
//
//                                @Override
//                                public void unSelected(int position, View view) {
//                                    super.unSelected(position, view);
//                                    GradientTextView text = (GradientTextView) view;
//                                    int start = Color.parseColor("#dddddd");
//                                    int end = Color.parseColor("#dddddd");
//                                    text.setColors(start, end);
//                                    text.setTextColor(Color.parseColor("#999999"));
//                                }
//                            };
//                            flowLayout.setAdapter(tagAdapter);
                            if (page == 1) quickTalkAdapter.setNewData(data.data.records);
                            else quickTalkAdapter.addData(data.data.records);
                        } else {
                            ToastUtils.showShort(getContext(), data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                });

    }


    private class QuickTalkAdapter extends BaseQuickAdapter<CommentWordDto, BaseViewHolder> {

        public QuickTalkAdapter(@Nullable List<CommentWordDto> data) {
            super(R.layout.live_item_quicktalk_flirt, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, CommentWordDto item) {
            helper.setText(R.id.tv_content, item.getTitle());
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != sendBackListener){
                        sendBackListener.sendBack(item.getTitle());
                    }
                    //跟ios统一，点击后关闭窗口
                    sendBackListener.setOnDismiss(true);
                    dismiss();
                }
            });
        }
    }

    public void hideProgressdialog() {
        progressDialog.cancel();
    }

    public EditText setClone() {
        if (getShowsDialog() && null != inputDlg) {
            KeyBoardUtils.closeKeybord(inputDlg, getContext());
        }

        return inputDlg;
    }

    public boolean getRjp() {
        if (fragment_chat_group.getVisibility() == View.VISIBLE || ll_kjhf.getVisibility() == View.VISIBLE) {//是否是表情或者快捷回复
            return true;
        } else {
            dismiss();
            return false;
        }
    }


    private void initEmotion() {
        //绑定输入框
        GlobalOnItemClickManagerUtils.getInstance().attachToEditText(inputDlg, 100);
        // 获取屏幕宽度
        int screenWidth = ScreenUtils.getScreenWidth(getContext());
        // item的间距
        int spacing = DensityUtils.dp2px(getContext(), 12);
        // 动态计算item的宽度和高度
        int itemWidth = (screenWidth - spacing * 8) / 7;
        //动态计算gridview的总高度
        int gvHeight = itemWidth * 3 + spacing * 6;
        List<GridView> emotionViews = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();

        for (String emojiName : EmotionUtils.EMOTION_STATIC_MAP.keySet()) {
            emotionNames.add(emojiName);
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 23) {
                GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                emotionViews.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<>();
            }
        }

        // 判断最后是否有不足23个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            emotionViews.add(gv);
        }

        //初始化指示器
        fragmentChatGroup.initIndicator(emotionViews.size());
        // 将多个GridView添加显示到ViewPager中
        emotionPagerAdapter = new EmotionPagerAdapter(emotionViews);
        fragmentChatVp.setAdapter(emotionPagerAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        fragmentChatVp.setLayoutParams(params);
    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
// 创建GridView
        GridView gv = new GridView(getActivity());
        //设置点击背景透明
        gv.setSelector(android.R.color.transparent);
        //设置7列
        gv.setNumColumns(8);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding * 2);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionGridViewAdapter adapter = new EmotionGridViewAdapter(getActivity(), emotionNames, itemWidth);
        gv.setAdapter(adapter);
        //设置全局点击事件
        gv.setOnItemClickListener(GlobalOnItemClickManagerUtils.getInstance().getOnItemClickListener());
        return gv;
    }


    public void hideSoftkeyboard() {
        try {
            ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalOnItemClickManagerUtils.getInstance().attachToEditText(null, 100);
    }
}
