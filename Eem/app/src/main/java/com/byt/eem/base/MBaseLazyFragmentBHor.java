package com.byt.eem.base;

import android.app.ProgressDialog;

import com.byt.eem.util.HttpUtil;
import com.souja.lib.base.BaseLazyFragmentB;
import com.souja.lib.inter.IHttpCallBack;

import org.xutils.http.RequestParams;

/**
 * ClassName
 * Created by Ydz on 2019/3/21 0021 15:54
 */
public abstract class MBaseLazyFragmentBHor extends BaseLazyFragmentB {

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
