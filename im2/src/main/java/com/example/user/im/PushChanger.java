package com.example.user.im;

import android.util.Log;

import com.example.user.im.entity.Message;

import java.util.Observable;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/9..
 * ClassName  :
 * Description  :
 */
public class PushChanger extends Observable {
     private static PushChanger instance;

     public static PushChanger getInstance(){
         if(instance == null){
             instance = new PushChanger();
         }
         return instance;
     }

     public void notifyWatchers(Message message){
           setChanged();  //必须添加
           notifyObservers(message);
     }
}
