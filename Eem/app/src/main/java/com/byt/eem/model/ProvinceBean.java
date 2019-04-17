package com.byt.eem.model;

import com.souja.lib.models.BaseModel;

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/13 2:37 PM
 * Description :
 */
public class ProvinceBean extends BaseModel {

    /**
     * Id : 1
     * ProvinceCode : sc
     * ProvinceName : 四川
     */

    private int Id;
    private String ProvinceCode;
    private String ProvinceName;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getProvinceCode() {
        return ProvinceCode;
    }

    public void setProvinceCode(String ProvinceCode) {
        this.ProvinceCode = ProvinceCode;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }
}
