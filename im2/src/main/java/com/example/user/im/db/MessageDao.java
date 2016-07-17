package com.example.user.im.db;

import android.content.Context;

import com.example.user.im.entity.Message;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/9..
 * ClassName  :
 * Description  :
 */
public class MessageDao {
    private Context context;
    private Dao<Message, Integer> dao;
    private SqlLiteHelper sqlLiteHelper;

    public MessageDao(Context context) {
        this.context = context;
        try {
            sqlLiteHelper = SqlLiteHelper.getInstance(context);
            dao = sqlLiteHelper.getDao(Message.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增
     */
    public void addMessage(Message message) {
        try {
            dao.create(message);
            //更新conversation
            ConversationDao conversationDao = new ConversationDao(context);
            conversationDao.syncConversation(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(List<Message> messages){
        try {
            //网络加载新的30数据,防止加载遗漏网络上的数据
            if(queryById(messages.get(0).get_id()).get_id() == null){
                deleteOldMessage(messages.get(0));
            }

            for (Message message:messages) {
                dao.create(message);
            }
            //更新最新的conversation
            ConversationDao conversationDao = new ConversationDao(context);
            conversationDao.syncConversation(messages.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除旧的数据
     * @param message
     */
    public void deleteOldMessage(Message message) throws SQLException{
        DeleteBuilder<Message,Integer> deleteBuilder = dao.deleteBuilder();
        Where where = deleteBuilder.where();
        where.in("senderId", message.getReceiverId(), message.getSenderId());
        where.and();
        where.in("receiverId", message.getReceiverId(), message.getSenderId());
        where.and();
        where.lt("timestamp", message.getTimestamp()); //&和小于
        deleteBuilder.delete();
    }


    /**
     * 删
     */
    public void deeleteMessageById(int id) {
        try {
            dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新表
     */
    public void updateMessage(Message message) {
        try {
            dao.update(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有
     */
    public List<Message> queryAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> queryBySendAndReceiveId(String sendId,String receiveId,long timestamp) throws SQLException{
        QueryBuilder queryBuilder = dao.queryBuilder();
        queryBuilder.orderBy(Message.TIMESTAMP,true); //降
        Where where = queryBuilder.where();
        where.in("senderId", sendId, receiveId);
        where.and();
        where.in("receiverId",sendId,receiveId);
        if(timestamp != 0){
            where.and();
            where.lt("timestamp",timestamp); //什么意思
        }
        queryBuilder.limit(30);
        return where.query();
    }

    /**
     * 根据id查询
     * 获取的User信息只有id
     * @param id
     * @return
     */
    public Message queryById(String  id) {
        try {
            return (Message) dao.queryForId(Integer.parseInt(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//
//    /**
//     * 获取People以及其属性User的详细信息
//     * @param id
//     * @return
//     */
//    public People queryWithUserById(int id) {
//        People people = null;
//        try {
//            people = (People) dao.queryForId(new Integer(id));
//            sqlLiteHelper.getDao(User.class).refresh(people.getUser()); //同步
//            return people;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public List<Message> queryByUserId(int userId){
        try {
            return dao.queryBuilder().where().eq("user_id", userId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
