package com.byt.eemblue;

import com.byt.eemblue.model.UserInfo;
import com.souja.lib.models.BaseModel;
import com.souja.lib.utils.GsonUtil;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        String data = "[{\"Id\":12,\"ParentId\":0,\"ParentTUserId\":null,\"PassWord\":\"1\",\"UserId\":\"user\",\"UserName\":\"体验用户\",\"TRoleId\":1,\"Phone\":null,\"PlatFormName\":\"智慧消防平台\",\"CompanyLogo\":\"/UpLoad/峰泰logo1副本.jpg\",\"CompanyName\":\"成都峰泰电气有限公司\",\"RoleName\":\"超级管理员\"}]";
        data = data.replace("\\\"", "'");
        System.out.println(data);

        ArrayList<UserInfo> mUserInfo = GsonUtil.getArr(data, UserInfo.class);
        System.out.println(mUserInfo.size());
        System.out.println(mUserInfo.get(0).getUserName());
//        String login = "{\"UserId\":\"user\",\"PassWord\":\"1\"}";

//        Login mLogin= new Login("test","123");
//        String loginStr = mLogin.toString();
//        System.out.println(loginStr);
//
//        String loginStr2 = loginStr.replace("\"", "\\\"");
//        System.out.println(loginStr2);
//        System.out.println("\""+loginStr2+"\"");
    }

    static class Login extends BaseModel {
        String userId, pwd;

        public Login(String userId, String pwd) {
            this.userId = userId;
            this.pwd = pwd;
        }
    }
}
