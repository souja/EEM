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
        //获取设备状态汇总信息(如故障75台,离线50台,正常309台,其他50台)
        String GET_DEVICE_STATE_COUNT = "/Home/GetDeviceStateCount";
        //获取实时告警信息
        String GET_DEVICE_WARN_BY_REAL_TIME = "/Home/GetDeviceWarnByRealTime";
        //获取各省项目汇总(如每个省有多少个项目)
        String GET_PROJECTS_GROUP_BY_PROVINCE = "/Home/GetProjectsGroupByProvince";
    }

}
