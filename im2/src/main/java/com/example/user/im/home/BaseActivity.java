package com.example.user.im.home;

import android.app.Activity;
import android.os.Bundle;

import com.example.user.http.exception.AppException;
import com.example.user.http.exception.OnGlobalExceptionListener;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/13..
 * ClassName  :
 * Description  :
 */
public abstract class BaseActivity extends Activity implements OnGlobalExceptionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initView();
        initData();
    }

    public abstract void setContentView();

    public abstract void initView();

    public abstract void initData();

    @Override
    public boolean handleGlobalException(AppException e) {
        return false;
    }
}
