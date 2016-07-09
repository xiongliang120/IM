package com.example.user.im.db;

import android.content.Context;

import com.example.user.im.entity.Message;
import com.j256.ormlite.dao.Dao;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    /**
     * 根据id查询
     * 获取的User信息只有id
     * @param id
     * @return
     */
    public Message queryById(int id) {
        try {
            return (Message) dao.queryForId(new Integer(id));
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
