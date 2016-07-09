package com.example.user.im;

import android.content.Context;
import android.content.Intent;

import com.example.user.im.entity.Message;
import com.example.user.im.entity.MessageStatus;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/9..
 * ClassName  :
 * Description  : 分离表现层和业务层
 */
public class PushManager {
    private static PushManager pushManager;
    private Context mContext; //必须是activity,非activity,startService()需要添加Flag

    public PushManager(Context context){
        this.mContext = context;
    }

    public static PushManager getInstance(Context context){
        if(pushManager == null){
            pushManager = new PushManager(context);
        }
        return pushManager;
    }

    public void handleMessage(Message message){
        PushChanger.getInstance().notifyWatchers(message);
    }

    public void sendMessage(Message message){
       //将发送操作放在service
//        Intent intent = new Intent(mContext,PushServie.class);
//        intent.putExtra(Contans.KEY_MESSAGE,message);
//        mContext.startService(intent);
        message.setMessageStatus(MessageStatus.ing);
        PushChanger.getInstance().notifyWatchers(message);
        message.setMessageStatus(MessageStatus.done);
        PushChanger.getInstance().notifyWatchers(message);
    }

    public void addWatcher(PushWatcher pushWatcher){
          PushChanger.getInstance().addObserver(pushWatcher);
    }

    public void removeWatcher(PushWatcher pushWatcher){
         PushChanger.getInstance().deleteObserver(pushWatcher);
    }

    public void removeWatchers() {
        PushChanger.getInstance().deleteObservers();
    }
}
