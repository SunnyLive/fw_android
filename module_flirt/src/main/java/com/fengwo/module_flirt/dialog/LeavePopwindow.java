package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.fengwo.module_comment.base.BasePopWindow;
import com.fengwo.module_flirt.R;

import razerdp.basepopup.BasePopupWindow;

public class LeavePopwindow extends BasePopupWindow {

    public LeavePopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_leave);
    }

    public interface LeavePopClickListener {
        void leaveClick();
    }

    LeavePopClickListener listener;

    public void setLeavePopClickListener(LeavePopClickListener l) {
        listener = l;
    }


}
