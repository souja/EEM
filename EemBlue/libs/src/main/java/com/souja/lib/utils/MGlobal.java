package com.souja.lib.utils;

import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;

import com.souja.lib.models.BaseModel;

import org.xutils.common.util.LogUtil;

import io.reactivex.functions.Consumer;

/**
 * Created by Souja on 2018/3/12 0012.
 */

public class MGlobal {

    private static MGlobal instance;

    public static MGlobal get() {
        if (instance == null) {
            synchronized (MGlobal.class) {
                if (instance == null) {
                    instance = new MGlobal();
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() {
        actionMap = new ArrayMap<>();
    }

    public static boolean bQmp;//是否全面屏（刘海屏）

    private final String KEY_SCREEN_PARAM = "windowScreenParams";

    private void initScreenParams(int w, int h, float de, int dp, boolean qmp) {
        deviceWidth = w;
        deviceHeight = h;
        density = de;
        dpi = dp;
        bQmp = qmp;
        LogUtil.e("[DeviceInfo]width=" + deviceWidth
                + ",height=" + deviceHeight + ",density=" + density + ",dpi=" + dpi);
        LogUtil.e(bQmp ? "全面/刘海屏手机" : "非 全面/刘海屏手机");
    }

    public void initScreenParam(DisplayMetrics dm) {
        this.deviceWidth = dm.widthPixels;
        this.deviceHeight = dm.heightPixels;
        this.density = dm.density;
        this.dpi = dm.densityDpi;
        LogUtil.e("mScale=" + (double) deviceWidth / 1080d);

        int sw = deviceWidth, sh = deviceHeight;
        bQmp = (sw == 1080 && sh > 1920) || (sw == 1440 && sh > 2560);
        initScreenParams(dm.widthPixels, dm.heightPixels, dm.density, dm.densityDpi, bQmp);
        SPHelper.putString(KEY_SCREEN_PARAM, new ScreenParam(deviceWidth, deviceHeight, dpi, density, bQmp ? 1 : 0).toString());
    }

    public boolean isInitializedScreenParams() {
        String screenParamStr = SPHelper.getString(KEY_SCREEN_PARAM);
        if (screenParamStr.isEmpty()) {
            return false;
        }
        ScreenParam param = (ScreenParam) GsonUtil.getObj(screenParamStr, ScreenParam.class);
        initScreenParams(param.width, param.height, param.density, param.densityDpi, param.bQmp == 1);
        return true;
    }

    private int deviceWidth, deviceHeight;
    private float density;
    private ArrayMap<Integer, Consumer<Object>> actionMap;
    private int dpi;

    //Rx functions======>>>
    public void addAction(int key, Consumer<Object> consumer) {
        actionMap.put(key, consumer);
    }

    public Consumer<Object> getAction(int key) {
        if (actionMap.containsKey(key))
            return actionMap.get(key);
        else return null;
    }

    public void delAction(int key) {
        if (actionMap.containsKey(key))
            actionMap.remove(key);
    }

    public boolean containsKey(int key) {
        return actionMap.containsKey(key);
    }

    public void clearActions() {
        if (actionMap == null) return;
        actionMap.clear();
    }

    private int keybordHeight;

    public void setKeybordHeight(int height) {
        keybordHeight = height;
    }

    public int getKeybordHeight() {
        return keybordHeight;
    }


    public int getDeviceWidth() {
        return deviceWidth;
    }

    public int getDeviceHeight() {
        return deviceHeight;
    }

    public float getDensity() {
        return density;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public int getDpi() {
        return dpi;
    }


    class ScreenParam extends BaseModel {
        public int width, height, densityDpi;
        public float density;
        public int bQmp;//是否全面屏

        public ScreenParam(int width, int height, int densityDpi, float density, int bQmp) {
            this.width = width;
            this.height = height;
            this.densityDpi = densityDpi;
            this.density = density;
            this.bQmp = bQmp;
        }
    }

}
