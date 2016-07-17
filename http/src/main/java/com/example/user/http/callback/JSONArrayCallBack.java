package com.example.user.http.callback;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
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
public abstract class JSONArrayCallBack<T> extends AbstractCallBack<ArrayList<T>> {

    @Override
    public ArrayList<T> bindData(String result) throws Exception {
        if (result == null) {
            return null;
        }
        Log.i("msg", result);
        //TODO  根据泛型判断是否是数组，然后采用数组解析，或是采用对象进行解析

        //解析json 二维数组
//        JSONObject jsonObject = new JSONObject(result);
//        JsonParser parser = new JsonParser();
//        JsonArray  array = parser.parse(jsonObject.toString()).getAsJsonArray();
        JSONArray array = new JSONArray(result);
        ArrayList<T> arrayList = new ArrayList<T>();

        Gson gson = new Gson();
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<T> entityClass = (Class) params[0];

        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = (JSONObject) array.get(i);
            T t = gson.fromJson(jsonObject.toString(), entityClass);
            arrayList.add(t);
        }

        return arrayList;
    }


}
