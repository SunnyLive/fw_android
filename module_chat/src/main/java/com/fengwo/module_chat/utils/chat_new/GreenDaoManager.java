package com.fengwo.module_chat.utils.chat_new;

import android.content.Context;

import com.fengwo.module_comment.utils.KLog;
import com.greendao.gen.DaoMaster;
import com.greendao.gen.DaoSession;

public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private MyOpenHelper mDevOpenHelper;
    private static GreenDaoManager mInstance = null;

    private GreenDaoManager(Context context) {
        mDevOpenHelper = new MyOpenHelper(context, "chat.db");
        mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static GreenDaoManager getInstance() {
        return mInstance;
    }

    public static void initGreenDaoManager(Context context) {
        if (mInstance == null) mInstance = new GreenDaoManager(context);
        KLog.a("GreenDaoManager -> mInstance = " + mInstance);
    }

    public DaoSession getSession() {
        KLog.a("GreenDaoManager","GreenDaoManager -> getSession = " + mDaoSession);
        if (null == mDaoSession){
            if (null == mDaoMaster)
                mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
            KLog.a("GreenDaoManager","GreenDaoManager -> again getSession = " + mDaoSession);
        }
        return mDaoSession;
    }
}
