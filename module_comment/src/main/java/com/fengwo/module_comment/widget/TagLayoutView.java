package com.fengwo.module_comment.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Scroller;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.L;
import com.google.gson.Gson;

import java.util.List;

public class TagLayoutView extends ViewGroup {
    private final String TAG = "TagLayoutView";

    private TagViewAdapter tagAdapter;
    private DataChangeObserve dataChangeObserve;
    // private OnTagClickListener tagClickListener;

    private int tagSpace;
    private int rowSpace;

    public TagLayoutView(Context context) {
        this(context, null);
    }

    public TagLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TagLayoutView);
        tagSpace = a.getDimensionPixelSize(R.styleable.TagLayoutView_tag_tag_space, (int) dp2px(8));
        rowSpace = a.getDimensionPixelSize(R.styleable.TagLayoutView_tag_row_space, (int) dp2px(4));

        a.recycle();
    }
    private float x1, x2;
    private float y1, y2;
    private float swing = 0;
    private float totalswings = 0;
    private Scroller mScroller;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = event.getX();
                y2 = event.getY();

                ///< 5作为阀值就可以了，可以根据效果调整
                if(y1 - y2 > 5) {         ///< 上
                    swing = y1 - y2;
                    ///< 采用刷新onLahyout实现滚动
                    //totalswings += swing;
                    //requestLayout();
                    ///< 采用系统View类的滚动方法
                    scrollBy(0, (int) swing);
                    ///< 采用Scroller滚动辅助类配合computeScroll实现上下滚动
                    //mScroller.startScroll(0, 0, 0, (int) swing);
                    invalidate();

                    Log.e("test", "上滑动");
                } else if(y2 - y1 > 5) { ///< 下
                    swing = -(y2 - y1);
                    ///< 采用刷新onLahyout实现滚动
                    //totalswings += swing;
                    //requestLayout();
                    ///< 采用系统View类的滚动方法
                    scrollBy(0, (int) swing);
                    ///< 采用Scroller滚动辅助类配合computeScroll实现上下滚动
                    //mScroller.startScroll(0, 0, 0, (int) swing);
                    invalidate();

                    Log.e("test", "下滑动");
                } else if(x1 - x2 > 50) { ///< 左
                } else if(x2 - x1 > 50) { ///< 右
                }
                x1 = x2;
                y1 = y2;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }


    private void addTagView() {
        if (tagAdapter == null) {
            return;
        }
        removeAllViews();
        // KLog.d(TAG, "-----addTagView()--" + tagAdapter.getCount());
        for (int index = 0; index < tagAdapter.getCount(); index++) {
            View tagView = tagAdapter.getView(index, null, this);
            addView(tagView);
            // final int position = index;
            // tagView.setOnClickListener(new OnClickListener() {
            // 	@Override
            // 	public void onClick(View v) {
            // 		if (tagClickListener != null) {
            // 			tagClickListener.onClicked(tagAdapter.getItem(position), position);
            // 		}
            // 	}
            // });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wantHeight = 0;
        int wantWidth = resolveSize(0, widthMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int childLeft = paddingLeft;
        int childTop = paddingTop;
        int lineHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View childView = getChildAt(i);
            LayoutParams params = childView.getLayoutParams();
            childView.measure(
                    getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, params.width),
                    getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, params.height)
            );
            int childHeight = childView.getMeasuredHeight();
            int childWidth = childView.getMeasuredWidth();
            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > wantWidth) {
                childLeft = paddingLeft;
                childTop += rowSpace + childHeight;
                lineHeight = childHeight;
            }

            childLeft += childWidth + tagSpace;
        }
        wantHeight += childTop + lineHeight + paddingBottom;
        setMeasuredDimension(wantWidth, resolveSize(wantHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int childLeft = paddingLeft;
        int childTop = paddingTop;
        int lineHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > width) {
                childLeft = paddingLeft;
                childTop += rowSpace + lineHeight;
                lineHeight = childHeight;
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + tagSpace;
        }
    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
    public void setAdapter(TagViewAdapter adapter) {
        if (tagAdapter == null) {
            this.tagAdapter = adapter;
            if (dataChangeObserve == null) {
                dataChangeObserve = new DataChangeObserve();
                tagAdapter.registerDataSetObserver(dataChangeObserve);
            }
            addTagView();
        }
    }

    // public void setOnTagClickListener(OnTagClickListener listener) {
    // 	this.tagClickListener = listener;
    // }

    private class DataChangeObserve extends DataSetObserver {
        @Override
        public void onChanged() {
            // KLog.d(TAG, "-----onChanged()--");
            addTagView();
        }
    }

    //adapter
    public static abstract class TagViewAdapter<T> extends BaseAdapter {
        private List<T> tagList;
        private OnTagClickListener tagClickListener;

        private int currentPosition = -1;

        public TagViewAdapter() {
        }

        public void setTagList(List<T> list) {
            // KLog.d("TagLayoutView", "-----setTagList()--" + list.size());
            this.tagList = list;
            System.out.println(new Gson().toJson(tagList) + "-=-=-=--");
            notifyDataSetChanged();
        }

        public void setCurrentPosition(int currentPosition) {
            this.currentPosition = currentPosition;
            notifyDataSetChanged();
        }

        public int getCurrentPosition() {
            return currentPosition;
        }

        public T getSelectedItem() {
            if (currentPosition == -1) {
                return null;
            }
            return tagList.get(currentPosition);
        }

        @Override
        public int getCount() {
            return tagList != null ? tagList.size() : 0;
        }

        @Override
        public T getItem(int position) {
            return tagList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = createView();
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tagClickListener != null) {
                            tagClickListener.onClicked(position);
                        }
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            bindView(holder, position);
            holder.itemView.setSelected(position == currentPosition);
            return convertView;
        }

        public class ViewHolder {
            public View itemView;

            public ViewHolder(View view) {
                itemView = view;
            }
        }

        protected abstract View createView();

        protected abstract void bindView(ViewHolder holder, int position);

        public void setOnTagClickListener(OnTagClickListener listener) {
            this.tagClickListener = listener;
        }

        public interface OnTagClickListener {
            void onClicked(int position);
        }
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }
}
