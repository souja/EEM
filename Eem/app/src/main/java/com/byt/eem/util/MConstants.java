package com.byt.eem.util;

/**
 * Created by Souja on 2018/6/27 0027.
 */

public class MConstants {
    public static String  HTTP ="https://testpay.chftne.cn/api";//pro

    //用户信息缓存KEY
    public static final String USERINFO_KEY = "eemUserInfo";
    //应用下载KEY
    public static final String DOWNLOAD_KEY = "eem-new";

    public static String VERSION = "";//接口版本,v1,v2...

    public static final int RX_UP_XS_LIST = 400;//更新学术列表


    public interface URL {
        //登录
        String LOGIN = "/SUser/Login";
    }

}
