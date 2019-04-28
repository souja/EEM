package com.byt.eem.act;

import android.content.Intent;
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
import com.souja.lib.utils.MTool;
import com.souja.lib.utils.SPHelper;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

public class ActSplash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MTool.setStatusBarFullTransparent(getWindow());
        MTool.setStatusBarTextColor(getWindow(), true);

        if (!MGlobal.get().isInitializedScreenParams()) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            MGlobal.get().initScreenParam(displayMetrics);
        }


        if (x.isDebug()) {
            String userInfoStr = SPHelper.getString(MConstants.USERINFO_KEY);
            LogUtil.e(userInfoStr);
            checkUserInfo(userInfoStr);
        } else
            goLogin();
    }

    private void checkUserInfo(String userInfoStr) {
        if (userInfoStr.isEmpty()) {
            LogUtil.e("没有登录过");
            goLogin();
        } else {
            try {
                UserInfo userInfo = (UserInfo) GsonUtil.getObj(userInfoStr, UserInfo.class);
                EApp.setUserInfo(userInfo);
                LogUtil.e("获取上次登录用户信息成功");
//                EApp.initPrivateDB();
                goMain();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("本地用户数据解析失败");
                goLogin();
            }
        }
    }

    private void goLogin() {
        go(ActLogin.class);
    }

    private void goMain() {
        go(ActMain.class);
    }

    private void go(Class c) {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(ActSplash.this, c));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 1500);
    }
}
