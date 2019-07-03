package com.byt.eemblue.util;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.byt.eemblue.EApp;
import com.souja.lib.enums.EnumExceptions;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.utils.GsonUtil;
import com.souja.lib.utils.MTool;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Souja on 2018/3/6 0006.
 */

public class HttpUtil {


    public static String formatUrl(String url) {
        if (url.contains("http:") || url.contains("https:")) return url;
        return MConstants.HTTP + url;
    }

    private static void addIdentify(RequestParams params) {
        if (EApp.getUserInfo() != null) {
            int id = EApp.getUserInfo().getId();
            params.setHeader("id", id + "");
        }
        params.setHeader("Auth", "HfHm7jBjmlDCrUm7xJFr8GDOWHCGE6ykOWzbEvrRgFvvEU1ziWHZ1hvJ2A9pTJbK");
    }

    private static String getErrMsgStr(String errStr) {
        if (errStr.contains("ConnectException") || errStr.contains("NoRouteToHostException")) {
            return EnumExceptions.SERVER_FAILED.getDesc();
        } else if (errStr.contains("Software caused connection abort")) {
            return EnumExceptions.NO_INTERNET.getDesc();
        } else if (errStr.contains("SocketTimeoutException")) {
            return EnumExceptions.SERVER_TIMEOUT.getDesc();
        } else if (errStr.contains("UnknownHostException") || errStr.contains("EOFException")) {
            return EnumExceptions.NO_INTERNET_A.getDesc();
        } else if (errStr.contains("502") || errStr.contains("404") || errStr.contains("NullPointerException")) {
            return EnumExceptions.BAD_GET_AWAY.getDesc();
        } else
            return errStr;
    }

    public static RequestParams formatParams(String paramJStr) {
        RequestParams paramJson = new RequestParams();
        if (!MTool.isEmpty(paramJStr)) {
            String finalStr = "\"" + paramJStr.replace("\"", "\\\"") + "\"";
            LogUtil.e("===Request params===" + paramJStr);
            LogUtil.e("===Request params===" + finalStr);
            paramJson.setBodyContent(finalStr);
        }
        paramJson.addHeader("Content-Type", "application/json");
        return paramJson;
    }

    public static RequestParams defaultParam(String key, String value) {
        RequestParams paramJson = new RequestParams();
        paramJson.addQueryStringParameter(key, value);
        paramJson.addHeader("Content-Type", "application/json");
        return paramJson;
    }

    public static RequestParams defaultParam() {
        RequestParams paramJson = new RequestParams();
        paramJson.addHeader("Content-Type", "application/json");
        return paramJson;
    }

    private static <T> void handleOnRequestSuccess(String result, RequestParams params,
                                                   final Class<T> dataClass, IHttpCallBack<T> callBack) {
        LogUtil.e("===" + params.getUri() + "===\nresponse===>>>" + result);
        if (result == null) {
            callBack.OnFailure("服务器异常");
        }
        Result resultObj = (Result) GsonUtil.getObj(result, Result.class);
        boolean success = resultObj.isSucess;
        String msg = resultObj.msg;
        if (success) {
            ODataPage pageObj = new ODataPage();
            String dataString = resultObj.dataJsonStr;
            if (TextUtils.isEmpty(dataString)) {
                callBack.OnSuccess("数据为空", pageObj, new ArrayList<>());
                return;
            }
            String formatDataStr = dataString.replace("\\\"", "'");
            if (params.getUri().contains(MConstants.URL.GET_DEVICES_STATE_BY_DEVICEID)
                    || params.getUri().contains(MConstants.URL.CHECK_UPDATE)) {
                ArrayList<T> list = new ArrayList<>();
                T r = (T) GsonUtil.getObj(formatDataStr, dataClass);
                list.add(r);
                callBack.OnSuccess(msg, pageObj, list);
            } else
                callBack.OnSuccess(msg, pageObj, GsonUtil.getArr(formatDataStr, dataClass));

        } else {
            callBack.OnFailure(msg == null ? "抱歉，服务器开小差了~" : msg);
        }

    }

    private static void handleOnRequestErr(AlertDialog dialog, RequestParams params, Throwable ex, IHttpCallBack callBack) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();

