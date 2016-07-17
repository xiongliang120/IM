package com.example.user.http.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.URLUtil;

import com.example.user.http.net.Request;
import com.example.user.http.exception.AppException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/26..
 * ClassName  :
 * Description  :
 */
public class HttpURLConnectionUtil {
    //如果对返回的是文件，保存在本地,返回保存路径.  如果返回图片, 则应该返回其Bitmap, 如果是返回json,则应该解析成相应的实体类。
    public static HttpURLConnection execute(Request request) throws AppException {
        if (!URLUtil.isNetworkUrl(request.getUrl())) {
            throw new AppException(AppException.ExceptionType.SERVER, "url  is invalid");
        }
        switch (request.getMethod()) {
            case GET:
            case DELETE:
                return get(request);
            case POST:
            case PUT:
                return post(request);
            default:
                break;

        }
        return null;
    }

    private static HttpURLConnection post(Request request) throws AppException {
        HttpURLConnection connection = null;
        try {
            request.cancleRequest();
            connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod(request.getMethod().name());
            setHeaders(connection, request.getHeadParams()); //设置头文件
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(request.getContent().getBytes());
            request.cancleRequest();
        } catch (InterruptedIOException exception) {
            throw new AppException(AppException.ExceptionType.TIME_OUT, exception.getMessage());
        } catch (IOException e) {
            throw new AppException(AppException.ExceptionType.SERVER, e.getMessage());
        }
        return connection;
    }


    private static HttpURLConnection get(Request request) throws AppException {
        HttpURLConnection connection = null;
        try {
            request.cancleRequest();
            connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            connection.setConnectTimeout(5000);
            setHeaders(connection, request.getHeadParams());
            connection.setRequestMethod(request.getMethod().name());
            request.cancleRequest();
        } catch (InterruptedIOException exception) {
            throw new AppException(AppException.ExceptionType.TIME_OUT, exception.getMessage());
        } catch (IOException e) {
            throw new AppException(AppException.ExceptionType.SERVER, e.getMessage());
        }
        return connection;
    }


    public static void setHeaders(HttpURLConnection connection, Map<String, String> headMap) {
        if (headMap == null || headMap.size() <= 0) {
            return;
        }
        for (Map.Entry<String, String> enter : headMap.entrySet()) {
            connection.setRequestProperty(enter.getKey(), enter.getValue());
        }
    }

    /**
     * 将输入流转化为Bitmap
     *
     * @param in
     * @return
     */
    private static Bitmap translateToBitmap(InputStream in) {
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return bitmap;
    }


}
