
package com.example.user.http.callback;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/5/28..
 * ClassName  :
 * Description  :
 */
public abstract class XMLCallBack<T> extends AbstractCallBack<T> {
    /**
     * 解析xml文件
     *
     * @param result
     * @return
     * @throws Exception
     */
    @Override
    public T bindData(String result) throws Exception {
        //TODO  解析xml操作
        return null;
    }
}
