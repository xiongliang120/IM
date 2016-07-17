package com.example.user.http.net;

import com.example.user.http.callback.ICallBack;
import com.example.user.http.exception.AppException;
import com.example.user.http.exception.OnGlobalExceptionListener;

import java.util.Map;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/26..
 * ClassName  :
 * Description  :
 */
public class Request {
    public boolean isCancle;
    private String tag;

    public void setCancle(boolean isCancle) {
        this.isCancle = isCancle;
        iCallBack.setCancle(isCancle);
    }

    public void cancleRequest() throws AppException {
        if (this.isCancle) {
            throw new AppException(AppException.ExceptionType.CANCLE, "the request is cancle");
        }
    }

    public int mMaxRetryCount = 3;
    public ICallBack iCallBack;
    public OnGlobalExceptionListener onGlobalExceptionListener;

    public void setOnGlobalExceptionListener(OnGlobalExceptionListener onGlobalExceptionListener) {
        this.onGlobalExceptionListener = onGlobalExceptionListener;
    }

    public boolean enableProgessUpdate = false;

    public boolean isEnableProgessUpdate() {
        return enableProgessUpdate;
    }

    public void setEnableProgessUpdate(boolean enableProgessUpdate) {
        this.enableProgessUpdate = enableProgessUpdate;
    }

    public ICallBack getiCallBack() {
        return iCallBack;
    }

    public void setiCallBack(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public enum Method {GET, POST, PUT, DELETE}

    ;
    public String url;
    public String content;
    public Map<String, String> headParams;
    public Method method;

    public Request(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeadParams() {
        return headParams;
    }

    public void setHeadParams(Map<String, String> headParams) {
        this.headParams = headParams;
    }

    public Method getMethod() {
        return method == null ? Method.GET : method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
