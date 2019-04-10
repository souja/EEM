package com.byt.eem.util;

/**
 * Created by Souja on 2018/6/27 0027.
 */

public class MConstants {
    public static String HTTP = "https://testpay.chftne.cn/api";//pro

    //用户信息缓存KEY
    public static final String USERINFO_KEY = "eemUserInfo";
    //应用下载KEY
    public static final String DOWNLOAD_KEY = "eem-new";

    public static String VERSION = "";//接口版本,v1,v2...

    public static final int RX_SCAN_QR_CODE = 100;//扫描二维码

    //Rui Constants↓


    public interface URL {
        //登录
        String LOGIN = "/SUser/Login";
        //获取设备状态汇总信息(如故障75台,离线50台,正常309台,其他50台)
        String GET_DEVICE_STATE_COUNT = "/Home/GetDeviceStateCount";
        //获取实时告警信息
        String GET_DEVICE_WARN_BY_REAL_TIME = "/Home/GetDeviceWarnByRealTime";
        //获取各省项目汇总(如每个省有多少个项目)
        String GET_PROJECTS_GROUP_BY_PROVINCE = "/Home/GetProjectsGroupByProvinceEx";
        //获取各类型消息的汇总(只获取告警消息)
        String GET_ALARM_STATISTICS = "/Message/GetAlarmStatistics";
        //按周查询各项目的汇总信息
        String GET_PROJECT_STATISTICS = "/Weekly/GetProjectStatistics";
        //按周查询各项目的汇总信息
        String GET_PROJECTS_BY_PROVINCE = "/Home/GetProjectsByProvince?provinceId=";
        //根据项目查询设备
        String GET_DEVICES_BY_PROJECT = "/Home/GetDevicesByProject?projectId=";
        //获取设备当前状态
        String GET_DEVICES_STATE_BY_DEVICEID = "/Home/GetDeviceStateByDeviceId?deviceId=";
        //获取当前设备的历史记录 Id beginTime endTime
        String GET_DEVICES_HISTORY_STATE = "/Home/GetDeviceHisState";
        //获取当前用户的所有项目包含其子用户的项目，用于设备新增的时候的项目选择
        String GET_MY_AND_MYCHILD_PROJECTS = "/Operate/GetMyAndChildProject?UID=";
        //获取所有设备类型
        String GET_ALL_DEVICE_TYPE = "/Operate/GetAllDeviceType";
        //保存设备信息
        String GET_SAVE_DEVICE_INFO = "/ProjectAndDevice/SaveDeviceInfo";
        //保存设备信息
//        String GET_ALARM_STATISTICS = "/Message/GetAlarmStatistics";


        //Rui Url↓
        String GET_MY_PROJECTS = "/Operate/GetMyProject";

    }

}
