package com.fengwo.module_chat.utils.chat_new;

import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.greendao.gen.ChatItemEntityDao;
import com.greendao.gen.ChatListItemEntityDao;
import com.greendao.gen.DaoMaster;
import com.greendao.gen.FlirtChatEntityDao;
import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

public class MyOpenHelper extends DaoMaster.OpenHelper {
    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        changeException(db, oldVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, ChatItemEntityDao.class, ChatListItemEntityDao.class, FlirtChatEntityDao.class);
    }

    /**
     * 修改错误数据
     * 数据库版本8之前的版本，sendStatus字段判断有问题，所以需要重置为0
     * 数据库版本3之后的版本，3之后的版本才有sendStatus字段
     */
    private void changeException(Database db, int oldVersion) {
        if (oldVersion > 3 && oldVersion < 8) {
            try {
                DaoConfig daoConfig = new DaoConfig(db, FlirtChatEntityDao.class);
                String tableName = daoConfig.tablename;
                String sql = "UPDATE " + tableName + " SET " + FlirtChatEntityDao.Properties.SendStatus.columnName + " = 0";
                db.execSQL(sql);
            } catch (Exception e) {
                BuglyLog.e("MyOpenHelper", "数据库版本8之前的版本，sendStatus字段判断有问题，所以需要重置为0, 更改数据库报错，该版本的数据版本为：" + oldVersion);
                CrashReport.postCatchedException(e);
            }
        }
    }
}