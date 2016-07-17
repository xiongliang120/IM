package com.example.user.http.exception;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/29..
 * ClassName  :
 * Description  :
 */
public class AppException extends Exception {
    public int statusCode;
    public String errorMessage;

    public enum ExceptionType {CANCLE, TIME_OUT, SERVER, JSON}

    ;
    public ExceptionType exceptionType;

    public AppException(int statusCode, String errorMessage) {
        this.exceptionType = ExceptionType.TIME_OUT;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public AppException(ExceptionType exceptionType, String detailMessage) {
        super(detailMessage);
        this.exceptionType = exceptionType;
        this.errorMessage = detailMessage;
    }
}
