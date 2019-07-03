package com.byt.eemblue;

import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.byt.eemblue.base.ActPhotoGallery;
import com.byt.eemblue.base.BaseAct;
import com.byt.eemblue.util.GlideUtil;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.RxImgPath;
import com.souja.lib.utils.LibConstants;
import com.souja.lib.utils.MTool;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class TestUpload extends BaseAct {


    @Override
    protected int setupViewRes() {
        return R.layout.act_test_upload;
    }

    private String path;
    private ImageView imageView;

    @Override
    protected void initMain() {
        imageView = findViewById(R.id.iv_test);
        findViewById(R.id.btn_test).setOnClickListener(v -> choosePhoto());
        findViewById(R.id.btn_upload).setOnClickListener(v -> doUpload());
    }


    private void doUpload() {

        Param mParams = new Param();
        mParams.base64File = "data:image/png;base64," + imageToBase64(path);
        mParams.description = "head-icon";
        mParams.name = new File(path).getName();
        RequestParams requestParams = formatParams(mParams.toString());
        requestParams.setUri("http://47.93.246.70:8082/simple/docs/document/iconPicUpload");

      /*  RequestParams params = new RequestParams("http://47.93.246.70:8082/simple/docs/document/iconUpload");
        params.setMultipart(true);
        File file = new File(path);
        params.addQueryStringParameter("name","testname.jpg");
        params.addQueryStringParameter("description","test desc");
        params.addQueryStringParameter("files",file.getAbsolutePath());
        params.addBodyParameter("file", file, null);*/
//        x.http().request(HttpMethod.POST, params, new Callback.ProgressCallback<String>() {
        x.http().request(HttpMethod.POST, requestParams, new Callback.ProgressCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.e("suc request:" + result);
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("err in request:" + ex.getMessage());
            }

            @Override
            public void onStarted() {
                LogUtil.e("starting request...");
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtil.e("loading request " + total + "-" + current + "-" + isDownloading);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("finished request");
            }
        });

    }

    public static RequestParams formatParams(String paramJStr) {
        RequestParams paramJson = new RequestParams();
//        paramJson.setAsJsonContent(true);
        if (!MTool.isEmpty(paramJStr)) {
            LogUtil.e("===Request params===" + paramJStr);
            paramJson.setBodyContent(paramJStr);
        }
        paramJson.addHeader("Content-Type", "application/json");
        return paramJson;
    }

    private void choosePhoto() {
        Consumer onChosen = (Consumer<RxImgPath>) model -> {
            model.mActivity.finish();
            if (model.pathList.size() > 0) {
                path = model.pathList.get(0);
                GlideUtil.load(_this, path, imageView);
            }
            delAction(LibConstants.RX_CHOOSE_PHOTO);
        };
        addAction(LibConstants.RX_CHOOSE_PHOTO, onChosen);
        ActPhotoGallery.open(_this, 1, new ArrayList<>());
    }

    class Param extends BaseModel {

        /**
         * base64File : string
         * description : string
         * name : string
         */

        private String base64File;
        private String description;
        private String name;

        public String getBase64File() {
            return base64File;
        }

        public void setBase64File(String base64File) {
            this.base64File = base64File;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            FileReader reader = new FileReader(new File(path));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }
}
