package com.example.user.http.callback;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/28..
 * ClassName  :
 * Description  :
 */
public abstract class JSONCallBack<T> extends AbstractCallBack<T> {

    //  {"ret":200,"data":{"name":0,"timestamp":0}
    @Override
    public T bindData(String result) throws Exception {
        if (result == null) {
            return null;
        }

        //TODO  根据泛型判断是否是数组，然后采用数组解析，或是采用对象进行解析
        Log.i("Tag", result);
        //解析json 二维数组
        JSONObject jsonObject = new JSONObject(result);
        Log.i("Tag", jsonObject.toString());
        //Object object = jsonObject.opt("data");
        Gson gson = new Gson();
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<T> entityClass = (Class) params[0];
        T t = (T) gson.fromJson(jsonObject.toString(), entityClass);
        return t;
    }
}
