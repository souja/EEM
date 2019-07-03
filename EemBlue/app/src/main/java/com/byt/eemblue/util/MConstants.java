package com.byt.eemblue.util;

/**
 * Created by Souja on 2018/6/27 0027.
 */

public class MConstants {
//        public static String HTTP = "https://wxzz.chftne.cn/api";//test
//    public static String HTTP = "https://xcwxapp.chftne.cn/api";//pro
//    public static String HTTP = "https://wxapp.xc-fire.cn/api";//pro
    public static String HTTP = "https://wxapp.xc-fire.cn/api";//pro
//        public static String HTTP = "https://wx.zzese.cn/api";//pro
    //用户信息缓存KEY
    public static final String USERINFO_KEY = "eemUserInfo";

    public static final String DOWNLOAD_KEY = "eemNewVersion";

    public static final int RX_SCAN_QR_CODE = 100;//扫描二维码
    public static final int RX_PROCESS_ALARM_MSG = 101;//处理告警消息
    public static final int RX_UPDATE_DEVICE_INFO = 102;//修改"设置"里面的参数后，更新数据

    //Rui Constants↓


    public interface URL {
        //检查更新
        String CHECK_UPDATE = "/Operate/CheckUpdate";
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
        String GET_DEVICES_STATE_BY_DEVICEID = "/Home/GetDeviceStateByDeviceIdEx?deviceId=";
        //获取当前设备的历史记录 Id beginTime endTime
        String GET_DEVICES_HISTORY_STATE = "/Home/GetDeviceHisState";
        //获取当前用户的所有项目包含其子用户的项目，用于设备新增的时候的项目选择
        String GET_MY_AND_MYCHILD_PROJECTS = "/Operate/GetMyAndChildProject?UID=";
        //获取所有设备类型
        String GET_ALL_DEVICE_TYPE = "/Operate/GetAllDeviceType";
        //保存设备信息
        String GET_SAVE_DEVICE_INFO = "/ProjectAndDevice/SaveDeviceInfo";
        //获取告警消息列表
        String GET_ALARM_MSG_LIST = "/Message/GetAlarmDetails";
        //获取告警消息列表分页信息
        String GET_ALARM_MSG_LIST_PAGEINFO = "/Message/GetAlarmDetailsPageInfo";
        //处理消息
        String PROCESS_ALARM_MSG = "/Message/ProcessAlarm";
        //设备操作
        String HANDLE_DEVICE = "/DeviceCtrlControl/%s";
        //获取设备参数
        String GET_DEVICE_PARAM = "/DeviceCtrlControl/GetParamFromDb?deviceCode=%s";
        //设置设备参数
        String SET_DEVICE_PARAM = "/DeviceCtrlControl/SetParamToDb";


        //Rui Url↓
        String GET_MY_PROJECTS = "/Operate/GetMyProject?userId=";
        String DELETE_PROJECT = "/Operate/DeleteProject?projectId=";
        //获取所有省份
        String GET_PROVINCES = "/ProjectAndDevice/GetProvinces";
        //根据省份ID获取城市
        String GET_CITIES_BY_ID = "/ProjectAndDevice/GetTcitys?provinceId=";
        //根据城市ID获取地区
        String GET_COUNTIES_BY_ID = "/ProjectAndDevice/GetTcountys?cityId=";
        //获取所有业主
        String GET_ALL_USER = "/ProjectAndDevice/GetAllUser";
        //保存项目
        String SAVE_PROJECT = "/ProjectAndDevice/SaveProject";
        //更新项目
        String UPDATE_PROJECT = "/ProjectAndDevice/UpdateProject";

    }

}
