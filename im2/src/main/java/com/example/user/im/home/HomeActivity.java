package com.example.user.im.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.user.im.PushChanger;
import com.example.user.im.PushManager;
import com.example.user.im.PushWatcher;
import com.example.user.im.R;
import com.example.user.im.db.ConversationDao;
import com.example.user.im.db.MessageDao;
import com.example.user.im.entity.Conversation;
import com.example.user.im.entity.Message;
import com.example.user.im.entity.MessageStatus;
import com.example.user.im.entity.MessageType;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testSendMessage();
        testDb();
    }


    /**
     * 测试数据库
     */
    public void testDb(){
        Conversation conversation = new Conversation("001","熊亮的Conversation");

        ConversationDao conversationDao = new ConversationDao(this);
        conversationDao.addConversation(conversation);

        Message message = new Message("002","熊亮的Message");
        message.setMessageStatus(MessageStatus.done);
        message.setMessageType(MessageType.splain);
        MessageDao messageDao = new MessageDao(this);
        messageDao.addMessage(message);

    }
    /**
     * 测试收消息
     */
    public void testPushMessage() {
        PushManager.getInstance(this).addWatcher(pushWatcher);
        Message message = new Message("001", "熊亮1");
        PushManager.getInstance(this).handleMessage(message);
    }

    /**
     * 测试发消息
     */
    public void testSendMessage() {
        PushManager.getInstance(this).addWatcher(pushWatcher);
        Message message = new Message("002", "熊亮2");
        PushManager.getInstance(this).sendMessage(message);
    }

    private PushWatcher pushWatcher = new PushWatcher() {
        @Override
        public void updateMessage(Message message) {
            Log.i("msg", "内容" + message.getContent() + "状态" + message.getMessageStatus());
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PushManager.getInstance(this).removeWatcher(pushWatcher);
    }
}
