package com.fengwo.module_chat.utils.chat_new;

import android.view.View;

import com.fengwo.module_comment.utils.ScreenUtils;

/**
 * @author wrs on 2018/11/29
 */
public class UIUtil {

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView  呼出window的view
     * @param contentViewHeight
     * @param verticalOffset   预留垂直偏移量(单位:px)
     * @param isRight   信息朝向
     * @return window显示的左上角的xOff,yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, int contentViewHeight, int verticalOffset, boolean isRight) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        final int anchorWidth = anchorView.getWidth();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());
        // 计算contentView的高宽
        final int windowHeight = contentViewHeight;
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight + verticalOffset < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = /*screenWidth - windowWidth*/isRight ? anchorLoc[0] : anchorLoc[0] + anchorWidth;
            windowPos[1] = anchorLoc[1] - windowHeight + verticalOffset + anchorHeight;
        } else {
            windowPos[0] = /*screenWidth - windowWidth*/isRight ? anchorLoc[0] : anchorLoc[0] + anchorWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight - verticalOffset;
        }
        return windowPos;
    }
}
