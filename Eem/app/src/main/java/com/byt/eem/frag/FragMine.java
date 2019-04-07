package com.byt.eem.frag;

import android.content.Intent;
import android.view.LayoutInflater;

import com.byt.eem.R;
import com.byt.eem.act.ActAboutUs;
import com.byt.eem.act.ActCreateDevice;
import com.byt.eem.act.ActMyProfile;
import com.byt.eem.act.ActMyProjects;
import com.byt.eem.act.ActScanQRCode;
import com.byt.eem.base.MBaseLazyFragmentB;
import com.byt.eem.util.MConstants;

import org.xutils.common.util.LogUtil;

import io.reactivex.functions.Consumer;

public class FragMine extends MBaseLazyFragmentB {

    @Override
    public void onFirstUserVisible() {
        _contentView = LayoutInflater.from(mBaseActivity).inflate(R.layout.frag_mine, null, false);
        setContentView(_contentView);

        //扫一扫
        _contentView.findViewById(R.id.scan).setOnClickListener(view -> {
            Consumer onScanBack = (Consumer<ActScanQRCode.RxScanResult>) rxResult -> {
                String result = rxResult.result;
                LogUtil.e("扫描结果：" + result);
                NEXT(new Intent(mBaseActivity, ActCreateDevice.class).putExtra("code", result));
                rxResult.act.finish();
                delAction(MConstants.RX_SCAN_QR_CODE);
            };
            addAction(MConstants.RX_SCAN_QR_CODE, onScanBack);
            GO(ActScanQRCode.class);
        });
        //我的项目
        _contentView.findViewById(R.id.myProjects).setOnClickListener(view -> ActMyProjects.Companion.launch(mBaseActivity));
        //添加设备
        _contentView.findViewById(R.id.addDevice).setOnClickListener(view -> GO(ActCreateDevice.class));
        //我的资料
        _contentView.findViewById(R.id.myProfile).setOnClickListener(view ->GO(ActMyProfile.class));
        //关于系统
        _contentView.findViewById(R.id.aboutSys).setOnClickListener(view -> GO(ActAboutUs.class));
    }

}
