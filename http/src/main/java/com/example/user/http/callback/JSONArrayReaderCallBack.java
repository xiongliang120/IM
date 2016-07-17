package com.example.user.http.callback;

import com.example.user.http.module.JsonReaderable;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/28..
 * ClassName  :
 * Description  : 当json数据比较大时，GSON就会崩溃，这是采用JsonReader进行手动解析
 */
public abstract class JSONArrayReaderCallBack<T extends JsonReaderable> extends AbstractCallBack<ArrayList<T>> {

    @Override
    public ArrayList<T> bindData(String result) throws Exception {
        if (result == null) {
            return null;
        }

        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<T> entityClass = (Class) params[0];

        T t;
        ArrayList<T> arrayList = new ArrayList<T>();
        FileReader fileReader = new FileReader(result);
        JsonReader jsonReader = new JsonReader(fileReader);
        String tagName;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            tagName = jsonReader.nextName();
            if ("data".equalsIgnoreCase(tagName)) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    t = entityClass.newInstance(); //重新获取对象
                    t.readFromJson(jsonReader);
                    arrayList.add(t);
                }
                jsonReader.endArray();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return arrayList;
    }
}
