package com.fengwo.module_comment.utils;

import android.app.Activity;
import android.os.Build;

import java.util.Stack;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2016-10-09.
 */
public class ActivitysManager {

    private static Stack<Activity> activityStack;
    private static ActivitysManager instance;

    private ActivitysManager() {
    }
    public int getSize(){
        return activityStack.size();
    }

    public static ActivitysManager getInstance() {
        if (instance == null) {
            instance = new ActivitysManager();
            activityStack = new Stack<>();
        }
        return instance;
    }

    public void popActivity() {
        Activity activity = activityStack.pop();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    public void popActivity(Activity activity) {
        L.e("activitysmanager", activity.getLocalClassName() + "push");
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public Activity currentActivity() {
        if (activityStack.size() > 0) {
            return activityStack.lastElement();
        }
        return null;
    }

    public void pushActivity(Activity activity) {
        L.e("activitysmanager", activity.getLocalClassName() + "push");
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.push(activity);
    }

    public void popAll() {
        for (; ; ) {
            if (activityStack.size() <= 0) {
                return;
            }
            popActivity();
        }
    }

    public void popAll(String activity) {
        //记录不需要移除的Activity
        Activity notRemoveActivity = null;
        for (int i = 0; i < activityStack.size(); i++) {
            Activity a = activityStack.pop();
            if (a != null && !activity.equals(a.getClass().getName())) {
                a.finish();
                i--;
            } else {
                notRemoveActivity = a;
            }
        }
        if (notRemoveActivity != null) {
            //添加回队列
            activityStack.push(notRemoveActivity);
        }
    }

    public void forEach(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activityStack.forEach(new Consumer<Activity>() {
                @Override
                public void accept(Activity activity) {
                    L.e("=== "+activity.getClass().getName());
                }
            });
        }
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }
}
