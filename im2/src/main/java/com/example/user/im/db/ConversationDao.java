package com.example.user.im.db;

import android.content.Context;

import com.example.user.im.entity.Conversation;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/6/10..
 * ClassName  :
 * Description  :
 */
public class ConversationDao {
    private Context context;
    private Dao<Conversation, Integer> dao;
    private SqlLiteHelper sqlLiteHelper;

    public ConversationDao(Context context) {
        this.context = context;
        try {
            sqlLiteHelper = SqlLiteHelper.getInstance(context);
            dao = sqlLiteHelper.getDao(Conversation.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增
     */
    public void addConversation(Conversation conversation) {
        try {
            dao.create(conversation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删
     */
    public void deleteConversationById(int id) {
        try {
            dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新表
     */
    public void updateConversation(Conversation conversation) {
        try {
            dao.update(conversation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有
     */
    public List<Conversation> queryAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据id查询
     * 获取的User信息只有id
     * @param id
     * @return
     */
    public Conversation queryById(int id) {
        try {
            return (Conversation) dao.queryForId(new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


//    /**
//     * 获取People以及其属性User的详细信息
//     * @param id
//     * @return
//     */
//    public Conversation queryWithUserById(int id) {
//        Conversation people = null;
//        try {
//            people = (Conversation) dao.queryForId(new Integer(id));
//            sqlLiteHelper.getDao(User.class).refresh(people.getUser()); //同步
//            return people;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public List<Conversation> queryByUserId(int userId){
        try {
            return dao.queryBuilder().where().eq("user_id", userId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
