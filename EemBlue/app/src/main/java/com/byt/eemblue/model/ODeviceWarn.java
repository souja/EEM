package com.byt.eemblue.model;

import com.souja.lib.models.BaseModel;

public class ODeviceWarn extends BaseModel {

    /**
     * Id : 21
     * AlarmId : 1874
     * HappenTime : 2019-03-26T14:18:44
     * Address : 岳池设备-001
     * DeviceName : 岳池县工业园区
     * WarnType : 设备掉线报警
     * IsProcess : 0
     * TProjectName : 岳池创新创业园
     * Contacts : null
     * ContactsPhone : null
     */

    private int Id;
    private int AlarmId;
    private String HappenTime;
    private String Address;
    private String DeviceName;
    private String WarnType;
    private int IsProcess;
    private String TProjectName;
    private Object Contacts;
    private Object ContactsPhone;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getAlarmId() {
        return AlarmId;
    }

    public void setAlarmId(int AlarmId) {
        this.AlarmId = AlarmId;
    }

    public String getHappenTime() {
        return HappenTime;
    }

    public void setHappenTime(String HappenTime) {
        this.HappenTime = HappenTime;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String DeviceName) {
        this.DeviceName = DeviceName;
    }

    public String getWarnType() {
        return WarnType;
    }

    public void setWarnType(String WarnType) {
        this.WarnType = WarnType;
    }

    public int getIsProcess() {
        return IsProcess;
    }

    public void setIsProcess(int IsProcess) {
        this.IsProcess = IsProcess;
    }

    public String getTProjectName() {
        return TProjectName;
    }

    public void setTProjectName(String TProjectName) {
        this.TProjectName = TProjectName;
    }

    public Object getContacts() {
        return Contacts;
    }

    public void setContacts(Object Contacts) {
        this.Contacts = Contacts;
    }

    public Object getContactsPhone() {
        return ContactsPhone;
    }

    public void setContactsPhone(Object ContactsPhone) {
        this.ContactsPhone = ContactsPhone;
    }
}
