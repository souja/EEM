package com.byt.eem.act;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.byt.eem.EApp;
import com.byt.eem.model.UserInfo;
import com.byt.eem.util.MConstants;
import com.souja.lib.utils.GsonUtil;
import com.souja.lib.utils.MGlobal;
import com.souja.lib.utils.SPHelper;

import org.xutils.common.util.LogUtil;

public class ActSplash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!MGlobal.get().isInitializedScreenParams()) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            MGlobal.get().initScreenParam(displayMetrics);
        }
        String model = Build.MODEL;
        String carrier = Build.MANUFACTURER;
        LogUtil.e("手机型号/厂商：" + model + "," + carrier);
//        SPHelper.putString("deviceInfo", model + " " + carrier);

//        int statusBarHeight = EApp.getStatusBarHeight(this);
//        EApp.setStatusBarHeight(statusBarHeight);

        String userInfoStr = SPHelper.getString(MConstants.USERINFO_KEY);
        if (!userInfoStr.isEmpty() && (!userInfoStr.contains("iMUserSig") || !userInfoStr.contains("userCode")))
            SPHelper.putString(MConstants.USERINFO_KEY, "");

        checkUserInfo();
    }

    private void checkUserInfo() {
        String userInfoStr = SPHelper.getString(MConstants.USERINFO_KEY);
        if (userInfoStr.isEmpty()) {
            LogUtil.e("没有登录过");
            goLogin();
        } else {
            try {
                UserInfo userInfo = (UserInfo) GsonUtil.getObj(userInfoStr, UserInfo.class);
                EApp.setUserInfo(userInfo);
                LogUtil.e("获取上次登录用户信息成功");
//                EApp.initPrivateDB();

                new Handler().postDelayed(() -> {
                    startActivity(new Intent(ActSplash.this, ActMain.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }, 1500);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("本地用户数据解析失败");
                goLogin();
            }
        }
    }

    private void goLogin() {
        startActivity(new Intent(ActSplash.this, ActLogin.class));
        finish();
    }
}
