package com.example.user.im.entity;

import android.content.Context;

import com.example.user.im.db.ConversationDao;
import com.example.user.im.IMApplication;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/9..
 * ClassName  :
 * Description  : 基本数据类型都有默认值
 */
@DatabaseTable(tableName = "tb_message")
public class Message implements Serializable {
    public static String TIMESTAMP="timeStamp";

    @DatabaseField(id = true)
    private String _id;
    @DatabaseField
    private String senderId;
    @DatabaseField
    private String sender_name;
    @DatabaseField
    private String sender_picture;
    @DatabaseField
    private String receiverId;
    @DatabaseField
    private String receiver_name;
    @DatabaseField
    private String receiver_picture;
    @DatabaseField
    private MessageType messageType;
    @DatabaseField
    private String content;
    @DatabaseField
    private MessageStatus messageStatus;
    @DatabaseField
    private long timestamp;
    @DatabaseField
    private boolean isRead;
    private int percent;   //没有加入数据库

    public Message(){}

    public Message(String _id,String content){
        this._id = _id;
        this.content = content;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }


    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_picture() {
        return sender_picture;
    }

    public void setSender_picture(String sender_picture) {
        this.sender_picture = sender_picture;
    }

    public String getReceiver_picture() {
        return receiver_picture;
    }

    public void setReceiver_picture(String receiver_picture) {
        this.receiver_picture = receiver_picture;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }



    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }



    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String toString(){
        return "sendId="+senderId+"sendName="+sender_name+"receiverId="+receiverId+"receiverName="+receiver_name;
    }

    /**
     * Message转换为Conversation
     * @return
     */
    public Conversation copyTo(Context context){
        Conversation conversation = new Conversation();
        conversation.setContent(getContent());
        conversation.setType(getMessageType());
        conversation.setMessageStatus(getMessageStatus());
        conversation.setTimestamp(getTimestamp());

        if(getSenderId().equals(IMApplication.self_id)){
            conversation.setTargetId(getReceiverId());
            conversation.setTargetName(getReceiver_name());
            conversation.setTargetPicture(getReceiver_picture());
            ConversationDao conversationDao = new ConversationDao(context);
            Conversation tmp = conversationDao.queryByTargetId(getSenderId());
            conversation.setUnReadNum(tmp == null ? 1 : tmp.getUnReadNum());

        }else{
            conversation.setTargetId(getSenderId());
            conversation.setTargetName(getSender_name());
            conversation.setTargetPicture(getSender_picture());
            ConversationDao conversationDao = new ConversationDao(context);
            Conversation tmp = conversationDao.queryByTargetId(getSenderId());
            if(tmp ==null){
                conversation.setUnReadNum(1);
            }else{
                conversation.setUnReadNum(tmp.getUnReadNum()+1);
            }

        }
        return conversation;
    }
}
