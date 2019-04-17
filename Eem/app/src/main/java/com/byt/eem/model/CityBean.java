package com.byt.eem.model;

import com.souja.lib.models.BaseModel;

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/13 10:50 PM
 * Description :
 */
public class CityBean extends BaseModel {


    /**
     * Id : 1
     * CityCode : cd
     * CityName : 成都市
     * TProvinceId : 1
     */

    private int Id;
    private String CityCode;
    private String CityName;
    private int TProvinceId;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String CityCode) {
        this.CityCode = CityCode;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public int getTProvinceId() {
        return TProvinceId;
    }

    public void setTProvinceId(int TProvinceId) {
        this.TProvinceId = TProvinceId;
    }
}
