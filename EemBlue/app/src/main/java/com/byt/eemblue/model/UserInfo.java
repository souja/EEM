package com.byt.eemblue.model;

import com.souja.lib.models.BaseModel;

public class UserInfo extends BaseModel {

    /**
     * {"IsSucess":true,"Msg":null,"DataJsonStr":"[{\"Id\":12,\"ParentId\":0,\"PassWord\":\"1\",
     * \"UserId\":\"user\",\"UserName\":\"体验用户\",\"TRoleId\":1,\"Phone\":null,\"PlatFormName\":
     * \"智慧消防平台\",\"CompanyLogo\":\"/UpLoad/峰泰logo1副本.jpg\",\"CompanyName\":\"成都峰泰电气有限公司\",
     * \"RoleName\":\"超级管理员\"}]","OutId":0}
     * Id : 12
     * ParentId : 0
     * ParentTUserId : null
     * PassWord : 1
     * UserId : user
     * UserName : 体验用户
     * TRoleId : 1
     * Phone : null
     * PlatFormName : 智慧消防平台
     * CompanyLogo : /UpLoad/峰泰logo1副本.jpg
     * CompanyName : 成都峰泰电气有限公司
     * RoleName : 超级管理员
     */

    private int Id;
    private int ParentId;
    private String ParentTUserId;
    private String PassWord;
    private String UserId;
    private String UserName;
    private int TRoleId;
    private String Phone;
    private String PlatFormName;
    private String CompanyLogo;
    private String CompanyName;
    private String RoleName;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int ParentId) {
        this.ParentId = ParentId;
    }

    public String getParentTUserId() {
        return ParentTUserId;
    }

    public void setParentTUserId(String ParentTUserId) {
        this.ParentTUserId = ParentTUserId;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String PassWord) {
        this.PassWord = PassWord;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public int getTRoleId() {
        return TRoleId;
    }

    public void setTRoleId(int TRoleId) {
        this.TRoleId = TRoleId;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getPlatFormName() {
        return PlatFormName;
    }

    public void setPlatFormName(String PlatFormName) {
        this.PlatFormName = PlatFormName;
    }

    public String getCompanyLogo() {
        return CompanyLogo;
    }

    public void setCompanyLogo(String CompanyLogo) {
        this.CompanyLogo = CompanyLogo;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String RoleName) {
        this.RoleName = RoleName;
    }
}
