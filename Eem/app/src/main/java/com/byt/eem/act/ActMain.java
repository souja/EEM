package com.byt.eem.act;


import com.byt.eem.R;
import com.byt.eem.base.BaseAct;

public class ActMain extends BaseAct {

    private long exitTime;

    @Override
    protected int setupViewRes() {
        return R.layout.act_main;
    }

    @Override
    protected void initMain() {

    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
