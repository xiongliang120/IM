package com.example.user.im.entity;

import android.app.Service;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/9..
 * ClassName  :
 * Description  :
 */
@DatabaseTable(tableName = "tb_conversation")
public class Conversation implements Serializable{
    @DatabaseField(id = true)  //表示一条记录的唯一标识
    private String  targetId;
    @DatabaseField
    private String targetName;
    @DatabaseField
    private String targetPicture;
    @DatabaseField
    private int unReadNum;
    @DatabaseField(columnName = "content")
    private String content;
    @DatabaseField(columnName = "type")
    private MessageType type;
    @DatabaseField(columnName = "messageStatus")
    private MessageStatus messageStatus;
    @DatabaseField
    private long timestamp;

    public Conversation(){}

    public Conversation(String targetId,String content){
        this.targetId = targetId;
        this.content = content;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetPicture() {
        return targetPicture;
    }

    public void setTargetPicture(String targetPicture) {
        this.targetPicture = targetPicture;
    }

    public int getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
