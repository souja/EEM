package com.byt.eemblue.model;

import com.souja.lib.models.BaseModel;

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/14 12:32 PM
 * Description :
 */
public class CountyBean extends BaseModel {


    /**
     * Id : 1
     * CountyCode : GXQ
     * CountyName : 高新区
     * TCityId : 1
     * TProvinceId : 1
     */

    private int Id;
    private String CountyCode;
    private String CountyName;
    private int TCityId;
    private int TProvinceId;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCountyCode() {
        return CountyCode;
    }

    public void setCountyCode(String CountyCode) {
        this.CountyCode = CountyCode;
    }

    public String getCountyName() {
        return CountyName;
    }

    public void setCountyName(String CountyName) {
        this.CountyName = CountyName;
    }

    public int getTCityId() {
        return TCityId;
    }

    public void setTCityId(int TCityId) {
        this.TCityId = TCityId;
    }

    public int getTProvinceId() {
        return TProvinceId;
    }

    public void setTProvinceId(int TProvinceId) {
        this.TProvinceId = TProvinceId;
    }
}
