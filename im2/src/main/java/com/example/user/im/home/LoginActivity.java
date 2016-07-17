package com.example.user.im.home;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.http.callback.JSONCallBack;
import com.example.user.http.exception.AppException;
import com.example.user.http.net.NetWorkRunnable;
import com.example.user.http.net.Request;
import com.example.user.http.util.ThreadPool;
import com.example.user.im.IMApplication;
import com.example.user.im.R;
import com.example.user.im.entity.Profile;
import com.example.user.im.util.Contans;
import com.example.user.im.util.PrefAccesor;
import com.example.user.im.util.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/13..
 * ClassName  :
 * Description  :
 */
public class LoginActivity extends BaseActivity {
    private EditText name;
    private EditText psw;
    private Button sure;
    private NetWorkRunnable netWorkRunnable;
    private ThreadPool threadPool = new ThreadPool();

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initView() {
        name = (EditText) findViewById(R.id.name);
        psw = (EditText) findViewById(R.id.password);
        sure = (Button) findViewById(R.id.sure);

        name.setText(PrefAccesor.getInstance(this).getString(Contans.KEY_ACCOUNT));
        psw.setText(PrefAccesor.getInstance(this).getString(Contans.KEY_PASSWORD));

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = name.getText().toString();
                String pswString = psw.getText().toString();
                doLogin(nameString, pswString);
            }
        });
    }

    private void doLogin(final String nameString, final String pswString) {
        final String cachePath = Environment.getExternalStorageDirectory() + File.separator + "cache.txt";
        final String url = UrlHelper.loadLogin();
        JSONObject json = new JSONObject();
        try {
            json.put("account", nameString);
            json.put("password", pswString);
            json.put("clientId", "android");
            json.put("clientVersion", "1.0.0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request(url);
        request.setContent(json.toString());
        request.setiCallBack(new JSONCallBack<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                PrefAccesor.getInstance(LoginActivity.this).saveString(Contans.KEY_ACCOUNT, nameString);
                PrefAccesor.getInstance(LoginActivity.this).saveString(Contans.KEY_PASSWORD, pswString);
                //存储用户信息
                IMApplication.setProfile(profile);
                goHome();
                Log.i("msg", "打印的信息=" + profile.toString());

            }

            @Override
            public void onFail(AppException e) {

            }
        });


        request.setOnGlobalExceptionListener(this);
        request.setMethod(Request.Method.POST);
        Map<String, String> head = new HashMap<String, String>();
        head.put("Accept", "application/json");
        head.put("content-type", "application/json"); //这行必须得加
        request.setHeadParams(head);

        netWorkRunnable = new NetWorkRunnable();
        netWorkRunnable.setRequest(request);
        threadPool.execute(netWorkRunnable);
    }

    private void goHome() {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

    @Override
    public void initData() {

    }


}
