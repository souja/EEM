package com.byt.eem.model;

import com.souja.lib.models.BaseModel;

public class OHomeProj extends BaseModel {


    /**
     * ProvinceId : 1
     * ProvinceName : 四川
     * Counts : 11
     */

    private int ProvinceId;
    private String ProvinceName;
    private int Counts;

    public int getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(int ProvinceId) {
        this.ProvinceId = ProvinceId;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public int getCounts() {
        return Counts;
    }

    public void setCounts(int Counts) {
        this.Counts = Counts;
    }
}
