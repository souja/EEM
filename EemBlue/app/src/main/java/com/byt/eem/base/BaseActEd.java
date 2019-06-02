package com.byt.eem.base;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.byt.eem.util.HttpUtil;
import com.souja.lib.base.ActBaseEd;
import com.souja.lib.inter.IHttpCallBack;

import org.xutils.http.RequestParams;


public abstract class BaseActEd extends ActBaseEd {

    public BaseActEd _this;

    @Override
    protected void handleOnCreate(Bundle savedInstanceState) {
        _this = this;
    }

    public <T> void Post(AlertDialog dialog, String url, RequestParams params,
                         final Class<T> dataClass, IHttpCallBack callBack) {
        addRequest(HttpUtil.Post(dialog, url, params, dataClass, callBack));
    }

    public <T> void Post(String url, final Class<T> dataClass, IHttpCallBack callBack) {
        Post(null, url, new RequestParams(), dataClass, callBack);
    }

    public <T> void Post(String url, RequestParams params, final Class<T> dataClass, IHttpCallBack callBack) {
        Post(null, url, params, dataClass, callBack);
    }

    public <T> void Post(AlertDialog dialog, String url,
                         final Class<T> dataClass, IHttpCallBack callBack) {
        Post(dialog, url, new RequestParams(), dataClass, callBack);
    }

    public void Post(AlertDialog dialog, String url, RequestParams params, IHttpCallBack<Object> callBack) {
        addRequest(HttpUtil.Post(dialog, url, params, Object.class, callBack));
    }

    public <T> void Get(AlertDialog dialog, String url, RequestParams params,
                        final Class<T> dataClass, IHttpCallBack callBack) {
        addRequest(HttpUtil.Get(dialog, url, params, dataClass, callBack));
    }

    public <T> void Delete(AlertDialog dialog, String url, RequestParams params,
                           final Class<T> dataClass, IHttpCallBack callBack) {
        addRequest(HttpUtil.Delete(dialog, url, params, dataClass, callBack));
    }
}
