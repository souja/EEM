package com.byt.eem.model;

import com.souja.lib.models.BaseModel;

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/19 12:04 AM
 * Description : 人员
 */
public class UserControllerBean extends BaseModel {


    /**
     * Id : 5
     * UserName : 西南地区
     */

    private int Id;
    private String UserName;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
}
