package com.example.user.http.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/27..
 * ClassName  :
 * Description  :
 */
public class ThreadPool {
    //创建
    public ExecutorService executorService;

    public ThreadPool() {
        executorService = Executors.newFixedThreadPool(5);
    }

    public ThreadPool(int length) {
        executorService = Executors.newFixedThreadPool(length);
    }

    public void execute(Runnable runnable) {
        if (executorService != null) {
            executorService.execute(runnable);
        }
    }

    public void closePool() {
        if (executorService != null) {
            executorService.shutdown();
        }
        executorService = null;
    }
}
