package com.example.user.http.callback;

import android.app.Activity;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/13..
 * ClassName  :
 * Description  :
 */
public abstract class StringCallBack extends AbstractCallBack<String> {
    @Override
    public String bindData(String result) throws Exception {
        return result;
    }
}
