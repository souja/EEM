package com.byt.eem.base;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.byt.eem.util.HttpUtil;
import com.souja.lib.base.ActBaseHor;
import com.souja.lib.inter.IHttpCallBack;

import org.xutils.http.RequestParams;


public abstract class BaseActHor extends ActBaseHor {

    public BaseActHor _this;

    @Override
    protected void handleOnCreate(Bundle savedInstanceState) {
        _this = this;
    }

    public <T> void Post(ProgressDialog dialog, String url, RequestParams params,
                         final Class<T> dataClass, IHttpCallBack callBack) {
        addRequest(HttpUtil.Post(dialog, url, params, dataClass, callBack));
    }

    public <T> void Post(String url, final Class<T> dataClass, IHttpCallBack callBack) {
        Post(null, url, new RequestParams(), dataClass, callBack);
    }

    public <T> void Post(String url, RequestParams params, final Class<T> dataClass, IHttpCallBack callBack) {
        Post(null, url, params, dataClass, callBack);
    }

    public <T> void Post(ProgressDialog dialog, String url,
                         final Class<T> dataClass, IHttpCallBack callBack) {
        Post(dialog, url, new RequestParams(), dataClass, callBack);
    }

    public <T> void Get(ProgressDialog dialog, String url, RequestParams params,
                        final Class<T> dataClass, IHttpCallBack callBack) {
        addRequest(HttpUtil.Get(dialog, url, params, dataClass, callBack));
    }

    public <T> void Delete(ProgressDialog dialog, String url, RequestParams params,
                           final Class<T> dataClass, IHttpCallBack callBack) {
        addRequest(HttpUtil.Delete(dialog, url, params, dataClass, callBack));
    }
}
