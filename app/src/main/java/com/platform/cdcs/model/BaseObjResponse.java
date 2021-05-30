package com.platform.cdcs.model;

/**
 * Created by holytang on 2017/10/11.
 */
public class BaseObjResponse<T> {

    private T result;
    private String apptoken;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getApptoken() {
        return apptoken;
    }

    public void setApptoken(String apptoken) {
        this.apptoken = apptoken;
    }
}
