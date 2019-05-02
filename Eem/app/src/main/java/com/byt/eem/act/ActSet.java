package com.byt.eem.act;

import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.util.MConstants;
import com.souja.lib.utils.MTool;

//设置
public class ActSet extends BaseAct {
    @Override
    protected int setupViewRes() {
        return R.layout.act_set;
    }

    private String deviceCode;
    @Override
    protected void initMain() {

       deviceCode =getIntent().getStringExtra("code");

       getDeviceParam();
    }

    private void getDeviceParam(){
//        Post(getDialog(),MTool.formatStr(MConstants.URL.HANDLE_DEVICE,"GetParamFromDb"));
    }
}
