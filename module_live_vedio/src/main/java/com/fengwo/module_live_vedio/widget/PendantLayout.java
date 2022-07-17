package com.fengwo.module_live_vedio.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_live_vedio.R;

import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;

/**
 * @anchor Administrator
 * @date 2020/9/1
 */
public class PendantLayout extends RelativeLayout {
    DragView rl_drag_container;
    ImageView tv_drag_content;
    EditText ed_title;
    int id;


    public PendantLayout(Context context) {
        this(context, null);
    }

    private IAddListListener listener;

    public void settext(String msg) {
        ed_title.setText(msg);
    }

    public interface IAddListListener {

        void deleteBank(int id, View v);

        void moveUpLoaction(int id, int l, int r, String title);
    }

    public void setIDeleteListener(IAddListListener listener) {
        this.listener = listener;
    }

    public PendantLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PendantLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.live_pendant_layout, this);

        View dragViewOutContainer = inflate.findViewById(R.id.dragViewOutContainer);
        View tv_rect_delete = inflate.findViewById(R.id.tv_rect_delete);
        rl_drag_container = inflate.findViewById(R.id.rl_drag_container);
        tv_drag_content = inflate.findViewById(R.id.tv_drag_content);
        View center_drag_container = inflate.findViewById(R.id.center_drag_container);
        View ll_rect_container = inflate.findViewById(R.id.ll_rect_container);
        ed_title = inflate.findViewById(R.id.ed_title);
        ed_title.setMovementMethod(null);
        ed_title.setInputType(TYPE_TEXT_FLAG_MULTI_LINE);
        ed_title.setSingleLine(false);
        ed_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                int[] position = new int[2];
                rl_drag_container.getLocationOnScreen(position);
                if (!TextUtils.isEmpty(ed_title.getText()) && null != listener) {
                    listener.moveUpLoaction(id, position[0], position[1], ed_title.getText().toString());
                    ed_title.clearFocus();
                    KeyBoardUtils.closeKeybord(ed_title, context);
                    //    ed_title.requestFocus();
                }


                return false;
            }
        });
        ed_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int[] position = new int[2];
                rl_drag_container.getLocationOnScreen(position);
                if (!TextUtils.isEmpty(ed_title.getText()) && null != listener) {
                    listener.moveUpLoaction(id, position[0], position[1], ed_title.getText().toString());
//                    ed_title.clearFocus();
//                    KeyBoardUtils.closeKeybord(ed_title, context);
                    //    ed_title.requestFocus();
                }
            }
        });

        rl_drag_container.setOnItemDragListener(new DragView.OnViewDragListener() {
            @Override
            public void onClick(DragView dragView, int mDragType) {
                if(ed_title!=null){
                    ed_title.clearFocus();
                    KeyBoardUtils.closeKeybord(ed_title, context);
                }

                if (null != listener) {
                    //       listener.deleteBank(PendantLayout.this);
                }
            }

            @Override
            public void onDragFinish(DragView dragView, int mDragType) {
                ll_rect_container.setVisibility(View.GONE);
            }

            @Override
            public void onDragStart(int mDragType) {
                ll_rect_container.setVisibility(View.VISIBLE);
                if(ed_title!=null){
                    ed_title.clearFocus();
                    KeyBoardUtils.closeKeybord(ed_title, context);
                }
            }

            @Override
            public void onDraging(DragView dragView, boolean type) {
                //   Log.e("hhh",DragView.isCollsionWithRect(dragView, center_drag_container)+"");
                if (null == listener) {
                    return;
                }
                if (type) {
                    int[] position = new int[2];
                    dragView.getLocationOnScreen(position);
                    if (!TextUtils.isEmpty(ed_title.getText())) {
                        listener.moveUpLoaction(id, position[0], position[1], ed_title.getText().toString());
                    } else {
                        listener.moveUpLoaction(id, position[0], position[1], "");
                    }
                } else
                    listener.deleteBank(id, PendantLayout.this);
//                if (DragView.isCollsionWithRect(dragView, center_drag_container)) {
//
//                } else {
//                    if (null != listener) {
//                    //
//                    }
//                    //删除
////                    rl_drag_container.setVisibility(View.GONE);
////                    ToastUtils.showShort(context,"删除挂件");
//                }
            }
        });
    }

    public void setImgView(String url, int id,int length) {
        this.id = id;
        rl_drag_container.setTag(url);

        ImageLoader.loadImg(tv_drag_content, url);
        if(length!= -1){
            ed_title.setFilters( new InputFilter[]{new InputFilter.LengthFilter(length)});
            //ed_title.setMaxLines(length);
        }


    }
    public void setEdittext(boolean type,String textcolor) {
        ed_title.setText("");
        if(!TextUtils.isEmpty(textcolor)){
            ed_title.setTextColor(Color.parseColor(textcolor));
        }

        if(type){
            ed_title.setVisibility(VISIBLE);
        }else {
            ed_title.setVisibility(GONE);
        }
    }
}
