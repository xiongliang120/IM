package com.example.user.im.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.user.im.util.Contans;
import com.example.user.im.entity.Message;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/9..
 * ClassName  :
 * Description  :
 */
public class IMPushServie extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message message = (Message) intent.getSerializableExtra(Contans.KEY_MESSAGE);
        switch (message.getMessageType()) {
            case splain:
                sendSplain();
                break;
            case image:
                break;
            case emoji:
                break;
            default:
                break;

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendSplain() {
        //TODO 发送消息到服务器
    }
}
