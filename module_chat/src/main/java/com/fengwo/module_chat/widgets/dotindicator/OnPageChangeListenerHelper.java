//package com.fengwo.module_chat.widgets.dotindicator;
//
//import android.util.Log;
//
//import androidx.viewpager.widget.ViewPager;
//
//public abstract class OnPageChangeListenerHelper implements ViewPager.OnPageChangeListener {
//    private int currentPage;
//    private int lastPage;
//
//    private int mPageSize;
//
//    private static final String TAG = "OnPageChangeListenerHel";
//
//    public OnPageChangeListenerHelper(int count) {
//        mPageSize = count;
//    }
//
//    private float lastOffset;
//
//    @Override
//    public final void onPageScrolled(int p, float positionOffset, int positionOffsetPixels) {
//        int selectedPosition = currentPage;
//        int position = getRelativePosition(p);
//        Log.d(TAG, "onPageScrolled: p : " + p + " position :" + position + " currentPage :" + currentPage +" positionOffset : "+ positionOffset);
////        int nextPosition = -1;
////
////        if (positionOffset != 0f){
////            if (lastOffset != 0f){
////                if (positionOffset > lastOffset){//右滑+
////                    nextPosition = getRelativePosition(p+1);
////                }else if (positionOffset < lastOffset){//左滑-
////                    nextPosition = getRelativePosition(p-1);
////                }
////            }
////            lastOffset = positionOffset;
////        }
//
//        if (position != currentPage && positionOffset == 0 || currentPage < position) {
//            resetPosition(selectedPosition);
//            selectedPosition = currentPage = position;
//        }
//
//        if (Math.abs(currentPage - position) > 1) {
//            resetPosition(selectedPosition);
//            currentPage = lastPage;
//        }
//
//        int nextPosition = -1;
//        if (currentPage == position && currentPage + 1 < getPageCount()) {
//            nextPosition = currentPage + 1;
//        } else if (currentPage > position) {
//            nextPosition = selectedPosition;
//            selectedPosition = currentPage - 1;
//        }
//
//
//        Log.d("X--X", "selectedPosition: " +selectedPosition + "nextPosition : " + nextPosition + "positionOffset : " + positionOffset);
//        onPageScrolled(selectedPosition, nextPosition, positionOffset);
//
//        lastPage = position;
//    }
//
//    @Override
//    public final void onPageSelected(int position) {
//        currentPage = getRelativePosition(position);
//        Log.d(TAG, "onPageSelected: " +currentPage);
//    }
//
//    private int getRelativePosition(int position) {
//        int page = position;
//        if (mPageSize > 1) {
//            if (page >= mPageSize - 2) {
//                page = 0;
//            } else if (page <= 0) {
//                page = mPageSize - 3;
//            } else {
//                page = page - 1;
//            }
//        }
//        return page;
//    }
//
//    @Override
//    public final void onPageScrollStateChanged(int i) {
//
//    }
//
//    abstract void onPageScrolled(int selectedPosition, int nextPosition, float positionOffset);
//
//    abstract void resetPosition(int position);
//
//    abstract int getPageCount();
//}
