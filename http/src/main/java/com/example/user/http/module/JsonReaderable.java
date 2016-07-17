package com.example.user.http.module;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/6/5..
 * ClassName  :
 * Description  :
 */
public interface JsonReaderable {
    public void readFromJson(JsonReader jsonReader) throws IOException;
}
