package com.example.user.http.callback;

import android.util.Log;

import com.example.user.http.exception.AppException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/28..
 * ClassName  :
 * Description  :
 */
public abstract class AbstractCallBack<T> implements ICallBack<T> {
    private String cachePath;
    public boolean isCancle;

    public void setCancle(boolean isCancle) {
        this.isCancle = isCancle;
    }

    public void cancleRequest() throws AppException {
        if (this.isCancle) {
            throw new AppException(AppException.ExceptionType.CANCLE, "the request is cancle");
        }
    }

    @Override
    public T parse(HttpURLConnection connection, boolean enableProgressUpdate) throws AppException {

        try {
            String result = null;
            connection.setConnectTimeout(50000);
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) { //各个服务器返回成功的值不一定是200
                if (cachePath == null) {
                    cancleRequest();
                    //存储在内存中,针对于直接解析
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    InputStream inputSteam = connection.getInputStream();
                    int length = 0;
                    byte a[] = new byte[2048];
                    while ((length = inputSteam.read(a)) != -1) {
                        cancleRequest();
                        outputStream.write(a);

                    }
                    inputSteam.close();
                    outputStream.flush();
                    outputStream.close();
                    result = outputStream.toString();
                    Log.i("msg", "result = " + result);

                } else {
                    //存储到文件中
                    cancleRequest();
                    InputStream inputSteam = connection.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(cachePath); //创建带文件夹的文件时会创建不了
                    Scanner scanner = new Scanner(inputSteam);
                    int totalProgress = connection.getContentLength();
                    int currentProgress = 0;
                    byte a[] = new byte[2048];
                    int length = 0;

                    while ((length = inputSteam.read(a)) != -1) {
                        cancleRequest();
                        outputStream.write(a);
                        if (enableProgressUpdate) {
                            currentProgress = currentProgress + length;
                            //将进度值回传给主线程
                            onProgressUpdate(currentProgress, totalProgress);
                        }
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputSteam.close();
                    result = cachePath;
                }
                T t = bindData(result);
                return postRequest(t);
            } else {
                String codeMessage = connection.getResponseMessage();
                throw new AppException(statusCode, codeMessage);
            }
        } catch (InterruptedIOException exception) {
            throw new AppException(AppException.ExceptionType.TIME_OUT, exception.getMessage());
        } catch (Exception e) {
            if (e instanceof AppException) {
                throw new AppException(((AppException) e).statusCode, ((AppException) e).errorMessage);
                //直接throw e即可
            } else {
                String message = e.getMessage();
                throw new AppException(AppException.ExceptionType.SERVER, e.getMessage());
            }


        }

    }

    public T postRequest(T t) {
        return t;
    }

    public T preRequest() {
        return null;
    }

    @Override
    public T parse(HttpURLConnection connection) throws AppException {
        return parse(connection, false);
    }


    public void onProgressUpdate(int currentProress, int totalProgress) {

    }

    public abstract T bindData(String result) throws Exception;


    /**
     * 从输入流中读取字符串
     *
     * @param in
     * @return
     */
    private String readInStream(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.next()).append("\n");
        }
        return stringBuilder.toString();
    }

    public ICallBack setCachePath(String cachePath) {
        this.cachePath = cachePath;
        return this;
    }
}
