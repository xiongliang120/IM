package com.example.user.http.net;

import com.example.user.http.callback.ICallBack;
import com.example.user.http.exception.AppException;
import com.example.user.http.util.HttpURLConnectionUtil;

import java.net.HttpURLConnection;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/29..
 * ClassName  :
 * Description  :
 */
public class NetWorkRunnable implements Runnable {
    public Request request;

    @Override
    public void run() {
        request(0);
    }

    public void request(int retry) {
        ICallBack callBack = request.getiCallBack();
        try {
            Object o = callBack.preRequest();
            if (o != null) {
                //直接返回数据
                callBack.onSuccess(o);
                return;
            }


            HttpURLConnection connection = HttpURLConnectionUtil.execute(request);
            if (request.isEnableProgessUpdate()) {
                callBack.onSuccess(callBack.parse(connection, request.isEnableProgessUpdate()));
            } else {
                callBack.onSuccess(callBack.parse(connection));
            }
        } catch (AppException e) {
            if (e.exceptionType == AppException.ExceptionType.TIME_OUT) {
                retry++;
                if (retry < request.mMaxRetryCount) {
                    request(retry);
                }
            }

            //对于全局异常交给全局去处理
            if (request.onGlobalExceptionListener != null && !request.onGlobalExceptionListener.handleGlobalException(e)) {
                callBack.onFail(e);
            }
        }
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}

