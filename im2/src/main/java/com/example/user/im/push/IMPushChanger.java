package com.example.user.im.push;

import android.content.Context;

import com.example.user.im.db.MessageDao;
import com.example.user.im.entity.Message;

import java.util.Observable;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/9..
 * ClassName  :
 * Description  :
 */
public class IMPushChanger extends Observable {
    private static IMPushChanger instance;
    private Context mContext;

    public IMPushChanger(Context context) {
        this.mContext = context;
    }

    public static IMPushChanger getInstance(Context context) {
        if (instance == null) {
            instance = new IMPushChanger(context);
        }
        return instance;
    }

    public void notifyWatchers(Message message) {
        //更新数据库
        MessageDao messageDao = new MessageDao(mContext);
        messageDao.addMessage(message);

        setChanged();  //必须添加
        notifyObservers(message);
    }
}
