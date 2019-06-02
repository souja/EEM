package com.byt.eem.model;

import com.souja.lib.models.BaseModel;

public class OHomeProj extends BaseModel {


    /**
     * ProvinceId : 1
     * ProvinceName : 四川
     * ProjectCount : 11
     */

    private int ProvinceId;
    private String ProvinceName;
    private int ProjectCount;
    private int ProperlyCount;
    private int OffLineCount;
    private int WarnCount;

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

    public int getProjectCount() {
        return ProjectCount;
    }

    public void setProjectCount(int Counts) {
        this.ProjectCount = Counts;
    }

    public int getProperlyCount() {
        return ProperlyCount;
    }

    public void setProperlyCount(int properlyCount) {
        ProperlyCount = properlyCount;
    }

    public int getOffLineCount() {
        return OffLineCount;
    }

    public void setOffLineCount(int offLineCount) {
        OffLineCount = offLineCount;
    }

    public int getWarnCount() {
        return WarnCount;
    }

    public void setWarnCount(int warnCount) {
        WarnCount = warnCount;
    }
}