        String errStr = ex.toString();
        LogUtil.e("===" + params.getUri() + "===\n===>>>onError:" + errStr);
        callBack.OnFailure(getErrMsgStr(errStr));
    }

    public static <T> Callback.Cancelable Post(AlertDialog dialog, String url, RequestParams mParams,
                                               final Class<T> dataClass, IHttpCallBack<T> callBack) {
        return Request(dialog, url, HttpMethod.POST, mParams, dataClass, callBack);
    }

    public static <T> Callback.Cancelable Get(AlertDialog dialog, String url, RequestParams mParams,
                                              final Class<T> dataClass, IHttpCallBack<T> callBack) {
        return Request(dialog, url, HttpMethod.GET, mParams, dataClass, callBack);
    }

    public static <T> Callback.Cancelable Delete(AlertDialog dialog, String url, RequestParams mParams,
                                                 final Class<T> dataClass, IHttpCallBack<T> callBack) {
        return Request(dialog, url, HttpMethod.DELETE, mParams, dataClass, callBack);
    }

    public static <T> Callback.Cancelable Request(AlertDialog dialog, String url, HttpMethod method, RequestParams mParams,
                                                  final Class<T> dataClass, IHttpCallBack<T> callBack) {
        if (!NetWorkUtils.isNetworkAvailable()) {
            callBack.OnFailure(EnumExceptions.NO_INTERNET.getDesc());
            return null;
        }
        String finalUrl = formatUrl(url);
        LogUtil.e("===Post to===" + finalUrl);
        mParams.setUri(finalUrl);
        mParams.setHeader("Content-Type", "application/json");

        addIdentify(mParams);

        return x.http().request(method, mParams, new Callback.ProgressCallback<String>() {

            @Override
            public void onSuccess(String result) {
                handleOnRequestSuccess(result, mParams, dataClass, callBack);
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handleOnRequestErr(dialog, mParams, ex, callBack);
            }

            @Override
            public void onStarted() {
                if (dialog != null) dialog.show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
    }

/*
    public static <T> Callback.Cancelable PostMult(AlertDialog dialog, RequestParams mParams,
                                                   final Class<T> dataClass, IHttpCallBack callBack) {
        if (!NetWorkUtils.isNetworkAvailable()) {
            callBack.OnFailure(EnumExceptions.NO_INTERNET.getDesc());
            return null;
        }
        String url = mParams.getUri();
        if (url.contains("/image/"))
            mParams.addBodyParameter("businessCode", "1001-01");
        LogUtil.e("===Post To===" + url);
        addIdentify(mParams);

        LogUtil.e("===Request Params===" + mParams.toString());

        return x.http().request(HttpMethod.POST, mParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                handleOnRequestSuccess(result, mParams, dataClass, callBack);
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handleOnRequestErr(dialog, mParams, ex, callBack);
            }


            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
    }
*/


    class Result extends BaseModel {

        /**
         * isSucess : true
         * msg : null
         * dataJsonStr : [{"Id":12,"ParentId":0,"ParentTUserId":null,"PassWord":"1","UserId":"user","UserName":"体验用户","TRoleId":1,"Phone":null,"PlatFormName":"智慧消防平台","CompanyLogo":"/UpLoad/峰泰logo1副本.jpg","CompanyName":"成都峰泰电气有限公司","RoleName":"超级管理员"}]
         * outId : 0
         */

        public boolean isSucess;
        public String msg;
        public String dataJsonStr;
        public int outId;

    }
/*
    public static Callback.Cancelable UploadImage(AlertDialog dialog, RequestParams params, IUploadImageCallBack callBack) {
        if (!NetWorkUtils.isNetworkAvailable()) {
            callBack.OnFailure(EnumExceptions.NO_INTERNET.getDesc());
            return null;
        }
        addIdentify(params);

        LogUtil.e("params:" + params.toString());

        return x.http().post(params, new Callback.ProgressCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.e("===uploadImages back===\nresponse===>>>" + result);
                Result resultObj = (Result) GsonUtil.getObj(result, Result.class);
                int code = resultObj.code;
                String msg = resultObj.msg;

                if (code == M_HTTP_SUCCESS) {
                    if (resultObj.data != null) {
                        Gson gson = new Gson();
                        Integer[] array = gson.fromJson(resultObj.data.toString(), Integer[].class);
                        List<Integer> parseData = Arrays.asList(array);

                        callBack.OnUploadSuccess(msg, parseData.get(0));
                    } else
                        callBack.OnUploadSuccess(msg, -1);
                } else {
                    if (code == M_MULT_LOGIN) loginOutDate();
                    else callBack.OnFailure(msg == null ? "服务器异常" : msg);
                }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if (dialog != null) dialog.show();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                handleOnRequestErr(dialog,params,ex,callBack);
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                String errStr = ex.toString();
                LogUtil.e("===" + "upload imgs===\n===>>>onError:" + errStr);
                callBack.OnFailure(getErrMsgStr(errStr));
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
    }
*/

//    private static boolean isNull(JsonElement element) {
//        return element == null || element.isJsonNull();
//    }

//    private static void loginOutDate() {
//        if (ActMain.get() != null) {
//            ActMain.get().backToLogin(true);
//        }
//    }

  /*  public interface DownloadListener {
        void onSuc(File file);

        void onErr(Throwable ex);

        void onProgress(long totalProgress, long currentProgress);
    }*/

/*
    public static Callback.Cancelable downloadFile(String sourceUrl, String savePath, DownloadListener downloadListener) {
        RequestParams params = new RequestParams(sourceUrl);
        //设置保存数据
        LogUtil.e("save path :" + savePath);
        params.setSaveFilePath(savePath);
        //设置是否可以立即取消下载
        params.setCancelFast(true);
        //设置是否自动根据头信息命名
        params.setAutoRename(false);
        //设置断点续传
        params.setAutoResume(true);

        params.setExecutor(new PriorityExecutor(3, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
        params.setRedirectHandler(request -> {
            RequestParams requestParams = request.getParams();
            String location = request.getResponseHeader("Location"); //协定的重定向地址
            if (!TextUtils.isEmpty(location)) {
                requestParams.setUri(location); //重新设置url地址
            }
            return requestParams;
        });
        params.setMaxRetryCount(5);
        addIdentify(params);
        return x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File file) {
                downloadListener.onSuc(file);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                downloadListener.onErr(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                LogUtil.e("完成..." + sourceUrl);
            }

            @Override
            public void onWaiting() {
                LogUtil.e("等待中..." + sourceUrl);
            }

            @Override
            public void onStarted() {
                LogUtil.e("开始下载 ..." + sourceUrl);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtil.e("on loading " + current + "/" + total);
                if (isDownloading) {
                    downloadListener.onProgress(total, current);
                }
            }
        });
    }
*/

}
