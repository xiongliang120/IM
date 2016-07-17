package com.example.user.im.util;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/14..
 * ClassName  :
 * Description  :
 */
public class UrlHelper {
    private static final String DOMAIN = "http://im.stay4it.com/v1";
    private static final String LOGIN = "/user/account/login";
    private static final String ACTION_GET_CONVERSATION = "/user/message/getConversationList";
    private static final String ACTION_GET_MESSAGE = "/user/message/getAllMessages";
    private static int PAGESIZE = 30;

    public static String loadLogin() {
        return DOMAIN + LOGIN;
    }

    public static String loadConversation() {
        return DOMAIN + ACTION_GET_CONVERSATION;
    }

    public static String loadAllMsg(String id, int state, long timestamp) {
        if (state == Contans.REFRESH || state == Contans.LOADMORE) {
            return DOMAIN + ACTION_GET_MESSAGE + "/" + id + "?endTimestamp=" + timestamp + "&timestamp=0&count=" + PAGESIZE;
        } else {
            return DOMAIN + ACTION_GET_MESSAGE + "/" + id + "?endTimestamp=" + timestamp + "&timestamp=0&count=" + Integer.MAX_VALUE;
        }

    }
}
