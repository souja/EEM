package com.byt.eemblue.model;

import com.souja.lib.models.BaseModel;

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/8 11:31 PM
 * Description :
 */
public class MyProjectBean extends BaseModel {


    /**
     * Address : 312123
     * PTime : 2019-03-25T06:57:25
     * TCityId : 1
     * TCountyId : 1
     * TProvinceId : 1
     * CityName : 成都市
     * CountyName : 高新区
     * ProvinceName : 四川
     * TUserId : 12
     * TProjectName : 社保ktv
     * Id : 49
     * Longitude : 116.345196
     * Latitude : 39.923568
     * TContactId : null
     * ContactName : null
     * Phone : null
     * UserName : 体验用户
     * Counts : 1
     */

    private String Address;
    private String PTime;
    private int TCityId;
    private int TCountyId;
    private int TProvinceId;
    private String CityName;
    private String CountyName;
    private String ProvinceName;
    private int TUserId;
    private String TProjectName;
    private int Id;
    private String Longitude;
    private String Latitude;
    private String TContactId;
    private String ContactName;
    private String Phone;
    private String UserName;
    private int Counts;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
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

    public int getTCountyId() {
        return TCountyId;
    }

    public void setTCountyId(int TCountyId) {
        this.TCountyId = TCountyId;
    }

    public int getTProvinceId() {
        return TProvinceId;
    }

    public void setTProvinceId(int TProvinceId) {
        this.TProvinceId = TProvinceId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getCountyName() {
        return CountyName;
    }

    public void setCountyName(String CountyName) {
        this.CountyName = CountyName;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public int getTUserId() {
        return TUserId;
    }

    public void setTUserId(int TUserId) {
        this.TUserId = TUserId;
    }

    public String getTProjectName() {
        return TProjectName;
    }

    public void setTProjectName(String TProjectName) {
        this.TProjectName = TProjectName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getTContactId() {
        return TContactId;
    }

    public void setTContactId(String TContactId) {
        this.TContactId = TContactId;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public int getCounts() {
        return Counts;
    }

    public void setCounts(int Counts) {
        this.Counts = Counts;
    }

}
