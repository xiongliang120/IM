package com.example.user.http.exception;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/29..
 * ClassName  :
 * Description  :
 */
public interface OnGlobalExceptionListener {
    public boolean handleGlobalException(AppException e);
}
