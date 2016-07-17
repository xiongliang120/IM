
package com.example.user.http.callback;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Scanner;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/28..
 * ClassName  :
 * Description  :
 */
public abstract class FileCallBack extends AbstractCallBack<String> {
    /**
     * 返回缓存的路径
     *
     * @param result
     * @return
     * @throws Exception
     */
    @Override
    public String bindData(String result) throws Exception {
        //TODO  对网络文件进行下载
        return result;
    }

    /**
     * 从网络上下载文件,返回本地存储路径
     *
     * @param connection
     * @return
     * @throws Exception
     */
    public String downFile(HttpURLConnection connection) throws Exception {
        if (connection.getResponseCode() == 200) {
            FileOutputStream outputStream = null;
            InputStream inputStream = connection.getInputStream();
            String path = Environment.getExternalStorageDirectory() + "/down";
            File filePath = null;
            File file = null;
            filePath = new File(path);
            if (!filePath.exists()) {  //先创建文件夹
                filePath.mkdirs();
            }
            file = new File(filePath, "aaa.txt");
            file.createNewFile();  //在创建文件
            outputStream = new FileOutputStream(file);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext()) { //向文件中写入数据
                outputStream.write(scanner.next().getBytes());
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            return file.toString();
        }
        return null;
    }
}
