package com.example.user.im.db;

import android.content.Context;

import com.example.user.im.entity.Conversation;
import com.example.user.im.entity.Message;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

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

    public List<Conversation> queryAllByTimeDesc() throws SQLException{
        QueryBuilder queryBuilder = dao.queryBuilder();
        queryBuilder.orderBy(Conversation.TIMESTAMP, false);
        return queryBuilder.query();
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
     *
     * @return
     */
    public Conversation queryByTargetId(String targetId) {
        try {
            return dao.queryBuilder().where().eq("targetId", targetId).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Conversation> queryByUserId(int userId) {
        try {
            return dao.queryBuilder().where().eq("user_id", userId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据Message同步Conversation
     *
     * @param message
     */
    public void syncConversation(Message message) {
        Conversation conversation = message.copyTo(context);
        addConversation(conversation);
    }

    /**
     * 清空unreadNum
     * @param targetId
     */
    public void markAsRead(String targetId) throws SQLException{
        //TODO  执行sql
        dao.executeRaw("update tb_conversation set"+Conversation.UNREADNUM+"=0" +"where targetId = ?",targetId);
    }
}
