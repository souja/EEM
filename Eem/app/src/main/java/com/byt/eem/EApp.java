package com.byt.eem;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.byt.eem.model.UserInfo;
import com.byt.eem.util.MConstants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.souja.lib.utils.GsonUtil;
import com.souja.lib.utils.SPHelper;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

public class EApp extends MultiDexApplication {

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.white, R.color.colorPrimary);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    private static EApp mContext;

    public static EApp getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        x.Ext.init(this);
        x.Ext.setDebug(true);
        LogUtil.customTagPrefix = "【EEM_APP】";

        SPHelper.init(mContext, getPackageName());
    }


    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        LogUtil.e("statusBarHeight=" + statusBarHeight);
        return statusBarHeight;
    }

    private static int statusBarHeight;

    public static int getStatusBarHeight() {
        return statusBarHeight;
    }

    public static void setStatusBarHeight(int height) {
        LogUtil.e("StatusBarHeight " + height);
        statusBarHeight = height;
    }

    private static UserInfo mUserInfo; //当前登录用户的信息

    public static UserInfo getUserInfo() {
        if (mUserInfo != null)
            return mUserInfo;
        else return initUserInfo();
    }

    private static UserInfo initUserInfo() {
        String userInfoStr = SPHelper.getString(MConstants.USERINFO_KEY);
        if (userInfoStr.isEmpty()) {
            return null;
        } else {
            try {
                UserInfo userInfo = (UserInfo) GsonUtil.getObj(userInfoStr, UserInfo.class);
                setUserInfo(userInfo);
                return userInfo;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("用户数据解析失败");
                return null;
            }
        }
    }

    public static void setUserInfo(UserInfo userInfo) {
        if (userInfo == null) SPHelper.remove(MConstants.USERINFO_KEY);
        else

            mUserInfo = userInfo;
    }

    //更新登录用户信息缓存
    public static void updateUserInfoCache() {
        SPHelper.putString(MConstants.USERINFO_KEY, GsonUtil.objToJsonString(mUserInfo));
    }

}
