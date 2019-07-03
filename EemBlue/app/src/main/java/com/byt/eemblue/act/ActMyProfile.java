package com.byt.eemblue.act;

import android.content.Intent;
import android.widget.TextView;

import com.byt.eemblue.EApp;
import com.byt.eemblue.R;
import com.byt.eemblue.base.BaseAct;

import butterknife.BindView;
import butterknife.ButterKnife;

//我的资料
public class ActMyProfile extends BaseAct {
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_userId)
    TextView tvUserId;
    @BindView(R.id.tv_companyName)
    TextView tvCompanyName;

    @Override
    protected int setupViewRes() {
        return R.layout.act_my_profile;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        tvUserName.setText(EApp.getUserInfo().getUserName());
        tvUserId.setText(EApp.getUserInfo().getUserId());
        tvCompanyName.setText(EApp.getUserInfo().getCompanyName());
        tvLogout.setOnClickListener(view -> {
            Intent it = new Intent(_this, ActLogin.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();
        });

    }

}
