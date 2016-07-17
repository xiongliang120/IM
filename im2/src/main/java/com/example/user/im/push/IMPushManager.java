package com.example.user.im.push;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.user.im.entity.Conversation;
import com.example.user.im.util.Contans;
import com.example.user.im.entity.Message;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/9..
 * ClassName  :
 * Description  : 分离表现层和业务层
 */
public class IMPushManager {
    private static IMPushManager IMPushManager;
    private Context mContext; //必须是activity,非activity,startService()需要添加Flag

    public IMPushManager(Context context) {
        this.mContext = context;
    }

    public static IMPushManager getInstance(Context context) {
        if (IMPushManager == null) {
            IMPushManager = new IMPushManager(context);
        }
        return IMPushManager;
    }


    public void handlePush(String msg) {
        //转换为Messag
        try {
            JSONObject jsonObject = new JSONObject(msg);
            Gson gson = new Gson();
            Message message = gson.fromJson(jsonObject.toString(), Message.class);
            Log.i("msg", message.getSenderId() + "..." + message.getReceiverId());
            IMPushChanger.getInstance(mContext).notifyWatchers(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void handlePush(Message message) {
        IMPushChanger.getInstance(mContext).notifyWatchers(message);
    }


    public void sendMessage(Message message) {
        //将发送操作放在service
        Intent intent = new Intent(mContext, IMPushServie.class);
        intent.putExtra(Contans.KEY_MESSAGE, message);
        mContext.startService(intent);
//        message.setMessageStatus(MessageStatus.ing);
//        IMPushChanger.getInstance(mContext).notifyWatchers(message);
//        message.setMessageStatus(MessageStatus.done);
//        IMPushChanger.getInstance(mContext).notifyWatchers(message);
    }

    public void addWatcher(IMPushWatcher IMPushWatcher) {
        IMPushChanger.getInstance(mContext).addObserver(IMPushWatcher);
    }

    public void removeWatcher(IMPushWatcher IMPushWatcher) {
        IMPushChanger.getInstance(mContext).deleteObserver(IMPushWatcher);
    }

    public void removeWatchers() {
        IMPushChanger.getInstance(mContext).deleteObservers();
    }
}
