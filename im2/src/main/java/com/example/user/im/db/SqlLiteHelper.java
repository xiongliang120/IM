package com.example.user.im.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.im.entity.Conversation;
import com.example.user.im.entity.Message;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/6/10..
 * ClassName  :
 * Description  :
 */
public class SqlLiteHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "xiongliang.db";
    private static SqlLiteHelper ormLiteHelper;
    private Map<String, Dao> daoMap = new HashMap<String, Dao>();

    public SqlLiteHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }


    public static SqlLiteHelper getInstance(Context context) {
        if (ormLiteHelper == null) {
            ormLiteHelper = new SqlLiteHelper(context);
        }
        return ormLiteHelper;
    }

    /**
     * 创建数据库表
     *
     * @param sqLiteDatabase
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Conversation.class);
            TableUtils.createTable(connectionSource, Message.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据库表
     *
     * @param sqLiteDatabase
     * @param connectionSource
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            //drop和clear的区别
            TableUtils.dropTable(connectionSource, Conversation.class, true);  //删除表操作
            TableUtils.dropTable(connectionSource, Message.class, true);
            onCreate(sqLiteDatabase, connectionSource); //创建表操作
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daoMap.containsKey(className)) {
            dao =  daoMap.get(className);
        }

        if(dao == null) {
            dao = super.getDao(clazz);
            daoMap.put(className,dao);
        }

        return dao;
    }



    @Override
    public void close() {
        super.close();
        if (daoMap != null && daoMap.size() >0) {
            for (Map.Entry<String,Dao> entry : daoMap.entrySet()) {
                Dao dao = entry.getValue();
                dao = null;
            }
        }
    }
}
