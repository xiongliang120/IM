package com.example.user.http.callback;

import com.example.user.http.exception.AppException;

import java.net.HttpURLConnection;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/28..
 * ClassName  :
 * Description  :
 */
public interface ICallBack<T> {
    public void setCancle(boolean isCancle);

    public void onSuccess(T t);

    public void onFail(AppException e);

    public T parse(HttpURLConnection connection) throws AppException;

    public T parse(HttpURLConnection connection, boolean enableProgressUpdate) throws AppException;

    /**
     * 请求http后,对获取的数据进行存储,排序等操作
     *
     * @param t
     * @return
     */
    public T postRequest(T t);

    /**
     * 在请求http前,先从数据库等缓存中获取数据展示, 不论缓存是否为null,都需要再次请求网络数据
     *
     * @return
     */
    public T preRequest();
}
