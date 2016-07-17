package com.example.user.im.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.user.im.R;
import com.example.user.im.baidu.Utils;
import com.example.user.im.db.ConversationDao;
import com.example.user.im.db.MessageDao;
import com.example.user.im.entity.Conversation;
import com.example.user.im.entity.Message;
import com.example.user.im.entity.MessageStatus;
import com.example.user.im.entity.MessageType;
import com.example.user.im.push.IMPushManager;
import com.example.user.im.push.IMPushWatcher;

import java.util.List;

public class HomeActivity extends Activity {
    private static String SEND="xiongliang";
    private static String RECEIVE="xiongli";
    private static String OTHERS="others";
    private static String MSG= "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testSendMessage();
        //testDb();
        testBaidu();
//        try {
//            //testChat();
//           // testConversaton();
//            testBack();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /*
      测试百度推送
     */
    public void testBaidu(){
        String api_key = Utils.getMetaValue(HomeActivity.this, "api_key");
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
                api_key);

    }

    public void testChat() throws Exception{
        IMPushManager.getInstance(this).addWatcher(messageWatcher);
       //TODO query list
        MessageDao messageDao = new MessageDao(this);
        List<Message> list = messageDao.queryBySendAndReceiveId(SEND, RECEIVE,0);
        for (Message message:list) {
            Log.i(MSG,message.getSenderId()+"..."+message.getReceiverId());
        }
        //TODO new Message
        Message message = new Message();
        message.setTimestamp(System.currentTimeMillis());
        message.setSenderId(RECEIVE);
        message.setReceiverId(SEND);
        message.setContent("这是xiongliang发给xiongli的消息11");
        //TODO notify
        IMPushManager.getInstance(this).handlePush(message);

        IMPushManager.getInstance(this).removeWatcher(messageWatcher);
    }

    public void updateConversation() throws Exception{
        ConversationDao conversationDao = new ConversationDao(this);
        List<Conversation> conversationList = conversationDao.queryAllByTimeDesc();
        for (Conversation conversation : conversationList) {
            Log.i(MSG,conversation.getTargetId()+".."+conversation.getContent()+".."+conversation.getUnReadNum());
        }
    }

    public void testConversaton() throws Exception{
        IMPushManager.getInstance(this).addWatcher(conversationWatcher);
        //query db
        updateConversation();

        //new message
        Message message = new Message();
        message.setContent("发送内容");
        message.setSenderId(RECEIVE);
        message.setReceiverId(SEND);
        message.setMessageStatus(MessageStatus.done);

        IMPushManager.getInstance(this).handlePush(message);
        IMPushManager.getInstance(this).removeWatcher(conversationWatcher);

    }

    /**
     * 测试从chat接收消息时,返回conversation的情况
     */
    public void testBack() throws Exception{
        IMPushManager.getInstance(this).addWatcher(messageWatcher);
        Message message = new Message();
        message.setContent("刚开始发送消息");
        message.setSenderId(SEND);
        message.setReceiverId(RECEIVE);
        message.setTimestamp(System.currentTimeMillis());
        message.setMessageStatus(MessageStatus.ing);

        IMPushManager.getInstance(this).handlePush(message);
        ConversationDao conversationDap = new ConversationDao(this);
        conversationDap.markAsRead(RECEIVE);
        IMPushManager.getInstance(this).removeWatcher(messageWatcher);

        IMPushManager.getInstance(this).addWatcher(conversationWatcher);
        message.setMessageStatus(MessageStatus.done);
        IMPushManager.getInstance(this).handlePush(message);
        updateConversation();
        IMPushManager.getInstance(this).removeWatcher(conversationWatcher);
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
        IMPushManager.getInstance(this).addWatcher(messageWatcher);
        Message message = new Message("001", "熊亮1");
        IMPushManager.getInstance(this).handlePush(message);
    }

    /**
     * 测试发消息
     */
    public void testSendMessage() {
        IMPushManager.getInstance(this).addWatcher(messageWatcher);
        Message message = new Message("002", "熊亮2");
        IMPushManager.getInstance(this).sendMessage(message);
    }

    private IMPushWatcher messageWatcher = new IMPushWatcher() {
        @Override
        public void updateMessage(Message message) {
            if(SEND.equals(message.getSenderId())){
                Log.i(MSG,"I send msg to"+message.getReceiverId()+"status="+message.getMessageStatus());
            }else{
                if(RECEIVE.equals(message.getSenderId())){
                    Log.i(MSG,message.getSenderId()+"send  message to me"+"status="+message.getMessageStatus());
                }else if(OTHERS.equals(message.getSenderId())){
                    Log.i(MSG,message.getSenderId()+"send  message to me"+"status="+message.getMessageStatus());
                }
            }
        }
    };

    private IMPushWatcher conversationWatcher = new IMPushWatcher(){
        @Override
        public void updateMessage(Message message) {
            Log.i(MSG,message.getSenderId()+"send message to "+message.getReceiverId()+"status="+message.getMessageStatus()+"未读数");
            super.updateMessage(message);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        IMPushManager.getInstance(this).removeWatcher(messageWatcher);
    }
}
