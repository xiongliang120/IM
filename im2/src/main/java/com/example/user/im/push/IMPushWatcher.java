package com.example.user.im.push;

import com.example.user.im.entity.Message;

import java.util.Observable;
import java.util.Observer;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/9..
 * ClassName  :
 * Description  :
 */
public class IMPushWatcher implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        //TODO  接收message, 保存数据库,更新UI
        if (o instanceof Message) {
            updateMessage((Message) o);
        }
    }

    public void updateMessage(Message message) {
    }
}
