package com.byt.eem.frag;

import android.content.Intent;
import android.view.LayoutInflater;

import com.byt.eem.R;
import com.byt.eem.act.ActCreateDevice;
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
//            startActivityForResult(new Intent(mBaseActivity, CaptureActivity.class), 12345);
        });
//        我的项目
        _contentView.findViewById(R.id.myProjects).setOnClickListener(view -> ActMyProjects.Companion.launch(mBaseActivity));
        //添加设备
        _contentView.findViewById(R.id.addDevice).setOnClickListener(view -> GO(ActCreateDevice.class));
        //我的资料
//        _contentView.findViewById(R.id.scan).setOnClickListener(view ->);
        //关于系统
//        _contentView.findViewById(R.id.scan).setOnClickListener(view -> );
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == mBaseActivity.RESULT_OK
//                && requestCode == 12345
//                && data != null) {
//
//            String result = data.getStringExtra("CODE");
//
//            LogUtil.e("Scan Result:" + result);
//
//        }
//    }
}
