package com.byt.eem.model;

import com.souja.lib.models.BaseModel;

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/14 12:46 PM
 * Description :
 */
public class ProjectInfoBean extends BaseModel {

    /**
     * Address : 广安神仙树1号
     * Latitude : 118.118
     * Longitude : 30.30
     * PTime : 2019-01-03 15:48
     * TCityId : 1
     * TContactId : 0
     * TCountyId : 1
     * TProjectName : 皇爵ktv
     * TProvinceId : 1
     * TUserId : 12
     * ContactName : 二大爷
     * Phone : 110
     */

    private String Address;
    private String Latitude;
    private String Longitude;
    private String PTime;
    private int TCityId;
    private int TContactId;
    private int TCountyId;
    private String TProjectName;
    private int TProvinceId;
    private int TUserId;
    private String ContactName;
    private String Phone;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getPTime() {
        return PTime;
    }

    public void setPTime(String PTime) {
        this.PTime = PTime;
    }

    public int getTCityId() {
        return TCityId;
    }

    public void setTCityId(int TCityId) {
        this.TCityId = TCityId;
    }

    public int getTContactId() {
        return TContactId;
    }

    public void setTContactId(int TContactId) {
        this.TContactId = TContactId;
    }

    public int getTCountyId() {
        return TCountyId;
    }

    public void setTCountyId(int TCountyId) {
        this.TCountyId = TCountyId;
    }

    public String getTProjectName() {
        return TProjectName;
    }

    public void setTProjectName(String TProjectName) {
        this.TProjectName = TProjectName;
    }

    public int getTProvinceId() {
        return TProvinceId;
    }

    public void setTProvinceId(int TProvinceId) {
        this.TProvinceId = TProvinceId;
    }

    public int getTUserId() {
        return TUserId;
    }

    public void setTUserId(int TUserId) {
        this.TUserId = TUserId;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String ContactName) {
        this.ContactName = ContactName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }
}
