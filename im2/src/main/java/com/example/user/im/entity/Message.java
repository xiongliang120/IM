package com.example.user.im.entity;

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
    @DatabaseField(id = true)
    private String string_id;
    @DatabaseField
    private String senderId;
    @DatabaseField
    private String senderName;
    @DatabaseField
    private String senderPicture;
    @DatabaseField
    private String receiverId;
    @DatabaseField
    private String receiverName;
    @DatabaseField
    private String receiverPicture;
    @DatabaseField
    private MessageType messageType;
    @DatabaseField
    private String content;
    @DatabaseField
    private MessageStatus messageStatus;
    @DatabaseField
    private long timeStamp;
    @DatabaseField
    private boolean isRead;
    private int percent;   //没有加入数据库

    public Message(){}

    public Message(String string_id,String content){
        this.string_id = string_id;
        this.content = content;
    }

    public String getString_id() {
        return string_id;
    }

    public void setString_id(String string_id) {
        this.string_id = string_id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPicture() {
        return senderPicture;
    }

    public void setSenderPicture(String senderPicture) {
        this.senderPicture = senderPicture;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPicture() {
        return receiverPicture;
    }

    public void setReceiverPicture(String receiverPicture) {
        this.receiverPicture = receiverPicture;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
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
}
