package com.example.user.http.net;

import com.example.user.http.util.ThreadPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/6/2..
 * ClassName  :
 * Description  :
 */
public class RequestManager {
    HashMap<String, List<Request>> requestMap = new HashMap<String, List<Request>>();
    ThreadPool threadPool = new ThreadPool();
    NetWorkRunnable netWorkRunnable;
    //到时制作成模板代码
    private static RequestManager requestManager;

    public static RequestManager getInstance() {
        if (requestManager == null) {
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    /**
     * 添加Request请求
     *
     * @param tag
     * @param request
     */
    public void addRequest(String tag, Request request) {
        List<Request> requests = requestMap.get(tag);
        if (requests == null) {
            requests = new ArrayList<Request>();
            requestMap.put(tag, requests);

        }
        requests.add(request);
    }

    /**
     * 删除跟某个activity有关的所有请求
     *
     * @param tag
     */
    public void deleteRequest(String tag) {
        List<Request> requests = requestMap.remove(tag);
        for (int i = 0; i < requests.size(); i++) {
            Request request = requests.get(i);
            request.setCancle(true); //将请求删除
        }
    }

    /**
     * 删除所有请求
     */
    public void deleteAllRet() {
        requestMap.keySet();
        for (Map.Entry<String, List<Request>> sets : requestMap.entrySet()) {
            List<Request> requests = sets.getValue();
            for (int i = 0; i < requests.size(); i++) {
                requests.get(i).setCancle(true);
            }
        }
        requestMap.clear();
        threadPool.closePool(); //即使有再多的等待线程,也不会再执行的。
    }

    /**
     * 删除某一个Request请求
     *
     * @param tag
     * @param request
     */
    public void deleteOneRequest(String tag, Request request) {
        for (Map.Entry<String, List<Request>> sets : requestMap.entrySet()) {
            List<Request> requests = sets.getValue();
            if (requests != null) {
                requests.remove(request);
                request.setCancle(true);
            }
        }
    }

    /**
     * 执行Request请求
     *
     * @param tag
     * @param request
     */
    public void executeRequest(String tag, Request request) {
        addRequest(tag, request);
        netWorkRunnable = new NetWorkRunnable();
        netWorkRunnable.setRequest(request);
        threadPool.execute(netWorkRunnable);
    }

}
