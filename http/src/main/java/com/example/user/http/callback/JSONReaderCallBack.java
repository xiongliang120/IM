package com.example.user.http.callback;

import com.example.user.http.module.JsonReaderable;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/28..
 * ClassName  :
 * Description  : 当json数据比较大时，GSON就会崩溃，这是采用JsonReader进行手动解析
 */
public abstract class JSONReaderCallBack<T extends JsonReaderable> extends AbstractCallBack<T> {

    //  {"ret":200,"data":[{"name":0,"timestamp":0},{"name":1,"timestamp":60000},{"name":2,"timestamp":120000},{"name":3,"timestamp":180000},{"name":4,"timestamp":240000},{"name":5,"timestamp":300000},{"name":6,"timestamp":360000},{"name":7,"timestamp":420000},{"name":8,"timestamp":480000},{"name":9,"timestamp":540000},{"name":10,"timestamp":600000},{"name":11,"timestamp":660000},{"name":12,"timestamp":720000},{"name":13,"timestamp":780000},{"name":14,"timestamp":840000},{"name":15,"timestamp":900000},{"name":16,"timestamp":960000},{"name":17,"timestamp":1020000},{"name":18,"timestamp":1080000},{"name":19,"timestamp":1140000},{"name":20,"timestamp":1200000},{"name":21,"timestamp":1260000},{"name":22,"timestamp":1320000},{"name":23,"timestamp":1380000},{"name":24,"timestamp":1440000},{"name":25,"timestamp":1500000},{"name":26,"timestamp":1560000},{"name":27,"timestamp":1620000},{"name":28,"timestamp":1680000},{"name":29,"timestamp":1740000},{"name":30,"timestamp":1800000}],"msg":""}����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������

    @Override
    public T bindData(String result) throws Exception {
        if (result == null) {
            return null;
        }

        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<T> entityClass = (Class) params[0];

        T t = entityClass.newInstance();

        FileReader fileReader = new FileReader(result);
        JsonReader jsonReader = new JsonReader(fileReader);
        jsonReader.beginObject();
        String node;
        while (jsonReader.hasNext()) {
            node = jsonReader.nextName();
            if ("data".equalsIgnoreCase(node)) {
                t.readFromJson(jsonReader);
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return t;
    }

//    /**
//     * 从网络上下载文件,返回本地存储路径
//     * @param connection
//     * @return
//     * @throws Exception
//     */
//    public String downFile(HttpURLConnection connection) throws  Exception{
//        if (connection.getResponseCode() == 200) {
//            FileOutputStream outputStream = null;
//            InputStream inputStream = connection.getInputStream();
//            String path = Environment.getExternalStorageDirectory() + "/down";
//            File filePath = null;
//            File file = null;
//            filePath = new File(path);
//            if (!filePath.exists()) {  //先创建文件夹
//                filePath.mkdirs();
//            }
//            file = new File(filePath, "aaa.txt");
//            file.createNewFile();  //在创建文件
//            outputStream = new FileOutputStream(file);
//            Scanner scanner = new Scanner(inputStream);
//            while (scanner.hasNext()) { //向文件中写入数据
//                outputStream.write(scanner.next().getBytes());
//            }
//            if (outputStream != null) {
//                    outputStream.flush();
//                    outputStream.close();
//            }
//            return file.toString();
//        }
//        return null;
//    }
}
